package com.yourpaints.yourpaints.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.yourpaints.yourpaints.model.AppDatabase;
import com.yourpaints.yourpaints.model.Event;

import java.util.List;

public class EventViewModel extends AndroidViewModel {

    private LiveData<List<Event>> events;

    public EventViewModel(@NonNull Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        events = database.eventsDao().loadAllEvents();
    }

    public LiveData<List<Event>> getEvents() {
        return events;
    }
}

