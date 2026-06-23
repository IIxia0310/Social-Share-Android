package com.example.registerapplication.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.registerapplication.Adapters.NoteListItemAdapter;
import com.example.registerapplication.Entity.Data.NoteHistoryltem;
import com.example.registerapplication.Entity.Data.NoteItem;
import com.example.registerapplication.Entity.Data.UserItem;
import com.example.registerapplication.Entity.Like;
import com.example.registerapplication.Entity.User;
import com.example.registerapplication.R;
import com.example.registerapplication.Response.ListTResponse;
import com.example.registerapplication.Service.ListTService;
import com.example.registerapplication.db.NoteHistoryDao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.NonNull;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 浏览记录
 */
public class Side3BrowsingHistory extends AppCompatActivity {

    private LinearLayoutCompat theme;
    private UserItem userItem =UserItem.getUserItem();
    private User user = userItem.getUser();


    private NoteHistoryDao historyDao;
    private Long userId; // 当前用户ID
//    private ListView historyListView;

    private boolean isLoading = false;
    private List<NoteHistoryltem> historyList;
    private NoteListItemAdapter adapter;
    private RecyclerView recyclerView;
    private ListTService listTService;
    private List<NoteItem> noteList;
    private String create_time = getCurrentTime();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.side3_browsing_history);


        // 初始化Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        listTService = retrofit.create(ListTService.class);

        initToolbar();
        initViews(); // 先初始化视图

        // 初始化 DAO
        historyDao = new NoteHistoryDao(this);

        // 获取当前用户ID
        userId = user.getUserId();
        
        init();
        
        // 加载浏览历史
        loadBrowseHistory();
        
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.Toolbar_Side3BrowsingHistory);
        toolbar.setTitle("浏览记录");
        toolbar.setNavigationIcon(R.drawable.toolbar_2);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void  initViews(){
        theme = findViewById(R.id.Theme_Side3BrowsingHistory);

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
    }
    public void init() {
        // 初始化RecyclerView
        recyclerView = findViewById(R.id.NoteList);
        // 设置2列瀑布流布局
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(
                2,
                StaggeredGridLayoutManager.VERTICAL
        );
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        recyclerView.setLayoutManager(layoutManager);

        // 初始化适配器
        noteList = new ArrayList<>();
        adapter = new NoteListItemAdapter(noteList);
        recyclerView.setAdapter(adapter);

        // 设置点击事件监听器
        adapter.setOnNoteItemClickListener(new NoteListItemAdapter.OnNoteItemClickListener() {
            @Override
            public void onNoteItemClick(NoteItem noteItem) {
                // 处理点击事件，例如跳转到笔记详情页
                Toast.makeText(Side3BrowsingHistory.this, "点击了笔记：" + noteItem.getNote().getTitle(), Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(Side3BrowsingHistory.this, NoteDetails.class);
                intent1.putExtra("note_item", noteItem);
                startActivity(intent1);
            }

            @Override
            public void onLikeButtonClick(NoteItem noteItem) {
                // 点赞处理
                int position = noteList.indexOf(noteItem);

                NoteListItemAdapter.NoteViewHolder holder = (NoteListItemAdapter.NoteViewHolder) recyclerView.findViewHolderForAdapterPosition(position);
                if (holder != null) {
                    if (noteItem.getLikeIng()==1) {
                        // 有则删除
                        Toast.makeText(Side3BrowsingHistory.this, "取消点赞：" + noteItem.getNote().getTitle(), Toast.LENGTH_SHORT).show();
                        Call<ListTResponse> call = listTService.delectLike(userId, noteItem.getNote().getNoteId());
                        call.enqueue(new Callback<ListTResponse>() {
                            @Override
                            public void onResponse(Call<ListTResponse> call, Response<ListTResponse> response) {
                                ResponseBody error = response.errorBody();
                                if (error == null) {
                                    ListTResponse likeResponse = response.body();
                                    if (likeResponse != null && likeResponse.isSuccess()) {
                                        loadBrowseHistory();
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
                        Toast.makeText(Side3BrowsingHistory.this, "点赞：" + noteItem.getNote().getTitle(), Toast.LENGTH_SHORT).show();
                        Like like = new Like(null, userId, noteItem.getNote().getNoteId(), null, create_time);
                        Call<ListTResponse> call = listTService.addLike(like);
                        call.enqueue(new Callback<ListTResponse>() {
                            @Override
                            public void onResponse(Call<ListTResponse> call, Response<ListTResponse> response) {
                                ResponseBody error = response.errorBody();
                                if (error == null) {
                                    ListTResponse likeResponse = response.body();
                                    if (likeResponse != null && likeResponse.isSuccess()) {
                                        loadBrowseHistory();
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
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) recyclerView.getLayoutManager();
                int[] into = new int[2];
                layoutManager.findLastVisibleItemPositions(into);
            }
        });

        // 初始化Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        listTService = retrofit.create(ListTService.class);

    }
    private void loadBrowseHistory() {
        // 从数据库获取浏览历史
        historyList = historyDao.getHistoryByUserId(userId);

        Call<ListTResponse<List<NoteItem>>> call = listTService.browseHistory(historyList);
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
                    }
                } else {
                    // 处理响应失败的情况，例如根据错误码显示不同提示
                    Log.e("FragmentHomeNoteItem", "响应失败，错误码: " + response.code());
                    noteList.clear();
                    adapter.notifyDataSetChanged();
                    isLoading = false;
                }
            }

            @Override
            public void onFailure(Call<ListTResponse<List<NoteItem>>> call, Throwable t) {
                // 处理请求失败的情况，例如根据异常类型显示不同提示
                Log.e("FragmentHomeNoteItem", "请求失败，异常信息: " + t.getMessage());
                noteList.clear();
                adapter.notifyDataSetChanged();
                isLoading = false;
            }
        });
    }

    // 时间
    private String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }
}