package com.yesongdh.shiro;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter;

import com.alibaba.fastjson.JSONObject;

/**
 * 使用某人的authc拦截器有点麻烦，不能细粒度控制访问接口，不如自定义拦截器
 * 用户的权限就是用户可访问的uri，如果当前访问的uri在用户权限中，则放行
 * @author yesong
 *
 */
public class AuthPermissionFilter extends PermissionsAuthorizationFilter{

	@Override
	public boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue)
			throws IOException {
		HttpServletRequest httpServletRequest = (HttpServletRequest)request;
		String uri = httpServletRequest.getRequestURI();
		Subject subject = getSubject(request, response);
		
		JSONObject errorJson = new JSONObject();
		Session session = subject.getSession();
		if (session == null) {
			errorJson.put("100001", "会话超时失效");
			writeResponse(response, errorJson);
			return false;
		} 

		if (!subject.isPermitted(uri)) {
			errorJson.put("100001", "无权访问");
			writeResponse(response, errorJson);
			return false;
		}
		
		return true;
	}
	
	private void writeResponse(ServletResponse servletResponse, JSONObject json) throws IOException {
		servletResponse.setContentType("text/html;charset=UTF-8");
		servletResponse.getWriter().write(json.toString());
	}
	
	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response, Object o) throws IOException {
		
		return super.onAccessDenied(request, response);
	}
}
