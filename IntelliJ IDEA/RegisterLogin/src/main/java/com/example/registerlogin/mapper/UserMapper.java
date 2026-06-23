package com.example.registerlogin.mapper;//package com.example.registerlogin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.registerlogin.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Update("UPDATE user SET interests = #{interests} WHERE user_id = #{user_id}")
    int updateUserInterests(long user_id, String interests);

    @Update("UPDATE user SET password = #{password} WHERE user_id = #{user_id}")
    int updateUserPassword(Long user_id, String password);


    @Update("UPDATE user SET theme = #{theme} WHERE user_id = #{user_id}")
    int updateUserTheme(long user_id, int theme);



}
