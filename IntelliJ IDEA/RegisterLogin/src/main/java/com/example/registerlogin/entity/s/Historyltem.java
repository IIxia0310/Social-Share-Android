//package com.example.registerlogin.entity.s;
//
//import com.baomidou.mybatisplus.annotation.IdType;
//import com.baomidou.mybatisplus.annotation.TableId;
//
//import java.util.Date;
//
//public class Historyltem {
//
//
//    @TableId(value = "history_id", type = IdType.AUTO) // value 为主键字段名，type 为主键生成策略
//
//
//    // 浏览记录的唯一标识
//    private Long historyId;
//    // 笔记 ID
//    private Long noteId;
//    // 用户 ID
//    private Long userId;
//    // 点赞总数
//    private Long likeSum;
//    // 当前是否点赞（0 表示否，1 表示是）
//    private int likeIng;
//    // 收藏总数
//    private Long collectSum;
//    // 当前是否收藏（0 表示否，1 表示是）
//    private int collectIng;
//    // 当前是否关注（0 表示否，1 表示是）
//    private int followIng;
//    // 浏览时间
//    private Date viewTime;
//
//    public long getHistoryId() {
//        return historyId;
//    }
//
//    public void setHistoryId(Long historyId) {
//        this.historyId = historyId;
//    }
//
//    public long getNoteId() {
//        return noteId;
//    }
//
//    public void setNoteId(Long noteId) {
//        this.noteId = noteId;
//    }
//
//    public long getUserId() {
//        return userId;
//    }
//
//    public void setUserId(Long userId) {
//        this.userId = userId;
//    }
//
//    public long getLikeSum() {
//        return likeSum;
//    }
//
//    public void setLikeSum(Long likeSum) {
//        this.likeSum = likeSum;
//    }
//
//    public int getLikeIng() {
//        return likeIng;
//    }
//
//    public void setLikeIng(int likeIng) {
//        this.likeIng = likeIng;
//    }
//
//    public long getCollectSum() {
//        return collectSum;
//    }
//
//    public void setCollectSum(Long collectSum) {
//        this.collectSum = collectSum;
//    }
//
//    public int getCollectIng() {
//        return collectIng;
//    }
//
//    public void setCollectIng(int collectIng) {
//        this.collectIng = collectIng;
//    }
//
//    public int getFollowIng() {
//        return followIng;
//    }
//
//    public void setFollowIng(int followIng) {
//        this.followIng = followIng;
//    }
//
//    public Date getViewTime() {
//        return viewTime;
//    }
//
//    public void setViewTime(Date viewTime) {
//        this.viewTime = viewTime;
//    }
//}