package com.yesongdh.shiro;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter;

import com.alibaba.fastjson.JSONObject;

/**
 * 自定义权限验证，检查subject对象中的权限是否和当前请求符合
 * @author yesong
 */
public class AuthPermissionFilter extends PermissionsAuthorizationFilter{

	List<String> systemUrl = new ArrayList<>();
	
	{
		systemUrl.add("/ghoststory/web/login");
		systemUrl.add("/ghoststory/web/logout");
		systemUrl.add("/ghoststory/web/unauthorized");
		systemUrl.add("/ghoststory/web/error");
	}
	
	@Override
	public boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue)
			throws IOException {
		HttpServletRequest httpServletRequest = (HttpServletRequest)request;
		String uri = httpServletRequest.getRequestURI();
		Subject subject = getSubject(request, response);
		System.out.println("-------------------------"+uri+","+subject.getPrincipal());
		//root用户拥有所有权限
		if (systemUrl.contains(uri)) {
			return true;
		}
		
		if (subject.hasRole("root")) {
			return true;
		}
		
		boolean ispermitted = subject.isPermitted(uri);
		System.out.println("-----------------------"+ ispermitted);
		return ispermitted;
	}
	
	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response, Object o) throws IOException {
		
		return super.onAccessDenied(request, response);
	}
}
