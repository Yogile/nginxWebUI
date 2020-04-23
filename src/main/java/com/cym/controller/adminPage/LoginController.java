package com.cym.controller.adminPage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cym.config.ScheduleTask;
import com.cym.model.Admin;
import com.cym.service.AdminService;
import com.cym.utils.BaseController;
import com.cym.utils.JsonResult;

/**
 * 登录页
 * 
 * @author Administrator
 *
 */
@RequestMapping("/adminPage")
@Controller
public class LoginController extends BaseController {
	@Autowired
	AdminService adminService;
	
	@RequestMapping("")
	public String admin() {
	
		return "redirect:adminPage/login";
	}
	
	@RequestMapping("login")
	public ModelAndView admin(ModelAndView modelAndView) {
		modelAndView.setViewName("/adminPage/login/index");
		return modelAndView;
	}

	@RequestMapping(value = "login/login")
	@ResponseBody
	public JsonResult submitLogin(String name, String pass, HttpServletRequest request) {
		Admin admin = adminService.login(name, pass);
		if (admin == null) {
			return renderError("登录失败");
		}
		request.getSession().setAttribute("admin", admin);
		return renderSuccess();
	}

	@RequestMapping("login/loginOut")
	public String loginOut(HttpSession httpSession) {
		httpSession.removeAttribute("admin");
		return "redirect:/adminPage/login";
	}


}
