package com.cym.model;

public class Http{
	String id;
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}


}
