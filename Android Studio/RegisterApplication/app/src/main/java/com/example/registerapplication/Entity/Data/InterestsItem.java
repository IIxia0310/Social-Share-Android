package com.example.registerapplication.Entity.Data;


public class InterestsItem {
    private int imageResId;
    private String text;

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }

    public void setText(String text) {
        this.text = text;
    }

    public InterestsItem(int imageResId, String text) {
        this.imageResId = imageResId;
        this.text = text;
    }
    @Override
    public String toString() {
        return text;
    }

    public int getImageResId() {
        return imageResId;
    }

    public String getText() {
        return text;
    }
}