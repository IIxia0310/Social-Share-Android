package com.example.registerapplication.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.registerapplication.Entity.Data.UserItem;
import com.example.registerapplication.Entity.Message;
import com.example.registerapplication.Entity.User;
import com.example.registerapplication.Entity.Data.MessageItem;
import com.example.registerapplication.R;
import com.example.registerapplication.Service.ListTService;

import de.hdodenhof.circleimageview.CircleImageView;
import lombok.NonNull;

import java.util.List;

//私信详情适配器
public class MessageDetailsItemAdapter extends RecyclerView.Adapter<MessageDetailsItemAdapter.MessageViewHolder> {
    private static final int TYPE_SELF_SENT = 0; // 自己发送的消息类型
    private static final int TYPE_OTHER_SENT = 1; // 对方发送的消息类型
    private List<Message> messageList;
    private MessageItem messageItem;
    private Context context;
    private ListTService listTService;
    private Long userId;
    private MessageUpdateListener messageUpdateListener;
    User userIng = UserItem.getUserItem().getUser();
    public MessageDetailsItemAdapter(MessageItem messageItem, List<Message> messageList, Context context) {
        this.messageItem = messageItem;
        this.messageList = messageList;
        this.context = context;
    }
    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == TYPE_SELF_SENT) {
            view = LayoutInflater.from(context).inflate(R.layout.message_details_item_b, parent, false);
        } else { view = LayoutInflater.from(context).inflate(R.layout.message_details_item_a, parent, false); }
        return new MessageViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = messageList.get(position);
        if (message != null) {
            String imageUrl = "http://10.0.2.2:8080/uploads/";
            String uImageUrl;
            if (message.getUserId().equals(userIng.getUserId())) {     // 判断是否是自己发送的消息
                uImageUrl = imageUrl + userIng.getPortraitImage();
            } else { uImageUrl = imageUrl + messageItem.getPortraitImage(); }
            // 加载用户头像
            Glide.with(holder.itemView)
                    .load(uImageUrl)
                    .placeholder(R.drawable.jz)
                    .into(holder.imageView);
            holder.contentTextView.setText(message.getContent());
            holder.timeTextView.setText(message.getCreateTime());
        }
    }
    @Override
    public int getItemCount() {
        return messageList.size();
    }
    @Override
    public int getItemViewType(int position) {
        Message message = messageList.get(position);
        // 判断是否是自己发送的消息，返回对应的类型
        return message.getUserId().equals(userIng.getUserId())? TYPE_SELF_SENT : TYPE_OTHER_SENT;
    }
    public void updateMessageList(List<Message> newMessageList) {
        this.messageList = newMessageList;
        notifyDataSetChanged();
    }
    static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView contentTextView;
        TextView timeTextView;
        CircleImageView imageView;
        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            contentTextView = itemView.findViewById(R.id.message_details_item_content);
            timeTextView = itemView.findViewById(R.id.message_details_item_time);
            imageView = itemView.findViewById(R.id.message_details_item_Image);
        }
    }
    public interface MessageUpdateListener{ void getMessageList();}
}