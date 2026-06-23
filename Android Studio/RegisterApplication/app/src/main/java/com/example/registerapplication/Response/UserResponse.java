package com.example.registerapplication.Response;//package com.example.registerapplication.entiy;


import com.example.registerapplication.Entity.User;

/**
 * 登录注册实体类
 */
public class UserResponse {

    private boolean success; //
    private String message; //
    private User user;

    private Long followSum; // 关注数量
    private Long fanSum;    // 粉丝数量

    private Long noteSum;    // 笔记数量
    private Long likeSum;   // 获赞数量
    private Long collectSum;   // 获收藏数量


    public UserResponse(boolean success, String message, User user, Long followSum, Long fanSum, Long noteSum, Long likeSum, Long collectSum) {
        this.success = success;
        this.message = message;
        this.user = user;
        this.followSum = followSum;
        this.fanSum = fanSum;
        this.noteSum = noteSum;
        this.likeSum = likeSum;
        this.collectSum = collectSum;
    }

    public Long getNoteSum() {
        return noteSum;
    }

    public void setNoteSum(Long noteSum) {
        this.noteSum = noteSum;
    }


    public Long getCollectSum() {
        return collectSum;
    }

    public void setCollectSum(Long collectSum) {
        this.collectSum = collectSum;
    }

    public Long getFollowSum() {
        return followSum;
    }

    public void setFollowSum(Long followSum) {
        this.followSum = followSum;
    }

    public Long getFanSum() {
        return fanSum;
    }

    public void setFanSum(Long fanSum) {
        this.fanSum = fanSum;
    }

    public Long getLikeSum() {
        return likeSum;
    }

    public void setLikeSum(Long likeSum) {
        this.likeSum = likeSum;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


}
