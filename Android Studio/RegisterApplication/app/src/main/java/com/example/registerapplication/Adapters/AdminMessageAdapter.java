package com.example.registerapplication.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.registerapplication.Entity.Data.MessageItem;
import com.example.registerapplication.Entity.User;
import com.example.registerapplication.R;

import java.util.List;

import lombok.NonNull;

public class AdminMessageAdapter extends RecyclerView.Adapter<AdminMessageAdapter.MessageItemViewHolder> {
    private Context context;
    private List<MessageItem> messageItemList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onRevampClick(MessageItem messageItem);
        void onDeleteClick(MessageItem messageItem);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public AdminMessageAdapter(List<MessageItem> messageItemList) {
        this.messageItemList = messageItemList;
    }

    @NonNull
    @Override
    public MessageItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.admin_massag_item, parent, false);
        return new MessageItemViewHolder(view, listener, messageItemList); // 只保留正确的构造函数调用
    }

    @Override
    public void onBindViewHolder(@NonNull MessageItemViewHolder holder, int position) {



        MessageItem messageItem = messageItemList.get(position);
        holder.username.setText(messageItem.getName());
        holder.id.setText(String.valueOf(messageItem.getUser_id()));

        holder.content.setText(messageItem.getContent());



        String createTime = messageItem.getTime();
        // 提取年月日（前 10 位）
        String formattedDate = createTime.substring(0, 10); // 结果："2023-10-01"
        holder.createTime.setText(formattedDate);



        // 加载头像（使用Glide库）
        String imageUrl = "http://10.0.2.2:8080/uploads/";
        String uImageUrl = imageUrl + messageItem.getPortraitImage();
        Glide.with(holder.itemView)
                .load(uImageUrl)
                .placeholder(R.drawable.jz)
                .into(holder.avatar);
    }



    private int dpToPx(int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    @Override
    public int getItemCount() {
        return messageItemList.size();
    }

    public void updateData(List<MessageItem> newMessageItem) {
        messageItemList.clear();
        messageItemList.addAll(newMessageItem);
        notifyDataSetChanged();
    }

    public static class MessageItemViewHolder extends RecyclerView.ViewHolder {
        private final List<MessageItem> messageItemList;
        private final OnItemClickListener listener;

        ImageView avatar;
        TextView username, id, content, password, birthday, createTime;
        TextView revampBtn, deleteBtn;


        public MessageItemViewHolder(@NonNull View itemView, OnItemClickListener listener, List<MessageItem> messageItemList) {
            super(itemView);
            this.listener = listener;
            this.messageItemList = messageItemList;


            avatar = itemView.findViewById(R.id.message_pImage);
            username = itemView.findViewById(R.id.message_name);
            id = itemView.findViewById(R.id.message_id);
            content = itemView.findViewById(R.id.message_content);
            createTime = itemView.findViewById(R.id.message_create);
            revampBtn = itemView.findViewById(R.id.message_revamp);
            deleteBtn = itemView.findViewById(R.id.message_delect);

            // 修改按钮点击事件
            revampBtn.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    MessageItem currentNote = messageItemList.get(position);
                    if (listener != null) {
                        listener.onRevampClick(currentNote);
                    }
                }
            });

            // 删除按钮点击事件
            deleteBtn.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    MessageItem currentNote = messageItemList.get(position);
                    if (listener != null) {
                        listener.onDeleteClick(currentNote);
                    }
                }
            });
        }
    }
}

