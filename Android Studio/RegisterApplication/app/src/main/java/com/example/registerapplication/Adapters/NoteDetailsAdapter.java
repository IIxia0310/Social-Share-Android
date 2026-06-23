package com.example.registerapplication.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.registerapplication.R;

import java.util.List;

import lombok.NonNull;


/**
 * 笔记详情适配器
 */
public class NoteDetailsAdapter extends RecyclerView.Adapter<NoteDetailsAdapter.ImageViewHolder> {

    private Context context;
    private List<String> imageList;

    public NoteDetailsAdapter(Context context, List<String> imageList) {
        this.context = context;
        this.imageList = imageList;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 确保使用正确的布局文件
        View view = LayoutInflater.from(context).inflate(R.layout.note_details_image_item, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        String imageUrl = imageList.get(position);
        // 使用Glide加载图片
        Glide.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.jz)
                .error(R.drawable.jz)
                .into(holder.imageView);
    }
    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            // 修改为布局文件中 ImageView 的正确id
            imageView = itemView.findViewById(R.id.NoteDetailsImage_item);
        }
    }
}