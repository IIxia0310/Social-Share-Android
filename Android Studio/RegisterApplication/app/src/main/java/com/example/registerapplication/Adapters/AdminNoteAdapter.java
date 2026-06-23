package com.example.registerapplication.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.registerapplication.Entity.Note;
import com.example.registerapplication.R;

import java.util.List;

import lombok.NonNull;

public class AdminNoteAdapter extends RecyclerView.Adapter<AdminNoteAdapter.NoteViewHolder> {
    private Context context;
    private List<Note> noteList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onRevampClick(Note note);
        void onDeleteClick(Note note);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public AdminNoteAdapter(List<Note> noteList) {
        this.noteList = noteList;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.admin_note_item, parent, false);
        return new NoteViewHolder(view, listener, noteList); // 只保留正确的构造函数调用
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = noteList.get(position);

        // 设置文本内容
        holder.noteTitle.setText(note.getTitle());
        holder.noteContent.setText(note.getContent());

        // 格式化并显示创建时间
        if (note.getCreateTime() != null && note.getCreateTime().length() >= 10) {
            holder.noteCreate.setText(note.getCreateTime().substring(0, 10));
        }

        // 加载图片
        loadImages(holder.imageContainer, note.getImageUrls());
    }

    private void loadImages(LinearLayout container, String imageUrls) {
        // 清空现有图片
        container.removeAllViews();

        if (imageUrls == null || imageUrls.isEmpty()) {
            return;
        }

        // 分割URL字符串（假设用逗号分隔）
        String[] urls = imageUrls.split(",");
        int imageSize = dpToPx(80); // 图片大小80dp

        for (String url : urls) {
            if (url.trim().isEmpty()) continue;

            ImageView imageView = new ImageView(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(imageSize, imageSize);
            params.setMargins(0, 0, dpToPx(8), 0); // 右边距8dp
            imageView.setLayoutParams(params);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            // 使用Glide加载图片
            Glide.with(context)
                    .load("http://10.0.2.2:8080/uploads/" + url.trim())
                    .placeholder(R.drawable.jz)
                    .into(imageView);

            container.addView(imageView);
        }
    }

    private int dpToPx(int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    public void updateData(List<Note> newNotes) {
        noteList.clear();
        noteList.addAll(newNotes);
        notifyDataSetChanged();
    }

    public static class NoteViewHolder extends RecyclerView.ViewHolder {
        private final List<Note> noteList;
        private final OnItemClickListener listener;

        LinearLayout imageContainer;
        TextView noteTitle, noteContent, noteCreate;
        TextView revampBtn, deleteBtn;

        public NoteViewHolder(@NonNull View itemView, OnItemClickListener listener, List<Note> noteList) {
            super(itemView);
            this.listener = listener;
            this.noteList = noteList;


            imageContainer = itemView.findViewById(R.id.note_image);
            noteTitle = itemView.findViewById(R.id.note_title);
            noteContent = itemView.findViewById(R.id.note_content);
            noteCreate = itemView.findViewById(R.id.note_create);
            revampBtn = itemView.findViewById(R.id.note_revamp);
            deleteBtn = itemView.findViewById(R.id.note_delect);

            // 修改按钮点击事件
            revampBtn.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Note currentNote = noteList.get(position);
                    if (listener != null) {
                        listener.onRevampClick(currentNote);
                    }
                }
            });

            // 删除按钮点击事件
            deleteBtn.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Note currentNote = noteList.get(position);
                    if (listener != null) {
                        listener.onDeleteClick(currentNote);
                    }
                }
            });
        }
    }
}