package com.yesongdh.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yesongdh.bean.StoryAudit;
import com.yesongdh.common.CommonMapper;

public interface StoryAuditMapper extends CommonMapper<StoryAudit> {
	
	//管理系统带审查列表
	List<StoryAudit> getStoryAuditsByCondition(StoryAudit storyAudit);
	
	//openapi查询审查是否通过列表
	List<StoryAudit> getStoryAudits(@Param("ids") List<String> ids);
}