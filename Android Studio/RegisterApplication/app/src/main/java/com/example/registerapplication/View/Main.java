package com.example.registerapplication.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

import com.example.registerapplication.R;
import com.example.registerapplication.Adapters.MainCollector;
import com.example.registerapplication.Entity.User;
import com.example.registerapplication.Entity.Data.UserItem;
import com.example.registerapplication.Fragments.FragmentHome;
import com.example.registerapplication.Fragments.FragmentAdd;
import com.example.registerapplication.Fragments.FragmentAttentin;
import com.example.registerapplication.Fragments.FragmentMe;
import com.example.registerapplication.Fragments.FragmentMessage;
import com.example.registerapplication.Response.UserResponse;
import com.example.registerapplication.Service.UserService;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import lombok.NonNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 主页面类
 */
public class Main extends AppCompatActivity {

    private Main currentActivity = Main.this;
    private static final String TAG = "Main";
    public static boolean needRefresh = true;    // 静态布尔变量，可能用于控制某些刷新相关逻辑，具体用途需进一步明确
    public static int lastFragment;    // 用于记录上个选择的Fragment的索引，方便切换时处理
    private Fragment fragHome, fragAttention, fragAdd, fragMessage, fragMe;    // 各个Fragment实例，对应底部导航栏的不同页面
    private Fragment[] fragments;    // 用于存储所有Fragment实例的数组
    private LinearLayout linearLayout;    // Fragment容器的LinearLayout实例


    private BottomNavigationView bottomNavigationView;    // 底部导航栏控件实例

    private UserService userService;
    private Call<UserResponse> userCall;

    private UserItem userItem;
    private User user ;

    private static final int REQUEST_CODE_SWITCH_TOPIC = 3; // 定义请求码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a2_main1);
        MainCollector.addActivity(this);

        // 初始化 Retrofit 及 UserService（恢复网络请求逻辑）
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/") // 替换为实际服务器地址
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        userService = retrofit.create(UserService.class);

        initViews();   // 初始化控件方法
        initFragment();   // 初始化initFragment方法
//        GetUserData();//调用获取用户数据方法
    }



    // 初始化控件
    private void initViews() {
        linearLayout = findViewById(R.id.Main1_fragment);
        bottomNavigationView = findViewById(R.id.Main1_Bottom);

        if (needRefresh) {
            TranslateAnimation animation = new TranslateAnimation(
                    Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f,
                    Animation.RELATIVE_TO_PARENT, 1.0f, Animation.RELATIVE_TO_PARENT, 0.0f
            );
            animation.setDuration(2000);
            bottomNavigationView.startAnimation(animation);
        }
        updateBottomNavigationViewBackground(); // 初始化时更新背景
    }

    // 初始化initFragment
    private void initFragment() {

        fragHome = new FragmentHome();
        fragAttention = new FragmentAttentin();
        fragAdd = new FragmentAdd();
        fragMessage = new FragmentMessage();
        fragMe = new FragmentMe();

        fragments = new Fragment[]{fragHome, fragAttention, fragAdd, fragMessage, fragMe};
        switch (lastFragment) {
            case 0:
                showFragment(fragHome);
                break;
            case 1:
                showFragment(fragAttention);
                break;
            case 2:
                showFragment(fragAdd);
                break;
            case 3:
                showFragment(fragMessage);
                break;
            case 4:
                showFragment(fragMe);
                break;
            default:
                showFragment(fragHome);
                lastFragment = 0;
                break;
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.bnavigation_home:
                        if (lastFragment != 0) {
                            switchFragment(lastFragment, 0);
                            lastFragment = 0;
                        }
                        return true;
                    case R.id.bnavigation_attention:
                        if (lastFragment != 1) {
                            switchFragment(lastFragment, 1);
                            lastFragment = 1;
                        }
                        return true;
                    case R.id.bnavigation_add:   //添加笔记
                        Intent intent = new Intent(currentActivity, NoteAdd.class);
                        startActivity(intent);
                        return true;
                    case R.id.bnavigation_message:
                        if (lastFragment != 3) {
                            switchFragment(lastFragment, 3);
                            lastFragment = 3;
                        }
                        return true;
                    case R.id.bnavigation_me:
                        if (lastFragment != 4) {
                            switchFragment(lastFragment, 4);
                            lastFragment = 4;
                        }
                        return true;
                }
                return false;
            }
        });
    }

    private void showFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.Main1_fragment, fragment);
        transaction.commit();
    }

    private void switchFragment(int lastIndex, int index) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        // 隐藏上个Fragment
        transaction.hide(fragments[lastIndex]);
        if (!fragments[index].isAdded()) {
            transaction.add(R.id.Main1_fragment, fragments[index]);
        }
        transaction.show(fragments[index]);
        try {
            transaction.commit();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    //调用获取用户数据方法
    private void GetUserData() {

        long userId = UserItem.getUserItem().getUserlogin();

        userCall = userService.eqUserID(userId);
        userCall.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful()) {
                    UserResponse userResponse = response.body();
                    if (userResponse != null && userResponse.isSuccess()) {
                        User loadedUser = userResponse.getUser();
                        //存入获取的User
                        UserItem.getUserItem().setUser(loadedUser);

                    }
                } else {
                    Log.e(TAG, "Main: 响应失败，code=" + response.code());
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Log.e(TAG, "Main: 网络请求失败，t=" + t.getMessage());
            }
        });
    }

    /**
     * 引入toolbar菜单栏
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 加载菜单资源文件
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toolbar3_select:
                // 处理按钮1的逻辑，查找全部笔记


                Intent intent = new Intent(currentActivity, NoteSearch.class);
                startActivity(intent);

                return true;
            case R.id.toolbar4_eixt:
                // 处理退出按钮点击逻辑，例如退出应用
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("提示")
                        .setMessage("确定要退出吗？")
                        .setPositiveButton("退出", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                MainCollector.finishAll();
//                                finish();
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MainCollector.removeActivity(this);
        if (userCall != null && !userCall.isCanceled()) {
            userCall.cancel();
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SWITCH_TOPIC && resultCode == RESULT_OK) {
            if (data != null) {
                int selectedTheme = data.getIntExtra("selectedTheme", 0);
                user = UserItem.getUserItem().getUser();
                if (user != null) {
                    user.setTheme(selectedTheme);
                    updateBottomNavigationViewBackground(); // 更新底部导航栏背景
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateBottomNavigationViewBackground(); // 确保每次回到主页面时应用最新的主题
    }

    private void updateBottomNavigationViewBackground() {

        userItem = UserItem.getUserItem();


        user = userItem.getUser();
        if (user != null) {
            switch (user.getTheme()) {
                case 1:
                    bottomNavigationView.setBackgroundResource(R.drawable.background1_pink_blue);
                    break;
                case 2:
                    bottomNavigationView.setBackgroundResource(R.drawable.background2_yellow_blue);
                    break;
                case 3:
                    bottomNavigationView.setBackgroundResource(R.drawable.background3_pink_yellow);
                    break;
                case 4:
                    bottomNavigationView.setBackgroundResource(R.drawable.background4_pink_purple);
                    break;
                case 5:
                    bottomNavigationView.setBackgroundResource(R.drawable.background5_purple_green);
                    break;
                case 6:
                    bottomNavigationView.setBackgroundResource(R.drawable.background6_green_blue);
                    break;
                default:
                    bottomNavigationView.setBackgroundResource(R.drawable.background1_pink_blue);
                    break;
            }
        }
    }
}