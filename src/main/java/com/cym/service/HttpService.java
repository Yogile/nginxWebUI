package com.cym.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cym.model.Http;
import com.cym.model.Server;
import com.cym.model.Stream;

import cn.craccd.sqlHelper.bean.Page;
import cn.craccd.sqlHelper.bean.Sort;
import cn.craccd.sqlHelper.bean.Sort.Direction;
import cn.craccd.sqlHelper.utils.CriteriaAndWrapper;
import cn.craccd.sqlHelper.utils.SqlHelper;
import cn.hutool.core.util.StrUtil;

@Service
public class HttpService {
	@Autowired
	SqlHelper sqlHelper;

	public boolean hasName(String name) {
		return sqlHelper.findCountByQuery(new CriteriaAndWrapper().eq("name", name), Http.class) > 0;
	}

	public void setAll(List<Http> https) {
		for (Http http : https) {
			Http httpOrg = sqlHelper.findOneByQuery(new CriteriaAndWrapper().eq("name", http.getName()), Http.class);

			if (httpOrg != null) {
				http.setId(httpOrg.getId());
			}

			if (http.getName().equals("gzip") && StrUtil.isEmpty(http.getValue())) {
				http.setValue("off");
			}

			sqlHelper.insertOrUpdate(http);

		}

	}

}
