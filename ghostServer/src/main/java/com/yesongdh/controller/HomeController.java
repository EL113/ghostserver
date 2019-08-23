package com.yesongdh.controller;

import java.util.Calendar;
import java.util.Date;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.fastjson.JSONObject;
import com.yesongdh.bean.StoryAudit;
import com.yesongdh.bean.StoryReport;
import com.yesongdh.common.BaseResponse;
import com.yesongdh.mapper.StoryAuditMapper;
import com.yesongdh.service.HomeService;

import io.lettuce.core.dynamic.annotation.Param;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Api("首页")
@RestController
public class HomeController extends BaseResponse{
	
	@Resource
	HomeService homeService;
	
	@ApiOperation(value = "首页推荐")
	@PostMapping("/recommend")
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
	
//	@PostMapping("/delete")
//	@ResponseBody
//	public JSONObject delete(
//			@RequestParam(required = false, name = "id") String id,
//			@RequestParam(required = true, name = "type") String type,
//			@RequestParam(required = true, name = "authorId") String authorId) {
//		return homeService.delete(id, type, authorId);
//	}
	
	// ---------------------------- 管理系统 ----------------------------------
	@ApiOperation(value = "待审查列表")
	@PostMapping("/audit/list")
	@ResponseBody
	public JSONObject auditList(@RequestBody StoryAudit storyAudit,
			@RequestParam("page") int page, @RequestParam("pageSize") int pageSize) {
		return success(homeService.auditList(storyAudit, page, pageSize));
	}
	
	@ApiOperation(value = "审查")
	@PostMapping("/audit")
	@ResponseBody
	public JSONObject audit(
			@RequestParam(required = true, name = "id") String id,
			@RequestParam(required = true, name = "op") int op,
			@RequestParam(required = false, name = "reason") String reason) {
		return homeService.audit(id, reason, op) ? success() : fail("操作失败");
	}
	
	@ApiOperation(value = "举报列表")
	@PostMapping("/report/list")
	@ResponseBody
	public JSONObject reportList(@RequestBody StoryReport storyReport,
			@RequestParam("page") int page, @RequestParam("pageSize") int pageSize) {
		return success(homeService.reportList(storyReport, page, pageSize));
	}
	
	@ApiOperation(value = "处理举报")
	@PostMapping("/report/handle")
	@ResponseBody
	public JSONObject handleReport(
			@RequestParam(required = true, name = "id") String id,
			@RequestParam(required = true, name = "handler") String handler,
			@RequestParam(required = false, name = "reason") String reason) {
		return homeService.handleReport(id, handler, reason) ? success() : fail("操作失败");
	}
	
	@ApiOperation(value = "删除故事")
	@PostMapping("/story/delete")
	@ResponseBody
	public JSONObject deleteStory(
			@RequestParam(required = true, name = "id") String id) {
		return homeService.deleteStory(id) ? success() : fail("操作失败");
	}
	
	//------------------------------------------- 管理模块 -----------------------------------------------
	
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
