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
import com.example.registerapplication.Entity.Data.UserItem;
import com.example.registerapplication.Entity.Like;
import com.example.registerapplication.Entity.Data.CommentItem;
import com.example.registerapplication.Entity.Data.NoteItem;
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
public class NoteDetailsMe extends AppCompatActivity implements CommentAdapter.CommentUpdateListener {

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
    private ImageView iv_likeButton;         // 底部点赞按钮
    private TextView tv_likeSum;         // 底部点赞数
    private ImageView iv_collectButton;         // 底部收藏按钮
    private TextView tv_collectSum;         // 底部收藏数

    private ImageView iv_commentButton;         // 底部收藏按钮
    private TextView tv_commentSum;         // 底部收藏数




    private ListTService listTService;
    private NoteService noteService;

    private boolean isLoading = false;
    private CommentAdapter commentAdapter;
    private List<CommentItem> commentItemList;

    private Long userIng = UserItem.getUserItem().getUserlogin(); // 获取当前登录用户 ID
    private String create_time = getCurrentTime();  // 获取当前时间

    private NoteItem noteItem;
    private NoteDetailsItem noteDetailsItem;


    private LinearLayoutCompat theme;
    private UserItem userItem =UserItem.getUserItem();
    private User user;
//
//    // 在 NoteDetails 类中添加一个内部类来处理数据库操作
//    private class AddBrowsingHistoryTask extends AsyncTask<Void, Void, Void> {
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            if (noteItem != null && userIng != null) {
//                // 添加历史记录
//                HistoryDbHelper historyDbHelper = new HistoryDbHelper(NoteDetails.this);
//                SQLiteDatabase db = historyDbHelper.getWritableDatabase();
//                historyDbHelper.addNoteItemToHistory(db, noteItem, userIng);
//            }
//            return null;
//        }
//    }

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

//        loadNoteInfo();        // 初始化用户信息（示例：模拟网络请求获取数据，实际需调用接口）

        // 初始化评论列表
        commentItemList = new ArrayList<>();
        commentAdapter = new CommentAdapter(commentItemList, listTService, userIng, this);
        rv_comment_list.setLayoutManager(new LinearLayoutManager(this));
        rv_comment_list.setAdapter(commentAdapter);

        addPageViews();     //浏览量加1
        addBrowsingHistory();

//        if (noteItem != null) {
//            // 在子线程中添加浏览历史记录
//            new AddBrowsingHistoryTask().execute();
//        }
    }

    //浏览量加1
    private void addPageViews() {
        if (noteItem != null&&noteItem.getNote().getVisibility()!=2) { //私密笔记不可添加浏览量
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

        Long note_id =  noteItem.getNote().getNoteId();
        Long likeSum =  noteItem.getLikeSum();
        int likeIng = noteItem.getLikeIng();
//        Long collectSum =  noteItem.getCollectSum();
//        int cllectIng = noteItem.getCollectIng();
//        int followIng= noteItem.getFollowIng();
//
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
//        bt_BottonComment = findViewById(R.id.NoteDetails_BottonComment);
        tv_likeSum = findViewById(R.id.NoteDetails_Bottom_like_sum);
        tv_collectSum = findViewById(R.id.NoteItem_Bottom_collect_sum);
        tv_commentSum = findViewById(R.id.NoteItem_Bottom_comment_sum);

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


        // 删除作品
        toolbarButton2 = findViewById(R.id.NoteDetails_ToolbarButton2);
        toolbarButton2.setOnClickListener(v -> {
            showDeleteConfirmationDialog();
        });

        // 编辑作品
        bt_BottonComment = findViewById(R.id.NoteDetails_BottonComment);
        bt_BottonComment.setOnClickListener(v -> {
            revampNote();
        });


        // 点赞
        iv_likeButton = findViewById(R.id.NoteDetails_Bottom_like_button);
        iv_likeButton.setOnClickListener(v -> {
            LikeOperation();
        });
        // 收藏
        iv_collectButton = findViewById(R.id.NoteDetails_Bottom_collect_button);
        iv_collectButton.setOnClickListener(v -> {
            CollectOperation();
        });

        //评论
        iv_commentButton = findViewById(R.id.NoteDetails_Bottom_comment_button);
        iv_commentButton.setOnClickListener(v -> {
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


    //编辑笔记方法
    private void revampNote() {
        Intent intent1 = new Intent(this, RevampNote.class);
        intent1.putExtra("note_item", noteItem); // 将 noteItem 作为参数传递
        startActivity(intent1);
    }


    //删除笔记对话框方法
    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("确认删除")
                .setMessage("确定要删除笔记：" + noteItem.getNote().getTitle() + " 吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        delectNote();
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }
    //删除笔记方法
    private void delectNote() {
        // 检查 noteItem 是否为空
        if (noteItem == null || noteItem.getNote() == null || noteItem.getNote().getNoteId() == null) {
            Log.e(TAG, "noteItem or noteId is null, cannot delete note.");
            Toast.makeText(this, "笔记信息缺失，无法删除", Toast.LENGTH_SHORT).show();
            return;
        }

        // 获取笔记 ID
        Long noteId = noteItem.getNote().getNoteId();

        // 调用网络接口删除笔记
        Call<NoteResponse> call = noteService.deleteNote(noteId);
        call.enqueue(new Callback<NoteResponse>() {
            @Override
            public void onResponse(Call<NoteResponse> call, Response<NoteResponse> response) {
                ResponseBody error = response.errorBody();
                if (error == null) {
                    NoteResponse deleteResponse = response.body();
                    if (deleteResponse != null && deleteResponse.isSuccess()) {
                        // 删除成功，给用户提示并关闭当前页面
                        Toast.makeText(NoteDetailsMe.this, "笔记删除成功", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        // 删除失败，给用户提示
                        Toast.makeText(NoteDetailsMe.this, "笔记删除失败", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    try {
                        // 网络请求错误，记录日志并给用户提示
                        Log.e(TAG, "网络请求错误: " + error.string());
                        Toast.makeText(NoteDetailsMe.this, "网络请求错误，删除失败", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<NoteResponse> call, Throwable t) {
                // 网络请求失败，记录日志并给用户提示
                Log.e(TAG, "网络请求失败: " + t.getMessage());
                Toast.makeText(NoteDetailsMe.this, "网络请求失败，删除失败", Toast.LENGTH_SHORT).show();
            }
        });



    }

    // 点赞方法
    private void LikeOperation() {
        if (noteItem.getLikeIng()==1) {
            // 取消点赞
            iv_likeButton.setImageResource(R.drawable.ic_like1);
            Toast.makeText(this, "取消点赞：" + noteItem.getNote().getTitle(), Toast.LENGTH_SHORT).show();
            Call<ListTResponse> call = listTService.delectLike(userIng, noteItem.getNote().getNoteId());
            call.enqueue(new Callback<ListTResponse>() {
                @Override
                public void onResponse(Call<ListTResponse> call, Response<ListTResponse> response) {
                    ResponseBody error = response.errorBody();
                    if (error == null) {
                        ListTResponse likeResponse = response.body();
                        if (likeResponse != null && likeResponse.isSuccess()) {
                            // 更新点赞状态
                            noteItem.setLikeIng(0);
                            noteItem.setLikeSum(noteItem.getLikeSum() - 1);
                            if (noteItem.getLikeSum() == 0) {
                                tv_likeSum.setText("点赞");
                            } else {
                                tv_likeSum.setText(noteItem.getLikeSum().toString());
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
                    iv_likeButton.setImageResource(R.drawable.ic_like2);
                    Toast.makeText(NoteDetailsMe.this, "取消点赞失败，请检查网络连接", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // 点赞
            iv_likeButton.setImageResource(R.drawable.ic_like2);
            Toast.makeText(this, "点赞：" + noteItem.getNote().getTitle(), Toast.LENGTH_SHORT).show();
            Like like = new Like(null, userIng, noteItem.getNote().getNoteId(), null, create_time);
            Call<ListTResponse> call = listTService.addLike(like);
            call.enqueue(new Callback<ListTResponse>() {
                @Override
                public void onResponse(Call<ListTResponse> call, Response<ListTResponse> response) {
                    ResponseBody error = response.errorBody();
                    if (error == null) {
                        ListTResponse likeResponse = response.body();
                        if (likeResponse != null && likeResponse.isSuccess()) {
                            // 更新点赞状态
                            noteItem.setLikeIng(1);
                            noteItem.setLikeSum(noteItem.getLikeSum() + 1);
                            tv_likeSum.setText(noteItem.getLikeSum().toString());
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
                    iv_likeButton.setImageResource(R.drawable.ic_like1);
                    Toast.makeText(NoteDetailsMe.this, "点赞失败，请检查网络连接", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    // 收藏方法
    private void CollectOperation() {
        if (noteDetailsItem.getCollectIng()==1) {
            // 取消收藏
            iv_collectButton.setImageResource(R.drawable.ic_collect1); // 假设 ic_collect1 是未收藏图标
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
                    iv_collectButton.setImageResource(R.drawable.ic_collect2); // 假设 ic_collect2 是已收藏图标
                    Toast.makeText(NoteDetailsMe.this, "取消收藏失败，请检查网络连接", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // 收藏
            iv_collectButton.setImageResource(R.drawable.ic_collect2); // 假设 ic_collect2 是已收藏图标
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
                    iv_collectButton.setImageResource(R.drawable.ic_collect1); // 假设 ic_collect1 是未收藏图标
                    Toast.makeText(NoteDetailsMe.this, "收藏失败，请检查网络连接", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    // 评论方法
    private void addComment() {
        // 创建 AlertDialog.Builder 对象
        AlertDialog.Builder builder = new AlertDialog.Builder(NoteDetailsMe.this);
        builder.setTitle("发表评论");

        // 创建输入框
        final EditText input = new EditText(NoteDetailsMe.this);
        input.setHint("请输入评论内容");
        builder.setView(input);

        // 设置确定按钮
        builder.setPositiveButton("提交", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String content = input.getText().toString().trim();
                if (!content.isEmpty()) {
                    // 处理评论提交逻辑，例如调用接口发送评论
                    Toast.makeText(NoteDetailsMe.this, "你输入的评论是：" + content, Toast.LENGTH_SHORT).show();

                    Comment comment = new Comment(null, userIng, noteItem.getNote().getNoteId(), content, create_time);

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
                    Toast.makeText(NoteDetailsMe.this, "评论内容不能为空", Toast.LENGTH_SHORT).show();
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
        Long note_id = noteItem.getNote().getNoteId();

        Log.e("NoteDetailsMe", "note_id"+note_id);


        Call<ListTResponse<NoteDetailsItem>> call = listTService.eqNoteDetailsItem(userIng,note_id);
        call.enqueue(new Callback<ListTResponse<NoteDetailsItem>>() {
            @Override
            public void onResponse(Call<ListTResponse<NoteDetailsItem>> call, Response<ListTResponse<NoteDetailsItem>> response) {
                if (response.isSuccessful()) {
                    ListTResponse<NoteDetailsItem> likeCountResponse = response.body();
                    if (likeCountResponse != null && likeCountResponse.isSuccess()) {
                        noteDetailsItem = likeCountResponse.getList();

                        if (noteDetailsItem==null){
                            Log.e("NoteDetailsMe", "获取笔记详情响应体为空");

                        }else {
                            loadNoteInfo();
                        }

                    }
                } else {
                    Log.e("NoteDetailsMe", "响应体为空");
                }
            }

            @Override
            public void onFailure(Call<ListTResponse<NoteDetailsItem>> call, Throwable t) {
                Log.e("NoteDetails", "笔记详情请求失败", t);
            }
        });

    }


    // 加载用户信息
    private void loadNoteInfo() {


//        noteItem = (NoteItem) getIntent().getSerializableExtra("note_item");
        String imageUrl = "http://10.0.2.2:8080/uploads/";

        // 加载用户头像
        String uImageUrl = imageUrl + noteDetailsItem.getUser().getPortraitImage();
        Glide.with(this)
                .load(uImageUrl)
                .placeholder(R.drawable.jz)
                .into(iv_PImage);
        // 加载笔记图片
        String images[] = noteDetailsItem.getNote().getImageUrls().split(",");
        for (String imagesIng : images) {
            imageList.add(imageUrl + imagesIng);
        }

        tv_name.setText(noteDetailsItem.getUser().getUsername());
        tv_title.setText(noteDetailsItem.getNote().getTitle());
        tv_content.setText(noteDetailsItem.getNote().getContent());
        tv_updateTime.setText(noteDetailsItem.getNote().getUpdateTime());

        toolbarButton2.setText("删除作品");
        toolbarButton2.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);

        bt_BottonComment.setText("编辑作品");
        bt_BottonComment.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);


//        //关注
//        if(userIng.equals(noteItem.getNote().getUserId())){
//
//            toolbarButton2.setText("自己");
//            toolbarButton2.setBackgroundResource(R.drawable.button_background2);
//            toolbarButton2.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
//
//
//        }else if (noteItem.getFollowIng()==1) {
//            toolbarButton2.setText("已关");
//            toolbarButton2.setBackgroundResource(R.drawable.button_background2);
//            toolbarButton2.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_all3), null, null, null);
//
//        } else {
//            toolbarButton2.setText("关注");
//            toolbarButton2.setBackgroundResource(R.drawable.button_background2);
//            toolbarButton2.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
//
//        }




        // 点赞
        if (noteDetailsItem.getLikeSum() == 0) {
            tv_likeSum.setText("点赞");
        } else {
            tv_likeSum.setText(noteDetailsItem.getLikeSum().toString());
        }
        if (noteDetailsItem.getLikeIng()==1) {
            iv_likeButton.setImageResource(R.drawable.ic_like2); // 设置为选中状态图标
        } else {
            iv_likeButton.setImageResource(R.drawable.ic_like1); // 设置为未选中状态图标
        }

        // 收藏
        if (noteDetailsItem.getCollectSum() == 0) {
            tv_collectSum.setText("收藏");
        } else {
            tv_collectSum.setText(noteDetailsItem.getCollectSum().toString());
        }
        if (noteDetailsItem.getCollectIng()==1) {
            iv_collectButton.setImageResource(R.drawable.ic_collect2); // 设置为已收藏状态图标
        } else {
            iv_collectButton.setImageResource(R.drawable.ic_collect1); // 设置为未收藏状态图标
        }

        adapter.notifyDataSetChanged();        // 通知适配器数据发生变化
        setupDots();        // 重新设置小圆点
        loadCommentData();
    }

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
                            if (commentItemList.size() == 0) {
                                tv_commentSum.setText("评论");
                            } else {
                                tv_commentSum.setText("" + commentItemList.size());
                            }

                            isLoading = false;
                        } else {
                            Log.e("NoteDetailsMe", "响应体为空");
                            isLoading = false;
                        }
                    }
                }

                @Override
                public void onFailure(Call<ListTResponse<List<CommentItem>>> call, Throwable t) {
                    Log.e("NoteDetailsMe", "请求失败", t);
                    isLoading = false;
                }
            });
        } else {
            Log.e("FragmentHomeNoteItem", "用户兴趣数组为 null，无法发起请求");
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
        // 重新获取评论信息
        loadNoteDetails();
        loadCommentData();
    }



    @Override
    public void onCommentUpdated() {
        loadCommentData();
    }
}