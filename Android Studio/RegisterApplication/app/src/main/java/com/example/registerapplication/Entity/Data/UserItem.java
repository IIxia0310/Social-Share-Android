package com.example.registerapplication.Entity.Data;

import com.example.registerapplication.Entity.User;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class UserItem {
    private static volatile UserItem userItem;
    private MutableLiveData<String> userInterestLiveData = new MutableLiveData<>();

    // 添加 volatile 关键字以确保线程安全
    private Long userRegister;     // 注册ID；
    private Long userlogin;  // 登录ID；
    private String adminlogin;  // 登录ID；
    private String addNoteInterest;  // 发布笔记类型；


    private User user;   //当前用户信息
    private Long followSum; // 关注数量
    private Long fanSum;    // 粉丝数量
    private Long noteSum;    // 作品数量
    private Long likeSum;   // 获赞数量
    private Long collectSum;   // 获收藏数量


    public void updateUser(UserItem newUser) {
        setUser(newUser.getUser());
        setFollowSum(newUser.getFollowSum());
        setFanSum(newUser.getFanSum());
        setNoteSum(newUser.getNoteSum());
        setLikeSum(newUser.getLikeSum());
        setCollectSum(newUser.getCollectSum());
    }


    public String getAdminlogin() {
        return adminlogin;
    }

    public void setAdminlogin(String adminlogin) {
        this.adminlogin = adminlogin;
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

    public Long getUserRegister() {
        return userRegister;
    }

    public void setUserRegister(Long userRegister) {
        this.userRegister = userRegister;
    }

    public Long getUserlogin() {
        return userlogin;
    }

    public void setUserlogin(Long userlogin) {
        this.userlogin = userlogin;
    }

    public String getAddNoteInterest() {
        return addNoteInterest;
    }

    public void setAddNoteInterest(String addNoteInterest) {
        this.addNoteInterest = addNoteInterest;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User userIng) {
        this.user = userIng;
    }

    private UserItem() {

    }

    public static UserItem getUserItem() {
        if (userItem == null) {
            synchronized (UserItem.class) {
                if (userItem == null) {
                    userItem = new UserItem();
                }
            }
        }
        return userItem;
    }

    public LiveData<String> getUserInterestLiveData() {
        return userInterestLiveData;
    }

    public void setUserInterest(String userInterest) {
        userInterestLiveData.setValue(userInterest);
    }
    public static void setUserItem(UserItem instance) {
        UserItem.userItem = instance;
    }

    public void setUserInterestLiveData(MutableLiveData<String> userInterestLiveData) {
        this.userInterestLiveData = userInterestLiveData;
    }


}