package com.cym.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.cym.utils.BaseController;

@RequestMapping("")
@Controller
public class MainController extends BaseController{


	@RequestMapping("")
	public ModelAndView index(ModelAndView modelAndView, String keywords) {

		modelAndView.setViewName("redirect:/adminPage/admin/");
		return modelAndView;
	}
}