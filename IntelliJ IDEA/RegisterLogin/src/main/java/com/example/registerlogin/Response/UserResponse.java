package com.example.registerlogin.Response;

import com.example.registerlogin.entity.Admin;
import com.example.registerlogin.entity.User;
import lombok.Data;

@Data
public class UserResponse<T>{
    private boolean success; //
    private String message; //
    private User user;
    private Admin admin;

    private Long followSum; // 关注数量
    private Long fanSum;    // 粉丝数量
    private Long noteSum;    // 粉丝数量
    private Long likeSum;   // 获赞数量
    private Long collectSum;   // 获收藏数量


}
