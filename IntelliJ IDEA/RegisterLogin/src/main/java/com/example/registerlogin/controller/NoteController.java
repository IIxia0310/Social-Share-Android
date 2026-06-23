package com.example.registerlogin.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.registerlogin.Response.ListTResponse;
import com.example.registerlogin.Response.NoteResponse;
import com.example.registerlogin.Response.UserResponse;
import com.example.registerlogin.db.*;
import com.example.registerlogin.entity.Collect;
import com.example.registerlogin.entity.Like;
import com.example.registerlogin.entity.Note;
import com.example.registerlogin.entity.User;
import com.example.registerlogin.entity.s.NoteDetailsItem;
import com.example.registerlogin.entity.s.NoteHistoryltem;
import com.example.registerlogin.entity.s.NoteItem;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class NoteController {
    private static final Logger logger = LoggerFactory.getLogger(NoteController.class);



    @Autowired
    private NoteDao noteDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private LikeDao likeDao;

    @Autowired
    private CollectDao collectDao;

    @Autowired
    private FollowDao followDao;

    @Autowired
    private CommentDao commentDao;


    // 浏览历史
    @RequestMapping(value = "/browseHistory", method = {RequestMethod.GET, RequestMethod.POST})
    public ListTResponse<List<NoteItem>> browseHistory(@RequestBody List<NoteHistoryltem> historyList) {
        ListTResponse<List<NoteItem>> response = new ListTResponse();

        List<NoteItem> noteItemList = new ArrayList<>();

        for (NoteHistoryltem noteHistoryltem : historyList) {
            // 空值检查
            if (noteHistoryltem != null) {
                Note note = noteDao.eqNoteID(noteHistoryltem.getNoteId());
                if (note != null) {
                    User user = userDao.eqID(note.getUserId());
                    Long likeSum = likeDao.eqNoteLikeSum(note.getNoteId());
                    int likeIng = likeDao.eqLikeIng(noteHistoryltem.getUserId(), note.getNoteId());

                    NoteItem noteItem = convertToNoteItem(note, user, likeSum, likeIng);
                    noteItemList.add(noteItem);
                }
            }
        }

        response.setSuccess(true);
        response.setMessage("查询成功");
        response.setList(noteItemList);
        return response;
    }




    //查询主页笔记列表
    @RequestMapping(value = "/homeNoteItems", method = {RequestMethod.GET, RequestMethod.POST})
    public ListTResponse<List<NoteItem>> homeNoteItems(@RequestParam String currentTitle, Long userIng){
        ListTResponse<List<NoteItem>> response = new ListTResponse();
        List<Note> noteList = new ArrayList<>();
        if (currentTitle.equals("推荐")){
            noteList = noteDao.eqPageViews();
        }else {
            noteList = noteDao.homeNoteItems(currentTitle);
        }
        // 只在首次加载或无用户ID时随机排序
        if (userIng == null || userIng == 0) {
            Collections.shuffle(noteList);
        }

        List<NoteItem> noteItemList = new ArrayList<>();

        for (Note note : noteList) {
            User user =  userDao.eqID(note.getUserId());
            Long likeSum = likeDao.eqNoteLikeSum(note.getNoteId());
            int likeIng = likeDao.eqLikeIng(userIng, note.getNoteId());

            NoteItem noteItem = convertToNoteItem(note, user, likeSum, likeIng);
            noteItemList.add(noteItem);
        }
        response.setSuccess(true);
        response.setMessage("查询成功");
        response.setList(noteItemList);
        return response;
    }



    //查询关注笔记列表
    @RequestMapping(value = "/attentinNoteItems", method = {RequestMethod.GET, RequestMethod.POST})
    public ListTResponse<List<NoteItem>> attentinNoteItems(@RequestParam String currentTitle,Long userIng){
        ListTResponse<List<NoteItem>> response = new ListTResponse();

        List<NoteItem> noteItemList = new ArrayList<>();
        List<Long> followuUser = followDao.eqGz(currentTitle,userIng);

        for (Long userId : followuUser) {
            List<Note> noteList = noteDao.attentinNoteItems(userId);
            for (Note note : noteList) {
                User user =  userDao.eqID( note.getUserId());
                Long likeSum = likeDao.eqNoteLikeSum(note.getNoteId());
                int likeIng = likeDao.eqLikeIng(userIng,note.getNoteId());

                NoteItem noteItem = convertToNoteItem(note,user,likeSum,likeIng);
                noteItemList.add(noteItem);
            }
        }
        response.setSuccess(true);
        response.setMessage("查询成功");
        response.setList(noteItemList);
        return response;
    }



    //查询我的笔记列表
    @RequestMapping(value = "/me1NoteItems", method = {RequestMethod.GET, RequestMethod.POST})
    public ListTResponse<List<NoteItem>> me1NoteItems(@RequestParam String currentTitle,Long userIng){
        ListTResponse<List<NoteItem>> response = new ListTResponse();

        List<Note> noteList = new  ArrayList<>();
        int draft;
        int visibility;

        if (currentTitle != null) {
            if (currentTitle.equals("草稿箱")){
               noteList = noteDao.eqDraft(userIng);

            }else if (currentTitle.equals("公开")){
                visibility=1;
                noteList = noteDao.eqVisibility(visibility,userIng);

            }else if (currentTitle.equals("私密")){
                visibility=2;
                noteList = noteDao.eqVisibility(visibility,userIng);
            }
        }
        
        List<NoteItem> noteItemList = new ArrayList<>();

        for (Note note : noteList) {
            User user =  userDao.eqID( note.getUserId());
            Long likeSum = likeDao.eqNoteLikeSum(note.getNoteId());
            int likeIng = likeDao.eqLikeIng(userIng,note.getNoteId());


            NoteItem noteItem = convertToNoteItem(note,user,likeSum,likeIng);
            noteItemList.add(noteItem);

        }
        response.setSuccess(true);
        response.setMessage("查询成功");
        response.setList(noteItemList);
        return response;
    }

    //查询我收藏笔记列表
    @RequestMapping(value = "/me2me3NoteItems", method = {RequestMethod.GET, RequestMethod.POST})
    public ListTResponse<List<NoteItem>> me2me3NoteItems(@RequestParam String currentTitle, Long userIng) {
        ListTResponse<List<NoteItem>> response = new ListTResponse();

        List<Collect> collectList = new ArrayList<>();
        List<Like> likeList = new ArrayList<>();
        List<Note> noteList = new ArrayList<>();

        Note note;

        if (currentTitle != null) {
            if (currentTitle.equals("收藏")) {
                collectList = collectDao.eqCollect(userIng);
                for (Collect collect : collectList) {
                    note = noteDao.eqNoteID(collect.getNoteId());
                    if (note != null) { // 检查 note 是否为 null
                        noteList.add(note);
                    }
                }
            } else if (currentTitle.equals("点赞")) {
                likeList = likeDao.eqUserLike(userIng);
                for (Like like : likeList) {
                    note = noteDao.eqNoteID(like.getNoteId());
                    if (note != null) { // 检查 note 是否为 null
                        noteList.add(note);
                    }
                }
            }
        }

        List<NoteItem> noteItemList = new ArrayList<>();

        for (Note noteIng : noteList) {
            if (noteIng != null) { // 检查 noteIng 是否为 null
                User user = userDao.eqID(noteIng.getUserId());
                Long likeSum = likeDao.eqNoteLikeSum(noteIng.getNoteId());
                int likeIng = likeDao.eqLikeIng(userIng, noteIng.getNoteId());
                NoteItem noteItem = convertToNoteItem(noteIng, user, likeSum, likeIng);
                noteItemList.add(noteItem);
            }
        }
        response.setSuccess(true);
        response.setMessage("查询成功");
        response.setList(noteItemList);
        return response;
    }

    // 将 Note 对象转换为 NoteItem 对象的方法
    private NoteItem convertToNoteItem(Note note, User user,Long likeSum,int likeIng) {
        NoteItem noteItem = new NoteItem();
        // 假设 NoteItem 有对应的 setter 方法
        noteItem.setNote(note);
        noteItem.setUser(user);
        noteItem.setLikeSum(likeSum);
        noteItem.setLikeIng(likeIng);

        return noteItem;
    }

    //查询笔记详情页
    @RequestMapping(value = "/eqNoteDetailsItem", method = {RequestMethod.GET, RequestMethod.POST})
    public ListTResponse<NoteDetailsItem> eqNoteDetailsItem(@RequestParam Long userIng, Long note_id){
        ListTResponse<NoteDetailsItem> response = new ListTResponse();

        NoteDetailsItem noteDetailsItem = new NoteDetailsItem();
        if (note_id!=null){
            Note note = noteDao.eqNoteID(note_id);
            User user =  userDao.eqID( note.getUserId());
            Long likeSum = likeDao.eqNoteLikeSum(note.getNoteId());
            int likeIng = likeDao.eqLikeIng(userIng,note.getNoteId());

            Long collectSum = collectDao.eqNoteCollectSum(note.getNoteId());
            int collecIng = collectDao.eqCollecIng(userIng,note.getNoteId());
            int followIng = followDao.eqFollowIng(userIng,note.getUserId());

            noteDetailsItem = convertToDetailsItem (note,user,likeSum,likeIng,collectSum,collecIng,followIng);

        }

//        User user = userDao.eqID(note_id);

        response.setSuccess(true);
        response.setMessage("查询成功");
        response.setList(noteDetailsItem);
        return response;
    }
    // 将 Note 对象转换为 NoteItem 对象的方法
    private NoteDetailsItem convertToDetailsItem(Note note, User user,Long likeSum,int likeIng,Long collectSum,int collecIng,int followIng) {
        NoteDetailsItem noteDetailsItem = new NoteDetailsItem();
        // 假设 NoteItem 有对应的 setter 方法
        noteDetailsItem.setNote(note);
        noteDetailsItem.setUser(user);
        noteDetailsItem.setLikeSum(likeSum);
        noteDetailsItem.setLikeIng(likeIng);
        noteDetailsItem.setCollectSum(collectSum);
        noteDetailsItem.setCollectIng(collecIng);
        noteDetailsItem.setFollowIng(followIng);
        return noteDetailsItem;
    }

    //添加笔记
    @PostMapping("/addNote")
    public NoteResponse addNote(@RequestBody Note note) {
        NoteResponse response = new NoteResponse();
        boolean result = noteDao.addNote(note);
        if (result) {
            response.setSuccess(true);
            response.setMessage("笔记添加成功");
        } else {
            response.setSuccess(false);
            response.setMessage("笔记添加失败");
        }
        return response;
    }

    //删除笔记
    @PostMapping("/deleteNote")
    public NoteResponse deleteNote(@RequestParam("note_id") Long note_id) {


        NoteResponse response = new NoteResponse();
        boolean result1 = noteDao.deleteNote(note_id);   //删除笔记
        boolean result2 = likeDao.delectNoteLike(note_id);   //删除相关的点赞
        boolean result3 = collectDao.deleteNoteCollect(note_id);   //删除相关的收藏
        boolean result4 = commentDao.delectNoteComment(note_id);   //删除相关的评论

        if (result1) {
            response.setSuccess(true);
            response.setMessage("笔记删除成功");
        } else {
            response.setSuccess(false);
            response.setMessage("笔记删除失败");
        }

        if (result2) {
            response.setMessage("笔记相关点赞删除成功");
        } else {
            response.setMessage("笔记相关点赞删除失败");
        }
        if (result3) {
            response.setMessage("笔记相关收藏删除成功");
        } else {
            response.setMessage("笔记相关收藏删除失败");
        }

        if (result4) {
            response.setMessage("笔记相关评论删除成功");
        } else {
            response.setMessage("笔记相关评论删除失败");
        }
        return response;
    }





    //浏览量+1
    @PostMapping("/addPageViews")
    public NoteResponse addPageViews(@RequestParam("note_id") Long note_id) {

        NoteResponse noteResponse = new NoteResponse();

        boolean success = noteDao.addPageViews(note_id);
        if (success) {
            noteResponse.setSuccess(true);
            noteResponse.setMessage("浏览量添加成功");
            return noteResponse;

        } else {
            noteResponse.setSuccess(false);
            noteResponse.setMessage("浏览量添加失败");
            return noteResponse;
        }

    }

    // 修改笔记信息
    @PostMapping("/revampNote")
    public NoteResponse revampNote(@RequestBody Note note) {
        NoteResponse noteResponse = new NoteResponse();


        Note updatedNote = noteDao.revampNote(note.getNoteId(), note);
        if (updatedNote != null) {
            noteResponse.setSuccess(true);
            noteResponse.setMessage("笔记更改成功");
            noteResponse.setNote(updatedNote);
        } else {
            noteResponse.setSuccess(false);
            noteResponse.setMessage(" 笔记更改失败");
        }
        return noteResponse;
    }

    //修改可见性
    @PostMapping("/updateNoteVisibility")
    public NoteResponse updateNoteVisibility(@RequestParam("user_id") Long user_id,@RequestParam("visibility") int visibility) {

        NoteResponse noteResponse = new NoteResponse();
        System.out.println("[DEBUG] 收到请求 - user_id: " + user_id + ", visibility: " + visibility);

        Boolean success = noteDao.updateNoteVisibility(user_id,visibility);

        System.out.println(success);

        if (success) {
            noteResponse.setSuccess(true);
            noteResponse.setMessage("修改成功");
            return noteResponse;

        } else {
            noteResponse.setSuccess(false);
            noteResponse.setMessage("修改失败");
            return noteResponse;
        }

    }


    // 获取所有笔记
    @PostMapping("/getAllNotes")
    public  ListTResponse<List<Note>> getAllNotes() {
        ListTResponse<List<Note>> response = new ListTResponse();
        List<Note> noteList = noteDao.list(); // 从数据库查询用户列表
//        System.out.println("查询到用户数量：" + userList.size()); // 添加日志

        response.setSuccess(true);
        response.setMessage("查询成功");
        response.setList(noteList);
        return response;

    }

}