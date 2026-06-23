package com.example.registerapplication.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.registerapplication.Adapters.MessageListItemAdapter;
import com.example.registerapplication.Adapters.Side2CommentListItemAdapter;
import com.example.registerapplication.Entity.Data.MessageItem;
import com.example.registerapplication.Entity.Data.SideCommentItem;
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

/**
 * 我的评论
 */
public class Side2Comment extends AppCompatActivity {


    private static final String TAG = "Side2Comment";
    private Side2CommentListItemAdapter adapter;
    private ListTService listTService;

    private boolean isLoading = false;

    private RecyclerView recyclerView;
    private List<SideCommentItem> sideCommentItems;



    private LinearLayoutCompat theme;
    private UserItem userItem =UserItem.getUserItem();
    private User user =userItem.getUser();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.side2_comment);

        // 创建 Retrofit 实例
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/") // 替换为实际的服务器地址
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // 创建 MessageService 实例
        listTService = retrofit.create(ListTService.class);



        initToolbar();
        initViews(); // 先初始化视图
        initSideComment();
    }


    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.Toolbar_Side2Comment);
        toolbar.setTitle("我的评论");
        toolbar.setNavigationIcon(R.drawable.toolbar_2);
        toolbar.setNavigationOnClickListener(v -> finish());
    }


    private void  initViews(){
        theme = findViewById(R.id.Theme_Side2Comment);
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

        recyclerView = findViewById(R.id.Side2Comment_list);
        // 设置线性布局管理器
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        // 初始化适配器
        sideCommentItems = new ArrayList<>();
        adapter = new Side2CommentListItemAdapter(sideCommentItems);
        recyclerView.setAdapter(adapter);


        adapter.setOnSide2CommentItemClickListener(new Side2CommentListItemAdapter.OnSide2CommentClickListener() {
            @Override
            public void onOnSide2CommentItemClick(SideCommentItem sideCommentItem) {


                if (sideCommentItem.getNoteItem().getNote()!=null){

                    //点击了定位，打开笔记
                    Intent intent1 = new Intent(Side2Comment.this, NoteDetails.class);
                    intent1.putExtra("note_item", sideCommentItem.getNoteItem());
                    startActivity(intent1);


                }else {
                    Toast.makeText(Side2Comment.this, "作品已不可见", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    private void initSideComment() {

        User userIng = UserItem.getUserItem().getUser();

        // 发起网络请求
        Call<ListTResponse<List<SideCommentItem>>> call = listTService.side2Comment(userIng.getUserId());
        call.enqueue(new Callback<ListTResponse<List<SideCommentItem>>>() {
            @Override
            public void onResponse(Call<ListTResponse<List<SideCommentItem>>> call, Response<ListTResponse<List<SideCommentItem>>> response) {
                if (response.isSuccessful()) {
                    ListTResponse<List<SideCommentItem>> newNotes = response.body();
                    if (newNotes != null) {
                        List<SideCommentItem> SideCommentItem = newNotes.getList();
                        if (SideCommentItem != null &&!SideCommentItem.isEmpty()) {
                            sideCommentItems.clear();
                            sideCommentItems.addAll(SideCommentItem);

                            // 打印获取到的数据
                            Log.d("Side2Comment", "获取到的消息列表数据：");
                            for (SideCommentItem item : SideCommentItem) {
                                Log.d("Side2Comment", item.toString());
                            }

                            // 通知适配器数据已更新
                            adapter.notifyDataSetChanged();
                        } else {
                            // 数据为空，清空适配器
                            sideCommentItems.clear();
                            adapter.notifyDataSetChanged();
                        }
                        isLoading = false;
                    } else {
                        Log.e("Side2Comment", "响应体为空");
                        // 响应体为空，清空适配器
                        sideCommentItems.clear();
                        adapter.notifyDataSetChanged();
                        isLoading = false;
                    }
                }
            }

            @Override
            public void onFailure(Call<ListTResponse<List<SideCommentItem>>> call, Throwable t) {
                Log.e("Side2Comment", "请求失败", t);
                // 请求失败，清空适配器
                sideCommentItems.clear();
                adapter.notifyDataSetChanged();
                isLoading = false;
            }
        });



    }


}