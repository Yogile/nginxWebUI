package com.cym.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.cym.NginxWebUI;
import com.cym.controller.adminPage.ConfController;
import com.cym.model.Admin;
import com.cym.utils.MessageUtils;

import cn.craccd.sqlHelper.utils.ConditionAndWrapper;
import cn.craccd.sqlHelper.utils.SqlHelper;
import cn.hutool.core.util.RuntimeUtil;

@SpringBootTest(classes = NginxWebUI.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MainTest {
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	SqlHelper sqlHelper;
	@Autowired
	MessageUtils m;
	@Autowired
	ConfController confController;

	@Test
	public void testStartUp() throws InterruptedException, IOException {

		Process ps = RuntimeUtil.exec("tasklist");
		//ps.waitFor();

		BufferedReader br = new BufferedReader(new InputStreamReader(ps.getInputStream()));
		String line;
		while ((line = br.readLine()) != null) {
			System.out.println(line);
		}
	}

}
