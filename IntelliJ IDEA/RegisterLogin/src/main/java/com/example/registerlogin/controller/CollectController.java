package com.example.registerlogin.controller;

import com.example.registerlogin.Response.NoteResponse;
import com.example.registerlogin.db.CollectDao;
import com.example.registerlogin.db.LikeDao;
import com.example.registerlogin.entity.Collect;
import com.example.registerlogin.entity.Like;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CollectController {
    private static final Logger logger = LoggerFactory.getLogger(CollectController.class);


    @Autowired
    private CollectDao collectDao;



    //添加点赞
    @PostMapping("/addCollect")
    public NoteResponse addCollect(@RequestBody Collect collect) {
        NoteResponse response = new NoteResponse();
        boolean result = collectDao.addCollect(collect);
        if (result) {
            response.setSuccess(true);
            response.setMessage("笔记添加成功");
        } else {
            response.setSuccess(false);
            response.setMessage("笔记添加失败");
        }
        return response;
    }




    //删除
    @PostMapping("/deleteCollect")
    public NoteResponse deleteCollect(@RequestParam("user_id") Long user_id, @RequestParam("note_id") Long note_id) {
        NoteResponse response = new NoteResponse();

        boolean result = collectDao.deleteCollect(user_id,note_id);
        if (result) {
            response.setSuccess(true);
            response.setMessage("笔记添加成功");
        } else {
            response.setSuccess(false);
            response.setMessage("笔记添加失败");
        }
        return response;
    }



}