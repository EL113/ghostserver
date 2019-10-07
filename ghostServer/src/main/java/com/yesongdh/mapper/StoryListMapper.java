package com.yesongdh.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.yesongdh.bean.StoryList;
import com.yesongdh.common.CommonMapper;

public interface StoryListMapper extends CommonMapper<StoryList> {

	@Select("select * from story_list where title like concat('%',#{keyword}, '%')")
	List<StoryList> searchStory(@Param("keyword") String keyWord);
}