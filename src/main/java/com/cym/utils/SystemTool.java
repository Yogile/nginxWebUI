package com.cym.utils;

import cn.hutool.system.SystemUtil;

public class SystemTool {
	
	
	public static String getSystem() {

		if (SystemUtil.get(SystemUtil.OS_NAME).toLowerCase().contains("win")) {
			return "Windows";
		} else {
			return "Linux";
		}

	}
	
	
	public static Boolean isWindows() {
		return getSystem().equals("Windows");
	}
	
	public static Boolean isLinux() {
		return getSystem().equals("Linux");
	}
	
	
}
