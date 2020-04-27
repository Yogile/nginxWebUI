package com.cym.config;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.cym.model.Admin;

import cn.craccd.sqlHelper.utils.SqlHelper;

@Configuration
@ComponentScan(basePackages = { "cn.craccd" })
public class SqlConfig {
	@Autowired
	SqlHelper sqlHelper;

	@PostConstruct
	public void initAdmin() {
		Long count = sqlHelper.findAllCount(Admin.class);
		
		if(count == 0) {
			Admin admin = new Admin();
			admin.setName("admin");
			admin.setPass("admin");
			
			sqlHelper.insert(admin);
		}
	}
}
