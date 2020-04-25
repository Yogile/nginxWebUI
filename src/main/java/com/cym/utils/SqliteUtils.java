package com.cym.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import cn.hutool.core.io.FileUtil;
import cn.hutool.db.Db;
import cn.hutool.db.ds.simple.SimpleDataSource;

@Component
public class SqliteUtils {
	String dbpath;

	DataSource ds;

	@PostConstruct
	public void init() throws IOException {
		ClassPathResource resource = new ClassPathResource("nginx.db");
		InputStream inputStream = resource.getInputStream();
		dbpath = FileUtil.getUserHomePath() + File.separator + ".nginx.db";
		if(!FileUtil.exist(dbpath)) {
			FileUtil.writeFromStream(inputStream, dbpath);
			System.out.println("释放:" + dbpath);
		}
		
		ds = new SimpleDataSource("jdbc:sqlite:" + dbpath,"","");

	}

	public Db use() {
		return Db.use(ds);
	}
}
