package com.example.registerlogin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.registerlogin.entity.Note;
import com.example.registerlogin.entity.User;
import com.example.registerlogin.entity.s.NoteItem;

import java.util.List;

public interface NoteService {

    List<NoteItem> convertToNoteItems(List<Note> notes, Long currentUserId);



    List<Note> list();
    Note eqNoteID(Long note_id);



    //主页笔记列表
    List<Note> eqPageViews();
    List<Note> homeNoteItems(String currentTitle);

    //关注笔记列表
    List<Note> attentinNoteItems(Long user_id);

    //查找我的所有笔记
    List<Note> eqUserID(Long user_id);

    //我的草稿箱
    List<Note> eqDraft(Long userIng);

    //我的作品，公开或私密
    List<Note> eqVisibility(int visibility,Long userIng);

    //添加笔记
    Boolean addNote(Note note);

    //添加笔记
    Boolean deleteNote(Long note_id);


    Boolean deleteUser(Long userIdIng);
    //修改笔记
    Note revampNote(Long note_id , Note user);

    // 浏览量+1
    Boolean addPageViews(Long note_id);


    Boolean updateNoteVisibility(Long userId, int visibility);


    //删除笔记 user_id
    Boolean deleteUserNotes(Long userId);
}
