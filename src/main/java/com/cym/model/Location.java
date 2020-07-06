package com.cym.model;

import cn.craccd.sqlHelper.bean.BaseModel;
import cn.craccd.sqlHelper.config.InitValue;
import cn.craccd.sqlHelper.config.Table;

@Table
public class Location extends BaseModel {
	String serverId;
	
	String path; // 监控路径
	Integer type; // 0 http 1 root 2 负载均衡
	
	String value; // http代理
	String upstreamId; // 负载均衡代理
	String upstreamPath;
	String locationParamJson;
	
	String rootPath; // 静态页面代理
	String rootPage;
	
	

	public String getRootPath() {
		return rootPath;
	}

	public void setRootPath(String rootPath) {
		this.rootPath = rootPath;
	}

	public String getRootPage() {
		return rootPage;
	}

	public void setRootPage(String rootPage) {
		this.rootPage = rootPage;
	}

	public String getUpstreamPath() {
		return upstreamPath;
	}

	public void setUpstreamPath(String upstreamPath) {
		this.upstreamPath = upstreamPath;
	}

	public String getLocationParamJson() {
		return locationParamJson;
	}

	public void setLocationParamJson(String locationParamJson) {
		this.locationParamJson = locationParamJson;
	}

	public String getUpstreamId() {
		return upstreamId;
	}

	public void setUpstreamId(String upstreamId) {
		this.upstreamId = upstreamId;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getServerId() {
		return serverId;
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}



}
