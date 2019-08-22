package com.yesongdh.mapper;

import java.util.List;

import com.yesongdh.bean.StoryReport;
import com.yesongdh.common.CommonMapper;

public interface StoryReportMapper extends CommonMapper<StoryReport> {

	List<StoryReport> getStoryReportsByCondition(StoryReport storyReport);
}