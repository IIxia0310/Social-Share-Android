package com.example.registerlogin.Response;

import com.example.registerlogin.entity.Like;
import com.example.registerlogin.entity.Note;
import lombok.Data;

@Data
public class LikeResponse<T>{
    private boolean success;
    private String message;
    private Like like;

    private Long likeSum;

}
