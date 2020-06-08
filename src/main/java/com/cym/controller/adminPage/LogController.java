package com.cym.controller.adminPage;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cym.model.Log;
import com.cym.service.LogService;
import com.cym.service.SettingService;
import com.cym.utils.BaseController;
import com.cym.utils.JsonResult;

import cn.craccd.sqlHelper.bean.Page;

@Controller
@RequestMapping("/adminPage/log")
public class LogController extends BaseController {
	@Autowired
	SettingService settingService;
	@Autowired
	LogService logService;
	@RequestMapping("")
	public ModelAndView index(HttpSession httpSession, ModelAndView modelAndView, Page page) {
		page = logService.search(page);

		modelAndView.addObject("page", page);
		modelAndView.setViewName("/adminPage/log/index");
		return modelAndView;
	}

//	private List<Log> getLogList() {
//		List<Log> list = new ArrayList<Log>();
//
//		File dir = new File(InitConfig.home + "log/");
//
//		File[] fileList = dir.listFiles();
//		for (File file : fileList) {
//			if (file.getName().contains("access") && file.getName().contains(".zip")) {
//				Log log = new Log();
//				log.setPath(file.getPath().replace("\\", "/"));
//				DateTime date = DateUtil.parse(file.getName().replace("access.", "").replace(".zip", ""), "yyyy-MM-dd_HH-mm-ss");
//				log.setTime(DateUtil.format(date, "yyyy-MM-dd HH:mm:ss"));
//
//				list.add(log);
//			}
//		}
//
//		return list;
//	}

	
	
	@RequestMapping("delAll")
	@ResponseBody
	public JsonResult delAll(String id) {
		sqlHelper.deleteByQuery(null, Log.class); 
		return renderSuccess();
	}
	
	@RequestMapping("detail")
	@ResponseBody
	public JsonResult detail(String id) {
		Log log = sqlHelper.findById(id, Log.class);
		return renderSuccess(log);
		
	}
	
	

}
