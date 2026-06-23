package com.example.registerlogin.db;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.registerlogin.entity.Admin;
import com.example.registerlogin.entity.User;
import com.example.registerlogin.mapper.AdminMapper;
import com.example.registerlogin.mapper.UserMapper;
import com.example.registerlogin.service.AdminService;
import com.example.registerlogin.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminDao implements AdminService {

    private static final Logger logger = LoggerFactory.getLogger(AdminDao.class);
    @Autowired
    AdminMapper adminMapper;


    @Override
    public Admin loginAdmin(String user_id, String password) {
        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("admin_id", user_id)
                .eq("password", password);
        return adminMapper.selectOne(queryWrapper);
    }

    @Override
    public Admin eqID(Long targetUserId) {
        UpdateWrapper<Admin> queryWrapper = new UpdateWrapper<>();
        queryWrapper.eq("admin_id", targetUserId);
        return adminMapper.selectOne(queryWrapper);
    }


}