package com.cym.model;

import cn.craccd.sqlHelper.bean.BaseModel;
import cn.craccd.sqlHelper.config.Table;

@Table
public class Upstream extends BaseModel {
	String name;
	String tactics; // 负载策略
	
	
	public String getTactics() {
		return tactics;
	}

	public void setTactics(String tactics) {
		this.tactics = tactics;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
