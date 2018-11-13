package com.yourpaints.yourpaints.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "user_friend_requests")
public class UserFriendRequest {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "user_id")
    String userId;

    @ColumnInfo(name = "user_name")
    String username;

    public UserFriendRequest(String userId, String username) {
        this.userId = userId;
        this.username = username;
    }

    @Ignore
    public UserFriendRequest(){}

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
