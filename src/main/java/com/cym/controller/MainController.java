package com.cym.controller;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.cym.model.Remote;
import com.cym.utils.BaseController;
import com.cym.utils.JsonResult;

import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

@RequestMapping("")
@Controller
public class MainController extends BaseController {

	@RequestMapping("")
	public ModelAndView index(ModelAndView modelAndView, String keywords) {

		modelAndView.setViewName("redirect:/adminPage/admin/");
		return modelAndView;
	}

	@ResponseBody
	@RequestMapping("/upload")
	public JsonResult upload(@RequestParam("file") MultipartFile file, HttpSession httpSession) {
		String path = FileUtil.getUserHomePath() + File.separator + System.currentTimeMillis() + "." + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);

		File dest = new File(path);
		// 保存文件
		try {
			file.transferTo(dest);
			String localType = (String) httpSession.getAttribute("localType");
			if ("远程".equals(localType)) {
				Remote remote = (Remote) httpSession.getAttribute("remote");

				HashMap<String, Object> paramMap = new HashMap<>();
				paramMap.put("file", dest);

				String rs = HttpUtil.post("http://" + remote.getIp() + ":" + remote.getPort() + "/upload", paramMap);
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
	
	

	
}