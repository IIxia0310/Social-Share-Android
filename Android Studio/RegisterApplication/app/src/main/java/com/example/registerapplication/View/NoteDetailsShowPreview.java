package com.example.registerapplication.View;

import android.content.DialogInterface;
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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.registerapplication.Adapters.CommentAdapter;
import com.example.registerapplication.Adapters.NoteDetailsAdapter;
import com.example.registerapplication.Entity.Collect;
import com.example.registerapplication.Entity.Comment;
import com.example.registerapplication.Entity.Data.CommentItem;
import com.example.registerapplication.Entity.Data.NoteDetailsItem;
import com.example.registerapplication.Entity.Data.NoteItem;
import com.example.registerapplication.Entity.Data.UserItem;
import com.example.registerapplication.Entity.Follow;
import com.example.registerapplication.Entity.Like;
import com.example.registerapplication.Entity.User;
import com.example.registerapplication.R;
import com.example.registerapplication.Response.ListTResponse;
import com.example.registerapplication.Response.NoteResponse;
import com.example.registerapplication.Service.ListTService;
import com.example.registerapplication.Service.NoteService;

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


/**
 * 笔记详情页
 */
public class NoteDetailsShowPreview extends AppCompatActivity implements CommentAdapter.CommentUpdateListener {

    private static final String TAG = "NoteDetails";
    private ImageView toolbarButton1;  // 返回
    private CircleImageView iv_PImage; // 头像
    private TextView tv_name;           // 姓名
    private Button toolbarButton2;           // 关注

    private ViewPager2 viewPager2;   // 图片
    private LinearLayout dotsLayout;  // 小圆点
    private List<String> imageList;   // 修改为 List<String> 类型
    private NoteDetailsAdapter adapter;  // 适配器
    private TextView tv_title;           // 标题
    private TextView tv_content;           // 内容
    private TextView tv_updateTime;           // 日期
    private TextView tv_comment_sum;           // 共有评论
    private RecyclerView rv_comment_list;   // 评论列表

    private Button bt_BottonComment;           // 底部评论按钮
    private ImageView iv_likeBotton;         // 底部点赞按钮
    private TextView tv_likeSum;         // 底部点赞数
    private ImageView iv_collectBotton;         // 底部收藏按钮
    private TextView tv_collectSum;         // 底部收藏数
    private TextView tv_commentBotton_sum;         // 底部评论数

    private ListTService listTService;
    private NoteService noteService;

    private boolean isLoading = false;
    private CommentAdapter commentAdapter;
    private List<CommentItem> commentItemList;

    private Long userIng = UserItem.getUserItem().getUserlogin(); // 获取当前登录用户 ID
    private String create_time = getCurrentTime();  // 获取当前时间

    private NoteItem noteItem;
    private  NoteDetailsItem noteDetailsItem;
    private LinearLayoutCompat theme;
    private UserItem userItem =UserItem.getUserItem();
    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_details);

        // 初始化 Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        listTService = retrofit.create(ListTService.class);
        noteService = retrofit.create(NoteService.class);

        initViews();        // 初始化界面视图
        initButton();      // 初始化按钮点击事件和监听器
        setupDots();        // 初始化小圆点
        loadNoteDetails();

        commentItemList = new ArrayList<>();
        commentAdapter = new CommentAdapter(commentItemList, listTService, userIng, this);



        rv_comment_list.setLayoutManager(new LinearLayoutManager(this));
        rv_comment_list.setAdapter(commentAdapter);



    }


    private void initViews() {
        // 头部
        iv_PImage = findViewById(R.id.NoteDetails_ToolbarPImage);
        tv_name = findViewById(R.id.NoteDetails_ToolbarName);

        // 中部
        viewPager2 = findViewById(R.id.NoteDetails_ViewPager2);
        dotsLayout = findViewById(R.id.dotsLayout);
        imageList = new ArrayList<>();        // 初始化 List 对象
        tv_title = findViewById(R.id.NoteDetails_title);
        tv_content = findViewById(R.id.NoteDetails_content);
        tv_updateTime = findViewById(R.id.NoteDetails_updateTime);

        // 评论
        tv_comment_sum = findViewById(R.id.NoteDetails_comment_sum);
        rv_comment_list = findViewById(R.id.NoteDetail_comment_list);

        // 底部
        bt_BottonComment = findViewById(R.id.NoteDetails_BottonComment);
        iv_likeBotton = findViewById(R.id.NoteDetails_Bottom_like_button);
        tv_likeSum = findViewById(R.id.NoteDetails_Bottom_like_sum);
        iv_collectBotton = findViewById(R.id.NoteDetails_Bottom_collect_button);
        tv_collectSum = findViewById(R.id.NoteItem_Bottom_collect_sum);
        tv_commentBotton_sum = findViewById(R.id.NoteItem_Bottom_comment_sum);

        theme = findViewById(R.id.Theme_NoteDetails);
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
        toolbarButton1 = findViewById(R.id.NoteDetails_ToolbarButton1);
        toolbarButton1.setOnClickListener(v -> {
            finish(); // 关闭当前页面
        });


        // 为头像添加点击事件
        iv_PImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (noteDetailsItem != null) {
                    Long targetUserId = noteDetailsItem.getUser().getUserId();
                    Intent intent = new Intent(NoteDetailsShowPreview.this, UserDetails.class);
                    intent.putExtra("userId", targetUserId);
                    startActivity(intent);
                }
            }
        });




        // 创建图片的适配器并设置给 ViewPager2
        adapter = new NoteDetailsAdapter(this, imageList);
        viewPager2.setAdapter(adapter);

        // 监听 ViewPager2 页面变化
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                updateDots(position);
            }
        });
    }



    //获取笔记详情
    private void loadNoteDetails() {


        noteItem = (NoteItem) getIntent().getSerializableExtra("note_item");
        Long note_id = noteItem.getNote().getNoteId();
        if (noteItem!=null){
            loadNoteInfo();

        }


    }

    // 加载笔记信息
    private void loadNoteInfo() {


        adapter.notifyDataSetChanged();        // 通知适配器数据发生变化
        setupDots();        // 重新设置小圆点


        String imageUrl = "http://10.0.2.2:8080/uploads/";

        // 加载用户头像
        String uImageUrl = imageUrl + noteItem.getUser().getPortraitImage();
        Glide.with(this)
                .load(uImageUrl)
                .placeholder(R.drawable.jz)
                .into(iv_PImage);
        // 加载笔记图片
        String images[] = noteItem.getNote().getImageUrls().split(",");
        for (String imagesIng : images) {
            imageList.add(imageUrl + imagesIng);
        }

        tv_name.setText(noteItem.getUser().getUsername());
        tv_title.setText(noteItem.getNote().getTitle());
        tv_content.setText(noteItem.getNote().getContent());
        tv_updateTime.setText(noteItem.getNote().getUpdateTime());


    }

    private void setupDots() {
        if (imageList != null) {
            dotsLayout.removeAllViews(); // 清除之前的小圆点
            for (int i = 0; i < imageList.size(); i++) {
                View dot = new View(this);
                dot.setBackgroundResource(R.drawable.dot);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        20, // 宽度
                        20  // 高度
                );
                params.setMargins(8, 0, 8, 0);
                dotsLayout.addView(dot, params);
            }
            updateDots(0);
        }
    }

    private void updateDots(int position) {
        if (dotsLayout != null) {
            for (int i = 0; i < dotsLayout.getChildCount(); i++) {
                View dot = dotsLayout.getChildAt(i);
                if (i == position) {
                    dot.setBackgroundResource(R.drawable.dot_selected);
                } else {
                    dot.setBackgroundResource(R.drawable.dot);
                }
            }
        }
    }

    // 时间
    private String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }


    @Override
    public void onResume() {
        super.onResume();

        loadNoteDetails();

    }



    @Override
    public void onCommentUpdated() {
    }
}