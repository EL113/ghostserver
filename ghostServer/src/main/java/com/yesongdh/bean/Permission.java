package com.yesongdh.bean;

import java.util.Date;
import javax.persistence.*;

@Table(name = "web_permission")
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 权限名称
     */
    @Column(name = "permission_name")
    private String permissionName;

    @Column(name = "gmt_create")
    private Date gmtCreate;

    /**
     * 权限对应的uri
     */
    private String uri;
    
    @Transient
    private String roleId;

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
     * 获取权限名称
     *
     * @return permission_name - 权限名称
     */
    public String getPermissionName() {
        return permissionName;
    }

    /**
     * 设置权限名称
     *
     * @param permissionName 权限名称
     */
    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
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

    /**
     * 获取权限对应的uri
     *
     * @return uri - 权限对应的uri
     */
    public String getUri() {
        return uri;
    }

    /**
     * 设置权限对应的uri
     *
     * @param uri 权限对应的uri
     */
    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getRoleId() {
		return roleId;
	}
    
    public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
}