package com.yourpaints.yourpaints.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.yourpaints.yourpaints.model.AppDatabase;
import com.yourpaints.yourpaints.model.Event;

class SingleEventViewModel extends ViewModel {

    private LiveData<Event> event;
    public SingleEventViewModel(AppDatabase database, String eventId) {
        event = database.eventsDao().getEventById(eventId);
    }

    public LiveData<Event> getEvent() {
        return event;
    }
}
