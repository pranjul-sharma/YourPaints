package com.yourpaints.yourpaints.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.yourpaints.yourpaints.model.AppDatabase;
import com.yourpaints.yourpaints.model.UserFriendRequest;

import java.util.List;

public class UserFriendRequestViewModel extends AndroidViewModel {

    private LiveData<List<UserFriendRequest>> friendRequests;
    public UserFriendRequestViewModel(@NonNull Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        friendRequests = database.userFriendRequestDao().getAllFriendRequests();
    }

    public LiveData<List<UserFriendRequest>> getFriendRequests() {
        return friendRequests;
    }
}
