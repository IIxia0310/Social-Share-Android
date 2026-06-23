package com.example.registerlogin.service;


public interface MessageService {

    Long getUnreadMessageCount(Long senderId, Long receiverId);

    void markMessageAsRead(Long messageId);


    Boolean deleteUserMessage(Long userId);
}
