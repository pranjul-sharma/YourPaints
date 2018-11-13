package com.yourpaints.yourpaints.viewmodels;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.yourpaints.yourpaints.model.AppDatabase;

public class UserPostViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final AppDatabase database;
    private final String username;

    public UserPostViewModelFactory(AppDatabase database, String username) {
        this.database = database;
        this.username = username;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new UserPostViewModel(database, username);
    }
}
