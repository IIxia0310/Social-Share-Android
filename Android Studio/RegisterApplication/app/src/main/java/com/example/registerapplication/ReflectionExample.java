package com.example.registerapplication;

import java.lang.reflect.Field;

public class ReflectionExample {

    public static void main(String[] args) {
        try {
            accessHiddenField();
        } catch (Exception e) {
            System.err.println("访问隐藏字段时发生错误: " + e.getMessage());
        }
    }

    public static void accessHiddenField() throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        // 通过反射获取 android.view.WindowInsets 类
        Class<?> windowInsetsClass = Class.forName("android.view.WindowInsets");
        // 获取 CONSUMED 字段
        Field consumedField = windowInsetsClass.getDeclaredField("CONSUMED");
        // 设置字段可访问
        consumedField.setAccessible(true);
        // 获取字段的值
        Object consumed = consumedField.get(null);
        System.out.println("成功获取到 WindowInsets.CONSUMED 的值: " + consumed);
    }
}