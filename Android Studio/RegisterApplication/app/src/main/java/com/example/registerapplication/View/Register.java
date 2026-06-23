package com.example.registerapplication.View;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.registerapplication.Entity.Data.UserItem;
import com.example.registerapplication.R;
import com.example.registerapplication.Response.UserResponse;
import com.example.registerapplication.Service.UserService;
import com.example.registerapplication.Entity.User;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import android.util.Log;

/**
 * 注册面类
 */
public class Register extends AppCompatActivity {

    private Register currentActivity = Register.this;

    //定义组件
    private EditText et_id, et_uname, et_passwrod;  //账号，密码，姓名
    private Spinner spinner_Year, spinner_Month, spinner_Day; //出生日期：年月日
    private RadioButton rbt_man, rbt_girl; //男生女生按钮
    private Button bt_login, bt_register;               //注册、返回
    private UserService loginRegisterService;


    private String string_id;
    private long userId; // 用户 ID
    private String password; // 用户密码
    private String username; // 用户名
    private String sex; // 性别
    private String interests; // 兴趣
    private String signature; // 个签
    private String location; // 定位
    private String portraitImage; // 头像
    private String backgroundImage; // 背景
    private String birthdayTime; // 出生日期
    private String createTime; // 注册时间


    private String selectedYear,selectedMonth,selectedDay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a1_register);

        // 隐藏 ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        loginRegisterService = retrofit.create(UserService.class);

        initViews(); // 先初始化视图
        initButton(); // 按钮

    }
    //初始化视图
    private void initViews() {

        //初始化组件:账号，密码，姓名
        et_id = findViewById(R.id.Register_id);
        et_passwrod = findViewById(R.id.Register_password);
        et_uname = findViewById(R.id.Register_name);

        //年月日
        spinner_Year = findViewById(R.id.Register_spinnerYear);
        spinner_Month = findViewById(R.id.Register_spinnerMonth);
        spinner_Day = findViewById(R.id.Register_spinnerDay);

        //性别
        rbt_man = (RadioButton) findViewById(R.id.Register_man);
        rbt_girl = (RadioButton) findViewById(R.id.Register_girl);

        // 设置年份数据
        List<String> yearList = new ArrayList<>();
        for (int i = 1920; i <= 2025; i++) {
            yearList.add(String.valueOf(i));
        }
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                yearList
        );
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_Year.setAdapter(yearAdapter);

        // 设置月份数据
        List<String> monthList = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            monthList.add(String.valueOf(i));
        }
        ArrayAdapter<String> monthAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                monthList
        );
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_Month.setAdapter(monthAdapter);

        // 设置日期数据（简单示例，未考虑大小月及闰年情况）
        List<String> dayList = new ArrayList<>();
        for (int i = 1; i <= 31; i++) {
            dayList.add(String.valueOf(i));
        }
        ArrayAdapter<String> dayAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                dayList
        );
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_Day.setAdapter(dayAdapter);

    }

    //按钮监听
    private void initButton() {

        //返回按钮监听
        bt_login = findViewById(R.id.Register_button1);
        bt_login.setOnClickListener(v -> {
            Toast.makeText(currentActivity, "登录界面", Toast.LENGTH_SHORT).show();
            finish();
        });
        //注册按钮监听
        bt_register = findViewById(R.id.Register_button2);
        bt_register.setOnClickListener(v -> {
            GetUserData();// 调用获取用户数据方法
        });
    }

    //调用获取用户数据方法
    private void GetUserData() {

        //账号:user_id ，密码:password  ，姓名:username
        string_id = et_id.getText().toString();      //账号
        password = et_passwrod.getText().toString(); //密码
        username = et_uname.getText().toString();    //姓名

        if (rbt_man.isChecked()) {                   //性别
            sex = rbt_man.getText().toString();
        } else {
            sex = rbt_girl.getText().toString();
        }
        interests="";                                // 兴趣
        signature = "这个人很懒，什么都没有留下~~";  //个性签名
        location ="";                                // 定位
        portraitImage = "image_portrait.tmp"; //头像
        backgroundImage = "image_background.tmp";//背景


        selectedYear = spinner_Year.getSelectedItem().toString();
        selectedMonth = spinner_Month.getSelectedItem().toString();
        selectedDay = spinner_Day.getSelectedItem().toString();
        birthdayTime = selectedYear + "-" + selectedMonth + "-" + selectedDay; // 出生日期
        createTime = getCurrentTime();        //当前时间


        //判断生日格式是否正确
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date birthDate = null;
        try {
            birthDate = sdf.parse(birthdayTime);
        } catch (ParseException e) {
            Toast.makeText(currentActivity, "日期格式不正确，请检查输入", Toast.LENGTH_SHORT).show();
            return;
        }
        //判断姓名是否有空格
        boolean isUserName = false;
        for (int i = 0; i < username.length(); i++) {
            char ch = username.charAt(i);
            if (Character.isWhitespace(ch) || (!Character.isLetterOrDigit(ch))) {
                isUserName = true;
                break;
            }
        }


        //判断输入是否为空
        if (string_id.isEmpty()) {
            Toast.makeText(this, "请输入账号", Toast.LENGTH_SHORT).show();
            return;
        } else if (password.isEmpty()) {
            Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
            return;
        } else if (username.isEmpty()) {
            Toast.makeText(this, "请输入姓名", Toast.LENGTH_SHORT).show();
            return;
        }
        //判断输入格式是否正确
        if (!isId(string_id)) {
            Toast.makeText(this, "输入的不是有效的手机号", Toast.LENGTH_SHORT).show();
            return;
        }else if (!isPassword(password)) {
            Toast.makeText(this, "密码长度在 6 到 16 位之间，且必须包含至少一个大写字母、一个小写字母和一个数字", Toast.LENGTH_SHORT).show();
            return;
        }else if(isUserName){
            Toast.makeText(this, "（姓名）包含空格或特殊符号，请重新输入", Toast.LENGTH_SHORT).show();
        }else {
            userId = Long.parseLong(string_id);
            register();//调用注册方法
        }

    }
    //注册方法
    private void register() {
        // 假设User类重写了toString方法
        User user = new User(userId, password, username,1, sex, interests, signature, location, portraitImage, backgroundImage, birthdayTime, createTime);
        Log.d("user", user.toString());

        // 用户注册，调用接口，传User
        Call<UserResponse> call = loginRegisterService.register(user);
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                ResponseBody error = response.errorBody();
                if (error == null) {
                    UserResponse userResponse = response.body();
                    if (userResponse != null && userResponse.isSuccess()) {
                        User user = userResponse.getUser();
                        if (user == null) {
                            Log.e("Register", "注册成功后获取的User对象为空");
                        } else {
                            Log.d("Register", "注册成功，" +
                                    "即将设置User对象到UserItem，userId: " + user.getUserId());
                            UserItem.getUserItem().setUser(user);
                        }
                        Intent intent = new Intent(currentActivity, Side1Interests.class);
                        intent.putExtra("user", user);
                        startActivity(intent);
                        finish();
                        Toast.makeText(currentActivity, "注册成功", Toast.LENGTH_SHORT).show();
                        Log.d("Register", "注册成功");
                    } else {
                        Toast.makeText(currentActivity, "用户存在！注册失败", Toast.LENGTH_SHORT).show();
                        Log.d("Register", "注册失败");
                    }
                } else {
                    try {
                        String data = error.string();
                        Log.i("data", data);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Log.e("Register", "网络请求失败: " + t.getMessage()); // 记录失败日志
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

    //获取当前时间
    private String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }


}
