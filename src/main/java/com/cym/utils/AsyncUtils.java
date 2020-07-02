package com.cym.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import cn.hutool.core.util.RuntimeUtil;

@Component
public class AsyncUtils {
	@Value("${server.port}")
	String port;
	@Value("${project.home}")
	String home;

	@Async
	public void run(String path) {
		try {
			String cmd = "nohup java -jar -Xmx64m " + path + " --server.port=" + port + " --project.home=" + home + " > /dev/null &";
			System.err.println(cmd);
			Process p = RuntimeUtil.exec(cmd);
			// 取得命令结果的输出流
			InputStream fis = p.getInputStream();
			// 用一个读输出流类去读
			InputStreamReader isr = new InputStreamReader(fis);
			// 用缓冲器读行
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			// 直到读完为止
			while ((line = br.readLine()) != null) {
				System.err.println(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
