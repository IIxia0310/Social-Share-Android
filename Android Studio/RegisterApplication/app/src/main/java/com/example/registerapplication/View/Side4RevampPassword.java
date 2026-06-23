package com.example.registerapplication.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.registerapplication.Entity.Data.UserItem;
import com.example.registerapplication.Entity.User;
import com.example.registerapplication.Fragments.FragmentMe;
import com.example.registerapplication.R;
import com.example.registerapplication.Response.ListTResponse;
import com.example.registerapplication.Response.UserResponse;
import com.example.registerapplication.Service.UserService;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 修改密码
 */
public class Side4RevampPassword extends AppCompatActivity {

    private LinearLayoutCompat theme;
    private UserItem userItem = UserItem.getUserItem();
    private User user = userItem.getUser();

    private TextInputEditText currentPasswordEditText, newPasswordEditText, confirmPasswordEditText;
    private TextInputLayout currentPasswordLayout, newPasswordLayout, confirmPasswordLayout;

    private Button button;
    private UserService userService;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.side4_revamp_password);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        userService = retrofit.create(UserService.class);


        initToolbar();
        initViews();
        initButton();
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.Toolbar_Side4RevampPassword);
        toolbar.setTitle("修改密码");
        toolbar.setNavigationIcon(R.drawable.toolbar_2);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void initViews() {
        theme = findViewById(R.id.Theme_Side4RevampPassword);
        currentPasswordEditText = findViewById(R.id.currentPasswordEditText);
        newPasswordEditText = findViewById(R.id.newPasswordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        currentPasswordLayout = findViewById(R.id.currentPasswordLayout);
        newPasswordLayout = findViewById(R.id.newPasswordLayout);
        confirmPasswordLayout = findViewById(R.id.confirmPasswordLayout);



        if (user != null) {
            switch (user.getTheme()) {
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

    private void initButton() {

        //修改按钮
        button = findViewById(R.id.changePasswordButton);
        button.setOnClickListener(v -> {
            changePassword();
        });



    }

    private void changePassword() {
        // 清除之前的错误
        currentPasswordLayout.setError(null);
        newPasswordLayout.setError(null);
        confirmPasswordLayout.setError(null);



        // 获取输入内容
        String currentPassword = currentPasswordEditText.getText().toString().trim();
        String newPassword = newPasswordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();

        // 验证输入
        if (TextUtils.isEmpty(currentPassword)) {
            currentPasswordLayout.setError("请输入当前密码");
            currentPasswordEditText.requestFocus();
            return;
        }



        if (TextUtils.isEmpty(newPassword)) {
            newPasswordLayout.setError("请输入新密码");
            newPasswordEditText.requestFocus();
            return;
        }


        if(currentPassword.equals(user.getPassword())){

            if (!isPassword(newPassword)) {
                Toast.makeText(this, "密码长度在 6 到 16 位之间，且必须包含至少一个大写字母、一个小写字母和一个数字", Toast.LENGTH_SHORT).show();
                return;
            }else if (!newPassword.equals(confirmPassword)) {
                confirmPasswordLayout.setError("两次输入的密码不一致");
                confirmPasswordEditText.requestFocus();
                return;
            }else {
                Call<UserResponse> call = userService.updateUserPassword(user.getUserId(), newPassword);
                call.enqueue(new Callback<UserResponse>() {
                    @Override
                    public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                        ResponseBody error = response.errorBody();

                        if (error == null) {
                            UserResponse userResponse = response.body();
                            if (userResponse != null && userResponse.isSuccess()) {
                                // 关闭页面
                                Toast.makeText(Side4RevampPassword.this, "修改成功", Toast.LENGTH_SHORT).show();

                                // 清空输入框
                                currentPasswordEditText.setText("");
                                newPasswordEditText.setText("");
                                confirmPasswordEditText.setText("");
                                finish();

//                                UserItem.getUserItem().setUser(userResponse.getUser());
                            } else {
                                // 弹窗
                                Toast.makeText(Side4RevampPassword.this, "修改失败", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            try {
                                String data = error.string();
                                Log.e("Side4RevampPassword", data); // 使用Log.e记录错误日志
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<UserResponse> call, Throwable t) {
                        Log.e("Side4RevampPassword", "网络请求失败: " + t.getMessage()); // 记录失败日志
                    }
                });
            }

        }else {
            Toast.makeText(this, "旧密码错误", Toast.LENGTH_SHORT).show();

        }

    }



    // 判断密码格式
    private boolean isPassword(String password) {
        // 定义正则表达式，匹配 6 到 16 位的字符串，且必须包含至少一个大写字母、一个小写字母和一个数字
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{6,16}$";
        return password.matches(regex);
    }

}