package com.example.registerapplication.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.registerapplication.Entity.Data.MessageItem;
import com.example.registerapplication.R;

import java.util.List;

import lombok.NonNull;

public class MessageListItemAdapter extends RecyclerView.Adapter<MessageListItemAdapter.MessageViewHolder> {

    private List<MessageItem> messageItems;
    private OnMessageItemClickListener listener;


    public interface OnMessageItemClickListener {
        void onMessageItemClick(MessageItem messageItem);   // 笔记点击事件方法

    }

    public void setOnMessageItemClickListener(OnMessageItemClickListener listener) {
        this.listener = listener;
    }

    public MessageListItemAdapter(List<MessageItem> messageItems) {
        this.messageItems = messageItems;
    }




    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_list_item, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {


        MessageItem messageItem = messageItems.get(position);


        String imageUrl = "http://10.0.2.2:8080/uploads/";
        String uImageUrl = imageUrl + messageItem.getPortraitImage();


        // 加载头像
        Glide.with(holder.itemView.getContext())
                .load(uImageUrl)
                .into(holder.messageImage);
        holder.messageName.setText(messageItem.getName());
        holder.messageContent.setText(messageItem.getContent());
        holder.messageTime.setText(messageItem.getTime());

        // 设置未读消息数量
        Long unreadCount = messageItem.getUnreadSum();
        if (unreadCount > 0) {
            holder.unreadCountText.setVisibility(View.VISIBLE);
            holder.unreadCountText.setText("  "+String.valueOf(unreadCount)+"  ");
        } else {
            holder.unreadCountText.setVisibility(View.GONE);
        }


        //点击笔记
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (listener != null) {
                    listener.onMessageItemClick(messageItem);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return messageItems.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {
        ImageView messageImage;
        TextView messageName;
        TextView messageContent;
        TextView messageTime;
        TextView unreadCountText;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            messageImage = itemView.findViewById(R.id.MessageItem_Image);
            messageName = itemView.findViewById(R.id.MessageItem_name);
            messageContent = itemView.findViewById(R.id.MessageItem_content);
            messageTime = itemView.findViewById(R.id.MessageItem_time);
            unreadCountText = itemView.findViewById(R.id.unread_count_text);
        }
    }
}