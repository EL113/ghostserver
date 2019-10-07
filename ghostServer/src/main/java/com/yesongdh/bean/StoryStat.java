package com.yesongdh.bean;

import java.util.Date;
import javax.persistence.*;

@Table(name = "story_stat")
public class StoryStat {
    /**
     * ����id��
     */
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * ��������
     */
    private String type;

    @Column(name = "thumb_up")
    private Integer thumbUp;

    @Column(name = "thumb_down")
    private Integer thumbDown;

    private Integer collection;

    private Integer score;

    /**
     * ����ʱ��
     */
    @Column(name = "gmt_create")
    private Date gmtCreate;

    /**
     * ��ȡ����id��
     *
     * @return id - ����id��
     */
    public Integer getId() {
        return id;
    }

    /**
     * ���ù���id��
     *
     * @param id ����id��
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * ��ȡ��������
     *
     * @return type - ��������
     */
    public String getType() {
        return type;
    }

    /**
     * ���ù�������
     *
     * @param type ��������
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
     * ��ȡ����ʱ��
     *
     * @return gmt_create - ����ʱ��
     */
    public Date getGmtCreate() {
        return gmtCreate;
    }

    /**
     * ���ô���ʱ��
     *
     * @param gmtCreate ����ʱ��
     */
    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }
    
    public void setDefault() {
    	this.collection = 0;
    	this.thumbDown = 0;
    	this.thumbUp = 0;
    	this.score = 0;
    }
}