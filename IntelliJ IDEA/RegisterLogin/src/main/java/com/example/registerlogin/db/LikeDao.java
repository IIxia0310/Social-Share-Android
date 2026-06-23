package com.example.registerlogin.db;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.registerlogin.entity.Collect;
import com.example.registerlogin.entity.Comment;
import com.example.registerlogin.entity.Follow;
import com.example.registerlogin.entity.Like;
import com.example.registerlogin.mapper.LikeMapper;
import com.example.registerlogin.service.LikeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class LikeDao implements LikeService {

    private static final Logger logger = LoggerFactory.getLogger(LikeDao.class);

    @Autowired
    private LikeMapper likeMapper;




    //评论ID查询点赞
    @Override
    public List<Like> eqCommentLike(Long commentIng) {
        QueryWrapper<Like> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("comment_id", commentIng);
        return likeMapper.selectList(queryWrapper);
    }



    //用户ID查询点赞
    @Override
    public List<Like> eqUserLike(Long userIng) {
        QueryWrapper<Like> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userIng);
        return likeMapper.selectList(queryWrapper);
    }

    //笔记ID查询笔记
    @Override
    public List<Like> eqNoteLike(Long noteIng) {
        QueryWrapper<Like> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("note_id", noteIng);
        return likeMapper.selectList(queryWrapper);
    }


//    //查询点赞笔记的ID
//    public List<Long> eqID(Long userIng) {
//        QueryWrapper<Like> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("user_id", userIng);
//        List<Like> noteCollectList = likeMapper.selectList(queryWrapper);
//        // 从 NoteCollect 列表中提取出 noteId 并返回
//        return noteCollectList.stream()
//                .map(Like::getNoteId)
//                .toList();
//    }



    // 查询笔记的点赞数量
    @Override
    public Long eqNoteLikeSum(long note_id) {
        QueryWrapper<Like> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("note_id", note_id);
        return likeMapper.selectCount(queryWrapper);

    }


    // 查询评论的点赞数量
    @Override
    public Long eqCommentLikeSum(long commentId) {
        QueryWrapper<Like> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("comment_id", commentId);
        return likeMapper.selectCount(queryWrapper);

    }

    //查询是否点赞
    public int eqLikeIng(Long user_id, Long note_id) {
        QueryWrapper<Like> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", user_id)
                .eq("note_id", note_id);
        Long count = likeMapper.selectCount(queryWrapper);
        // 如果 count 为 null，返回 0，否则返回实际数量
        return count != null? count.intValue() : 0;
    }


    // 查询是否点赞评论
    public int eqLikeIngComment(Long user_id, Long comment_id) {
        QueryWrapper<Like> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", user_id)
                .eq("comment_id", comment_id);

        Long count = likeMapper.selectCount(queryWrapper);
        return count != null? count.intValue() : 0;
    }


    //点赞笔记
    @Override
    public Boolean addLike(Like like) {
        int result = likeMapper.insert(like);
        return result > 0;
    }

    //点赞评论
    @Override
    public Boolean addLikeComment(Like like) {
        int result = likeMapper.insert(like);
        return result > 0;
    }




    //删除点赞
    public Boolean delectLike(Long user_id, Long note_id) {
        // 构建查询条件
        QueryWrapper<Like> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", user_id)
                .eq("note_id", note_id);

        // 执行删除操作并获取删除的记录数
        int result = likeMapper.delete(queryWrapper);
        return result > 0;
    }

    //删除评论点赞
    public Boolean deleteLikeComment(Long user_id, Long comment_id) {
        // 构建查询条件
        QueryWrapper<Like> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", user_id)
                .eq("comment_id", comment_id);

        // 执行删除操作并获取删除的记录数
        int result = likeMapper.delete(queryWrapper);
        return result > 0;
    }

    @Override
    public Boolean deleteNoteLike(Long noteId) {
        // 构建查询条件
        QueryWrapper<Like> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("note_id", noteId);
        int result = likeMapper.delete(queryWrapper);
        return result > 0;
    }



    @Override
    public Boolean deleteUserLike(Long userId) {
        // 构建查询条件
        QueryWrapper<Like> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        int result = likeMapper.delete(queryWrapper);
        return result > 0;
    }


    //删除笔记的所有点赞
    public boolean delectNoteLike(Long note_id) {

        // 构建查询条件
        QueryWrapper<Like> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("note_id", note_id);
        // 执行删除操作并获取删除的记录数
        int result = likeMapper.delete(queryWrapper);
        return result > 0;

    }

    //查询用户的总获赞数
    public Long eqLikeSum(Long userId) {
        QueryWrapper<Like> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        return likeMapper.selectCount(queryWrapper);
    }
}

