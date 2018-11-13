package com.yourpaints.yourpaints.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "messages")
public class Message {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "message_id")
    int messageId;
    @ColumnInfo(name="sender_id")
    String senderId;
    @ColumnInfo(name="message_text")
    String messageText;
    @ColumnInfo(name="photo_url")
    String photoUrl;
    @ColumnInfo(name = "sent_at")
    long timestamp;

    public Message(String senderId, String messageText, String photoUrl) {
        this.senderId = senderId;
        this.messageText = messageText;
        this.photoUrl = photoUrl;
        this.timestamp = new Date().getTime();
    }

    public String getMessageText() {
        return messageText;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
