package com.cym.service;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cym.ext.DataGroup;
import com.cym.ext.KeyValue;
import com.cym.model.Bak;
import com.cym.model.LogInfo;

import cn.craccd.sqlHelper.utils.ConditionAndWrapper;
import cn.craccd.sqlHelper.utils.SqlHelper;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;

@Service
public class LogInfoService {
	@Autowired
	SqlHelper sqlHelper;
	@Autowired
	JdbcTemplate jdbcTemplate;

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

	public DataGroup getDataGroup() {
		DataGroup dataGroup = new DataGroup();

		dataGroup.setUv(jdbcTemplate.queryForObject("select count(1) from (select count(1) from log_info group by remote_addr) as temp", Integer.class));
		dataGroup.setPv(jdbcTemplate.queryForObject("select count(1) from log_info", Integer.class));

		dataGroup.setStatus(jdbcTemplate.query("select status as name,count(1) as value FROM log_info group by status", new BeanPropertyRowMapper<KeyValue>(KeyValue.class)));

		dataGroup.setBrowser(new ArrayList<KeyValue>());
		String[] browsers = new String[] { "Android", "iPhone", "Windows" };
		for (String browser : browsers) {
			KeyValue keyValue = new KeyValue();
			keyValue.setName(browser);
			keyValue.setValue(jdbcTemplate.queryForObject("select count(1) from log_info where http_user_agent like '%" + browser + "%'", Integer.class));
			dataGroup.getBrowser().add(keyValue);
		}

		List<KeyValue> keyValues = new ArrayList<KeyValue>();
		List<Map<String, Object>> list = jdbcTemplate.queryForList("select request, http_host FROM log_info");

		for (Map<String, Object> map : list) {
			String name = map.get("http_host").toString() + map.get("request").toString().split(" ")[1];
			if (name.contains("?")) {
				name = name.split("\\?")[0];
			}

			KeyValue keyValue = findKeyValue(keyValues, name);
			keyValue.setValue(keyValue.getValue() + 1);
		}

		CollectionUtil.sort(keyValues, new Comparator<KeyValue>() {

			@Override
			public int compare(KeyValue o1, KeyValue o2) {
				return o1.getValue() - o2.getValue();
			}
		});

		dataGroup.setHttpReferer(new ArrayList<KeyValue>());
		for (int i = 0; i < 10 && i < keyValues.size(); i++) {
			dataGroup.getHttpReferer().add(keyValues.get(i));
		}

		return dataGroup;
	}

	private KeyValue findKeyValue(List<KeyValue> list, String name) {
		for (KeyValue keyValue : list) {
			if (keyValue.getName().equals(name)) {
				return keyValue;
			}
		}

		KeyValue keyValue = new KeyValue();
		keyValue.setName(name);
		keyValue.setValue(0);
		list.add(keyValue);

		return keyValue;
	}

}
