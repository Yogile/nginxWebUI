package com.cym.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cym.model.Location;
import com.cym.model.Server;

import cn.craccd.sqlHelper.bean.Page;
import cn.craccd.sqlHelper.bean.Sort;
import cn.craccd.sqlHelper.bean.Sort.Direction;
import cn.craccd.sqlHelper.utils.CriteriaAndWrapper;
import cn.craccd.sqlHelper.utils.CriteriaOrWrapper;
import cn.craccd.sqlHelper.utils.SqlHelper;
import cn.hutool.core.util.StrUtil;

@Service
public class ServerService {
	@Autowired
	SqlHelper sqlHelper;

	public Page search(Page page, String word, Integer type) {
		CriteriaAndWrapper criteriaAndWrapper = new CriteriaAndWrapper();

		if (StrUtil.isNotEmpty(word)) {
			criteriaAndWrapper.and(new CriteriaOrWrapper().like("serverName", word).like("proxyPass", word));
		}

		if (type != null) {
			criteriaAndWrapper.eq("type", type);
		}

		page = sqlHelper.findPage(criteriaAndWrapper, new Sort("id", Direction.DESC), page, Server.class);

		return page;
	}

	@Transactional
	public void deleteById(String id) {
		sqlHelper.deleteById(id, Server.class);
		sqlHelper.deleteByQuery(new CriteriaAndWrapper().eq("serverId", id), Location.class);
	}

	public List<Location> getLocationByServerId(String serverId) {
		return sqlHelper.findListByQuery(new CriteriaAndWrapper().eq("serverId", serverId), Location.class);
	}

}
