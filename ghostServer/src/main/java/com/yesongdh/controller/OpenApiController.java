package com.yesongdh.controller;

import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.fastjson.JSONObject;
import com.yesongdh.annotation.IgnoreToken;
import com.yesongdh.bean.StoryAudit;
import com.yesongdh.common.BaseResponse;
import com.yesongdh.service.HomeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
@RequestMapping(value="/openapi")
public class OpenApiController extends BaseResponse{
	public static final Logger logger = LoggerFactory.getLogger(OpenApiController.class);
	
	@Resource
	HomeService homeService;
	
	@ApiOperation(value = "首页推荐")
	@GetMapping("/recommend")
	@IgnoreToken
	@ResponseBody
	public JSONObject getRecommend(
			@RequestParam(required = true, name = "pageNo") int pageNo,
			@RequestParam(required = true, name = "pageSize") int pageSize) {
		return success(homeService.getRecommend(pageNo, pageSize));
	}
	
	@ApiOperation(value = "故事详情")
	@GetMapping("/content")
	@IgnoreToken
	@ResponseBody
	public JSONObject getContent(
			@RequestParam(required = true, name = "id") String id,
			@RequestParam(required = true, name = "type") String type,
			@RequestParam(required = true, name = "pageNo") int pageNo,
			@RequestParam(required = true, name = "pageSize") int pageSize) {
		return success(homeService.getContent(id, type, pageNo, pageSize));
	}
	
	@ApiOperation(value = "赞")
	@PutMapping("/thumbUp")
	@IgnoreToken
	@ResponseBody
	public JSONObject thumbUp(
			@RequestParam(required = true, name = "id") String id,
			@RequestParam(required = true, name = "type") String type,
			@RequestParam(required = true, name = "op") int op) {
		return homeService.thumbUp(id, type, op) ? success() : fail("操作失败");
	}
	
	@ApiOperation(value = "踩")
	@PutMapping("/thumbDown")
	@IgnoreToken
	@ResponseBody
	public JSONObject thumbDown(
			@RequestParam(required = true, name = "id") String id,
			@RequestParam(required = true, name = "type") String type,
			@RequestParam(required = true, name = "op") int op) {
		return homeService.thumbDown(id, type, op) ? success() : fail("操作失败");
	}
	
	@ApiOperation(value = "收藏")
	@PutMapping("/collect")
	@IgnoreToken
	@ResponseBody
	public JSONObject collect(
			@RequestParam(required = true, name = "id") String id,
			@RequestParam(required = true, name = "type") String type,
			@RequestParam(required = true, name = "op") int op) {
		return homeService.collect(id, type, op) ? success() : fail("操作失败");
	}
	
	@ApiOperation(value = "发布故事")
	@PostMapping("/publish")
	@IgnoreToken
	@ResponseBody
	public JSONObject publish(@RequestBody StoryAudit story) {
		String id = homeService.publish(story);
		return id == null ? fail("操作失败") : success(id);
	}
	
	@ApiOperation(value = "举报")
	@PostMapping("/report")
	@IgnoreToken
	@ResponseBody
	public JSONObject publish(@RequestParam("id") String id, @RequestParam("reason") String reason) {
		return homeService.report(id, reason) ? success(id) : fail("操作失败");
	}
	
}
