package com.example.registerlogin.db;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.registerlogin.entity.Like;
import com.example.registerlogin.entity.User;
import com.example.registerlogin.mapper.UserMapper;
import com.example.registerlogin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class UserDao implements UserService {


    private static final Logger logger = LoggerFactory.getLogger(UserDao.class);
    @Autowired
    UserMapper userMapper;


    //查找所有用户信息
    @Override
    public List<User> list() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        return userMapper.selectList(queryWrapper);
    }



    //MySQL语句       查找
    @Override
    public User eqID(Long user_id) {
        UpdateWrapper<User> queryWrapper = new UpdateWrapper<>();
        queryWrapper.eq("user_id", user_id);
        return userMapper.selectOne(queryWrapper);
    }

    //MySQL语句       增加、注册
    @Override
    public Boolean register(User user) {
        int result = userMapper.insert(user);
        return result > 0;
    }

    //MySQL语句    查询、登录
    // 查询、登录
    @Override
    public User login(Long user_id, String password) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", user_id)
                .eq("password", password);
        return userMapper.selectOne(queryWrapper);
    }



    //MySQL语句    条件修改、兴趣
    // 根据条件 A 修改 B 的信息
    @Override
    public Boolean updateUserInterests(Long user_id, String interests) {
        int result = userMapper.updateUserInterests(user_id, interests);
        return result > 0;
    }

    @Override
    public Boolean updateUserPassword(Long user_id, String password) {
        int result = userMapper.updateUserPassword(user_id, password);
        return result > 0;    }


    @Override
    public Boolean updateUserTheme(Long user_id, int userTheme) {
        int result = userMapper.updateUserTheme(user_id, userTheme);
        return result > 0;
    }


    //修改用户信息： 根据条件 A 修改 B 的信息
    @Override
    public User userRevamp(Long userIdIng, User user) {
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("user_id", user.getUserId())
                .set("username", user.getUsername())
                .set("sex", user.getSex())
                .set("signature", user.getSignature())
                .set("location", user.getLocation())
                .set("portrait_image", user.getPortraitImage())
                .set("background_image", user.getBackgroundImage())
                .set("birthday_time", user.getBirthdayTime())
                .eq("user_id", userIdIng);

        int rowsAffected = userMapper.update(null, updateWrapper);
        if (rowsAffected > 0) {
            return userMapper.selectOne(new QueryWrapper<User>().eq("user_id", userIdIng));
        }
        return null;
    }

    @Override
    public Boolean deleteUser(long userIdIng) {
        // 构建查询条件
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userIdIng);
        // 执行删除操作并获取删除的记录数
        int result = userMapper.delete(queryWrapper);
        return result > 0;
    }


}