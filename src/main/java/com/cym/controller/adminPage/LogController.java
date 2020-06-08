package com.cym.controller.adminPage;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cym.config.InitConfig;
import com.cym.ext.DataGroup;
import com.cym.model.Log;
import com.cym.model.LogInfo;
import com.cym.service.LogInfoService;
import com.cym.service.SettingService;
import com.cym.utils.BaseController;
import com.cym.utils.JsonResult;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;

@Controller
@RequestMapping("/adminPage/log")
public class LogController extends BaseController {
	@Autowired
	SettingService settingService;
	@Autowired
	LogInfoService logInfoService;
	@RequestMapping("")
	public ModelAndView index(HttpSession httpSession, ModelAndView modelAndView) {
		List<Log> logList = getLogList();

		CollectionUtil.sort(logList, new Comparator<Log>() {

			@Override
			public int compare(Log o1, Log o2) {
				return StrUtil.compare(o2.getTime(), o1.getTime(), true);
			}
		});

		modelAndView.addObject("logList", logList);
		modelAndView.setViewName("/adminPage/log/index");
		return modelAndView;
	}

	private List<Log> getLogList() {
		List<Log> list = new ArrayList<Log>();

		File dir = new File(InitConfig.home + "log/");

		File[] fileList = dir.listFiles();
		for (File file : fileList) {
			if (file.getName().contains("access") && file.getName().contains(".zip")) {
				Log log = new Log();
				log.setPath(file.getPath().replace("\\", "/"));
				DateTime date = DateUtil.parse(file.getName().replace("access.", "").replace(".zip", ""), "yyyy-MM-dd_HH-mm-ss");
				log.setTime(DateUtil.format(date, "yyyy-MM-dd HH:mm:ss"));

				list.add(log);
			}
		}

		return list;
	}

	@RequestMapping("del")
	@ResponseBody
	public JsonResult del(String path) {
		FileUtil.del(path);
		return renderSuccess();
	}
	
	
	@RequestMapping("content")
	@ResponseBody
	public JsonResult content(String path) {
		DataGroup dataGroup = logInfoService.findByPath(path);
		return renderSuccess(dataGroup);
		
	}
	
	

}
