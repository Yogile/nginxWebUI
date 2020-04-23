package com.cym.controller.adminPage;

import java.io.IOException;

import javax.servlet.http.HttpSession;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cym.model.Http;
import com.cym.utils.BaseController;
import com.cym.utils.JsonResult;

@Controller
@RequestMapping("/adminPage/http")
public class HttpController extends BaseController {

	@RequestMapping("")
	public ModelAndView index(HttpSession httpSession, ModelAndView modelAndView) throws IOException {
		Http http = mongoHelper.findOneByQuery(new Criteria(), Http.class);
		if (http == null) {
			http = new Http();
			http.setGzip("on");
			http.setClientMaxBodySize("512M");
			mongoHelper.insertOrUpdate(http);
		}

		modelAndView.addObject("http", http);
		modelAndView.setViewName("/adminPage/http/index");
		return modelAndView;
	}

	@RequestMapping(value = "addOver")
	@ResponseBody
	public JsonResult addOver(String name, String value) {
		Http http = mongoHelper.findOneByQuery(new Criteria(), Http.class);
		if (name.equals("gzip")) {
			http.setGzip(value);
		} else if (name.equals("clientMaxBodySize")) {
			http.setClientMaxBodySize(value);
		}

		mongoHelper.updateById(http);

		return renderSuccess();

	}
}
