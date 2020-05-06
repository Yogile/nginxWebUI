package com.cym.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.cym.NginxWebUI;
import com.cym.model.Admin;

import cn.craccd.sqlHelper.utils.SqlHelper;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = NginxWebUI.class)
public class MainTest {
	@Autowired
	SqlHelper sqlHelper;
	@Before
	public void before() {
		System.out.println("--------------测试开始----------");
	}

	@Test
	public void testStartUp()  {
		Admin admin = new Admin();
		
		admin.setName("test");
		admin.setPass("admin");
		sqlHelper.insert(admin);
	}

	@After
	public void after() {
		System.out.println("--------------测试结束----------");
	}

}
