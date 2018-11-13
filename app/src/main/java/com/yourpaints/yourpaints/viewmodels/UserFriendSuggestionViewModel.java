package com.yourpaints.yourpaints.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.yourpaints.yourpaints.model.AppDatabase;
import com.yourpaints.yourpaints.model.UserFriendSuggestion;

import java.util.List;

public class UserFriendSuggestionViewModel extends AndroidViewModel {

    private LiveData<List<UserFriendSuggestion>> friendSuggestions;

    public UserFriendSuggestionViewModel(@NonNull Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        Log.v("suggestion","retriving data in viewmodel");
        friendSuggestions = database.userFriendSuggestionDao().getAllUserFriendSuggestions();
    }

    public LiveData<List<UserFriendSuggestion>> getFriendSuggestions() {
        return friendSuggestions;
    }
}
