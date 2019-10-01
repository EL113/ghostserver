package com.yesongdh.controller;

import java.util.List;
import javax.annotation.Resource;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.yesongdh.bean.Admin;
import com.yesongdh.bean.Permission;
import com.yesongdh.bean.Role;
import com.yesongdh.bean.StoryAudit;
import com.yesongdh.bean.StoryReport;
import com.yesongdh.common.BaseResponse;
import com.yesongdh.service.WebManagerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
@RequestMapping("/web")
public class ManagerController extends BaseResponse{
	
	@Resource
	WebManagerService webManagerService;
	
	@ApiOperation(value = "待审查列表")
	@PostMapping("/audit/list")
	@ResponseBody
	public JSONObject auditList(@RequestBody StoryAudit storyAudit,
			@RequestParam("page") int page, @RequestParam("pageSize") int pageSize) {
		return success(webManagerService.auditList(storyAudit, page, pageSize));
	}
	
	@ApiOperation(value = "审查")
	@PostMapping("/audit")
	@ResponseBody
	public JSONObject audit(
			@RequestParam(required = true, name = "id") String id,
			@RequestParam(required = true, name = "op") int op,
			@RequestParam(required = false, name = "reason") String reason) {
		return webManagerService.audit(id, reason, op) ? success() : fail("操作失败");
	}
	
	@ApiOperation(value = "举报列表")
	@PostMapping("/report/list")
	@ResponseBody
	public JSONObject reportList(@RequestBody StoryReport storyReport,
			@RequestParam("page") int page, @RequestParam("pageSize") int pageSize) {
		return success(webManagerService.reportList(storyReport, page, pageSize));
	}
	
	@ApiOperation(value = "处理举报")
	@PostMapping("/report/handle")
	@ResponseBody
	public JSONObject handleReport(
			@RequestParam(required = true, name = "id") String id,
			@RequestParam(required = true, name = "handler") String handler,
			@RequestParam(required = false, name = "reason") String reason) {
		return webManagerService.handleReport(id, handler, reason) ? success() : fail("操作失败");
	}
	
	@ApiOperation(value = "删除故事")
	@PostMapping("/story/delete")
	@ResponseBody
	public JSONObject deleteStory(
			@RequestParam(required = true, name = "id") String id) {
		return webManagerService.deleteStory(id) ? success() : fail("操作失败");
	}
	
	//---------------------------------------------------- 权限管理 ---------------------------------------
	@ApiOperation(value = "登录")
	@PostMapping("/login")
	@ResponseBody
	public JSONObject adminLogin(
			@RequestParam(required = true, name = "name") String name,
			@RequestParam(required = true, name = "passwd") String passwd) {
		Subject subject = SecurityUtils.getSubject();
		UsernamePasswordToken userToken = new UsernamePasswordToken(name, passwd);
		try {
			subject.login(userToken);
		} catch (DisabledAccountException e) {
			return fail(e.getMessage());
		} catch (ExcessiveAttemptsException e) {
			return fail(e.getMessage());
		} catch (UnknownAccountException e) {
			return fail(e.getMessage());
		} catch (AuthenticationException e) {
			return fail("密码错误");
		} 
		
		return success();
	}
	
	@ApiOperation(value = "登出")
	@PostMapping("/logout")
	@ResponseBody
	public JSONObject adminLogout() {
		Subject subject = SecurityUtils.getSubject();
		System.out.println("-----------------------"+subject.getPrincipal()+" logout");
		subject.logout();
		return success("退出登录成功");
	}
	
	@ApiOperation(value = "查询用户")
	@PostMapping("/admin/list")
	@ResponseBody
	public JSONObject adminList(@RequestBody Admin admin,
			@RequestParam("pageNo") int pageNo, @RequestParam("pageSize") int pageSize) {
		List<Admin> admins = webManagerService.adminList(admin, pageNo, pageSize);
		return success(new PageInfo<Admin>(admins));
	}
	
	//修改包括 名称 密码 角色 锁定状态
	@ApiOperation(value = "操作管理用户 修改用户信息 锁定解锁 角色")
	@PostMapping("/admin/operate")
	@ResponseBody
	public JSONObject adminMod(@RequestBody Admin admin,
			@RequestParam(value = "op", required = true) String operate) {
		String message = webManagerService.adminOperate(admin, operate);
		return message == null ? success() : fail(message);
	}
	
	@ApiOperation(value = "操作权限")
	@PostMapping("/perm/operate")
	@ResponseBody
	public JSONObject permOperate(@RequestParam(required = true, name = "operate") String operate,
			@RequestBody List<Permission> permissions) {
		String msg = webManagerService.permOperate(operate, permissions);
		return msg == null ? success() : fail(msg);
	}
	
	//根据角色查询权限
	@ApiOperation(value = "查询权限")
	@PostMapping("/perm/list")
	@ResponseBody
	public JSONObject permList(@RequestBody Permission permission, 
			@RequestParam(required = true, name = "pageNo") int pageNo, 
			@RequestParam(required = true, name = "pageSize") int pageSize) {
		return success(webManagerService.permList(permission, pageNo, pageSize));
	}
	
	@ApiOperation(value = "查询角色")
	@PostMapping("/role/list")
	@ResponseBody
	public JSONObject roleList(@RequestBody Role role, 
			@RequestParam(required = true, name = "pageNo") int pageNo, 
			@RequestParam(required = true, name = "pageSize") int pageSize) {
		return success(webManagerService.roleList(role, pageNo, pageSize));
	}
	
	//修改角色包括 修改角色信息 角色的权限信息
	@ApiOperation(value = "操作角色")
	@PostMapping("/role/operate")
	@ResponseBody
	public JSONObject roleOperate(
			@RequestParam(required = true, name = "operate") String operate,
			@RequestBody List<Role> roles) {
		return webManagerService.roleOperate(operate, roles) ? success() : fail("操作失败");
	}
	
	@RequestMapping("/unauthorized")
	@ResponseBody
	public JSONObject unauthorized() {
		return fail("无权限");
	}
	
	@RequestMapping("/error")
	@ResponseBody
	public JSONObject adminError() {
		return fail("未登录");
	}
}
