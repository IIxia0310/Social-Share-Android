package com.example.registerlogin.entity.s;


import com.example.registerlogin.entity.Note;
import com.example.registerlogin.entity.User;

import java.io.Serializable;

public class NoteDetailsItem implements Serializable {

    private Note note;
    private User user;
    private Long likeSum;
    private int likeIng;

    private Long collectSum;
    private int collectIng;

    private int followIng;

    public NoteDetailsItem() {

    }

    public Note getNote() {
        return note;
    }

    public void setNote(Note note) {
        this.note = note;
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

    public Long getCollectSum() {
        return collectSum;
    }

    public void setCollectSum(Long collectSum) {
        this.collectSum = collectSum;
    }

    public int getCollectIng() {
        return collectIng;
    }

    public void setCollectIng(int collectIng) {
        this.collectIng = collectIng;
    }

    public int getFollowIng() {
        return followIng;
    }

    public void setFollowIng(int followIng) {
        this.followIng = followIng;
    }

    public NoteDetailsItem(Note note, User user, Long likeSum, int likeIng, Long collectSum, int collectIng, int followIng) {
        this.note = note;
        this.user = user;
        this.likeSum = likeSum;
        this.likeIng = likeIng;
        this.collectSum = collectSum;
        this.collectIng = collectIng;
        this.followIng = followIng;
    }



}