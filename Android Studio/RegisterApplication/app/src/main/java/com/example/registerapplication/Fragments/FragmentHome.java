package com.example.registerapplication.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.registerapplication.Entity.User;
import com.example.registerapplication.Entity.Data.TitleInfo;
import com.example.registerapplication.Entity.Data.UserItem;
import com.example.registerapplication.R;
import com.example.registerapplication.View.Side1Interests;
import com.example.registerapplication.View.Side2Comment;
import com.example.registerapplication.View.Side3BrowsingHistory;
import com.example.registerapplication.View.Side4RevampPassword;
import com.example.registerapplication.View.Side5SwitchTopic;
import com.example.registerapplication.View.Side6CustomerServiceHelp;
import com.example.registerapplication.View.Side7CommunityConvention;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import lombok.NonNull;

public class FragmentHome extends Fragment implements Side1Interests.OnInterestsUpdatedListener {
    private static final String TAG = "FragmentHome";
    private static final int REQUEST_CODE_INTERESTS = 1;
    private static final int REQUEST_CODE_SWITCH_TOPIC = 2;

    private ImageView iv_mageView;
    private TextView tv_username;
    private TextView tv_signature;

    private TabLayout tabLayout;
    private ViewPager2 viewPager2;

    private List<TitleInfo> titleInfos;
    private FragmentHomeNoteItem fragmentHomeNoteItem;

    private LinearLayout theme1;
    private DrawerLayout theme2;
    private UserItem  userItem = UserItem.getUserItem();
    private User user = userItem.getUser();

    private boolean isCurrentTabRecommendation = false;
    private String previousInterests = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentHomeNoteItem = new FragmentHomeNoteItem();
    }

    public void refreshNoteList() {
        if (fragmentHomeNoteItem != null) {
            fragmentHomeNoteItem.loadNoteData();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.f1_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initToolbar(view);
        initDrawerLayout(view);
        initDrawerLayoutViews(view);
        setupNavigationView(view);

        NavigationView navView = view.findViewById(R.id.nav_view);
        if (navView != null) {
            View headerView = navView.getHeaderView(0);
            if (headerView != null) {
                iv_mageView = headerView.findViewById(R.id.Main2_pImage);
                tv_username = headerView.findViewById(R.id.Main2_name);
                tv_signature = headerView.findViewById(R.id.Main2_signature);
            } else {
                Log.e(TAG, "侧边菜单栏按钮组件为空!");
            }
        } else {
            Log.e(TAG, "侧边菜单栏组件为空!");
        }


        if (user != null) {
            setUserInfo(user);
        }
    }

    private void initToolbar(View view) {
        Toolbar toolbar = view.findViewById(R.id.Toolbar1_Home);
        if (toolbar != null) {
            AppCompatActivity activity = (AppCompatActivity) requireActivity();
            activity.setSupportActionBar(toolbar);
            ActionBar actionBar = activity.getSupportActionBar();
            if (actionBar != null) {
                actionBar.setTitle("分享生活APP~~");
                toolbar.setTitleTextAppearance(requireContext(), R.style.ToolbarTitleBoldStyle);
            }
            toolbar.setNavigationIcon(R.drawable.toolbar_1);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DrawerLayout drawerLayout = view.findViewById(R.id.Theme_Fragment1_Home);
                    if (drawerLayout != null) {
                        drawerLayout.openDrawer(GravityCompat.START);
                    }
                }
            });
        }
    }

    private void initDrawerLayout(View view) {
        DrawerLayout drawerLayout = view.findViewById(R.id.Theme_Fragment1_Home);
        if (drawerLayout != null) {
            AppCompatActivity activity = (AppCompatActivity) requireActivity();
            Toolbar myToolbar = view.findViewById(R.id.Toolbar1_Home);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    activity,
                    drawerLayout,
                    myToolbar,
                    R.string.open,
                    R.string.close
            );
            drawerLayout.addDrawerListener(toggle);
            toggle.syncState();
        }
    }

    private void initDrawerLayoutViews(View view) {
        tabLayout = view.findViewById(R.id.home_tab_layout);
        tabLayout.setTabGravity(TabLayout.GRAVITY_START);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        viewPager2 = view.findViewById(R.id.home_view_Pager2);

        NavigationView navView = view.findViewById(R.id.nav_view);
        View headerView = navView.getHeaderView(0);
        theme1 = headerView.findViewById(R.id.Theme_main2);
        theme2 = view.findViewById(R.id.Theme_Fragment1_Home);

        if (theme1 == null) {
            Log.e(TAG, "theme1 is null");
        }
        if (theme2 == null) {
            Log.e(TAG, "theme2 is null");
        }

        if (user != null) {
            updateTheme(user.getTheme());
        }
    }

    private void setupNavigationView(View view) {
        NavigationView navView = view.findViewById(R.id.nav_view);
        if (navView != null) {
            navView.setNavigationItemSelectedListener(item -> {
                handleNavigationItemClick(item);
                return true;
            });
        } else {
            Log.e(TAG, "侧边栏导航菜单视图为空!");
        }
    }

    private void handleNavigationItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.SideMenu1_myInterests:
                Intent intent1 = new Intent(requireActivity(), Side1Interests.class);
                intent1.putExtra("user_id", user.getUserId());
                startActivityForResult(intent1, REQUEST_CODE_INTERESTS);
                break;
            case R.id.SideMenu2_myComment:
                Intent intent2 = new Intent(requireActivity(), Side2Comment.class);
                startActivity(intent2);
                break;
            case R.id.SideMenu3_browsingHistory:
                Intent intent3 = new Intent(requireActivity(), Side3BrowsingHistory.class);
                startActivity(intent3);
                break;
            case R.id.SideMenu4_revampPassword:
                Intent intent4 = new Intent(requireActivity(), Side4RevampPassword.class);
                startActivity(intent4);
                break;
            case R.id.SideMenu5_Switch_Topic:
                Intent intent5 = new Intent(requireActivity(), Side5SwitchTopic.class);
                startActivityForResult(intent5, REQUEST_CODE_SWITCH_TOPIC);
                break;
            case R.id.SideMenu6_CustomerServiceHelp:
                Intent intent6 = new Intent(requireActivity(), Side6CustomerServiceHelp.class);
                startActivity(intent6);
                break;
            case R.id.SideMenu7_CommunityConvention:
                Intent intent7 = new Intent(requireActivity(), Side7CommunityConvention.class);
                startActivity(intent7);
                break;
        }
    }

    public void setUserInfo(User user) {
        if (user != null) {
            if (tv_username != null) {
                tv_username.setText(user.getUsername());
            }
            if (tv_signature != null) {
                tv_signature.setText(user.getSignature());
            }

            String imageUrl = "http://10.0.2.2:8080/uploads/";
            String userIngPortraitImage = user.getPortraitImage();
            String PortraitImageUrl = imageUrl + userIngPortraitImage;
            if (iv_mageView != null) {
                Glide.with(this)
                        .load(PortraitImageUrl)
                        .placeholder(R.drawable.jz)
                        .error(R.drawable.jz)
                        .into(iv_mageView);
            }

            String tabLayoutStr = "推荐, " + user.getInterests();
            Log.e(TAG, ": Interests=" + user.getInterests());
            Log.e(TAG, ": tabLayout=" + tabLayoutStr);
            if (isAdded()) {
                setupViewPagerAndTabLayout(tabLayoutStr, user.getUserId());
            }
        }
    }

    private void setupViewPagerAndTabLayout(String userInterests, Long userId) {
        try {
            String[] interestArray = userInterests.split(", ");
            if (interestArray.length == 0) {
                Log.e(TAG, "interestArray用户兴趣为空");
                return;
            }
            titleInfos = initInterestTitles(interestArray);
            viewPager2.setAdapter(new MyFragmentStateAdapter(getChildFragmentManager(), getLifecycle(), interestArray, userId));
            new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
                tab.setText(titleInfos.get(position).getTitle());
                if (titleInfos.get(position).getTitle().equals("推荐")) {
                    isCurrentTabRecommendation = false;
                } else {
                    isCurrentTabRecommendation = false;
                }
            }).attach();
            setupTabLayoutListener(tabLayout, viewPager2);
        } catch (Exception e) {
            Log.e(TAG, "setupViewPagerAndTabLayout: Error setting up ViewPager and TabLayout", e);
        }
    }

    private List<TitleInfo> initInterestTitles(String[] interestArray) {
        List<TitleInfo> interestTitles = new ArrayList<>();
        for (String title : interestArray) {
            TitleInfo info = new TitleInfo(title, title);
            Log.d(TAG, "Adding TitleInfo: title=" + info.getTitle() + ", pu_title=" + info.getPu_title());
            interestTitles.add(info);
        }
        return interestTitles;
    }

    private void setupTabLayoutListener(TabLayout tabLayout, ViewPager2 viewPager2) {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition(), false);
                if (tab.getText().equals("推荐")) {
                    isCurrentTabRecommendation = false;
                } else {
                    isCurrentTabRecommendation = false;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // 未选中状态，可根据需要添加逻辑
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // 再次选中状态，可根据需要添加逻辑
            }
        });
    }

    private class MyFragmentStateAdapter extends FragmentStateAdapter {
        private String[] interestArray;
        private Long userId;

        public MyFragmentStateAdapter(@NonNull androidx.fragment.app.FragmentManager fragmentManager, @NonNull androidx.lifecycle.Lifecycle lifecycle, String[] interestArray, Long userId) {
            super(fragmentManager, lifecycle);
            this.interestArray = interestArray;
            this.userId = userId;
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            FragmentHomeNoteItem fragment = new FragmentHomeNoteItem();
            Bundle args = new Bundle();
            args.putString("currentTitle", interestArray[position]);
            args.putLong("userId", userId);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getItemCount() {
            return titleInfos != null? titleInfos.size() : 0;
        }
    }

    @Override
    public void onInterestsUpdated(String interests) {
        if (user != null) {
            user.setInterests(interests);
            setUserInfo(user);
            previousInterests = interests;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_INTERESTS && resultCode == AppCompatActivity.RESULT_OK) {
            if (data != null) {
                String interests = data.getStringExtra("interests");
                user = UserItem.getUserItem().getUser();
                if (user != null) {
                    user.setInterests(interests);
                    setUserInfo(user);
                    onInterestsUpdated(interests);
                } else {
                    Log.e(TAG, "用户数据为空，无法更新兴趣");
                }
            }
        } else if (requestCode == REQUEST_CODE_SWITCH_TOPIC && resultCode == AppCompatActivity.RESULT_OK) {
            if (data != null) {
                int selectedTheme = data.getIntExtra("selectedTheme", 0);
                user = UserItem.getUserItem().getUser();
                if (user != null) {
                    user.setTheme(selectedTheme);
                    updateTheme(selectedTheme);
                }
            }
        }
    }

    private void updateTheme(int theme) {
        if (theme1 != null) {
            switch (theme) {
                case 1:
                    theme1.setBackgroundResource(R.drawable.background1_pink_blue);
                    if (theme2 != null) {
                        theme2.setBackgroundResource(R.drawable.background1_pink_blue);
                    }
                    break;
                case 2:
                    theme1.setBackgroundResource(R.drawable.background2_yellow_blue);
                    if (theme2 != null) {
                        theme2.setBackgroundResource(R.drawable.background2_yellow_blue);
                    }
                    break;
                case 3:
                    theme1.setBackgroundResource(R.drawable.background3_pink_yellow);
                    if (theme2 != null) {
                        theme2.setBackgroundResource(R.drawable.background3_pink_yellow);
                    }
                    break;
                case 4:
                    theme1.setBackgroundResource(R.drawable.background4_pink_purple);
                    if (theme2 != null) {
                        theme2.setBackgroundResource(R.drawable.background4_pink_purple);
                    }
                    break;
                case 5:
                    theme1.setBackgroundResource(R.drawable.background5_purple_green);
                    if (theme2 != null) {
                        theme2.setBackgroundResource(R.drawable.background5_purple_green);
                    }
                    break;
                case 6:
                    theme1.setBackgroundResource(R.drawable.background6_green_blue);
                    if (theme2 != null) {
                        theme2.setBackgroundResource(R.drawable.background6_green_blue);
                    }
                    break;
                default:
                    theme1.setBackgroundResource(R.drawable.background1_pink_blue);
                    if (theme2 != null) {
                        theme2.setBackgroundResource(R.drawable.background1_pink_blue);
                    }
                    break;
            }
        }
    }
}