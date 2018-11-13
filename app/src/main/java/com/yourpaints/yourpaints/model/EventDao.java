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
public interface EventDao {

    @Query("SELECT * FROM events")
    LiveData<List<Event>> loadAllEvents();

    @Insert
    void insertEvent(Event event);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateEvent(Event event);

    @Delete
    void deleteEvent(Event event);

    @Query("DELETE FROM events")
    void deleteAllEvents();

    @Query("SELECT * FROM events WHERE event_id = :id")
    LiveData<Event> getEventById(String id);

}
