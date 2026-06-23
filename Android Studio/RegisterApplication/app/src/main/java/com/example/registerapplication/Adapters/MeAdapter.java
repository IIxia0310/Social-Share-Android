package com.example.registerapplication.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;

import com.example.registerapplication.R;

import java.util.ArrayList;
import java.util.List;

import lombok.NonNull;

public class MeAdapter extends FragmentStateAdapter {

    private static final int NUM_TABS = 3;

    public MeAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return PortfolioFragment.newInstance(position);
    }

    @Override
    public int getItemCount() {
        return NUM_TABS;
    }

    public static class PortfolioFragment extends Fragment {

        private static final String ARG_POSITION = "position";

        public static PortfolioFragment newInstance(int position) {
            PortfolioFragment fragment = new PortfolioFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_POSITION, position);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.note_list, container, false);
            RecyclerView recyclerView = view.findViewById(R.id.NoteList);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

            List<String> data = new ArrayList<>();
            for (int i = 0; i < 20; i++) {
                data.add("Item " + i);
            }

            PortfolioItemAdapter adapter = new PortfolioItemAdapter(data);
            recyclerView.setAdapter(adapter);

            return view;
        }
    }

    public static class PortfolioItemAdapter extends RecyclerView.Adapter<PortfolioItemAdapter.ViewHolder> {

        private final List<String> data;

        public PortfolioItemAdapter(List<String> data) {
            this.data = data;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.textView.setText(data.get(position));
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            TextView textView;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                textView = itemView.findViewById(android.R.id.text1);
            }
        }
    }
}