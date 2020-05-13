package com.cym.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cym.model.Http;
import com.cym.model.Server;

import cn.craccd.sqlHelper.bean.Page;
import cn.craccd.sqlHelper.bean.Sort;
import cn.craccd.sqlHelper.bean.Sort.Direction;
import cn.craccd.sqlHelper.utils.SqlHelper;

@Service
public class HttpService {
	@Autowired
	SqlHelper sqlHelper;
	
	
	public Page search(Page page) {

		page = sqlHelper.findPage(new Sort("name", Direction.ASC), page, Http.class);

		return page;
	}
}
