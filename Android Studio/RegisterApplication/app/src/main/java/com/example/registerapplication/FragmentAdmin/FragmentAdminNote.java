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
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.registerapplication.Adapters.AdminNoteAdapter;
import com.example.registerapplication.Entity.Note;
import com.example.registerapplication.Response.ListTResponse;
import com.example.registerapplication.Response.NoteResponse;
import com.example.registerapplication.Service.ListTService;
import com.example.registerapplication.R;
import com.example.registerapplication.Service.NoteService;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FragmentAdminNote extends Fragment implements AdminNoteAdapter.OnItemClickListener {
    private static final String TAG = "FragmentAdminNote";
    private SearchView searchView;
    private RecyclerView userRecyclerView;
    private RecyclerView searchRecyclerView;
    private AdminNoteAdapter adapter;
    private AdminNoteAdapter searchAdapter;
    private List<Note> noteList = new ArrayList<>();
    private List<Note> searchResultList = new ArrayList<>();
    private ListTService listTService;
    private NoteService noteService;
    private Handler handler = new Handler();
    private Runnable searchRunnable;
    private static final long DEBOUNCE_DELAY = 300;
    private View currentView;
    private boolean isSearchMode = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin2_note, container, false);
        currentView = view;

        // 初始化Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        listTService = retrofit.create(ListTService.class);
        noteService = retrofit.create(NoteService.class);

        initViews(view);
        initSearchView(view);
        initUserList();
        loadAllUsers(view);

        return view;
    }

    private void initViews(View view) {
        searchView = view.findViewById(R.id.AdminNote_searchView);
        userRecyclerView = view.findViewById(R.id.admin1_noteS_list); // 原始列表
        searchRecyclerView = view.findViewById(R.id.admin1_note_list); // 搜索结果列表
    }

    private void initUserList() {
        // 初始化原始列表适配器
        adapter = new AdminNoteAdapter(noteList);
        adapter.setOnItemClickListener(this);
        userRecyclerView.setAdapter(adapter);
        userRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // 初始化搜索结果适配器
        searchAdapter = new AdminNoteAdapter(searchResultList);
        searchAdapter.setOnItemClickListener(this);
        searchRecyclerView.setAdapter(searchAdapter);
        searchRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
    }

    @Override
    public void onRevampClick(Note note) {
        if (note != null) {
            showRevampDialog(note);
        }
    }

    @Override
    public void onDeleteClick(Note note) {
        if (note != null) {
            showDeleteConfirmation(note);
        }
    }

    private void showRevampDialog(Note note) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("修改笔记信息");

        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_edit_note, null);
        TextView id = dialogView.findViewById(R.id.Dialog_note_id);
        TextView tv_visibility = dialogView.findViewById(R.id.Dialog_note_visibility);
        RadioButton visibility1 = dialogView.findViewById(R.id.Dialog_note_visibility1);
        RadioButton visibility2 = dialogView.findViewById(R.id.Dialog_note_visibility2);

        id.setText(String.valueOf(note.getTitle()));
        if (note.getVisibility() == 1) {
            tv_visibility.setText("公开");
            visibility1.setChecked(true);
        } else {
            tv_visibility.setText("私密");
            visibility2.setChecked(true);
        }

        builder.setView(dialogView)
                .setPositiveButton("修改", (dialog, which) -> {
                    int visibility = visibility1.isChecked() ? 1 : 2;
                    updateNoteVisibility(note.getNoteId(), visibility);
                })
                .setNegativeButton("取消", null)
                .show();
    }

    private void showDeleteConfirmation(Note note) {
        new AlertDialog.Builder(requireContext())
                .setTitle("确认删除")
                .setMessage("确定要删除笔记 " + note.getTitle() + " 吗?")
                .setPositiveButton("删除", (dialog, which) -> deleteNote(note.getNoteId()))
                .setNegativeButton("取消", null)
                .show();
    }

    private void updateNoteVisibility(long noteId, int visibility) {
        Call<NoteResponse> call = noteService.updateNoteVisibility(noteId, visibility);
        call.enqueue(new Callback<NoteResponse>() {
            @Override
            public void onResponse(Call<NoteResponse> call, Response<NoteResponse> response) {
                Toast.makeText(requireContext(),
                        response.isSuccessful() ? "修改成功" : "修改失败",
                        Toast.LENGTH_SHORT).show();
                loadAllUsers(currentView);
            }

            @Override
            public void onFailure(Call<NoteResponse> call, Throwable t) {
                Log.e("Side4RevampPassword", "网络请求失败: " + t.getMessage());
            }
        });
    }

    private void deleteNote(long noteId) {
        Call<NoteResponse> call = noteService.deleteNote(noteId);
        call.enqueue(new Callback<NoteResponse>() {
            @Override
            public void onResponse(Call<NoteResponse> call, Response<NoteResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    Toast.makeText(requireContext(), "笔记删除成功", Toast.LENGTH_SHORT).show();
                    loadAllUsers(currentView);
                } else {
                    Toast.makeText(requireContext(), "删除失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NoteResponse> call, Throwable t) {
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

        Log.d(TAG, "加载所有笔记");
        Call<ListTResponse<List<Note>>> call = listTService.getAllNotes();
        call.enqueue(new Callback<ListTResponse<List<Note>>>() {
            @Override
            public void onResponse(Call<ListTResponse<List<Note>>> call, Response<ListTResponse<List<Note>>> response) {
                showLoading(view, false);
                if (response.isSuccessful() && response.body() != null) {
                    List<Note> notes = response.body().getList();
                    Log.d(TAG, "加载成功，笔记数量: " + notes.size());
                    setupUserRecyclerView(notes);
                    if (notes.isEmpty()) {
                        handleSearchError(view, "暂无笔记数据");
                    }
                } else {
                    handleSearchError(view, "加载失败: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ListTResponse<List<Note>>> call, Throwable t) {
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

        view.findViewById(R.id.AdminNote_searchText).setOnClickListener(v -> {
            String query = searchView.getQuery().toString();
            if (!query.isEmpty()) {
                performSearch(view, query);
                searchView.clearFocus();
            }
        });
    }

    //搜索笔记
    private void performSearch(View view, String query) {
        view.findViewById(R.id.searchBeforeGroup).setVisibility(View.GONE);
        view.findViewById(R.id.searchResultContainer).setVisibility(View.VISIBLE);
        ((TextView) view.findViewById(R.id.searchResultTitle)).setText("搜索结果: " + query);
        isSearchMode = true;
        Log.d(TAG, "执行搜索: " + query);
        if (listTService != null) {
            showLoading(view, true);
            view.findViewById(R.id.emptyView).setVisibility(View.GONE);
            Call<ListTResponse<List<Note>>> call = listTService.adminNoteSearch(queryToList(query));
            call.enqueue(new Callback<ListTResponse<List<Note>>>() {
                @Override
                public void onResponse(Call<ListTResponse<List<Note>>> call, Response<ListTResponse<List<Note>>> response) {
                    showLoading(view, false);
                    if (response.isSuccessful() && response.body() != null) {
                        List<Note> notes = response.body().getList();
                        Log.d(TAG, "搜索成功，结果数量: " + notes.size());
                        setupSearchResultRecyclerView(notes);
                        if (notes.isEmpty()) { handleSearchError(view, "未找到相关笔记"); }
                    } else {
                        handleSearchError(view, "搜索失败: " + response.message());
                    }
                }

                @Override
                public void onFailure(Call<ListTResponse<List<Note>>> call, Throwable t) {
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
                Call<ListTResponse<List<Note>>> call = listTService.adminNoteSearch(keywords);
                call.enqueue(new Callback<ListTResponse<List<Note>>>() {
                    @Override
                    public void onResponse(Call<ListTResponse<List<Note>>> call, Response<ListTResponse<List<Note>>> response) {
                        showLoading(view, false);
                        if (response.isSuccessful() && response.body() != null) {
                            List<Note> notes = response.body().getList();
                            Log.d(TAG, "实时搜索结果数量: " + notes.size());
                            setupSearchResultRecyclerView(notes);
                            if (notes.isEmpty()) {
                                handleSearchError(view, "未找到相关笔记");
                            }
                        } else {
                            handleSearchError(view, "搜索失败: " + response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<ListTResponse<List<Note>>> call, Throwable t) {
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

    private void setupUserRecyclerView(List<Note> notes) {
        Log.d(TAG, "设置原始列表数据，数量: " + notes.size());
        noteList.clear();
        noteList.addAll(notes);
        adapter.notifyDataSetChanged();

        View emptyView = currentView.findViewById(R.id.emptyView);
        if (emptyView != null) {
            emptyView.setVisibility(notes.isEmpty() ? View.VISIBLE : View.GONE);
        }
    }

    private void setupSearchResultRecyclerView(List<Note> notes) {
        Log.d(TAG, "设置搜索结果列表数据，数量: " + notes.size());
        searchResultList.clear();
        searchResultList.addAll(notes);
        searchAdapter.notifyDataSetChanged();

        View emptyView = currentView.findViewById(R.id.emptyView);
        if (emptyView != null) {
            emptyView.setVisibility(notes.isEmpty() ? View.VISIBLE : View.GONE);
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
            noteList.clear();
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
}