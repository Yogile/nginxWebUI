package com.cym.utils;

import java.util.List;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.RuntimeUtil;

public class RuntimeTool {

	public static String execForOne(String... cmds) throws IORuntimeException {
		List<String> list = RuntimeUtil.execForLines(CharsetUtil.systemCharset(), cmds);

		if (list != null && list.size() > 0) {
			for(String str :list) {
				
			}
			
			return list.get(list.size() - 1).trim();
		} else {
			return null;
		}
	}
}
