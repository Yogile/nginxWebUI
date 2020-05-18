package com.cym.controller.adminPage;

import java.nio.charset.Charset;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cym.model.Remote;
import com.cym.service.RemoteService;
import com.cym.service.SettingService;
import com.cym.utils.BaseController;
import com.cym.utils.JsonResult;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;

@Controller
@RequestMapping("/adminPage/remote")
public class RemoteController extends BaseController {
	@Autowired
	RemoteService remoteService;
	@Autowired
	SettingService settingService;

	@RequestMapping("")
	public ModelAndView index(HttpSession httpSession, ModelAndView modelAndView) {
		List<Remote> remoteList = sqlHelper.findAll(Remote.class);

		for (Remote remote : remoteList) {
			remote.setStatus(0);
			try {
				String rs = HttpUtil.get(remote.getProtocol() + "://" + remote.getIp() + ":" + remote.getPort() + "/adminPage/remote/alive?creditKey=" + remote.getCreditKey(), 500);
				if (rs.equals("true")) {
					remote.setStatus(1);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		Remote remote = new Remote();
		remote.setIp("本地");
		remoteList.add(0, remote);

		modelAndView.addObject("remoteList", remoteList);
		modelAndView.setViewName("/adminPage/remote/index");
		return modelAndView;
	}

	@RequestMapping("addOver")
	@ResponseBody
	public JsonResult addOver(Remote remote) {

		String key = remoteService.getCreditKey(remote);

		if (StrUtil.isNotEmpty(key)) {
			remote.setCreditKey(key);

			sqlHelper.insertOrUpdate(remote);
			return renderSuccess();
		} else {
			return renderError("远程授权未通过,请检查");
		}

	}

	@RequestMapping("detail")
	@ResponseBody
	public JsonResult detail(String id) {
		return renderSuccess(sqlHelper.findById(id, Remote.class));
	}

	@RequestMapping("del")
	@ResponseBody
	public JsonResult del(String id) {
		sqlHelper.deleteById(id, Remote.class);

		return renderSuccess();
	}

	@RequestMapping("content")
	@ResponseBody
	public JsonResult content(String id) {

		Remote remote = sqlHelper.findById(id, Remote.class);

		String rs = HttpUtil.get(remote.getProtocol() + "://" + remote.getIp() + ":" + remote.getPort() + "/adminPage/remote/readContent?creditKey=" + remote.getCreditKey());

		return renderSuccess(rs);
	}

	@RequestMapping("alive")
	@ResponseBody
	public String alive() {

		return "true";
	}

	@RequestMapping("readContent")
	@ResponseBody
	public String readContent() {

		String nginxPath = settingService.get("nginxPath");

		if (FileUtil.exist(nginxPath)) {
			String orgStr = FileUtil.readString(nginxPath, Charset.defaultCharset());
			return orgStr;
		} else {
			return "文件不存在";
		}

	}

	@RequestMapping("change")
	@ResponseBody
	public JsonResult change(String id, HttpSession httpSession) {
		Remote remote = sqlHelper.findById(id, Remote.class);

		if (remote == null) {
			httpSession.setAttribute("localType", "本地");
			httpSession.removeAttribute("remote");
		} else {
			httpSession.setAttribute("localType", "远程");
			httpSession.setAttribute("remote", remote);
		}

		return renderSuccess();
	}
}
