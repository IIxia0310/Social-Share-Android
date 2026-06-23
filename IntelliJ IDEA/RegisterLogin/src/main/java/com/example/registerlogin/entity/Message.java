package com.example.registerlogin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *  消息对话实体类
 */

@Data
@Entity // 添加JPA实体注解
@TableName("message") // 指定数据库表名（如果与类名不一致）
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableId(value = "message_id", type = IdType.AUTO) // value 为主键字段名，type 为主键生成策略

    private Long messageId; //信息记录唯一标识
    private Long userId; //发送信息的用户id
    private Long messageUserId; //接收信息的用户id
    private String content; //对话内容
    private String createTime; //时间
    private boolean  isRead;   //判断是否已读

    public Message() {

    }


    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getMessageUserId() {
        return messageUserId;
    }

    public void setMessageUserId(Long messageUserId) {
        this.messageUserId = messageUserId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public Message(Long messageId, Long userId, Long messageUserId, String content, String createTime, boolean isRead) {
        this.messageId = messageId;
        this.userId = userId;
        this.messageUserId = messageUserId;
        this.content = content;
        this.createTime = createTime;
        this.isRead = isRead;
    }
}