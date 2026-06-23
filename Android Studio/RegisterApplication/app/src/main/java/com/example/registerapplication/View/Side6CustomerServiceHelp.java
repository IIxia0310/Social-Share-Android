package com.example.registerapplication.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.Toolbar;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.registerapplication.Entity.Data.UserItem;
import com.example.registerapplication.Entity.Message;
import com.example.registerapplication.Entity.User;
import com.example.registerapplication.Fragments.FragmentMessage;
import com.example.registerapplication.R;
import com.example.registerapplication.Response.ListTResponse;
import com.example.registerapplication.Service.ListTService;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * 客服帮助
 */
public class Side6CustomerServiceHelp extends AppCompatActivity {

    private LinearLayoutCompat theme;
    private EditText editText;
    private Button button;

    private String content;

    private UserItem userItem =UserItem.getUserItem();
    private User user = userItem.getUser();


    private ListTService listTService;
    private String create_time = getCurrentTime();  // 获取当前时间
    private Long userIng = UserItem.getUserItem().getUserlogin(); // 获取当前登录用户 ID


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.side6_customer_service_help);

        initToolbar();
        initViews(); // 先初始化视图

        // 初始化 Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        listTService = retrofit.create(ListTService.class);

    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.Toolbar_Side6CustomerServiceHelp);
        toolbar.setTitle("客服帮助");
        toolbar.setNavigationIcon(R.drawable.toolbar_2);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void  initViews(){
        theme = findViewById(R.id.Theme_Side6CustomerServiceHelp);
        if (user!= null) {
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

        editText = findViewById(R.id.Side6CustomerServiceHelp_editText);
        button = findViewById(R.id.Side6CustomerServiceHelp_button);
        button.setOnClickListener(v -> {
            CustomerServiceHelp(); // 调用获取数据方法
        });


    }

    private void CustomerServiceHelp() {

        // 获取输入的数据
        content = editText.getText().toString();

        //判断输入是否为空
        if (content.isEmpty()) {
            Toast.makeText(this, "请输入之后再提交", Toast.LENGTH_SHORT).show();
            return;
        }else {
            // 处理发送消息的逻辑
            Message message = new Message(null,userIng,1L,content,create_time,false);


            // 发送消息到服务器（使用 Retrofit 示例）
            Call<ListTResponse> call = listTService.addMessage(message);
            call.enqueue(new Callback<ListTResponse>() {
                @Override
                public void onResponse(Call<ListTResponse> call, Response<ListTResponse> response) {
                    if (response.isSuccessful()) {
                        ListTResponse responseBody = response.body();
                        if (responseBody != null && responseBody.isSuccess()) {




                            Toast.makeText(Side6CustomerServiceHelp.this, "留言成功", Toast.LENGTH_SHORT).show();
                            editText.setText("");
                        } else {
                            Log.e("Side6CustomerServiceHelp", "消息发送失败");
                        }
                    } else {
                        Log.e("Side6CustomerServiceHelp", "网络请求失败");
                    }
                }

                @Override
                public void onFailure(Call<ListTResponse> call, Throwable t) {
                    Log.e("Side6CustomerServiceHelp", "请求失败", t);
                }
            });


        }
    }

    // 时间
    private String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }
}