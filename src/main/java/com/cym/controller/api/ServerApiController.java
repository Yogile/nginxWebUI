package com.cym.controller.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cym.model.Location;
import com.cym.model.Server;
import com.cym.service.ServerService;
import com.cym.utils.BaseController;
import com.cym.utils.JsonResult;
import com.cym.utils.SnowFlakeUtils;

import cn.craccd.sqlHelper.bean.Page;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(tags = "反向代理(server)接口")
@RestController
@RequestMapping("/api/server")
public class ServerApiController extends BaseController {
	@Autowired
	ServerService serverService;

	@ApiOperation("获取server分页列表")
	@GetMapping("getPage")
	public JsonResult<List<Server>> getPage(@ApiParam("当前页数(从1开始)") Integer current, @ApiParam("每页数量") Integer limit, @ApiParam("查询关键字") String keywords) {
		Page page = new Page();
		page.setCurr(current);
		page.setLimit(limit);
		page = serverService.search(page, keywords);

		return renderSuccess(page);
	}

	@ApiOperation("添加或编辑server")
	@PostMapping("insertOrUpdate")
	public JsonResult insertOrUpdate(Server server) {
		if (StrUtil.isEmpty(server.getId())) {
			server.setSeq(SnowFlakeUtils.getId());
		}
		sqlHelper.insert(server);
		return renderSuccess(server);
	}

	@ApiOperation("删除server")
	@GetMapping("delete")
	public JsonResult delete(String id) {
		serverService.deleteById(id);

		return renderSuccess();
	}

	@ApiOperation("根据serverId获取location列表")
	@GetMapping("getLocationByServerId")
	public JsonResult getLocationByServerId(String serverId) {
		List<Location> locationList = serverService.getLocationByServerId(serverId);

		return renderSuccess(locationList);
	}

	@ApiOperation("添加或编辑location")
	@PostMapping("insertOrUpdateLocation")
	public JsonResult insertOrUpdateLocation(Location location) {
		sqlHelper.insert(location);
		return renderSuccess(location);
	}

	@ApiOperation("删除location")
	@GetMapping("deleteLocation")
	public JsonResult deleteLocation(String id) {
		sqlHelper.deleteById(id, Location.class);

		return renderSuccess();
	}
}
