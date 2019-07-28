package com.yesongdh.bean;

import java.util.Date;
import javax.persistence.*;

@Table(name = "story_stat")
public class StoryStat {
    /**
     * 故事id号
     */
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 故事类型
     */
    private String type;

    @Column(name = "thumb_up")
    private Integer thumbUp;

    @Column(name = "thumb_down")
    private Integer thumbDown;

    private Integer collection;

    private Integer score;

    /**
     * 创建时间
     */
    @Column(name = "gmt_create")
    private Date gmtCreate;

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
     * @return thumb_up
     */
    public Integer getThumbUp() {
        return thumbUp;
    }

    /**
     * @param thumbUp
     */
    public void setThumbUp(Integer thumbUp) {
        this.thumbUp = thumbUp;
    }

    /**
     * @return thumb_down
     */
    public Integer getThumbDown() {
        return thumbDown;
    }

    /**
     * @param thumbDown
     */
    public void setThumbDown(Integer thumbDown) {
        this.thumbDown = thumbDown;
    }

    /**
     * @return collection
     */
    public Integer getCollection() {
        return collection;
    }

    /**
     * @param collection
     */
    public void setCollection(Integer collection) {
        this.collection = collection;
    }

    /**
     * @return score
     */
    public Integer getScore() {
        return score;
    }

    /**
     * @param score
     */
    public void setScore(Integer score) {
        this.score = score;
    }

    /**
     * 获取创建时间
     *
     * @return gmt_create - 创建时间
     */
    public Date getGmtCreate() {
        return gmtCreate;
    }

    /**
     * 设置创建时间
     *
     * @param gmtCreate 创建时间
     */
    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }
}