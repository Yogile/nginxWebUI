package com.cym.config;

import java.io.IOException;
import java.io.InputStream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.cym.service.SettingService;
import com.cym.utils.RuntimeTool;
import com.cym.utils.SystemTool;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.RuntimeUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.ZipUtil;

@Component
public class CertConfig {

	public String acmeSh;
	@Autowired
	SettingService settingService;

	@PostConstruct
	public void init() throws IOException {
		if (SystemTool.isLinux()) {
			// 初始化acme.sh
			String userDir = "/home/nginxWebUI/";

			ClassPathResource resource = new ClassPathResource("acme.zip");
			InputStream inputStream = resource.getInputStream();

			FileUtil.writeFromStream(inputStream, userDir + "acme.zip");
			FileUtil.mkdir(userDir + ".acme.sh");
			ZipUtil.unzip(userDir + "acme.zip", userDir + ".acme.sh");
			FileUtil.del(userDir + "acme.zip");

			acmeSh = userDir + ".acme.sh/acme.sh";
			RuntimeUtil.exec("chmod 777 " + acmeSh);

			// 找寻nginx配置文件
			String nginxPath = settingService.get("nginxPath");
			if (StrUtil.isEmpty(nginxPath)) {
				// 检查是否存在容器中的nginx.conf
				nginxPath = userDir + "nginx.conf";
				if (FileUtil.exist(nginxPath)) {
					settingService.set("nginxPath", nginxPath);
				} else {
					// 都没有才查找nginx.conf
					nginxPath = RuntimeTool.execForOne("find / -name nginx.conf");

					if (StrUtil.isNotEmpty(nginxPath) && FileUtil.exist(nginxPath)) {
						settingService.set("nginxPath", nginxPath);
					}
				}
			}

			String nginxExe = settingService.get("nginxExe");
			if (StrUtil.isEmpty(nginxExe)) {
				String rs = RuntimeTool.execForOne("which nginx");
				if (StrUtil.isNotEmpty(rs)) {
					settingService.set("nginxExe", "nginx");

					// 尝试启动nginx
					String[] command = { "/bin/sh", "-c", "ps -ef|grep nginx" };
					rs = RuntimeUtil.execForStr(command);
					if (!rs.contains("nginx: master process")) {
						RuntimeUtil.exec("nginx");
					}
				}
			}
		}
	}
}
