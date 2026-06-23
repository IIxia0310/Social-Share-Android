package com.example.registerapplication.View;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.registerapplication.FragmentAdmin.FragmentAdminData;
import com.example.registerapplication.FragmentAdmin.FragmentAdminMessage;
import com.example.registerapplication.FragmentAdmin.FragmentAdminNote;
import com.example.registerapplication.FragmentAdmin.FragmentAdminUser;
import com.example.registerapplication.R;


//管理员端主页
public class MainAdmin extends AppCompatActivity implements View.OnClickListener {
    // 左侧菜单项
    private LinearLayout userItem;
    private LinearLayout noteItem;
    private LinearLayout messageItem;
    private LinearLayout dataItem;
    // 顶部导航栏
    private TextView logoutText;
    private View sidebarToggle;
    // 抽屉布局
    private DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a2_main_admin);
        // 初始化视图组件
        initViews();
        // 设置菜单项点击事件
        userItem.setOnClickListener(this);
        noteItem.setOnClickListener(this);
        messageItem.setOnClickListener(this);
        sidebarToggle.setOnClickListener(this);
        // 默认显示用户管理界面
        showFragment(new FragmentAdminUser());
    }
    private void initViews() {
        // 初始化抽屉布局
        drawerLayout = findViewById(R.id.drawer_layout);
        // 初始化左侧菜单
        userItem = findViewById(R.id.user_item);
        noteItem = findViewById(R.id.note_item);
        messageItem = findViewById(R.id.message_item);
        // 初始化顶部导航栏
        sidebarToggle = findViewById(R.id.sidebar_toggle);
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.user_item) {
            showFragment(new FragmentAdminUser());
            closeDrawer();
        } else if (id == R.id.note_item) {
            showFragment(new FragmentAdminNote());
            closeDrawer();
        } else if (id == R.id.message_item) {
            showFragment(new FragmentAdminMessage());
            closeDrawer();
        }
        else if (id == R.id.sidebar_toggle) {
            toggleDrawer();     // 切换侧边栏状态
        }
    }
    private void showFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content_frame_layout, fragment);
        transaction.commit();
    }
    // 侧边栏操作方法
    private void toggleDrawer() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            drawerLayout.openDrawer(GravityCompat.START);
        }
    }
    private void closeDrawer() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }
    // 处理返回键逻辑
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}