package com.cym.controller.adminPage;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.cym.model.Http;
import com.cym.model.Server;
import com.cym.utils.BaseController;
import com.github.odiszapc.nginxparser.NgxBlock;
import com.github.odiszapc.nginxparser.NgxConfig;
import com.github.odiszapc.nginxparser.NgxDumper;
import com.github.odiszapc.nginxparser.NgxParam;

import cn.hutool.db.Entity;

@Controller
@RequestMapping("/adminPage/conf")
public class ConfController extends BaseController {

	@RequestMapping("")
	public ModelAndView index(HttpSession httpSession, ModelAndView modelAndView) throws IOException, SQLException {
		Http http = sqliteUtils.use().findAll("http").get(0).toBean(Http.class);
		modelAndView.addObject("http", http);
		List<Server> servers = sqliteUtils.use().findAll(Entity.create("server"), Server.class);
		modelAndView.addObject("servers", servers);

		String confStr = buildConf(http, servers);
		modelAndView.addObject("confStr", confStr);
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

				ngxParam = new NgxParam();
				ngxParam.addValue("server_name " + server.getServerName());
				ngxBlockServer.addEntry(ngxParam);

				ngxParam = new NgxParam();
				String value = "listen " + server.getListen();
				if (server.getSsl() == 1) {
					value += " ssl";
				}
				ngxParam.addValue(value);
				ngxBlockServer.addEntry(ngxParam);

				if (server.getSsl() == 1) {
					ngxParam = new NgxParam();
					ngxParam.addValue("ssl_certificate " + server.getPem());
					ngxBlockServer.addEntry(ngxParam);

					ngxParam = new NgxParam();
					ngxParam.addValue("ssl_certificate_key " + server.getKey());
					ngxBlockServer.addEntry(ngxParam);
				}

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

				ngxBlockHttp.addEntry(ngxBlockServer);

				// ssl添加80端口转跳
				if (server.getSsl() == 1) {
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
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

}
