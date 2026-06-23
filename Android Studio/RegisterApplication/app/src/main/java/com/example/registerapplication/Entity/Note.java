package com.example.registerapplication.Entity;

import java.io.Serializable;

/**
 * 笔记实体类
 */
public class Note implements Serializable {
    // 添加无参构造函数

    private Long noteId; // 帖子内容id
    private Long userId; // 用户名
    private Long page_views; //浏览量
    private int visibility; // 可见性（1-公开，2-私密，3-互关）
    private int draft; // 草稿（1-是，2-否，3-删除）
    private String interest; // 兴趣分类
    private String title; // 标题
    private String content; // 笔记文字
    private String imageUrls; // 笔记图片
    private String videoUrl; // 笔记视频
    private String createTime; // 创建时间
    private String updateTime; // 更新时间


    public Note() {
    }
    public Note(Long noteId, Long userId, Long page_views, int visibility, int draft, String interest, String title, String content, String imageUrls, String videoUrl, String createTime, String updateTime) {
        this.noteId = noteId;
        this.userId = userId;
        this.page_views = page_views;
        this.visibility = visibility;
        this.draft = draft;
        this.interest = interest;
        this.title = title;
        this.content = content;
        this.imageUrls = imageUrls;
        this.videoUrl = videoUrl;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public Long getPage_views() {
        return page_views;
    }

    public void setPage_views(Long page_views) {
        this.page_views = page_views;
    }

    public Long getNoteId() {
        return noteId;
    }

    public void setNoteId(Long noteId) {
        this.noteId = noteId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }


    public int getVisibility() {
        return visibility;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

    public int getDraft() {
        return draft;
    }

    public void setDraft(int draft) {
        this.draft = draft;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(String imageUrls) {
        this.imageUrls = imageUrls;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "Note{" +
                "noteId=" + noteId +
                ", userId=" + userId +
                ", page_views=" + page_views +
                ", visibility=" + visibility +
                ", draft=" + draft +
                ", interest='" + interest + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", imageUrls='" + imageUrls + '\'' +
                ", videoUrl='" + videoUrl + '\'' +
                ", createTime='" + createTime + '\'' +
                ", updateTime='" + updateTime + '\'' +
                '}';
    }
}