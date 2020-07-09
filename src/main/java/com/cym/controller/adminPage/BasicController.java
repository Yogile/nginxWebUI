package com.cym.controller.adminPage;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cym.model.Basic;
import com.cym.service.SettingService;
import com.cym.utils.BaseController;
import com.cym.utils.JsonResult;

@Controller
@RequestMapping("/adminPage/basic")
public class BasicController extends BaseController {
	@Autowired
	SettingService settingService;

	@RequestMapping("")
	public ModelAndView index(ModelAndView modelAndView) {
		List<Basic> basicList = sqlHelper.findAll(Basic.class);

		modelAndView.addObject("basicList", basicList);
		modelAndView.setViewName("/adminPage/basic/index");
		return modelAndView;
	}

	@RequestMapping("addOver")
	@ResponseBody
	public JsonResult addOver(Basic base) {
//		if (StrUtil.isEmpty(base.getId()) && baseService.hasName(base.getName())) {
//			return renderError("名称已存在");
//		}
		sqlHelper.insertOrUpdate(base);

		return renderSuccess();
	}

	@RequestMapping("detail")
	@ResponseBody
	public JsonResult detail(String id) {
		return renderSuccess(sqlHelper.findById(id, Basic.class));
	}

	@RequestMapping("del")
	@ResponseBody
	public JsonResult del(String id) {
		sqlHelper.deleteById(id, Basic.class);

		return renderSuccess();
	}

}
