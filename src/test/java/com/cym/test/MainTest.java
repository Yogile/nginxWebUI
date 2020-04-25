package com.cym.test;

import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.cym.NginxWebUI;
import com.cym.model.Server;

import cn.hutool.core.io.FileUtil;
import cn.hutool.db.Db;
import cn.hutool.db.Entity;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = NginxWebUI.class)
public class MainTest {


	@Before
	public void before() {
		System.out.println("--------------测试开始----------");
	}

	@Test
	public void testStartUp() throws SQLException  {
		Db.use().insert(Entity.parse(new Server()));
		System.out.println( FileUtil.getUserHomeDir());
	}

	@After
	public void after() {
		System.out.println("--------------测试结束----------");
	}

}
