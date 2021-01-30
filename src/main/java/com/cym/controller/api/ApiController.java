package com.cym.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cym.ext.LocationReq;
import com.cym.ext.ServerReq;
import com.cym.service.AdminService;
import com.cym.utils.BaseController;
import com.cym.utils.JsonResult;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "接口")
@RestController
@RequestMapping("/api/remote")
public class ApiController extends BaseController {
	@Autowired
	AdminService adminService;

	@ApiOperation("添加Http")
	@PostMapping("addHttp")
	public JsonResult addHttp() {
		return renderSuccess();
	}

	@ApiOperation("添加Basic")
	@PostMapping("addBasic")
	public JsonResult addBasic() {
		return renderSuccess();
	}

	@ApiOperation("添加Server")
	@PostMapping("addServer")
	public JsonResult addServer(ServerReq serverReq) {
		return renderSuccess();
	}

	@ApiOperation("添加Location")
	@PostMapping("addLocation")
	public JsonResult addLocation(LocationReq locationReq) {
		return renderSuccess();
	}

	@ApiOperation("添加Stream")
	@PostMapping("addStream")
	public JsonResult addStream() {
		return renderSuccess();
	}

	@ApiOperation("添加Upstream")
	@PostMapping("addUpstream")
	public JsonResult addUpstream() {
		return renderSuccess();
	}

	@ApiOperation("添加UpstreamServer")
	@PostMapping("addUpstreamServer")
	public JsonResult addUpstreamServer() {
		return renderSuccess();
	}
}
