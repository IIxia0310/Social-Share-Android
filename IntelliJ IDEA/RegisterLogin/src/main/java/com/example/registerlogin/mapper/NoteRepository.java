//package com.example.registerlogin.mapper;
//
//
//import com.example.registerlogin.entity.Note;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.stereotype.Repository;
//
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
//@Repository
//public interface NoteRepository extends JpaRepository<Note, Long> {
//
//    // 使用JPQL实现多关键词搜索
//    @Query("SELECT n FROM Note n WHERE " +
//            "LOWER(n.title) LIKE CONCAT('%', LOWER(:keyword), '%') OR " +
//            "LOWER(n.content) LIKE CONCAT('%', LOWER(:keyword), '%')")
//
//
//
//    List<Note> findByKeyword(String keyword);
//
//    // 自定义多关键词搜索方法
//    default List<Note> searchByKeywords(Set<String> keywords) {
//        List<Note> result = new ArrayList<>();
//        Set<Long> matchedIds = new HashSet<>();
//
//        // 对每个关键词执行搜索并合并结果
//        for (String keyword : keywords) {
//            List<Note> notes = findByKeyword(keyword);
//            for (Note note : notes) {
//                if (!matchedIds.contains(note.getNoteId())) {
//                    result.add(note);
//                    matchedIds.add(note.getNoteId());
//                }
//            }
//        }
//
//        return result;
//    }
//}