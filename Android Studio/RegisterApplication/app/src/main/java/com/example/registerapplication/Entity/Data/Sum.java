package com.example.registerapplication.Entity.Data;

public class Sum {

    private Long followSum; // 关注数量
    private Long fanSum;    // 粉丝数量
    private Long likeSum;   // 获赞数量
    private Long collectSum;   // 获收藏数量

    public Sum(Long followSum, Long fanSum, Long likeSum, Long collectSum) {
        this.followSum = followSum;
        this.fanSum = fanSum;
        this.likeSum = likeSum;
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

    public Long getCollectSum() {
        return collectSum;
    }

    public void setCollectSum(Long collectSum) {
        this.collectSum = collectSum;
    }
}
