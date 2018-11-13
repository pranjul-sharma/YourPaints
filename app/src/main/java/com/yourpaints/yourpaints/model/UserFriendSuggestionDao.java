package com.yourpaints.yourpaints.model;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface UserFriendSuggestionDao {

    @Query("SELECT * FROM user_friend_suggestions")
    LiveData<List<UserFriendSuggestion>> getAllUserFriendSuggestions();

    @Insert
    void insertUserFriend(UserFriendSuggestion userFriendSuggestion);

    @Delete
    void deleteUserFriend(UserFriendSuggestion userFriendSuggestion);

    @Query("DELETE FROM user_friend_suggestions")
    void deleteAllUserFriends();
}
