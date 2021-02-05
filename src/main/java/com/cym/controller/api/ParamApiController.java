package com.cym.controller.api;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cym.model.Param;
import com.cym.service.ParamService;
import com.cym.utils.BaseController;
import com.cym.utils.JsonResult;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "额外参数接口")
@RestController
@RequestMapping("/api/param")
public class ParamApiController extends BaseController {

	@Autowired
	ParamService paramService;

	@ApiOperation("根据项目获取参数列表")
	@GetMapping("getList")
	public JsonResult<List<Param>> getList(String serverId, String locationId, String upstreamId) {
		List<Param> list = paramService.getList(serverId, locationId, upstreamId);
		return renderSuccess(list);
	}

	@ApiOperation("添加或编辑参数")
	@RequestMapping("insertOrUpdate")
	public JsonResult<?> insertOrUpdate(Param param) throws IOException {
		if (StrUtil.isEmpty(param.getServerId()) || StrUtil.isEmpty(param.getLocationId()) || StrUtil.isEmpty(param.getUpstreamId())) {
			renderError(m.get("apiStr.paramError"));
		}
		
		sqlHelper.insertOrUpdate(param);
		
		return renderSuccess();
	}

	@ApiOperation("删除")
	@RequestMapping("del")
	public JsonResult<?> del(String id) {
		sqlHelper.deleteById(id, Param.class);

		return renderSuccess();
	}

}
