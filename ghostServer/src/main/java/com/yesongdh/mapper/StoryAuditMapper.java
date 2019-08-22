package com.yesongdh.mapper;

import java.util.List;

import com.yesongdh.bean.StoryAudit;
import com.yesongdh.common.CommonMapper;

public interface StoryAuditMapper extends CommonMapper<StoryAudit> {
	
	List<StoryAudit> getStoryAuditsByCondition(StoryAudit storyAudit);
}