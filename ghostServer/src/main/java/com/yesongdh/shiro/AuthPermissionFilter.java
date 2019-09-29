package com.yesongdh.shiro;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter;

import com.alibaba.fastjson.JSONObject;

/**
 * 自定义权限验证，检查subject对象中的权限是否和当前请求符合
 * @author yesong
 */
public class AuthPermissionFilter extends PermissionsAuthorizationFilter{

	public static List<String> systemUrl = new ArrayList<>();
	
	static {
		systemUrl.add("/web/login");
		systemUrl.add("/web/logout");
		systemUrl.add("/web/unauthorized");
		systemUrl.add("/web/error");
	}
	
	@Override
	public boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue)
			throws IOException {
		HttpServletRequest httpServletRequest = (HttpServletRequest)request;
		String uri = httpServletRequest.getServletPath();
//		uri = uri.substring(0, uri.indexOf(";"));
		Subject subject = getSubject(request, response);
		
		//root用户拥有所有权限 系统url不被拦截
		if (systemUrl.contains(uri)) {
			return true;
		}
		
		//会话超时
		System.out.println("-----------------------auth block0:"+uri+","+subject.getPrincipal()+","+subject.getSession().getId());
		if (!subject.isAuthenticated()) {
//			try {
//				cleanup(httpServletRequest, response, new Exception());
//			} catch (ServletException e) {
//				e.printStackTrace();
//			}
			return false;
		}
//		System.out.println("-----------------------auth block1");
		if (subject.hasRole("root")) {
			return true;
		}
		boolean ispermitted = subject.isPermitted(uri);
		return ispermitted;
	}
	
	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response, Object o) throws Exception {
		//这里直接抛出异常 检查用户状态 未登录或已登录 已登录的情况下抛出无权限异常
		return super.onAccessDenied(request, response);
	}
}
