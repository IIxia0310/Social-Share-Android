package com.example.registerapplication.Service;

import com.example.registerapplication.Entity.Note;
import com.example.registerapplication.Entity.User;
import com.example.registerapplication.Response.ListTResponse;
import com.example.registerapplication.Response.NoteResponse;
import com.example.registerapplication.Response.UserResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface NoteService {

    //添加笔记
    @POST("addNote")
    Call<NoteResponse> addNote(@Body Note note);

    //删除笔记
    @POST("deleteNote")
    Call<NoteResponse> deleteNote(@Query("note_id") Long note_id);

    //浏览量+1
    @POST("addPageViews")
    Call<NoteResponse> addPageViews(@Query("note_id") long note_id);

    //修改笔记信息
    @POST("revampNote")
    Call<NoteResponse> revampNote(@Body Note note);

    //修改可见性
    @POST("updateNoteVisibility")
    Call<NoteResponse> updateNoteVisibility(@Query("user_id") Long user_id, @Query("visibility") int visibility);


}
