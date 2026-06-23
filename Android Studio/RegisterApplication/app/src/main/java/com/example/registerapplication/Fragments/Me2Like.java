package com.example.registerapplication.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.registerapplication.Adapters.NoteListItemAdapter;
import com.example.registerapplication.Entity.Data.UserItem;
import com.example.registerapplication.Entity.Like;
import com.example.registerapplication.Entity.Data.NoteItem;
import com.example.registerapplication.R;
import com.example.registerapplication.Response.ListTResponse;
import com.example.registerapplication.Service.ListTService;
import com.example.registerapplication.View.NoteDetails;

import org.jetbrains.annotations.Nullable;

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

import static android.app.Activity.RESULT_OK;

/**
 * 点赞笔记列表
 */
public class Me2Like extends Fragment {

    private static final String TAG = "Me2Like";
    private RecyclerView NoteList;
    private NoteListItemAdapter adapter;
    private List<NoteItem> noteList;
    private ListTService listTService;

    private boolean isLoading = false;

    private String currentTitle = "点赞"; // 新增：用于存储当前标题
    private Long userIng= UserItem.getUserItem().getUserlogin(); // 新增：用于存储当前登录用户
    private String create_time = getCurrentTime();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.me2_like, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);


    }



    public void init(View view) {
        // 初始化RecyclerView
        NoteList = view.findViewById(R.id.NoteList);
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
                Toast.makeText(requireContext(), "点击了笔记：" + noteItem.getNote().getTitle(), Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(requireActivity(), NoteDetails.class);
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
                        Toast.makeText(requireContext(), "取消点赞：" + noteItem.getNote().getTitle(), Toast.LENGTH_SHORT).show();
                        Call<ListTResponse> call = listTService.delectLike(userIng, noteItem.getNote().getNoteId());
                        call.enqueue(new Callback<ListTResponse>() {
                            @Override
                            public void onResponse(Call<ListTResponse> call, Response<ListTResponse> response) {
                                ResponseBody error = response.errorBody();
                                if (error == null) {
                                    ListTResponse likeResponse = response.body();
                                    if (likeResponse != null && likeResponse.isSuccess()) {
                                        loadNoteData();
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
                        Toast.makeText(requireContext(), "点赞：" + noteItem.getNote().getTitle(), Toast.LENGTH_SHORT).show();
                        Like like = new Like(null, userIng, noteItem.getNote().getNoteId(), null, create_time);
                        Call<ListTResponse> call = listTService.addLike(like);
                        call.enqueue(new Callback<ListTResponse>() {
                            @Override
                            public void onResponse(Call<ListTResponse> call, Response<ListTResponse> response) {
                                ResponseBody error = response.errorBody();
                                if (error == null) {
                                    ListTResponse likeResponse = response.body();
                                    if (likeResponse != null && likeResponse.isSuccess()) {
                                        loadNoteData();
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

        // 初始化Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        listTService = retrofit.create(ListTService.class);

        // 获取当前标题信息
        if (getArguments() != null) {
            currentTitle = getArguments().getString("currentTitle");
            userIng = getArguments().getLong("userId");

            if (currentTitle != null && userIng != null) {
                Log.d(TAG, "Current title: " + currentTitle);
                Log.d(TAG, "Current userId: " + userIng);

                loadNoteData();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            // 重新加载笔记数据
            loadNoteData();
        }
    }

    public void loadNoteData() {
        isLoading = true;
        if (currentTitle != null) {
            eqNoteData( currentTitle, userIng);
        } else {
            Log.e("Me2Like", "标题 null，无法发起请求");
            // 标题 null，清空适配器
            noteList.clear();
            adapter.notifyDataSetChanged();
            isLoading = false;
        }
    }

    public void eqNoteData(String currentTitle,Long userIng) {
        Call<ListTResponse<List<NoteItem>>> call = listTService.me2me3NoteItems(currentTitle, userIng);
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
                        Log.e("Me2Like", "响应体为空");
                        // 响应体为空，清空适配器
                        noteList.clear();
                        adapter.notifyDataSetChanged();
                        isLoading = false;
                    }
                }
            }

            @Override
            public void onFailure(Call<ListTResponse<List<NoteItem>>> call, Throwable t) {
                Log.e("Me2Like", "请求失败", t);
                // 请求失败，清空适配器
                noteList.clear();
                adapter.notifyDataSetChanged();
                isLoading = false;
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        // 重新获取笔记信息
        loadNoteData();
    }


    // 时间
    private String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }
}