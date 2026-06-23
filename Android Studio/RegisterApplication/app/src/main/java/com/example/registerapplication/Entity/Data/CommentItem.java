package com.example.registerapplication.Entity.Data;

import com.example.registerapplication.Entity.Comment;
import com.example.registerapplication.Entity.User;

import java.io.Serializable;

public class CommentItem implements Serializable {
    private Comment comment;
    private User user;

    private Long likeSum;
    private int  likeIng;

    private String commentUser;

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getLikeSum() {
        return likeSum;
    }

    public void setLikeSum(Long likeSum) {
        this.likeSum = likeSum;
    }

    public int getLikeIng() {
        return likeIng;
    }

    public void setLikeIng(int likeIng) {
        this.likeIng = likeIng;
    }

    public String getCommentUser() {
        return commentUser;
    }

    public void setCommentUser(String commentUser) {
        this.commentUser = commentUser;
    }

    public CommentItem(Comment comment, User user, Long likeSum, int likeIng, String commentUser) {
        this.comment = comment;
        this.user = user;
        this.likeSum = likeSum;
        this.likeIng = likeIng;
        this.commentUser = commentUser;
    }

}