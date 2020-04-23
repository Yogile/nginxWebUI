package com.cym.model;

import org.springframework.data.mongodb.core.mapping.Document;

import cn.craccd.mongoHelper.bean.BaseModel;

@Document
public class Server extends BaseModel{
	String serverName;
	String listen;
	Boolean ssl;
	String pem;
	String key;

	String proxyPass;
	String proxyPassPort;
	
	public String getProxyPassPort() {
		return proxyPassPort;
	}

	public void setProxyPassPort(String proxyPassPort) {
		this.proxyPassPort = proxyPassPort;
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

	public Boolean getSsl() {
		return ssl;
	}

	public void setSsl(Boolean ssl) {
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
