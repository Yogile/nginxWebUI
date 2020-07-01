package com.cym.controller.adminPage;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cym.model.MonitorInfo;
import com.cym.service.MonitorService;
import com.cym.utils.BaseController;
import com.cym.utils.JsonResult;

@RequestMapping("/adminPage/monitor")
@Controller
public class MonitorController extends BaseController {
	@Autowired
	MonitorService monitorService;

	@RequestMapping("")
	public ModelAndView index(HttpSession httpSession, ModelAndView modelAndView) {

		modelAndView.setViewName("/adminPage/monitor/index");
		return modelAndView;
	}

	@RequestMapping("check")
	@ResponseBody
	public JsonResult check() {

		MonitorInfo monitorInfo = monitorService.getMonitorInfo();
		return renderSuccess(monitorInfo);
	}

}
