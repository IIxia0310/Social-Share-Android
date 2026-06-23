package com.example.registerlogin.controller;//package com.example.registerlogin.controller;

import com.example.registerlogin.Response.ListTResponse;
import com.example.registerlogin.Response.NoteResponse;
import com.example.registerlogin.db.*;
import com.example.registerlogin.entity.Admin;
import com.example.registerlogin.entity.Collect;
import com.example.registerlogin.entity.Note;
import com.example.registerlogin.entity.User;
import com.example.registerlogin.Response.UserResponse;
import com.example.registerlogin.entity.s.FollowItem;
import com.example.registerlogin.entity.s.NoteItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {
    @Autowired
    AdminDao adminDao;

    @Autowired
    UserDao userDao;

    @Autowired
    FollowDao followDao;

    @Autowired
    LikeDao likeDao;

    @Autowired
    CollectDao collectDao;

    @Autowired
    private NoteDao noteDao;

    @Autowired
    private CommentDao commentDao;

    @Autowired
    private MessageDao messageDao;

    // 获取所有用户
    @PostMapping("/getAllUsers")
    public  ListTResponse<List<User>> getAllUsers() {
        ListTResponse<List<User>> response = new ListTResponse();
        List<User> userList = userDao.list(); // 从数据库查询用户列表
//        System.out.println("查询到用户数量：" + userList.size()); // 添加日志

        response.setSuccess(true);
        response.setMessage("查询成功");
        response.setList(userList);
        return response;

    }



    //查询关注笔记列表
    @RequestMapping(value = "/eqFollowFanList", method = {RequestMethod.GET, RequestMethod.POST})
    public ListTResponse<List<FollowItem>> eqFollowFanList(@RequestParam String currentTitle,Long userIng){
        ListTResponse<List<FollowItem>> response = new ListTResponse();
        List<FollowItem> followItemList = new ArrayList<>();


        List<Long> followuUser = followDao.eqGz(currentTitle,userIng);
        FollowItem followItem = null;
        String createTime;
        for (Long userId : followuUser) {

            User user =  userDao.eqID( userId);

            if (currentTitle.equals("我的关注")){
                int followfanIng = followDao.eqFollowIng(user.getUserId(),userIng);
                createTime = followDao.eqFollowTime(user.getUserId(),userIng);
                followItem = convertToFollowItem(user,1,followfanIng,createTime);
            }else {
                int followfanIng = followDao.eqFollowIng(userIng,user.getUserId());
                createTime = followDao.eqFollowTime(userIng,user.getUserId());
                followItem = convertToFollowItem(user,followfanIng,followfanIng,createTime);
            }
            followItemList.add(followItem);
        }
        response.setSuccess(true);
        response.setMessage("查询成功");
        response.setList(followItemList);
        return response;


    }
    private FollowItem convertToFollowItem(User user,int followIng,int followfanIng,String createTime ) {
        FollowItem followItem = new FollowItem();
        // 假设 FollowItem 有对应的 setter 方法
        followItem.setUser(user);
        followItem.setFollowIng(followIng);
        followItem.setFollowFanIng(followfanIng);
        followItem.setCreateTime(createTime);
        return followItem;
    }


    // 查询用户信息
    @PostMapping("/eqUserID")
    public UserResponse eqUserID(@RequestBody Long userId) {
        UserResponse userResponse = new UserResponse();

        User user1 = userDao.eqID(userId);

        if (user1 != null) {
            Long followSum = followDao.eqFollowSum(user1.getUserId());
            Long fanSum = followDao.eqFanSum(user1.getUserId());
            Long noteSum = noteDao.eqNoteSum(user1.getUserId());

            // 初始化总点赞数和总收藏数
            Long totalLikeSum = 0L;
            Long totalCollectSum = 0L;

            List<Note> noteList = noteDao.eqUserID(userId);
            for (Note note : noteList) {
                // 获取当前笔记的点赞数和收藏数
                Long noteLikeSum = likeDao.eqNoteLikeSum(note.getNoteId());
                Long noteCollectSum = collectDao.eqNoteCollectSum(note.getNoteId());

                // 累加总点赞数和总收藏数
                if (noteLikeSum != null) {
                    totalLikeSum += noteLikeSum;
                }
                if (noteCollectSum != null) {
                    totalCollectSum += noteCollectSum;
                }
            }

            userResponse.setSuccess(true);
            userResponse.setMessage("用户查询成功");
            userResponse.setUser(user1);
            userResponse.setFollowSum(followSum);
            userResponse.setFanSum(fanSum);
            userResponse.setNoteSum(noteSum);
            userResponse.setLikeSum(totalLikeSum);  // 返回总点赞数
            userResponse.setCollectSum(totalCollectSum);  // 返回总收藏数
        } else {
            userResponse.setSuccess(false);
            userResponse.setMessage("用户不存在");
        }
        return userResponse;
    }


    @PostMapping("/register")
    public UserResponse register(@RequestBody User user) {
        UserResponse userResponse = new UserResponse();
        User user1=userDao.eqID(user.getUserId());
        if(user1!=null){
            userResponse.setSuccess(false);
            userResponse.setMessage("用户存在！注册失败");
            userResponse.setUser(user1);
        }else {
            if (userDao.register(user)){
                userResponse.setSuccess(true);
                userResponse.setMessage("注册成功");
                User user2=userDao.eqID(user.getUserId());
                userResponse.setUser(user2);
                return userResponse;
            }
        }
        return userResponse;
    }



    //用户登录
    @PostMapping("/login")
    public UserResponse login(@RequestParam("user_id") Long user_id,
                              @RequestParam("password") String password){

        UserResponse userResponse = new UserResponse();
        User user1=userDao.login(user_id,password);

        if(user1!=null){
            userResponse.setSuccess(true);
            userResponse.setMessage("登录成功");
            userResponse.setUser(user1);

        }else {
            userResponse.setSuccess(false);
            userResponse.setMessage("用户名或者密码错误");
        }
        return userResponse;
    }





    //管理员登录
    @PostMapping("/loginAdmin")
    public UserResponse loginAdmin(@RequestParam("user_id") String user_id,@RequestParam("password") String password){

        UserResponse userResponse = new UserResponse();
        Admin admin=adminDao.loginAdmin(user_id,password);


        if(admin!=null){
            userResponse.setSuccess(true);
            userResponse.setMessage("登录成功");
            userResponse.setAdmin(admin);

        }else {
            userResponse.setSuccess(false);
            userResponse.setMessage("用户名或者密码错误");
        }
        return userResponse;
    }

    //修改兴趣
    @PostMapping("/updateUserInterests")
    public UserResponse updateUserInterestsPassword(@RequestParam("user_id") Long user_id,
                                                    @RequestParam("interests") String interests) {
        UserResponse userResponse = new UserResponse();
        boolean success = userDao.updateUserInterests(user_id,interests);
        if (success) {
            userResponse.setSuccess(true);
            userResponse.setMessage("选择成功");
            return userResponse;
        } else {
            userResponse.setSuccess(false);
            userResponse.setMessage("选择失败");
            return userResponse;
        }
    }

    //修改密码
    @PostMapping("/updateUserPassword")
    public UserResponse updateUserPassword(@RequestParam("user_id") Long user_id,@RequestParam("password") String password) {

        UserResponse userResponse = new UserResponse();

        Boolean success = userDao.updateUserPassword(user_id,password);
        if (success) {
            userResponse.setSuccess(true);
            userResponse.setMessage("选择成功");
            return userResponse;

        } else {
            userResponse.setSuccess(false);
            userResponse.setMessage("选择失败");
            return userResponse;
        }

    }


    //修改主题
    @PostMapping("/updateUserTheme")
    public UserResponse updateUserTheme(@RequestParam("user_id") Long user_id,@RequestParam("userTheme") int  userTheme) {

        UserResponse userResponse = new UserResponse();

        boolean success = userDao.updateUserTheme(user_id,userTheme);
        if (success) {

            User user = userDao.eqID(user_id);

            userResponse.setSuccess(true);
            userResponse.setMessage("切换主题成功");
            userResponse.setUser(user);

            return userResponse;

        } else {
            userResponse.setSuccess(false);
            userResponse.setMessage("切换主题失败");
            return userResponse;
        }

    }


    // 修改用户信息
    @PostMapping("/userRevamp")
    public UserResponse userRevamp(@RequestParam("userIdIng") long userIdIng, @RequestBody User user) {
        UserResponse userResponse = new UserResponse();
        User updatedUser = userDao.userRevamp(userIdIng, user);
        if (updatedUser != null) {
            userResponse.setSuccess(true);
            userResponse.setMessage("信息更改成功");
            userResponse.setUser(updatedUser);
        } else {
            userResponse.setSuccess(false);
            userResponse.setMessage("信息更改失败");
        }
        return userResponse;
    }

    @PostMapping("/deleteUser")
    public UserResponse deleteUser(@RequestParam("user_id") long userId) {
        UserResponse userResponse = new UserResponse();

        List<Note> noteList = noteDao.eqUserID(userId);
        for (Note note:noteList){

            boolean result1 = noteDao.deleteNote(note.getNoteId());   //删除笔记
            boolean result2 = likeDao.delectNoteLike(note.getNoteId());   //删除相关的点赞
            boolean result3 = collectDao.deleteNoteCollect(note.getNoteId());   //删除相关的收藏
            boolean result4 = commentDao.delectNoteComment(note.getNoteId());   //删除相关的评论
        }

        boolean result11 = userDao.deleteUser(userId);   //删除用户
        boolean result22 = likeDao.deleteUserLike(userId);   //删除相关的点赞
        boolean result33 = collectDao.deleteUserCollect(userId);   //删除相关的收藏
        boolean result44 = commentDao.deleteUserComment(userId);   //删除相关的评论
        boolean result55 = followDao.deleteUserFollow(userId);   //删除相关的评论
        boolean result66 = messageDao.deleteUserMessage(userId);   //删除相关的评论



        return userResponse;

    }


}
