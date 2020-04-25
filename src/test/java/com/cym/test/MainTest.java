package com.cym.test;

import java.sql.SQLException;
import java.util.HashSet;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.cym.NginxWebUI;
import com.cym.model.Server;

import cn.craccd.sqlite.utils.CriteriaAndWrapper;
import cn.craccd.sqlite.utils.NosqlHelper;
import cn.hutool.core.io.FileUtil;
import cn.hutool.db.Db;
import cn.hutool.db.Entity;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = NginxWebUI.class)
public class MainTest {
	@Autowired
	NosqlHelper nosqlHelper;

	@Before
	public void before() {
		System.out.println("--------------测试开始----------");
	}

	@Test
	public void testStartUp() throws SQLException  {
		
		CriteriaAndWrapper criteriaAndWrapper = new CriteriaAndWrapper();
		HashSet<String> set = new HashSet<String>();
		set.add("1");
		set.add("2");
		criteriaAndWrapper.in("ssl", set);
		
		
		nosqlHelper.findListByQuery(criteriaAndWrapper, Server.class);
	}

	@After
	public void after() {
		System.out.println("--------------测试结束----------");
	}

}
