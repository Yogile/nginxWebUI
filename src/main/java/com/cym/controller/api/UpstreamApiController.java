package com.cym.controller.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cym.model.Upstream;
import com.cym.model.UpstreamServer;
import com.cym.service.UpstreamService;
import com.cym.utils.BaseController;
import com.cym.utils.JsonResult;
import com.cym.utils.SnowFlakeUtils;

import cn.craccd.sqlHelper.bean.Page;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(tags = "负载均衡(upstream)接口")
@RestController
@RequestMapping("/api/upstream")
public class UpstreamApiController extends BaseController {
	@Autowired
	UpstreamService upstreamService;

	@ApiOperation("获取upstream分页列表")
	@GetMapping("getPage")
	public JsonResult<Page<Upstream>> getPage(@ApiParam("当前页数(从1开始)") Integer current, @ApiParam("每页数量") Integer limit, @ApiParam("查询关键字") String keywords) {
		Page page = new Page();
		page.setCurr(current);
		page.setLimit(limit);
		page = upstreamService.search(page, keywords);

		return renderSuccess(page);
	}

	@ApiOperation("添加或编辑upstream")
	@PostMapping("insertOrUpdate")
	public JsonResult<?> insertOrUpdate(Upstream upstream) {

		if (StrUtil.isEmpty(upstream.getId())) {
			upstream.setSeq(SnowFlakeUtils.getId());
		}
		sqlHelper.insert(upstream);
		return renderSuccess(upstream);
	}

	@ApiOperation("删除upstream")
	@GetMapping("delete")
	public JsonResult<?> delete(String id) {
		upstreamService.deleteById(id);

		return renderSuccess();
	}

	@ApiOperation("根据upstreamId获取server列表")
	@GetMapping("getServerByUpstreamId")
	public JsonResult<List<UpstreamServer>> getServerByUpstreamId(String upstreamId) {
		List<UpstreamServer> list = upstreamService.getUpstreamServers(upstreamId);

		return renderSuccess(list);
	}

	@ApiOperation("添加或编辑server")
	@PostMapping("insertOrUpdateServer")
	public JsonResult insertOrUpdateServer(UpstreamServer upstreamServer) {
		sqlHelper.insert(upstreamServer);
		return renderSuccess(upstreamServer);
	}

	@ApiOperation("删除server")
	@GetMapping("deleteServer")
	public JsonResult deleteServer(String id) {
		sqlHelper.deleteById(id, UpstreamServer.class);
		return renderSuccess();
	}
}
