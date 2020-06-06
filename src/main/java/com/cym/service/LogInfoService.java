package com.cym.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
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
import com.cym.model.DateResult;
import com.cym.model.LogInfo;

import cn.craccd.sqlHelper.utils.ConditionAndWrapper;
import cn.craccd.sqlHelper.utils.SqlHelper;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.ZipUtil;
import cn.hutool.json.JSONException;
import cn.hutool.json.JSONUtil;

@Service
public class LogInfoService {
	@Autowired
	SqlHelper sqlHelper;
	@Autowired
	JdbcTemplate jdbcTemplate;

	@Transactional
	public void addOver(String path) {
		BufferedReader reader = null;
		try {
			File zipFile = new File(path);
			File outFile = new File(path.replace(".zip", "") + File.separator + zipFile.getName().replace(".zip", ".log"));
			ZipUtil.unzip(zipFile);

			sqlHelper.deleteByQuery(new ConditionAndWrapper(), LogInfo.class);

			reader = FileUtil.getReader(outFile, "UTF-8");
			List<Object> list = new ArrayList<Object>();
			while (true) {
				String json = reader.readLine();
				if (StrUtil.isEmpty(json)) {
					sqlHelper.insertAll(list);
					list.clear();
					break;
				}

				try {
					list.add(JSONUtil.toBean(json, LogInfo.class));
				} catch (JSONException e) {
					e.printStackTrace();
				}

				if (list.size() == 1000) {
					sqlHelper.insertAll(list);
					list.clear();
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IoUtil.close(reader);
			FileUtil.del(path.replace(".zip", "") + File.separator);
		}
	}

	public DataGroup getDataGroup(String path) {
		DataGroup dataGroup = findByPath(path);
		if (dataGroup != null) {
			return dataGroup;
		} else {
			dataGroup = new DataGroup();
		}

		// pvuv
		dataGroup.setUv(jdbcTemplate.queryForObject("select count(1) from (select count(1) from log_info group by remote_addr) as temp", Integer.class));
		dataGroup.setPv(jdbcTemplate.queryForObject("select count(1) from log_info", Integer.class));

		// 状态
		dataGroup.setStatus(jdbcTemplate.query("select status as name,count(1) as value FROM log_info group by status", new BeanPropertyRowMapper<KeyValue>(KeyValue.class)));

		// 系统
		dataGroup.setBrowser(new ArrayList<KeyValue>());
		String[] browsers = new String[] { "Android", "iPhone", "Windows" };
		Integer allCount = 0;
		for (String browser : browsers) {
			KeyValue keyValue = new KeyValue();
			keyValue.setName(browser);
			keyValue.setValue(jdbcTemplate.queryForObject("select count(1) from log_info where http_user_agent like '%" + browser + "%'", Integer.class));
			dataGroup.getBrowser().add(keyValue);
			allCount += keyValue.getValue();
		}

		KeyValue keyValue = new KeyValue();
		keyValue.setName("Other");
		keyValue.setValue(sqlHelper.findCountByQuery(null, LogInfo.class).intValue() - allCount);
		dataGroup.getBrowser().add(keyValue);

		// 域名
		dataGroup.setHttpReferer(
				jdbcTemplate.query("select http_host as name,count(1) as value FROM log_info group by http_host order by value asc", new BeanPropertyRowMapper<KeyValue>(KeyValue.class)));

		saveDataGroup(dataGroup, path);
		return dataGroup;
	}

	public void saveDataGroup(DataGroup dataGroup, String path) {
		sqlHelper.deleteByQuery(new ConditionAndWrapper().eq("name", path), DateResult.class);

		DateResult dateResult = new DateResult();
		dateResult.setName(path);
		dateResult.setJson(JSONUtil.toJsonStr(dataGroup));

		sqlHelper.insert(dateResult);
	}

	public DataGroup findByPath(String path) {
		DateResult dateResult = sqlHelper.findOneByQuery(new ConditionAndWrapper().eq("name", path), DateResult.class);
		if (dateResult != null) {
			return JSONUtil.toBean(dateResult.getJson(), DataGroup.class);
		}
		return null;
	}

}
