package com.yourpaints.yourpaints.model;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface PostDao {

    @Query("SELECT * FROM posts ORDER BY created_at DESC")
    LiveData<List<Post>> getAllPosts();

    @Insert
    void insertPost(Post post);

    @Delete
    void deletePost(Post post);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updatePost(Post post);

    @Query("DELETE FROM posts")
    void deleteAllPosts();

    @Query("SELECT * FROM posts WHERE post_id = :id")
    LiveData<Post> getPostById(String id);

    @Query("SELECT * FROM posts WHERE user_name = :username ORDER BY created_at DESC")
    LiveData<List<Post>> getUserPosts(String username);

    @Query("SELECT * FROM posts")
    List<Post> getPostsForWidget();
}
