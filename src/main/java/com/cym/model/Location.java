package com.cym.model;

import cn.craccd.sqlHelper.bean.BaseModel;
import cn.craccd.sqlHelper.config.InitValue;
import cn.craccd.sqlHelper.config.Table;

@Table
public class Location extends BaseModel {
	String serverId;
	
	String path;
	Integer type; // 0 http 1 root

	@InitValue("0")
	Integer proxyPassType; // 0 地址 1 负债均衡
	String proxyPass;

	String root;
	String upstreamId;

	
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


	public Integer getProxyPassType() {
		return proxyPassType;
	}

	public void setProxyPassType(Integer proxyPassType) {
		this.proxyPassType = proxyPassType;
	}

	public String getProxyPass() {
		return proxyPass;
	}

	public void setProxyPass(String proxyPass) {
		this.proxyPass = proxyPass;
	}

	public String getRoot() {
		return root;
	}

	public void setRoot(String root) {
		this.root = root;
	}

	public String getUpstreamId() {
		return upstreamId;
	}

	public void setUpstreamId(String upstreamId) {
		this.upstreamId = upstreamId;
	}

}
