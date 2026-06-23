package com.example.registerapplication.Entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;

/**
 * 用户实体类
 */
public class User implements Serializable {

    private Long userId; // 用户 ID
    private String password; // 用户密码
    private String username; // 用户名
    private int theme; // 主题
    private String sex; // 性别
    private String interests; // 兴趣
    private String signature; // 个签
    private String location; // 定位
    private String portraitImage; // 头像
    private String backgroundImage; // 背景
    private String birthdayTime; // 出生日期
    private String createTime; // 注册时间
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public User(Long userId, String password, String username, int theme, String sex, String interests, String signature, String location, String portraitImage, String backgroundImage, String birthdayTime, String createTime) {
        this.userId = userId;
        this.password = password;
        this.username = username;
        this.theme = theme;
        this.sex = sex;
        this.interests = interests;
        this.signature = signature;
        this.location = location;
        this.portraitImage = portraitImage;
        this.backgroundImage = backgroundImage;
        this.birthdayTime = birthdayTime;
        this.createTime = createTime;
    }

    public int getTheme() {
        return theme;
    }

    public void setTheme(int theme) {
        this.theme = theme;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getInterests() {
        return interests;
    }

    public void setInterests(String interests) {
        this.interests = interests;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPortraitImage() {
        return portraitImage;
    }

    public void setPortraitImage(String portraitImage) {
        this.portraitImage = portraitImage;
    }

    public String getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(String backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public String getBirthdayTime() {
        return birthdayTime;
    }

    public void setBirthdayTime(String birthdayTime) {
        this.birthdayTime = birthdayTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public static SimpleDateFormat getDateFormat() {
        return DATE_FORMAT;
    }

    public User() {
    }


}