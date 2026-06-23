package com.example.registerlogin.entity.s;

import java.io.Serializable;
import java.util.Date;

public class NoteHistoryltem implements Serializable {
    private Long id;
    private Long userId;
    private Long noteId;
    private Long browseTime;

    public NoteHistoryltem(Long userId, Long noteId, Long browseTime) {
        this.userId = userId;
        this.noteId = noteId;
        this.browseTime = browseTime;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getNoteId() {
        return noteId;
    }

    public Long getBrowseTime() {
        return browseTime;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setNoteId(Long noteId) {
        this.noteId = noteId;
    }

    public void setBrowseTime(Long browseTime) {
        this.browseTime = browseTime;
    }
}