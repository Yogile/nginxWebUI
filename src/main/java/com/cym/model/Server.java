package com.cym.model;

import cn.craccd.sqlite.bean.BaseModel;
import cn.craccd.sqlite.config.InitValue;
import cn.craccd.sqlite.config.Table;

@Table
public class Server extends BaseModel{
	String id;
	
	String serverName;
	String listen;
	Integer ssl;
	String pem;
	String key;

	String proxyPass;
	Integer proxyPassPort;
	
	@InitValue("/home/www")
	String root;
	
	public String getRoot() {
		return root;
	}

	public void setRoot(String root) {
		this.root = root;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getProxyPassPort() {
		return proxyPassPort;
	}

	public void setProxyPassPort(Integer proxyPassPort) {
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
