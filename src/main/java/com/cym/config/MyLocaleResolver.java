package com.cym.config;

import java.util.Locale;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;
import org.springframework.web.servlet.LocaleResolver;

public class MyLocaleResolver implements LocaleResolver {

	@Override
	public Locale resolveLocale(HttpServletRequest httpServletRequest) {
		String l = httpServletRequest.getParameter("l");
		Cookie[] cookies = httpServletRequest.getCookies();

		if (!StringUtils.isEmpty(l)) {
			String[] split = l.split("_");
			return new Locale(split[0], split[1]);
		} else if (cookies != null && cookies.length > 0) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("l")) {
					String[] split = cookie.getName().split("_");
					return new Locale(split[0], split[1]);
				}
			}
		}

		return Locale.getDefault();
	}

	@Override
	public void setLocale(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Locale locale) {

	}
}