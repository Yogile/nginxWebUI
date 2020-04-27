package com.cym.controller.adminPage;

import java.sql.SQLException;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cym.model.Admin;
import com.cym.service.AdminService;
import com.cym.utils.BaseController;
import com.cym.utils.JsonResult;

import cn.craccd.sqlHelper.bean.Page;

@Controller
@RequestMapping("/adminPage/admin")
public class AdminController extends BaseController {
	@Autowired
	AdminService adminService;
	
	@RequestMapping("")
	public ModelAndView index(HttpSession httpSession, ModelAndView modelAndView, Page page) {
		page = adminService.search(page);

		modelAndView.addObject("page", page);
		modelAndView.setViewName("/adminPage/admin/index");
		return modelAndView;
	}

	@RequestMapping("addOver")
	@ResponseBody
	public JsonResult addOver(Admin admin) throws SQLException {

		sqlHelper.insertOrUpdate(admin);

		return renderSuccess();
	}

	@RequestMapping("detail")
	@ResponseBody
	public JsonResult detail(String id) throws SQLException {
		return renderSuccess(sqlHelper.findById(id, Admin.class));
	}

	@RequestMapping("del")
	@ResponseBody
	public JsonResult del(String id) throws SQLException {
		sqlHelper.deleteById(id, Admin.class);
		
		return renderSuccess();
	}

}
