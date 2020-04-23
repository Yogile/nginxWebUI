package com.cym.controller.adminPage;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cym.ext.UserExt;
import com.cym.model.Department;
import com.cym.model.User;
import com.cym.service.UserService;
import com.cym.utils.BaseController;
import com.cym.utils.JsonResult;

import cn.craccd.mongoHelper.bean.Page;

@Controller
@RequestMapping("/adminPage/user")
public class UserController extends BaseController {
	@Autowired
	UserService userService;
	
	@RequestMapping("")
	public ModelAndView index(ModelAndView modelAndView, Page page, String keywords) {

		page = userService.search(page, keywords);

		List<UserExt> extList = new ArrayList<>();
		for (User user : page.getRecords(User.class)) {
			UserExt userExt = new UserExt();
			userExt.setUser(user);
			userExt.setDepartment(mongoHelper.findById(user.getDepartmentId(), Department.class));
			extList.add(userExt);
		}
		page.setRecords(extList);

		modelAndView.addObject("departmentList", mongoHelper.findAll(Department.class));
		
		modelAndView.addObject("keywords", keywords);
		modelAndView.addObject("page", page);
		modelAndView.setViewName("/adminPage/user/index");
		return modelAndView;
	}

	@RequestMapping("addOver")
	@ResponseBody
	public JsonResult addOver(User user) {
		mongoHelper.insertOrUpdate(user);

		return renderSuccess();
	}

	@RequestMapping("detail")
	@ResponseBody
	public JsonResult detail(String id) {
		User user = mongoHelper.findById(id, User.class);
		UserExt userExt = new UserExt();
		userExt.setUser(user);

		return renderSuccess(userExt);
	}

	@RequestMapping("del")
	@ResponseBody
	public JsonResult del(String id) {
		
		mongoHelper.deleteById(id, User.class);
		return renderSuccess();
	}
	
	

}
