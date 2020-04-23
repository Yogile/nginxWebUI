package com.cym.controller.adminPage;

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

import cn.craccd.mongoHelper.bean.Page;

@Controller
@RequestMapping("/adminPage/admin")
public class AdminController extends BaseController {
	@Autowired
	AdminService adminService;

	@RequestMapping("")
	public ModelAndView index(ModelAndView modelAndView, Page page, String keywords) {

		page = adminService.search(page, keywords);
		modelAndView.addObject("keywords", keywords);
		modelAndView.addObject("page", page);
		modelAndView.setViewName("/adminPage/admin/index");
		return modelAndView;
	}

	@RequestMapping("addOver")
	@ResponseBody
	public JsonResult addOver(Admin admin) {
		adminService.add(admin);
		return renderSuccess();
	}

	@RequestMapping("detail")
	@ResponseBody
	public JsonResult detail(String id) {
		Admin admin = mongoHelper.findById(id, Admin.class);
		return renderSuccess(admin);
	}

	@RequestMapping("del")
	@ResponseBody
	public JsonResult del(String id) {
		mongoHelper.deleteById(id, Admin.class);
		return renderSuccess();
	}

	

	@RequestMapping("goMenu")
	public String goMenu(HttpSession httpSession) {
		return "redirect:/adminPage/user";
	}
}
