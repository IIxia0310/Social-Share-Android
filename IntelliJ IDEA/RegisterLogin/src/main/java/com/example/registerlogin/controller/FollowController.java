package com.example.registerlogin.controller;

import com.example.registerlogin.Response.ListTResponse;
import com.example.registerlogin.Response.NoteResponse;
import com.example.registerlogin.db.CollectDao;
import com.example.registerlogin.db.FollowDao;
import com.example.registerlogin.entity.Collect;
import com.example.registerlogin.entity.Follow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FollowController {
    private static final Logger logger = LoggerFactory.getLogger(FollowController.class);


    @Autowired
    private FollowDao followDao;



    //添加关注
    @PostMapping("/addFollow")
    public NoteResponse addFollow(@RequestBody Follow follow) {
        NoteResponse response = new NoteResponse();
        boolean result = followDao.addFollow(follow);
        if (result) {
            response.setSuccess(true);
            response.setMessage("关注成功");
        } else {
            response.setSuccess(false);
            response.setMessage("关注失败");
        }
        return response;
    }


    //删除关注
    @PostMapping("/delectFollow")
    public NoteResponse delectFollow(@RequestParam("user_id") Long user_id, @RequestParam("follow_user") Long follow_user) {
        NoteResponse response = new NoteResponse();

        boolean result = followDao.delectFollow(user_id,follow_user);
        if (result) {
            response.setSuccess(true);
            response.setMessage("取消关注成功");
        } else {
            response.setSuccess(false);
            response.setMessage("取消关注失败");
        }
        return response;
    }

    //获取关注状态
    @PostMapping("/eqtFollowIng")
    public ListTResponse<Integer> eqtFollowIng(@RequestParam("userIng") Long userIng, @RequestParam("user_id") Long user_id) {
        ListTResponse response = new ListTResponse();


        int followIng = followDao.eqFollowIng(userIng,user_id);



        response.setSuccess(true);
        response.setMessage("查询成功");
        response.setList(followIng);
        return response;

    }


}