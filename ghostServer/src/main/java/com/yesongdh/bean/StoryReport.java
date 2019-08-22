package com.yesongdh.bean;

import java.util.Date;
import javax.persistence.*;

@Table(name = "story_report")
public class StoryReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 故事id
     */
    @Column(name = "story_id")
    private Integer storyId;

    /**
     * 举报理由
     */
    private String reason;

    /**
     * 处理人
     */
    private String handler;

    /**
     * 处理结果
     */
    private String result;

    /**
     * 0 未处理 1 已处理
     */
    private Boolean status;

    /**
     * 修改时间
     */
    @Column(name = "modify_time")
    private Date modifyTime;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;
    
    @Transient
    private Date createTime0;
    
    @Transient
    private Date createTime1;

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
     * 获取故事id
     *
     * @return story_id - 故事id
     */
    public Integer getStoryId() {
        return storyId;
    }

    /**
     * 设置故事id
     *
     * @param storyId 故事id
     */
    public void setStoryId(Integer storyId) {
        this.storyId = storyId;
    }

    /**
     * 获取举报理由
     *
     * @return reason - 举报理由
     */
    public String getReason() {
        return reason;
    }

    /**
     * 设置举报理由
     *
     * @param reason 举报理由
     */
    public void setReason(String reason) {
        this.reason = reason;
    }

    /**
     * 获取处理人
     *
     * @return handler - 处理人
     */
    public String getHandler() {
        return handler;
    }

    /**
     * 设置处理人
     *
     * @param handler 处理人
     */
    public void setHandler(String handler) {
        this.handler = handler;
    }

    /**
     * 获取处理结果
     *
     * @return result - 处理结果
     */
    public String getResult() {
        return result;
    }

    /**
     * 设置处理结果
     *
     * @param result 处理结果
     */
    public void setResult(String result) {
        this.result = result;
    }

    /**
     * 获取0 未处理 1 已处理
     *
     * @return status - 0 未处理 1 已处理
     */
    public Boolean getStatus() {
        return status;
    }

    /**
     * 设置0 未处理 1 已处理
     *
     * @param status 0 未处理 1 已处理
     */
    public void setStatus(Boolean status) {
        this.status = status;
    }

    /**
     * 获取修改时间
     *
     * @return modify_time - 修改时间
     */
    public Date getModifyTime() {
        return modifyTime;
    }

    /**
     * 设置修改时间
     *
     * @param modifyTime 修改时间
     */
    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", storyId=").append(storyId);
        sb.append(", reason=").append(reason);
        sb.append(", handler=").append(handler);
        sb.append(", result=").append(result);
        sb.append(", status=").append(status);
        sb.append(", modifyTime=").append(modifyTime);
        sb.append(", createTime=").append(createTime);
        sb.append("]");
        return sb.toString();
    }
}