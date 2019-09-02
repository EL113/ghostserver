package com.yesongdh.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.yesongdh.bean.Permission;
import com.yesongdh.bean.Role;
import com.yesongdh.common.CommonMapper;

public interface RoleMapper extends CommonMapper<Role> {

	@Select("select * from web_")
	List<Permission> roleList(String admin);
}