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

import cn.craccd.sqlite.bean.Sort;
import cn.hutool.core.util.StrUtil;
import cn.hutool.db.Entity;

@Controller
@RequestMapping("/adminPage/server")
public class ServerController extends BaseController {

	@RequestMapping("")
	public ModelAndView index(HttpSession httpSession, ModelAndView modelAndView) throws IOException, SQLException {
		List<Server> servers = nosqlHelper.findAll(Server.class);

		modelAndView.addObject("servers", servers);
		modelAndView.setViewName("/adminPage/server/index");
		return modelAndView;
	}

	@RequestMapping("addOver")
	@ResponseBody
	public JsonResult addOver(Server server) throws SQLException {
//		if(StrUtil.isEmpty(server.getId())) {
//			server.setId(null); 
//		}
		
//		sqliteUtils.use().insertOrUpdate(Entity.parse(server).setTableName("server"), "id");
		nosqlHelper.insertOrUpdate(server);

		return renderSuccess();
	}

	@RequestMapping("detail")
	@ResponseBody
	public JsonResult detail(String id) throws SQLException {
//		Entity where = new Entity("server");
//		where.put("id", id);
//		Entity entity = sqliteUtils.use().get(where);
		return renderSuccess(nosqlHelper.findById(id, Server.class));
	}

	@RequestMapping("del")
	@ResponseBody
	public JsonResult del(String id) throws SQLException {
//		Entity where = new Entity("server");
//		where.put("id", id);
//		sqliteUtils.use().del(where);
		
		nosqlHelper.deleteById(id, Server.class);
		return renderSuccess();
	}

}
