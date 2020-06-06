package com.cym.ext;

import java.util.List;

public class DataGroup {
	Integer pv;
	Integer uv;

	List<KeyValue> status;
	List<KeyValue> browser;

	List<KeyValue> httpReferer;

	public List<KeyValue> getHttpReferer() {
		return httpReferer;
	}

	public void setHttpReferer(List<KeyValue> httpReferer) {
		this.httpReferer = httpReferer;
	}

	public List<KeyValue> getBrowser() {
		return browser;
	}

	public void setBrowser(List<KeyValue> browser) {
		this.browser = browser;
	}

	public List<KeyValue> getStatus() {
		return status;
	}

	public void setStatus(List<KeyValue> status) {
		this.status = status;
	}

	public Integer getPv() {
		return pv;
	}

	public void setPv(Integer pv) {
		this.pv = pv;
	}

	public Integer getUv() {
		return uv;
	}

	public void setUv(Integer uv) {
		this.uv = uv;
	}


}
