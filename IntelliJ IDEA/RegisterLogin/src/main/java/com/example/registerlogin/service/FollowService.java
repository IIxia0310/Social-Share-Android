package com.example.registerlogin.service;

import com.example.registerlogin.entity.*;

import java.util.List;

public interface FollowService {



    //判断关注
    int  eqFollowIng(Long user_id, Long follow_user) ;


    //关注用户列表
    List<Long> eqGz(String currentTitle,Long user_id);

    Long eqFollowSum(Long user_id);
    Long eqFanSum(Long user_id);



    Boolean addFollow(Follow follow);
    Boolean delectFollow(Long user_id,Long follow_id);




    Boolean deleteUserFollow(Long userId);
}
