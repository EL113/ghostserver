package com.yesongdh.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.yesongdh.bean.Content;
import com.yesongdh.bean.RecommendId;
import com.yesongdh.bean.RecommendItem;
import com.yesongdh.bean.StoryList;

@Mapper
public interface HomeMapper {

	@Select("select content from tab_${type}_content where id = #{id} limit 5 offset #{startIndex}")
	List<String> getContent(@Param("id") String id, @Param("type") String type, int startIndex);
	
	@Select("select count(1) from tab_${type}_content where id = #{id}")
	Integer getContentSize(@Param("id") String id, @Param("type") String type);
	
	@Select("select thumb_up, thumb_down, collection from tab_stat where id = #{id} and type = #{type}")
	Map<String, Long> getStat(@Param("id") String id, @Param("type") String type);

	@Update("update tab_stat set thumb_up = thumb_up + 1 where id = #{id} and type = #{type}")
	int thumbUp(@Param("id") String id, @Param("type") String type);
	
	@Select("select thumb_up from tab_stat where id = #{id} and type = #{type}")
	int getThumbUpCount(String id, String type);
	
	@Select("select thumb_down from tab_stat where id = #{id} and type = #{type}")
	int getThumbDownCount(String id, String type);
	
	@Update("update tab_stat set thumb_up = thumb_up - 1 where id = #{id} and type = #{type}")
	int thumbUpCancel(@Param("id") String id, @Param("type") String type);
	
	@Update("update tab_stat set thumb_down = thumb_down + 1 where id = #{id} and type = #{type}")
	int thumbDown(String id, String type);
	
	@Update("update tab_stat set thumb_down = thumb_down - 1 where id = #{id} and type = #{type}")
	int thumbDownCancel(String id, String type);
	
	@Update("update tab_stat set collection = collection + 1 where id = #{id} and type = #{type}")
	int collect(String id, String type);
	
	@Select("select collection from tab_stat where id = #{id} and type = #{type}")
	int getCollectCount(String id, String type);
	
	@Update("update tab_stat set collection = collection - 1 where id = #{id} and type = #{type}")
	int collectCancel(String id, String type);

	@Insert("insert into tab_audit (id, sub_id, title,author,type,content,`desc`, authorId)value"
			+ "(#{id}, #{subId}, #{title},#{author},#{type},#{content},#{desc},#{authorId})")
	int publish(String id, int subId, String title, String author, String type, String content,String desc, String authorId);
	
	@Delete("delete from tab_audit where id = #{id}")
	int resetPublish(String id);
	
	@Select("select id,sub_id,title,author, authorId,type,content,`desc` from tab_audit where id = #{id}")
	List<Content> getAuditItem(String id);
	
	@Insert("insert into tab_${content.type}_content (id, sub_id, content)value("
			+ "#{content.id},#{content.subId},#{content.content})")
	int insertContent(@Param("content") Content content);
	
	@Insert("insert into tab_${content.type}_list (id, title, author, `desc`, authorId)value("
			+ "#{content.id},#{content.title},#{content.author}, #{content.desc}, #{content.authorId})")
	int insertItem(@Param("content") Content content);
	
	@Insert("insert into tab_stat (id, type)value(#{id},#{type})")
	int insertStat(String id, String type);

	@Update("update tab_audit set reason = #{reason}, status=#{operation} where id = #{id}")
	int auditReason(String id, String reason, int operation);

	@Delete("delete from tab_audit where id = #{id}")
	int deleteAudit(String id);

	@Delete("delete from tab_${type}_list where id = #{id}")
	int deleteItem(String id, String type);
	
	@Delete("delete from tab_stat where id = #{id} and typeName = #{type}")
	int deleteStatItem(String id, String type);
	
	@Delete("delete from tab_${type}_content where id = #{id}")
	int deleteContent(String id, String type);

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
