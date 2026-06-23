package com.example.registerapplication.Adapters.NoteSearch;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.registerapplication.Entity.NoteSearch.HotItem;
import com.example.registerapplication.R;

import java.util.List;

import lombok.NonNull;


public class HotAdapter extends RecyclerView.Adapter<HotAdapter.ViewHolder> {
    private List<HotItem> hotList;
    private OnItemClickListener listener;

    public HotAdapter(List<HotItem> hotList) {
        this.hotList = hotList;
        // 移除设置图片的相关代码
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_hot, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HotItem item = hotList.get(position);
        holder.title.setText(item.getTitle());
        holder.hotCount.setText(String.valueOf(item.getHotCount()));
    }

    @Override
    public int getItemCount() {
        return hotList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView hotCount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.hot_title);
            hotCount = itemView.findViewById(R.id.hot_count);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(hotList.get(getAdapterPosition()));
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(HotItem item);
    }
}