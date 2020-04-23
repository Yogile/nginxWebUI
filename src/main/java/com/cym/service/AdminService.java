package com.cym.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cym.model.Admin;

import cn.craccd.mongoHelper.bean.Page;
import cn.craccd.mongoHelper.utils.CriteriaAndWrapper;
import cn.craccd.mongoHelper.utils.MongoHelper;
import cn.hutool.core.util.StrUtil;

@Service
public class AdminService  {
	@Autowired
	MongoHelper mongoHelper;
	
	public Page search(Page page, String word) {
		CriteriaAndWrapper criteriaAndWrapper = new CriteriaAndWrapper();

		if (StrUtil.isNotEmpty(word)) {
			criteriaAndWrapper.like("name", word);
		}
		page = mongoHelper.findPage(criteriaAndWrapper, page, Admin.class);

		return page;
	}

	public Admin login(String name, String pass) {
		CriteriaAndWrapper criteriaAndWrapper = new CriteriaAndWrapper().eq("name", name).eq("pass", pass);
		
		return mongoHelper.findOneByQuery(criteriaAndWrapper, Admin.class);
	}

	@Transactional
	public void add(Admin admin) {
		mongoHelper.insert(admin);
		
	}

}
