package com.example.registerapplication.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.registerapplication.Entity.Data.FollowItem;
import com.example.registerapplication.Entity.Follow;
import com.example.registerapplication.Entity.User;
import com.example.registerapplication.R;
import com.example.registerapplication.Response.ListTResponse;
import com.example.registerapplication.Service.ListTService;
import com.example.registerapplication.View.UserDetails;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import lombok.NonNull;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageFollowAdapter extends RecyclerView.Adapter<MessageFollowAdapter.FollowViewHolder> {
    private static final String TAG = "MessageFollowAdapter";

    private List<FollowItem> fanList;
    private ListTService listTService;
    private Long userId;
    private String create_time = getCurrentTime();  // 获取当前时间
    private String date;
    public MessageFollowAdapter(List<FollowItem> fanList, ListTService listTService, Long userId) {
        this.fanList = fanList;
        this.listTService = listTService;
        this.userId = userId;
    }

    @NonNull
    @Override
    public FollowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message1_gview2_follow_item, parent, false);
        return new FollowViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull FollowViewHolder holder, int position) {
        FollowItem followItem = fanList.get(position);
        if (followItem != null) {
            User user = followItem.getUser();
            if (user != null) {
                Context context = holder.itemView.getContext();
                String imageUrl = "http://10.0.2.2:8080/uploads/";
                // 加载用户头像
                String uImageUrl = imageUrl + user.getPortraitImage();
                Glide.with(context)
                        .load(uImageUrl)
                        .placeholder(R.drawable.jz)
                        .into(holder.userImage);
                holder.userName.setText(user.getUsername());

                // 为头像添加点击事件
                holder.userImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (user != null) {
                            Long targetUserId = user.getUserId();

                            Intent intent = new Intent(context, UserDetails.class);
                            // 可以传递数据
                            intent.putExtra("userId", targetUserId);
                            context.startActivity(intent);

                        }
                    }
                });



                // 是否关注
                if (followItem.getFollowIng() == 1) {
                    // 是否互关
                    holder.fanButton.setText("互关");
                    holder.fanButton.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.ic_all6), null, null, null);
                    // 只显示年月日
                    String date = followItem.getCreateTime().substring(0, 10);
                    holder.tiem.setText("Ta关注了你—" + date);

                    if (followItem.getFollowFanIng() == 1) {
                        holder.fanButton.setText("互关");
                        holder.fanButton.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.ic_all6), null, null, null);
                        // 只显示年月日
                        date = followItem.getCreateTime().substring(0, 10);
                        holder.tiem.setText("Ta关注了你—" + date);
                    } else {
                        holder.fanButton.setText("回关");
                        holder.fanButton.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                        // 只显示年月日
                        date = followItem.getCreateTime().substring(0, 10);
                        holder.tiem.setText("Ta关注了你,期待你的回关—" + date);
                    }
                } else {
                    holder.fanButton.setText("回关");
                    holder.fanButton.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    // 只显示年月日
                    String date = followItem.getCreateTime().substring(0, 10);
                    holder.tiem.setText("Ta关注了你,期待你的回关—" + date);
                }

                // 处理关注按钮的点击事件
                holder.fanButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 是否关注
                        if (followItem.getFollowIng() == 1) {
                            // 取消关注
                            holder.fanButton.setText("回关");
                            holder.fanButton.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                            // 只显示年月日
                            date = followItem.getCreateTime().substring(0, 10);
                            holder.tiem.setText("Ta关注了你,期待你的回关—" + date);

                            Call<ListTResponse> call = listTService.delectFollow(userId, followItem.getUser().getUserId());
                            call.enqueue(new Callback<ListTResponse>() {
                                @Override
                                public void onResponse(Call<ListTResponse> call, Response<ListTResponse> response) {
                                    ResponseBody error = response.errorBody();
                                    if (error == null) {
                                        ListTResponse likeResponse = response.body();
                                        if (likeResponse != null && likeResponse.isSuccess()) {
                                            // 更新关注状态
                                            followItem.setFollowIng(0);
                                            // 通知 NoteDetails 更新评论列表
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<ListTResponse> call, Throwable t) {
                                    Log.e("Register", "网络请求失败: " + t.getMessage());
                                    // 恢复关注状态
                                    // 是否关注
                                    if (followItem.getFollowIng() == 1) {
                                        // 是否互关
                                        if (followItem.getFollowFanIng() == 1) {
                                            holder.fanButton.setText("互关");
                                            holder.fanButton.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.ic_all6), null, null, null);
                                            // 只显示年月日
                                            String date = followItem.getCreateTime().substring(0, 10);
                                            holder.tiem.setText("Ta关注了你—" + date);
                                        } else {
                                            holder.fanButton.setText("回关");
                                            holder.fanButton.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                                            // 只显示年月日
                                            date = followItem.getCreateTime().substring(0, 10);
                                            holder.tiem.setText("Ta关注了你,期待你的回关—" + date);
                                        }
                                    } else {
                                        holder.fanButton.setText("回关");
                                        // 只显示年月日
                                        date = followItem.getCreateTime().substring(0, 10);
                                        holder.tiem.setText("Ta关注了你,期待你的回关—" + date);
                                    }

                                    Toast.makeText(v.getContext(), "取消点赞失败，请检查网络连接", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            // 关注
                            holder.fanButton.setText("互关");
                            holder.fanButton.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.ic_all6), null, null, null);
                            // 只显示年月日
                            date = followItem.getCreateTime().substring(0, 10);
                            holder.tiem.setText("Ta关注了你—" + date);

                            Follow follow = new Follow(null, userId, followItem.getUser().getUserId(), create_time);
                            Call<ListTResponse> call = listTService.addFollow(follow);
                            call.enqueue(new Callback<ListTResponse>() {
                                @Override
                                public void onResponse(Call<ListTResponse> call, Response<ListTResponse> response) {
                                    ResponseBody error = response.errorBody();
                                    if (error == null) {
                                        ListTResponse likeResponse = response.body();
                                        if (likeResponse != null && likeResponse.isSuccess()) {
                                            // 更新关注状态
                                            followItem.setFollowIng(1);
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<ListTResponse> call, Throwable t) {
                                    Log.e("Register", "网络请求失败: " + t.getMessage());
                                    // 恢复关注状态
                                    // 是否互关
                                    if (followItem.getFollowFanIng() == 1) {
                                        holder.fanButton.setText("互关");
                                        holder.fanButton.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.ic_all6), null, null, null);
                                        // 只显示年月日
                                        date = followItem.getCreateTime().substring(0, 10);
                                        holder.tiem.setText("Ta关注了你—" + date);
                                    } else {
                                        holder.fanButton.setText("回关");
                                        // 只显示年月日
                                        date = followItem.getCreateTime().substring(0, 10);
                                        holder.tiem.setText("Ta关注了你,期待你的回关—" + date);
                                    }
                                }
                            });
                        }
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return fanList.size();
    }

    public class FollowViewHolder extends RecyclerView.ViewHolder {
        CircleImageView userImage;
        TextView userName;
        TextView tiem;
        Button fanButton;

        public FollowViewHolder(@NonNull View itemView) {
            super(itemView);
            userImage = itemView.findViewById(R.id.Message1Gview2FollowItem_ImageView);
            userName = itemView.findViewById(R.id.Message1Gview2FollowItem_userName);
            tiem = itemView.findViewById(R.id.Message1Gview2FollowItem_time);
            fanButton = itemView.findViewById(R.id.Message1Gview2FollowItem_button);
        }
    }

    // 时间
    private String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // 修改为只获取年月日
        return sdf.format(new Date());
    }
}