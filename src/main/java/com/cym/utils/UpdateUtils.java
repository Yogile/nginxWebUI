package com.cym.utils;

import java.io.File;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.stereotype.Component;

import com.cym.model.Version;

import cn.hutool.core.util.RuntimeUtil;
import cn.hutool.http.HttpUtil;

/**
 * 升级程序
 */
@Component
public class UpdateUtils {
	@Value("${server.port}")
	String port;

	// 获取jar路径
	public File getBaseJarPath() {
		ApplicationHome home = new ApplicationHome(getClass());
		File jarFile = home.getSource();
		return jarFile;
	}

	public void startUpdate(Version version) {

		File jar = getBaseJarPath();
		String[] names = version.getUrl().split("/");
		String name = names[names.length - 1];
		String path = jar.getParent() + "/" + name;

		HttpUtil.downloadFile(version.getUrl(), path);

		RuntimeUtil.exec("nohup java -jar -Xmx64m " + path + " --server.port=" + port + " > /del/null &");

	}
}
