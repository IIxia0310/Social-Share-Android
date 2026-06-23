package com.example.registerlogin.db;


import com.example.registerlogin.Repository.AdminMessageRepository;
import com.example.registerlogin.Repository.AdminNoteRepository;
import com.example.registerlogin.Repository.AdminUserRepository;
import com.example.registerlogin.entity.Message;
import com.example.registerlogin.entity.Note;
import com.example.registerlogin.entity.User;

import com.example.registerlogin.Repository.NoteRepository;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SearchService {

    @Autowired
    private NoteRepository noteRepository;
    @Autowired
    private AdminUserRepository adminUserRepository;
    @Autowired
    private AdminNoteRepository adminNoteRepository;

    @Autowired
    private AdminMessageRepository adminMessageRepository;


    public List<Note> searchNotes(List<String> keywords) {
        // 对关键词进行分词处理
        List<String> allKeywords = new ArrayList<>();
        for (String keyword : keywords) {
            List<Term> termList = HanLP.segment(keyword);
            for (Term term : termList) {
                allKeywords.add(term.word);
            }
        }

        // 移除重复关键词
        Set<String> uniqueKeywords = new HashSet<>(allKeywords);

        // 执行搜索逻辑
        return noteRepository.searchByKeywords(uniqueKeywords);
    }

    public List<User> AdminSearchUsers(List<String> keywords) {
        // 对关键词进行分词处理
        List<String> allKeywords = new ArrayList<>();
        for (String keyword : keywords) {
            List<Term> termList = HanLP.segment(keyword);
            for (Term term : termList) {
                allKeywords.add(term.word);
            }
        }

        // 移除重复关键词
        Set<String> uniqueKeywords = new HashSet<>(allKeywords);

        // 执行搜索逻辑
        return adminUserRepository.searchByKeywords(uniqueKeywords);
    }

    public List<Note> AdminSearchNotes(List<String> keywords) {

        // 对关键词进行分词处理
        List<String> allKeywords = new ArrayList<>();
        for (String keyword : keywords) {
            List<Term> termList = HanLP.segment(keyword);
            for (Term term : termList) {
                allKeywords.add(term.word);
            }
        }

        // 移除重复关键词
        Set<String> uniqueKeywords = new HashSet<>(allKeywords);

        // 执行搜索逻辑
        return adminNoteRepository.searchByKeywords(uniqueKeywords);
    }




    public List<Message> AdminSearchMessage(List<String> keywords) {

        // 对关键词进行分词处理
        List<String> allKeywords = new ArrayList<>();
        for (String keyword : keywords) {
            List<Term> termList = HanLP.segment(keyword);
            for (Term term : termList) {
                allKeywords.add(term.word);
            }
        }

        // 移除重复关键词
        Set<String> uniqueKeywords = new HashSet<>(allKeywords);

        // 执行搜索逻辑
        return adminMessageRepository.searchByKeywords(uniqueKeywords);
    }
}