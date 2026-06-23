package com.example.registerlogin.db;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.registerlogin.entity.Comment;
import com.example.registerlogin.entity.Like;
import com.example.registerlogin.entity.Note;
import com.example.registerlogin.mapper.CommentMapper;
import com.example.registerlogin.service.CommentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentDao implements CommentService {



    private static final Logger logger = LoggerFactory.getLogger(CommentDao.class);
    @Autowired
    private CommentMapper commentMapper;



    //查询用户评论列表
    @Override
    public List<Comment> eqUserCommentList(Long userId) {
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                .orderByDesc("create_time"); // 按照 create_time 降序排列

        // 使用 noteMapper 的 selectList 方法执行查询，并返回查询结果列表
        return commentMapper.selectList(queryWrapper);
    }


    //查询笔记评论列表
    @Override
    public List<Comment> eqNoteCommentList(Long noteId) {
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("note_id", noteId)
                       .orderByDesc("create_time"); // 按照 create_time 降序排列

        // 使用 noteMapper 的 selectList 方法执行查询，并返回查询结果列表
        return commentMapper.selectList(queryWrapper);
    }

    //查询是否评论
    public String eqCommentUser(Long commentId, Long noteUsreId, Long userIng) {
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("comment_id", commentId)
                .eq("user_id", userIng);

        // 执行第一次查询
        Comment comment = commentMapper.selectOne(queryWrapper);

        if (comment != null) {
            return String.valueOf("自己");                // 如果第二次查询到记录，返回用户 ID
        } else {
            // 如果第一次未查询到记录，构建第二次查询条件
            queryWrapper.clear(); // 清除之前的查询条件
            queryWrapper.eq("comment_id", commentId)
                    .eq("user_id", noteUsreId);

            // 执行第二次查询
            comment = commentMapper.selectOne(queryWrapper);
            if (comment != null) {
                return String.valueOf("楼主");            // 如果第一次查询到记录，返回用户 ID
            }
        }
        return null;        // 如果两次查询都未找到记录，返回 null
    }


    //添加评论
    public Boolean addComment(Comment comment) {
        int result = commentMapper.insert(comment);
        return result > 0;
    }


    //删除评论
    public Boolean deleteComment(Long comment_id) {
        // 构建查询条件
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("comment_id", comment_id);

        // 执行删除操作并获取删除的记录数
        int result = commentMapper.delete(queryWrapper);
        return result > 0;
    }


    public Boolean delectNoteComment(Long noteId) {
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("note_id", noteId);

        // 执行删除操作并获取删除的记录数
        int result = commentMapper.delete(queryWrapper);
        return result > 0;
    }

    @Override
    public Boolean deleteUserComment(Long userId) {
        // 构建查询条件
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        int result = commentMapper.delete(queryWrapper);
        return result > 0;
    }

}
