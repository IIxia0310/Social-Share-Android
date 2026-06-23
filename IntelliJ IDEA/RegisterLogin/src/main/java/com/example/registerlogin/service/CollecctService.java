package com.example.registerlogin.service;

import com.example.registerlogin.entity.Collect;
import com.example.registerlogin.entity.Like;

import java.util.List;

public interface CollecctService {

    List<Collect>  eqNoteCollect(Long noteIng);

    //查询用户的收藏笔记的ID
    List<Collect>  eqCollect(Long userIng);

    //查询笔记收藏数量
    Long  eqNoteCollectSum(long note_id);

    Boolean addCollect(Collect collect);
    Boolean deleteCollect(Long user_id,Long note_id);


    //删除收藏 user_id
    Boolean deleteNoteCollect(Long noteId);
    Boolean deleteUserCollect(Long userId);
}
