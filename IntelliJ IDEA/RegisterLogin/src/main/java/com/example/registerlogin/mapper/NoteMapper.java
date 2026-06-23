package com.example.registerlogin.mapper;//package com.example.registerlogin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.registerlogin.entity.Note;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.data.jpa.repository.Query;
import org.w3c.dom.Node;

import java.util.List;

@Mapper
public interface NoteMapper extends BaseMapper<Note> {


    @Update("UPDATE note SET page_views = page_views+1 WHERE note_id = #{note_id}")
    int addPageViews(Long note_id);

//    // 自定义获取所有笔记的方法
//    @Query("SELECT n FROM Note n")
//    List<Note> getAllNotes();



    @Update("UPDATE note SET visibility = #{visibility} WHERE note_id = #{note_id}")
    int updateNoteVisibility(Long note_id, int visibility);

}

