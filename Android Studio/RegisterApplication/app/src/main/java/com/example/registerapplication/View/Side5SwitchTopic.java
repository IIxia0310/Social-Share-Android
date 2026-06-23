package com.example.registerapplication.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.registerapplication.Entity.Data.UserItem;
import com.example.registerapplication.Entity.User;
import com.example.registerapplication.R;
import com.example.registerapplication.Response.UserResponse;
import com.example.registerapplication.Service.UserService;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 切换主题
 */
public class Side5SwitchTopic extends AppCompatActivity {

    private Side5SwitchTopic currentActivity = Side5SwitchTopic.this;

    private UserService userService;

    private LinearLayoutCompat theme;
    private UserItem userItem;
    private User user ;

    private RadioGroup rgThemeGroup;
    private RadioButton theme1, theme2, theme3, theme4, theme5; //主题按钮
    private Button button;

    private int userTheme = 0; // 初始化 userTheme

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.side5_switch_topic);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        userService = retrofit.create(UserService.class);

        initToolbar();
        initViews();
        initButton();
        setupInitialSelection();
        setupRadioGroupListener();
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.Toolbar_Side5SwitchTopic);
        toolbar.setTitle("切换主题");
        toolbar.setNavigationIcon(R.drawable.toolbar_2);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void initViews() {
//        theme1 = (RadioButton) findViewById(R.id.rb_theme1);
//        theme2 = (RadioButton) findViewById(R.id.rb_theme2);
//        theme3 = (RadioButton) findViewById(R.id.rb_theme3);
//        theme4 = (RadioButton) findViewById(R.id.rb_theme4);
//        theme5 = (RadioButton) findViewById(R.id.rb_theme5);
        rgThemeGroup = findViewById(R.id.rg_theme_group);

        theme = findViewById(R.id.Theme_Side5SwitchTopic);

        userItem = UserItem.getUserItem();

        user =  userItem.getUser();
        // 从 user 对象获取初始主题值
        if (user != null) {
            userTheme = user.getTheme();
            if (userTheme > 0) {
                switch (userTheme) {
                    case 1:
                        theme.setBackgroundResource(R.drawable.background1_pink_blue);
                        break;
                    case 2:
                        theme.setBackgroundResource(R.drawable.background2_yellow_blue);
                        break;
                    case 3:
                        theme.setBackgroundResource(R.drawable.background3_pink_yellow);
                        break;
                    case 4:
                        theme.setBackgroundResource(R.drawable.background4_pink_purple);
                        break;
                    case 5:
                        theme.setBackgroundResource(R.drawable.background5_purple_green);
                        break;
                    default:
                        theme.setBackgroundResource(R.drawable.background1_pink_blue);
                        break;
                }
            }
        }
    }

    // 按钮监听
    private void initButton() {
        // 确定切换主题
        button = findViewById(R.id.Side5SwitchTopic_Button);
        button.setOnClickListener(v -> {
            revamp_theme();
        });
    }


    private void revamp_theme() {
        Toast.makeText(currentActivity, "当前选择的是："+userTheme, Toast.LENGTH_SHORT).show();

        Call<UserResponse> call = userService.updateUserTheme(user.getUserId(),userTheme);
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                ResponseBody error = response.errorBody();

                if (error == null) {
                    UserResponse userResponse = response.body();
                    if (userResponse != null && userResponse.isSuccess()) {
                        // 关闭页面
                        User user = userResponse.getUser();
                        UserItem.getUserItem().setUser(user); //存入获取的User

                        // 设置结果并返回新的主题信息
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("selectedTheme", userTheme);
                        setResult(RESULT_OK, resultIntent);
                        finish();

                    } else {
                        // 弹窗
                        Toast.makeText(currentActivity, "选择失败，", Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Log.e("LoginFailure", "网络请求失败: " + t.getMessage()); // 记录失败日志
            }
        });

    }


    private void setupRadioGroupListener() {
        rgThemeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int selectedTheme = getThemeFromRadioButtonId(checkedId);
                userTheme = selectedTheme; // 更新 userTheme 变量
                if (user != null) {
                    user.setTheme(userTheme);
                }
                saveSelectedTheme(selectedTheme);
                // 更新背景
                if (theme != null) {
                    switch (selectedTheme) {
                        case 1:
                            theme.setBackgroundResource(R.drawable.background1_pink_blue);
                            break;
                        case 2:
                            theme.setBackgroundResource(R.drawable.background2_yellow_blue);
                            break;
                        case 3:
                            theme.setBackgroundResource(R.drawable.background3_pink_yellow);
                            break;
                        case 4:
                            theme.setBackgroundResource(R.drawable.background4_pink_purple);
                            break;
                        case 5:
                            theme.setBackgroundResource(R.drawable.background5_purple_green);
                            break;
                        case 6:
                            theme.setBackgroundResource(R.drawable.background6_green_blue);
                            break;
                        default:
                            theme.setBackgroundResource(R.drawable.background1_pink_blue);
                            break;
                    }
                }
            }
        });
    }

    private void setupInitialSelection() {
        if (user != null) {
            int currentTheme = user.getTheme();
            int radioButtonId = getRadioButtonIdFromTheme(currentTheme);
            if (radioButtonId != -1) {
                rgThemeGroup.check(radioButtonId);
            }
        }
    }
    @Override
    public void onBackPressed() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("selectedTheme", userTheme);
        setResult(RESULT_OK, resultIntent);
        finish();
    }



    private int getRadioButtonIdFromTheme(int theme) {
        switch (theme) {
            case 1:
                return R.id.rb_theme1;
            case 2:
                return R.id.rb_theme2;
            case 3:
                return R.id.rb_theme3;
            case 4:
                return R.id.rb_theme4;
            case 5:
                return R.id.rb_theme5;
            case 6:
                return R.id.rb_theme6;
            default:
                return -1;
        }
    }

    private int getThemeFromRadioButtonId(int radioButtonId) {
        switch (radioButtonId) {
            case R.id.rb_theme1:
                return 1;
            case R.id.rb_theme2:
                return 2;
            case R.id.rb_theme3:
                return 3;
            case R.id.rb_theme4:
                return 4;
            case R.id.rb_theme5:
                return 5;
            case R.id.rb_theme6:
                return 6;
            default:
                return -1;
        }
    }

    private void saveSelectedTheme(int theme) {
        SharedPreferences prefs = getSharedPreferences("ThemePrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("SelectedTheme", theme);
        editor.apply();
    }


}