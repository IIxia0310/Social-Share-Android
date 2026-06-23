package com.example.registerapplication.Entity;

import java.io.Serializable;

/**
 *  消息对话实体类
 */
public class Message implements Serializable {


    private Long messageId; //信息记录唯一标识
    private Long userId; //发送信息的用户id
    private Long messageUserId; //接收信息的用户id
    private String content; //对话内容
    private String createTime; //时间
    private boolean  isRead;   //判断是否已读


    @Override
    public String toString() {
        return "Message{" +
                "messageId=" + messageId +
                ", userId=" + userId +
                ", messageUserId=" + messageUserId +
                ", content='" + content + '\'' +
                ", createTime='" + createTime + '\'' +
                ", isRead=" + isRead +
                '}';
    }

    public Message(Long messageId, Long userId, Long messageUserId, String content, String createTime, boolean  isRead) {
        this.messageId = messageId;
        this.userId = userId;
        this.messageUserId = messageUserId;
        this.content = content;
        this.createTime = createTime;
        this.isRead = isRead;
    }

    public boolean  isRead() {
        return isRead;
    }

    public void setRead(boolean  read) {
        isRead = read;
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
}