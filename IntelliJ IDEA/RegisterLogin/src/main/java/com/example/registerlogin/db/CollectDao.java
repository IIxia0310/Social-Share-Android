package com.example.registerlogin.db;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.registerlogin.entity.Collect;
import com.example.registerlogin.entity.Like;
import com.example.registerlogin.entity.Note;
import com.example.registerlogin.mapper.CollectMapper;
import com.example.registerlogin.service.CollecctService;
import com.example.registerlogin.service.LikeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CollectDao implements CollecctService {

    private static final Logger logger = LoggerFactory.getLogger(CollectDao.class);
    @Autowired
    private CollectMapper collectMapper;


    //笔记ID查询笔记
    @Override
    public List<Collect> eqNoteCollect(Long noteIng) {
        QueryWrapper<Collect> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("note_id", noteIng);
        return collectMapper.selectList(queryWrapper);
    }



    //笔记ID查询笔记
    @Override
    public List<Collect> eqCollect(Long userIng) {
        QueryWrapper<Collect> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userIng);
        return collectMapper.selectList(queryWrapper);
    }

    // 查询笔记的收藏数量
    @Override
    public Long eqNoteCollectSum(long note_id) {
        QueryWrapper<Collect> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("note_id", note_id);
        return collectMapper.selectCount(queryWrapper);

    }
    // 查询是否收藏
    public int eqCollecIng(Long userIng, Long noteId) {
        QueryWrapper<Collect> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userIng)
                .eq("note_id", noteId)
                .orderByDesc("create_time"); // 按照 create_time 降序排列

        Long count = collectMapper.selectCount(queryWrapper);
        // 如果 count 为 null，返回 0，否则返回实际数量
        return count != null? count.intValue() : 0;

    }

    //添加收藏
    @Override
    public Boolean addCollect(Collect collect) {
        int result = collectMapper.insert(collect);
        return result > 0;
    }

    //删除收藏
    @Override
    public Boolean deleteCollect(Long user_id, Long note_id) {
        // 构建查询条件
        QueryWrapper<Collect> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", user_id)
                .eq("note_id", note_id);

        // 执行删除操作并获取删除的记录数
        int result = collectMapper.delete(queryWrapper);
        return result > 0;
    }

    @Override
    public Boolean deleteNoteCollect(Long noteId) {
        // 构建查询条件
        QueryWrapper<Collect> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("note_id", noteId);

        // 执行删除操作并获取删除的记录数
        int result = collectMapper.delete(queryWrapper);
        return result > 0;
    }



    @Override
    public Boolean deleteUserCollect(Long userId) {
        // 构建查询条件
        QueryWrapper<Collect> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);

        // 执行删除操作并获取删除的记录数
        int result = collectMapper.delete(queryWrapper);
        return result > 0;
    }





    //查询用户的总收藏数
    public Long eqCollectSum(Long userId) {
        QueryWrapper<Collect> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        return collectMapper.selectCount(queryWrapper);
    }


}