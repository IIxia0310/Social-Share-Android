package com.example.registerapplication.FragmentAdmin;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.registerapplication.Adapters.AdminMessageAdapter;
import com.example.registerapplication.Adapters.AdminNoteAdapter;
import com.example.registerapplication.Adapters.AdminUserAdapter;
import com.example.registerapplication.Entity.Data.MessageItem;
import com.example.registerapplication.Entity.Data.UserItem;
import com.example.registerapplication.Entity.Message;
import com.example.registerapplication.Entity.Note;
import com.example.registerapplication.Entity.User;
import com.example.registerapplication.Response.ListTResponse;
import com.example.registerapplication.Response.NoteResponse;
import com.example.registerapplication.Response.UserResponse;
import com.example.registerapplication.Service.ListTService;
import com.example.registerapplication.R;
import com.example.registerapplication.Service.NoteService;
import com.example.registerapplication.Service.UserService;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FragmentAdminMessage extends Fragment implements AdminMessageAdapter.OnItemClickListener {
    private static final String TAG = "FragmentAdminMessage";
    private SearchView searchView;
    private RecyclerView userRecyclerView;
    private RecyclerView searchRecyclerView;
    private AdminMessageAdapter adapter;
    private AdminMessageAdapter searchAdapter;
    private List<MessageItem> messageItemList = new ArrayList<>();
    private List<MessageItem> searchResultList = new ArrayList<>();
    private ListTService listTService;
    private UserService userService;
    private Handler handler = new Handler();
    private Runnable searchRunnable;
    private static final long DEBOUNCE_DELAY = 300;
    private View currentView;
    private boolean isSearchMode = false;

    private String create_time = getCurrentTime();  // 获取当前时间


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin3_message, container, false);
        currentView = view;

        // 初始化Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        listTService = retrofit.create(ListTService.class);
        userService = retrofit.create(UserService.class);

        initViews(view);
        initSearchView(view);
        initUserList();
        loadAllUsers(view);

        return view;
    }

    private void initViews(View view) {
        searchView = view.findViewById(R.id.AdminMessage_searchView);
        userRecyclerView = view.findViewById(R.id.admin1_massageS_list); // 原始列表
        searchRecyclerView = view.findViewById(R.id.admin1_massage_list); // 搜索结果列表
    }

    private void initUserList() {
        // 初始化原始列表适配器
        adapter = new AdminMessageAdapter(messageItemList);
        adapter.setOnItemClickListener(this);
        userRecyclerView.setAdapter(adapter);
        userRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // 初始化搜索结果适配器
        searchAdapter = new AdminMessageAdapter(searchResultList);
        searchAdapter.setOnItemClickListener(this);
        searchRecyclerView.setAdapter(searchAdapter);
        searchRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
    }

    @Override
    public void onRevampClick(MessageItem messageItem) {
        if (messageItem != null) {
            showRevampDialog(messageItem);
        }
    }

    @Override
    public void onDeleteClick(MessageItem messageItem) {
        if (messageItem != null) {
            showDeleteConfirmation(messageItem);
        }
    }

    private void showRevampDialog(MessageItem messageItem) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("回复用户");

        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_edit_message, null);
        TextView id = dialogView.findViewById(R.id.Dialog_user_id);
        EditText etPassword = dialogView.findViewById(R.id.Dialog_et_content);


        String idS = String.valueOf(messageItem.getUser_id());
        id.setText(idS);


        etPassword.setHint("输入回复");
        builder.setView(dialogView)
                .setPositiveButton("回复", (dialog, which) -> {

                    String newContent = etPassword.getText().toString();
                    updateUser(messageItem.getUser_id(), newContent);
                })
                .setNegativeButton("取消", null)
                .show();
    }

    private void showDeleteConfirmation(MessageItem messageItem) {
        new AlertDialog.Builder(requireContext())
                .setTitle("确认删除")
                .setMessage("确定要删除留言 " + messageItem.getContent() + " 吗?")
                .setPositiveButton("删除", (dialog, which) -> deleteUser(messageItem.getUser_id()))
                .setNegativeButton("取消", null)
                .show();
    }





    private void updateUser(long userId, String newContent) {


        Message message = new Message(null,1L,userId,newContent,create_time,false);


        // 发送消息到服务器（使用 Retrofit 示例）
        Call<ListTResponse> call = listTService.addMessage(message);
        call.enqueue(new Callback<ListTResponse>() {
            @Override
            public void onResponse(Call<ListTResponse> call, Response<ListTResponse> response) {
                Toast.makeText(requireContext(),
                        response.isSuccessful() ? "回复成功" : "回复失败",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ListTResponse> call, Throwable t) {
                Log.e("MessageDetails", "请求失败", t);
            }
        });
    }

    private void deleteUser(long userId) {
        Call<UserResponse> call = userService.deleteUser(userId);

        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                Toast.makeText(requireContext(),
                        response.isSuccessful() ? "删除成功" : "删除失败",
                        Toast.LENGTH_SHORT).show();
                loadAllUsers(getView());
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(requireContext(), "网络错误", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadAllUsers(View view) {
        if (!isNetworkConnected()) {
            handleSearchError(view, "无网络连接，请检查网络设置");
            return;
        }
        showLoading(view, true);
        view.findViewById(R.id.emptyView).setVisibility(View.GONE);
        Call<ListTResponse<List<MessageItem>>> call = listTService.messageItem(1L);
        call.enqueue(new Callback<ListTResponse<List<MessageItem>>>() {
            @Override
            public void onResponse(Call<ListTResponse<List<MessageItem>>> call, Response<ListTResponse<List<MessageItem>>> response) {
                showLoading(view, false);
                if (response.isSuccessful() && response.body() != null) {
                    List<MessageItem> messageItems = response.body().getList();
                    Log.d(TAG, "加载成功，留言数量: " + messageItems.size());
                    setupUserRecyclerView(messageItems);
                    if (messageItems.isEmpty()) { handleSearchError(view, "暂无留言数据"); }
                } else {
                    handleSearchError(view, "加载失败: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ListTResponse<List<MessageItem>>> call, Throwable t) {
                showLoading(view, false);
                handleSearchError(view, t instanceof IOException ? "网络错误" : "服务器异常");
            }
        });
    }

    private void initSearchView(View view) {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!query.isEmpty()) {
                    Log.d(TAG, "搜索提交: " + query);
                    performSearch(view, query);
                    searchView.clearFocus();
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    Log.d(TAG, "搜索文本为空，显示原始内容");
                    showPreSearchContent(view);
                } else {
                    if (!isNetworkConnected()) {
                        handleSearchError(view, "无网络连接，请检查网络设置");
                        return false;
                    }
                    Log.d(TAG, "搜索文本变化: " + newText);
                    performRealTimeSearch(view, newText);
                }
                return false;
            }
        });

        view.findViewById(R.id.AdminMessage_searchText).setOnClickListener(v -> {
            String query = searchView.getQuery().toString();
            if (!query.isEmpty()) {
                performSearch(view, query);
                searchView.clearFocus();
            }
        });
    }
    //搜索评论
    private void performSearch(View view, String query) {
        view.findViewById(R.id.searchBeforeGroup).setVisibility(View.GONE);
        view.findViewById(R.id.searchResultContainer).setVisibility(View.VISIBLE);
        ((TextView) view.findViewById(R.id.searchResultTitle)).setText("搜索结果: " + query);
        isSearchMode = true;
        Log.d(TAG, "执行搜索: " + query);
        if (listTService != null) {
            showLoading(view, true);
            view.findViewById(R.id.emptyView).setVisibility(View.GONE);

            Call<ListTResponse<List<MessageItem>>> call = listTService.adminMessageSearch(queryToList(query));
            call.enqueue(new Callback<ListTResponse<List<MessageItem>>>() {
                @Override
                public void onResponse(Call<ListTResponse<List<MessageItem>>> call, Response<ListTResponse<List<MessageItem>>> response) {
                    showLoading(view, false);
                    if (response.isSuccessful() && response.body() != null) {
                        List<MessageItem> messageItems = response.body().getList();
                        Log.d(TAG, "搜索成功，结果数量: " + messageItems.size());
                        setupSearchResultRecyclerView(messageItems);
                        if (messageItems.isEmpty()) {
                            handleSearchError(view, "未找到相关记录");
                        }
                    } else {
                        handleSearchError(view, "搜索失败: " + response.message());
                    }
                }

                @Override
                public void onFailure(Call<ListTResponse<List<MessageItem>>> call, Throwable t) {
                    showLoading(view, false);
                    handleSearchError(view, "搜索服务出错，请稍后重试");
                }
            });
        } else {
            handleSearchError(view, "搜索服务未初始化");
        }
    }

    private List<String> queryToList(String query) {
        List<Term> termList = HanLP.segment(query);
        List<String> keywords = new ArrayList<>();
        for (Term term : termList) {
            keywords.add(term.word);
        }
        return keywords;
    }

    private void performRealTimeSearch(View view, String query) {
        view.findViewById(R.id.searchBeforeGroup).setVisibility(View.GONE);
        view.findViewById(R.id.searchResultContainer).setVisibility(View.VISIBLE);
        isSearchMode = true;

        if (searchRunnable != null) {
            handler.removeCallbacks(searchRunnable);
        }

        searchRunnable = () -> {
            Log.d(TAG, "实时搜索: " + query);
            showLoading(view, true);
            view.findViewById(R.id.emptyView).setVisibility(View.GONE);

            List<String> keywords = queryToList(query);

            if (listTService != null) {
                Call<ListTResponse<List<MessageItem>>> call = listTService.adminMessageSearch(keywords);
                call.enqueue(new Callback<ListTResponse<List<MessageItem>>>() {
                    @Override
                    public void onResponse(Call<ListTResponse<List<MessageItem>>> call, Response<ListTResponse<List<MessageItem>>> response) {
                        showLoading(view, false);
                        if (response.isSuccessful() && response.body() != null) {
                            List<MessageItem> messageItems = response.body().getList();
                            Log.d(TAG, "实时搜索结果数量: " + messageItems.size());
                            setupSearchResultRecyclerView(messageItems);
                            if (messageItems.isEmpty()) {
                                handleSearchError(view, "未找到相关记录");
                            }
                        } else {
                            handleSearchError(view, "搜索失败: " + response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<ListTResponse<List<MessageItem>>> call, Throwable t) {
                        showLoading(view, false);
                        if (t instanceof IOException) {
                            handleSearchError(view, "网络连接错误，请检查网络设置");
                        } else {
                            handleSearchError(view, "搜索服务出错，请稍后重试");
                        }
                        t.printStackTrace();
                    }
                });
            } else {
                showLoading(view, false);
                handleSearchError(view, "搜索服务未初始化");
            }
        };

        handler.postDelayed(searchRunnable, DEBOUNCE_DELAY);
    }

    private void setupUserRecyclerView(List<MessageItem> messageItems) {
        Log.d(TAG, "设置原始列表数据，数量: " + messageItems.size());
        messageItemList.clear();
        messageItemList.addAll(messageItems);
        adapter.notifyDataSetChanged();

        View emptyView = currentView.findViewById(R.id.emptyView);
        if (emptyView != null) {
            emptyView.setVisibility(messageItems.isEmpty() ? View.VISIBLE : View.GONE);
        }
    }

    private void setupSearchResultRecyclerView(List<MessageItem> messageItems) {
        Log.d(TAG, "设置搜索结果列表数据，数量: " + messageItems.size());
        searchResultList.clear();
        searchResultList.addAll(messageItems);
        searchAdapter.notifyDataSetChanged();

        View emptyView = currentView.findViewById(R.id.emptyView);
        if (emptyView != null) {
            emptyView.setVisibility(messageItems.isEmpty() ? View.VISIBLE : View.GONE);
        }
    }

    private void showPreSearchContent(View view) {
        Log.d(TAG, "显示搜索前内容");
        view.findViewById(R.id.searchBeforeGroup).setVisibility(View.VISIBLE);
        view.findViewById(R.id.searchResultContainer).setVisibility(View.GONE);
        isSearchMode = false;
        loadAllUsers(view);
    }

    private void showLoading(View view, boolean show) {
        View loadingView = view.findViewById(R.id.loadingView);
        if (loadingView != null) {
            loadingView.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    private void handleSearchError(View view, String message) {
        Log.e(TAG, "搜索错误: " + message);
        View emptyView = view.findViewById(R.id.emptyView);
        TextView emptyText = view.findViewById(R.id.emptyText);

        if (emptyView != null) {
            emptyText.setText(message);
            emptyView.setVisibility(View.VISIBLE);
        }

        if (isSearchMode) {
            searchResultList.clear();
            searchAdapter.notifyDataSetChanged();
        } else {
            messageItemList.clear();
            adapter.notifyDataSetChanged();
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) requireContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            android.net.NetworkCapabilities networkCapabilities =
                    connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
            return networkCapabilities != null &&
                    (networkCapabilities.hasTransport(android.net.NetworkCapabilities.TRANSPORT_WIFI) ||
                            networkCapabilities.hasTransport(android.net.NetworkCapabilities.TRANSPORT_CELLULAR));
        } else {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
        }
    }


    // 时间
    private String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }

}