package com.example.registerapplication.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.registerapplication.Entity.Data.TitleInfo;
import com.example.registerapplication.Entity.Data.UserItem;
import com.example.registerapplication.Entity.User;
import com.example.registerapplication.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import lombok.NonNull;

/**
 * Me界面
 */
public class FragmentAttentin extends Fragment {
    private static final String TAG = "FragmentAttentin";

    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private List<TitleInfo> titleInfos;

    private FragmentAttentinNoteItem fragmentAttentinNoteItem;
    private DrawerLayout theme1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentAttentinNoteItem = new FragmentAttentinNoteItem();

        // 设置该Fragment有选项菜单
        setHasOptionsMenu(true);
    }

    public void refreshNoteList() {
        if (fragmentAttentinNoteItem != null) {
            fragmentAttentinNoteItem.loadNoteData();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.f2_attentin, container, false);

        initToolbar(view);
        initViews(view);
        return view;
    }

    /**
     * 初始化Toolbar
     * @param view 当前视图
     */
    private void initToolbar(View view) {
        // 初始化 Toolbar
        Toolbar toolbar = view.findViewById(R.id.Toolbar2_Attentin);
        if (toolbar != null) {
            AppCompatActivity activity = (AppCompatActivity) requireActivity();
            activity.setSupportActionBar(toolbar);
            ActionBar actionBar = activity.getSupportActionBar();
            if (actionBar != null) {
                actionBar.setTitle("我的关注");
                toolbar.setTitleTextAppearance(requireContext(), R.style.ToolbarTitleBoldStyle);

            }
            toolbar.setNavigationIcon(R.drawable.fragment_2);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        User user = UserItem.getUserItem().getUser();
        if (user != null) {
            setUserInfo(user);
        }
    }

    /**
     * 初始化界面视图
     * @param view 当前视图
     */
    private void initViews(View view) {
        tabLayout = view.findViewById(R.id.attentin_tab_layout);
        tabLayout.setTabGravity(TabLayout.GRAVITY_START);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        viewPager2 = view.findViewById(R.id.attentin_view_Pager2);

        // 初始化主题布局
        theme1 = view.findViewById(R.id.Theme_Fragment2_Attentin); // 替换为实际的布局 ID
        if (theme1 != null) {
            User user = UserItem.getUserItem().getUser();
            if (user != null) {
                updateTheme(user.getTheme());
            }
        }
    }

    public void setUserInfo(User user) {
        if (user != null) {
            String tabLayoutStr = "我的关注, 我的粉丝";
            // 检查 Fragment 是否已经附着到 Activity
            if (isAdded()) {
                setupViewPagerAndTabLayout(tabLayoutStr, user.getUserId()); // 传递用户 ID
            }
        }
    }

    private void setupViewPagerAndTabLayout(String userInterests, Long userId) {
        try {
            String[] interestArray = userInterests.split(", ");

            if (interestArray.length == 0) {
                Log.e(TAG, "标题为空");
                return;
            }
            titleInfos = initInterestTitles(interestArray);

            viewPager2.setAdapter(new FragmentAttentin.MyFragmentStateAdapter(getChildFragmentManager(), getLifecycle(), interestArray, userId));
            new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
                tab.setText(titleInfos.get(position).getTitle());
            }).attach();
            setupTabLayoutListener(tabLayout, viewPager2);
        } catch (Exception e) {
//            Log.e(TAG, "setupViewPagerAndTabLayout: Error setting up ViewPager and TabLayout", e);
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
            FragmentAttentinNoteItem fragment = new FragmentAttentinNoteItem();
            Bundle args = new Bundle();

            // 传递当前标题信息
            args.putString("currentTitle", interestArray[position]);
            args.putLong("userId", userId); // 传递用户 ID
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getItemCount() {
            return titleInfos != null ? titleInfos.size() : 0;
        }
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

    @Override
    public void onResume() {
        super.onResume();
        User user = UserItem.getUserItem().getUser();
        if (user != null) {
            updateTheme(user.getTheme());
        }
    }
}