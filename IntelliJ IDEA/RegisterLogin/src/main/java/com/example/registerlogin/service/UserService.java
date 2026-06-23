package com.example.registerlogin.service;

import com.example.registerlogin.entity.Note;
import com.example.registerlogin.entity.User;

import java.util.List;

public interface UserService {

    List<User> list();

    User eqID(Long user_id);

    Boolean register(User user);

    User login(Long user_id, String password);

    Boolean updateUserInterests(Long user_id, String interest);

    Boolean updateUserPassword(Long user_id, String password);


    Boolean updateUserTheme(Long user_id, int userTheme);

    User userRevamp(Long userIdIng,User user);


    Boolean deleteUser(long userIdIng);
}
