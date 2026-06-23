package com.example.registerlogin.controller;

import com.example.registerlogin.Response.ListTResponse;
import com.example.registerlogin.Response.NoteResponse;

import com.example.registerlogin.db.*;
import com.example.registerlogin.entity.*;

import com.example.registerlogin.entity.s.MessageItem;
import com.example.registerlogin.entity.s.NoteItem;
import com.example.registerlogin.entity.s.SideCommentItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class LikeController {
    private static final Logger logger = LoggerFactory.getLogger(LikeController.class);


    @Autowired
    private LikeDao likeDao;


    @Autowired
    private CollectDao collectDao;

    @Autowired
    private CommentDao commentDao;

    @Autowired
    private NoteDao noteDao;
    @Autowired
    private UserDao userDao;
    //我收到的点赞和收藏
    @RequestMapping(value = "/mssage1GView1Like", method = {RequestMethod.GET, RequestMethod.POST})
    public ListTResponse<List<SideCommentItem>> mssage1GView1Like(@RequestParam Long userIng) {
        ListTResponse<List<SideCommentItem>> response = new ListTResponse<>();
        List<SideCommentItem> sideCommentItemLists = new ArrayList<>();

        List<Note> noteList = noteDao.eqUserID(userIng);
        for (Note note : noteList) {
            Long likeSum = likeDao.eqNoteLikeSum(note.getNoteId());
            int likeIng = likeDao.eqLikeIng(userIng, note.getNoteId());

            // 处理笔记点赞
            List<Like> noteLikeList = likeDao.eqNoteLike(note.getNoteId());
            for (Like like : noteLikeList) {
                User user = userDao.eqID(like.getUserId());
                if (user != null) {
                    Comment comment = new Comment();
                    comment.setContent("点赞了你的笔记");
                    comment.setCreateTime(like.getCreateTime());

                    NoteItem noteItem = new NoteItem();
                    noteItem.setNote(note);
                    noteItem.setUser(user);
                    noteItem.setLikeSum(likeSum);
                    noteItem.setLikeIng(likeIng);

                    SideCommentItem sideCommentItem = new SideCommentItem();
                    sideCommentItem.setComment(comment);
                    sideCommentItem.setUser(user);
                    sideCommentItem.setNoteItem(noteItem);
                    sideCommentItemLists.add(sideCommentItem);
                }
            }

            // 处理笔记收藏
            List<Collect> collectList = collectDao.eqNoteCollect(note.getNoteId());
            for (Collect collect : collectList) {
                User user = userDao.eqID(collect.getUserId());
                if (user != null) {
                    Comment comment = new Comment();
                    comment.setContent( "收藏了你的笔记");
                    comment.setCreateTime(collect.getCreateTime());

                    NoteItem noteItem = new NoteItem();
                    noteItem.setNote(note);
                    noteItem.setUser(user);
                    noteItem.setLikeSum(likeSum);
                    noteItem.setLikeIng(likeIng);

                    SideCommentItem sideCommentItem = new SideCommentItem();
                    sideCommentItem.setComment(comment);
                    sideCommentItem.setUser(user);
                    sideCommentItem.setNoteItem(noteItem);
                    sideCommentItemLists.add(sideCommentItem);
                }
            }

            // 处理评论点赞
            List<Comment> commentList = commentDao.eqUserCommentList(userIng);
            for (Comment comment1 : commentList) {
                List<Like> commentLikeList = likeDao.eqCommentLike(comment1.getCommentId());
                for (Like like : commentLikeList) {
                    User user = userDao.eqID(like.getUserId());
                    if (user != null) {
                        Comment comment = new Comment();
                        comment.setContent("点赞了你的评论");
                        comment.setCreateTime(like.getCreateTime());

                        NoteItem noteItem = new NoteItem();
                        noteItem.setNote(note);
                        noteItem.setUser(user);
                        noteItem.setLikeSum(likeSum);
                        noteItem.setLikeIng(likeIng);

                        SideCommentItem sideCommentItem = new SideCommentItem();
                        sideCommentItem.setComment(comment);
                        sideCommentItem.setUser(user);
                        sideCommentItem.setNoteItem(noteItem);
                        sideCommentItemLists.add(sideCommentItem);
                    }
                }
            }
        }

        response.setSuccess(true);
        response.setMessage("查询成功");
        response.setList(sideCommentItemLists);
        return response;
    }






    //添加点赞
    @PostMapping("/addLike")
    public NoteResponse addLike(@RequestBody Like like) {
        NoteResponse response = new NoteResponse();
        boolean result = likeDao.addLike(like);
        if (result) {
            response.setSuccess(true);
            response.setMessage("笔记添加成功");
        } else {
            response.setSuccess(false);
            response.setMessage("笔记添加失败");
        }
        return response;
    }


    //添加评论点赞
    @PostMapping("/addLikeComment")
    public NoteResponse addLikeComment(@RequestBody Like like) {
        NoteResponse response = new NoteResponse();
        boolean result = likeDao.addLikeComment(like);
        if (result) {
            response.setSuccess(true);
            response.setMessage("笔记添加成功");
        } else {
            response.setSuccess(false);
            response.setMessage("笔记添加失败");
        }
        return response;
    }

    //删除点赞
    @PostMapping("/delectLike")
    public NoteResponse delectLike(@RequestParam("user_id") Long user_id, @RequestParam("note_id") Long note_id) {
        NoteResponse response = new NoteResponse();

        boolean result = likeDao.delectLike(user_id,note_id);
        if (result) {
            response.setSuccess(true);
            response.setMessage("删除成功");
        } else {
            response.setSuccess(false);
            response.setMessage("删除失败");
        }
        return response;
    }




    //删除评论的点赞
    @PostMapping("/deleteLikeComment")
    public NoteResponse deleteLikeComment(@RequestParam("user_id") Long user_id, @RequestParam("comment_id") Long comment_id) {
        NoteResponse response = new NoteResponse();

        boolean result = likeDao.deleteLikeComment(user_id,comment_id);
        if (result) {
            response.setSuccess(true);
            response.setMessage("笔记添加成功");
        } else {
            response.setSuccess(false);
            response.setMessage("笔记添加失败");
        }
        return response;
    }


    @PostMapping("/eqCommentLikeSum")
    public ListTResponse eqCommentLikeSum(@RequestParam("commentId") Long commentId) {
        ListTResponse response = new ListTResponse();
        try {
            // 调用 DAO 方法查询评论的点赞数
            Long likeSum = likeDao.eqCommentLikeSum(commentId);
            // 将查询结果设置到响应对象中
            response.setSuccess(true);
            response.setMessage("查询成功");
            response.setList(likeSum);
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("查询失败：" + e.getMessage());
        }
        return response;
    }



}