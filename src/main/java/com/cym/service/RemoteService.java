package com.cym.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cym.model.Group;
import com.cym.model.Remote;

import cn.craccd.sqlHelper.utils.CriteriaAndWrapper;
import cn.craccd.sqlHelper.utils.CriteriaOrWrapper;
import cn.craccd.sqlHelper.utils.SqlHelper;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;

@Service
public class RemoteService {
	@Autowired
	SqlHelper sqlHelper;

	public void getCreditKey(Remote remote) {
		Map<String, Object> paramMap = new HashMap<String, Object>();

		paramMap.put("name", remote.getName());
		paramMap.put("pass", remote.getPass());

		try {
			String rs = HttpUtil.post(remote.getProtocol() + "://" + remote.getIp() + ":" + remote.getPort() + "/adminPage/login/getCredit", paramMap, 2000);

			if (StrUtil.isNotEmpty(rs)) {
				JSONObject jsonObject = new JSONObject(rs);
				if (jsonObject.getBool("success")) {
					remote.setSystem(jsonObject.getJSONObject("obj").getStr("system"));
					remote.setCreditKey(jsonObject.getJSONObject("obj").getStr("creditKey"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public List<Remote> getBySystem(String system) {
		return sqlHelper.findListByQuery(new CriteriaAndWrapper().eq("system", system), Remote.class);
	}

	public List<Remote> getListByParent(String parentId) {
		CriteriaAndWrapper criteriaAndWrapper = new CriteriaAndWrapper();
		if (StrUtil.isEmpty(parentId)) {
			criteriaAndWrapper.and(new CriteriaOrWrapper().eq("parentId", "").isNull("parentId"));
		} else {
			criteriaAndWrapper.eq("parentId", parentId);
		}

		return sqlHelper.findListByQuery(criteriaAndWrapper, Remote.class);
	}

}
