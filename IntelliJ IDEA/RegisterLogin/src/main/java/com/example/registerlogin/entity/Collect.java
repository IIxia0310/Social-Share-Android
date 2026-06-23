package com.example.registerlogin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

/**
 *  收藏实体类
 */
public class Collect {


    @TableId(value = "collect_id", type = IdType.AUTO) // value 为主键字段名，type 为主键生成策略

    private Long collectId; //收藏记录唯一标识
    private Long userId; //用户 ID
    private Long noteId; //被收藏笔记的 ID
    private String createTime; //收藏时间

    public Collect() {

    }

    public void setCollectId(Long collectId) {
        this.collectId = collectId;
    }

    public Long getCollectId() {
        return collectId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getNoteId() {
        return noteId;
    }

    public void setNoteId(Long noteId) {
        this.noteId = noteId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Collect(Long collectId, Long userId, Long noteId, String createTime) {
        this.collectId = collectId;
        this.userId = userId;
        this.noteId = noteId;
        this.createTime = createTime;
    }



}