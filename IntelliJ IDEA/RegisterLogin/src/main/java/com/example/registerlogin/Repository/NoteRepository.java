package com.example.registerlogin.Repository;

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
public interface NoteRepository extends JpaRepository<Note, Long> {

    @Query("SELECT n FROM Note n WHERE " +
            "LOWER(n.interest) LIKE CONCAT('%', LOWER(:keyword), '%') OR " +
            "LOWER(n.title) LIKE CONCAT('%', LOWER(:keyword), '%') OR " +
            "LOWER(n.content) LIKE CONCAT('%', LOWER(:keyword), '%')")
    List<Note> findByKeyword(String keyword);

    default List<Note> searchByKeywords(Set<String> keywords) {
        List<Note> result = new ArrayList<>();
        Set<Long> matchedIds = new HashSet<>(); // 使用 Set 存储已匹配的笔记 ID，避免重复

        // 遍历每个关键词，执行单关键词搜索并合并结果
        for (String keyword : keywords) {
            List<Note> notes = findByKeyword(keyword); // 调用单关键词搜索方法
            for (Note note : notes) {
                if (!matchedIds.contains(note.getNoteId())) { // 检查笔记 ID 是否已存在（去重）
                    result.add(note);
                    matchedIds.add(note.getNoteId());
                }
            }
        }
        return result;
    }
}