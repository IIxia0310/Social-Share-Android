package com.example.registerlogin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

/**
 *  关注实体类
 */
public class Follow {

    @TableId(value = "follow_id", type = IdType.AUTO) // value 为主键字段名，type 为主键生成策略


    private Long followId; //关注记录唯一标识
    private Long userId; //关注者的用户id
    private Long followUser; //被关注者的用户id
    private String createTime; //关注时间

    public Long getFollowId() {
        return followId;
    }

    public void setFollowId(Long followId) {
        this.followId = followId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getFollowUser() {
        return followUser;
    }

    public void setFollowUser(Long followUser) {
        this.followUser = followUser;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Follow(Long followId, Long userId, Long followUser, String createTime) {
        this.followId = followId;
        this.userId = userId;
        this.followUser = followUser;
        this.createTime = createTime;
    }
}