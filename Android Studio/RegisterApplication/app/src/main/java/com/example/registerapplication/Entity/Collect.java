package com.example.registerapplication.Entity;

/**
 *  收藏实体类
 */
public class Collect {

    private Long CollectId; //收藏记录唯一标识
    private Long userId; //用户 ID
    private Long noteId; //被收藏笔记的 ID
    private String createTime; //收藏时间

    public Long getCollectId() {
        return CollectId;
    }

    public void setCollectId(Long collectId) {
        CollectId = collectId;
    }

    public Collect(Long collectId, Long userId, Long noteId, String createTime) {
        CollectId = collectId;
        this.userId = userId;
        this.noteId = noteId;
        this.createTime = createTime;
    }
}