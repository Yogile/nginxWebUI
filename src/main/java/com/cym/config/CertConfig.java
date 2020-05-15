package com.cym.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.annotation.PostConstruct;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ZipUtil;

@Component
public class CertConfig {
	
	public String acmeSh;
	
	@PostConstruct
	public void init() throws IOException {
		
		String userDir = FileUtil.getUserHomePath();
		
		ClassPathResource resource = new ClassPathResource("acme.zip");
		InputStream inputStream = resource.getInputStream();
		
		FileUtil.writeFromStream(inputStream, userDir + File.separator + "acme.zip");
		FileUtil.mkdir(userDir + File.separator + "acme");
		ZipUtil.unzip(userDir + File.separator + "acme.zip", userDir + File.separator + "acme");
		FileUtil.del(userDir + File.separator + "acme.zip");
		
		acmeSh = userDir + File.separator + "acme" + File.separator + "acme.sh";
		System.err.println(acmeSh);
	}
	
	
	
}
