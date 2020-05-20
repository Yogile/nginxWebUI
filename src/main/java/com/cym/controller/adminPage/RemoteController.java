package com.cym.controller.adminPage;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cym.ext.AsycPack;
import com.cym.model.Remote;
import com.cym.service.ConfService;
import com.cym.service.RemoteService;
import com.cym.service.SettingService;
import com.cym.utils.BaseController;
import com.cym.utils.JsonResult;
import com.cym.utils.SystemTool;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;

@Controller
@RequestMapping("/adminPage/remote")
public class RemoteController extends BaseController {
	@Autowired
	RemoteService remoteService;
	@Autowired
	SettingService settingService;
	@Autowired
	ConfService confService;
	@Value("${project.version}")
	String version;
	@Value("${server.port}")
	Integer port;

	@RequestMapping("")
	public ModelAndView index(HttpSession httpSession, ModelAndView modelAndView) {
		List<Remote> remoteList = sqlHelper.findAll(Remote.class);

		for (Remote remote : remoteList) {
			remote.setStatus(0);
			try {
				String version = HttpUtil.get(remote.getProtocol() + "://" + remote.getIp() + ":" + remote.getPort() + "/adminPage/remote/version?creditKey=" + remote.getCreditKey(), 500);
				if (StrUtil.isNotEmpty(version)) {
					remote.setVersion(version);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		Remote remote = new Remote();
		remote.setIp("本地");
		remote.setVersion(version);
		remote.setPort(port);
		remote.setSystem(SystemTool.getSystem());

		remoteList.add(0, remote);

		modelAndView.addObject("remoteList", remoteList);
		modelAndView.setViewName("/adminPage/remote/index");

		return modelAndView;
	}

	@RequestMapping("asyc")
	@ResponseBody
	public JsonResult asyc(String id) {
		Remote remoteFrom = sqlHelper.findById(id, Remote.class);
		String json = null;
		String system = null;
		if (StrUtil.isEmpty(id)) {
			// 本地
			json = getAsycPack();
			system = SystemTool.getSystem();
		} else {
			// 远程
			json = HttpUtil.get(remoteFrom.getProtocol() + "://" + remoteFrom.getIp() + ":" + remoteFrom.getPort() + "/adminPage/remote/getAsycPack?creditKey=" + remoteFrom.getCreditKey(), 500);
			system = remoteFrom.getSystem();
		}

		List<Remote> remoteList = sqlHelper.findAll(Remote.class);
		for (Remote remoteTo : remoteList) {
			if (remoteTo.getSystem().equals(system)) {
				try {
					String version = HttpUtil.get(remoteTo.getProtocol() + "://" + remoteTo.getIp() + ":" + remoteTo.getPort() + "/adminPage/remote/version?creditKey=" + remoteTo.getCreditKey(), 500);
					if (StrUtil.isNotEmpty(version)) {
						// 在线
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("json", json);
						HttpUtil.post(remoteTo.getProtocol() + "://" + remoteTo.getIp() + ":" + remoteTo.getPort() + "/adminPage/remote/setAsycPack?creditKey=" + remoteTo.getCreditKey(), map);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		return renderSuccess();
	}

	@RequestMapping("getAsycPack")
	@ResponseBody
	public String getAsycPack() {
		AsycPack asycPack = confService.getAsycPack();

		return JSONUtil.toJsonStr(asycPack);
	}

	@RequestMapping("setAsycPack")
	@ResponseBody
	public JsonResult setAsycPack(String json) {
		AsycPack asycPack = JSONUtil.toBean(json, AsycPack.class);

		confService.setAsycPack(asycPack);

		return renderSuccess();
	}

	@RequestMapping("addOver")
	@ResponseBody
	public JsonResult addOver(Remote remote) {

		remoteService.getCreditKey(remote);

		if (StrUtil.isNotEmpty(remote.getCreditKey())) {
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

	@RequestMapping("version")
	@ResponseBody
	public String version() {
		return version;
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
