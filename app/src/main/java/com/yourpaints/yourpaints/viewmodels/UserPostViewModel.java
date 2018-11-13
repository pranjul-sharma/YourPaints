package com.yourpaints.yourpaints.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.yourpaints.yourpaints.model.AppDatabase;
import com.yourpaints.yourpaints.model.Event;
import com.yourpaints.yourpaints.model.Post;

import java.util.List;

public class UserPostViewModel extends ViewModel {

    private LiveData<List<Post>> userPosts;

    public UserPostViewModel(AppDatabase database, String username) {
        userPosts = database.postDao().getUserPosts(username);
    }

    public LiveData<List<Post>> getUserPosts() {
        return userPosts;
    }
}
