package com.yesongdh.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.yesongdh.bean.RecommendId;
import com.yesongdh.bean.StoryList;

import tk.mybatis.mapper.common.Mapper;

public interface StoryMapper extends Mapper<StoryMapper> {

	@Select("select id, type from tab_stat order by score desc, id asc limit #{pageCount} offset #{startIndex}")
	List<RecommendId> getRecommendIds(@Param("pageSize") int pageSize, @Param("startIndex") int startIndex);
	
	List<StoryList> getRecommendItem(@Param("ids") List<RecommendId> ids);
}
