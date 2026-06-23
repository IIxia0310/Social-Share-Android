package com.example.registerlogin.db;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.registerlogin.entity.Note;
import com.example.registerlogin.entity.User;
import com.example.registerlogin.entity.s.NoteItem;
import com.example.registerlogin.mapper.NoteMapper;
import com.example.registerlogin.service.NoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class NoteDao implements NoteService {
    private static final Logger logger = LoggerFactory.getLogger(NoteDao.class);
    private static final int MAX_LENGTH = 2048;

    @Autowired
    private NoteMapper noteMapper;

    @Autowired
    private UserDao userDao;

    @Autowired
    private LikeDao likeDao;





    @Override
    public List<NoteItem> convertToNoteItems(List<Note> notes, Long currentUserId) {
        List<NoteItem> noteItems = new ArrayList<>();

        for (Note note : notes) {
            // 构建NoteItem对象
            NoteItem noteItem = new NoteItem();
            noteItem.setNote(note);


            noteItem.setUser( userDao.eqID(note.getUserId()));
            noteItem.setLikeSum(likeDao.eqNoteLikeSum(note.getNoteId()));
            noteItem.setLikeIng(likeDao.eqLikeIng(currentUserId, note.getNoteId()));


            noteItems.add(noteItem);
        }

        return noteItems;    }

    @Override
    public List<Note> list() {
        QueryWrapper<Note> queryWrapper = new QueryWrapper<>();
        return noteMapper.selectList(queryWrapper);
    }





    //笔记ID查询笔记
    public Note eqNoteID(Long noteId) {
        QueryWrapper<Note> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("note_id", noteId)


                .orderByDesc("create_time"); // 按照 create_time 降序排列
        return noteMapper.selectOne(queryWrapper);
    }

    @Override
    public List<Note> eqPageViews() {

        QueryWrapper<Note> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("draft", 2)
                .eq("visibility", 1)
                .orderByDesc("page_views"); // 按照 create_time 降序排列

        return noteMapper.selectList(queryWrapper);
    }

    //查找用户兴趣的笔记
    @Override
    public List<Note> homeNoteItems(String currentTitle) {
        QueryWrapper<Note> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("interest", currentTitle)
                .eq("draft", 2)
                .eq("visibility", 1)
                .orderByDesc("create_time"); // 按照 create_time 降序排列

        return noteMapper.selectList(queryWrapper);
    }


    //查找关注的人的笔记
    @Override
    public List<Note> attentinNoteItems(Long user_id) {
        QueryWrapper<Note> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", user_id)
                .eq("draft", 2)
                .eq("visibility", 1)
                .orderByDesc("create_time"); // 按照 create_time 降序排列

        return noteMapper.selectList(queryWrapper);
    }


    //查找我的所有笔记
    @Override
    public  List<Note> eqUserID(Long user_id) {
        QueryWrapper<Note> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", user_id) ;
        return noteMapper.selectList(queryWrapper);
    }




    //查找我的笔记草稿箱
    public List<Note> eqDraft( Long userIng) {
        QueryWrapper<Note> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userIng)
                .eq("draft", 1)
                .orderByDesc("create_time"); // 按照 create_time 降序排列
        return noteMapper.selectList(queryWrapper);


    }

    //查找我的笔记公开或者私密
    public List<Note> eqVisibility(int visibility, Long userIng) {
        QueryWrapper<Note> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userIng)
                .eq("visibility", visibility)
                .eq("draft", 2)
                .orderByDesc("create_time"); // 按照 create_time 降序排列

        return noteMapper.selectList(queryWrapper);
    }



    //添加笔记
    @Override
    public Boolean addNote(Note note) {
        logger.info("开始添加笔记，笔记信息: {}", note);
        String imageUrls = note.getImageUrls();
        if (imageUrls != null && imageUrls.length() > MAX_LENGTH) {
            imageUrls = imageUrls.substring(0, MAX_LENGTH);
            note.setImageUrls(imageUrls);
            logger.warn("image_urls 数据超长，已截断，截断后的长度: {}", imageUrls.length());
        }

        String videoUrl = note.getVideoUrl();
        if (videoUrl != null && videoUrl.length() > MAX_LENGTH) {
            videoUrl = videoUrl.substring(0, MAX_LENGTH);
            note.setVideoUrl(videoUrl);
            logger.warn("video_url 数据超长，已截断，截断后的长度: {}", videoUrl.length());
        }

        try {
            int result = noteMapper.insert(note);
            if (result > 0) {
                logger.info("笔记添加成功，笔记 ID: {}", note.getNoteId());
                return true;
            } else {
                logger.error("笔记添加失败，笔记信息: {}", note);
                return false;
            }
        } catch (Exception e) {
            logger.error("插入笔记数据时发生异常，笔记信息: {}", note, e);
            return false;
        }
    }

    //删除笔记
    @Override
    public Boolean deleteNote(Long note_id) {
        // 构建查询条件
        QueryWrapper<Note> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("note_id", note_id);
        // 执行删除操作并获取删除的记录数
        int result = noteMapper.delete(queryWrapper);
        return result > 0;
    }

    @Override
    public Boolean deleteUser(Long userIdIng) {
        // 构建查询条件
        QueryWrapper<Note> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userIdIng);
        // 执行删除操作并获取删除的记录数
        int result = noteMapper.delete(queryWrapper);
        return result > 0;
    }


    //浏览量加1
    @Override
    public Boolean addPageViews(Long note_id) {
        int result = noteMapper.addPageViews(note_id);
        return result > 0;
    }



    //修改可见性
    @Override
    public Boolean updateNoteVisibility(Long userId, int visibility) {
        int result = noteMapper.updateNoteVisibility(userId, visibility);
        System.out.println("[DEBUG] SQL 影响行数: " + result);

        return result > 0;

    }


    //修改笔记
    @Override
    public Note revampNote(Long noteIng, Note note) {
        UpdateWrapper<Note> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("note_id", note.getNoteId())
                .set("user_id", note.getUserId())
                .set("page_views", note.getPage_views())
                .set("visibility", note.getVisibility())
                .set("draft", note.getDraft())
                .set("interest", note.getInterest())
                .set("title", note.getTitle())
                .set("content", note.getContent())
                .set("image_urls", note.getImageUrls())
                .set("video_url", note.getVideoUrl())
                .set("create_time", note.getCreateTime())
                .set("update_time", note.getUpdateTime())
                .eq("note_id", noteIng);

        int rowsAffected = noteMapper.update(null, updateWrapper);
        if (rowsAffected > 0) {
            return noteMapper.selectOne(new QueryWrapper<Note>().eq("note_id", noteIng));
        }
        return null;
    }


    //查询用户的作品总数
    public Long eqNoteSum(Long userId) {
        QueryWrapper<Note> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                .eq("draft", 2)
                .eq("visibility", 1);
        return noteMapper.selectCount(queryWrapper);
    }

    @Override
    public Boolean deleteUserNotes(Long userId) {
        // 构建查询条件
        QueryWrapper<Note> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        // 执行删除操作并获取删除的记录数
        int result = noteMapper.delete(queryWrapper);
        return result > 0;

    }

}