package com.yesongdh.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.yesongdh.bean.Content;
import com.yesongdh.bean.RecommendId;
import com.yesongdh.bean.RecommendItem;

public interface HomeMapper {

	@Select("select id, type from tab_stat order by score desc limit #{pageCount} offset #{startIndex}")
	List<RecommendId> getRecommendIds(@Param("pageCount") int pageCount, @Param("startIndex") int startIndex);
	
	@Select("select id, title, `desc`, create_time from tab_${id.type}_list where id = #{id.id}")
	RecommendItem getRecommendItem(@Param("id") RecommendId id);
	
	@Select("select content from tab_${type}_content where id = #{id}")
	List<String> getContent(@Param("id") String id, @Param("type") String type);
	
	@Select("select thumb_up, thumb_down, collection from tab_stat where id = #{id} and type = #{type}")
	Map<String, Integer> getStat(@Param("id") String id, @Param("type") String type);

	@Update("update tab_stat set thumb_up = thumb_up + 1 where id = #{id} and type = #{type}")
	int thumbUp(@Param("id") String id, @Param("type") String type);
	
	@Update("update tab_stat set thumb_up = thumb_up - 1 where id = #{id} and type = #{type}")
	int thumbUpCancel(@Param("id") String id, @Param("type") String type);
	
	@Update("update tab_stat set thumb_down = thumb_down + 1 where id = #{id} and type = #{type}")
	int thumbDown(String id, String type);
	
	@Update("update tab_stat set thumb_down = thumb_down - 1 where id = #{id} and type = #{type}")
	int thumbDownCancel(String id, String type);
	
	@Update("update tab_stat set collection = collection + 1 where id = #{id} and type = #{type}")
	int collect(String id, String type);
	
	@Update("update tab_stat set collection = collection - 1 where id = #{id} and type = #{type}")
	int collectCancel(String id, String type);

	@Insert("insert into tab_audit (id, sub_id, title,author,type,content,`desc`)value"
			+ "(#{id}, #{subId}, #{title},#{author},#{type},#{content},#{desc})")
	int publish(String id, int subId, String title, String author, String type, String content,String desc);
	
	@Delete("delete from tab_audit where id = #{id}")
	int resetPublish(String id);
	
	@Select("select id,sub_id,title,author,type,content,`desc` from tab_audit where id = #{id}")
	List<Content> getAuditItem(String id);
	
	@Insert("insert into tab_${content.type}_content (id, sub_id, content)value("
			+ "#{content.id},#{content.subId},#{content.content})")
	int insertContent(@Param("content") Content content);
	
	@Insert("insert into tab_${content.type}_list (id, title, author, `desc`)value("
			+ "#{content.id},#{content.title},#{content.author}, #{content.desc})")
	int insertItem(@Param("content") Content content);
	
	@Insert("insert into tab_stat (id, type)value(#{id},#{type})")
	int insertStat(String id, String type);

	@Update("update tab_audit set reason = #{reason} where id = #{id}")
	int auditReason(String id, String reason);

	@Delete("delete from tab_audit where id = #{id}")
	int deleteAudit(String id);

	@Delete("delete from tab_${content.type}_list where id = #{content.id}")
	int deleteItem(@Param("content") Content content);
	
	@Delete("delete from tab_${content.type}_content where id = #{content.id}")
	int deleteContent(@Param("content") Content content);
}
