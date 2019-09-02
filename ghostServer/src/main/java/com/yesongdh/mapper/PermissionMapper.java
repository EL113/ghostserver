package com.yesongdh.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.yesongdh.bean.Permission;
import com.yesongdh.common.CommonMapper;

public interface PermissionMapper extends CommonMapper<Permission> {

	@Select("select * from web_permission where id in (select id from web_role_permission where role_id = #{role})")
	List<Permission> permList(@Param("role") String role);
}