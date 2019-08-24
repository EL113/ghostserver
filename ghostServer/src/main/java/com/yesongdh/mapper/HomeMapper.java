package com.yesongdh.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Select;
import com.yesongdh.bean.StoryList;

public interface HomeMapper {

	@Select("select story_list.* from story_list inner join story_stat where story_list.id = story_stat.id order by story_stat.score desc")
	List<StoryList> getStoryListByStatOrder();
	
}
