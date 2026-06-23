package com.example.registerapplication.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.registerapplication.Entity.Data.MessageItem;
import com.example.registerapplication.Entity.Data.SideCommentItem;
import com.example.registerapplication.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import lombok.NonNull;

public class Side2CommentListItemAdapter extends RecyclerView.Adapter<Side2CommentListItemAdapter.Side2CommentViewHolder> {

    private List<SideCommentItem> sideCommentItems;
    private OnSide2CommentClickListener listener;


    public interface OnSide2CommentClickListener {
        void onOnSide2CommentItemClick(SideCommentItem sideCommentItem);   // 笔记点击事件方法

    }

    public void setOnSide2CommentItemClickListener(OnSide2CommentClickListener listener) {
        this.listener = listener;
    }

    public Side2CommentListItemAdapter(List<SideCommentItem> sideCommentItems) {
        this.sideCommentItems = sideCommentItems;
    }




    @NonNull
    @Override
    public Side2CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.side2_comment_list_item, parent, false);
        return new Side2CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Side2CommentViewHolder holder, int position) {


        SideCommentItem sideCommentItem = sideCommentItems.get(position);
        // 假设从API获取了图片原始尺寸（实际开发中应从数据模型获取）
        int originalWidth = 1080;
        int originalHeight = 1350;



        String imageUrl = "http://10.0.2.2:8080/uploads/";
        String uImageUrl = imageUrl + sideCommentItem.getUser().getPortraitImage();


        // 加载头像
        Glide.with(holder.itemView.getContext())
                .load(uImageUrl)
                .into(holder.avatarImageView);


        holder.usernameTextView.setText(sideCommentItem.getUser().getUsername());
        holder.contentTextView.setText(sideCommentItem.getComment().getContent());

        String date = sideCommentItem.getComment().getCreateTime().substring(0, 10);
        holder.timeTextView.setText(date);



        if (sideCommentItem.getNoteItem().getNote()!=null){
            // 加载图片
            Glide.with(holder.itemView)
                    .load(imageUrl + sideCommentItem.getNoteItem().getNote().getImageUrls().split(",")[0])
                    .into(holder.imageImageView);
        }else {
            holder.imageImageView.setImageResource(R.drawable.image_bkj);

        }

        //点击笔记
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onOnSide2CommentItemClick(sideCommentItem);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return sideCommentItems.size();
    }



    public class Side2CommentViewHolder extends RecyclerView.ViewHolder {
        CircleImageView avatarImageView;
        TextView usernameTextView;
        TextView contentTextView;
        TextView timeTextView;
        ImageView imageImageView;


        public Side2CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            avatarImageView = itemView.findViewById(R.id.Side2CommentListItem_Image);
            usernameTextView = itemView.findViewById(R.id.Side2CommentListItem_name);
            contentTextView = itemView.findViewById(R.id.Side2CommentListItem_content);
            timeTextView = itemView.findViewById(R.id.Side2CommentListItem_time);
            imageImageView = itemView.findViewById(R.id.Side2CommentListItem_nImage);

        }
    }
}