package com.cym.test;

import java.util.ArrayList;
import java.util.List;

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
		List<Admin> list = new ArrayList<Admin>();
		for(int i=0;i<10;i++) {
			Admin admin = new Admin();
			admin.setName("admin");
			admin.setPass("admin");
			
			list.add(admin);
		}
		sqlHelper.insertAll(list);
		
	}

	@After
	public void after() {
		System.out.println("--------------测试结束----------");
	}

}
