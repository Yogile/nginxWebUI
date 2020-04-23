package com.cym.controller.adminPage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

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
	@Value("${custom.admin.name}")
	private String name;
	@Value("${custom.admin.pass}")
	private String pass;

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

		if (this.name.equals(name) && this.pass.equals(pass)) {
			request.getSession().setAttribute("isLogin", true);
			return renderSuccess();
		} else {
			return renderError();
		}
	}

	@RequestMapping("login/loginOut")
	public String loginOut(HttpSession httpSession) {
		httpSession.removeAttribute("isLogin");
		return "redirect:/adminPage/login";
	}

}
