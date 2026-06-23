package com.example.registerapplication.Entity.NoteSearch;


public class HotItem {
    private String title;
    private int hotCount;

    public HotItem(String title, int hotCount) {
        this.title = title;
        this.hotCount = hotCount;
        // 移除图片资源ID字段
    }

    public String getTitle() {
        return title;
    }

    public int getHotCount() {
        return hotCount;
    }
}