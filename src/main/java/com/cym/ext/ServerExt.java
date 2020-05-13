package com.cym.ext;

import java.util.List;

import com.cym.model.Location;
import com.cym.model.Server;
import com.cym.model.Upstream;

public class ServerExt {
	Server server;

	List<LocationExt> locationExtList;


	public Server getServer() {
		return server;
	}

	public void setServer(Server server) {
		this.server = server;
	}

	public List<LocationExt> getLocationExtList() {
		return locationExtList;
	}

	public void setLocationExtList(List<LocationExt> locationExtList) {
		this.locationExtList = locationExtList;
	}


}
