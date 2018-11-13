package com.yourpaints.yourpaints.model;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.util.Log;

@Database(entities = {Event.class, Message.class, Post.class, UserFriendSuggestion.class, UserFriendRequest.class}, version = 1, exportSchema = false)
@TypeConverters({ListConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    private static final String TAG = AppDatabase.class.getSimpleName();
    private static final String DATABASE_NAME = "your_paints_db";
    private static final Object LOCK = new Object();
    private static AppDatabase sInstance;

    public static AppDatabase getInstance(Context context){
        if (sInstance == null){
            synchronized (LOCK) {
                Log.v(TAG,"Creating new Database Instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, AppDatabase.DATABASE_NAME)
                        .build();
            }
        }
        Log.v(TAG,"Getting the database Instance");
        return sInstance;
    }

    public abstract EventDao eventsDao();
    public abstract MessageDao messageDao();
    public abstract PostDao postDao();
    public abstract UserFriendSuggestionDao userFriendSuggestionDao();
    public abstract UserFriendRequestDao userFriendRequestDao();

}
