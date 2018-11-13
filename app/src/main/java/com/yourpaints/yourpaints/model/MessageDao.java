package com.yourpaints.yourpaints.model;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface MessageDao {

    @Insert
    void insertMessage(Message message);

    @Delete
    void deleteMessage(Message message);

    @Query("DELETE FROM messages")
    void deleteAllMessages();

    @Query("SELECT * FROM messages")
    List<Message> getMessagesForWidget();

    @Query("SELECT * FROM messages WHERE sender_id = :id or sender_id = :id2")
    LiveData<List<Message>> getChatMessages(String id, String id2);
}
