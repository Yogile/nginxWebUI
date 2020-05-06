package com.cym.model;

import cn.craccd.sqlHelper.bean.BaseModel;
import cn.craccd.sqlHelper.config.Table;

@Table
public class UpstreamServer extends BaseModel {
	String upstreamId;
	String server;
	Integer port;
	Integer weight;
	
	
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}
	public String getUpstreamId() {
		return upstreamId;
	}
	public void setUpstreamId(String upstreamId) {
		this.upstreamId = upstreamId;
	}
	public String getServer() {
		return server;
	}
	public void setServer(String server) {
		this.server = server;
	}
	public Integer getWeight() {
		return weight;
	}
	public void setWeight(Integer weight) {
		this.weight = weight;
	}
	
	
}
