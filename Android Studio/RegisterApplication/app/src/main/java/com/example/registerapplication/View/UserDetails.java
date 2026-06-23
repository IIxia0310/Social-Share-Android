package com.example.registerapplication.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.registerapplication.Adapters.NoteListItemAdapter;
import com.example.registerapplication.Entity.Data.MessageItem;
import com.example.registerapplication.Entity.Data.NoteItem;
import com.example.registerapplication.Entity.Data.UserItem;
import com.example.registerapplication.Entity.Follow;
import com.example.registerapplication.Entity.Like;
import com.example.registerapplication.Fragments.FragmentMe;
import com.example.registerapplication.R;
import com.example.registerapplication.Response.ListTResponse;
import com.example.registerapplication.Response.UserResponse;
import com.example.registerapplication.Service.ListTService;
import com.example.registerapplication.Service.UserService;
import com.example.registerapplication.Entity.User;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import lombok.NonNull;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserDetails extends AppCompatActivity {
    private RecyclerView NoteList;
    private NoteListItemAdapter adapter;

    private LinearLayout theme;
    private ImageView toolbarButton1;  // 返回
    private TextView tv_name;           // 姓名
    private Button toolbarButton2;           // 关注
    private Button toolbarButton3;           // 关注

    private LinearLayout user_Bimage; // 背景容器（LinearLayout）
    private ImageView user_PImage;       // 头像（ImageView）
    private TextView userNameTextView;
    private TextView userIdTextView;
    private TextView userLocationTextView;
    private TextView userSignatureTextView;
    private TextView userFollowTextView;
    private TextView userFanTextView;
    private TextView userLikeTextView;
    private TextView userNoteSumTextView;
    private UserService userService;
    private ListTService listTService;
    private ImageDownloadTask currentImageDownloadTask;
    private User user;
    private String create_time = getCurrentTime();  // 获取当前时间

    private UserItem userItem = UserItem.getUserItem();
    private Long userIng = UserItem.getUserItem().getUserlogin();
    private Integer followIng;

    private MessageItem messageItem;
    private boolean isLoading = false;

    private RecyclerView noteRecyclerView;
    private NoteListItemAdapter noteAdapter;
    private List<NoteItem> noteList = new ArrayList<>();

    int integer ;
    Long userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_details);

        // 初始化 Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        userService = retrofit.create(UserService.class);
        listTService = retrofit.create(ListTService.class);

        // 初始化视图
        initViews();
        initButton();
        init();

        // 接收传递过来的 userId
         userId = getIntent().getLongExtra("userId", -1);
        if (userId != -1) {
            // 根据 userId 加载用户详情
            loadUserDetails(userId);
            loadUserNotes();
        } else {
            Toast.makeText(this, "未获取到有效的用户 ID", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void initViews() {

        tv_name = findViewById(R.id.UserDetails_ToolbarName);

        user_Bimage = findViewById(R.id.UserDetails_Bimage);
        user_PImage = findViewById(R.id.UserDetails_Pimage);
        userNameTextView = findViewById(R.id.UserDetails_name);
        userIdTextView = findViewById(R.id.UserDetails_id);
        userLocationTextView = findViewById(R.id.UserDetails_location);
        userSignatureTextView = findViewById(R.id.me_user_signature);
        userFollowTextView = findViewById(R.id.UserDetails_follow);
        userFanTextView = findViewById(R.id.UserDetails_fan);
        userLikeTextView = findViewById(R.id.UserDetails_like);
        userNoteSumTextView = findViewById(R.id.UserDetails_noteSum);

        theme = findViewById(R.id.Theme_UserDetails);
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
        toolbarButton1 = findViewById(R.id.UserDetails_ToolbarButton1);
        toolbarButton1.setOnClickListener(v -> {
            finish(); // 关闭当前页面
        });

        // 关注
        toolbarButton2 = findViewById(R.id.UserDetails_ToolbarButton2);
        toolbarButton2.setOnClickListener(v -> {
            addFollw();
        });

        // 去私信
        toolbarButton3 = findViewById(R.id.UserDetails_Message_button);
        toolbarButton3.setOnClickListener(v -> {
            // 传递数据
            messageItem = new MessageItem(user.getPortraitImage(),user.getUsername(),user.getUserId(),
                    integer,null,null,null);
            Intent intent1 = new Intent(UserDetails.this, MessageDetails.class);
            intent1.putExtra("message_item", messageItem);
            startActivity(intent1);
        });



    }


    public void init( ) {
        // 初始化RecyclerView
        NoteList = findViewById(R.id.NoteList);
        // 设置2列瀑布流布局
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(
                2,
                StaggeredGridLayoutManager.VERTICAL
        );
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        NoteList.setLayoutManager(layoutManager);

        // 初始化适配器
        noteList = new ArrayList<>();
        adapter = new NoteListItemAdapter(noteList);
        NoteList.setAdapter(adapter);

        // 设置点击事件监听器
        adapter.setOnNoteItemClickListener(new NoteListItemAdapter.OnNoteItemClickListener() {
            @Override
            public void onNoteItemClick(NoteItem noteItem) {
                // 处理点击事件，例如跳转到笔记详情页
                Toast.makeText(UserDetails.this, "点击了笔记：" + noteItem.getNote().getTitle(), Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(UserDetails.this, NoteDetails.class);
                intent1.putExtra("note_item", noteItem);
                startActivity(intent1);
            }

            @Override
            public void onLikeButtonClick(NoteItem noteItem) {
                // 点赞处理
                int position = noteList.indexOf(noteItem);
                NoteListItemAdapter.NoteViewHolder holder = (NoteListItemAdapter.NoteViewHolder) NoteList.findViewHolderForAdapterPosition(position);
                if (holder != null) {
                    if (noteItem.getLikeIng()==1) {
                        // 有则删除
                        Toast.makeText(UserDetails.this, "取消点赞：" + noteItem.getNote().getTitle(), Toast.LENGTH_SHORT).show();
                        Call<ListTResponse> call = listTService.delectLike(userIng, noteItem.getNote().getNoteId());
                        call.enqueue(new Callback<ListTResponse>() {
                            @Override
                            public void onResponse(Call<ListTResponse> call, Response<ListTResponse> response) {
                                ResponseBody error = response.errorBody();
                                if (error == null) {
                                    ListTResponse likeResponse = response.body();
                                    if (likeResponse != null && likeResponse.isSuccess()) {
                                        loadUserNotes();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<ListTResponse> call, Throwable t) {
                                Log.e("Register", "网络请求失败: " + t.getMessage()); // 记录失败日志
                            }
                        });
                    } else {
                        // 添加
                        Toast.makeText(UserDetails.this, "点赞：" + noteItem.getNote().getTitle(), Toast.LENGTH_SHORT).show();
                        Like like = new Like(null, userIng, noteItem.getNote().getNoteId(), null, create_time);
                        Call<ListTResponse> call = listTService.addLike(like);
                        call.enqueue(new Callback<ListTResponse>() {
                            @Override
                            public void onResponse(Call<ListTResponse> call, Response<ListTResponse> response) {
                                ResponseBody error = response.errorBody();
                                if (error == null) {
                                    ListTResponse likeResponse = response.body();
                                    if (likeResponse != null && likeResponse.isSuccess()) {
                                        loadUserNotes();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<ListTResponse> call, Throwable t) {
                                Log.e("Register", "网络请求失败: " + t.getMessage()); // 记录失败日志
                            }
                        });
                    }
                }
            }
        });

        // 监听RecyclerView滚动事件
        NoteList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) recyclerView.getLayoutManager();
                int[] into = new int[2];
                layoutManager.findLastVisibleItemPositions(into);
            }
        });



    }

    private void loadUserDetails(Long userId) {
        Call<UserResponse> call = userService.eqUserID(userId);
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                ResponseBody error = response.errorBody();
                if (error == null) {
                    UserResponse userResponse = response.body();
                    if (userResponse != null && userResponse.isSuccess()) {
                        user = userResponse.getUser();

                        String imageUrl = "http://10.0.2.2:8080/uploads/";

                        // 下载背景图片
                        String userIngBackgroundImage = user.getBackgroundImage();
                        String BackgroundImageUrl = imageUrl + userIngBackgroundImage;
                        currentImageDownloadTask = new ImageDownloadTask(ImageType.BACKGROUND);
                        currentImageDownloadTask.execute(BackgroundImageUrl);

                        // 下载头像图片
                        String userIngPortraitImage = user.getPortraitImage();
                        String PortraitImageUrl = imageUrl + userIngPortraitImage;
                        currentImageDownloadTask = new ImageDownloadTask(ImageType.PORTRAIT);
                        currentImageDownloadTask.execute(PortraitImageUrl);

                        // 设置用户信息到对应的 TextView

                        tv_name.setText(user.getUsername() + "的资料");
                        userNameTextView.setText(user.getUsername());
                        userIdTextView.setText(String.valueOf(user.getUserId()));
                        userLocationTextView.setText(user.getLocation());
                        userSignatureTextView.setText(user.getSignature());
                        userFollowTextView.setText(String.valueOf(userResponse.getFollowSum()));
                        userFanTextView.setText(String.valueOf(userResponse.getFanSum()));
                        userLikeTextView.setText(String.valueOf(userResponse.getLikeSum()));
                        userNoteSumTextView.setText("Ta的作品  "+userResponse.getNoteSum()+"个");

                        // 获取最新的关注状态
                        Call<ListTResponse<Integer>> call1 = listTService.eqtFollowIng(userIng, userId);
                        call1.enqueue(new Callback<ListTResponse<Integer>>() {
                            @Override
                            public void onResponse(Call<ListTResponse<Integer>> call, Response<ListTResponse<Integer>> response) {
                                if (response.isSuccessful()) {
                                    ListTResponse<Integer> listTResponse = response.body();
                                    if (listTResponse != null && listTResponse.isSuccess()) {
                                        integer = listTResponse.getList();
                                        if (integer == 1) {
                                            followIng = 1;
                                            toolbarButton2.setText("已关");
                                            toolbarButton2.setBackgroundResource(R.drawable.button_background6);
                                            toolbarButton2.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_all3), null, null, null);
                                        } else {
                                            followIng = 0;
                                            toolbarButton2.setText("关注");
                                            toolbarButton2.setBackgroundResource(R.drawable.button_background6);
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
                    }
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Log.e("Login", "网络请求失败: " + t.getMessage()); // 记录失败日志
            }
        });
    }

    private void loadUserNotes() {


        String currentTitle="公开";
        Call<ListTResponse<List<NoteItem>>> call = listTService.me1NoteItems(currentTitle, userId);
        call.enqueue(new Callback<ListTResponse<List<NoteItem>>>() {
            @Override
            public void onResponse(Call<ListTResponse<List<NoteItem>>> call, Response<ListTResponse<List<NoteItem>>> response) {
                if (response.isSuccessful()) {
                    ListTResponse<List<NoteItem>> newNotes = response.body();
                    if (newNotes != null) {
                        List<NoteItem> noteItemsArray = newNotes.getList();
                        if (noteItemsArray != null && !noteItemsArray.isEmpty()) {
                            noteList.clear();
                            noteList.addAll(noteItemsArray);
                            // 通知适配器数据已更新
                            adapter.notifyDataSetChanged();
                        } else {
                            // 数据为空，清空适配器
                            noteList.clear();
                            adapter.notifyDataSetChanged();
                        }
                        isLoading = false;
                    } else {
                        Log.e("FragmentHomeNoteItem", "响应体为空");
                        // 响应体为空，清空适配器
                        noteList.clear();
                        adapter.notifyDataSetChanged();
                        isLoading = false;
                    }
                } else {
                    Toast.makeText(UserDetails.this, "获取笔记列表失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ListTResponse<List<NoteItem>>> call, Throwable t) {
                Log.e("UserDetails", "获取笔记列表请求失败: " + t.getMessage());
                Toast.makeText(UserDetails.this, "网络请求失败，请检查网络连接", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 关注方法
    private void addFollw() {
        // 自己
        if (userIng.equals(user.getUserId())) {
            Toast.makeText(this, "自己不能关注自己！", Toast.LENGTH_SHORT).show();
            toolbarButton2.setText("自己");
            toolbarButton2.setBackgroundResource(R.drawable.button_background6);
            toolbarButton2.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        } else {
            // 不是自己
            if (followIng == 1) {
                Toast.makeText(this, "取消关注：" + user.getUsername(), Toast.LENGTH_SHORT).show();
                toolbarButton2.setText("关注");
                toolbarButton2.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);

                Call<ListTResponse> call = listTService.delectFollow(userIng, user.getUserId());
                call.enqueue(new Callback<ListTResponse>() {
                    @Override
                    public void onResponse(Call<ListTResponse> call, Response<ListTResponse> response) {
                        ResponseBody error = response.errorBody();
                        if (error == null) {
                            ListTResponse likeResponse = response.body();
                            if (likeResponse != null && likeResponse.isSuccess()) {
                                // 更新关注状态
                                followIng = 0;
                            }
                        } else {
                            try {
                                Log.e("UserDetails", "网络请求错误: " + error.string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ListTResponse> call, Throwable t) {
                        Log.e("UserDetails", "网络请求失败: " + t.getMessage());
                        // 恢复关注状态
                        toolbarButton2.setText("已关");
                        toolbarButton2.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_all3), null, null, null);
                        Toast.makeText(UserDetails.this, "取消关注失败，请检查网络连接", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                // 关注
                toolbarButton2.setText("已关");
                toolbarButton2.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_all3), null, null, null);

                Follow follow = new Follow(null, userIng, user.getUserId(), create_time);
                Call<ListTResponse> call = listTService.addFollow(follow);
                call.enqueue(new Callback<ListTResponse>() {
                    @Override
                    public void onResponse(Call<ListTResponse> call, Response<ListTResponse> response) {
                        ResponseBody error = response.errorBody();
                        if (error == null) {
                            ListTResponse likeResponse = response.body();
                            if (likeResponse != null && likeResponse.isSuccess()) {
                                // 更新关注状态
                                followIng = 1;
                            }
                        } else {
                            try {
                                Log.e("UserDetails", "网络请求错误: " + error.string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ListTResponse> call, Throwable t) {
                        Log.e("UserDetails", "网络请求失败: " + t.getMessage());
                        // 恢复关注状态
                        toolbarButton2.setText("关注");
                        toolbarButton2.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                        Toast.makeText(UserDetails.this, "关注失败，请检查网络连接", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    private enum ImageType {
        PORTRAIT,
        BACKGROUND
    }

    private class ImageDownloadTask extends AsyncTask<String, Void, Bitmap> {
        private ImageType imageType;

        public ImageDownloadTask(ImageType imageType) {
            this.imageType = imageType;
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            String url = urls[0];
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            try (okhttp3.Response response = client.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    InputStream inputStream = response.body().byteStream();
                    return BitmapFactory.decodeStream(inputStream);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                switch (imageType) {
                    case PORTRAIT:
                        user_PImage.setImageBitmap(bitmap);
                        break;
                    case BACKGROUND:
                        BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), bitmap);
                        user_Bimage.setBackground(bitmapDrawable);
                        break;
                }
            }
        }
    }

    // 时间
    private String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }
}