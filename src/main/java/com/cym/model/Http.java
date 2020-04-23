package com.cym.model;

import java.util.List;

public class Http {
	String gzip;
	String clientMaxBodySize;

	List<Server> servers;

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

	public List<Server> getServers() {
		return servers;
	}

	public void setServers(List<Server> servers) {
		this.servers = servers;
	}

}
