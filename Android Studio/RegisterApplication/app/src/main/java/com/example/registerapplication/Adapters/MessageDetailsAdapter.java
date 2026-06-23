package com.example.registerapplication.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.registerapplication.Entity.Message;
import com.example.registerapplication.R;

import de.hdodenhof.circleimageview.CircleImageView;
import lombok.NonNull;

import java.util.List;

public class MessageDetailsAdapter extends RecyclerView.Adapter<MessageDetailsAdapter.MessageViewHolder> {

    private static final int TYPE_A = 0;
    private static final int TYPE_B = 1;

    private List<Message> messageList;
    private Context context;

    public MessageDetailsAdapter(Context context, List<Message> messageList) {
        this.context = context;
        this.messageList = messageList;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == TYPE_A) {
            view = LayoutInflater.from(context).inflate(R.layout.message_details_item_a, parent, false);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.message_details_item_b, parent, false);
        }
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = messageList.get(position);

        holder.contentTextView.setText(message.getContent());
        holder.timeTextView.setText(message.getCreateTime());
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messageList.get(position);
        return message.isRead() ? TYPE_B : TYPE_A;
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
}