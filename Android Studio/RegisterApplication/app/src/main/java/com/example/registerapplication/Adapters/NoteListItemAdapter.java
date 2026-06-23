package com.example.registerapplication.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.registerapplication.Entity.Data.NoteItem;
import com.example.registerapplication.R;

import java.util.List;

import lombok.NonNull;


//笔记列表适配器
public class NoteListItemAdapter extends RecyclerView.Adapter<NoteListItemAdapter.NoteViewHolder> {
    private final List<NoteItem> noteItems;
    private OnNoteItemClickListener listener;

    public interface OnNoteItemClickListener {
        void onNoteItemClick(NoteItem noteItem);   // 笔记点击事件方法
        void onLikeButtonClick(NoteItem noteItem); // 点赞点击事件方法
    }
    public void setOnNoteItemClickListener(OnNoteItemClickListener listener) { this.listener = listener; }
    public NoteListItemAdapter(List<NoteItem> noteItems) { this.noteItems = noteItems; }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_item, parent, false);
        return new NoteViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        NoteItem noteItem = noteItems.get(position);             // 计算列宽（两列布局）
        int columnWidth = holder.itemView.getResources().getDisplayMetrics().widthPixels / 2;
        int originalWidth = 1080;     // 假设从API获取了图片原始尺寸（实际开发中应从数据模型获取）
        int originalHeight = 1350;
        int displayHeight = ViewUtils.calculateImageHeight(   // 动态计算笔记图片高度
                originalWidth,
                originalHeight,
                columnWidth
        );
        String imageUrl = "http://10.0.2.2:8080/uploads/";          // 加载用户头像
        String uImageUrl = imageUrl + noteItem.getUser().getPortraitImage();
        Glide.with(holder.itemView)
                .load(uImageUrl)
                .placeholder(R.drawable.jz)
                .into(holder.uimage);
        holder.nimage.getLayoutParams().height = displayHeight;     // 设置笔记图片高度

        Glide.with(holder.itemView)                     // 加载图片
                .load(imageUrl + noteItem.getNote().getImageUrls().split(",")[0])
                .override(columnWidth, displayHeight) // 优化内存
                .into(holder.nimage);

        holder.title.setText(noteItem.getNote().getTitle());      // 设置标题和内容,用户名和赞总和
        holder.content.setText(noteItem.getNote().getContent());
        holder.name.setText(noteItem.getUser().getUsername());

        if (noteItem.getLikeSum() == 0) { holder.like_sum.setText("点赞"); }
        else { holder.like_sum.setText(noteItem.getLikeSum().toString()); }
        if (noteItem.getLikeIng()==1) { holder.likeButton.setImageResource(R.drawable.ic_like2);  }
        else { holder.likeButton.setImageResource(R.drawable.ic_like1);   }// 设置为未选中状态图标

        //点击笔记
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) { listener.onNoteItemClick(noteItem); }
            }
        });
        //点击赞
        holder.likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) { listener.onLikeButtonClick(noteItem); }
            }
        });
    }
    @Override
    public int getItemCount() {
        return noteItems.size();
    }
    public static class NoteViewHolder extends RecyclerView.ViewHolder {
        ImageView nimage, uimage;
        TextView title;
        TextView content;
        TextView name;
        TextView like_sum;
        ImageView likeButton;
        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            nimage = itemView.findViewById(R.id.NoteItem_nImage);
            title = itemView.findViewById(R.id.NoteItem_title);
            content = itemView.findViewById(R.id.NoteItem_content);
            uimage = itemView.findViewById(R.id.NoteItem_PImage);
            name = itemView.findViewById(R.id.NoteItem_name);
            like_sum = itemView.findViewById(R.id.NoteItem_like_sum);
            likeButton = itemView.findViewById(R.id.NoteItem_like_button);
        }
    }
}