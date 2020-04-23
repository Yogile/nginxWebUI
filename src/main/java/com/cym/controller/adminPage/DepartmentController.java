package com.cym.controller.adminPage;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cym.ext.DepartmentExt;
import com.cym.model.Department;
import com.cym.service.DepartmentService;
import com.cym.utils.BaseController;
import com.cym.utils.JsonResult;

import cn.craccd.mongoHelper.bean.Page;

@Controller
@RequestMapping("/adminPage/department")
public class DepartmentController extends BaseController {
	@Autowired
	DepartmentService departmentService;

	@RequestMapping("")
	public ModelAndView index(ModelAndView modelAndView, Page page, String keywords) {

		page = departmentService.search(page, keywords);

		List<DepartmentExt> extList = new ArrayList<>();
		for (Department department : page.getRecords(Department.class)) {
			DepartmentExt departmentExt = new DepartmentExt();
			departmentExt.setDepartment(department);
			departmentExt.setUserCount(departmentService.getUserCount(department.getId()));
			extList.add(departmentExt);
		}
		page.setRecords(extList);

		modelAndView.addObject("keywords", keywords);
		modelAndView.addObject("page", page);
		modelAndView.setViewName("/adminPage/department/index");
		return modelAndView;
	}

	@RequestMapping("addOver")
	@ResponseBody
	public JsonResult addOver(Department department) {
		mongoHelper.insertOrUpdate(department);

		return renderSuccess();
	}

	@RequestMapping("detail")
	@ResponseBody
	public JsonResult detail(String id) {
		Department department = mongoHelper.findById(id, Department.class);
		DepartmentExt departmentExt = new DepartmentExt();
		departmentExt.setDepartment(department);

		return renderSuccess(departmentExt);
	}

	@RequestMapping("del")
	@ResponseBody
	public JsonResult del(String id) {
		Long count = departmentService.getUserCount(id);
		if(count > 0) {
			return renderError("此部门已有员工,不可删除");
		}
		
		mongoHelper.deleteById(id, Department.class);
		return renderSuccess();
	}
	
	
	
}
