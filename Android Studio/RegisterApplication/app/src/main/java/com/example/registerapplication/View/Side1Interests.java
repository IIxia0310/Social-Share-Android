package com.example.registerapplication.View;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.example.registerapplication.Adapters.InterestsAdapter;
import com.example.registerapplication.Entity.Data.InterestsItem;
import com.example.registerapplication.Entity.Data.UserItem;
import com.example.registerapplication.Entity.User;
import com.example.registerapplication.Response.UserResponse;
import com.example.registerapplication.Service.UserService;
import com.example.registerapplication.R;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Side1Interests extends AppCompatActivity {
    // 定义接口
    public interface OnInterestsUpdatedListener {
        void onInterestsUpdated(String interests);
    }

    private Side1Interests currentActivity = Side1Interests.this;
    private GridView gridView;
    private Button confirmButton;
    private InterestsAdapter adapter;
    private String interests;
    private UserService userService;
    List<InterestsItem> dataList = new ArrayList<>();

    private LinearLayout theme;
    private UserItem userItem = UserItem.getUserItem();
    private User user = userItem.getUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.side1_interests);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        userService = retrofit.create(UserService.class);


        initViews();
        initButton();
    }


    private void initViews() {
        gridView = findViewById(R.id.Interests1_gridView);
        // 准备数据
        dataList = new ArrayList<>();
        dataList.add(new InterestsItem(R.drawable.a1, "旅游"));
        dataList.add(new InterestsItem(R.drawable.a2, "美食"));
        dataList.add(new InterestsItem(R.drawable.a3, "彩妆"));
        dataList.add(new InterestsItem(R.drawable.a4, "护肤"));
        dataList.add(new InterestsItem(R.drawable.a5, "发型"));
        dataList.add(new InterestsItem(R.drawable.a6, "游戏"));
        dataList.add(new InterestsItem(R.drawable.a7, "摄影"));
        dataList.add(new InterestsItem(R.drawable.a8, "影视"));
        dataList.add(new InterestsItem(R.drawable.a9, "头像"));
        dataList.add(new InterestsItem(R.drawable.a10, "搞笑"));
        dataList.add(new InterestsItem(R.drawable.a11, "音乐"));
        dataList.add(new InterestsItem(R.drawable.a12, "学习"));
        dataList.add(new InterestsItem(R.drawable.a13, "舞蹈"));
        dataList.add(new InterestsItem(R.drawable.a14, "绘画"));
        dataList.add(new InterestsItem(R.drawable.a15, "家居"));
        dataList.add(new InterestsItem(R.drawable.a16, "情感"));
        dataList.add(new InterestsItem(R.drawable.a17, "明星"));
        dataList.add(new InterestsItem(R.drawable.a18, "运动"));
        dataList.add(new InterestsItem(R.drawable.a19, "健身"));
        dataList.add(new InterestsItem(R.drawable.a20, "社交"));
        dataList.add(new InterestsItem(R.drawable.a21, "露营"));
        dataList.add(new InterestsItem(R.drawable.a22, "机车"));
        dataList.add(new InterestsItem(R.drawable.a23, "汽车"));
        dataList.add(new InterestsItem(R.drawable.a24, "婚礼"));
        dataList.add(new InterestsItem(R.drawable.a25, "母婴"));
        dataList.add(new InterestsItem(R.drawable.a26, "宠物"));
        dataList.add(new InterestsItem(R.drawable.a27, "家具"));

        adapter = new InterestsAdapter(this, dataList);
        gridView.setAdapter(adapter);

        theme = findViewById(R.id.Theme_Side1Interests);
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
            getUserInterests(user.getUserId());
        }
    }

    private void initButton() {
        //确认按钮
        confirmButton = findViewById(R.id.btn_confirm);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user == null) {
                    Toast.makeText(currentActivity, "用户信息未获取到，请稍后重试", Toast.LENGTH_SHORT).show();
                    return;
                }
                List<Integer> selectedIndices = adapter.getSelectedIndices();
                if (selectedIndices.isEmpty()) {
                    Toast.makeText(currentActivity, "请至少选择一项", Toast.LENGTH_SHORT).show();
                } else {
                    StringBuilder message = new StringBuilder();
                    for (int index : selectedIndices) {
                        // 明确类型转换
                        InterestsItem item = (InterestsItem) adapter.getItem(index);
                        if (item != null) {
                            String selectedText = item.getText();
                            message.append(selectedText).append(", ");
                        }
                    }
                    // 移除最后的逗号和空格
                    if (message.length() > 3) {
                        message.setLength(message.length() - 2);
                    }
                    interests = message.toString();

                    Call<UserResponse> call = userService.updateUserInterests(user.getUserId(), interests);
                    call.enqueue(new Callback<UserResponse>() {
                        @Override
                        public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                            ResponseBody error = response.errorBody();

                            if (error == null) {
                                UserResponse userResponse = response.body();
                                if (userResponse != null && userResponse.isSuccess()) {
                                    // 关闭页面
                                    Toast.makeText(currentActivity, "已选择兴趣：" + interests, Toast.LENGTH_SHORT).show();
                                    // 设置结果并返回
                                    Intent resultIntent = new Intent();
                                    resultIntent.putExtra("interests", interests);
                                    setResult(RESULT_OK, resultIntent);
                                    finish();

                                } else {
                                    // 弹窗
                                    Toast.makeText(currentActivity, "选择失败，", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                try {
                                    String data = error.string();
                                    Log.e("LoginError", data); // 使用Log.e记录错误日志
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                        }

                        @Override
                        public void onFailure(Call<UserResponse> call, Throwable t) {
                            Log.e("LoginFailure", "网络请求失败: " + t.getMessage()); // 记录失败日志
                        }
                    });
                }
            }
        });
    }

    //获取用户数据
    private void getUserInterests(Long userId) {
        if (userId == null) {
            Log.e("Side1Interests", "用户ID为空，无法获取用户兴趣");
            return;
        }
        Call<UserResponse> call = userService.eqUserID(userId);
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                ResponseBody error = response.errorBody();
                if (error == null) {
                    UserResponse userResponse = response.body();
                    if (userResponse != null && userResponse.isSuccess()) {
                        String userInterests = userResponse.getUser().getInterests();
                        Log.d("Interests", "Retrieved user interests: " + userInterests);
                        Toast.makeText(currentActivity, "userInterests" + userInterests, Toast.LENGTH_SHORT).show();
                        setSelectedInterests(userInterests);
                    } else {
                        Toast.makeText(currentActivity, "获取用户兴趣失败", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    try {
                        String data = error.string();
                        Log.e("InterestsError", data);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Log.e("InterestsFailure", "获取用户兴趣网络请求失败: " + t.getMessage());
            }
        });
    }

    private void setSelectedInterests(String userInterests) {
        if (userInterests != null &&!userInterests.isEmpty()) {
            // 去除所有空格
            userInterests = userInterests.replaceAll("\\s+", "");
            String[] interestsArray = userInterests.split(",");
            Set<String> interestSet = new HashSet<>(Arrays.asList(interestsArray));

            for (int i = 0; i < dataList.size(); i++) {
                InterestsItem item = dataList.get(i);
                String itemText = item.getText().trim();
                // 忽略大小写进行比较
                if (interestSet.stream().anyMatch(interest -> interest.equalsIgnoreCase(itemText))) {
                    adapter.setItemSelected(i, true);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }
}