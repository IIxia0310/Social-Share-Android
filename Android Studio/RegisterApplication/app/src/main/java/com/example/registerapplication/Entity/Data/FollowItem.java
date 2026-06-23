package com.example.registerapplication.Entity.Data;

import com.example.registerapplication.Entity.Note;
import com.example.registerapplication.Entity.User;

import java.io.Serializable;

public class FollowItem implements Serializable{

    private User user;
    private int followIng;
    private int followFanIng;
    private String createTime; // 注册时间

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getFollowIng() {
        return followIng;
    }

    public void setFollowIng(int followIng) {
        this.followIng = followIng;
    }

    public int getFollowFanIng() {
        return followFanIng;
    }

    public void setFollowFanIng(int followFanIng) {
        this.followFanIng = followFanIng;
    }

    public FollowItem(User user, int followIng, int followFanIng, String createTime) {
        this.user = user;
        this.followIng = followIng;
        this.followFanIng = followFanIng;
        this.createTime = createTime;
    }
}