package com.example.registerapplication;


import android.app.Application;
import com.hankcs.hanlp.HanLP;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化 HanLP
        new Thread(() -> {
            try {
                // 配置 HanLP 路径（可选）
                System.setProperty("hanlp.model_path", getFilesDir() + "/hanlp");
                // 预加载分词器
                HanLP.segment("预加载测试");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}