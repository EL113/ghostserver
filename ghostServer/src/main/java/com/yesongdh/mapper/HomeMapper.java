package com.yesongdh.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Select;
import com.yesongdh.bean.StoryList;

public interface HomeMapper {

	@Select("select a.* from story_list a inner join story_stat b where a.id = b.id order by b.score desc")
	List<StoryList> getStoryListByStatOrder();
	
}
