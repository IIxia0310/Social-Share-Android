package com.example.registerapplication.Adapters;//package com.example.registerapplication.Adapters;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.bumptech.glide.Glide;
//import com.example.registerapplication.Entity.Like;
//import com.example.registerapplication.Entity.dbData.CommentItem;
//import com.example.registerapplication.R;
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.List;
//
//import de.hdodenhof.circleimageview.CircleImageView;
//import lombok.NonNull;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//import com.example.registerapplication.Response.LikeResponse;
//import com.example.registerapplication.Service.ListTService;
//import com.example.registerapplication.Response.ListTResponse;
//
//public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
//
//    private List<CommentItem> commentItemList;
//    private ListTService listTService;
//    private Long userId;
//
//    private String create_time = getCurrentTime();
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
//        // 加载评论点赞数量
//        loadCommentLikeCount(commentItem.getComment().getCommentId(), holder);
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
//                    holder.likeButton.setImageResource(R.drawable.ic_like1);
//                    Call<ListTResponse> call = listTService.deleteLikeCollect(commentItem.getUser().getUserId(), commentItem.getComment().getCommentId());
//                    call.enqueue(new Callback<ListTResponse>() {
//                        @Override
//                        public void onResponse(Call<ListTResponse> call, Response<ListTResponse> response) {
//                            if (response.isSuccessful()) {
//                                // 成功后再次获取最新点赞数据
//                                loadCommentLikeCount(commentItem.getComment().getCommentId(), holder);
//                            } else {
//                                // 失败时恢复本地数据
//                                commentItem.setLikeIng(true);
//                                commentItem.setLikeSum(commentItem.getLikeSum() + 1);
//                                holder.likeButton.setImageResource(R.drawable.ic_like2);
//                                holder.likeSum.setText(String.valueOf(commentItem.getLikeSum()));
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<ListTResponse> call, Throwable t) {
//                            // 失败时恢复本地数据
//                            commentItem.setLikeIng(true);
//                            commentItem.setLikeSum(commentItem.getLikeSum() + 1);
//                            holder.likeButton.setImageResource(R.drawable.ic_like2);
//                            holder.likeSum.setText(String.valueOf(commentItem.getLikeSum()));
//                        }
//                    });
//                } else {
//                    // 点赞
//                    holder.likeButton.setImageResource(R.drawable.ic_like2);
//                    Like like = new Like(null, commentItem.getUser().getUserId(), null, commentItem.getComment().getCommentId(), create_time);
//                    Call<ListTResponse> call = listTService.addLikeCollect(like);
//                    call.enqueue(new Callback<ListTResponse>() {
//                        @Override
//                        public void onResponse(Call<ListTResponse> call, Response<ListTResponse> response) {
//                            if (response.isSuccessful()) {
//                                // 成功后再次获取最新点赞数据
//                                loadCommentLikeCount(commentItem.getComment().getCommentId(), holder);
//                            } else {
//                                // 失败时恢复本地数据
//                                commentItem.setLikeIng(false);
//                                commentItem.setLikeSum(commentItem.getLikeSum() - 1);
//                                holder.likeButton.setImageResource(R.drawable.ic_like1);
//                                holder.likeSum.setText(String.valueOf(commentItem.getLikeSum()));
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<ListTResponse> call, Throwable t) {
//                            // 失败时恢复本地数据
//                            commentItem.setLikeIng(false);
//                            commentItem.setLikeSum(commentItem.getLikeSum() - 1);
//                            holder.likeButton.setImageResource(R.drawable.ic_like1);
//                            holder.likeSum.setText(String.valueOf(commentItem.getLikeSum()));
//                        }
//                    });
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
//        TextView userNameIng;
//        TextView comment;
//        TextView updateTime;
//        TextView location;
//        ImageView likeButton;
//        TextView likeSum;
//
//        public CommentViewHolder(@NonNull View itemView) {
//            super(itemView);
//            userPImage = itemView.findViewById(R.id.NoteCommen_PImage);
//            userName = itemView.findViewById(R.id.NoteCommen_userName);
//            userNameIng = itemView.findViewById(R.id.NoteCommen_userNameIng);
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