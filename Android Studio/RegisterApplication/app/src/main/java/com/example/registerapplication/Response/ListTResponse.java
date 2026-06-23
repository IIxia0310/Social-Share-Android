package com.example.registerapplication.Response;


import java.util.List;

public class ListTResponse<T> {
    private boolean success;
    private String message;
    private T list;

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

    public T getList() {
        return list;
    }

    public void setList(T list) {
        this.list = list;
    }

}