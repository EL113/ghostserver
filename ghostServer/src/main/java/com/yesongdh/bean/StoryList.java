package com.yesongdh.bean;

import java.util.Date;
import javax.persistence.*;

@Table(name = "story_list")
public class StoryList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 标题
     */
    private String title;

    /**
     * 类型
     */
    private String type;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 作者id号
     */
    @Column(name = "author_id")
    private String authorId;

    /**
     * 作者名称
     */
    private String author;

    /**
     * 简述
     */
    private String brief;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取标题
     *
     * @return title - 标题
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置标题
     *
     * @param title 标题
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 获取类型
     *
     * @return type - 类型
     */
    public String getType() {
        return type;
    }

    /**
     * 设置类型
     *
     * @param type 类型
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取作者id号
     *
     * @return author_id - 作者id号
     */
    public String getAuthorId() {
        return authorId;
    }

    /**
     * 设置作者id号
     *
     * @param authorId 作者id号
     */
    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    /**
     * 获取作者名称
     *
     * @return author - 作者名称
     */
    public String getAuthor() {
        return author;
    }

    /**
     * 设置作者名称
     *
     * @param author 作者名称
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * 获取简述
     *
     * @return brief - 简述
     */
    public String getBrief() {
        return brief;
    }

    /**
     * 设置简述
     *
     * @param brief 简述
     */
    public void setBrief(String brief) {
        this.brief = brief;
    }
}