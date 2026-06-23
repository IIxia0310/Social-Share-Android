package com.example.registerapplication.View;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.registerapplication.Adapters.FanAdapter;
import com.example.registerapplication.Entity.Data.FollowItem;
import com.example.registerapplication.Entity.Data.UserItem;
import com.example.registerapplication.Entity.User;
import com.example.registerapplication.R;
import com.example.registerapplication.Response.ListTResponse;
import com.example.registerapplication.Service.ListTService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MeFan extends AppCompatActivity {


    private Toolbar toolbar;
    private TextView fanSumText;
    private RecyclerView recyclerView;
    private ListTService listTService;
    private List<FollowItem> fanList;
    private FanAdapter fanAdapter;

    private boolean isLoading = false;
    private LinearLayoutCompat theme;
    private UserItem userItem =UserItem.getUserItem();
    private User user =userItem.getUser();
    private  Long fanSum;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.me1_fan);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        listTService = retrofit.create(ListTService.class);


        // 获取传递过来的followSum数据
        Intent intent = getIntent();
        if (intent != null) {
            fanSum = intent.getLongExtra("fanSum", -1);
            if (fanSum != -1) {
                // 可以在这里使用followSum数据，例如打印日志或者进行其他操作
                Log.d("MeFan", "接收到的fanSum: " + fanSum);
                Toast.makeText(this, "接收到的v: " + fanSum, Toast.LENGTH_SHORT).show();
            } else {
                Log.w("MeFan", "未获取到followSum数据，使用默认值 -1");
            }
        } else {
            Log.e("MeFan", "获取Intent失败");
        }

        initToolbar();
        initViews(); // 先初始化视图
        loadFollowInfo();        // 初始化关注列表


    }


    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.Toolbar_MeFan);
        toolbar.setTitle("我的粉丝");
        toolbar.setNavigationIcon(R.drawable.toolbar_2);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    //初始化视图
    private void initViews() {


        fanSumText = findViewById(R.id.MeFan_fan_sum);
        String fanSumS = fanSum != null ? String.valueOf(fanSum) : "0";
        fanSumText.setText("共有 " + fanSumS + " 个粉丝");



        // 绑定组件id
        theme = findViewById(R.id.Theme_MeFan);
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
        recyclerView = findViewById(R.id.MeFan_RecyclerView);

        // 初始化 followList
        fanList = new ArrayList<>();
        fanAdapter = new FanAdapter(fanList, listTService, user.getUserId());
        recyclerView.setAdapter(fanAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


    private void loadFollowInfo(){

        String currentTitle = "我的粉丝";
        if(user.getUserId()!=null){

            Call<ListTResponse<List<FollowItem>>> call = listTService.eqFollowFanList(currentTitle,user.getUserId());
            call.enqueue(new Callback<ListTResponse<List<FollowItem>>>() {
                @Override
                public void onResponse(Call<ListTResponse<List<FollowItem>>> call, Response<ListTResponse<List<FollowItem>>> response) {
                    if (response.isSuccessful()) {
                        ListTResponse<List<FollowItem>> newNotes = response.body();
                        if (newNotes != null) {
                            List<FollowItem> followItemsArray = newNotes.getList();
                            if (followItemsArray != null &&!followItemsArray.isEmpty()) {
                                fanList.clear();
                                fanList.addAll(followItemsArray);
                                // 通知适配器数据已更新
                                fanAdapter.notifyDataSetChanged();
                            } else {
                                // 数据为空，清空适配器
                                fanList.clear();
                                fanAdapter.notifyDataSetChanged();
                            }
                            isLoading = false;
                        } else {
                            Log.e("MeFan", "响应体为空");
                            // 响应体为空，清空适配器
                            fanList.clear();
                            fanAdapter.notifyDataSetChanged();
                            isLoading = false;
                        }
                    } else {
                        Log.e("MeFan", "请求不成功，状态码: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<ListTResponse<List<FollowItem>>> call, Throwable t) {
                    Log.e("MeFan", "请求失败", t);
                    // 请求失败，清空适配器
                    fanList.clear();
                    fanAdapter.notifyDataSetChanged();
                    isLoading = false;
                }
            });
        }
    }

}