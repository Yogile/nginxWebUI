package com.cym.controller.adminPage;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cym.config.VersionConfig;
import com.cym.model.Admin;
import com.cym.model.Remote;
import com.cym.service.AdminService;
import com.cym.service.CreditService;
import com.cym.service.SettingService;
import com.cym.utils.BaseController;
import com.cym.utils.JsonResult;
import com.cym.utils.PwdCheckUtil;
import com.cym.utils.SystemTool;

import cn.craccd.sqlHelper.utils.ConditionAndWrapper;
import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.ShearCaptcha;
import cn.hutool.captcha.generator.RandomGenerator;
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
	@Autowired
	VersionConfig versionConfig;

	@Value("${project.version}")
	String currentVersion;

	@Autowired
	SettingService settingService;

	@RequestMapping("map")
	public ModelAndView map(ModelAndView modelAndView) {

		modelAndView.setViewName("/adminPage/login/map");
		return modelAndView;
	}

	@RequestMapping("")
	public ModelAndView admin(ModelAndView modelAndView) {


		modelAndView.addObject("adminCount", sqlHelper.findCountByQuery(new ConditionAndWrapper(), Admin.class));
		modelAndView.setViewName("/adminPage/login/index");
		return modelAndView;
	}

	@RequestMapping("login")
	@ResponseBody
	public JsonResult submitLogin(String name, String pass, String code, HttpSession httpSession) {
		String imgCode = (String) httpSession.getAttribute("imgCode");
		if (StrUtil.isNotEmpty(imgCode) && !imgCode.equalsIgnoreCase(code)) {
			return renderError(m.get("loginStr.backError1"));
		}

		if (adminService.login(name, pass)) {

			httpSession.setAttribute("localType", "local");
			httpSession.setAttribute("isLogin", true);

			// 检查更新
			versionConfig.getNewVersion();

			return renderSuccess();
		} else {
			return renderError(m.get("loginStr.backError2"));
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
			return renderError(m.get("loginStr.backError3"));
		}

	}

	@ResponseBody
	@RequestMapping("getLocalType")
	public JsonResult getLocalType(HttpSession httpSession) {
		String localType = (String) httpSession.getAttribute("localType");
		if (StrUtil.isNotEmpty(localType)) {
			if ("local".equals(localType)) {
				return renderSuccess(m.get("remoteStr.local"));
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

	@ResponseBody
	@RequestMapping("changeLang")
	public JsonResult changeLang() {
		if (settingService.get("lang") != null && settingService.get("lang").equals("en_US")) {
			settingService.set("lang", "");
		} else {
			settingService.set("lang", "en_US");
		}

		return renderSuccess();
	}

	@RequestMapping("addAdmin")
	@ResponseBody
	public JsonResult addAdmin(String name, String pass) {

		Long adminCount = sqlHelper.findCountByQuery(new ConditionAndWrapper(), Admin.class);
		if (adminCount > 0) {
			return renderError(m.get("loginStr.backError4"));
		}

		if (!(PwdCheckUtil.checkContainUpperCase(pass) && PwdCheckUtil.checkContainLowerCase(pass) && PwdCheckUtil.checkContainDigit(pass) && PwdCheckUtil.checkPasswordLength(pass, "8", "100"))) {
			return renderError(m.get("loginStr.tips"));
		}

		Admin admin = new Admin();
		admin.setName(name);
		admin.setPass(pass);

		sqlHelper.insert(admin);

		return renderSuccess();
	}

	@RequestMapping("/getCode")
	public void getCode(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
		ShearCaptcha captcha = CaptchaUtil.createShearCaptcha(100, 40);
		captcha.setGenerator(new RandomGenerator("0123456789", 4));

		String createText = captcha.getCode();
		httpServletRequest.getSession().setAttribute("imgCode", createText);

		captcha.write(httpServletResponse.getOutputStream());
	}
}
