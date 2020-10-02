package com.cym.utils;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.RuntimeUtil;

@Component
public class AsyncUtils {
	@Value("${server.port}")
	String port;
	@Value("${project.home}")
	String home;
	private static final Logger LOG = LoggerFactory.getLogger(AsyncUtils.class);

	@Async
	public void run(String path) {
		ThreadUtil.safeSleep(2000);

		System.out.println(path);
		File file = new File(path);
		String[] list = file.getParentFile().list();
		for (String f : list) {
			System.out.println(f);
			if (f.startsWith("nginxWebUI") && f.endsWith(".jar")) {
				System.err.println("del " + f);
				FileUtil.del(f);
			}
		}

		String cmd = "mv " + path + " " + path.replace(".update", "");
		LOG.info(cmd);
		RuntimeUtil.exec(cmd);

		cmd = "nohup java -jar -Xmx64m " + path.replace(".update", "") + " --server.port=" + port + " --project.home=" + home + " > /dev/null &";
		LOG.info(cmd);
		RuntimeUtil.exec(cmd);

	}
	
	public static void main(String[] args) {
		File file = new File("E:\\home\\test.gz");
		String[] list = file.getParentFile().list();
		for (String f : list) {
			System.out.println(f);
			if (f.startsWith("nginxWebUI") && f.endsWith(".jar")) {
				System.err.println("del " + f);
				FileUtil.del(f);
			}
		}
	}
}
