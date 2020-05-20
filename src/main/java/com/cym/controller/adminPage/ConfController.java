package com.cym.controller.adminPage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cym.ext.ConfExt;
import com.cym.ext.ConfFile;
import com.cym.model.Http;
import com.cym.model.Location;
import com.cym.model.Server;
import com.cym.model.Stream;
import com.cym.model.Upstream;
import com.cym.model.UpstreamServer;
import com.cym.service.ConfService;
import com.cym.service.ServerService;
import com.cym.service.SettingService;
import com.cym.service.UpstreamService;
import com.cym.utils.BaseController;
import com.cym.utils.JsonResult;
import com.cym.utils.SystemTool;
import com.github.odiszapc.nginxparser.NgxBlock;
import com.github.odiszapc.nginxparser.NgxConfig;
import com.github.odiszapc.nginxparser.NgxDumper;
import com.github.odiszapc.nginxparser.NgxParam;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.RuntimeUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.ZipUtil;
import cn.hutool.system.SystemUtil;

@Controller
@RequestMapping("/adminPage/conf")
public class ConfController extends BaseController {
	@Autowired
	UpstreamController upstreamController;
	@Autowired
	UpstreamService upstreamService;
	@Autowired
	SettingService settingService;
	@Autowired
	ServerService serverService;
	@Autowired
	ConfService confService;

	@RequestMapping("")
	public ModelAndView index(HttpSession httpSession, ModelAndView modelAndView) throws IOException, SQLException {

//		String confStr = buildConf();
//		modelAndView.addObject("confStr", confStr);

		String nginxPath = settingService.get("nginxPath");
		modelAndView.addObject("nginxPath", nginxPath);

		String decompose = settingService.get("decompose");
		modelAndView.addObject("decompose", decompose);

		modelAndView.setViewName("/adminPage/conf/index");
		return modelAndView;
	}

	@RequestMapping(value = "replace")
	@ResponseBody
	public JsonResult replace(String nginxPath, String nginxContent, String[] subContent, String[] subName) {
		settingService.set("nginxPath", nginxPath);

		if (!FileUtil.exist(nginxPath)) {
			return renderError("目标文件不存在");
		}
		
		try {
			confService.replace(nginxPath, nginxContent,subContent,  subName);
			return renderSuccess("替换成功，原文件已备份");
		} catch (Exception e) {
			e.printStackTrace();

			return renderError("替换失败:" + e.getMessage());
		}

	}

	@RequestMapping(value = "check")
	@ResponseBody
	public JsonResult check(String nginxPath) {
		settingService.set("nginxPath", nginxPath);

		try {
			String rs = null;
			if (SystemTool.isWindows()) {
				File file = new File(nginxPath);
				if (file.exists() && file.getParentFile().getParentFile().exists()) {
					File nginxDir = file.getParentFile().getParentFile();
					rs = RuntimeUtil.execForStr("cmd /c powershell cd " + nginxDir.getPath() + "; ./nginx.exe -t;");

				} else {
					return renderError("nginx目录不存在");
				}
			} else {
				rs = RuntimeUtil.execForStr("nginx -t");
			}

			if (rs.contains("successful")) {
				return renderSuccess("效验成功");
			} else {
				return renderError("效验失败:<br>" + rs.replace("\n", "<br>"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return renderError("效验失败:<br>" + e.getMessage().replace("\n", "<br>"));
		}
	}

	@RequestMapping(value = "reboot")
	@ResponseBody
	public JsonResult reboot(String nginxPath) {
		settingService.set("nginxPath", nginxPath);
		try {
			String rs = null;
			if (SystemTool.isWindows()) {
				File file = new File(nginxPath);
				if (file.exists() && file.getParentFile().getParentFile().exists()) {
					File nginxDir = file.getParentFile().getParentFile();
					rs = RuntimeUtil.execForStr("cmd /c powershell cd " + nginxDir.getPath() + "; ./nginx.exe -s reload;");
				} else {
					return renderError("nginx目录不存在");
				}
			} else {
				rs = RuntimeUtil.execForStr("nginx -s reload");
			}

			if (StrUtil.isEmpty(rs)) {
				return renderSuccess("重启成功");
			} else {
				return renderError("重启失败:<br>" + rs.replace("\n", "<br>"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return renderError("重启失败:<br>" + e.getMessage().replace("\n", "<br>"));
		}
	}

	@RequestMapping(value = "loadConf")
	@ResponseBody
	public JsonResult loadConf() {
		String decompose = settingService.get("decompose");

		ConfExt confExt = confService.buildConf(StrUtil.isNotEmpty(decompose) && decompose.equals("true"));
		return renderSuccess(confExt);
	}

	@RequestMapping(value = "loadOrg")
	@ResponseBody
	public JsonResult loadOrg(String nginxPath) {
		nginxPath = nginxPath.replace("\\", "/");
		
		settingService.set("nginxPath", nginxPath);

		String decompose = settingService.get("decompose");
		ConfExt confExt = confService.buildConf(StrUtil.isNotEmpty(decompose) && decompose.equals("true"));

		if (FileUtil.exist(nginxPath)) {
			String orgStr = FileUtil.readString(nginxPath, Charset.defaultCharset());
			confExt.setConf(orgStr);

			for (ConfFile confFile : confExt.getFileList()) {
				confFile.setConf("");

				String filePath = nginxPath.replace("nginx.conf", "conf.d/" + confFile.getName());
				if (FileUtil.exist(filePath)) {
					confFile.setConf(FileUtil.readString(filePath, Charset.defaultCharset()));
				}
			}

			return renderSuccess(confExt);
		} else {
			return renderError("文件不存在");
		}

	}

	@RequestMapping(value = "decompose")
	@ResponseBody
	public JsonResult decompose(String decompose) {
		settingService.set("decompose", decompose);
		return renderSuccess();
	}
}
