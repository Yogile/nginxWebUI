package com.cym.controller.adminPage;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cym.model.Admin;
import com.cym.model.Remote;
import com.cym.service.AdminService;
import com.cym.service.CreditService;
import com.cym.utils.BaseController;
import com.cym.utils.JsonResult;
import com.cym.utils.PwdCheckUtil;
import com.cym.utils.SystemTool;

import cn.craccd.sqlHelper.utils.CriteriaAndWrapper;
import cn.craccd.sqlHelper.utils.CriteriaWrapper;
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

		modelAndView.addObject("adminCount", sqlHelper.findCountByQuery(new CriteriaAndWrapper(), Admin.class));
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

	@RequestMapping("noServer")
	public ModelAndView noServer(ModelAndView modelAndView) {
		modelAndView.setViewName("/adminPage/login/noServer");
		return modelAndView;
	}

	@ResponseBody
	@RequestMapping("getCredit")
	public JsonResult getCredit(String name, String pass) {
		if (adminService.login(name, pass)) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("creditKey", creditService.make());
			map.put("system", SystemTool.getSystem());
			return renderSuccess(map);
		} else {
			return renderError("授权失败");
		}

	}

	@ResponseBody
	@RequestMapping("getLocalType")
	public JsonResult getLocalType(HttpSession httpSession) {
		String localType = (String) httpSession.getAttribute("localType");
		if (StrUtil.isNotEmpty(localType)) {
			if ("本地".equals(localType)) {
				return renderSuccess("本地");
			} else {
				Remote remote = (Remote) httpSession.getAttribute("remote");
				if (StrUtil.isNotEmpty(remote.getDescr())) {
					return renderSuccess(remote.getDescr());
				}

				return renderSuccess(remote.getIp() + ":" + remote.getPort());
			}
		}

		return renderSuccess("");
	}

	@RequestMapping("addAdmin")
	@ResponseBody
	public JsonResult addAdmin(String name, String pass) {

		Long adminCount = sqlHelper.findCountByQuery(new CriteriaAndWrapper(), Admin.class);
		if (adminCount > 0) {
			return renderError("管理员已初始化, 不能再次初始化");
		}

		if (!(PwdCheckUtil.checkContainUpperCase(pass) && PwdCheckUtil.checkContainLowerCase(pass) && PwdCheckUtil.checkContainDigit(pass) && PwdCheckUtil.checkPasswordLength(pass, "8", "100"))) {
			return renderError("密码复杂度太低");
		}
		

		Admin admin = new Admin();
		admin.setName(name);
		admin.setPass(pass);

		sqlHelper.insert(admin);

		return renderSuccess();
	}

}
