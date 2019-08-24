package com.yesongdh.controller;

import java.util.Calendar;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.fastjson.JSONObject;
import com.yesongdh.annotation.IgnoreToken;
import com.yesongdh.bean.StoryAudit;
import com.yesongdh.common.BaseResponse;
import com.yesongdh.mapper.StoryAuditMapper;
import com.yesongdh.service.HomeService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Api
@RestController
@RequestMapping(value="/openapi")
public class OpenApiController extends BaseResponse{
	public static final Logger logger = LoggerFactory.getLogger(OpenApiController.class);
	
	@Resource
	HomeService homeService;
	
	@ApiOperation(value = "首页推荐")
	@PostMapping("/recommend")
	@IgnoreToken
	@ResponseBody
	public JSONObject getRecommend(
			@RequestParam(required = true, name = "pageNo") int pageNo,
			@RequestParam(required = true, name = "pageSize") int pageSize) {
		return success(homeService.getRecommend(pageNo, pageSize));
	}
	
	@ApiOperation(value = "故事详情")
	@PostMapping("/content")
	public JSONObject getContent(
			@RequestParam(required = true, name = "id") String id,
			@RequestParam(required = true, name = "type") String type,
			@RequestParam(required = true, name = "pageNo") int pageNo,
			@RequestParam(required = true, name = "pageSize") int pageSize) {
		return success(homeService.getContent(id, type, pageNo, pageSize));
	}
	
	@ApiOperation(value = "赞")
	@PostMapping("/thumbUp")
	@ResponseBody
	public JSONObject thumbUp(
			@RequestParam(required = true, name = "id") String id,
			@RequestParam(required = true, name = "type") String type,
			@RequestParam(required = true, name = "op") int op) {
		return homeService.thumbUp(id, type, op) ? success() : fail("操作失败");
	}
	
	@ApiOperation(value = "踩")
	@PostMapping("/thumbDown")
	@ResponseBody
	public JSONObject thumbDown(
			@RequestParam(required = true, name = "id") String id,
			@RequestParam(required = true, name = "type") String type,
			@RequestParam(required = true, name = "op") int op) {
		return homeService.thumbDown(id, type, op) ? success() : fail("操作失败");
	}
	
	@ApiOperation(value = "收藏")
	@PostMapping("/collect")
	@ResponseBody
	public JSONObject collect(
			@RequestParam(required = true, name = "id") String id,
			@RequestParam(required = true, name = "type") String type,
			@RequestParam(required = true, name = "op") int op) {
		return homeService.collect(id, type, op) ? success() : fail("操作失败");
	}
	
	@ApiOperation(value = "发布故事")
	@PostMapping("/publish")
	@ResponseBody
	public JSONObject publish(@RequestBody StoryAudit story) {
		String id = homeService.publish(story);
		return id == null ? fail("操作失败") : success(id);
	}
	
	@ApiOperation(value = "举报")
	@PostMapping("/report")
	@ResponseBody
	public JSONObject publish(@RequestParam("id") String id, @RequestParam("reason") String reason) {
		return homeService.report(id, reason) ? success(id) : fail("操作失败");
	}
	
	//------------------------------------------- 定时器 -------------------------------------------------
	
	@Autowired
	StoryAuditMapper storyAuditMapper;
	
	@Scheduled(cron = "* * * 1 * *")
	public void deleteInvalidStory() {
		Example example = new Example(StoryAudit.class);
		Criteria criteria = example.createCriteria();
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -30);
		criteria.andLessThan("create_time", calendar.getTime());
		storyAuditMapper.deleteByExample(example);
	}
}