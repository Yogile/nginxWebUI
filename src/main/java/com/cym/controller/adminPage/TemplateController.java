package com.cym.controller.adminPage;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cym.model.Location;
import com.cym.model.Param;
import com.cym.model.Template;
import com.cym.service.TemplateService;
import com.cym.utils.BaseController;
import com.cym.utils.JsonResult;

import cn.hutool.json.JSONUtil;

@Controller
@RequestMapping("/adminPage/template")
public class TemplateController extends BaseController {
	@Autowired
	TemplateService templateService;
	
	@RequestMapping("")
	public ModelAndView index(HttpSession httpSession, ModelAndView modelAndView) {
		List<Template> templateList = sqlHelper.findAll(Template.class);

		modelAndView.addObject("templateList", templateList);
		modelAndView.setViewName("/adminPage/template/index");
		return modelAndView;
	}

	@RequestMapping("addOver")
	@ResponseBody
	public JsonResult addOver(Template template,String paramJson) {
		List<Param> params = JSONUtil.toList(JSONUtil.parseArray(paramJson), Param.class);
		
		templateService.addOver(template, params);

		return renderSuccess();
	}

	@RequestMapping("detail")
	@ResponseBody
	public JsonResult detail(String id) {
		return renderSuccess(sqlHelper.findById(id, Template.class));
	}

	@RequestMapping("del")
	@ResponseBody
	public JsonResult del(String id) {
		sqlHelper.deleteById(id, Template.class);

		return renderSuccess();
	}

}
