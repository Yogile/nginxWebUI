package com.cym.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.cym.NginxWebUI;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = NginxWebUI.class)
public class MainTest {

	
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
