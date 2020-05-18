package com.cym.controller.adminPage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cym.model.Credit;
import com.cym.service.AdminService;
import com.cym.service.CreditService;
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
	@Autowired
	CreditService creditService;

	@RequestMapping("login")
	public ModelAndView admin(ModelAndView modelAndView) {
		modelAndView.setViewName("/adminPage/login/index");
		return modelAndView;
	}

	@RequestMapping("login/login")
	@ResponseBody
	public JsonResult submitLogin(String name, String pass, HttpSession httpSession) {

		if (adminService.login(name, pass)) {

			httpSession.setAttribute("localType", "本地");
			httpSession.removeAttribute("remote");
			httpSession.setAttribute("isLogin", true);
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

	@RequestMapping("login/getCredit")
	public JsonResult getCredit(String name, String pass) {
		if (adminService.login(name, pass)) {
			String key = creditService.make();
			return renderSuccess(key);
		} else {
			return renderError("授权失败");
		}

	}

}
