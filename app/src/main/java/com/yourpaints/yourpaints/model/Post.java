package com.yourpaints.yourpaints.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Entity(tableName = "posts")
public class Post implements Parcelable {

    public static final Creator<Post> CREATOR = new Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel in) {
            return new Post(in);
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };
    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "post_id")
    String postId;
    @ColumnInfo(name = "user_name")
    String userName;
    @ColumnInfo(name = "post_text")
    String postText;
    @ColumnInfo(name = "post_image_url")
    String postImgUrl;
    @ColumnInfo(name = "like_count")
    int likeCount;
    @ColumnInfo(name = "comment_count")
    int commentCount;
    @ColumnInfo(name = "share_count")
    int shareCount;
    @ColumnInfo(name = "created_at")
    long timestamp;
    @ColumnInfo(name = "likes_arr_id")
    ArrayList<String> likesArrayName;
    @ColumnInfo(name = "comments_arr_id")
    ArrayList<String> commentsArrayName;
    @ColumnInfo(name = "comments_arr_text")
    ArrayList<String> commentsArrayText;
    @ColumnInfo(name = "share_arr_id")
    ArrayList<String> shareArrayName;

    public Post(String userName, String postText, String postImgUrl) {
        this.userName = userName;
        this.postId = generatePostId(userName);
        this.postImgUrl = postImgUrl;
        this.postText = postText;
        this.likeCount = 0;
        this.commentCount = 0;
        this.shareCount = 0;
        this.commentsArrayName = new ArrayList<>();
        this.shareArrayName = new ArrayList<>();
        this.likesArrayName = new ArrayList<>();
        this.timestamp = Calendar.getInstance().getTimeInMillis();
    }

    @Ignore
    public Post() {
    }

    @Ignore
    protected Post(Parcel in) {
        postId = in.readString();
        userName = in.readString();
        postText = in.readString();
        postImgUrl = in.readString();
        likeCount = in.readInt();
        commentCount = in.readInt();
        shareCount = in.readInt();
        timestamp = in.readLong();
        likesArrayName = in.createStringArrayList();
        commentsArrayName = in.createStringArrayList();
        commentsArrayText = in.createStringArrayList();
        shareArrayName = in.createStringArrayList();
    }

    private String generatePostId(String userId) {
        StringBuilder builder = new StringBuilder();
        long timeStamp = new Date().getTime();
        builder.append(userId).append(timeStamp);
        return builder.toString();
    }

    public List<String> getLikesArrayName() {
        return likesArrayName;
    }

    public void setLikesArrayName(ArrayList<String> likesArrayName) {
        this.likesArrayName = likesArrayName;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getShareCount() {
        return shareCount;
    }

    public void setShareCount(int shareCount) {
        this.shareCount = shareCount;
    }

    public String getPostImgUrl() {
        return postImgUrl;
    }

    public void setPostImgUrl(String postImgUrl) {
        this.postImgUrl = postImgUrl;
    }

    public String getPostText() {
        return postText;
    }

    public void setPostText(String postText) {
        this.postText = postText;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userId) {
        this.userName = userId;
    }

    public List<String> getCommentsArrayName() {
        return commentsArrayName;
    }

    public void setCommentsArrayName(ArrayList<String> commentsArrayName) {
        this.commentsArrayName = commentsArrayName;
    }

    public List<String> getCommentsArrayText() {
        return commentsArrayText;
    }

    public void setCommentsArrayText(ArrayList<String> commentsArrayText) {
        this.commentsArrayText = commentsArrayText;
    }

    public List<String> getShareArrayName() {
        return shareArrayName;
    }

    public void setShareArrayName(ArrayList<String> shareArrayName) {
        this.shareArrayName = shareArrayName;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(postId);
        parcel.writeString(userName);
        parcel.writeString(postText);
        parcel.writeString(postImgUrl);
        parcel.writeInt(likeCount);
        parcel.writeInt(commentCount);
        parcel.writeInt(shareCount);
        parcel.writeLong(timestamp);
        parcel.writeStringList(likesArrayName);
        parcel.writeStringList(commentsArrayName);
        parcel.writeStringList(commentsArrayText);
        parcel.writeStringList(shareArrayName);
    }
}
