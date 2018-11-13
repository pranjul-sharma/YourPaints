package com.yourpaints.yourpaints.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.yourpaints.yourpaints.model.AppDatabase;
import com.yourpaints.yourpaints.model.Post;

public class SinglePostViewModel extends ViewModel {
    private LiveData<Post> post;

    public SinglePostViewModel(AppDatabase database, String postId) {
        post = database.postDao().getPostById(postId);
    }

    public LiveData<Post> getPost() {
        return post;
    }
}
