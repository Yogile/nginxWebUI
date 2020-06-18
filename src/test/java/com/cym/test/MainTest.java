package com.cym.test;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import com.cym.NginxWebUI;

import cn.craccd.sqlHelper.utils.SqlHelper;
import cn.hutool.core.util.RuntimeUtil;

@SpringBootTest(classes = NginxWebUI.class)
public class MainTest {
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	SqlHelper sqlHelper;
	@Value("${project.version}")
	String version;

	@BeforeAll
	static void before() {
		System.out.println("--------------测试开始----------");
	}

	@Test
	public void testStartUp() {
		String cmd = "cmd dir";
		try {
			Process process = RuntimeUtil.exec(cmd);
			BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line = null;
			while ((line = in.readLine()) != null) {
				System.out.println(line);
			}
			in.close();
			int re = process.waitFor();
			System.out.println(re);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@AfterAll
	static void after() {
		System.out.println("--------------测试结束----------");
	}

}
