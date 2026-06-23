package com.example.registerlogin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

public class Admin {

    @TableId(type = IdType.AUTO) // 如果是自增主键
    private Long adminId;
    private String adminUser;
    private String password;
    private String portrait_image;

    public Admin(Long adminId, String adminUser, String password, String portrait_image) {
        this.adminId = adminId;
        this.adminUser = adminUser;
        this.password = password;
        this.portrait_image = portrait_image;
    }

    public String getPortrait_image() {
        return portrait_image;
    }

    public void setPortrait_image(String portrait_image) {
        this.portrait_image = portrait_image;
    }

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    public String getAdminUser() {
        return adminUser;
    }

    public void setAdminUser(String adminUser) {
        this.adminUser = adminUser;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
