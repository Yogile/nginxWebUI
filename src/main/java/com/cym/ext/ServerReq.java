package com.cym.ext;

import java.util.List;

import cn.craccd.sqlHelper.config.InitValue;
import io.swagger.annotations.ApiModel;

@ApiModel("Server")
public class ServerReq {
	
	String serverName;
	String listen;
	
	@InitValue("0")
	Integer def; // 是否为默认server

	@InitValue("0")
	Integer rewrite; // 0否 1是
	@InitValue("80")
	String rewriteListen; // 转跳监听

	@InitValue("0")
	Integer ssl; // 0 否 1是
	@InitValue("0")
	Integer http2; // 0否 1是
	
	String pem;
	String key;
	
	@InitValue("0")
	Integer proxyType; // 代理类型 0 http 1 tcp 2 udp
	String proxyUpstreamId;

	String pemStr;
	String keyStr;

	@InitValue("true")
	Boolean enable; // 是否启用

	String descr; // 描述
	@InitValue("TLSv1 TLSv1.1 TLSv1.2 TLSv1.3")
	String protocols; // 加密协议

	String passwordId;
	String denyAllowId;
	
	Long seq; 
	
	
	List<LocationReq> locationList;
}
