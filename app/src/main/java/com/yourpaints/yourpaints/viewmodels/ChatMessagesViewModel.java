package com.yourpaints.yourpaints.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.yourpaints.yourpaints.model.AppDatabase;
import com.yourpaints.yourpaints.model.Message;

import java.util.List;

public class ChatMessagesViewModel extends ViewModel {
    private LiveData<List<Message>> messages;

    public ChatMessagesViewModel(AppDatabase database, String senderId, String receiverId) {
        messages = database.messageDao().getChatMessages(senderId, receiverId);
    }

    public LiveData<List<Message>> getMessages() {
        return messages;
    }
}
