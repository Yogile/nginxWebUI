package com.cym.model;

import org.springframework.data.mongodb.core.mapping.Document;

import cn.craccd.mongoHelper.bean.BaseModel;

@Document
public class Admin extends BaseModel {
	String name;
	String pass;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

}
