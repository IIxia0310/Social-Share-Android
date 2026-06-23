package com.example.registerlogin.db;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.registerlogin.entity.Like;
import com.example.registerlogin.entity.Message;
import com.example.registerlogin.mapper.MessageMapper;
import com.example.registerlogin.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MessageDao implements MessageService {

    private static final Logger logger = LoggerFactory.getLogger(MessageDao.class);

    @Autowired
    private MessageMapper messageMapper;

    public List<Message> eqmMessageltem(Long userIng) {
        QueryWrapper<Message> queryWrapper = new QueryWrapper<>();
        queryWrapper.and(wrapper -> wrapper.eq("user_id", userIng).or().eq("message_user_id", userIng))
                .orderByDesc("create_time");
        return messageMapper.selectList(queryWrapper);
    }

    // 查询未读数量
    @Override
    public Long getUnreadMessageCount(Long currentUserId, Long otherUserId) {
        QueryWrapper<Message> queryWrapper = new QueryWrapper<>();
        // 限定是对方发送给自己的消息
        queryWrapper.eq("user_id", otherUserId)
                .eq("message_user_id", currentUserId)
                .eq("is_read", 0); // 假设未读状态在数据库中用 0 表示
        return messageMapper.selectCount(queryWrapper);
    }

    @Override
    public void markMessageAsRead(Long messageId) {
        UpdateWrapper<Message> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("message_id", messageId); // 假设消息表中有 id 字段作为主键
        updateWrapper.set("is_read", 1); // 假设 1 表示已读
        messageMapper.update(null, updateWrapper);
    }

    @Override
    public Boolean deleteUserMessage(Long userId) {
        // 构建查询条件
        QueryWrapper<Message> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        int result = messageMapper.delete(queryWrapper);
        return result > 0;
    }



    public List<Message> getMessages(Long userIng, Long targetUserId) {
        QueryWrapper<Message> queryWrapper = new QueryWrapper<>();
        // 筛选出两个用户之间的消息，无论谁是发送者谁是接收者
        queryWrapper.and(wrapper -> wrapper.and(innerWrapper ->
                                innerWrapper.eq("user_id", userIng).eq("message_user_id", targetUserId))
                        .or(innerWrapper -> innerWrapper.eq("user_id", targetUserId).eq("message_user_id", userIng)))
                .orderByAsc("create_time"); // 按时间正序排列
        return messageMapper.selectList(queryWrapper);
    }


    // 添加聊天信息
    public boolean addMessage(Message message) {

        int result = messageMapper.insert(message);
        return result > 0;
    }

}