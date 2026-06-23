package com.example.registerlogin.Repository;

import com.example.registerlogin.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 用户数据访问接口，继承 Spring Data JPA 的 JpaRepository，提供基本 CRUD 操作
 * 并扩展自定义多关键词搜索功能，支持对用户信息的模糊查询
 */
@Repository
public interface AdminUserRepository extends JpaRepository<User, Long> {
    @Query("SELECT n FROM User n WHERE " +
            "LOWER(n.userId) LIKE CONCAT('%', LOWER(:keyword), '%') OR " +
            "LOWER(n.username) LIKE CONCAT('%', LOWER(:keyword), '%') OR " +
            "LOWER(n.sex) LIKE CONCAT('%', LOWER(:keyword), '%') OR " +
            "LOWER(n.location) LIKE CONCAT('%', LOWER(:keyword), '%') OR " +
            "LOWER(n.birthdayTime) LIKE CONCAT('%', LOWER(:keyword), '%') OR " +
            "LOWER(n.createTime) LIKE CONCAT('%', LOWER(:keyword), '%')")
    List<User> findByKeyword(String keyword);

    default List<User> searchByKeywords(Set<String> keywords) {
        List<User> result = new ArrayList<>();
        Set<Long> matchedUserIds = new HashSet<>(); // 使用用户 ID 去重，避免重复记录
        // 遍历每个关键词，执行单关键词搜索并合并结果
        for (String keyword : keywords) {
            List<User> users = findByKeyword(keyword); // 调用单关键词搜索方法
            for (User user : users) {
                if (!matchedUserIds.contains(user.getUserId())) { // 通过用户 ID 判断是否已存在
                    result.add(user);
                    matchedUserIds.add(user.getUserId()); // 记录已添加的用户 ID
                }
            }
        }
        return result;
    }
}