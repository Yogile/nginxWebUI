package com.cym.controller;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.cym.config.InitConfig;
import com.cym.config.VersionConfig;
import com.cym.model.Remote;
import com.cym.model.Version;
import com.cym.utils.AsyncUtils;
import com.cym.utils.BaseController;
import com.cym.utils.JsonResult;
import com.cym.utils.SystemTool;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;

@RequestMapping("")
@Controller
public class MainController extends BaseController {
	@Autowired
	AsyncUtils asyncUtils;



	@RequestMapping("")
	public ModelAndView index(ModelAndView modelAndView, String keywords) {

		modelAndView.setViewName("redirect:/adminPage/admin/");
		return modelAndView;
	}

	@ResponseBody
	@RequestMapping("/upload")
	public JsonResult upload(@RequestParam("file") MultipartFile file, HttpSession httpSession) {
		try {
			File temp = new File(FileUtil.getTmpDir() + "/" + file.getOriginalFilename());
			file.transferTo(temp);

			// 移动文件
			File dest = new File(InitConfig.home + "cert/" + file.getOriginalFilename());
			FileUtil.move(temp, dest, true);

			String localType = (String) httpSession.getAttribute("localType");
			if ("远程".equals(localType)) {
				Remote remote = (Remote) httpSession.getAttribute("remote");

				HashMap<String, Object> paramMap = new HashMap<>();
				paramMap.put("file", dest);

				String rs = HttpUtil.post(remote.getProtocol() + "://" + remote.getIp() + ":" + remote.getPort() + "/upload", paramMap);
				JsonResult jsonResult = JSONUtil.toBean(rs, JsonResult.class);
				FileUtil.del(dest);
				return jsonResult;
			}

			return renderSuccess(dest.getPath().replace("\\", "/"));
		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
		}

		return renderError();
	}

	@ResponseBody
	@RequestMapping("/autoUpdate")
	public JsonResult autoUpdate(String url) {
		if (!SystemTool.isLinux()) {
			return renderError("只有运行在Linux才能进行自动更新");
		}

		ApplicationHome home = new ApplicationHome(getClass());
		File jar = home.getSource();
		String[] names = url.split("/");
		String name = names[names.length - 1];
		String path = jar.getParent() + "/" + name;

		HttpUtil.downloadFile(url, path);
		System.out.println(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
		asyncUtils.run(path);
		System.out.println(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
		return renderSuccess();
	}


}