package com.yesongdh.shiro;

import java.util.List;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import com.yesongdh.bean.Admin;
import com.yesongdh.mapper.AdminMapper;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

public class ShiroRealm extends AuthorizingRealm{
	
	@Autowired
	AdminMapper adminMapper;
	
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		String userName = (String) principals.getPrimaryPrincipal();
		List<String> roles = adminMapper.getAdminRoles(userName);
		//权限就是uri
		List<String> permissions = adminMapper.getRolePermissions(roles);
		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		authorizationInfo.addRoles(roles);
		authorizationInfo.addStringPermissions(permissions);
		return authorizationInfo;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		UsernamePasswordToken userToken = (UsernamePasswordToken)token;
		String userName = userToken.getUsername();
		Example userExample = new Example(Admin.class);
		Criteria criteria = userExample.createCriteria();
		criteria.andEqualTo("name", userName);
		List<Admin> userAdmins = adminMapper.selectByExample(userExample);
		
		if (userAdmins.size() != 1) {
			throw new AuthenticationException("无效用户");
		}
		Admin userAdmin = userAdmins.get(0);
		//账户状态 0 启用 1 未启用
		if ("1".equals(userAdmin.getStatus())) {
			throw new AuthenticationException("账户已被禁用");
		}
		
		//检查用户信息
		if (!userAdmin.getPasswd().equals(new String(userToken.getPassword()))) {
			
		}
		
		return new SimpleAuthenticationInfo(userName, userAdmin.getPasswd(), getName());
	}
	
}
