package com.example.registerlogin.entity.s;

public class MessageItem {
    private String portraitImage; // 头像
    private String name;   //聊天对象姓名
    private Long user_id;    // 聊天对象id
    private int followIng;   // 是否关注聊天对象

    private String content;   //最新聊天内容
    private String time;       //最新聊天时间
    private Long unreadSum;    //与当前用户的未读的信息数量

    public MessageItem(String portraitImage, String name, Long user_id, int followIng, String content, String time, Long unreadSum) {
        this.portraitImage = portraitImage;
        this.name = name;
        this.user_id = user_id;
        this.followIng = followIng;
        this.content = content;
        this.time = time;
        this.unreadSum = unreadSum;
    }

    public String getPortraitImage() {
        return portraitImage;
    }

    public void setPortraitImage(String portraitImage) {
        this.portraitImage = portraitImage;
    }



    public Long getUnreadSum() {
        return unreadSum;
    }

    public void setUnreadSum(Long unreadSum) {
        this.unreadSum = unreadSum;
    }

    public int getFollowIng() {
        return followIng;
    }

    public void setFollowIng(int followIng) {
        this.followIng = followIng;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}