package com.cym.controller.api;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cym.model.Admin;
import com.cym.service.AdminService;
import com.cym.service.CreditService;
import com.cym.utils.BaseController;
import com.cym.utils.JsonResult;

import io.swagger.annotations.Api;

@Api(tags = "获取token")
@RestController
@RequestMapping("/api/token")
public class TokenController  extends BaseController{
	@Autowired
	AdminService adminService;
	@Autowired
	CreditService creditService;
	
	@RequestMapping("getToken")
	@ResponseBody
	public JsonResult getToken(String name, String pass) {

		// 用户名密码
		Admin admin = adminService.login(name, pass);
		if (admin == null || !admin.getApi()) {
			return renderError(m.get("loginStr.backError2")); // 用户名密码错误
		}

		Map<String, String> map = new HashMap<String, String>();
		map.put("token", creditService.make());

		return renderSuccess(map);
	}
}
