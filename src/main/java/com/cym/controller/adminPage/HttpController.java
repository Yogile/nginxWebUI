package com.cym.controller.adminPage;

import java.sql.SQLException;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cym.model.Http;
import com.cym.service.HttpService;
import com.cym.utils.BaseController;
import com.cym.utils.JsonResult;

import cn.craccd.sqlHelper.bean.Page;

@Controller
@RequestMapping("/adminPage/http")
public class HttpController extends BaseController {
	@Autowired
	HttpService httpService;
	
	@RequestMapping("")
	public ModelAndView index(HttpSession httpSession, ModelAndView modelAndView, Page page) {
		page = httpService.search(page);

		modelAndView.addObject("page", page);
		modelAndView.setViewName("/adminPage/http/index");
		return modelAndView;
	}

	@RequestMapping("addOver")
	@ResponseBody
	public JsonResult addOver(Http http) throws SQLException {

		sqlHelper.insertOrUpdate(http);

		return renderSuccess();
	}

	@RequestMapping("detail")
	@ResponseBody
	public JsonResult detail(String id) throws SQLException {
		return renderSuccess(sqlHelper.findById(id, Http.class));
	}

	@RequestMapping("del")
	@ResponseBody
	public JsonResult del(String id) throws SQLException {
		sqlHelper.deleteById(id, Http.class);
		
		return renderSuccess();
	}

}
