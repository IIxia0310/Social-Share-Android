package com.example.registerlogin.controller;



import com.example.registerlogin.Response.ListTResponse;
import com.example.registerlogin.db.SearchService;
import com.example.registerlogin.db.UserDao;
import com.example.registerlogin.entity.Message;
import com.example.registerlogin.entity.Note;
import com.example.registerlogin.entity.User;
import com.example.registerlogin.entity.s.MessageItem;
import com.example.registerlogin.entity.s.NoteItem;
import com.example.registerlogin.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/search")
public class SearchController{

    @Autowired
    private SearchService searchService;

    @Autowired
    private NoteService noteService;


    @Autowired
    private UserDao userDao;

    //用户搜索笔记
    @GetMapping("/noteSearch")
    public ListTResponse<List<NoteItem>> noteSearch(
            @RequestParam("keywords") List<String> keywords,
            @RequestParam("userIng") Long userIng) {

        ListTResponse<List<NoteItem>> response = new ListTResponse<>();

        try {
            List<Note> noteList = searchService.searchNotes(keywords);
            List<NoteItem> noteItemList = noteService.convertToNoteItems(noteList, userIng);

            response.setSuccess(true);
            response.setMessage("搜索成功");
            response.setList(noteItemList);
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("搜索失败: " + e.getMessage());
        }

        return response;
    }

    //管理员搜索用户
    @GetMapping("/adminUserSearch")
    public ListTResponse<List<User>> adminUserSearch(
            @RequestParam("keywords") List<String> keywords) {

        ListTResponse<List<User>> response = new ListTResponse<>();

        try {
            List<User> userList = searchService.AdminSearchUsers(keywords);
            response.setSuccess(true);
            response.setMessage("搜索成功");
            response.setList(userList);
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("搜索失败: " + e.getMessage());
        }

        return response;
    }

    //管理员搜索笔记
    @GetMapping("/adminNoteSearch")
    public ListTResponse<List<Note>> adminNoteSearch(
            @RequestParam("keywords") List<String> keywords) {

        ListTResponse<List<Note>> response = new ListTResponse<>();

        try {
            List<Note> userList = searchService.AdminSearchNotes(keywords);
            response.setSuccess(true);
            response.setMessage("搜索成功");
            response.setList(userList);
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("搜索失败: " + e.getMessage());
        }

        return response;
    }

    //管理员搜索留言
    @GetMapping("/adminMessageSearch")
    public ListTResponse<List<MessageItem>> adminMessageSearch(
            @RequestParam("keywords") List<String> keywords) {

        ListTResponse<List<MessageItem>> response = new ListTResponse<>();

        if (keywords.isEmpty()) {
            response.setSuccess(false);
            response.setMessage("搜索关键词不能为空");
            return response;
        }

        try {
            List<Message> messageList = searchService.AdminSearchMessage(keywords);

            // 使用循环查询（性能较差，建议用方案一）
            List<MessageItem> messageItemList = new ArrayList<>();
            for (Message message : messageList) {
                User user = userDao.eqID(message.getUserId());
                messageItemList.add(new MessageItem(
                        user != null ? user.getPortraitImage() : null,
                        user != null ? user.getUsername() : "未知用户",
                        message.getUserId(),
                        0,
                        message.getContent(),
                        message.getCreateTime(),
                        null
                ));
            }

            response.setSuccess(true);
            response.setList(messageItemList);
            response.setMessage("搜索成功");
            return response;

        }  catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("搜索失败: " + e.getMessage());
            return response;
        }
    }
}