package com.cym.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cym.model.Remote;

import cn.craccd.sqlHelper.utils.SqlHelper;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;

@Service
public class RemoteService {
	@Autowired
	SqlHelper sqlHelper;

	public String getCreditKey(Remote remote) {
		Map<String, Object> paramMap = new HashMap<String, Object>();

		paramMap.put("name", remote.getName());
		paramMap.put("pass", remote.getPass());

		try {
			String rs = HttpUtil.post(remote.getProtocol() + "://" + remote.getIp() + ":" + remote.getPort() + "/adminPage/login/getCredit", paramMap, 2000);

			if (StrUtil.isNotEmpty(rs)) {
				JSONObject jsonObject = new JSONObject(rs);
				if (jsonObject.getBool("success")) {
					return jsonObject.getStr("obj");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

}
