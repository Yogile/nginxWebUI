package com.cym.controller.adminPage;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.cym.model.Http;
import com.cym.model.Server;
import com.cym.utils.BaseController;

@Controller
@RequestMapping("/adminPage/conf")
public class ConfController extends BaseController {
	
	@RequestMapping("")
	public ModelAndView index(HttpSession httpSession, ModelAndView modelAndView) throws IOException {
		Http http = mongoHelper.findOneByQuery(new Criteria(), Http.class);
		modelAndView.addObject("http", http);
		List<Server> servers = mongoHelper.findAll(Server.class);
		modelAndView.addObject("servers", servers);
		
		String confStr = buildConf(http, servers);
		modelAndView.addObject("confStr", confStr);
		modelAndView.setViewName("/adminPage/conf/index");
		return modelAndView;
	}

	private String buildConf(Http http, List<Server> servers) {
	
		
		
		return "";
	}
}
