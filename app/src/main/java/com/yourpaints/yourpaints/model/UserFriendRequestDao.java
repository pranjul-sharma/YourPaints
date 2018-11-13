package com.yourpaints.yourpaints.model;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface UserFriendRequestDao {

    @Query("SELECT * FROM user_friend_requests")
    LiveData<List<UserFriendRequest>> getAllFriendRequests();

    @Insert
    void insertUserFriendRequest(UserFriendRequest userFriendRequest);

    @Delete
    void deleteUserFriendRequest(UserFriendRequest userFriendRequest);

    @Query("DELETE FROM user_friend_requests")
    void deleteAllUserFriendRequests();

    @Query("SELECT * FROM user_friend_requests")
    List<UserFriendRequest> getRequestsForWidget();
}
