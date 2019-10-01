package com.yesongdh.shiro;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import com.yesongdh.bean.Admin;
import com.yesongdh.bean.Role;
import com.yesongdh.mapper.AdminMapper;
import com.yesongdh.mapper.RoleMapper;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

public class ShiroRealm extends AuthorizingRealm{
	
	@Autowired
	AdminMapper adminMapper;
	
	@Autowired
	RoleMapper roleMapper;
	
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		String userName = (String) principals.getPrimaryPrincipal();
		Admin record = new Admin();
		record.setName(userName);
		record.setStatus("0");
		List<Admin> admins = adminMapper.select(record);
		List<String> roles = new ArrayList<String>();
		roles.add(admins.get(0).getName());
		
		//权限就是uri
		List<String> permissions = roleMapper.getRolePermissions(roles);
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
			throw new UnknownAccountException("无效用户");
		}
		Admin userAdmin = userAdmins.get(0);
		//账户状态 0 启用 1 未启用
		if ("1".equals(userAdmin.getStatus())) {
			throw new DisabledAccountException("账户已被禁用");
		}
		
		ByteSource credentialSalt = ByteSource.Util.bytes(userAdmin.getSalt());
		return new SimpleAuthenticationInfo(userName, userAdmin.getPasswd(), credentialSalt, getName());
	}
	
//	public static void main(String[] args) {
//        String hashAlgorithName = "MD5";
//        String password = "123456";
//        int hashIterations = 1024;
//        String credentialSalt = UUID.randomUUID().toString();
//        System.out.println(credentialSalt);
//        ByteSource credentialsSalt = ByteSource.Util.bytes(credentialSalt);
//        Object obj = new SimpleHash(hashAlgorithName, password, credentialsSalt, hashIterations);
//        System.out.println(obj);
//    }
	
}
