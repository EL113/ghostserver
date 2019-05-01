package com.yesongdh.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.yesongdh.bean.RecommendId;
import com.yesongdh.bean.RecommendItem;

public interface HomeMapper {

	@Select("select id, type from tab_stat order by score desc limit #{pageCount} offset #{startIndex}")
	List<RecommendId> getRecommendIds(@Param("pageCount") int pageCount, @Param("startIndex") int startIndex);
	
	@Select("select id, title, `desc` from tab_${id.type}_list where id = #{id.id}")
	RecommendItem getRecommendItem(@Param("id") RecommendId id);
}
