package com.example.registerlogin.Response;

import com.example.registerlogin.entity.s.NoteItem;

import java.util.List;

/**
 * 该类用于封装包含列表数据的响应信息，是一个通用的响应类，
 * 可用于不同类型的列表数据响应，具备响应结果、消息、列表数据、
 * 总页数和总元素数等属性。
 * @param <T> 列表数据的类型
 */

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