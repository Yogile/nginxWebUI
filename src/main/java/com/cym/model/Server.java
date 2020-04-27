package com.cym.model;


import cn.craccd.sqlHelper.bean.BaseModel;
import cn.craccd.sqlHelper.config.InitIndex;
import cn.craccd.sqlHelper.config.InitValue;
import cn.craccd.sqlHelper.config.Table;

@Table
public class Server extends BaseModel{
	@InitIndex
	String serverName;
	String listen;
	Integer ssl; // 0 http 1 https 2 html
	String pem;
	@InitIndex(unique = true)
	String key;

	String proxyPass;
	Integer proxyPassPort;
	
	String root;
	
	public String getRoot() {
		return root;
	}

	public void setRoot(String root) {
		this.root = root;
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
