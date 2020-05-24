package com.cym.config;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.cym.model.Version;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;

@Configuration
public class VersionConfig {

	@Value("${spring.application.name}")
	String projectName;

	Version version;

	@PostConstruct
	public void getNewVersion() {
		try {
			String json = HttpUtil.get("http://craccd.oss-cn-beijing.aliyuncs.com/version.json", 1000);
			if (StrUtil.isNotEmpty(json)) {
				version = JSONUtil.toBean(json, Version.class);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public Version getVersion() {
		return version;
	}

	public void setVersion(Version version) {
		this.version = version;
	}
	
	
}
