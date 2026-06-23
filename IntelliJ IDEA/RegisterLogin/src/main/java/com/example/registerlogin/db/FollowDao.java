package com.example.registerlogin.db;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.registerlogin.entity.Comment;
import com.example.registerlogin.entity.Follow;
import com.example.registerlogin.entity.Like;
import com.example.registerlogin.entity.Note;
import com.example.registerlogin.mapper.FollowMapper;
import com.example.registerlogin.mapper.LikeMapper;
import com.example.registerlogin.service.FollowService;
import com.example.registerlogin.service.LikeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class FollowDao implements FollowService {

    private static final Logger logger = LoggerFactory.getLogger(FollowDao.class);

    @Autowired
    private FollowMapper followMapper;




    // 查询用户关注
    public int eqFollowIng(Long user_id, Long follow_user) {
        QueryWrapper<Follow> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", user_id)
                .eq("follow_user", follow_user)
                .orderByDesc("create_time"); // 按照 create_time 降序排列

        Long count = followMapper.selectCount(queryWrapper);
        // 如果 count 为 null，返回 0，否则返回实际数量
        return count != null? count.intValue() : 0;

    }

    //查询关注时间
    public String eqFollowTime(Long userIng, Long userId) {
//        List<String> TimeList = new ArrayList<>();
        String Time = "";
        QueryWrapper<Follow> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                .eq("follow_user", userIng)
                .orderByDesc("create_time"); // 按照 create_time 降序排列
        List<Follow> followList = followMapper.selectList(queryWrapper);



        for (Follow follow : followList) {
            Time= follow.getCreateTime();
        }


        return Time;

    }


    @Override
    public List<Long> eqGz(String currentTitle, Long user_id) {
        QueryWrapper<Follow> queryWrapper = new QueryWrapper<>();
        List<Long> followUserIdList = new ArrayList<>();

        if (currentTitle.equals("我的关注")) {
            queryWrapper.eq("user_id", user_id);

            List<Follow> followList = followMapper.selectList(queryWrapper);
            for (Follow follow : followList) {
                followUserIdList.add(follow.getFollowUser());
            }

        }else if (currentTitle.equals("我的粉丝")){
            queryWrapper.eq("follow_user", user_id)
                    .orderByDesc("create_time"); // 按照 create_time 降序排列


            List<Follow> followList = followMapper.selectList(queryWrapper);
            for (Follow follow : followList) {
                followUserIdList.add(follow.getUserId());
            }
        }

        return followUserIdList;
    }



    //添加关注
    public Boolean addFollow(Follow follow) {
        int result = followMapper.insert(follow);
        return result > 0;
    }


    //删除关注
    public Boolean delectFollow(Long user_id, Long follow_user) {
        // 构建查询条件
        QueryWrapper<Follow> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", user_id)
                .eq("follow_user", follow_user);

        // 执行删除操作并获取删除的记录数
        int result = followMapper.delete(queryWrapper);
        return result > 0;
    }

    @Override
    public Boolean deleteUserFollow(Long userId) {
        // 构建查询条件
        QueryWrapper<Follow> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        // 执行删除操作并获取删除的记录数
        int result = followMapper.delete(queryWrapper);
        return result > 0;
    }



    //查询关注数
    public Long eqFollowSum(Long userId) {
        QueryWrapper<Follow> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        return followMapper.selectCount(queryWrapper);
    }
    //查询粉丝数
    public Long eqFanSum(Long userId) {
        QueryWrapper<Follow> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("follow_user", userId);
        return followMapper.selectCount(queryWrapper);
    }

}