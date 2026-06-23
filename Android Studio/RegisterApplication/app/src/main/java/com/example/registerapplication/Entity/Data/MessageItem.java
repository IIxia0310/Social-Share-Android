package com.example.registerapplication.Entity.Data;

import android.os.Parcel;
import android.os.Parcelable;

public class MessageItem implements Parcelable {
    private String portraitImage; // 头像
    private String name;   //聊天对象姓名
    private Long user_id;    // 聊天对象id
    private int followIng;   // 是否关注聊天对象
    private String content;   //最新聊天内容
    private String time;       //最新聊天时间
    private Long unreadSum;    //与当前用户的未读的信息数量

    public MessageItem(String portraitImage, String name, Long user_id, int followIng, String content, String time, Long unreadSum) {
        this.portraitImage = portraitImage;
        this.name = name;
        this.user_id = user_id;
        this.followIng = followIng;
        this.content = content;
        this.time = time;
        this.unreadSum = unreadSum;
    }

    protected MessageItem(Parcel in) {
        portraitImage = in.readString();
        name = in.readString();
        if (in.readByte() == 0) {
            user_id = null;
        } else {
            user_id = in.readLong();
        }
        followIng = in.readInt();
        content = in.readString();
        time = in.readString();
        if (in.readByte() == 0) {
            unreadSum = null;
        } else {
            unreadSum = in.readLong();
        }
    }

    public static final Creator<MessageItem> CREATOR = new Creator<MessageItem>() {
        @Override
        public MessageItem createFromParcel(Parcel in) {
            return new MessageItem(in);
        }

        @Override
        public MessageItem[] newArray(int size) {
            return new MessageItem[size];
        }
    };

    public String getPortraitImage() {
        return portraitImage;
    }

    public void setPortraitImage(String portraitImage) {
        this.portraitImage = portraitImage;
    }

    public Long getUnreadSum() {
        return unreadSum;
    }

    public void setUnreadSum(Long unreadSum) {
        this.unreadSum = unreadSum;
    }

    public int getFollowIng() {
        return followIng;
    }

    public void setFollowIng(int followIng) {
        this.followIng = followIng;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(portraitImage);
        dest.writeString(name);
        if (user_id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(user_id);
        }
        dest.writeInt(followIng);
        dest.writeString(content);
        dest.writeString(time);
        if (unreadSum == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(unreadSum);
        }
    }
}