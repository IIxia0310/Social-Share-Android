package com.example.registerapplication.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

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

import com.bumptech.glide.Glide;
import com.example.registerapplication.Adapters.CommentAdapter;
import com.example.registerapplication.Adapters.NoteDetailsAdapter;
import com.example.registerapplication.Entity.Collect;
import com.example.registerapplication.Entity.Comment;
import com.example.registerapplication.Entity.Data.NoteDetailsItem;
import com.example.registerapplication.Entity.Data.NoteHistoryltem;
import com.example.registerapplication.Entity.Data.UserItem;
import com.example.registerapplication.Entity.Follow;
import com.example.registerapplication.Entity.Like;
import com.example.registerapplication.Entity.Data.CommentItem;
import com.example.registerapplication.Entity.Data.NoteItem;
import com.example.registerapplication.Entity.User;
import com.example.registerapplication.R;
import com.example.registerapplication.Response.ListTResponse;
import com.example.registerapplication.Response.NoteResponse;
import com.example.registerapplication.Service.ListTService;
import com.example.registerapplication.Service.NoteService;
import com.example.registerapplication.db.NoteHistoryDao;

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
public class NoteDetails extends AppCompatActivity implements CommentAdapter.CommentUpdateListener {

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


    private NoteHistoryDao historyDao;
    private Long userId; // 当前用户ID
    private Long noteId; // 当前笔记ID


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

//        loadNoteInfo();        // 初始化笔记信息

        // 初始化评论列表
        commentItemList = new ArrayList<>();
        commentAdapter = new CommentAdapter(commentItemList, listTService, userIng, this);



        rv_comment_list.setLayoutManager(new LinearLayoutManager(this));
        rv_comment_list.setAdapter(commentAdapter);

        addPageViews();     //浏览量加1
        addBrowsingHistory();



        // 初始化 DAO
        historyDao = new NoteHistoryDao(this);
        userId = userIng;
        noteId = noteItem.getNote().getNoteId();


        recordBrowseHistory();        // 记录浏览历史


    }

    private void recordBrowseHistory() {
        // 检查是否已浏览过
        boolean hasBrowsed = historyDao.hasBrowsed(userId, noteId);

        if (hasBrowsed) {
            // 如果已浏览过，更新浏览时间
            historyDao.updateBrowseTime(userId, noteId, System.currentTimeMillis());
        } else {
            // 如果未浏览过，添加新记录
            NoteHistoryltem historyItem = new NoteHistoryltem(
                    userId,
                    noteId,
                    System.currentTimeMillis()
            );
            historyDao.addHistory(historyItem);
        }
    }

    //浏览量加1
    private void addPageViews() {
        if (noteItem != null) {
            Call<NoteResponse> call = noteService.addPageViews(noteItem.getNote().getNoteId());
            call.enqueue(new Callback<NoteResponse>() {
                @Override
                public void onResponse(Call<NoteResponse> call, Response<NoteResponse> response) {
                    ResponseBody error = response.errorBody();
                    if (error == null) {
                        NoteResponse noteResponse = response.body();
                        if (noteResponse != null && noteResponse.isSuccess()) {
                        }
                    }
                }
                @Override
                public void onFailure(Call<NoteResponse> call, Throwable t) {
                    Log.e("FragmentHomeNoteItem", "网络请求失败: " + t.getMessage()); // 记录失败日志
                }
            });
        } else {
            Log.e("NoteDetails", "noteItem is null, cannot add page views.");
        }
    }


    //添加浏览记录
    private void addBrowsingHistory() {
        if (noteItem == null) {
            Log.e("NoteDetails", "noteItem is null in addBrowsingHistory");
            return;
        }
        if (userIng == null) {
            Log.e("NoteDetails", "userIng is null in addBrowsingHistory");
            return;
        }

//        Long note_id =  noteItem.getNote().getNoteId();
//        Long likeSum =  noteItem.getLikeSum();
//        int likeIng = noteItem.getLikeIng();
//        Long collectSum =  noteItem.getCollectSum();
//        int cllectIng = noteItem.getCollectIng();
//        int followIng= noteItem.getFollowIng();
////
//
//        Historyltem historyltem = new Historyltem(null,note_id,userIng,likeSum,likeIng,collectSum,cllectIng,followIng,create_time);



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

                    if (targetUserId!=1){
                        Intent intent = new Intent(NoteDetails.this, UserDetails.class);
                        intent.putExtra("userId", targetUserId);
                        startActivity(intent);
                    }

                }
            }
        });



        // 关注
        toolbarButton2 = findViewById(R.id.NoteDetails_ToolbarButton2);
        toolbarButton2.setOnClickListener(v -> {
            addFollw();
        });

        // 点赞
        iv_likeBotton = findViewById(R.id.NoteDetails_Bottom_like_button);
        iv_likeBotton.setOnClickListener(v -> {
            LikeOperation();
        });
        // 收藏
        iv_collectBotton = findViewById(R.id.NoteDetails_Bottom_collect_button);
        iv_collectBotton.setOnClickListener(v -> {
            CollectOperation();
        });

        // 评论
        bt_BottonComment = findViewById(R.id.NoteDetails_BottonComment);
        bt_BottonComment.setOnClickListener(v -> {
            addComment();
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

    // 关注方法
    private void addFollw() {

        //自己
        if(userIng.equals(noteDetailsItem.getNote().getUserId())){

            Toast.makeText(this, "自己不能关注自己！" , Toast.LENGTH_SHORT).show();
            toolbarButton2.setText("自己");
            toolbarButton2.setBackgroundResource(R.drawable.button_background6);
            toolbarButton2.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);

        }else{

            //不是自己
            if (noteDetailsItem.getFollowIng()==1) {

                Toast.makeText(this, "取消关注：" + noteDetailsItem.getNote().getTitle(), Toast.LENGTH_SHORT).show();
                toolbarButton2.setText("关注");
                toolbarButton2.setBackgroundResource(R.drawable.button_background6);
                toolbarButton2.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);

                Call<ListTResponse> call = listTService.delectFollow(userIng, noteDetailsItem.getNote().getUserId());
                call.enqueue(new Callback<ListTResponse>() {
                    @Override
                    public void onResponse(Call<ListTResponse> call, Response<ListTResponse> response) {
                        ResponseBody error = response.errorBody();
                        if (error == null) {
                            ListTResponse likeResponse = response.body();
                            if (likeResponse != null && likeResponse.isSuccess()) {
                                // 更新点赞状态
                                noteDetailsItem.setFollowIng(0);
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
                        toolbarButton2.setBackgroundResource(R.drawable.button_background6);
                        toolbarButton2.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_all3), null, null, null);

                        Toast.makeText(NoteDetails.this, "取消点赞失败，请检查网络连接", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                //关注
                toolbarButton2.setText("已关");

                toolbarButton2.setBackgroundResource(R.drawable.button_background6);
                toolbarButton2.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_all3), null, null, null);


                Follow follow = new Follow(null,userIng,noteDetailsItem.getNote().getUserId(),create_time);
                Call<ListTResponse> call = listTService.addFollow(follow);
                call.enqueue(new Callback<ListTResponse>() {
                    @Override
                    public void onResponse(Call<ListTResponse> call, Response<ListTResponse> response) {
                        ResponseBody error = response.errorBody();
                        if (error == null) {
                            ListTResponse likeResponse = response.body();
                            if (likeResponse != null && likeResponse.isSuccess()) {
                                // 更新关注状态
                                noteDetailsItem.setFollowIng(1);

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
                        toolbarButton2.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                        Toast.makeText(NoteDetails.this, "点赞失败，请检查网络连接", Toast.LENGTH_SHORT).show();
                    }
                });
            }


        }
    }



    // 点赞方法
    private void LikeOperation() {
        if (noteDetailsItem.getLikeIng()==1) {
            // 取消点赞
            iv_likeBotton.setImageResource(R.drawable.ic_like1);
            Toast.makeText(this, "取消点赞：" + noteDetailsItem.getNote().getTitle(), Toast.LENGTH_SHORT).show();
            Call<ListTResponse> call = listTService.delectLike(userIng, noteDetailsItem.getNote().getNoteId());
            call.enqueue(new Callback<ListTResponse>() {
                @Override
                public void onResponse(Call<ListTResponse> call, Response<ListTResponse> response) {
                    ResponseBody error = response.errorBody();
                    if (error == null) {
                        ListTResponse likeResponse = response.body();
                        if (likeResponse != null && likeResponse.isSuccess()) {
                            // 更新点赞状态
                            noteDetailsItem.setLikeIng(0);
                            noteDetailsItem.setLikeSum(noteDetailsItem.getLikeSum() - 1);
                            if (noteDetailsItem.getLikeSum() == 0) {
                                tv_likeSum.setText("点赞");
                            } else {
                                tv_likeSum.setText(noteDetailsItem.getLikeSum().toString());
                            }
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
                    // 恢复点赞状态
                    iv_likeBotton.setImageResource(R.drawable.ic_like2);
                    Toast.makeText(NoteDetails.this, "取消点赞失败，请检查网络连接", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // 点赞
            iv_likeBotton.setImageResource(R.drawable.ic_like2);
            Toast.makeText(this, "点赞：" + noteDetailsItem.getNote().getTitle(), Toast.LENGTH_SHORT).show();
            Like like = new Like(null, userIng, noteDetailsItem.getNote().getNoteId(), null, create_time);
            Call<ListTResponse> call = listTService.addLike(like);
            call.enqueue(new Callback<ListTResponse>() {
                @Override
                public void onResponse(Call<ListTResponse> call, Response<ListTResponse> response) {
                    ResponseBody error = response.errorBody();
                    if (error == null) {
                        ListTResponse likeResponse = response.body();
                        if (likeResponse != null && likeResponse.isSuccess()) {
                            // 更新点赞状态
                            noteDetailsItem.setLikeIng(1);
                            noteDetailsItem.setLikeSum(noteDetailsItem.getLikeSum() + 1);
                            tv_likeSum.setText(noteDetailsItem.getLikeSum().toString());
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
                    // 恢复点赞状态
                    iv_likeBotton.setImageResource(R.drawable.ic_like1);
                    Toast.makeText(NoteDetails.this, "点赞失败，请检查网络连接", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    // 收藏方法
    private void CollectOperation() {
        if (noteDetailsItem.getCollectIng()==1) {
            // 取消收藏
            iv_collectBotton.setImageResource(R.drawable.ic_collect1); // 假设 ic_collect1 是未收藏图标
            Toast.makeText(this, "取消收藏：" + noteDetailsItem.getNote().getTitle(), Toast.LENGTH_SHORT).show();
            Call<ListTResponse> call = listTService.deleteCollect(userIng, noteDetailsItem.getNote().getNoteId());
            call.enqueue(new Callback<ListTResponse>() {
                @Override
                public void onResponse(Call<ListTResponse> call, Response<ListTResponse> response) {
                    ResponseBody error = response.errorBody();
                    if (error == null) {
                        ListTResponse collectResponse = response.body();
                        if (collectResponse != null && collectResponse.isSuccess()) {
                            // 更新收藏状态
                            noteDetailsItem.setCollectIng(0);
                            noteDetailsItem.setCollectSum(noteDetailsItem.getCollectSum() - 1);
                            if (noteDetailsItem.getCollectSum() == 0) {
                                tv_collectSum.setText("收藏");
                            } else {
                                tv_collectSum.setText(noteDetailsItem.getCollectSum().toString());
                            }
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
                    // 恢复收藏状态
                    iv_collectBotton.setImageResource(R.drawable.ic_collect2); // 假设 ic_collect2 是已收藏图标
                    Toast.makeText(NoteDetails.this, "取消收藏失败，请检查网络连接", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // 收藏
            iv_collectBotton.setImageResource(R.drawable.ic_collect2); // 假设 ic_collect2 是已收藏图标
            Toast.makeText(this, "收藏：" + noteDetailsItem.getNote().getTitle(), Toast.LENGTH_SHORT).show();
            Collect collect = new Collect(null, userIng, noteDetailsItem.getNote().getNoteId(), create_time);
            Call<ListTResponse> call = listTService.addCollect(collect);
            call.enqueue(new Callback<ListTResponse>() {
                @Override
                public void onResponse(Call<ListTResponse> call, Response<ListTResponse> response) {
                    ResponseBody error = response.errorBody();
                    if (error == null) {
                        ListTResponse collectResponse = response.body();
                        if (collectResponse != null && collectResponse.isSuccess()) {
                            // 更新收藏状态
                            noteDetailsItem.setCollectIng(1);
                            noteDetailsItem.setCollectSum(noteDetailsItem.getCollectSum() + 1);
                            tv_collectSum.setText(noteDetailsItem.getCollectSum().toString());
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
                    // 恢复收藏状态
                    iv_collectBotton.setImageResource(R.drawable.ic_collect1); // 假设 ic_collect1 是未收藏图标
                    Toast.makeText(NoteDetails.this, "收藏失败，请检查网络连接", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    // 评论方法
    private void addComment() {
        // 创建 AlertDialog.Builder 对象
        AlertDialog.Builder builder = new AlertDialog.Builder(NoteDetails.this);
        builder.setTitle("发表评论");

        // 创建输入框
        final EditText input = new EditText(NoteDetails.this);
        input.setHint("请输入评论内容");
        builder.setView(input);

        // 设置确定按钮
        builder.setPositiveButton("提交", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String content = input.getText().toString().trim();
                if (!content.isEmpty()) {
                    // 处理评论提交逻辑，例如调用接口发送评论
                    Toast.makeText(NoteDetails.this, "你输入的评论是：" + content, Toast.LENGTH_SHORT).show();

                    Comment comment = new Comment(null, userIng, noteDetailsItem.getNote().getNoteId(), content, create_time);

                    Call<ListTResponse> call = listTService.addComment(comment);
                    call.enqueue(new Callback<ListTResponse>() {
                        @Override
                        public void onResponse(Call<ListTResponse> call, Response<ListTResponse> response) {
                            ResponseBody error = response.errorBody();
                            if (error == null) {
                                ListTResponse collectResponse = response.body();
                                if (collectResponse != null && collectResponse.isSuccess()) {
                                    // 更新评论列表
                                    loadCommentData();
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
                        }
                    });

                } else {
                    Toast.makeText(NoteDetails.this, "评论内容不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 设置取消按钮
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // 显示对话框
        builder.show();
    }
    //获取笔记详情
    private void loadNoteDetails() {
        noteItem = (NoteItem) getIntent().getSerializableExtra("note_item");
        noteId = noteItem.getNote().getNoteId();
        Call<ListTResponse<NoteDetailsItem>> call = listTService.eqNoteDetailsItem(userIng,noteId);
        call.enqueue(new Callback<ListTResponse<NoteDetailsItem>>() {
            @Override
            public void onResponse(Call<ListTResponse<NoteDetailsItem>> call, Response<ListTResponse<NoteDetailsItem>> response) {
                if (response.isSuccessful()) {
                    ListTResponse<NoteDetailsItem> likeCountResponse = response.body();
                    if (likeCountResponse != null && likeCountResponse.isSuccess()) {
                        noteDetailsItem = likeCountResponse.getList();
                        if (noteDetailsItem==null){
                            Log.e("NoteDetails", "响应体为空");
                        }else {
                            loadNoteInfo();
                        }
                    }
                }
            }
            @Override
            public void onFailure(Call<ListTResponse<NoteDetailsItem>> call, Throwable t) {
                Log.e("NoteDetails", "笔记详情请求失败", t);
            }
        });
    }

    // NoteDetails类的修改部分
    private void loadNoteInfo() {
        // 确保noteDetailsItem不为空
        if (noteDetailsItem != null) {
            String imageUrl = "http://10.0.2.2:8080/uploads/";

            // 加载用户头像
            String uImageUrl = imageUrl + noteDetailsItem.getUser().getPortraitImage();
            Glide.with(this)
                    .load(uImageUrl)
                    .placeholder(R.drawable.jz)
                    .into(iv_PImage);

            // 加载笔记图片
            String[] images = noteDetailsItem.getNote().getImageUrls().split(",");
            imageList.clear(); // 清空imageList
            for (String img : images) {
                imageList.add(imageUrl + img);
            }

            tv_name.setText(noteDetailsItem.getUser().getUsername());
            tv_title.setText(noteDetailsItem.getNote().getTitle());
            tv_content.setText(noteDetailsItem.getNote().getContent());
            tv_updateTime.setText(noteDetailsItem.getNote().getUpdateTime());

            // 关注
            if (userIng.equals(noteDetailsItem.getNote().getUserId())) {
                toolbarButton2.setText("自己");
                toolbarButton2.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            } else if (noteDetailsItem.getFollowIng() == 1) {
                toolbarButton2.setText("已关");
                toolbarButton2.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_all3), null, null, null);
            } else {
                toolbarButton2.setText("关注");
                toolbarButton2.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            }

            // 点赞
            if (noteItem.getLikeSum() == 0) {
                tv_likeSum.setText("点赞");
            } else {
                tv_likeSum.setText(noteDetailsItem.getLikeSum().toString());
            }
            if (noteItem.getLikeIng() == 1) {
                iv_likeBotton.setImageResource(R.drawable.ic_like2); // 设置为选中状态图标
            } else {
                iv_likeBotton.setImageResource(R.drawable.ic_like1); // 设置为未选中状态图标
            }

            // 收藏
            if (noteDetailsItem.getCollectSum() == 0) {
                tv_collectSum.setText("收藏");
            } else {
                tv_collectSum.setText(noteDetailsItem.getCollectSum().toString());
            }
            if (noteDetailsItem.getCollectIng() == 1) {
                iv_collectBotton.setImageResource(R.drawable.ic_collect2); // 设置为已收藏状态图标
            } else {
                iv_collectBotton.setImageResource(R.drawable.ic_collect1); // 设置为未收藏状态图标
            }

            adapter.notifyDataSetChanged();
            setupDots();
            loadCommentData();
        }
    }
    //获取评论列表
    public void loadCommentData() {
        isLoading = true;
        Long noteIng = noteItem.getNote().getNoteId();
        if (noteIng != null) {
            Call<ListTResponse<List<CommentItem>>> call = listTService.commentItem(noteIng, noteItem.getUser().getUserId(), userIng);
            call.enqueue(new Callback<ListTResponse<List<CommentItem>>>() {
                @Override
                public void onResponse(Call<ListTResponse<List<CommentItem>>> call, Response<ListTResponse<List<CommentItem>>> response) {
                    if (response.isSuccessful()) {
                        ListTResponse<List<CommentItem>> newComments = response.body();
                        if (newComments != null) {
                            commentItemList.clear();
                            List<CommentItem> commentItemArray = newComments.getList();
                            if (commentItemArray != null) {
                                commentItemList.addAll(commentItemArray);
                            }
                            // 通知适配器数据已更新
                            commentAdapter.notifyDataSetChanged();
                            tv_comment_sum.setText("共有 " + commentItemList.size() + " 条评论");
                            // 评论
                            if (commentItemList.size() == 0) { tv_commentBotton_sum.setText("收藏");
                            } else { tv_commentBotton_sum.setText("" + commentItemList.size()); }
                            isLoading = false;
                        } else {
                            Log.e("NoteDetails-commentItem", "响应体为空");
                            isLoading = false;
                        }
                    }
                }

                @Override
                public void onFailure(Call<ListTResponse<List<CommentItem>>> call, Throwable t) {
                    Log.e("NoteDetails", "笔记评论请求失败", t);
                    isLoading = false;
                }
            });
        } else {
            Log.e("NoteDetails-commentItem", "用户id为 null，无法发起请求");
            isLoading = false;
        }
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
        // 重新获取评论信息
        loadCommentData();
    }



    @Override
    public void onCommentUpdated() {
        loadCommentData();
    }
}