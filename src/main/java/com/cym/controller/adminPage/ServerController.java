package com.cym.controller.adminPage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cym.model.Server;
import com.cym.service.ServerService;
import com.cym.utils.BaseController;
import com.cym.utils.JsonResult;

import cn.craccd.sqlHelper.bean.Page;
import cn.craccd.sqlHelper.bean.Sort;
import cn.hutool.core.util.StrUtil;
import cn.hutool.db.Entity;

@Controller
@RequestMapping("/adminPage/server")
public class ServerController extends BaseController {
	@Autowired
	ServerService serverService;

	@RequestMapping("")
	public ModelAndView index(HttpSession httpSession, ModelAndView modelAndView, Page page, String keywords, Integer type) {
		page = serverService.search(page, keywords, type);

		modelAndView.addObject("page", page);
		modelAndView.addObject("type", type);
		modelAndView.addObject("keywords", keywords);
		modelAndView.setViewName("/adminPage/server/index");
		return modelAndView;
	}

	@RequestMapping("addOver")
	@ResponseBody
	public JsonResult addOver(Server server) throws SQLException {

		if (server.getType() == 0) { // http
			server.setRoot(null);
		} else if (server.getType() == 1) { // root
			server.setProxyPass(null);
		} 

		if (server.getSsl() == 0) {
			server.setPem(null);
			server.setKey(null);
			server.setRewrite(null);
		}

		if (StrUtil.isNotEmpty(server.getId())) {
			sqlHelper.updateAllColumnById(server);
		} else {
			sqlHelper.insert(server);
		}

		return renderSuccess();
	}

	@RequestMapping("detail")
	@ResponseBody
	public JsonResult detail(String id) throws SQLException {
		return renderSuccess(sqlHelper.findById(id, Server.class));
	}

	@RequestMapping("del")
	@ResponseBody
	public JsonResult del(String id) throws SQLException {
		serverService.deleteById(id);

		return renderSuccess();
	}

}
