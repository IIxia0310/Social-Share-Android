package com.example.registerapplication.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.registerapplication.Entity.User;
import com.example.registerapplication.Entity.Data.TitleInfo;
import com.example.registerapplication.Entity.Data.UserItem;
import com.example.registerapplication.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import lombok.NonNull;

public class Me1Notes extends Fragment {

    private static final String TAG = "Me1Notes";


    private TabLayout tabLayout;
    private ViewPager2 viewPager2;

    private List<TitleInfo> titleInfos;
    private Me1NotesNoteItem me1NotesNoteItem;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        me1NotesNoteItem = new Me1NotesNoteItem();
    }
    public void refreshNoteList() {
        if (me1NotesNoteItem != null) {
            me1NotesNoteItem.loadNoteData();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.me2_notes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initDrawerLayoutViews(view);
        User user = UserItem.getUserItem().getUser();
        if (user != null) {
            setUserInfo(user);
        }

    }

    private void initDrawerLayoutViews(View view) {
        tabLayout = view.findViewById(R.id.Me1Notes_TabLayout);
        tabLayout.setTabGravity(TabLayout.GRAVITY_START);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        viewPager2 = view.findViewById(R.id.Me1Notes_ViewPager2);

    }


    public void setUserInfo(User user) {
        if (user != null) {

            String tabLayoutStr = "公开, 私密, 草稿箱";
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

            viewPager2.setAdapter(new MyFragmentStateAdapter(getChildFragmentManager(), getLifecycle(), interestArray, userId));
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
            Me1NotesNoteItem fragment = new Me1NotesNoteItem();
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

}