package com.cym.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.cym.controller.adminPage.UpstreamController;
import com.cym.ext.ConfExt;
import com.cym.ext.ConfFile;
import com.cym.model.Http;
import com.cym.model.Location;
import com.cym.model.Server;
import com.cym.model.Stream;
import com.cym.model.Upstream;
import com.cym.model.UpstreamServer;
import com.github.odiszapc.nginxparser.NgxBlock;
import com.github.odiszapc.nginxparser.NgxConfig;
import com.github.odiszapc.nginxparser.NgxDumper;
import com.github.odiszapc.nginxparser.NgxParam;

import cn.craccd.sqlHelper.utils.SqlHelper;
import cn.hutool.core.util.RuntimeUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.system.SystemUtil;

@Service
public class ConfService {
	@Autowired
	UpstreamController upstreamController;
	@Autowired
	UpstreamService upstreamService;
	@Autowired
	SettingService settingService;
	@Autowired
	ServerService serverService;
	@Autowired
	SqlHelper sqlHelper;

	public ConfExt buildConf(Boolean decompose) {
		ConfExt confExt = new ConfExt();
		confExt.setFileList(new ArrayList<ConfFile>());

		String nginxPath = settingService.get("nginxPath");
		try {
			ClassPathResource resource = new ClassPathResource("nginxOrg.conf");
			InputStream inputStream = resource.getInputStream();

			NgxConfig ngxConfig = NgxConfig.read(inputStream);

			// 获取http
			List<Http> httpList = sqlHelper.findAll(Http.class);
			NgxBlock ngxBlockHttp = ngxConfig.findBlock("http");
			for (Http http : httpList) {
				NgxParam ngxParam = new NgxParam();
				ngxParam.addValue(http.getName() + " " + http.getValue());
				ngxBlockHttp.addEntry(ngxParam);
			}

			boolean hasHttp = false;
			// 添加upstream
			NgxParam ngxParam = null;
			List<Upstream> upstreams = upstreamService.getListByProxyType(0);

			for (Upstream upstream : upstreams) {
				NgxBlock ngxBlockServer = new NgxBlock();
				ngxBlockServer.addValue("upstream " + upstream.getName());

				if (StrUtil.isNotEmpty(upstream.getTactics())) {
					ngxParam = new NgxParam();
					ngxParam.addValue(upstream.getTactics());
					ngxBlockServer.addEntry(ngxParam);
				}

				List<UpstreamServer> upstreamServers = upstreamService.getUpstreamServers(upstream.getId());
				for (UpstreamServer upstreamServer : upstreamServers) {
					ngxParam = new NgxParam();
					ngxParam.addValue("server " + upstreamController.buildStr(upstreamServer, upstream.getProxyType()));
					ngxBlockServer.addEntry(ngxParam);
				}
				hasHttp = true;

				if (decompose) {
					addConfFile(confExt, "upstreams." + upstream.getName() + ".conf", ngxBlockServer);

					ngxParam = new NgxParam();
					ngxParam.addValue("include " + nginxPath.replace("nginx.conf", "conf.d/upstreams." + upstream.getName() + ".conf"));
					ngxBlockHttp.addEntry(ngxParam);

				} else {
					ngxBlockHttp.addEntry(ngxBlockServer);
				}

			}

			// 添加server
			List<Server> servers = serverService.getListByProxyType(0);
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

					ngxParam = new NgxParam();
					ngxParam.addValue("ssl_protocols TLSv1 TLSv1.1 TLSv1.2 TLSv1.3");
					ngxBlockServer.addEntry(ngxParam);

				}

				List<Location> locationList = serverService.getLocationByServerId(server.getId());

				// http转发配置
				for (Location location : locationList) {
					if (location.getType() == 0 || location.getType() == 2) { // http或负载均衡
						// 添加location
						NgxBlock ngxBlockLocation = new NgxBlock();
						ngxBlockLocation.addValue("location");
						ngxBlockLocation.addValue(location.getPath());

						if (location.getType() == 0) {
							ngxParam = new NgxParam();
							ngxParam.addValue("proxy_pass " + location.getValue());
							ngxBlockLocation.addEntry(ngxParam);
						} else if (location.getType() == 2) {
							Upstream upstream = sqlHelper.findById(location.getUpstreamId(), Upstream.class);
							if (upstream != null) {
								ngxParam = new NgxParam();
								ngxParam.addValue("proxy_pass http://" + upstream.getName());
								ngxBlockLocation.addEntry(ngxParam);
							}
						}

						ngxParam = new NgxParam();
						ngxParam.addValue("proxy_set_header Host $host");
						ngxBlockLocation.addEntry(ngxParam);

						ngxParam = new NgxParam();
						ngxParam.addValue("proxy_set_header X-Real-IP $remote_addr");
						ngxBlockLocation.addEntry(ngxParam);

						ngxParam = new NgxParam();
						ngxParam.addValue("proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for");
						ngxBlockLocation.addEntry(ngxParam);

						ngxParam = new NgxParam();
						ngxParam.addValue("proxy_set_header X-Forwarded-Proto $scheme");
						ngxBlockLocation.addEntry(ngxParam);

						ngxBlockServer.addEntry(ngxBlockLocation);

					} else if (location.getType() == 1) { // 静态html
						NgxBlock ngxBlockLocation = new NgxBlock();
						ngxBlockLocation.addValue("location");
						ngxBlockLocation.addValue(location.getPath());

						if (location.getPath().equals("/")) {
							ngxParam = new NgxParam();
							ngxParam.addValue("root " + location.getValue());
							ngxBlockLocation.addEntry(ngxParam);
						} else {
							ngxParam = new NgxParam();
							ngxParam.addValue("alias " + location.getValue());
							ngxBlockLocation.addEntry(ngxParam);
						}

						ngxParam = new NgxParam();
						ngxParam.addValue("index index.html");
						ngxBlockLocation.addEntry(ngxParam);

						ngxBlockServer.addEntry(ngxBlockLocation);
					}
				}
				hasHttp = true;

				// 是否需要分解
				if (decompose) {
					addConfFile(confExt, server.getServerName() + ".conf", ngxBlockServer);
					
					ngxParam = new NgxParam();
					ngxParam.addValue("include " + nginxPath.replace("nginx.conf", "conf.d/" + server.getServerName() + ".conf"));
					ngxBlockHttp.addEntry(ngxParam);

				} else {
					ngxBlockHttp.addEntry(ngxBlockServer);
				}

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

					hasHttp = true;

					// 是否需要分解
					if (decompose) {
						addConfFile(confExt, server.getServerName() + ".conf", ngxBlockServer);
						
					} else {
						ngxBlockHttp.addEntry(ngxBlockServer);
					}
				}

			}
			if (!hasHttp) {
				ngxConfig.remove(ngxBlockHttp);
			}

			// TCP转发
			// 创建stream
			List<Stream> streamList = sqlHelper.findAll(Stream.class);
			boolean hasStream = false;
			NgxBlock ngxBlockStream = ngxConfig.findBlock("stream");
			for (Stream stream : streamList) {
				ngxParam = new NgxParam();
				ngxParam.addValue(stream.getName() + " " + stream.getValue());
				ngxBlockStream.addEntry(ngxParam);

				hasStream = true;
			}

			// 添加upstream
			upstreams = upstreamService.getListByProxyType(1);
			for (Upstream upstream : upstreams) {
				NgxBlock ngxBlockServer = new NgxBlock();
				ngxBlockServer.addValue("upstream " + upstream.getName());

				if (StrUtil.isNotEmpty(upstream.getTactics())) {
					ngxParam = new NgxParam();
					ngxParam.addValue(upstream.getTactics());
					ngxBlockServer.addEntry(ngxParam);
				}

				List<UpstreamServer> upstreamServers = upstreamService.getUpstreamServers(upstream.getId());
				for (UpstreamServer upstreamServer : upstreamServers) {
					ngxParam = new NgxParam();
					ngxParam.addValue("server " + upstreamController.buildStr(upstreamServer, upstream.getProxyType()));
					ngxBlockServer.addEntry(ngxParam);
				}

				if (decompose) {
					addConfFile(confExt, "upstreams." + upstream.getName() + ".conf", ngxBlockServer);
					
					ngxParam = new NgxParam();
					ngxParam.addValue("include " + nginxPath.replace("nginx.conf", "conf.d/upstreams." + upstream.getName() + ".conf"));
					ngxBlockStream.addEntry(ngxParam);
				} else {
					ngxBlockStream.addEntry(ngxBlockServer);
				}

				hasStream = true;
			}

			// 添加server
			servers = serverService.getListByProxyType(1);
			for (Server server : servers) {

				NgxBlock ngxBlockServer = new NgxBlock();
				ngxBlockServer.addValue("server");

				// 监听端口
				ngxParam = new NgxParam();
				ngxParam.addValue("listen " + server.getListen());
				ngxBlockServer.addEntry(ngxParam);

				// 指向负载均衡
				Upstream upstream = sqlHelper.findById(server.getProxyUpstreamId(), Upstream.class);
				if (upstream != null) {
					ngxParam = new NgxParam();
					ngxParam.addValue("proxy_pass " + upstream.getName());
					ngxBlockServer.addEntry(ngxParam);
				}

				// 其他一些参数
				ngxParam = new NgxParam();
				ngxParam.addValue("proxy_connect_timeout 1s");
				ngxBlockServer.addEntry(ngxParam);

				ngxParam = new NgxParam();
				ngxParam.addValue("proxy_timeout 3s");
				ngxBlockServer.addEntry(ngxParam);

				if (decompose) {
					addConfFile(confExt, "stream." + server.getListen() + ".conf", ngxBlockServer);
					
					ngxParam = new NgxParam();
					ngxParam.addValue("include " + nginxPath.replace("nginx.conf", "conf.d/stream." + server.getListen() + ".conf"));
					ngxBlockStream.addEntry(ngxParam);
				} else {
					ngxBlockStream.addEntry(ngxBlockServer);
				}

				hasStream = true;
			}

			if (!hasStream) {
				ngxConfig.remove(ngxBlockStream);
			}

			String conf = new NgxDumper(ngxConfig).dump();

			// 装载ngx_stream_module模块
			if (hasStream && !SystemUtil.get(SystemUtil.OS_NAME).toLowerCase().contains("win")) {
				String module = settingService.get("ngx_stream_module");
				if (StrUtil.isEmpty(module)) {
					module = RuntimeUtil.execForStr("find / -name ngx_stream_module.so").trim();
				}

				if (StrUtil.isNotEmpty(module)) {
					settingService.set("ngx_stream_module", module);
					conf = "load_module " + module + ";\n" + conf;
				}

			}

			confExt.setConf(conf);

			return confExt;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	private void addConfFile(ConfExt confExt, String name, NgxBlock ngxBlockServer) {
		boolean hasSameName = false;
		for (ConfFile confFile : confExt.getFileList()) {
			if (confFile.getName().equals(name)) {
				confFile.setConf(confFile.getConf() + "\n" + buildStr(ngxBlockServer));
				hasSameName = true;
			}
		}

		if (!hasSameName) {
			ConfFile confFile = new ConfFile();
			confFile.setName(name);
			confFile.setConf(buildStr(ngxBlockServer));
			confExt.getFileList().add(confFile);
		}
	}

	private String buildStr(NgxBlock ngxBlockServer) {

		NgxConfig ngxConfig = new NgxConfig();
		ngxConfig.addEntry(ngxBlockServer);

		return new NgxDumper(ngxConfig).dump();
	}

}
