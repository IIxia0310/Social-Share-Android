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
import com.example.registerapplication.Entity.User;
import com.example.registerapplication.R;

import java.util.List;

import lombok.NonNull;

public class AdminUserAdapter extends RecyclerView.Adapter<AdminUserAdapter.UserViewHolder> {
    private Context context;
    private List<User> userList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onRevampClick(User user);
        void onDeleteClick(User user);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public AdminUserAdapter(List<User> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.admin_user_item, parent, false);
        return new UserViewHolder(view, listener, userList); // 只保留正确的构造函数调用
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.username.setText(user.getUsername());
        holder.sex.setText(user.getSex());
        holder.userId.setText("" + user.getUserId());
        holder.password.setText("••••••••"); // 安全考虑，不显示真实密码
        holder.birthday.setText(user.getBirthdayTime());
//        holder.createTime.setText(user.getCreateTime());


        String createTime = user.getCreateTime();
        // 提取年月日（前 10 位）
        String formattedDate = createTime.substring(0, 10); // 结果："2023-10-01"
        holder.createTime.setText(formattedDate);



        // 加载头像（使用Glide库）
        String imageUrl = "http://10.0.2.2:8080/uploads/";
        String uImageUrl = imageUrl + user.getPortraitImage();
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
        return userList.size();
    }

    public void updateData(List<User> newNotes) {
        userList.clear();
        userList.addAll(newNotes);
        notifyDataSetChanged();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        private final List<User> userList;
        private final OnItemClickListener listener;

        ImageView avatar;
        TextView username, sex, userId, password, birthday, createTime;
        TextView revampBtn, deleteBtn;


        public UserViewHolder(@NonNull View itemView, OnItemClickListener listener, List<User> userList) {
            super(itemView);
            this.listener = listener;
            this.userList = userList;


            avatar = itemView.findViewById(R.id.user_pImage);
            username = itemView.findViewById(R.id.user_name);
            sex = itemView.findViewById(R.id.user_sex);
            userId = itemView.findViewById(R.id.user_id);
            password = itemView.findViewById(R.id.user_password);
            birthday = itemView.findViewById(R.id.user_birthday);
            createTime = itemView.findViewById(R.id.user_create);
            revampBtn = itemView.findViewById(R.id.user_revamp);
            deleteBtn = itemView.findViewById(R.id.user_delect);
            revampBtn = itemView.findViewById(R.id.user_revamp);
            deleteBtn = itemView.findViewById(R.id.user_delect);

            // 修改按钮点击事件
            revampBtn.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    User currentNote = userList.get(position);
                    if (listener != null) {
                        listener.onRevampClick(currentNote);
                    }
                }
            });

            // 删除按钮点击事件
            deleteBtn.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    User currentNote = userList.get(position);
                    if (listener != null) {
                        listener.onDeleteClick(currentNote);
                    }
                }
            });
        }
    }
}

