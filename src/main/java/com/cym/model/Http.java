package com.cym.model;

import cn.craccd.sqlHelper.bean.BaseModel;
import cn.craccd.sqlHelper.config.Table;

@Table
public class Http extends BaseModel{ 
	String gzip;
	Integer clientMaxBodySize;

	public String getGzip() {
		return gzip;
	}

	public void setGzip(String gzip) {
		this.gzip = gzip;
	}

	public Integer getClientMaxBodySize() {
		return clientMaxBodySize;
	}

	public void setClientMaxBodySize(Integer clientMaxBodySize) {
		this.clientMaxBodySize = clientMaxBodySize;
	}



}
