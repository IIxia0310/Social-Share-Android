package com.example.registerapplication.Response;//package com.example.registerapplication.entiy;


import com.example.registerapplication.Entity.Like;
import com.example.registerapplication.Entity.User;

/**
 *
 */
public class LikeResponse {
    private boolean success;
    private String message;
    private Like like;
    private Long likeSum;

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

    public Like getLike() {
        return like;
    }

    public void setLike(Like like) {
        this.like = like;
    }

    public Long getLikeSum() {
        return likeSum;
    }

    public void setLikeSum(Long likeSum) {
        this.likeSum = likeSum;
    }

    public LikeResponse(boolean success, String message, Like like, Long likeSum) {
        this.success = success;
        this.message = message;
        this.like = like;
        this.likeSum = likeSum;
    }
}
