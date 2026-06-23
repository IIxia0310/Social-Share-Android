package com.example.registerapplication.View;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.registerapplication.Entity.Data.UserItem;
import com.example.registerapplication.Entity.User;
import com.example.registerapplication.Fragments.FragmentAttentin;
import com.example.registerapplication.Fragments.FragmentHome;
import com.example.registerapplication.Fragments.FragmentMe;
import com.example.registerapplication.Fragments.FragmentMessage;
import com.example.registerapplication.Fragments.Me1Notes;
import com.example.registerapplication.Fragments.Me2Like;
import com.example.registerapplication.Fragments.Me3Collect;
import com.example.registerapplication.R;
import com.example.registerapplication.Response.UserResponse;
import com.example.registerapplication.Service.UserService;

import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//import com.example.registerapplication.Fragments.Me1Notes;

/**
 * 登录界面类
 */
public class LoginAdmin extends AppCompatActivity {

    private LoginAdmin currentActivity = LoginAdmin.this;
    private UserService userService;
    private EditText et_id, et_password;
    private Button button1;
    private TextView button2;
    private String string_id;
    private String password; // 用户密码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a1_login_admin);

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
        et_id = findViewById(R.id.LoginAdmin_id);
        et_password = findViewById(R.id.LoginAdmin_passwprd);
    }

    //按钮监听
    private void initButton() {

        // 登录监听按钮
        button1 = findViewById(R.id.LoginAdmin_button1);
        button1.setOnClickListener(v -> {
            GetAdminData(); // 调用获取用户数据方法
        });

        button2 = findViewById(R.id.LoginAdmin_button2);
        button2.setOnClickListener(v -> {
            Toast.makeText(currentActivity, "用户登录界面", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(currentActivity, Login.class);
            startActivity(intent);
        });

    }

   // 调用获取数据方法
    private void GetAdminData() {

        // 获取输入的数据
        string_id = et_id.getText().toString();
        password = et_password.getText().toString();



//        判断输入是否为空
        if (string_id.isEmpty()) {
            Toast.makeText(this, "请输入账号", Toast.LENGTH_SHORT).show();
            return;
        } else if (password.isEmpty()) {
            Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
            return;
        }
        login();


    }
    //登录方法
    private void login() {
        Toast.makeText(this, "管理员登录", Toast.LENGTH_SHORT).show();


        //登录
        Call<UserResponse> call = userService.loginAdmin(string_id,password);
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                ResponseBody error = response.errorBody();
                if (error == null) {
                    UserResponse userResponse = response.body();
                    if (userResponse != null && userResponse.isSuccess()) {
                        UserItem.getUserItem().setAdminlogin(string_id);
                        Toast.makeText(currentActivity, "管理员登录成功", Toast.LENGTH_SHORT).show();
                        Log.d("Login", "登录成功,登录ID是 "+string_id );

                        // 跳转
                        Intent intent = new Intent(currentActivity, MainAdmin.class);
                        startActivity(intent);
                        finish();
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
}