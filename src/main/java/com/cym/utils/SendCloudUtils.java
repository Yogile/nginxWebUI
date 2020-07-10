package com.cym.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

@Component
public class SendCloudUtils {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	public static String apiUser = "nginxWebUI";
	public static String apiKey = "5G8MAnKjINCBjAsX";

	public void sendMail(String to, String name) {
		Map<String, Object> map = new HashMap<>();
		map.put("apiUser", apiUser);
		map.put("apiKey", apiKey);

		map.put("from", "nginxWebUI@nginxWebUI.cn");
		map.put("fromName", "nginxWebUI");

		List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
		Map<String, String> data = new HashMap<String, String>();
		data.put("to", to);
		data.put("name", "【" + name + "】");
		dataList.add(data);

		final String xsmtpapi = convert(dataList);

		map.put("xsmtpapi", JSONUtil.toJsonStr(xsmtpapi));

		map.put("templateInvokeName", "nginx_stop");

		String rs = HttpUtil.post("https://api.sendcloud.net/apiv2/mail/sendtemplate", map);

		logger.info(rs);
	}

	public static String convert(List<Map<String, String>> dataList) {

		JSONObject ret = new JSONObject();

		JSONArray to = new JSONArray();
		JSONArray names = new JSONArray();

		for (Map<String, String> map : dataList) {
			to.put(map.get("to"));
			names.put(map.get("name"));
		}

		JSONObject sub = new JSONObject();
		sub.set("%name%", names);

		ret.set("to", to);
		ret.set("sub", sub);
		
		return ret.toStringPretty();
	}

	public static void main(String[] args) {
		new SendCloudUtils().sendMail("cym1102@qq.com", "长春服务器");
	}
}
