package com.cym.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cym.service.SettingService;

import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;

@Component
public class SendMailUtils {

	Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	SettingService settingService;

	
//	public static String apiUser = "nginxWebUI";
//	public static String apiKey = "5G8MAnKjINCBjAsX";

	
//	public void sendMailCLoud(String to, String title, String templateName) {
//		Map<String, Object> map = new HashMap<>();
//		map.put("apiUser", apiUser);
//		map.put("apiKey", apiKey);
//
//		map.put("from", "nginxWebUI@nginxWebUI.cn");
//		map.put("fromName", "nginxWebUI");
//
//		List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
//		Map<String, String> data = new HashMap<String, String>();
//		data.put("to", to);
//		data.put("name", "【" + name + "】");
//		dataList.add(data);
//
//		final String xsmtpapi = convert(dataList);
//
//		map.put("xsmtpapi", JSONUtil.toJsonStr(xsmtpapi));
//
//		map.put("templateInvokeName", templateName);
//
//		String rs = HttpUtil.post("https://api.sendcloud.net/apiv2/mail/sendtemplate", map);
//
//		logger.info(rs);
//	}
//
//	public static String convert(List<Map<String, String>> dataList) {
//
//		JSONObject ret = new JSONObject();
//
//		JSONArray to = new JSONArray();
//		JSONArray names = new JSONArray();
//
//		for (Map<String, String> map : dataList) {
//			to.put(map.get("to"));
//			names.put(map.get("name"));
//		}
//
//		JSONObject sub = new JSONObject();
//		sub.set("%name%", names);
//
//		ret.set("to", to);
//		ret.set("sub", sub);
//
//		return ret.toStringPretty();
//	}

	public void sendMailSmtp(String to, String title, String msg) {

		MailAccount account = new MailAccount();
		account.setHost(settingService.get("mail_host"));
		if (settingService.get("mail_port") != null) {
			account.setPort(Integer.parseInt(settingService.get("mail_port")));
		}
		account.setAuth(true);
		account.setFrom(settingService.get("mail_from"));
		account.setUser(settingService.get("mail_user"));
		account.setPass(settingService.get("mail_pass"));
		if (settingService.get("mail_ssl") != null) {
			account.setSslEnable(Boolean.parseBoolean(settingService.get("mail_ssl")));
		}

		MailUtil.send(account, to, title, msg, false);

	}
}
