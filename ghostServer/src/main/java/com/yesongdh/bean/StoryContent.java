package com.yesongdh.bean;

import javax.persistence.*;

@Table(name = "story_content")
public class StoryContent {
    /**
     * 故事id号
     */
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 故事子id号
     */
	@Id
    @Column(name = "sub_id")
    private Integer subId;

    /**
     * 故事内容
     */
    private String content;

    /**
     * 获取故事id号
     *
     * @return id - 故事id号
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置故事id号
     *
     * @param id 故事id号
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取故事子id号
     *
     * @return sub_id - 故事子id号
     */
    public Integer getSubId() {
        return subId;
    }

    /**
     * 设置故事子id号
     *
     * @param subId 故事子id号
     */
    public void setSubId(Integer subId) {
        this.subId = subId;
    }

    /**
     * 获取故事内容
     *
     * @return content - 故事内容
     */
    public String getContent() {
        return content;
    }

    /**
     * 设置故事内容
     *
     * @param content 故事内容
     */
    public void setContent(String content) {
        this.content = content;
    }
}