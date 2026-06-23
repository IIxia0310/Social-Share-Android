package com.example.registerlogin.controller;

import com.example.registerlogin.Response.ListTResponse;
import com.example.registerlogin.Response.NoteResponse;
import com.example.registerlogin.db.*;
import com.example.registerlogin.entity.*;
import com.example.registerlogin.entity.s.CommentItem;
import com.example.registerlogin.entity.s.NoteItem;
import com.example.registerlogin.entity.s.SideCommentItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
public class CommentController {
    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);


    @Autowired
    private CommentDao commentDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private NoteDao noteDao;
    @Autowired
    private LikeDao likeDao;

    // 查询我的评论
    @RequestMapping(value = "/side2Comment", method = {RequestMethod.GET, RequestMethod.POST})
    public ListTResponse<List<SideCommentItem>> side2Comment(@RequestParam Long userIng) {
        ListTResponse<List<SideCommentItem>> response = new ListTResponse<>();


        List<Comment> commentList = commentDao.eqUserCommentList(userIng);
        List<SideCommentItem> sideCommentItemLists = new ArrayList<>();
        Long likeSum = null;
        int likeIng = 0;
        User user1 = new User();

        for (Comment comment : commentList) {
            User user =  userDao.eqID( comment.getUserId());
            Note note = noteDao.eqNoteID(comment.getNoteId());

            if (note!=null){
                user1 =  userDao.eqID(comment.getUserId());
                likeSum  = likeDao.eqNoteLikeSum(note.getNoteId());
                likeIng = likeDao.eqLikeIng(userIng,note.getNoteId());

            }

            NoteItem noteItem = new NoteItem();
            noteItem.setNote(note);
            noteItem.setUser(user1);
            noteItem.setLikeSum(likeSum);
            noteItem.setLikeIng(likeIng);

            SideCommentItem sideCommentItem = new SideCommentItem();
            sideCommentItem.setComment(comment);
            sideCommentItem.setUser(user);
            sideCommentItem.setNoteItem(noteItem);
            sideCommentItemLists.add(sideCommentItem);

        }

        response.setSuccess(true);
        response.setMessage("查询成功");
        response.setList(sideCommentItemLists);
        return response;
    }


    // 我收到的评论
    @RequestMapping(value = "/mssage1GView3Comment", method = {RequestMethod.GET, RequestMethod.POST})
    public ListTResponse<List<SideCommentItem>> mssage1GView3Comment(@RequestParam Long userIng) {
        ListTResponse<List<SideCommentItem>> response = new ListTResponse<>();

        List<SideCommentItem> sideCommentItemLists = new ArrayList<>();
        Long likeSum = null;
        int likeIng = 0;

        List<Note> noteList =  noteDao.eqUserID(userIng);

        for (Note note : noteList){
            List<Comment> commentList = commentDao.eqNoteCommentList(note.getNoteId());

            for (Comment comment: commentList){

                User user = userDao.eqID(comment.getUserId());
                Note note1 = noteDao.eqNoteID(comment.getNoteId());

                if (note1!=null){
                    likeSum  = likeDao.eqNoteLikeSum(note1.getNoteId());
                    likeIng = likeDao.eqLikeIng(userIng,note1.getNoteId());

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
        response.setSuccess(true);
        response.setMessage("查询成功");
        response.setList(sideCommentItemLists);
        return response;
    }


    //评论列表
    @RequestMapping(value = "/commentItem", method = {RequestMethod.GET, RequestMethod.POST})
    public ListTResponse<List<CommentItem>> noteItems(@RequestParam Long noteIng, Long noteUsreId ,Long userIng){
        ListTResponse<List<CommentItem>> response = new ListTResponse();

        List<Comment> commentList = commentDao.eqNoteCommentList(noteIng);
        List<CommentItem> commentItemList = new ArrayList<>();

        for (Comment comment : commentList) {

            User user =  userDao.eqID(comment.getUserId());
            Long likeSum = likeDao.eqCommentLikeSum(comment.getCommentId());
            int likeIng = likeDao.eqLikeIngComment(userIng,comment.getCommentId());

            //查询评论的用户是自己还是楼主还是群众
            String commentUser = commentDao.eqCommentUser(comment.getCommentId(),noteUsreId,userIng);

            CommentItem commentItem = convertToCommentItem(comment,user,likeSum,likeIng,commentUser);
            commentItemList.add(commentItem);

        }

        response.setSuccess(true);
        response.setMessage("查询成功");
        response.setList(commentItemList);
        return response;
    }

    // 将 Note 对象转换为 NoteItem 对象的方法
    private CommentItem convertToCommentItem(Comment comment, User user,Long likeSum,int likeIng,String commentUser) {
        CommentItem commentItem = new CommentItem();
        // 假设 NoteItem 有对应的 setter 方法
        commentItem.setComment(comment);
        commentItem.setUser(user);
        commentItem.setLikeSum(likeSum);
        commentItem.setLikeIng(likeIng);
        commentItem.setCommentUser(commentUser);
        return commentItem;
    }




//    //添加笔记
//    @PostMapping("/addCollect")
//    public NoteResponse addCollect(@RequestBody Comment comment) {
//        NoteResponse response = new NoteResponse();
//        boolean result = commentDao.addCollect(comment);
//        if (result) {
//            response.setSuccess(true);
//            response.setMessage("笔记成功");
//        } else {
//            response.setSuccess(false);
//            response.setMessage("笔记添加失败");
//        }
//        return response;
//    }


    // 删除评论
    @PostMapping("/deleteComment")
    public ListTResponse deleteComment(@RequestParam("user_id") Long user_id, @RequestParam("comment_id") Long comment_id) {
        ListTResponse response = new ListTResponse();

        boolean result = commentDao.deleteComment(comment_id);

        if (result) {
            response.setSuccess(true);
            response.setMessage("删除评论成功");
        } else {
            response.setSuccess(false);
            response.setMessage("删除评论失败");
        }

        return response;
    }
    //添加评论
    @PostMapping("/addComment")
    public NoteResponse addComment(@RequestBody Comment comment) {
        NoteResponse response = new NoteResponse();
        boolean result = commentDao.addComment(comment);
        if (result) {
            response.setSuccess(true);
            response.setMessage("笔记评论成功");
        } else {
            response.setSuccess(false);
            response.setMessage("笔记评论失败");
        }
        return response;
    }

}
