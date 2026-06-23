package com.example.registerlogin.controller;

import com.example.registerlogin.Response.ListTResponse;
import com.example.registerlogin.Response.NoteResponse;
import com.example.registerlogin.db.AdminDao;
import com.example.registerlogin.db.FollowDao;
import com.example.registerlogin.db.MessageDao;
import com.example.registerlogin.db.UserDao;
import com.example.registerlogin.entity.Admin;
import com.example.registerlogin.entity.Like;
import com.example.registerlogin.entity.Message;
import com.example.registerlogin.entity.User;
import com.example.registerlogin.entity.s.MessageItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class MessageController {
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    private MessageDao messageDao;

    @Autowired
    private UserDao userDao;


    @Autowired
    private FollowDao followDao;


    @Autowired
    private AdminDao adminDao;


    // 查询聊天列表
//    @RequestMapping(value = "/messageItem", method = {RequestMethod.GET, RequestMethod.POST})
//    public ListTResponse<List<MessageItem>> messageItem(@RequestParam Long userIng) {
//        ListTResponse<List<MessageItem>> response = new ListTResponse<>();
//
//        List<Message> messageList = messageDao.eqmMessageltem(userIng);
//
//        // 用于存储每个聊天对象的最新消息
//        Map<Long, Message> latestMessageMap = new HashMap<>();
//
//        // 找出每个聊天对象的最新消息
//        for (Message message : messageList) {
//            Long targetUserId = message.getUserId().equals(userIng)? message.getMessageUserId() : message.getUserId();
//            if (!latestMessageMap.containsKey(targetUserId) ||
//                    message.getCreateTime().compareTo(latestMessageMap.get(targetUserId).getCreateTime()) > 0) {
//                latestMessageMap.put(targetUserId, message);
//            }
//        }
//
//        List<MessageItem> messageItemList = new ArrayList<>();
//
//        for (Map.Entry<Long, Message> entry : latestMessageMap.entrySet()) {
//            Long targetUserId = entry.getKey();
//            Message latestMessage = entry.getValue();
//
//            User user = userDao.eqID(targetUserId);
//            int followIng = followDao.eqFollowIng(userIng, targetUserId);
//
//            String Pimage = user.getPortraitImage();
//            String name = user.getUsername();
//            Long user_id = user.getUserId();
//            String content = latestMessage.getContent();
//            String time = latestMessage.getCreateTime();
//
//            // 查询未读消息数量
//            Long unreadSum = messageDao.getUnreadMessageCount(userIng, targetUserId);
//
//            MessageItem messageItem = new MessageItem(Pimage, name, user_id, followIng, content, time, unreadSum);
//            messageItemList.add(messageItem);
//        }
//
////        // 打印 messageItemList 的内容
////        System.out.println("messageItemList 的内容: ");
////        for (MessageItem item : messageItemList) {
////            System.out.println(item.toString());
////        }
//
//
//        response.setSuccess(true);
//        response.setMessage("查询成功");
//        response.setList(messageItemList);
//        return response;
//    }


    // 查询聊天列表
    @RequestMapping(value = "/messageItem", method = {RequestMethod.GET, RequestMethod.POST})
    public ListTResponse<List<MessageItem>> messageItem(@RequestParam Long userIng) {
        ListTResponse<List<MessageItem>> response = new ListTResponse<>();
        List<Message> messageList = messageDao.eqmMessageltem(userIng);
        Map<Long, Message> latestMessageMap = new HashMap<>();    // 用于存储每个聊天对象的最新消息
        for (Message message : messageList) {        // 找出每个聊天对象的最新消息
            Long targetUserId = message.getUserId().equals(userIng)? message.getMessageUserId() : message.getUserId();
            if (!latestMessageMap.containsKey(targetUserId) ||
                    message.getCreateTime().compareTo(latestMessageMap.get(targetUserId).getCreateTime()) > 0) {
                latestMessageMap.put(targetUserId, message);
            }
        }
        List<MessageItem> messageItemList = new ArrayList<>();
        for (Map.Entry<Long, Message> entry : latestMessageMap.entrySet()) {
            Long targetUserId = entry.getKey();
            Message latestMessage = entry.getValue();
            User user = userDao.eqID(targetUserId);
            int followIng = followDao.eqFollowIng(userIng, targetUserId);
            String Pimage = user.getPortraitImage();
            String name = user.getUsername();
            Long user_id = user.getUserId();
            String content = latestMessage.getContent();
            String time = latestMessage.getCreateTime();
            Long unreadSum = messageDao.getUnreadMessageCount(userIng, targetUserId);// 查询未读消息数量
            MessageItem messageItem = new MessageItem(Pimage, name, user_id, followIng, content, time, unreadSum);
            messageItemList.add(messageItem);
        }
        response.setSuccess(true);
        response.setMessage("查询成功");
        response.setList(messageItemList);
        return response;
    }


    // 查询聊天列表
    @RequestMapping(value = "/getMessages", method = {RequestMethod.GET, RequestMethod.POST})
    public ListTResponse<List<Message>> getMessages(@RequestParam("userIng")Long userIng,@RequestParam("targetUserId") Long targetUserId) {
        ListTResponse<List<Message>> response = new ListTResponse<>();

        List<Message> messageList1 = messageDao.getMessages(userIng,targetUserId);


        // 打印 messageItemList 的内容
        System.out.println("message 的内容: ");
        for (Message item1 : messageList1) {
            System.out.println(item1.toString());
        }

        response.setSuccess(true);
        response.setMessage("查询成功");
        response.setList(messageList1);
        return response;
    }


    // 标记消息为已读
    @RequestMapping(value = "/markMessageAsRead", method = {RequestMethod.GET, RequestMethod.POST})
    public void markMessageAsRead(@RequestParam Long messageId) {
        messageDao.markMessageAsRead(messageId);
    }


    //添加聊天信息
    @PostMapping("/addMessage")
    public ListTResponse addMessage(@RequestBody Message message) {
        ListTResponse response = new ListTResponse();
        boolean result = messageDao.addMessage(message);
        if (result) {
            response.setSuccess(true);
            response.setMessage("信息添加成功");
        } else {
            response.setSuccess(false);
            response.setMessage("信息添加失败");
        }
        return response;
    }



}