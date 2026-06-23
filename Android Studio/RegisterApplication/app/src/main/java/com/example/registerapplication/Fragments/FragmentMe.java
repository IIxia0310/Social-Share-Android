package com.example.registerapplication.Fragments;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;


import com.example.registerapplication.Entity.Data.UserItem;
import com.example.registerapplication.Entity.User;
import com.example.registerapplication.R;
import com.example.registerapplication.Response.UserResponse;
import com.example.registerapplication.Service.UserService;
import com.example.registerapplication.View.MeFan;
import com.example.registerapplication.View.MeFollow;
import com.example.registerapplication.View.RevampUser;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import lombok.NonNull;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Me界面
 */
public class FragmentMe extends Fragment {
    private UserService userService;

    private static final ExecutorService executorService = Executors.newFixedThreadPool(3);
    private static final String TAG = "FragmentMe";
    // 作品相关Fragment数组
    private Fragment[] fragments;

    // 布局视图变量（类型与布局一致）
    private LinearLayout user_Bimage; // 背景容器（LinearLayout）
    private ImageView user_PImage;       // 头像（ImageView）
    private TextView userName;            // 用户名
    private TextView userID;              // 用户ID
    private TextView userLocation;        // IP归属地
    private TextView userSignature;       // 个性签名
    private TextView userfollowSum;      // 关注数
    private TextView userfanSum;           // 粉丝数
    private TextView userLikeSum;           // 获赞数
    private Button editInfoButton;        //编辑资料
    private LinearLayout userfollowButton;        //关注按钮
    private LinearLayout userfanButton;        //粉丝按钮
    private LinearLayout userLikeButton;        //获赞和收藏按钮


    private User loadedUser ;
    private Long followSum ;
    private Long fanSum ;
    private Long noteSum ;
    private Long likeSum;
    private Long collectSum ;

    long sanoteSum ;
    long safeLikeSum ;
    long safeCollectSum;

    private ImageDownloadTask currentImageDownloadTask;

    private NestedScrollView nestedScrollView;
    private ViewPager2 viewPager2;
    private TabLayout meWorksTabLayout; // 修改为 TabLayout 类型
    private LinearLayout theme1; // 背景容器（LinearLayout）


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置该Fragment有选项菜单
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.f5_me, container, false);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        userService = retrofit.create(UserService.class);
        initToolbar(view);
        initViews(view);        // 初始化界面视图（类型与布局一致）
        initButtonClickListeners(view);        // 初始化按钮点击事件（传入当前已加载的 view，关键修复点）
        loadUserInfo();        // 初始化用户信息（示例：模拟网络请求获取数据，实际需调用接口）
        initViewPagerAndTabLayout(view);        // 初始化 ViewPager2 和 TabLayout

        return view;
    }

    private void initToolbar(View view) {
        // 初始化 Toolbar
        Toolbar toolbar = view.findViewById(R.id.Toolbar5_Me);
        if (toolbar != null) {
            AppCompatActivity activity = (AppCompatActivity) requireActivity();
            activity.setSupportActionBar(toolbar);
            ActionBar actionBar = activity.getSupportActionBar();
            if (actionBar != null) {
                actionBar.setTitle("我的资料");
                toolbar.setTitleTextAppearance(requireContext(), R.style.ToolbarTitleBoldStyle);

            }
            toolbar.setNavigationIcon(R.drawable.fragment_5);
        }
    }

    /**
     * 初始化界面视图（类型匹配，使用传入的 view）
     */
    private void initViews(View view) {
        theme1 = view.findViewById(R.id.Theme_Fragmentf5_Me);

        user_Bimage = view.findViewById(R.id.me_user_Bimage);
        user_PImage = view.findViewById(R.id.me_user_Pimage);
        userName = view.findViewById(R.id.me_user_name);
        // 用户ID（确保布局中存在该TextView）
        userID = view.findViewById(R.id.me_user_id);
        // 其他信息视图
        userLocation = view.findViewById(R.id.me_user_location);
        userSignature = view.findViewById(R.id.me_user_signature);

        userfollowSum = view.findViewById(R.id.me_follow);
        userfanSum = view.findViewById(R.id.me_fan);
        userLikeSum = view.findViewById(R.id.me_like);
    }

    /**
     * 初始化按钮点击事件（接收已加载的 view，避免使用 requireView()）
     */
    private void initButtonClickListeners(View view) {

        //关注被点击
        userfollowButton = view.findViewById(R.id.me_follow_button);
        userfollowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireActivity(), MeFollow.class);
                intent.putExtra("followSum", followSum);
                requireActivity().startActivity(intent);
            }
        });

        //粉丝被点击
        userfanButton = view.findViewById(R.id.me_fan_button);
        userfanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireActivity(), MeFan.class);
                intent.putExtra("fanSum", fanSum);
                requireActivity().startActivity(intent);
            }
        });
        //获赞和收藏被点击
        userLikeButton = view.findViewById(R.id.me_like_button);
        userLikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.me1_like, null);
                TextView noteCountTextView = dialogView.findViewById(R.id.note_count);
                TextView likeCountTextView = dialogView.findViewById(R.id.like_count);
                TextView collectCountTextView = dialogView.findViewById(R.id.collect_count);
                Button confirmButton = dialogView.findViewById(R.id.confirm_button);

                noteCountTextView.setText(String.valueOf(noteSum));
                likeCountTextView.setText(String.valueOf(likeSum));
                collectCountTextView.setText(String.valueOf(collectSum));

                builder.setView(dialogView);
                AlertDialog dialog = builder.create();
                dialog.show();

                // 通过反射设置对话框宽度，以下是示例代码
                try {
                    Window window = dialog.getWindow();
                    if (window != null) {
                        WindowManager.LayoutParams layoutParams = window.getAttributes();
                        layoutParams.width = (int) (Resources.getSystem().getDisplayMetrics().widthPixels * 0.8); // 设置为屏幕宽度的80%，可按需调整比例
                        window.setAttributes(layoutParams);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                confirmButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });

        //编辑资料
        editInfoButton = view.findViewById(R.id.me_bitton_userRevamp);
        editInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireActivity(), RevampUser.class);
                requireActivity().startActivity(intent);
            }
        });

//        Button settingsButton = view.findViewById(R.id.me_bitton_setting);
//        settingsButton.setOnClickListener(v ->
//                Log.d(TAG, "设置按钮被点击")
//        );
    }


    //获取用户信息
    private void loadUserInfo() {
        UserItem userItem = UserItem.getUserItem();
        User meUser = userItem.getUser();
        if (meUser != null) {
            Call<UserResponse> call = userService.eqUserID(meUser.getUserId());
            call.enqueue(new Callback<UserResponse>() {
                @Override
                public void onResponse(Call<UserResponse> call, retrofit2.Response<UserResponse> response) {
                    ResponseBody error = response.errorBody();
                    if (error == null) {
                        UserResponse userResponse = response.body();
                        if (userResponse != null && userResponse.isSuccess()) {
                            loadedUser = userResponse.getUser();
                            followSum = userResponse.getFollowSum();
                            fanSum = userResponse.getFanSum();
                            noteSum = userResponse.getNoteSum();
                            likeSum = userResponse.getLikeSum();
                            collectSum = userResponse.getCollectSum();
                            UserItem.getUserItem().setUser(loadedUser); // 存入获取的User
                            UserItem.getUserItem().setFollowSum(followSum); // 存入获取的关注数
                            UserItem.getUserItem().setFanSum(fanSum); // 存入获取的粉丝数
                            UserItem.getUserItem().setNoteSum(noteSum); // 存入获取的粉丝数
                            UserItem.getUserItem().setLikeSum(likeSum); // 存入获取的获赞数
                            UserItem.getUserItem().setCollectSum(collectSum); // 存入获取的收藏数

                            // 更新UI
                            requireActivity().runOnUiThread(() -> {
                                userName.setText(loadedUser.getUsername());
                                userID.setText(String.valueOf(loadedUser.getUserId()));
                                userLocation.setText(loadedUser.getLocation());
                                userSignature.setText(loadedUser.getSignature());

                                sanoteSum= noteSum != null ? noteSum : 0;
                                safeLikeSum = likeSum != null ? likeSum : 0;
                                safeCollectSum = collectSum != null ? collectSum : 0;
                                Long S1 = safeLikeSum + safeCollectSum;

                                String followSumText = followSum != null ? String.valueOf(followSum) : "0";
                                String fanSumText = fanSum != null ? String.valueOf(fanSum) : "0";

                                userfollowSum.setText(followSumText);
                                userfanSum.setText(fanSumText);
                                userLikeSum.setText(String.valueOf(S1));

                                String imageUrl = "http://10.0.2.2:8080/uploads/";

                                String userIngBackgroundImage = loadedUser.getBackgroundImage();
                                String BackgroundImageUrl = imageUrl + userIngBackgroundImage;
                                currentImageDownloadTask = new ImageDownloadTask(ImageType.BACKGROUND);
                                currentImageDownloadTask.execute(BackgroundImageUrl);

                                String userIngPortraitImage = loadedUser.getPortraitImage();
                                String PortraitImageUrl = imageUrl + userIngPortraitImage;
                                currentImageDownloadTask = new ImageDownloadTask(ImageType.PORTRAIT);
                                currentImageDownloadTask.execute(PortraitImageUrl);
                            });
                        }
                    } else {
                        Log.d("FragmentMe", ",没有获取到数据");
                    }
                }
                @Override
                public void onFailure(Call<UserResponse> call, Throwable t) {
                    Log.e("Login", "网络请求失败: " + t.getMessage()); }
            });
        } else {
            Log.e(TAG, "onResponse: loadedUser 为 null"); // 新增日志
        }
    }

    /**
     * 初始化 ViewPager2 和 TabLayout
     */
    private void initViewPagerAndTabLayout(View view) {
        nestedScrollView = view.findViewById(R.id.nestedScrollView);
        viewPager2 = view.findViewById(R.id.view_pager);
        meWorksTabLayout = view.findViewById(R.id.me_works_tab_layout); // 修改为 TabLayout 类型
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                int tabLayoutTop = meWorksTabLayout.getTop();
                if (scrollY >= tabLayoutTop) {
                    // 滚动到 me_works_tab_layout 位置，停止 NestedScrollView 滚动
                    nestedScrollView.setNestedScrollingEnabled(false);
                    viewPager2.setUserInputEnabled(true);
                } else {
                    // 未滚动到 me_works_tab_layout 位置，继续 NestedScrollView 滚动
                    nestedScrollView.setNestedScrollingEnabled(true);
                    viewPager2.setUserInputEnabled(false);
                }
            }
        });

        fragments = new Fragment[]{
                new Me1Notes(),
                new Me2Like(),
                new Me3Collect()
        };

        viewPager2.setAdapter(new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                return fragments[position];
            }

            @Override
            public int getItemCount() {
                return fragments.length;
            }
        });

        new TabLayoutMediator(meWorksTabLayout, viewPager2, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("作品");
                    break;
                case 1:
                    tab.setText("赞过");
                    break;
                case 2:
                    tab.setText("收藏");
                    break;
            }
        }).attach();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_me_menu, menu);
        // 隐藏搜索菜单项（根据实际菜单ID调整）
        MenuItem searchItem = menu.findItem(R.id.toolbar3_select);
        if (searchItem != null) {
            searchItem.setVisible(false);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onResume() {
        super.onResume();
        // 重新获取用户信息
        loadUserInfo();

        // 更新主题背景
        User user = UserItem.getUserItem().getUser();
        if (user != null) {
            updateTheme(user.getTheme());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (currentImageDownloadTask != null) {
            currentImageDownloadTask.cancel(true);
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
            try (Response response = client.newCall(request).execute()) {
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
            if (isAdded()) {
                if (bitmap != null) {
                    switch (imageType) {
                        case PORTRAIT:
                            user_PImage.setImageBitmap(bitmap);
                            break;
                        case BACKGROUND:
                            BitmapDrawable bitmapDrawable = new BitmapDrawable(requireContext().getResources(), bitmap);
                            user_Bimage.setBackground(bitmapDrawable);
                            break;
                    }
                }
            }
        }
    }

    private void updateTheme(int theme) {
        if (theme1 != null) {
            switch (theme) {
                case 1:
                    theme1.setBackgroundResource(R.drawable.background1_pink_blue);
                    break;
                case 2:
                    theme1.setBackgroundResource(R.drawable.background2_yellow_blue);
                    break;
                case 3:
                    theme1.setBackgroundResource(R.drawable.background3_pink_yellow);
                    break;
                case 4:
                    theme1.setBackgroundResource(R.drawable.background4_pink_purple);
                    break;
                case 5:
                    theme1.setBackgroundResource(R.drawable.background5_purple_green);
                    break;
                case 6:
                    theme1.setBackgroundResource(R.drawable.background6_green_blue);
                    break;
                default:
                    theme1.setBackgroundResource(R.drawable.background1_pink_blue);
                    break;
            }
        }
    }
}