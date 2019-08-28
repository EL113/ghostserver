package com.yesongdh.controller;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.fastjson.JSONObject;
import com.yesongdh.bean.StoryAudit;
import com.yesongdh.bean.StoryReport;
import com.yesongdh.common.BaseResponse;
import com.yesongdh.service.HomeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class ManagerController extends BaseResponse{
	
	@Resource
	HomeService homeService;
	
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
	
	//---------------------------------------------------- 登录 ---------------------------------------
	@ApiOperation(value = "登录")
	@PostMapping("/admin/login")
	@ResponseBody
	public JSONObject adminLogin(
			@RequestParam(required = true, name = "name") String name,
			@RequestParam(required = true, name = "passwd") String passwd) {
		Subject subject = SecurityUtils.getSubject();
		UsernamePasswordToken userToken = new UsernamePasswordToken(name, passwd);
		subject.login(userToken);
		return success();
	}
	
	@ApiOperation(value = "登出")
	@PostMapping("/admin/logout")
	@ResponseBody
	public JSONObject adminLogout() {
		SecurityUtils.getSubject().logout();
		return success("退出登录成功");
	}
	
	@ApiOperation(value = "授权错误")
	@PostMapping("/admin/error")
	@ResponseBody
	public JSONObject adminError(
			@RequestParam(required = true, name = "name") String name,
			@RequestParam(required = true, name = "passwd") String passwd) {
		Subject subject = SecurityUtils.getSubject();
		UsernamePasswordToken userToken = new UsernamePasswordToken(name, passwd);
		subject.login(userToken);
		return success();
	}
}
