package com.cym.controller.adminPage;

import java.io.IOException;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.cym.model.Http;
import com.cym.utils.BaseController;
import com.github.odiszapc.nginxparser.NgxBlock;
import com.github.odiszapc.nginxparser.NgxConfig;

@Controller
@RequestMapping("/adminPage/http")
public class HttpController extends BaseController {
	@Value("${custom.nginx}")
	String nginx;
	// http项
	Http http;
	
	@RequestMapping("")
	public ModelAndView index(HttpSession httpSession, ModelAndView modelAndView) throws IOException {
		// 根据路径读取整个配置文件
		NgxConfig ngxConfig = NgxConfig.read(nginx);

		// 找到http{} 因为不是单独的一条数据所以使用findBlock
		NgxBlock servers = ngxConfig.findBlock("http").findBlock("server");

		

		modelAndView.addObject("http", http);
		modelAndView.setViewName("/adminPage/nginx/http");
		return modelAndView;
	}

}
