package com.example.registerapplication.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.registerapplication.Adapters.Message1GridViewAdapter;
import com.example.registerapplication.Adapters.MessageListItemAdapter;
import com.example.registerapplication.Entity.Data.InterestsItem;
import com.example.registerapplication.Entity.Data.UserItem;
import com.example.registerapplication.Entity.User;
import com.example.registerapplication.Entity.Data.MessageItem;
import com.example.registerapplication.R;
import com.example.registerapplication.Response.ListTResponse;
import com.example.registerapplication.Service.ListTService;
import com.example.registerapplication.View.Message1GView1Like;
import com.example.registerapplication.View.Message1GView2Follow;
import com.example.registerapplication.View.Message1GView3Comment;
import com.example.registerapplication.View.MessageDetails;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import lombok.NonNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 信息
 */
public class FragmentMessage extends Fragment {

    private static final String TAG = "FragmentMessage";
    private Message1GridViewAdapter messageGridViewAdapter;
    private MessageListItemAdapter adapter;
    private ListTService listTService;

    private boolean isLoading = false;

    private GridView gridView;
    private RecyclerView MessageList;
    private List<MessageItem> messageList;

    private View rootView; // 用于设置主题背景的根视图

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置该Fragment有选项菜单
        setHasOptionsMenu(true);
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.f4_message, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rootView = view; // 假设根视图为传入的view，根据实际情况调整

        // 创建 Retrofit 实例
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/") // 替换为实际的服务器地址
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // 创建 MessageService 实例
        listTService = retrofit.create(ListTService.class);

        initToolbar(view);
        initViews(view);
        initMessage();

        User user = UserItem.getUserItem().getUser();
        if (user != null) {
            updateTheme(user.getTheme());
        }
    }

    private void initToolbar(View view) {
        Toolbar toolbar = view.findViewById(R.id.Toolbar4_Message);
        if (toolbar != null) {
            AppCompatActivity activity = (AppCompatActivity) requireActivity();
            activity.setSupportActionBar(toolbar);
            ActionBar actionBar = activity.getSupportActionBar();
            if (actionBar != null) {
                actionBar.setTitle("消息对话");
                toolbar.setTitleTextAppearance(requireContext(), R.style.ToolbarTitleBoldStyle);

            }
            toolbar.setNavigationIcon(R.drawable.fragment_4);
        }
    }

    private void initViews(View view) {
        gridView = view.findViewById(R.id.Message_gridView);
//        confirmButton = view.findViewById(R.id.btn_confirm);
        List<InterestsItem> dataList = new ArrayList<>();
        dataList.add(new InterestsItem(R.drawable.message_grid_view1, "赞和收藏"));
        dataList.add(new InterestsItem(R.drawable.message_grid_view2, "新增关注"));
        dataList.add(new InterestsItem(R.drawable.message_grid_view3, "收到评论"));
        messageGridViewAdapter = new Message1GridViewAdapter(requireContext(), dataList);
        gridView.setAdapter(messageGridViewAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                messageGridViewAdapter.setSelectedPosition(position);
                view.setBackgroundResource(R.color.colorGray);
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        view.setBackgroundResource(R.drawable.message_grid_view);
                    }
                }, 200);

                Intent intent;
                switch (position) {
                    case 0:
                        intent = new Intent(requireContext(), Message1GView1Like.class);
                        break;
                    case 1:
                        intent = new Intent(requireContext(), Message1GView2Follow.class);
                        break;
                    case 2:
                        intent = new Intent(requireContext(), Message1GView3Comment.class);
                        break;
                    default:
                        return;
                }
                startActivity(intent);
            }
        });

        MessageList = view.findViewById(R.id.Message_list);
        // 设置线性布局管理器
        MessageList.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(requireContext()));


        // 初始化适配器
        messageList = new ArrayList<>();
        adapter = new MessageListItemAdapter(messageList);
        MessageList.setAdapter(adapter);

        adapter.setOnMessageItemClickListener(new MessageListItemAdapter.OnMessageItemClickListener() {
            @Override
            public void onMessageItemClick(MessageItem messageItem) {
                Toast.makeText(requireContext(), "点击了聊天：" + messageItem.getName(), Toast.LENGTH_SHORT).show();


                if (messageItem != null) {
                    // 传递数据
                    Intent intent1 = new Intent(requireActivity(), MessageDetails.class);
                    intent1.putExtra("message_item", messageItem);
                    startActivity(intent1);

                } else {
                    Log.e("FragmentMessage", "messageItem 为 null，无法传递数据");
                }

            }
        });

    }

    private void initMessage() {

        User userIng = UserItem.getUserItem().getUser();

        // 获取私信列表，发起网络请求
        Call<ListTResponse<List<MessageItem>>> call = listTService.messageItem(userIng.getUserId());
        call.enqueue(new Callback<ListTResponse<List<MessageItem>>>() {
            @Override
            public void onResponse(Call<ListTResponse<List<MessageItem>>> call, Response<ListTResponse<List<MessageItem>>> response) {
                if (response.isSuccessful()) {
                    ListTResponse<List<MessageItem>> newNotes = response.body();
                    if (newNotes != null) {
                        List<MessageItem> Messageltem = newNotes.getList();
                        if (Messageltem != null &&!Messageltem.isEmpty()) {
                            messageList.clear();
                            messageList.addAll(Messageltem);
                            Log.d("FragmentMessage", "获取到的消息列表数据：");        // 打印获取到的数据
                            for (MessageItem item : Messageltem) {
                                Log.d("FragmentMessage", item.toString());
                            }
                            adapter.notifyDataSetChanged();   // 通知适配器数据已更新
                        } else {
                            messageList.clear();   // 数据为空，清空适配器
                            adapter.notifyDataSetChanged();
                        }
                        isLoading = false;
                    } else {
                        Log.e("FragmentHomeNoteItem", "响应体为空");
                        // 响应体为空，清空适配器
                        messageList.clear();
                        adapter.notifyDataSetChanged();
                        isLoading = false;
                    }
                }
            }

            @Override
            public void onFailure(Call<ListTResponse<List<MessageItem>>> call, Throwable t) {
                Log.e("FragmentMessage", "请求失败", t);
                // 请求失败，清空适配器
                messageList.clear();
                adapter.notifyDataSetChanged();
                isLoading = false;
            }
        });

    }


    @Override
    public void onResume() {
        super.onResume();
        // 重新获取聊天信息
        initMessage();

        User user = UserItem.getUserItem().getUser();
        if (user != null) {
            updateTheme(user.getTheme());
        }
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_me_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.toolbar3_select);
        if (searchItem != null) {
            searchItem.setVisible(false);
        }
        requireActivity().invalidateOptionsMenu();
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void updateTheme(int theme) {
        if (rootView != null) {
            switch (theme) {
                case 1:
                    rootView.setBackgroundResource(R.drawable.background1_pink_blue);
                    break;
                case 2:
                    rootView.setBackgroundResource(R.drawable.background2_yellow_blue);
                    break;
                case 3:
                    rootView.setBackgroundResource(R.drawable.background3_pink_yellow);
                    break;
                case 4:
                    rootView.setBackgroundResource(R.drawable.background4_pink_purple);
                    break;
                case 5:
                    rootView.setBackgroundResource(R.drawable.background5_purple_green);
                    break;
                case 6:
                    rootView.setBackgroundResource(R.drawable.background6_green_blue);
                    break;
                default:
                    rootView.setBackgroundResource(R.drawable.background1_pink_blue);
                    break;
            }
        }
    }



}