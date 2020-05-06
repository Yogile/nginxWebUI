package com.cym.model;

import cn.craccd.sqlHelper.bean.BaseModel;
import cn.craccd.sqlHelper.config.InitIndex;
import cn.craccd.sqlHelper.config.InitValue;
import cn.craccd.sqlHelper.config.Table;

@Table
public class Server extends BaseModel {
	@InitIndex
	String serverName;
	String listen;
	Integer type; // 0 http 1 root
	Integer ssl; // 0 否 1是
	Integer rewrite; // 0否 1是
	String pem;
	String key;
	
	@InitValue("0")
	Integer proxyPassType; //0 地址 1 负债均衡
	String proxyPass;

	String root;
	String upstreamId;
	
	
	public String getUpstreamId() {
		return upstreamId;
	}

	public void setUpstreamId(String upstreamId) {
		this.upstreamId = upstreamId;
	}

	public Integer getProxyPassType() {
		return proxyPassType;
	}

	public void setProxyPassType(Integer proxyPassType) {
		this.proxyPassType = proxyPassType;
	}

	public Integer getRewrite() {
		return rewrite;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public void setRewrite(Integer rewrite) {
		this.rewrite = rewrite;
	}

	public String getRoot() {
		return root;
	}

	public void setRoot(String root) {
		this.root = root;
	}

	public String getProxyPass() {
		return proxyPass;
	}

	public void setProxyPass(String proxyPass) {
		this.proxyPass = proxyPass;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getListen() {
		return listen;
	}

	public void setListen(String listen) {
		this.listen = listen;
	}

	public Integer getSsl() {
		return ssl;
	}

	public void setSsl(Integer ssl) {
		this.ssl = ssl;
	}

	public String getPem() {
		return pem;
	}

	public void setPem(String pem) {
		this.pem = pem;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

}
