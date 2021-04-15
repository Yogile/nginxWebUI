package com.cym.controller.api;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cym.config.InitConfig;
import com.cym.controller.adminPage.CertController;
import com.cym.model.Cert;
import com.cym.service.CertService;
import com.cym.utils.BaseController;
import com.cym.utils.JsonResult;

import cn.hutool.core.io.FileUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(tags = "证书接口")
@RestController
@RequestMapping("/api/cert")
public class CertApiController  extends BaseController{
	
	@Autowired
	CertController certController;
	@Autowired
	CertService certService;
	
	@SuppressWarnings("unchecked")
	@ApiOperation("获取证书分页列表")
	@PostMapping("getPage")
	public JsonResult<List<Cert>> getPage(@ApiParam("当前页数(从1开始)") @RequestParam(defaultValue = "1") Integer current, //
			@ApiParam("每页数量(默认为10)") @RequestParam(defaultValue = "10") Integer limit, //
			@ApiParam("查询关键字") String keywords) {
		List<Cert> certs = sqlHelper.findAll(Cert.class);

		return renderSuccess(certs);
	}

	
	@RequestMapping("addOver")
	@ResponseBody
	public JsonResult<Cert> addOver(Cert cert) {
		if (certService.hasSame(cert)) {
			return renderError(m.get("certStr.same"));
		}

		sqlHelper.insertOrUpdate(cert);
		return renderSuccess(cert);
	}
	
	@RequestMapping("setAutoRenew")
	@ResponseBody
	public JsonResult setAutoRenew(Cert cert) {
		sqlHelper.updateById(cert);
		return renderSuccess();
	}
	
	@RequestMapping("detail")
	@ResponseBody
	public JsonResult detail(String id) {
		return renderSuccess(sqlHelper.findById(id, Cert.class));
	}
	

	@RequestMapping("del")
	@ResponseBody
	public JsonResult del(String id) {
		Cert cert = sqlHelper.findById(id, Cert.class);

		FileUtil.del(InitConfig.acmeShDir + cert.getDomain());
		sqlHelper.deleteById(id, Cert.class);
		return renderSuccess();
	}
	
	@RequestMapping("apply")
	@ResponseBody
	public JsonResult apply(String id, String type) {

		return certController.apply(id, type);
	}
	
	
	@RequestMapping("download")
	@ResponseBody
	public void download(String id, HttpServletResponse response) throws IOException {

		certController.download(id, response);
	}
}
