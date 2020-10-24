package com.cym.ext;

public class DiskInfo {
	String path;

	String usableSpace;
	String totalSpace;

	String percent;
	
	
	
	public String getPercent() {
		return percent;
	}

	public void setPercent(String percent) {
		this.percent = percent;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getUsableSpace() {
		return usableSpace;
	}

	public void setUsableSpace(String usableSpace) {
		this.usableSpace = usableSpace;
	}

	public String getTotalSpace() {
		return totalSpace;
	}

	public void setTotalSpace(String totalSpace) {
		this.totalSpace = totalSpace;
	}

}
