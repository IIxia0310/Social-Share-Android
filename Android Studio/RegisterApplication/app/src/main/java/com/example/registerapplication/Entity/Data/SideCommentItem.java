package com.example.registerapplication.Entity.Data;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.registerapplication.Entity.Comment;
import com.example.registerapplication.Entity.User;

import java.io.Serializable;

public class SideCommentItem implements Serializable {

    private User user; // 头像
    private Comment comment;
    private NoteItem noteItem;      //笔记

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public NoteItem getNoteItem() {
        return noteItem;
    }

    public void setNoteItem(NoteItem noteItem) {
        this.noteItem = noteItem;
    }

    public SideCommentItem(User user, Comment comment, NoteItem noteItem) {
        this.user = user;
        this.comment = comment;
        this.noteItem = noteItem;
    }
}