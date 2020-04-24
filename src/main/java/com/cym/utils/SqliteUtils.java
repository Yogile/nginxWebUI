package com.cym.utils;

import java.sql.SQLException;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import cn.hutool.db.Db;

@Component
public class SqliteUtils {
	
	@PostConstruct
	public void init() {
		
		
		try {
			Db.use().findAll("server");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) {
		new SqliteUtils().init();
	}
	
}
