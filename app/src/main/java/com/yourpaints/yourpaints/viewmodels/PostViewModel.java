package com.yourpaints.yourpaints.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.yourpaints.yourpaints.model.AppDatabase;
import com.yourpaints.yourpaints.model.Event;
import com.yourpaints.yourpaints.model.Post;

import java.util.List;

public class PostViewModel extends AndroidViewModel {

    private LiveData<List<Post>> posts;

    public PostViewModel(@NonNull Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        posts = database.postDao().getAllPosts();
    }

    public LiveData<List<Post>> getPosts() {
        return posts;
    }

}
