package com.cym.model;

import cn.craccd.sqlHelper.bean.BaseModel;
import cn.craccd.sqlHelper.config.SingleIndex;
import cn.craccd.sqlHelper.config.Table;

@Table
public class DateResult extends BaseModel {
	@SingleIndex
	String name;

	String json;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

}
