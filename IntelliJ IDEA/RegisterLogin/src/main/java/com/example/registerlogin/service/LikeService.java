package com.example.registerlogin.service;

import com.example.registerlogin.entity.Collect;
import com.example.registerlogin.entity.Like;
import com.example.registerlogin.entity.Note;
import com.example.registerlogin.entity.User;

import java.util.List;

public interface LikeService {


    //查询用户的点赞笔记的ID
    List<Like>  eqCommentLike(Long commentIng);
    List<Like>  eqUserLike(Long userIng);
    List<Like>  eqNoteLike(Long noteIng);

    Long eqLikeSum(Long user_id);


    //查询笔记点赞数量
    Long  eqNoteLikeSum(long note_id);
    Long  eqCommentLikeSum(long comment_id);


    Boolean addLike(Like like);
    Boolean addLikeComment(Like like);


    Boolean delectLike(Long user_id,Long note_id);
    Boolean deleteLikeComment(Long user_id, Long comment_id);



    Boolean deleteNoteLike(Long noteId);
    Boolean deleteUserLike(Long userId);





}
