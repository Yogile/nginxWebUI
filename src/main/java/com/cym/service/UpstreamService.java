package com.cym.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cym.model.Upstream;
import com.cym.model.UpstreamServer;

import cn.craccd.sqlHelper.bean.Page;
import cn.craccd.sqlHelper.bean.Sort;
import cn.craccd.sqlHelper.bean.Sort.Direction;
import cn.craccd.sqlHelper.utils.CriteriaAndWrapper;
import cn.craccd.sqlHelper.utils.CriteriaOrWrapper;
import cn.craccd.sqlHelper.utils.SqlHelper;
import cn.hutool.core.util.StrUtil;

@Service
public class UpstreamService {
	@Autowired
	SqlHelper sqlHelper;

	public Page search(Page page, String word) {
		CriteriaAndWrapper criteriaAndWrapper = new CriteriaAndWrapper();

		if (StrUtil.isNotEmpty(word)) {
			criteriaAndWrapper.and(new CriteriaOrWrapper().like("name", word));
		}

		page = sqlHelper.findPage(criteriaAndWrapper, new Sort("id", Direction.DESC), page, Upstream.class);

		return page;
	}

	@Transactional
	public void deleteById(String id) {
		sqlHelper.deleteById(id, Upstream.class);
		sqlHelper.deleteByQuery(new CriteriaAndWrapper().eq("upstreamId", id), UpstreamServer.class);
	}

	@Transactional
	public void addOver(Upstream upstream, String[] servers, Integer[] ports, Integer[] weights, Integer[] maxFails, Integer[] failTimeout, String[] status) {
		sqlHelper.insertOrUpdate(upstream);
		sqlHelper.deleteByQuery(new CriteriaAndWrapper().eq("upstreamId", upstream.getId()), UpstreamServer.class);

		for (int i = 0; i < servers.length; i++) {
			UpstreamServer upstreamServer = new UpstreamServer();
			upstreamServer.setUpstreamId(upstream.getId());
			upstreamServer.setServer(servers[i]);
			upstreamServer.setPort(ports[i]);
			upstreamServer.setWeight(weights[i]);

			upstreamServer.setMaxFails(maxFails[i]);
			upstreamServer.setFailTimeout(failTimeout[i]);
			upstreamServer.setStatus(status[i]);
			
			sqlHelper.insert(upstreamServer);
		}

	}

	public List<UpstreamServer> getUpstreamServers(String id) {
		return sqlHelper.findListByQuery(new CriteriaAndWrapper().eq("upstreamId", id), UpstreamServer.class);
	}

}
