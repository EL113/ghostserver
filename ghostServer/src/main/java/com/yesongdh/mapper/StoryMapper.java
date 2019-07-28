package com.yesongdh.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.yesongdh.bean.RecommendId;
import com.yesongdh.bean.StoryList;
import com.yesongdh.common.CommonMapper;

@Mapper
public interface StoryMapper extends CommonMapper<StoryList> {

	@Select("select id, type from tab_stat order by score desc, id asc")
	List<RecommendId> getRecommendIds();
	
	List<StoryList> getRecommendItem(@Param("ids") List<RecommendId> ids);
}
