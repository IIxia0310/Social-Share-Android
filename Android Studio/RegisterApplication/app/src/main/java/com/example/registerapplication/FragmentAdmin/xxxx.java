//package com.example.registerapplication.FragmentAdmin;
//
//import android.content.Context;
//import android.net.ConnectivityManager;
//import android.net.NetworkInfo;
//import android.os.Bundle;
//import android.os.Handler;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.RadioButton;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AlertDialog;
//import androidx.appcompat.widget.SearchView;
//import androidx.fragment.app.Fragment;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.registerapplication.Adapters.AdminNoteAdapter;
//import com.example.registerapplication.Entity.Note;
//import com.example.registerapplication.R;
//import com.example.registerapplication.Response.ListTResponse;
//import com.example.registerapplication.Response.NoteResponse;
//import com.example.registerapplication.Service.ListTService;
//import com.example.registerapplication.Service.NoteService;
//import com.hankcs.hanlp.HanLP;
//import com.hankcs.hanlp.seg.common.Term;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//import retrofit2.Retrofit;
//import retrofit2.converter.gson.GsonConverterFactory;
//
//public class xxxx extends Fragment implements AdminNoteAdapter.OnItemClickListener {
//    private static final String TAG = "FragmentAdminNote";
//    private SearchView searchView;
//    private RecyclerView userRecyclerView; // 只需要一个RecyclerView
//    private AdminNoteAdapter adapter;
//    private List<Note> noteList = new ArrayList<>(); // 统一使用这个列表作为数据源
//    private ListTService listTService;
//    private NoteService noteService;
//    private Handler handler = new Handler();
//    private Runnable searchRunnable;
//    private static final long DEBOUNCE_DELAY = 300;
//    private View currentView; // 保存当前视图引用
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        currentView = inflater.inflate(R.layout.admin2_note, container, false);
//
//        // 初始化Retrofit
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://10.0.2.2:8080/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        listTService = retrofit.create(ListTService.class);
//        noteService = retrofit.create(NoteService.class);
//
//        initViews(currentView);
//        initSearchView(currentView);
//        initUserList();
//        loadAllUsers(currentView);
//
//        return currentView;
//    }
//
//    private void initViews(View view) {
//        searchView = view.findViewById(R.id.AdminNote_searchView);
//        userRecyclerView = view.findViewById(R.id.admin1_note_list);
//        // 移除对userSRecyclerView的引用，不再需要两个RecyclerView
//    }
//
//    private void initUserList() {
//        adapter = new AdminNoteAdapter(); // 使用无参构造函数
//        adapter.setOnItemClickListener(this);
//        userRecyclerView.setAdapter(adapter);
//        userRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
//    }
//
//    @Override
//    public void onRevampClick(int position, Note note) {
//        showRevampDialog(note);
//    }
//
//    @Override
//    public void onDeleteClick(int position, Note note) {
//        showDeleteConfirmation(note);
//    }
//
//    private void showRevampDialog(Note note) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
//        builder.setTitle("修改笔记信息");
//
//        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_edit_note, null);
//
//        TextView id = dialogView.findViewById(R.id.Dialog_note_id);
//        TextView tv_visibility = dialogView.findViewById(R.id.Dialog_note_visibility);
//        RadioButton visibility1 = dialogView.findViewById(R.id.Dialog_note_visibility1);
//        RadioButton visibility2 = dialogView.findViewById(R.id.Dialog_note_visibility2);
//
//        String idS = String.valueOf(note.getNoteId());
//        id.setText(idS);
//        if (note.getVisibility() == 1) {
//            tv_visibility.setText("公开");
//            visibility1.setChecked(true);
//        } else {
//            tv_visibility.setText("私密");
//            visibility2.setChecked(true);
//        }
//
//        builder.setView(dialogView)
//                .setPositiveButton("修改", (dialog, which) -> {
//                    int visibility = visibility1.isChecked() ? 1 : 2;
//                    updateNoteVisibility(note.getNoteId(), visibility); // 传递noteId而非userId
//                })
//                .setNegativeButton("取消", null)
//                .show();
//    }
//
//    private void showDeleteConfirmation(Note note) {
//        new AlertDialog.Builder(requireContext())
//                .setTitle("确认删除")
//                .setMessage("确定要删除笔记 " + note.getNoteId() + " 吗?")
//                .setPositiveButton("删除", (dialog, which) -> deleteNote(note.getNoteId()))
//                .setNegativeButton("取消", null)
//                .show();
//    }
//
//    private void updateNoteVisibility(long noteId, int visibility) {
//        Call<NoteResponse> call = noteService.updateNoteVisibility(noteId, visibility);
//        call.enqueue(new Callback<NoteResponse>() {
//            @Override
//            public void onResponse(Call<NoteResponse> call, Response<NoteResponse> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    NoteResponse noteResponse = response.body();
//                    if (noteResponse.isSuccess()) {
//                        Toast.makeText(requireContext(), "修改成功", Toast.LENGTH_SHORT).show();
////                        loadAllUsers(currentView); /
//
//                        loadAllUsers(getView());
//                    } else {
//                        Toast.makeText(requireContext(), "修改失败：" + noteResponse.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//                    Toast.makeText(requireContext(), "修改失败：" + response.message(), Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<NoteResponse> call, Throwable t) {
//                Log.e("Side4RevampPassword", "网络请求失败: " + t.getMessage());
//                Toast.makeText(requireContext(), "网络错误", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    private void deleteNote(long noteId) {
//        Call<NoteResponse> call = noteService.deleteNote(noteId);
//        call.enqueue(new Callback<NoteResponse>() {
//            @Override
//            public void onResponse(Call<NoteResponse> call, Response<NoteResponse> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    NoteResponse deleteResponse = response.body();
//                    if (deleteResponse.isSuccess()) {
//                        Toast.makeText(requireContext(), "笔记删除成功", Toast.LENGTH_SHORT).show();
////                        loadAllUsers(currentView); // 刷新列表
//                        loadAllUsers(getView());
//                    } else {
//                        Toast.makeText(requireContext(), "笔记删除失败：" + deleteResponse.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//                    Toast.makeText(requireContext(), "笔记删除失败：" + response.message(), Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<NoteResponse> call, Throwable t) {
//                Log.e("FragmentAdminNote", "删除请求失败: " + t.getMessage());
//                Toast.makeText(requireContext(), "网络错误", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    private void loadAllUsers(View view) {
//        if (!isNetworkConnected()) {
//            handleSearchError(view, "无网络连接，请检查网络设置");
//            return;
//        }
//
//        showLoading(view, true);
//        view.findViewById(R.id.emptyView).setVisibility(View.GONE);
//
//        Call<ListTResponse<List<Note>>> call = listTService.getAllNotes();
//        call.enqueue(new Callback<ListTResponse<List<Note>>>() {
//            @Override
//            public void onResponse(Call<ListTResponse<List<Note>>> call, Response<ListTResponse<List<Note>>> response) {
//                showLoading(view, false);
//                if (response.isSuccessful() && response.body() != null) {
//                    List<Note> notes = response.body().getList();
//                    setupUserRecyclerView(notes);
//                    if (notes.isEmpty()) {
//                        handleSearchError(view, "暂无笔记数据");
//                    }
//                } else {
//                    handleSearchError(view, "加载失败: " + response.message());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ListTResponse<List<Note>>> call, Throwable t) {
//                showLoading(view, false);
//                handleSearchError(view, t instanceof IOException ? "网络错误" : "服务器异常");
//            }
//        });
//    }
//
//    private void initSearchView(View view) {
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                if (!query.isEmpty()) {
//                    performSearch(view, query);
//                    searchView.clearFocus();
//                }
//                return true;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                if (newText.isEmpty()) {
//                    showPreSearchContent(view);
//                } else {
//                    if (!isNetworkConnected()) {
//                        handleSearchError(view, "无网络连接，请检查网络设置");
//                        return false;
//                    }
//                    performRealTimeSearch(view, newText);
//                }
//                return false;
//            }
//        });
//
//        view.findViewById(R.id.AdminNote_searchText).setOnClickListener(v -> {
//            String query = searchView.getQuery().toString();
//            if (!query.isEmpty()) {
//                performSearch(view, query);
//                searchView.clearFocus();
//            }
//        });
//    }
//
//    private void performSearch(View view, String query) {
//        view.findViewById(R.id.searchBeforeGroup).setVisibility(View.GONE);
//        view.findViewById(R.id.searchResultContainer).setVisibility(View.VISIBLE);
//        ((TextView) view.findViewById(R.id.searchResultTitle)).setText("搜索结果: " + query);
//
//        if (!isNetworkConnected()) {
//            handleSearchError(view, "无网络连接，请检查网络设置");
//            return;
//        }
//
//        List<Term> termList = HanLP.segment(query);
//        List<String> keywords = new ArrayList<>();
//        for (Term term : termList) {
//            keywords.add(term.word);
//        }
//
//        if (listTService != null) {
//            showLoading(view, true);
//            view.findViewById(R.id.emptyView).setVisibility(View.GONE);
//
//            Call<ListTResponse<List<Note>>> call = listTService.adminNoteSearch(keywords);
//            call.enqueue(new Callback<ListTResponse<List<Note>>>() {
//                @Override
//                public void onResponse(Call<ListTResponse<List<Note>>> call, Response<ListTResponse<List<Note>>> response) {
//                    showLoading(view, false);
//                    if (response.isSuccessful() && response.body() != null) {
//                        List<Note> notes = response.body().getList();
//                        setupUserRecyclerView(notes);
//                        if (notes.isEmpty()) {
//                            handleSearchError(view, "未找到相关笔记");
//                        }
//                    } else {
//                        handleSearchError(view, "搜索失败: " + response.message());
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<ListTResponse<List<Note>>> call, Throwable t) {
//                    showLoading(view, false);
//                    if (t instanceof IOException) {
//                        handleSearchError(view, "网络连接错误，请检查网络设置");
//                    } else {
//                        handleSearchError(view, "搜索服务出错，请稍后重试");
//                    }
//                    t.printStackTrace();
//                }
//            });
//        } else {
//            handleSearchError(view, "搜索服务未初始化");
//        }
//    }
//
//    private void performRealTimeSearch(View view, String query) {
//        view.findViewById(R.id.searchBeforeGroup).setVisibility(View.GONE);
//        view.findViewById(R.id.searchResultContainer).setVisibility(View.VISIBLE);
//
//        if (searchRunnable != null) {
//            handler.removeCallbacks(searchRunnable);
//        }
//
//        searchRunnable = () -> {
//            showLoading(view, true);
//            view.findViewById(R.id.emptyView).setVisibility(View.GONE);
//
//            List<Term> termList = HanLP.segment(query);
//            List<String> keywords = new ArrayList<>();
//            for (Term term : termList) {
//                keywords.add(term.word);
//            }
//
//            if (listTService != null) {
//                Call<ListTResponse<List<Note>>> call = listTService.adminNoteSearch(keywords);
//                call.enqueue(new Callback<ListTResponse<List<Note>>>() {
//                    @Override
//                    public void onResponse(Call<ListTResponse<List<Note>>> call, Response<ListTResponse<List<Note>>> response) {
//                        showLoading(view, false);
//                        if (response.isSuccessful() && response.body() != null) {
//                            List<Note> notes = response.body().getList();
//                            setupUserRecyclerView(notes);
//                            if (notes.isEmpty()) {
//                                handleSearchError(view, "未找到相关笔记");
//                            }
//                        } else {
//                            handleSearchError(view, "搜索失败: " + response.message());
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<ListTResponse<List<Note>>> call, Throwable t) {
//                        showLoading(view, false);
//                        if (t instanceof IOException) {
//                            handleSearchError(view, "网络连接错误，请检查网络设置");
//                        } else {
//                            handleSearchError(view, "搜索服务出错，请稍后重试");
//                        }
//                        t.printStackTrace();
//                    }
//                });
//            } else {
//                showLoading(view, false);
//                handleSearchError(view, "搜索服务未初始化");
//            }
//        };
//
//        handler.postDelayed(searchRunnable, DEBOUNCE_DELAY);
//    }
//
//    private void setupUserRecyclerView(List<Note> notes) {
//        noteList.clear();
//        noteList.addAll(notes);
//        adapter.setData(noteList); // 使用适配器的setData方法更新数据
//
//        View emptyView = currentView.findViewById(R.id.emptyView);
//        if (emptyView != null) {
//            emptyView.setVisibility(notes.isEmpty() ? View.VISIBLE : View.GONE);
//        }
//    }
//
//    private void showPreSearchContent(View view) {
//        view.findViewById(R.id.searchBeforeGroup).setVisibility(View.VISIBLE);
//        view.findViewById(R.id.searchResultContainer).setVisibility(View.GONE);
//        loadAllUsers(view);
//    }
//
//    private void showLoading(View view, boolean show) {
//        View loadingView = view.findViewById(R.id.loadingView);
//        if (loadingView != null) {
//            loadingView.setVisibility(show ? View.VISIBLE : View.GONE);
//        }
//    }
//
//    private void handleSearchError(View view, String message) {
//        View emptyView = view.findViewById(R.id.emptyView);
//        TextView emptyText = view.findViewById(R.id.emptyText);
//
//        if (emptyView != null) {
//            emptyText.setText(message);
//            emptyView.setVisibility(View.VISIBLE);
//        }
//
//        noteList.clear();
//        adapter.notifyDataSetChanged();
//    }
//
//    private boolean isNetworkConnected() {
//        ConnectivityManager connectivityManager =
//                (ConnectivityManager) requireContext().getSystemService(Context.CONNECTIVITY_SERVICE);
//
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
//            android.net.NetworkCapabilities networkCapabilities =
//                    connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
//            return networkCapabilities != null &&
//                    (networkCapabilities.hasTransport(android.net.NetworkCapabilities.TRANSPORT_WIFI) ||
//                            networkCapabilities.hasTransport(android.net.NetworkCapabilities.TRANSPORT_CELLULAR));
//        } else {
//            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
//            return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
//        }
//    }
//}