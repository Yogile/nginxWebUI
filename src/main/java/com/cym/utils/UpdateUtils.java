package com.cym.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.stereotype.Component;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.RuntimeUtil;
import cn.hutool.http.HttpUtil;

/**
 * 升级程序
 */
@Component
public class UpdateUtils {
	@Autowired
	AsyncUtils asyncUtils;
	// 获取jar路径
	public File getBaseJarPath() {
		ApplicationHome home = new ApplicationHome(getClass());
		File jarFile = home.getSource();
		return jarFile;
	}

	public void startUpdate(String url) {
		System.err.println("-------------startUpdate---------------");
		File jar = getBaseJarPath();
		String[] names = url.split("/");
		String name = names[names.length - 1];
		String path = jar.getParent() + "/" + name;

		HttpUtil.downloadFile(url, path);

		asyncUtils.run(path);
	}
}
