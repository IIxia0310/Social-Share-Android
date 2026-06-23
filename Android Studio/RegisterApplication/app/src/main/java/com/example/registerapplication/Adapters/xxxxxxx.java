package com.example.registerapplication.Adapters;//package com.example.registerapplication.Adapters;
//
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.bumptech.glide.Glide;
//import com.example.registerapplication.Entity.Comment;
//import com.example.registerapplication.Entity.Like;
//import com.example.registerapplication.Entity.dbData.CommentItem;
//import com.example.registerapplication.R;
//
//import java.io.IOException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.List;
//
//import de.hdodenhof.circleimageview.CircleImageView;
//import lombok.NonNull;
//import okhttp3.ResponseBody;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//import com.example.registerapplication.Service.ListTService;
//import com.example.registerapplication.Response.ListTResponse;
//import com.example.registerapplication.View.NoteDetails;
//
//public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
//
//    private List<CommentItem> commentItemList;
//    private ListTService listTService;
//    private Long userId;
//
//    private String create_time = getCurrentTime();
//    private NoteDetails noteDetails;
//
//
//    public CommentAdapter(List<CommentItem> commentItemList, ListTService listTService, Long userId) {
//        this.commentItemList = commentItemList;
//        this.listTService = listTService;
//        this.userId = userId;
//    }
//
//    @NonNull
//    @Override
//    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_commen_list_item, parent, false);
//        return new CommentViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
//        CommentItem commentItem = commentItemList.get(position);
//
//        String imageUrl = "http://10.0.2.2:8080/uploads/";
//        // 加载用户头像
//        String uImageUrl = imageUrl + commentItem.getUser().getPortraitImage();
//        Glide.with(holder.itemView)
//                .load(uImageUrl)
//                .placeholder(R.drawable.jz)
//                .into(holder.userPImage);
//
//        holder.userName.setText(commentItem.getUser().getUsername());
//        holder.comment.setText(commentItem.getComment().getContent());
//        holder.updateTime.setText(commentItem.getComment().getCreateTime());
//        holder.location.setText(commentItem.getUser().getLocation());
//
//
//
//        if (commentItem.getCommentUser() == null) {
//            holder.userCommenUser.setText("");
//        } else if (commentItem.getCommentUser().equals("楼主")) {
//            holder.userCommenUser.setText("楼主");
//        } else if (commentItem.getCommentUser().equals("自己")) {
//            holder.userCommenUser.setText("自己");
//
//            // 显示按钮
//            holder.userDelectButton.setVisibility(View.VISIBLE);
//
//            // 设置按钮点击事件监听器
//            holder.userDelectButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    // 处理按钮点击事件
//                    Toast.makeText(v.getContext(), "删除按钮被点击了", Toast.LENGTH_SHORT).show();
//                    //删除评论和删除评论点赞
//
//
//                    Call<ListTResponse> call = listTService.deleteComment(userId, commentItem.getComment().getCommentId());
//                    call.enqueue(new Callback<ListTResponse>() {
//                        @Override
//                        public void onResponse(Call<ListTResponse> call, Response<ListTResponse> response) {
//                            ResponseBody error = response.errorBody();
//                            if (error == null) {
//                                ListTResponse likeResponse = response.body();
//                                if (likeResponse != null && likeResponse.isSuccess()) {
//                                    // 更新评论列表
//
//                                    if (noteDetails != null) {
//                                        noteDetails.loadData();
//                                    }
//
//                                }
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<ListTResponse> call, Throwable t) {
//                            Log.e("Register", "网络请求失败: " + t.getMessage());
//                            // 恢复点赞状态
//                            holder.likeButton.setImageResource(R.drawable.ic_like2);
//                        }
//                    });
//
//                }
//            });
//        } else {
//            // 隐藏按钮
//            holder.userDelectButton.setVisibility(View.GONE);
//        }
//
//
//
//        if (commentItem.getLikeSum() == 0) {
//            holder.likeSum.setText("");
//        } else {
//            holder.likeSum.setText(String.valueOf(commentItem.getLikeSum()));
//        }
//
//        if (commentItem.getLikeIng()) {
//            holder.likeButton.setImageResource(R.drawable.ic_like2); // 设置为选中状态图标
//        } else {
//            holder.likeButton.setImageResource(R.drawable.ic_like1); // 设置为未选中状态图标
//        }
//
//        holder.likeButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (commentItem.getLikeIng()) {
//                    // 取消评论点赞
//
//                    holder.likeButton.setImageResource(R.drawable.ic_like1);
//
//                    Call<ListTResponse> call = listTService.deleteLikeComment(userId,commentItem.getComment().getCommentId());
//                    call.enqueue(new Callback<ListTResponse>() {
//                        @Override
//                        public void onResponse(Call<ListTResponse> call, Response<ListTResponse> response) {
//                            ResponseBody error = response.errorBody();
//                            if (error == null) {
//                                ListTResponse likeResponse = response.body();
//                                if (likeResponse != null && likeResponse.isSuccess()) {
//                                    // 更新点赞状态
//                                    loadCommentLikeCount(commentItem.getComment().getCommentId(), holder);
//
//                                    commentItem.setLikeIng(false);
//                                    commentItem.setLikeSum(commentItem.getLikeSum() - 1);
//                                    if (commentItem.getLikeSum() == 0) {
//                                        holder.likeSum.setText("");
//                                    } else {
//                                        holder.likeSum.setText(commentItem.getLikeSum().toString());
//                                    }
//                                }
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<ListTResponse> call, Throwable t) {
//                            Log.e("Register", "网络请求失败: " + t.getMessage());
//                            // 恢复点赞状态
//                            holder.likeButton.setImageResource(R.drawable.ic_like2);
//                        }
//                    });
//                } else {
//                    // 点赞
//                    //点击了点赞
//                    holder.likeButton.setImageResource(R.drawable.ic_like2);
//
//                    Like like = new Like(null,userId,null,commentItem.getComment().getCommentId(),create_time);
//                    Call<ListTResponse> call = listTService.addLikeComment(like);
//                    call.enqueue(new Callback<ListTResponse>() {
//                        @Override
//                        public void onResponse(Call<ListTResponse> call, Response<ListTResponse> response) {
//                            ResponseBody error = response.errorBody();
//                            if (error == null) {
//                                ListTResponse likeResponse = response.body();
//                                if (likeResponse != null && likeResponse.isSuccess()) {
//                                    // 更新点赞状态
//                                    commentItem.setLikeIng(true);
//                                    commentItem.setLikeSum(commentItem.getLikeSum() + 1);
//                                    holder.likeSum.setText(commentItem.getLikeSum().toString());
//                                }
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<ListTResponse> call, Throwable t) {
//                            Log.e("Register", "网络请求失败: " + t.getMessage());
//                            // 恢复点赞状态
//                            holder.likeButton.setImageResource(R.drawable.ic_like1);
//                        }
//                    });
//
//
//                }
//            }
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return commentItemList.size();
//    }
//
//    public class CommentViewHolder extends RecyclerView.ViewHolder {
//
//        CircleImageView userPImage;
//        TextView userName;
//        TextView userCommenUser;
//        Button userDelectButton;
//        TextView comment;
//        TextView updateTime;
//        TextView location;
//        ImageView likeButton;
//        TextView likeSum;
//
//
//        public CommentViewHolder(@NonNull View itemView) {
//            super(itemView);
//            userPImage = itemView.findViewById(R.id.NoteCommen_PImage);
//            userName = itemView.findViewById(R.id.NoteCommen_userName);
//            userCommenUser = itemView.findViewById(R.id.NoteCommen_user);
//            userDelectButton = itemView.findViewById(R.id.NoteCommen_comentDelectButton);
//            comment = itemView.findViewById(R.id.NoteCommen_coment);
//            updateTime = itemView.findViewById(R.id.NoteCommen_updateTime);
//            location = itemView.findViewById(R.id.NoteCommen_location);
//            likeButton = itemView.findViewById(R.id.NoteCommen_likeButton);
//            likeSum = itemView.findViewById(R.id.NoteCommen_likeSum);
//        }
//    }
//
//    // 时间
//    private String getCurrentTime() {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        return sdf.format(new Date());
//    }
//
//
//    //移除删除的评论
//    public void removeComment(int position) {
//        if (position >= 0 && position < commentItemList.size()) {
//            commentItemList.remove(position);
//            notifyItemRemoved(position);
//            notifyItemRangeChanged(position, commentItemList.size());
//        }
//    }
//
//
//    //获取指定的评论的点赞数量
//    private void loadCommentLikeCount(Long commentId, CommentViewHolder holder) {
//        Call<ListTResponse<Long>> call = listTService.eqCommentLikeSum(commentId);
//        call.enqueue(new Callback<ListTResponse<Long>>() {
//            @Override
//            public void onResponse(Call<ListTResponse<Long>> call, Response<ListTResponse<Long>> response) {
//                if (response.isSuccessful()) {
//                    ListTResponse<Long> likeCountResponse = response.body();
//                    if (likeCountResponse != null && likeCountResponse.isSuccess()) {
//                        Long likeCount = likeCountResponse.getList();
//                        CommentItem commentItem = commentItemList.get(holder.getAdapterPosition());
//                        commentItem.setLikeSum(likeCount);
//                        if (likeCount > 0) {
//                            commentItem.setLikeIng(true);
//                            holder.likeButton.setImageResource(R.drawable.ic_like2);
//                        } else {
//                            commentItem.setLikeIng(false);
//                            holder.likeButton.setImageResource(R.drawable.ic_like1);
//                        }
//                        holder.likeSum.setText(String.valueOf(likeCount));
//                    }
//                } else {
//                    // 处理失败响应
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ListTResponse<Long>> call, Throwable t) {
//                // 处理网络请求失败
//            }
//        });
//    }
//}