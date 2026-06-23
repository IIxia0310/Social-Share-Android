package com.example.registerapplication.View;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.registerapplication.Adapters.NoteListItemAdapter;
import com.example.registerapplication.Adapters.NoteSearch.HistoryAdapter;
import com.example.registerapplication.Adapters.NoteSearch.HotAdapter;
import com.example.registerapplication.Adapters.NoteSearch.SuggestionAdapter;
import com.example.registerapplication.Entity.NoteSearch.HotItem;
import com.example.registerapplication.Entity.Data.NoteItem;
import com.example.registerapplication.Entity.NoteSearch.SuggestionItem;
import com.example.registerapplication.Entity.Data.UserItem;
import com.example.registerapplication.Response.ListTResponse;
import com.example.registerapplication.Service.ListTService;
import com.example.registerapplication.R;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NoteSearch extends AppCompatActivity {
    private SearchView searchView;
    private RecyclerView historyRecyclerView, suggestionRecyclerView, hotRecyclerView, noteRecyclerView;
    private HistoryAdapter historyAdapter;
    private SuggestionAdapter suggestionAdapter;
    private HotAdapter hotAdapter;
    private NoteListItemAdapter noteAdapter;
    private List<String> historyList = new ArrayList<>();
    private List<SuggestionItem> suggestionList = new ArrayList<>();
    private List<HotItem> hotList = new ArrayList<>();
    private List<NoteItem> noteList = new ArrayList<>();
    private ListTService listTService;
    private Handler handler = new Handler();
    private Runnable searchRunnable;
    private static final long DEBOUNCE_DELAY = 300; // 防抖延迟时间，单位毫秒

    private Long userIng = UserItem.getUserItem().getUserlogin();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_search);

        // 初始化视图组件
        initViews();

        // 初始化搜索栏
        initSearchView();

        // 初始化历史记录列表
        initHistoryList();

        // 初始化猜你想搜列表
        initSuggestionList();

        // 初始化热点列表
        initHotList();

        // 初始化笔记列表
        initNoteList();

        // 初始化搜索服务
        initNoteService();

        // 设置清除历史按钮点击事件
        findViewById(R.id.NoteSearch_ClearHistoryBtn).setOnClickListener(v -> clearSearchHistory());
    }

    private void initNoteService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        listTService = retrofit.create(ListTService.class);
    }

    private void initViews() {
        searchView = findViewById(R.id.NoteSearch_searchView);
        historyRecyclerView = findViewById(R.id.NoteSearch_historyRecyclerView);
        suggestionRecyclerView = findViewById(R.id.NoteSearch_suggestionRecyclerView);
        hotRecyclerView = findViewById(R.id.NoteSearch_hotRecyclerView);
        noteRecyclerView = findViewById(R.id.NoteList);

        // 返回按钮点击事件
        findViewById(R.id.NoteSearch_button1).setOnClickListener(v -> onBackPressed());
    }

    private void initSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!query.isEmpty()) {
                    performSearch(query);
                    saveSearchHistory(query);
                    searchView.clearFocus(); // 收起键盘
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // 实时搜索逻辑
                if (newText.isEmpty()) {
                    // 搜索框为空，显示搜索前内容
                    showPreSearchContent();
                } else {
                    // 检查网络连接
                    if (!isNetworkConnected()) {
                        handleSearchError("无网络连接，请检查网络设置");
                        return false;
                    }

                    // 使用 HanLP 分词并搜索
                    performRealTimeSearch(newText);
                }
                return false;
            }
        });

        // 搜索按钮点击事件
        findViewById(R.id.NoteSearch_searchText).setOnClickListener(v -> {
            String query = searchView.getQuery().toString();
            if (!query.isEmpty()) {
                performSearch(query);
                saveSearchHistory(query);
                searchView.clearFocus();
            }
        });
    }

    private void initHistoryList() {
        // 设置历史记录RecyclerView为水平布局
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                this, LinearLayoutManager.HORIZONTAL, false);
        historyRecyclerView.setLayoutManager(layoutManager);

        // 加载历史记录
        loadSearchHistory();

        // 设置适配器
        historyAdapter = new HistoryAdapter(historyList);
        historyRecyclerView.setAdapter(historyAdapter);

        // 设置历史记录项点击事件
        historyAdapter.setOnItemClickListener(query -> {
            searchView.setQuery(query, true); // 填充搜索框并提交
        });
    }

    private void initSuggestionList() {
        // 设置猜你想搜RecyclerView为2列网格布局
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        suggestionRecyclerView.setLayoutManager(layoutManager);

        // 生成测试数据
        generateSuggestionData();

        // 设置适配器
        suggestionAdapter = new SuggestionAdapter(suggestionList);
        suggestionRecyclerView.setAdapter(suggestionAdapter);

        // 设置点击事件
        suggestionAdapter.setOnItemClickListener(item -> {
            searchView.setQuery(item.getTitle(), true);
        });
    }

    private void initHotList() {
        // 设置热点RecyclerView为垂直列表
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        hotRecyclerView.setLayoutManager(layoutManager);

        // 生成测试数据
        generateHotData();

        // 设置适配器
        hotAdapter = new HotAdapter(hotList);
        hotRecyclerView.setAdapter(hotAdapter);

        // 设置点击事件
        hotAdapter.setOnItemClickListener(item -> {
            searchView.setQuery(item.getTitle(), true);
        });
    }

    private void initNoteList() {
        // 设置笔记RecyclerView为瀑布流布局
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        noteRecyclerView.setLayoutManager(layoutManager);

        // 设置适配器
        noteAdapter = new NoteListItemAdapter(noteList);
        noteRecyclerView.setAdapter(noteAdapter);

        // 设置点击事件
        noteAdapter.setOnNoteItemClickListener(new NoteListItemAdapter.OnNoteItemClickListener() {
            @Override
            public void onNoteItemClick(NoteItem noteItem) {
                // 处理笔记点击事件
                Toast.makeText(NoteSearch.this, "点击了笔记：" + noteItem.getNote().getTitle(), Toast.LENGTH_SHORT).show();
                // 跳转到笔记详情页
                 Intent intent = new Intent(NoteSearch.this, NoteDetails.class);
                 intent.putExtra("note_item", noteItem);
                 startActivity(intent);
            }

            @Override
            public void onLikeButtonClick(NoteItem noteItem) {
                // 处理点赞事件
            }
        });
    }

    private void performSearch(String query) {
        // 显示搜索结果区域，隐藏搜索前内容
        findViewById(R.id.searchBeforeGroup).setVisibility(View.GONE);
        findViewById(R.id.searchResultContainer).setVisibility(View.VISIBLE);
        ((TextView) findViewById(R.id.searchResultTitle)).setText("搜索结果: " + query);

        // 检查网络连接
        if (!isNetworkConnected()) {
            handleSearchError("无网络连接，请检查网络设置");
            return;
        }

        // 使用 HanLP 进行分词
        List<Term> termList = HanLP.segment(query);
        List<String> keywords = new ArrayList<>();
        for (Term term : termList) {
            keywords.add(term.word);
        }
        // 执行实际的搜索逻辑
        if (listTService != null) {
            // 显示加载状态
            showLoading(true);
            findViewById(R.id.emptyView).setVisibility(View.GONE);
            // 搜索笔记：调用后端API，传递分词后的关键词列表
            Call<ListTResponse<List<NoteItem>>> call = listTService.noteSearch(keywords, userIng);
            call.enqueue(new Callback<ListTResponse<List<NoteItem>>>() {
                @Override
                public void onResponse(Call<ListTResponse<List<NoteItem>>> call, Response<ListTResponse<List<NoteItem>>> response) {
                    showLoading(false);
                    if (response.isSuccessful()) {
                        ListTResponse<List<NoteItem>> body = response.body();
                        if (body != null) {
                            List<NoteItem> notes = body.getList();
                            setupNoteRecyclerView(notes);
                            if (notes.isEmpty()) {
                                handleSearchError("未找到相关笔记");
                            }
                        }
                    } else {
                        handleSearchError("搜索失败: " + response.message());
                    }
                }

                @Override
                public void onFailure(Call<ListTResponse<List<NoteItem>>> call, Throwable t) {
                    showLoading(false);
                    if (t instanceof IOException) {
                        handleSearchError("网络连接错误，请检查网络设置: " + t.getMessage());
                    } else {
                        handleSearchError("搜索服务出错，请稍后重试: " + t.getMessage());
                    }
                    t.printStackTrace();
                }
            });
        } else {
            handleSearchError("搜索服务未初始化");
        }
    }
    private void performRealTimeSearch(String query) {
        findViewById(R.id.searchBeforeGroup).setVisibility(View.GONE);
        findViewById(R.id.searchResultContainer).setVisibility(View.VISIBLE);
        // 防抖处理：取消之前的搜索请求
        if (searchRunnable != null) {
            handler.removeCallbacks(searchRunnable);
        }

        // 创建新的搜索请求
        searchRunnable = () -> {
            // 显示加载状态
            showLoading(true);
            findViewById(R.id.emptyView).setVisibility(View.GONE);

            // 使用 HanLP 进行分词
            List<Term> termList = HanLP.segment(query);
            List<String> keywords = new ArrayList<>();
            for (Term term : termList) {
                keywords.add(term.word);
            }

            // 执行实际的搜索逻辑
            if (listTService != null) {
                // 调用后端API，传递分词后的关键词列表
                Call<ListTResponse<List<NoteItem>>> call = listTService.noteSearch(keywords,userIng);
                call.enqueue(new Callback<ListTResponse<List<NoteItem>>>() {
                    @Override
                    public void onResponse(Call<ListTResponse<List<NoteItem>>> call, Response<ListTResponse<List<NoteItem>>> response) {
                        showLoading(false);

                        if (response.isSuccessful() && response.body() != null) {
                            List<NoteItem> notes = response.body().getList();
                            setupNoteRecyclerView(notes);

                            if (notes.isEmpty()) {
                                handleSearchError("未找到相关笔记");
                            }
                        } else {
                            handleSearchError("搜索失败: " + response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<ListTResponse<List<NoteItem>>> call, Throwable t) {
                        showLoading(false);

                        // 区分不同类型的网络错误
                        if (t instanceof IOException) {
                            handleSearchError("网络连接错误，请检查网络设置");
                        } else {
                            handleSearchError("搜索服务出错，请稍后重试");
                        }

                        t.printStackTrace();
                    }
                });
            } else {
                showLoading(false);
                handleSearchError("搜索服务未初始化");
            }
        };

        // 延迟执行搜索请求
        handler.postDelayed(searchRunnable, DEBOUNCE_DELAY);
    }

    private void setupNoteRecyclerView(List<NoteItem> notes) {
        noteList.clear();
        noteList.addAll(notes);
        noteAdapter.notifyDataSetChanged();
    }

    private void showPreSearchContent() {
        findViewById(R.id.searchBeforeGroup).setVisibility(View.VISIBLE);
        findViewById(R.id.searchResultContainer).setVisibility(View.GONE);
    }

    private void saveSearchHistory(String query) {
        // 保存搜索历史到SharedPreferences
        SharedPreferences prefs = getSharedPreferences("search_history", Context.MODE_PRIVATE);
        Set<String> historySet = prefs.getStringSet("history", new HashSet<>());

        // 创建新的历史记录集合，保持顺序并去重
        List<String> newHistoryList = new ArrayList<>(historySet);
        newHistoryList.remove(query); // 移除已存在的查询（如果有）
        newHistoryList.add(0, query); // 添加到开头（最新）

        // 限制历史记录数量
        if (newHistoryList.size() > 10) {
            newHistoryList = newHistoryList.subList(0, 10);
        }

        // 保存到SharedPreferences
        SharedPreferences.Editor editor = prefs.edit();
        editor.putStringSet("history", new HashSet<>(newHistoryList));
        if (!editor.commit()) {
            Toast.makeText(this, "保存搜索历史失败", Toast.LENGTH_SHORT).show();
        }

        // 更新UI
        loadSearchHistory();
    }

    private void loadSearchHistory() {
        // 从SharedPreferences加载搜索历史
        SharedPreferences prefs = getSharedPreferences("search_history", Context.MODE_PRIVATE);
        Set<String> historySet = prefs.getStringSet("history", new HashSet<>());

        // 更新历史记录列表
        historyList.clear();
        historyList.addAll(historySet);

        // 通知适配器数据已更改
        if (historyAdapter != null) {
            historyAdapter.notifyDataSetChanged();
        }

        // 显示或隐藏历史记录标题和清除按钮
        View historyTitle = findViewById(R.id.historyTitle);
        View clearHistoryBtn = findViewById(R.id.NoteSearch_ClearHistoryBtn);
        if (historyList.isEmpty()) {
            historyTitle.setVisibility(View.GONE);
            clearHistoryBtn.setVisibility(View.GONE);
        } else {
            historyTitle.setVisibility(View.VISIBLE);
            clearHistoryBtn.setVisibility(View.VISIBLE);
        }
    }

    private void clearSearchHistory() {
        // 清除搜索历史
        SharedPreferences prefs = getSharedPreferences("search_history", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove("history");
        if (!editor.commit()) {
            Toast.makeText(this, "清除搜索历史失败", Toast.LENGTH_SHORT).show();
        }

        // 更新UI
        loadSearchHistory();

        // 显示提示
        Toast.makeText(this, "已清除搜索历史", Toast.LENGTH_SHORT).show();
    }

    private void generateSuggestionData() {
        // 生成猜你想搜测试数据
        suggestionList.add(new SuggestionItem("潮流发型"));
        suggestionList.add(new SuggestionItem("美食推荐"));
        suggestionList.add(new SuggestionItem("穿搭技巧"));
        suggestionList.add(new SuggestionItem("怪趣头像"));
        suggestionList.add(new SuggestionItem("宠物出道"));
        suggestionList.add(new SuggestionItem("热门影视"));
    }

    private void generateHotData() {
        // 生成热点测试数据
        hotList.add(new HotItem("出行记ᯓ，美食分享", 1234));
        hotList.add(new HotItem("森系婚礼，与自然共舞", 987));
        hotList.add(new HotItem("隐藏在城市里的美食小店", 876));
        hotList.add(new HotItem("泥嚎！吃不吃苹狗", 765));
        hotList.add(new HotItem("油焖大虾太香了～", 654));
    }

    // 添加加载状态显示
    private void showLoading(boolean show) {
        View loadingView = findViewById(R.id.loadingView);
        if (loadingView != null) {
            loadingView.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    // 增强错误处理
    private void handleSearchError(String message) {
        View emptyView = findViewById(R.id.emptyView);
        TextView emptyText = findViewById(R.id.emptyText);

        if (emptyView != null) {
            emptyText.setText(message);
            emptyView.setVisibility(View.VISIBLE);
        }

        noteList.clear();
        noteAdapter.notifyDataSetChanged();
    }

    // 检查网络连接状态
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}