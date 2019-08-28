package com.yesongdh.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.yesongdh.bean.Admin;
import com.yesongdh.common.CommonMapper;

public interface AdminMapper extends CommonMapper<Admin> {
	
	@Select("select role_name from web_role where id in (select role_id from web_user_role where user_name = #{userName}) and status = 0")
	List<String> getAdminRoles(String userName);
	
	List<String> getRolePermissions(List<String> roleName);
}