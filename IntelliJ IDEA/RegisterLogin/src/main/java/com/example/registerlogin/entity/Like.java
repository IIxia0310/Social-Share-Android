package com.example.registerlogin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 点赞实体类
 */
@TableName("`like`")
public class Like {


    @TableId(value = "like_id", type = IdType.AUTO) // value 为主键字段名，type 为主键生成策略


    private Long likeId; //点赞记录唯一标识
    private Long userId; //用户id
    private Long noteId; //被点赞笔记
    private Long commentId; //被点赞笔记
    private String createTime; //点赞时间


    @Override
    public String toString() {
        return "Like{" +
                "likeId=" + likeId +
                ", userId=" + userId +
                ", noteId=" + noteId +
                ", commentId=" + commentId +
                ", createTime='" + createTime + '\'' +
                '}';
    }

    public Long getLikeId() {
        return likeId;
    }

    public void setLikeId(Long likeId) {
        this.likeId = likeId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getNoteId() {
        return noteId;
    }

    public void setNoteId(Long noteId) {
        this.noteId = noteId;
    }

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Like(Long likeId, Long userId, Long noteId, Long commentId, String createTime) {
        this.likeId = likeId;
        this.userId = userId;
        this.noteId = noteId;
        this.commentId = commentId;
        this.createTime = createTime;
    }
}