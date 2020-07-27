package com.cym.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cym.model.Param;
import com.cym.model.Template;

import cn.craccd.sqlHelper.utils.ConditionAndWrapper;
import cn.craccd.sqlHelper.utils.SqlHelper;

@Service
public class TemplateService {
	@Autowired
	SqlHelper sqlHelper;

	@Transactional
	public void addOver(Template template, List<Param> params) {
		sqlHelper.insertOrUpdate(template);
		
		sqlHelper.deleteByQuery(new ConditionAndWrapper().eq("templateId", template.getId()), Template.class);
		
		for(Param param:params) {
			param.setTemplateId(template.getId());
			sqlHelper.insertOrUpdate(param);
		}
		
	}
	
	
}
