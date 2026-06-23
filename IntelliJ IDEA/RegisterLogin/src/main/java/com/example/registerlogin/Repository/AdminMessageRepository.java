package com.example.registerlogin.Repository;


import com.example.registerlogin.entity.Message;
import com.example.registerlogin.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
/**
 * 笔记数据访问接口，继承 Spring Data JPA 的 JpaRepository，提供基本 CRUD 操作
 * 并扩展自定义多关键词搜索功能
 */
@Repository
public interface AdminMessageRepository extends JpaRepository<Message, Long> {
    // 使用JPQL实现多关键词搜索
    @Query("SELECT n FROM Message n WHERE " +
            "LOWER(n.messageUserId) LIKE CONCAT('%', LOWER(:keyword), '%') OR " +
            "LOWER(n.content) LIKE CONCAT('%', LOWER(:keyword), '%') OR " +
            "LOWER(n.createTime) LIKE CONCAT('%', LOWER(:keyword), '%')")
    List<Message> findByKeyword(String keyword);
    // 自定义多关键词搜索方法
    default List<Message> searchByKeywords(Set<String> keywords) {
        List<Message> result = new ArrayList<>();
        Set<Long> matchedIds = new HashSet<>();
        // 对每个关键词执行搜索并合并结果
        for (String keyword : keywords) {
            List<Message> notes = findByKeyword(keyword);
            for (Message message : notes) {
                if (!matchedIds.contains(message.getUserId())) {
                    result.add(message);
                    matchedIds.add(message.getUserId());
                }
            }
        }
        return result;
    }
}