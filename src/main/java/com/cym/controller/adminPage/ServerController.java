package com.cym.controller.adminPage;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cym.model.Server;
import com.cym.utils.BaseController;
import com.cym.utils.JsonResult;

@Controller
@RequestMapping("/adminPage/server")
public class ServerController extends BaseController {

	@RequestMapping("")
	public ModelAndView index(HttpSession httpSession, ModelAndView modelAndView) throws IOException {

		List<Server> servers = mongoHelper.findAll(Server.class);

		modelAndView.addObject("servers", servers);
		modelAndView.setViewName("/adminPage/server/index");
		return modelAndView;
	}
	
	@RequestMapping("addOver")
	@ResponseBody
	public JsonResult addOver(Server server) {
		mongoHelper.insertOrUpdate(server);
		return renderSuccess();
	}

	@RequestMapping("detail")
	@ResponseBody
	public JsonResult detail(String id) {
		Server server = mongoHelper.findById(id, Server.class);
		return renderSuccess(server);
	}

	@RequestMapping("del")
	@ResponseBody
	public JsonResult del(String id) {
		mongoHelper.deleteById(id, Server.class);
		return renderSuccess();
	}


}
