package com.example.registerapplication.View;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.registerapplication.Adapters.MessageDetailsItemAdapter;
import com.example.registerapplication.Entity.Data.UserItem;
import com.example.registerapplication.Entity.Follow;
import com.example.registerapplication.Entity.Message;
import com.example.registerapplication.Entity.Data.MessageItem;
import com.example.registerapplication.Entity.User;
import com.example.registerapplication.R;
import com.example.registerapplication.Response.ListTResponse;
import com.example.registerapplication.Service.ListTService;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MessageDetails extends AppCompatActivity implements MessageDetailsItemAdapter.MessageUpdateListener {
    private static final String TAG = "MessageDetails";

    private ImageView toolbarButton1;
    private Button toolbarButton2;

    private CircleImageView iv_PImage; // 头像
    private TextView tv_name;  // 姓名

    private RecyclerView rv_message_list; // 信息列表
    private MessageDetailsItemAdapter messageDetailsItemAdapter;

    private EditText chatInput;  // 输入框
    private Button sendButton;   // 发送按钮

    private Long userIng = UserItem.getUserItem().getUserlogin(); // 获取当前登录用户 ID
    private String create_time = getCurrentTime();  // 获取当前时间

    private boolean isLoading = false;

    private ListTService listTService;
    private MessageItem messageItem;

    private List<Message> messageList = new ArrayList<>();
    private LinearLayout theme;
    private UserItem userItem =UserItem.getUserItem();
    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "MessageDetails onCreate 方法被调用");
        setContentView(R.layout.message_details);

        // 初始化 Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        listTService = retrofit.create(ListTService.class);

        // 初始化组件
        initViews();
        initButton();      // 初始化按钮点击事件和监听器
        loadMessageInfo();        // 初始化数据

        // 初始化信息列表
        messageDetailsItemAdapter = new MessageDetailsItemAdapter(messageItem,messageList, this);
        rv_message_list.setLayoutManager(new LinearLayoutManager(this));
        rv_message_list.setAdapter(messageDetailsItemAdapter);
    }

    private void initViews() {
        tv_name = findViewById(R.id.MessageDetails_ToolbarName);

        rv_message_list = findViewById(R.id.MessageDetails_message_list);
        chatInput = findViewById(R.id.MessageDetails_editText_chat_input);

        // 设置 RecyclerView 的布局管理器
        rv_message_list.setLayoutManager(new LinearLayoutManager(this));

        theme = findViewById(R.id.Theme_MessageDetails);
        user = userItem.getUser();
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
        // 返回
        toolbarButton1 = findViewById(R.id.MessageDetails_ToolbarButton1);
        toolbarButton1.setOnClickListener(v -> {
            finish(); // 关闭当前页面
        });

        // 为头像添加点击事件
        iv_PImage = findViewById(R.id.MessageDetails_ToolbarPImage);
        iv_PImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (messageItem != null) {
                    Long targetUserId = messageItem.getUser_id();
                    Intent intent = new Intent(MessageDetails.this, UserDetails.class);
                    intent.putExtra("userId", targetUserId);
                    startActivity(intent);
                }
            }
        });


        // 关注
        toolbarButton2 = findViewById(R.id.MessageDetails_ToolbarButton2);
        toolbarButton2.setOnClickListener(v -> {
            addFollw();
        });

        // 为发送按钮设置点击事件
        sendButton = findViewById(R.id.MessageDetails_button_chat_send);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = chatInput.getText().toString().trim();
                if (!content.isEmpty()) {
                    sendMessage(content);
                    chatInput.setText("");
                }

            }
        });
    }


    private void sendMessage(String content) {
        // 处理发送消息的逻辑
        Message message = new Message(null,userIng,messageItem.getUser_id(),content,create_time,false);
        // 将新消息添加到本地消息列表
        messageList.add(message);
        // 通知适配器数据已更新
        messageDetailsItemAdapter.notifyItemInserted(messageList.size() - 1);

        // 发送消息到服务器（使用 Retrofit 示例）
        Call<ListTResponse> call = listTService.addMessage(message);
        call.enqueue(new Callback<ListTResponse>() {
            @Override
            public void onResponse(Call<ListTResponse> call, Response<ListTResponse> response) {
                if (response.isSuccessful()) {
                    ListTResponse responseBody = response.body();
                    if (responseBody != null && responseBody.isSuccess()) {
                        Log.d("MessageDetails", "消息发送成功");
                    } else {
                        Log.e("MessageDetails", "消息发送失败");
                    }
                } else {
                    Log.e("MessageDetails", "网络请求失败");
                }
            }

            @Override
            public void onFailure(Call<ListTResponse> call, Throwable t) {
                Log.e("MessageDetails", "请求失败", t);
            }
        });
    }



    // 关注方法
    private void addFollw() {
        // 自己
        if (userIng.equals(messageItem.getUser_id())) {
            Toast.makeText(this, "自己不能关注自己！", Toast.LENGTH_SHORT).show();
            toolbarButton2.setText("自己");
            toolbarButton2.setBackgroundResource(R.drawable.button_background2);
            toolbarButton2.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        } else {
            // 不是自己
            if (messageItem.getFollowIng() == 1) {
                Toast.makeText(this, "取消关注：" + messageItem.getName(), Toast.LENGTH_SHORT).show();
                toolbarButton2.setText("关注");
                toolbarButton2.setBackgroundResource(R.drawable.button_background2);
                toolbarButton2.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);

                Call<ListTResponse> call = listTService.delectFollow(userIng, messageItem.getUser_id());
                call.enqueue(new Callback<ListTResponse>() {
                    @Override
                    public void onResponse(Call<ListTResponse> call, Response<ListTResponse> response) {
                        ResponseBody error = response.errorBody();
                        if (error == null) {
                            ListTResponse likeResponse = response.body();
                            if (likeResponse != null && likeResponse.isSuccess()) {
                                // 更新点赞状态
                                messageItem.setFollowIng(0);
                            }
                        } else {
                            try {
                                Log.e(TAG, "网络请求错误: " + error.string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ListTResponse> call, Throwable t) {
                        Log.e("Register", "网络请求失败: " + t.getMessage());
                        // 恢复关注状态
                        toolbarButton2.setText("已关");
                        toolbarButton2.setBackgroundResource(R.drawable.button_background2);
                        toolbarButton2.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_all3), null, null, null);

                        Toast.makeText(MessageDetails.this, "取消点赞失败，请检查网络连接", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                // 关注
                toolbarButton2.setText("已关");
                toolbarButton2.setBackgroundResource(R.drawable.button_background2);
                toolbarButton2.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_all3), null, null, null);

                Follow follow = new Follow(null, userIng, messageItem.getUser_id(), create_time);
                Call<ListTResponse> call = listTService.addFollow(follow);
                call.enqueue(new Callback<ListTResponse>() {
                    @Override
                    public void onResponse(Call<ListTResponse> call, Response<ListTResponse> response) {
                        ResponseBody error = response.errorBody();
                        if (error == null) {
                            ListTResponse likeResponse = response.body();
                            if (likeResponse != null && likeResponse.isSuccess()) {
                                // 更新关注状态
                                messageItem.setFollowIng(1);
                            }
                        } else {
                            try {
                                Log.e(TAG, "网络请求错误: " + error.string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ListTResponse> call, Throwable t) {
                        Log.e("Register", "网络请求失败: " + t.getMessage());
                        // 恢复关注状态
                        toolbarButton2.setText("关注");
                        toolbarButton2.setBackgroundResource(R.drawable.button_background2);
                        toolbarButton2.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                        Toast.makeText(MessageDetails.this, "点赞失败，请检查网络连接", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    // 加载聊天框信息
    private void loadMessageInfo() {
        messageItem = (MessageItem) getIntent().getParcelableExtra("message_item");
        if (messageItem != null) {
            String imageUrl = "http://10.0.2.2:8080/uploads/";
            // 加载用户头像
            String uImageUrl = imageUrl + messageItem.getPortraitImage();
            Glide.with(this)
                    .load(uImageUrl)
                    .placeholder(R.drawable.jz)
                    .into(iv_PImage);

            tv_name.setText(messageItem.getName());

            // 获取最新的关注状态
            Call<ListTResponse<Integer>> call = listTService.eqtFollowIng(userIng, messageItem.getUser_id());
            call.enqueue(new Callback<ListTResponse<Integer>>() {
                @Override
                public void onResponse(Call<ListTResponse<Integer>> call, Response<ListTResponse<Integer>> response) {
                    if (response.isSuccessful()) {
                        ListTResponse<Integer> listTResponse = response.body();
                        if (listTResponse != null && listTResponse.isSuccess()) {
                            int integer = listTResponse.getList();
                            if (integer ==1) {
                                messageItem.setFollowIng(1);
                                toolbarButton2.setText("已关");
                                toolbarButton2.setBackgroundResource(R.drawable.button_background2);
                                toolbarButton2.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_all3), null, null, null);
                            } else {
                                messageItem.setFollowIng(0);
                                toolbarButton2.setText("关注");
                                toolbarButton2.setBackgroundResource(R.drawable.button_background2);
                                toolbarButton2.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                            }
                        }
                    } else {
                        Log.e("MessageDetails", "获取关注状态失败");
                    }
                }

                @Override
                public void onFailure(Call<ListTResponse<Integer>> call, Throwable t) {
                    Log.e("MessageDetails", "获取关注状态请求失败", t);
                }
            });

            // 获取消息列表
            getMessageList();
        } else {
            Log.e("MessageDetails", "messageItem 为 null，无法加载消息信息");
            // 可以在这里添加一些默认处理逻辑，比如显示提示信息
        }
    }

    public void getMessageList() {

        Long targetUserId = messageItem.getUser_id();
        Call<ListTResponse<List<Message>>> call = listTService.getMessages(userIng, targetUserId);
        call.enqueue(new Callback<ListTResponse<List<Message>>>() {
            @Override
            public void onResponse(Call<ListTResponse<List<Message>>> call, Response<ListTResponse<List<Message>>> response) {
                if (response.isSuccessful()) {
                    ListTResponse<List<Message>> newMessages = response.body();
                    if (newMessages != null) {
                        messageList.clear();
                        List<Message> messageItemArray = newMessages.getList();
                        if (messageItemArray != null) {
                            for (Message message : messageItemArray) {
                                // 打印消息的相关信息，例如消息ID、发送者ID、内容、是否已读等
                                Log.d("MessageDetails", message.toString());
                                if (message.getUserId().equals(userIng)) {
                                    message.setRead(true); // 当前用户发送的消息标记为已读
                                } else {
                                    if (!message.isRead()) {
                                        message.setRead(true);
                                        markMessageAsRead(message.getMessageId()); // 调用标记消息为已读的接口
                                    }
                                }
                                messageList.add(message);
                            }
                        }
                        // 通知适配器数据已更新
                        messageDetailsItemAdapter.notifyDataSetChanged();

                        isLoading = false;
                    } else {
                        Log.e("MessageDetails", "响应体为空");
                    }
                }
            }

            @Override
            public void onFailure(Call<ListTResponse<List<Message>>> call, Throwable t) {
                Log.e("FragmentHomeNoteItem", "请求失败", t);
                isLoading = false;
            }
        });
    }

    private void markMessageAsRead(Long messageId) {


        Call<ListTResponse> call = listTService.markMessageAsRead(messageId);
        call.enqueue(new Callback<ListTResponse>() {
            @Override
            public void onResponse(Call<ListTResponse> call, Response<ListTResponse> response) {
                if (response.isSuccessful()) {
                    ListTResponse responseBody = response.body();
                    if (responseBody != null && responseBody.isSuccess()) {
                        Log.d("MessageDetails", "消息标记为已读成功");


                    } else {
                        Log.e("MessageDetails", "消息标记为已读失败");
                    }
                } else {
                    Log.e("MessageDetails", "网络请求失败");
                }
            }

            @Override
            public void onFailure(Call<ListTResponse> call, Throwable t) {
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // 重新获取聊天信息
        getMessageList();
    }

    // 时间
    private String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }

}