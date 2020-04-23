package com.cym.model;

import org.springframework.data.mongodb.core.mapping.Document;

import cn.craccd.mongoHelper.bean.BaseModel;

@Document
public class Http extends BaseModel{
	String gzip;
	String clientMaxBodySize;

	public String getGzip() {
		return gzip;
	}

	public void setGzip(String gzip) {
		this.gzip = gzip;
	}

	public String getClientMaxBodySize() {
		return clientMaxBodySize;
	}

	public void setClientMaxBodySize(String clientMaxBodySize) {
		this.clientMaxBodySize = clientMaxBodySize;
	}

}
