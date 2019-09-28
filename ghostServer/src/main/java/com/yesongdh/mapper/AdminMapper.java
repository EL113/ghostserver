package com.yesongdh.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.yesongdh.bean.Admin;
import com.yesongdh.common.CommonMapper;

public interface AdminMapper extends CommonMapper<Admin> {
	
	@Select("select role from web_admin where name = #{userName} and status = 0")
	List<String> getAdminRoles(@Param("userName") String userName);
	
	List<String> getRolePermissions(@Param("roles") List<String> roleName);

	@Insert("insert into web_role (role_name, status)values(#{roleName}, '0')")
	int insertRole(@Param("roleName") String roleName);

	int insertRolePerms(@Param("roleId") int roleId, @Param("permsList")  List<String> permsList);
}