package com.example.registerapplication.Entity;

import java.io.Serializable;

/**
 *  评论实体类
 */
public class Comment implements Serializable {

    private Long commentId; //评论记录唯一标识
    private Long userId; //用户id
    private Long noteId; //被评论笔记的id
    private String content; //评论内容
    private String createTime; //评论时间

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Comment(Long commentId, Long userId, Long noteId, String content, String createTime) {
        this.commentId = commentId;
        this.userId = userId;
        this.noteId = noteId;
        this.content = content;
        this.createTime = createTime;
    }
}