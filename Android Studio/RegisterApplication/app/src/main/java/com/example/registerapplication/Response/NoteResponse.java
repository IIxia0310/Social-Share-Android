package com.example.registerapplication.Response;//package com.example.registerapplication.entiy;


import com.example.registerapplication.Entity.Note;

/**
 * 登录注册实体类
 */
public class NoteResponse {


    private boolean success;
    private String message;
    private Note note;

    public NoteResponse(boolean success, String message, Note note) {
        this.success = success;
        this.message = message;
        this.note = note;
    }

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

    public Note getNote() {
        return note;
    }

    public void setNote(Note note) {
        this.note = note;
    }
}
