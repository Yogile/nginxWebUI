package com.cym.ext;

import cn.craccd.sqlHelper.config.InitValue;
import io.swagger.annotations.ApiModel;

@ApiModel("Location")
public class LocationReq {

	String path; // 监控路径
	Integer type; // 0 http 1 root 2 负载均衡 3 空白
	String locationParamJson; // 额外参数

	String value; // http代理

	String upstreamId; // 负载均衡代理
	String upstreamPath;

	String rootPath; // 静态页面代理
	String rootPage;
	String rootType;
	
	@InitValue("1")
	Integer header; // 是否设置header参数
	@InitValue("0")
	Integer websocket; // websocket支持
}
