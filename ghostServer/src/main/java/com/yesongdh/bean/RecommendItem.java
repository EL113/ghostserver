package com.yesongdh.bean;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 待删除
 * @author yesong
 *
 */
@Table(name="story_list")
public class RecommendItem {
	@Id
	private String id;
	private String author;
	@Column(name = "author_id")
	private String authorId;
	private String type;
	private String title;
	private String desc;
	@Column(name = "create_time")
	private String createTime;
	
	public void setAuthorId(String authorId) {
		this.authorId = authorId;
	}
	public String getAuthorId() {
		return authorId;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
}