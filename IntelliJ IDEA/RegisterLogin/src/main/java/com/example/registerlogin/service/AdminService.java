package com.example.registerlogin.service;

import com.example.registerlogin.entity.Admin;
import com.example.registerlogin.entity.User;

public interface AdminService {



    Admin loginAdmin(String user_id, String password);


    Admin eqID(Long targetUserId);
}
