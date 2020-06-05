package com.cym.service;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cym.model.LogInfo;

import cn.craccd.sqlHelper.utils.ConditionAndWrapper;
import cn.craccd.sqlHelper.utils.SqlHelper;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;

@Service
public class LogInfoService {
	@Autowired
	SqlHelper sqlHelper;

	@Transactional
	public void addOver(String path) {
		try {
			sqlHelper.deleteByQuery(new ConditionAndWrapper(), LogInfo.class);

			BufferedReader reader = FileUtil.getReader(path, "UTF-8");
			List<Object> list = new ArrayList<Object>();
			while (true) {
				String json = reader.readLine();
				if (StrUtil.isEmpty(json)) {
					sqlHelper.insertAll(list);
					list.clear();
					break;
				}
				list.add(JSONUtil.toBean(json, LogInfo.class));
				if (list.size() == 1000) {
					sqlHelper.insertAll(list);
					list.clear();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
