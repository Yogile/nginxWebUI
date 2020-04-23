package com.cym.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.cym.nginxWebUI;

import cn.craccd.mongoHelper.utils.ImportExportUtil;
import cn.craccd.mongoHelper.utils.MongoHelper;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = nginxWebUI.class)
public class MainTest {

	@Autowired
	MongoHelper mongoHelper;
	@Autowired
	ImportExportUtil importExportUtil;

	@Before
	public void before() {
		System.out.println("--------------测试开始----------");
	}

	@Test
	public void testStartUp()  {
	
	}

	@After
	public void after() {
		System.out.println("--------------测试结束----------");
	}

}
