package com.yourpaints.yourpaints.viewmodels;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.yourpaints.yourpaints.model.AppDatabase;

public class SingleEventViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final AppDatabase database;
    private final String eventId;

    public SingleEventViewModelFactory(AppDatabase database, String eventId) {
        this.database = database;
        this.eventId = eventId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new SingleEventViewModel(database, eventId);
    }
}
