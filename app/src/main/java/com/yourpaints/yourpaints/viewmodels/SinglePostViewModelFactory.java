package com.yourpaints.yourpaints.viewmodels;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.yourpaints.yourpaints.model.AppDatabase;

public class SinglePostViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final AppDatabase database;
    private final String postId;

    public SinglePostViewModelFactory(AppDatabase database, String postId) {
        this.database = database;
        this.postId = postId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new SinglePostViewModel(database, postId);
    }
}
