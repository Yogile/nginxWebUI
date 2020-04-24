package com.cym.controller.adminPage;

import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cym.model.Http;
import com.cym.utils.BaseController;
import com.cym.utils.JsonResult;

import cn.hutool.db.Db;
import cn.hutool.db.Entity;

@Controller
@RequestMapping("/adminPage/http")
public class HttpController extends BaseController {

	@RequestMapping("")
	public ModelAndView index(HttpSession httpSession, ModelAndView modelAndView) throws SQLException {
		Http http = null;
		List<Entity> result = Db.use().findAll("http");

		if (result.size() == 0) {
			http = new Http();
			http.setGzip("on");
			http.setClientMaxBodySize(512);
			Db.use().insert(Entity.parse(http));
		} else {
			http = result.get(0).toBean(Http.class);
		}

		modelAndView.addObject("http", http);
		modelAndView.setViewName("/adminPage/http/index");
		return modelAndView;
	}

	@RequestMapping(value = "addOver")
	@ResponseBody
	public JsonResult addOver(String name, String value) throws SQLException {
		List<Entity> result = Db.use().findAll("http");
		
		Http http = result.get(0).toBean(Http.class);
		if (name.equals("gzip")) {
			http.setGzip(value);
		} else if (name.equals("clientMaxBodySize")) {
			http.setClientMaxBodySize(Integer.parseInt(value));
		}

		Db.use().insertOrUpdate(Entity.parse(http), "id");

		return renderSuccess();

	}
}
