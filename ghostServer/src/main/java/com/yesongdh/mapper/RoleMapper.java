package com.yesongdh.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;

import com.yesongdh.bean.Role;
import com.yesongdh.common.CommonMapper;

public interface RoleMapper extends CommonMapper<Role> {

	//添加用户角色
	int insertRolePerms(@Param("roleId") Long roleId, @Param("permsList")  List<String> permsList);

	//获取用户权限
	List<String> getRolePermissions(@Param("roles") List<String> roleName);
	
	//删除角色的所有权限用于重新更新用户角色权限
	@Delete("delete from web_role_permission where role_id = #{roleId}")
	int deleteRolePerms(@Param("roleId") Long roleId);
}