package com.example.registerapplication.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.registerapplication.Entity.Data.UserItem;
import com.example.registerapplication.Fragments.FragmentAttentin;
import com.example.registerapplication.Fragments.FragmentHome;
import com.example.registerapplication.Fragments.FragmentMe;
import com.example.registerapplication.Fragments.FragmentMessage;
//import com.example.registerapplication.Fragments.Me1Notes;
import com.example.registerapplication.Fragments.Me1Notes;
import com.example.registerapplication.Fragments.Me2Like;
import com.example.registerapplication.Fragments.Me3Collect;
import com.example.registerapplication.R;
import com.example.registerapplication.Response.UserResponse;
import com.example.registerapplication.Service.UserService;
import com.example.registerapplication.Entity.User;

import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 登录界面类
 */
public class Login extends AppCompatActivity {

    private Login currentActivity = Login.this;
    private UserService userService;
    private EditText et_id, et_password;
    private Button bt_login, bt_register;
    private TextView bt_admin;

    private String string_id;
    private long userId; // 用户 ID
    private String password; // 用户密码

    private Fragment fragHome =  new FragmentHome();
    private Fragment fragmentAttentin =  new FragmentAttentin();
    private Fragment fragmentMessage =  new FragmentMessage();
    private Fragment fragmentMe =  new FragmentMe();
    private Fragment me1Notes =  new Me1Notes();
    private Fragment me2Like =  new Me2Like();
    private Fragment me3Collect =  new Me3Collect();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a1_login);

        // 隐藏 ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        userService = retrofit.create(UserService.class);

        initViews(); // 先初始化视图
        initButton(); // 按钮

    }

    //初始化视图
    private void initViews() {
        // 绑定组件id
        et_id = findViewById(R.id.Login_id);
        et_password = findViewById(R.id.Login_passwprd);
    }

    //按钮监听
    private void initButton() {

        // 登录监听按钮
        bt_login = findViewById(R.id.Login_botton1);
        bt_login.setOnClickListener(v -> {
            GetUserData(); // 调用获取用户数据方法
        });

        // 注册监听按钮
        bt_register = findViewById(R.id.Login_botton2);
        bt_register.setOnClickListener(v -> {
            Toast.makeText(currentActivity, "注册界面", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(currentActivity, Register.class);
            startActivity(intent);
        });

        // 注册监听按钮
        bt_admin = findViewById(R.id.Login_botton3);
        bt_admin.setOnClickListener(v -> {
            Toast.makeText(currentActivity, "管理员登录界面", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(currentActivity, LoginAdmin.class);
            startActivity(intent);
        });
    }

   // 调用获取用户数据方法
    private void GetUserData() {

        // 获取输入的数据
        string_id = et_id.getText().toString();
        password = et_password.getText().toString();

        userId = Long.parseLong(string_id);
        login();//调用注册方法
        //判断输入是否为空
//        if (string_id.isEmpty()) {
//            Toast.makeText(this, "请输入账号", Toast.LENGTH_SHORT).show();
//            return;
//        } else if (password.isEmpty()) {
//            Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
//            return;
//        }

//        //判断输入格式是否正确
//        if (!isId(string_id)) {
//            Toast.makeText(this, "输入的不是有效的手机号", Toast.LENGTH_SHORT).show();
//            return;
//        } else if (!isPassword(password)) {
//            Toast.makeText(this, "密码长度在 6 到 16 位之间，且必须包含至少一个大写字母、一个小写字母和一个数字", Toast.LENGTH_SHORT).show();
//            return;
//        }else {
//            userId = Long.parseLong(string_id);
//            login();//调用注册方法
//        }
    }
    //登录方法
    private void login() {
        // 创建User对象
//        User user = new User(userId, password, null,1,
//                null,null,null,null,
//                null,null,
//                null,null);
        //登录
        Call<UserResponse> call = userService.login(userId,password);
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                ResponseBody error = response.errorBody();
                if (error == null) {
                    UserResponse userResponse = response.body();
                    if (userResponse != null && userResponse.isSuccess()) {
                        User user = userResponse.getUser();
                        UserItem.getUserItem().setUser(user); //存入获取的User
                        if (fragHome instanceof FragmentHome) {
                            ((FragmentHome) fragHome).setUserInfo(user); }// 传递用户信息到 FragmentHome
                        if (me1Notes instanceof Me1Notes) {
                            ((Me1Notes) me1Notes).setUserInfo(user);  } // 传递用户信息到 FragmentHome
                        int delayMillis = 50; // 设置延迟时间，单位为毫秒，这里设置为 2000 毫秒（即 2 秒）
                        new Handler().postDelayed(new Runnable() {  // 使用 Handler 延迟跳转页面
                            @Override
                            public void run() {
                                // 保存当前登录用户id
                                UserItem.getUserItem().setUserlogin(userId);
                                Toast.makeText(currentActivity, "登录成功", Toast.LENGTH_SHORT).show();
                                Log.d("Login", "登录成功,登录ID是 "+userId );
                                // 跳转
                                Intent intent = new Intent(currentActivity, Main.class);
                                startActivity(intent);
                                finish();
                            }
                        }, delayMillis);
                    } else {// 弹窗
                        Toast.makeText(currentActivity, "登录失败，请输入正确的账号密码", Toast.LENGTH_SHORT).show();
                        Log.d("Login", "登录失败,请输入正确的账号密码 " );

                    }
                }
            }
            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Log.e("Login", "网络请求失败: " + t.getMessage()); // 记录失败日志
            }
        });
    }

    // 判断手机号ID格式
    public boolean isId(String string_id) {
        // 正则表达式，用于匹配常见的中国手机号格式
        String regex = "^1[3-9]\\d{9}$";
        return Pattern.matches(regex, string_id);
    }

    // 判断密码格式
    private boolean isPassword(String password) {
        // 定义正则表达式，匹配 6 到 16 位的字符串，且必须包含至少一个大写字母、一个小写字母和一个数字
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{6,16}$";
        return password.matches(regex);
    }
}