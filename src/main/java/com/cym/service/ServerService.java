package com.cym.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cym.model.Server;

import cn.craccd.sqlite.bean.Page;
import cn.craccd.sqlite.bean.Sort;
import cn.craccd.sqlite.utils.CriteriaAndWrapper;
import cn.craccd.sqlite.utils.CriteriaOrWrapper;
import cn.craccd.sqlite.utils.SqlHelper;
import cn.hutool.core.util.StrUtil;

@Service
public class ServerService {
	@Autowired
	SqlHelper sqliteHelper;

	public Page search(Page page, String word, Integer ssl) {
		CriteriaAndWrapper criteriaAndWrapper = new CriteriaAndWrapper();

		if (StrUtil.isNotEmpty(word)) {
			criteriaAndWrapper.and(new CriteriaOrWrapper().like("serverName", word).like("listen", word));
		}

		if (ssl != null) {
			criteriaAndWrapper.eq("ssl", ssl);
		}

		page = sqliteHelper.findPage(criteriaAndWrapper, new Sort("id", "desc"), page, Server.class);

		return page;
	}

}
