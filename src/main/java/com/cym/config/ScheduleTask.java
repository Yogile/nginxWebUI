package com.cym.config;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.cym.controller.adminPage.CertController;
import com.cym.controller.adminPage.ConfController;
import com.cym.controller.adminPage.RemoteController;
import com.cym.model.Cert;
import com.cym.model.Remote;
import com.cym.service.LogService;
import com.cym.service.SettingService;
import com.cym.utils.SendCloudUtils;

import cn.craccd.sqlHelper.utils.SqlHelper;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.ZipUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;

@Configuration // 1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling // 2.开启定时任务
public class ScheduleTask {

	final SqlHelper sqlHelper;
	final CertController certController;
	final SettingService settingService;
	final ConfController confController;
	final RemoteController remoteController;
	final LogService logInfoService;
	final SendCloudUtils smCloudUtils;

	public ScheduleTask(SendCloudUtils smCloudUtils, RemoteController remoteController, SqlHelper sqlHelper, CertController certController, SettingService settingService,
			ConfController confController, LogService logInfoService) {
		this.sqlHelper = sqlHelper;
		this.certController = certController;
		this.settingService = settingService;
		this.confController = confController;
		this.logInfoService = logInfoService;
		this.remoteController = remoteController;
		this.smCloudUtils = smCloudUtils;
	}

	@PostConstruct
	public void runTest() {

	}

	// 使用TimeUnit.DAYS.toMillis()进行时间粒度转换。Modified by Sai on 2020-6-17.
	@Scheduled(cron = "0 0 2 * * ?")
	public void certTasks() {
		List<Cert> certList = sqlHelper.findAll(Cert.class);

		System.out.println("检查需要续签的证书");
		long time = System.currentTimeMillis();
		for (Cert cert : certList) {
			if (cert.getMakeTime() != null && cert.getAutoRenew() == 1 && time - cert.getMakeTime() > TimeUnit.DAYS.toMillis(59)) {
				System.out.println(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss") + " 开始续签证书:" + cert.getDomain());
				// 大于60天的续签
				certController.apply(cert.getId(), "renew");
			}
		}
	}

	// 分隔日志,每天
	@Scheduled(cron = "0 55 23 * * ?")
	public void diviLog() {
		if (FileUtil.exist(InitConfig.home + "log/access.log")) {

			String date = DateUtil.format(new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1)), "yyyy-MM-dd_HH-mm-ss");
			// 分隔日志
			File dist = new File(InitConfig.home + "log/access." + date + ".log");
			FileUtil.move(new File(InitConfig.home + "log/access.log"), dist, true);
			ZipUtil.zip(dist); // 打包
			FileUtil.del(dist); // 删除原文件
			// 重启Nginx。
			confController.reload(null, null, null);
			// 马上解析分隔出来的日志
			logInfoService.buildDataGroup(InitConfig.home + "log/access." + date + ".zip");
		}

		// 删掉7天前日志文件(zip)
		long time = System.currentTimeMillis();
		File dir = new File(InitConfig.home + "log/");
		Optional.ofNullable(dir.listFiles()).ifPresent(fileList -> Arrays.stream(fileList).filter(file -> file.getName().contains("access") && file.getName().endsWith(".zip")).forEach(file -> {
			DateTime date = DateUtil.parse(file.getName().replace("access.", "").replace(".zip", ""), "yyyy-MM-dd_HH-mm-ss");
			if (time - date.getTime() > TimeUnit.DAYS.toMillis(8)) {
				FileUtil.del(file);
			}
		})

		);
	}

	// 检查nginx运行
	@Scheduled(cron = "0 * * * * ?")
	public void nginxTasks() {
		System.err.println("检查nginx运行");

		String lastSend = settingService.get("lastSend");
		String mail = settingService.get("mail");
		String nginxMonitor = settingService.get("nginxMonitor");
		if ("true".equals(nginxMonitor) && StrUtil.isNotEmpty(mail) && (StrUtil.isEmpty(lastSend) || System.currentTimeMillis() - Long.parseLong(lastSend) > TimeUnit.HOURS.toMillis(1))) {

			List<Remote> remoteList = sqlHelper.findAll(Remote.class);

			List<String> names = new ArrayList<>();
			for (Remote remote : remoteList) {
				try {
					String json = HttpUtil.get(remote.getProtocol() + "://" + remote.getIp() + ":" + remote.getPort() + "/adminPage/remote/version?creditKey=" + remote.getCreditKey(), 1000);
					Map<String, Object> map = JSONUtil.toBean(json, new TypeReference<Map<String, Object>>() {
					}.getType(), false);

					if ((Integer) map.get("nginx") == 0) {
						names.add(remote.getDescr() + "(" + remote.getIp() + ":" + remote.getPort() + ")");
					}
				} catch (Exception e) {
					e.printStackTrace();
					names.add(remote.getDescr() + "(" + remote.getIp() + ":" + remote.getPort() + ")");
				}
			}

			Map<String, Object> map = remoteController.version();
			if ((Integer) map.get("nginx") == 0) {
				names.add(0, "本地(127.0.0.1:8080)");
			}

			if (names.size() > 0) {
				smCloudUtils.sendMail(mail, StrUtil.join(" ", names));
				settingService.set("lastSend", String.valueOf(System.currentTimeMillis()));
			}
		}

	}

}