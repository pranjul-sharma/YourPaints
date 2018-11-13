package com.yourpaints.yourpaints.viewmodels;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.yourpaints.yourpaints.model.AppDatabase;

public class ChatMessagesViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final AppDatabase database;
    private final String senderId;
    private final String receiverId;

    public ChatMessagesViewModelFactory(AppDatabase database, String senderId, String receiverId) {
        this.database = database;
        this.senderId = senderId;
        this.receiverId = receiverId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ChatMessagesViewModel(database, senderId, receiverId);
    }
}
