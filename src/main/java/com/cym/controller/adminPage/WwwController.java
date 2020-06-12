package com.cym.controller.adminPage;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cym.config.InitConfig;
import com.cym.model.Www;
import com.cym.service.WwwService;
import com.cym.utils.BaseController;
import com.cym.utils.JsonResult;

import cn.craccd.sqlHelper.bean.Sort;
import cn.craccd.sqlHelper.bean.Sort.Direction;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ZipUtil;


@RequestMapping("/adminPage/www")
@Controller
public class WwwController extends BaseController {
	@Autowired
	WwwService wwwService;
	
	@RequestMapping("")
	public ModelAndView index(HttpSession httpSession, ModelAndView modelAndView) {

		modelAndView.addObject("list", sqlHelper.findAll(new Sort("dir", Direction.ASC), Www.class));
		modelAndView.setViewName("/adminPage/www/index");
		return modelAndView;
	}

	@RequestMapping("addOver")
	@ResponseBody
	public JsonResult addOver(Www www) {
		if(wwwService.hasName(www.getName())) {
			return renderError("名称重复");
		}
		
		String dir = InitConfig.home + "/wwww/" + www.getName();
		ZipUtil.unzip(www.getDir(), dir);
		FileUtil.del(www.getDir());
		www.setDir(dir);
		
		sqlHelper.insertOrUpdate(www);
		return renderSuccess();
	}

	@RequestMapping("del")
	@ResponseBody
	public JsonResult del(String id) {
		Www www = sqlHelper.findById(id, Www.class);
		sqlHelper.deleteById(id, Www.class);
		FileUtil.del(www.getDir());

		return renderSuccess();
	}

}
