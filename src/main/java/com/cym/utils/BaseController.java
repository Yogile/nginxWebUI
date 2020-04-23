package com.cym.utils;

import javax.servlet.http.HttpSession;

import com.cym.model.Admin;

/**
 * Author: D.Yang Email: koyangslash@gmail.com Date: 16/10/9 Time: 下午1:37
 * Describe: 基础控制器
 */
public class BaseController {
	
	
	protected JsonResult renderError() {
		JsonResult result = new JsonResult();
		result.setSuccess(false);
		result.setStatus("500");
		return result;
	}

	protected JsonResult renderAuthError() {
		JsonResult result = new JsonResult();
		result.setSuccess(false);
		result.setStatus("401");
		return result;
	}

	protected JsonResult renderError(String msg) {
		JsonResult result = renderError();
		result.setMsg(msg);
		return result;
	}

	protected JsonResult renderSuccess() {
		JsonResult result = new JsonResult();
		result.setSuccess(true);
		result.setStatus("200");
		return result;
	}


	protected JsonResult renderSuccess(Object obj) {
		JsonResult result = renderSuccess();
		result.setObj(obj);
		return result;
	}

	protected Admin getLoginAdmin(HttpSession httpSession) {
		return (Admin) httpSession.getAttribute("admin");
	}



	

	
}
