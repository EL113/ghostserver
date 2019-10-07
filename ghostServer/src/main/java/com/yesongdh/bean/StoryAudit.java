package com.yesongdh.bean;

import java.util.Date;
import java.util.List;

import javax.persistence.*;

@Table(name = "story_audit")
public class StoryAudit {
    /**
     * 故事id号
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer rid;

    private Integer id;
    
    /**
     * 故事子id
     */
    @Column(name = "sub_id")
    private Integer subId;

    private String title;

    private String author;

    /**
     * 故事类型
     */
    private String type;

    /**
     * 故事内容
     */
    private String content;

    /**
     * 简述
     */
    private String brief;

    /**
     * 审核状态 0 审核中 1 通过 2 未通过
     */
    private String status;
    
    /**
     * 用户id号
     */
    @Column(name = "author_id")
    private String authorId;

    /**
     * 原因
     */
    private String reason;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    @Transient
    private Date createDate0;
    
    @Transient
    private Date createDate1;
    
    @Transient
    private List<String> ids;
    
    public List<String> getIds() {
		return ids;
	}
    
    public void setIds(List<String> ids) {
		this.ids = ids;
	}
    
    public Integer getRid() {
		return rid;
	}
    
    public void setRid(Integer rid) {
		this.rid = rid;
	}
    
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
     * 获取故事子id
     *
     * @return sub_id - 故事子id
     */
    public Integer getSubId() {
        return subId;
    }

    /**
     * 设置故事子id
     *
     * @param subId 故事子id
     */
    public void setSubId(Integer subId) {
        this.subId = subId;
    }

    /**
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * @param author
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * 获取故事类型
     *
     * @return type - 故事类型
     */
    public String getType() {
        return type;
    }

    /**
     * 设置故事类型
     *
     * @param type 故事类型
     */
    public void setType(String type) {
        this.type = type;
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

    /**
     * 获取审核状态 0 审核中 1 通过 2 未通过
     *
     * @return status - 审核状态 0 审核中 1 通过 2 未通过
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置审核状态 0 审核中 1 通过 2 未通过
     *
     * @param status 审核状态 0 审核中 1 通过 2 未通过
     */
    public void setStatus(String status) {
        this.status = status;
    }

    public String getAuthorId() {
		return authorId;
	}
    
    public void setAuthorId(String authorId) {
		this.authorId = authorId;
	}

    /**
     * 获取原因
     *
     * @return reason - 原因
     */
    public String getReason() {
        return reason;
    }

    /**
     * 设置原因
     *
     * @param reason 原因
     */
    public void setReason(String reason) {
        this.reason = reason;
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
    
    public Date getCreateDate0() {
		return createDate0;
	}

	public void setCreateDate0(Date createDate0) {
		this.createDate0 = createDate0;
	}

	public Date getCreateDate1() {
		return createDate1;
	}

	public void setCreateDate1(Date createDate1) {
		this.createDate1 = createDate1;
	}

}