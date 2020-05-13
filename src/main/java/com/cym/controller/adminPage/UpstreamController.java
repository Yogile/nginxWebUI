package com.cym.controller.adminPage;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cym.ext.UpstreamExt;
import com.cym.model.Upstream;
import com.cym.model.UpstreamServer;
import com.cym.service.UpstreamService;
import com.cym.utils.BaseController;
import com.cym.utils.JsonResult;

import cn.craccd.sqlHelper.bean.Page;
import cn.hutool.core.util.StrUtil;

@Controller
@RequestMapping("/adminPage/upstream")
public class UpstreamController extends BaseController {
	@Autowired
	UpstreamService upstreamService;

	@RequestMapping("")
	public ModelAndView index(HttpSession httpSession, ModelAndView modelAndView, Page page, String keywords) {
		page = upstreamService.search(page, keywords);

		List<UpstreamExt> list = new ArrayList<UpstreamExt>();
		for (Upstream upstream : page.getRecords(Upstream.class)) {
			UpstreamExt upstreamExt = new UpstreamExt();
			upstreamExt.setUpstream(upstream);

			List<String> str = new ArrayList<String>();
			List<UpstreamServer> servers = upstreamService.getUpstreamServers(upstream.getId());
			for (UpstreamServer upstreamServer : servers) {
				str.add(buildStr(upstreamServer));
			}

			upstreamExt.setServerStr(StrUtil.join("<br>", str));
			list.add(upstreamExt);
		}
		page.setRecords(list);

		modelAndView.addObject("page", page);
		modelAndView.addObject("keywords", keywords);
		modelAndView.setViewName("/adminPage/upstream/index");
		return modelAndView;
	}

	public String buildStr(UpstreamServer upstreamServer) {
		String status = "";
		if (!"none".equals(upstreamServer.getStatus())) {
			status = upstreamServer.getStatus();
		}

		return upstreamServer.getServer() + ":" + upstreamServer.getPort() //
				+ " weight=" + upstreamServer.getWeight() //
				+ " fail_timeout=" + upstreamServer.getFailTimeout() + "s"//
				+ " max_fails=" + upstreamServer.getMaxFails() //
				+ " " + status;
	}

	@RequestMapping("addOver")
	@ResponseBody
	public JsonResult addOver(Upstream upstream, String[] server, Integer[] port, Integer[] weight, Integer[] maxFails, Integer[] failTimeout, String[] status) throws SQLException {

		upstreamService.addOver(upstream, server, port, weight, maxFails, failTimeout, status);

		return renderSuccess();
	}

	@RequestMapping("detail")
	@ResponseBody
	public JsonResult detail(String id) throws SQLException {

		UpstreamExt upstreamExt = new UpstreamExt();
		upstreamExt.setUpstream(sqlHelper.findById(id, Upstream.class));
		upstreamExt.setUpstreamServerList(upstreamService.getUpstreamServers(id));

		return renderSuccess(upstreamExt);
	}

	@RequestMapping("del")
	@ResponseBody
	public JsonResult del(String id) throws SQLException {
		
		
		upstreamService.del(id);

		return renderSuccess();
	}

}
