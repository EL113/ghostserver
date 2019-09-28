package com.yesongdh.bean;

import java.util.Date;
import java.util.List;
import javax.persistence.*;

@Table(name = "web_role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 角色名称
     */
    @Column(name = "role_name")
    private String roleName;

    /**
     * 0 已启用 1 未启用
     */
    private String status;

    @Column(name = "gmt_create")
    private Date gmtCreate;

    @Transient
    private List<String> perms;
    
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
     * 获取角色名称
     *
     * @return role_name - 角色名称
     */
    public String getRoleName() {
        return roleName;
    }

    /**
     * 设置角色名称
     *
     * @param roleName 角色名称
     */
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    /**
     * 获取0 已启用 1 未启用
     *
     * @return status - 0 已启用 1 未启用
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置0 已启用 1 未启用
     *
     * @param status 0 已启用 1 未启用
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return gmt_create
     */
    public Date getGmtCreate() {
        return gmtCreate;
    }

    /**
     * @param gmtCreate
     */
    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", roleName=").append(roleName);
        sb.append(", status=").append(status);
        sb.append(", gmtCreate=").append(gmtCreate);
        sb.append("]");
        return sb.toString();
    }

	public void setPerms(List<String> perms) {
		this.perms = perms;
	}
	
	public List<String> getPerms() {
		return perms;
	}
}