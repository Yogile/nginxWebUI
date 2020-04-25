package com.cym.controller.adminPage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cym.model.Server;
import com.cym.utils.BaseController;
import com.cym.utils.JsonResult;

import cn.hutool.db.Db;
import cn.hutool.db.Entity;

@Controller
@RequestMapping("/adminPage/server")
public class ServerController extends BaseController {

	@RequestMapping("")
	public ModelAndView index(HttpSession httpSession, ModelAndView modelAndView) throws IOException, SQLException {

		List<Server> servers = Db.use().findAll(new Entity("server"), Server.class);

		modelAndView.addObject("servers", servers);
		modelAndView.setViewName("/adminPage/server/index");
		return modelAndView;
	}

	@RequestMapping("addOver")
	@ResponseBody
	public JsonResult addOver(Server server) throws SQLException {
		Db.use().insertOrUpdate(Entity.parse(server).setTableName("server"), "id");

		return renderSuccess();
	}

	@RequestMapping("detail")
	@ResponseBody
	public JsonResult detail(String id) throws SQLException {
		Entity where = new Entity("server");
		where.put("id", id);
		Entity entity = Db.use().get(where);
		return renderSuccess(entity.toBean(Server.class));
	}

	@RequestMapping("del")
	@ResponseBody
	public JsonResult del(String id) throws SQLException {
		Entity where = new Entity("server");
		where.put("id", id);
		Db.use().del(where);
		return renderSuccess();
	}

}
