package com.cym.model;

import java.util.List;

public class Server {
	String serverName;
	String listen;
	Boolean ssl;
	String pem;
	String key;
	List<Location> locations;
	Boolean redirectToHttps;

	public Boolean getRedirectToHttps() {
		return redirectToHttps;
	}

	public void setRedirectToHttps(Boolean redirectToHttps) {
		this.redirectToHttps = redirectToHttps;
	}

	public List<Location> getLocations() {
		return locations;
	}

	public void setLocations(List<Location> locations) {
		this.locations = locations;
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
