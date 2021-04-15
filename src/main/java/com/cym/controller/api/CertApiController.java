package com.cym.controller.api;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cym.controller.adminPage.CertController;
import com.cym.model.Cert;
import com.cym.service.CertService;
import com.cym.utils.BaseController;
import com.cym.utils.JsonResult;

import cn.craccd.sqlHelper.bean.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(tags = "证书接口")
@RestController
@RequestMapping("/api/cert")
public class CertApiController extends BaseController {

	@Autowired
	CertController certController;
	@Autowired
	CertService certService;

	@SuppressWarnings("unchecked")
	@ApiOperation("获取证书分页列表")
	@PostMapping("getPage")
	public JsonResult<Page<Cert>> getPage(@ApiParam("当前页数(从1开始)") @RequestParam(defaultValue = "1") Integer current, //
			@ApiParam("每页数量(默认为10)") @RequestParam(defaultValue = "10") Integer limit) {
		Page page = new Page();
		page.setCurr(current);
		page.setLimit(limit);
		page = sqlHelper.findPage(page, Cert.class);

		return renderSuccess(page);
	}

	@ApiOperation("添加或编辑证书")
	@RequestMapping("addOver")
	public JsonResult addOver(Cert cert) {
		return certController.addOver(cert);
	}

	@ApiOperation("设置证书自动续签")
	@RequestMapping("setAutoRenew")
	public JsonResult setAutoRenew(String id, Integer autoRenew) {
		Cert cert = new Cert();
		cert.setId(id);
		cert.setAutoRenew(autoRenew);

		certController.setAutoRenew(cert);
		return renderSuccess();
	}

	@ApiOperation("删除证书")
	@RequestMapping("del")
	public JsonResult del(String id) {
		return certController.del(id);
	}

	@ApiOperation("执行申请")
	@RequestMapping("apply")
	public JsonResult apply(String id, String type) {

		return certController.apply(id, type);
	}

	@ApiOperation("下载证书文件")
	@RequestMapping("download")
	public void download(String id, HttpServletResponse response) throws IOException {
		certController.download(id, response);
	}
}
