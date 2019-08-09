package com.yesongdh.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import com.yesongdh.bean.StoryStat;
import com.yesongdh.common.CommonMapper;

public interface StoryStatMapper extends CommonMapper<StoryStat> {
	
	@Update("update story_stat set thumb_up = thumb_up + 1 where id = #{id} and type = #{type}")
	int thumbUp(@Param("id") String id, @Param("type") String type);
	
	@Update("update story_stat set thumb_up = thumb_up - 1 where id = #{id} and type = #{type}")
	int thumbUpCancel(@Param("id") String id, @Param("type") String type);
	
	@Update("update story_stat set thumb_down = thumb_down + 1 where id = #{id} and type = #{type}")
	int thumbDown(String id, String type);
	
	@Update("update story_stat set thumb_down = thumb_down - 1 where id = #{id} and type = #{type}")
	int thumbDownCancel(String id, String type);
}