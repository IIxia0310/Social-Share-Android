package com.example.registerapplication.Service;//package com.example.registerapplication.Service;
import com.example.registerapplication.Entity.User;
import com.example.registerapplication.Response.UserResponse;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UserService {

//    // 定义获取所有用户的接口，假设后端 API 路径为 "/users"
//    @GET("users") // HTTP GET 请求，路径为 "users"（需与后端一致）
//    Call<List<User>> getAllUsers(); // 返回值为 Call<List<User>>，表示获取用户列表的请求

    //查找用户，返回用户全部信息
    @POST("eqUserID")
    Call<UserResponse> eqUserID(@Body Long userId);


//    //查找用户兴趣
//    @POST("eqUserInterests")
//    Call<UserResponse> eqUserInterests(long userId);


    //注册
    @POST("register")
    Call<UserResponse> register(@Body User user);

    //登录
    @POST("login")
    Call<UserResponse> login(@Query("user_id") Long user_id, @Query("password") String password);

    //管理员登录
    @POST("loginAdmin")
    Call<UserResponse> loginAdmin(@Query("user_id") String user_id, @Query("password") String password);


    //修改用户兴趣
    @POST("updateUserInterests")
    Call<UserResponse> updateUserInterests(@Query("user_id") Long user_id,@Query("interests") String interests);

    //修改用户主题
    @POST("updateUserTheme")
    Call<UserResponse> updateUserTheme(@Query("user_id") Long user_id,@Query("userTheme") int userTheme);


    //修改用户信息
    @POST("userRevamp")
    Call<UserResponse> userRevamp(@Query("userIdIng") long userIdIng, @Body User user);

    //修改用户密码
    @POST("updateUserPassword")
    Call<UserResponse> updateUserPassword(@Query("user_id") Long user_id, @Query("password") String password);

    //删除用户
    @POST("deleteUser")
    Call<UserResponse> deleteUser(@Query("user_id")long userId);
}
