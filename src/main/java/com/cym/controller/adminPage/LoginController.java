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
import com.cym.model.Remote;
import com.cym.service.AdminService;
import com.cym.service.CreditService;
import com.cym.utils.BaseController;
import com.cym.utils.JsonResult;

import cn.hutool.core.util.StrUtil;

/**
 * 登录页
 * 
 * @author Administrator
 *
 */
@RequestMapping("/adminPage/login")
@Controller
public class LoginController extends BaseController {
	@Autowired
	AdminService adminService;
	@Autowired
	CreditService creditService;

	@RequestMapping("")
	public ModelAndView admin(ModelAndView modelAndView) {
		modelAndView.setViewName("/adminPage/login/index");
		return modelAndView;
	}

	@RequestMapping("login")
	@ResponseBody
	public JsonResult submitLogin(String name, String pass, HttpSession httpSession) {

		if (adminService.login(name, pass)) {

			httpSession.setAttribute("localType", "本地");
			httpSession.setAttribute("isLogin", true);
			return renderSuccess();
		} else {
			return renderError();
		}
	}

	@RequestMapping("loginOut")
	public String loginOut(HttpSession httpSession) {
		httpSession.removeAttribute("isLogin");
		return "redirect:/adminPage/login";
	}

	@ResponseBody
	@RequestMapping("getCredit")
	public JsonResult getCredit(String name, String pass) {
		if (adminService.login(name, pass)) {
			String key = creditService.make();
			return renderSuccess(key);
		} else {
			return renderError("授权失败");
		}

	}

	@ResponseBody
	@RequestMapping("getLocalType")
	public JsonResult getLocalType(HttpSession httpSession) {
		String localType = (String) httpSession.getAttribute("localType");
		if (StrUtil.isNotEmpty(localType)) {
			if (localType.equals("本地")) {
				return renderSuccess("本地");
			} else {
				Remote remote = (Remote) httpSession.getAttribute("remote");
				return renderSuccess(remote.getIp() + ":" + remote.getPort());
			}
		}
		
		return renderSuccess("");
	}

}
