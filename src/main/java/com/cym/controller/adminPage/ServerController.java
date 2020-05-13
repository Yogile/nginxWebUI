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

import com.cym.ext.ServerExt;
import com.cym.model.Server;
import com.cym.model.Upstream;
import com.cym.service.ServerService;
import com.cym.utils.BaseController;
import com.cym.utils.JsonResult;

import cn.craccd.sqlHelper.bean.Page;
import cn.hutool.core.util.StrUtil;

@Controller
@RequestMapping("/adminPage/server")
public class ServerController extends BaseController {
	@Autowired
	ServerService serverService;

	@RequestMapping("")
	public ModelAndView index(HttpSession httpSession, ModelAndView modelAndView, Page page, String keywords, Integer type) {
		page = serverService.search(page, keywords, type);

		List<ServerExt> exts = new ArrayList<ServerExt>();
		for (Server server : page.getRecords(Server.class)) {
			ServerExt serverExt = new ServerExt();
			serverExt.setServer(server);
			exts.add(serverExt);
		}
		page.setRecords(exts);

		modelAndView.addObject("page", page);
		modelAndView.addObject("type", type);

		modelAndView.addObject("upstreamList", sqlHelper.findAll(Upstream.class));
		modelAndView.addObject("keywords", keywords);
		modelAndView.setViewName("/adminPage/server/index");
		return modelAndView;
	}

	@RequestMapping("addOver")
	@ResponseBody
	public JsonResult addOver(Server server, Integer type[], String[] path, String[] value, String[] upstreamId) throws SQLException {

		serverService.addOver(server, type,  path,  value,  upstreamId);

		return renderSuccess();
	}

	@RequestMapping("detail")
	@ResponseBody
	public JsonResult detail(String id) throws SQLException {
		Server server = sqlHelper.findById(id, Server.class);

		ServerExt serverExt = new ServerExt();
		serverExt.setServer(server);
		serverExt.setLocationList(serverService.getLocationByServerId(id));

		return renderSuccess(serverExt);
	}

	@RequestMapping("del")
	@ResponseBody
	public JsonResult del(String id) throws SQLException {
		serverService.deleteById(id);

		return renderSuccess();
	}

}
