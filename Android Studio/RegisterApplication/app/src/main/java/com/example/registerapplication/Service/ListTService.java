package com.example.registerapplication.Service;


import com.example.registerapplication.Entity.Collect;
import com.example.registerapplication.Entity.Comment;
import com.example.registerapplication.Entity.Data.FollowItem;
import com.example.registerapplication.Entity.Data.NoteDetailsItem;
import com.example.registerapplication.Entity.Data.NoteHistoryltem;
import com.example.registerapplication.Entity.Data.SideCommentItem;
import com.example.registerapplication.Entity.Follow;
import com.example.registerapplication.Entity.Like;
import com.example.registerapplication.Entity.Message;
import com.example.registerapplication.Entity.Data.CommentItem;
import com.example.registerapplication.Entity.Data.MessageItem;
import com.example.registerapplication.Entity.Data.NoteItem;
import com.example.registerapplication.Entity.Note;
import com.example.registerapplication.Entity.User;
import com.example.registerapplication.Response.ListTResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ListTService {

    //搜索列表
    @GET("/api/search/noteSearch")
    Call<ListTResponse<List<NoteItem>>> noteSearch(
            @Query("keywords") List<String> keywords,
            @Query("userIng") Long userIng
    );


    //搜索用户
    @GET("/api/search/adminUserSearch")
    Call<ListTResponse<List<User>>> adminUserSearch(@Query("keywords") List<String> keywords);


    //搜索笔记
    @GET("/api/search/adminNoteSearch")
    Call<ListTResponse<List<Note>>> adminNoteSearch(@Query("keywords") List<String> keywords);

    //搜索留言
    @GET("/api/search/adminMessageSearch")
    Call<ListTResponse<List<MessageItem>>> adminMessageSearch(@Query("keywords")List<String> keywords);


    //所有笔记列表
    @POST("getAllNotes")
    Call<ListTResponse<List<Note>>> getAllNotes();

    // 所有用户列表
    @POST("getAllUsers")
    Call<ListTResponse<List<User>>> getAllUsers();

    // 浏览历史列表
    @POST("browseHistory")
    Call<ListTResponse<List<NoteItem>>> browseHistory(@Body List<NoteHistoryltem> historyList);


    //获取兴趣笔记列表
    @GET("homeNoteItems")
    Call<ListTResponse<List<NoteItem>>> homeNoteItems(@Query("currentTitle") String currentTitle, @Query("userIng") Long userIng);

    //获取关注笔记列表
    @GET("attentinNoteItems")
    Call<ListTResponse<List<NoteItem>>> attentinNoteItems(@Query("currentTitle") String currentTitle, @Query("userIng") Long userIng);

    //获取我的作品笔记列表
    @GET("me1NoteItems")
    Call<ListTResponse<List<NoteItem>>> me1NoteItems(@Query("currentTitle")String currentTitle, @Query("userIng") Long userIng);

    //获取我的收藏和点赞笔记列表
    @GET("me2me3NoteItems")
    Call<ListTResponse<List<NoteItem>>> me2me3NoteItems(@Query("currentTitle")String currentTitle, @Query("userIng") Long userIng);



    //获取评论列表
    @GET("commentItem")
    Call<ListTResponse<List<CommentItem>>> commentItem(@Query("noteIng") Long noteIng, @Query("noteUsreId") Long noteUsreId, @Query("userIng") Long userIng);

    //获取聊天列表
    @GET("messageItem")
    Call<ListTResponse<List<MessageItem>>> messageItem(@Query("userIng") Long userIng);

    //获取我的评论
    @GET("side2Comment")
    Call<ListTResponse<List<SideCommentItem>>> side2Comment(@Query("userIng") Long userIng);


    //获取我收到点赞和收藏
    @GET("mssage1GView1Like")
    Call<ListTResponse<List<SideCommentItem>>> mssage1GView1Like(@Query("userIng") Long userIng);

    //获取我收到的评论
    @GET("mssage1GView3Comment")
    Call<ListTResponse<List<SideCommentItem>>> mssage1GView3Comment(@Query("userIng") Long userIng);

    //获取信息列表
    @GET("getMessages")
    Call<ListTResponse<List<Message>>> getMessages(@Query("userIng")Long userIng,@Query("targetUserId") Long targetUserId);

    //获取关注和粉丝列表
    @GET("eqFollowFanList")
    Call<ListTResponse<List<FollowItem>>> eqFollowFanList(@Query("currentTitle")String currentTitle,@Query("userIng")Long userIng);

    //查询当前评论的点赞数
    @GET("eqCommentLikeSum")
    Call<ListTResponse<Long>> eqCommentLikeSum(@Query("commentId") Long commentId);

    //查询笔记详情
    @GET("eqNoteDetailsItem")
    Call<ListTResponse<NoteDetailsItem>> eqNoteDetailsItem(@Query("userIng")Long userIng,@Query("note_id")Long note_id);

    //信息标记已读
    @POST("markMessageAsRead")
    Call<ListTResponse> markMessageAsRead(@Query("messageId") Long messageId);

    //添加点赞
    @POST("addLike")
    Call<ListTResponse> addLike(@Body Like like);

    //添加评论点赞
    @POST("addLikeComment")
    Call<ListTResponse> addLikeComment(@Body Like like);

    //添加收藏
    @POST("addCollect")
    Call<ListTResponse> addCollect(@Body Collect collect);

    //添加评论
    @POST("addComment")
    Call<ListTResponse> addComment(@Body Comment comment);

    //添加关注
    @POST("addFollow")
    Call<ListTResponse> addFollow(@Body Follow follow);

    //添加聊天信息
    @POST("addMessage")
    Call<ListTResponse> addMessage(@Body Message message_id);

    //删除点赞
    @POST("delectLike")
    Call<ListTResponse> delectLike(@Query("user_id") Long user_id, @Query("note_id") Long note_id);

    //删除评论点赞
    @POST("deleteLikeComment")
    Call<ListTResponse> deleteLikeComment(@Query("user_id") Long user_id, @Query("comment_id") Long comment_id);

    //删除收藏
    @POST("deleteCollect")
    Call<ListTResponse> deleteCollect(@Query("user_id") Long user_id, @Query("note_id") Long note_id);

    //删除评论
    @POST("deleteComment")
    Call<ListTResponse> deleteComment(@Query("user_id") Long user_id, @Query("comment_id") Long comment_id);

    //删除关注
    @POST("delectFollow")
    Call<ListTResponse> delectFollow(@Query("user_id") Long user_id, @Query("follow_user") Long follow_user);

    //获取关注状态
    @POST("eqtFollowIng")
    Call<ListTResponse<Integer>> eqtFollowIng(@Query("userIng") Long userIng, @Query("user_id") Long user_id);


}
