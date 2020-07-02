package com.cym.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.RuntimeUtil;

@Component
public class AsyncUtils {
	@Value("${server.port}")
	String port;
	@Value("${project.home}")
	String home;

	@Async
	public void run(String path) {
		ThreadUtil.safeSleep(2000);
		
		String cmd = "nohup java -jar -Xmx64m " + path + " --server.port=" + port + " --project.home=" + home + " > /dev/null &";
		RuntimeUtil.exec(cmd);
	}
}
