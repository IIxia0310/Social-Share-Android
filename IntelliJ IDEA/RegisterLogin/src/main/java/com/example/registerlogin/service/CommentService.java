package com.example.registerlogin.service;

import com.example.registerlogin.entity.Comment;
import com.example.registerlogin.entity.Like;

import java.util.List;

public interface CommentService {


    //评论列表
    List<Comment> eqUserCommentList(Long noteId);
    List<Comment> eqNoteCommentList(Long noteId);


     String eqCommentUser(Long commentId, Long noteUsreId, Long userIng) ;

    Boolean addComment(Comment comment);

    Boolean deleteComment( Long comment_id);


    Boolean deleteUserComment(Long userId);
}
