package com.example.registerapplication;


public class ViewUtils {
    public static int calculateImageHeight(int originalWidth, int originalHeight, int targetWidth) {
        return (int) ((float) originalHeight / originalWidth * targetWidth);
    }
}