package com.cym.config;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.cym.controller.adminPage.CertController;
import com.cym.controller.adminPage.ConfController;
import com.cym.model.Cert;
import com.cym.model.Log;
import com.cym.service.SettingService;
import com.cym.utils.SystemTool;

import cn.craccd.sqlHelper.utils.SqlHelper;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.RuntimeUtil;
import cn.hutool.core.util.StrUtil;

@Configuration // 1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling // 2.开启定时任务
public class ScheduleTask {

	@Autowired
	SqlHelper sqlHelper;
	@Autowired
	CertController certController;
	@Autowired
	SettingService settingService;
	@Autowired
	ConfController confController;

	@Scheduled(cron = "0 0 2 * * ?")
	public void mongodbTasks() {
		List<Cert> certList = sqlHelper.findAll(Cert.class);

		System.out.println("检查需要续签的证书");
		long time = System.currentTimeMillis();
		for (Cert cert : certList) {
			if (cert.getMakeTime() != null && cert.getAutoRenew() == 1 && time - cert.getMakeTime() > 59 * 24 * 60 * 60 * 1000) {
				System.out.println(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss") + " 开始续签证书:" + cert.getDomain());
				// 大于60天的续签
				certController.renew(cert.getId());
			}
		}
	}

	// 分隔日志,每分钟
	@Scheduled(cron = "0 * * * * ?")
	public void diviLog() {
		if (FileUtil.exist("/home/nginxWebUI/log/access.log")) {
			String date = DateUtil.format(new Date(), "yyyy-MM-dd_HH-mm-ss");

			FileUtil.move(new File("/home/nginxWebUI/log/access.log"), new File("/home/nginxWebUI/log/access." + date + ".log"), true);
			confController.reload(null, null, null);
		}
		
		// 删掉1小时前日志
//		Long time = System.currentTimeMillis();
//		File dir = new File("/home/nginxWebUI/log/");
//		File[] fileList = dir.listFiles();
//		for (File file : fileList) {
//			if (file.getName().contains("access") && !file.getName().equals("access.log")) {
//				DateTime date = DateUtil.parse(file.getName().replace("access.", "").replace(".log", ""), "yyyy-MM-dd_HH-mm-ss");
//				if(time - date.getTime() >  60 * 60 * 1000) {
//					FileUtil.del(file);
//				}
//			}
//		}

	}

}