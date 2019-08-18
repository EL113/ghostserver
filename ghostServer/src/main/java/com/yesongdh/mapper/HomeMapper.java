package com.yesongdh.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import com.yesongdh.bean.RecommendItem;
import com.yesongdh.bean.StoryList;

public interface HomeMapper {

	@Select("select a.* from story_list a inner join story_stat b where a.id = b.id order by b.score desc")
	List<StoryList> getStoryListByStatOrder();
	
	@Delete("delete from tab_stat where id = #{id} and typeName = #{type}")
	int deleteStatItem(String id, String type);
	
	@Select("select status from tab_audit where id = #{id}")
	List<Integer> getAuditResult(String id);

	@Select("select id, title, `desc`, create_time from tab_${type}_list where "
			+ "title like concat('%', #{keyword}, '%') and limit #{count} offset #{startIndex}")
	List<RecommendItem> searchKeyword(String type, String keyword, int startIndex, int count);

	@Select("select count(1) from tab_${type}_list where title like concat('%', #{keyword}, '%')")
	int searchKeywordCount(String type, String keyword);

	@Select("select id, title, `desc`,author, create_time from tab_${type}_list limit #{count} offset #{startIndex}")
	List<RecommendItem> getTypeList(String type, int count, int startIndex);

}
