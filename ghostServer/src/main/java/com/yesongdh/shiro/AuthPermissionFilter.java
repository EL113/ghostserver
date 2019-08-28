package com.yesongdh.shiro;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter;

public class AuthPermissionFilter extends PermissionsAuthorizationFilter{

	@Override
	public boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue)
			throws IOException {
		// TODO Auto-generated method stub
		return super.isAccessAllowed(request, response, mappedValue);
	}
}
