package com.example.registerlogin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.registerlogin.entity.Like;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


@Mapper
public interface LikeMapper extends BaseMapper<Like> {

//    @Select("SELECT `like_id`, `user_id`, `note_id`, `create_time` FROM `like` WHERE (note_id = #{note_id})")
//
//    List<Like> selectList(@Param("note_id") long note_id);

}