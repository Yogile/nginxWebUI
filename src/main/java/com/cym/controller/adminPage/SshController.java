package com.cym.controller.adminPage;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.cym.utils.BaseController;

@RequestMapping("ssh")
@Controller
public class SshController extends BaseController {

	@RequestMapping("")
	public ModelAndView websshpage(ModelAndView modelAndView) {
		modelAndView.setViewName("/adminPage/ssh/index");
		return modelAndView;
	}
}
