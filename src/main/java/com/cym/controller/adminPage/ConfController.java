package com.cym.controller.adminPage;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cym.model.Http;
import com.cym.model.Server;
import com.cym.service.SettingService;
import com.cym.utils.BaseController;
import com.cym.utils.JsonResult;
import com.github.odiszapc.nginxparser.NgxBlock;
import com.github.odiszapc.nginxparser.NgxConfig;
import com.github.odiszapc.nginxparser.NgxDumper;
import com.github.odiszapc.nginxparser.NgxParam;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.RuntimeUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.db.Entity;

@Controller
@RequestMapping("/adminPage/conf")
public class ConfController extends BaseController {

	@Autowired
	SettingService settingService;

	@RequestMapping("")
	public ModelAndView index(HttpSession httpSession, ModelAndView modelAndView) throws IOException, SQLException {
		Http http = sqlHelper.findOneByQuery(null, null, Http.class);
		modelAndView.addObject("http", http);

		List<Server> servers = sqlHelper.findAll(Server.class);
		modelAndView.addObject("servers", servers);

		String confStr = buildConf(http, servers);
		modelAndView.addObject("confStr", confStr);

		String nginxPath = settingService.get("nginxPath");
		if (StrUtil.isEmpty(nginxPath)) {
			nginxPath = "/etc/nginx/nginx.conf";
			settingService.set("nginxPath", nginxPath);
		}
		modelAndView.addObject("nginxPath", nginxPath);

		modelAndView.setViewName("/adminPage/conf/index");
		return modelAndView;
	}

	private String buildConf(Http http, List<Server> servers) {

		try {
			ClassPathResource resource = new ClassPathResource("nginx.conf");
			InputStream inputStream = resource.getInputStream();

			NgxConfig ngxConfig = NgxConfig.read(inputStream);
			// 设置http
			NgxBlock ngxBlockHttp = ngxConfig.findBlock("http");
			NgxParam ngxParam = new NgxParam();
			ngxParam.addValue("gzip " + http.getGzip());
			ngxBlockHttp.addEntry(ngxParam);

			ngxParam = new NgxParam();
			ngxParam.addValue("client_max_body_size " + http.getClientMaxBodySize());
			ngxBlockHttp.addEntry(ngxParam);

			// 添加server
			for (Server server : servers) {
				NgxBlock ngxBlockServer = new NgxBlock();
				ngxBlockServer.addValue("server");

				// 监听域名
				if (StrUtil.isNotEmpty(server.getServerName())) {
					ngxParam = new NgxParam();
					ngxParam.addValue("server_name " + server.getServerName());
					ngxBlockServer.addEntry(ngxParam);
				}

				// 监听端口
				ngxParam = new NgxParam();
				String value = "listen " + server.getListen();
				if (server.getSsl() == 1) {
					value += " ssl";
				}
				ngxParam.addValue(value);
				ngxBlockServer.addEntry(ngxParam);

				// ssl配置
				if (server.getSsl() == 1) {
					ngxParam = new NgxParam();
					ngxParam.addValue("ssl_certificate " + server.getPem());
					ngxBlockServer.addEntry(ngxParam);

					ngxParam = new NgxParam();
					ngxParam.addValue("ssl_certificate_key " + server.getKey());
					ngxBlockServer.addEntry(ngxParam);
				}

				// http转发配置
				if (server.getType() == 0) {
					// 添加location
					NgxBlock ngxBlockLocation = new NgxBlock();
					ngxBlockLocation.addValue("location");
					ngxBlockLocation.addValue("/");

					ngxParam = new NgxParam();
					ngxParam.addValue("proxy_pass http://" + server.getProxyPass() + ":" + server.getProxyPassPort());
					ngxBlockLocation.addEntry(ngxParam);

					ngxParam = new NgxParam();
					ngxParam.addValue("proxy_set_header Host $host");
					ngxBlockLocation.addEntry(ngxParam);

					ngxParam = new NgxParam();
					ngxParam.addValue("proxy_set_header X-Real-IP $remote_addr");
					ngxBlockLocation.addEntry(ngxParam);

					ngxParam = new NgxParam();
					ngxParam.addValue("proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for");
					ngxBlockLocation.addEntry(ngxParam);

					ngxBlockServer.addEntry(ngxBlockLocation);

				} else if (server.getType() == 1) { // 静态html
					ngxParam = new NgxParam();
					ngxParam.addValue("root " + server.getRoot());
					ngxBlockServer.addEntry(ngxParam);

					ngxParam = new NgxParam();
					ngxParam.addValue("index index.html");
					ngxBlockServer.addEntry(ngxParam);
				}
				ngxBlockHttp.addEntry(ngxBlockServer);

				// https添加80端口重写
				if (server.getSsl() == 1 && server.getRewrite() == 1) {
					ngxBlockServer = new NgxBlock();
					ngxBlockServer.addValue("server");

					ngxParam = new NgxParam();
					ngxParam.addValue("server_name " + server.getServerName());
					ngxBlockServer.addEntry(ngxParam);

					ngxParam = new NgxParam();
					ngxParam.addValue("listen 80");
					ngxBlockServer.addEntry(ngxParam);

					ngxParam = new NgxParam();
					ngxParam.addValue("rewrite ^(.*)$ https://${server_name}$1 permanent");
					ngxBlockServer.addEntry(ngxParam);

					ngxBlockHttp.addEntry(ngxBlockServer);
				}

			}

			return new NgxDumper(ngxConfig).dump();
		} catch (

		Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@RequestMapping(value = "replace")
	@ResponseBody
	public JsonResult replace(String nginxPath,String nginxContent) throws SQLException {
		settingService.set("nginxPath", nginxPath);

		try {
			FileUtil.writeString(nginxContent, nginxPath, Charset.defaultCharset());
			return renderSuccess("替换成功");
		} catch (Exception e) {
			e.printStackTrace();

			return renderError("替换失败:" + e.getMessage());
		}

	}

	@RequestMapping(value = "check")
	@ResponseBody
	public JsonResult check(String nginxPath) throws SQLException {

		try {
			String rs = RuntimeUtil.execForStr("nginx -t");
			if (rs.contains("successful")) {
				return renderSuccess("效验成功");
			} else {
				return renderError("效验失败:" + rs);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return renderError("效验失败:" + e.getMessage());
		}
	}

	@RequestMapping(value = "reboot")
	@ResponseBody
	public JsonResult reboot(String nginxPath) throws SQLException {
		try {
			String rs = RuntimeUtil.execForStr("nginx -s reload");
			if (rs.trim().equals("")) {
				return renderSuccess("重启成功");
			} else {
				return renderError("重启失败:" + rs);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return renderError("重启失败:" + e.getMessage());
		}
	}

}
