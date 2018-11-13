package com.yourpaints.yourpaints.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.SupportActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.yourpaints.yourpaints.AppExecutors;
import com.yourpaints.yourpaints.MessageActivity;
import com.yourpaints.yourpaints.NetworkUtils;
import com.yourpaints.yourpaints.R;
import com.yourpaints.yourpaints.SplashActivity;
import com.yourpaints.yourpaints.UserProfileActivity;
import com.yourpaints.yourpaints.adapters.FriendRequestAdapter;
import com.yourpaints.yourpaints.model.AppDatabase;
import com.yourpaints.yourpaints.model.UserFriendRequest;
import com.yourpaints.yourpaints.model.UserFriendSuggestion;
import com.yourpaints.yourpaints.viewmodels.UserFriendRequestViewModel;
import com.yourpaints.yourpaints.viewmodels.UserFriendSuggestionViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.animators.SlideInDownAnimator;

import static com.yourpaints.yourpaints.LoginActivity.USERS;
import static com.yourpaints.yourpaints.adapters.FriendRequestAdapter.VIEW_TYPE_FRIEND_REQUEST;
import static com.yourpaints.yourpaints.adapters.FriendRequestAdapter.VIEW_TYPE_NO_FRIEND_REQ;
import static com.yourpaints.yourpaints.adapters.FriendRequestAdapter.VIEW_TYPE_PEOPLE_KNOW;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RequestsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RequestsFragment extends Fragment {

    public final static String FRIEND_REQ = "friend_req";
    @BindView(R.id.recycler_view)
    RecyclerView friendRequestsList;
    AppExecutors executors;
    AppDatabase database;
    FriendRequestAdapter adapter;

    List<UserFriendRequest> friendRequests;
    List<UserFriendSuggestion> friendSuggestions;

    public RequestsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment RequestsFragment.
     */

    public static RequestsFragment newInstance() {
        return new RequestsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recycler, container, false);
        ButterKnife.bind(this, view);
        executors = AppExecutors.getInstance();
        database = AppDatabase.getInstance(getContext());

        if (NetworkUtils.isNetworkEnabled(getContext())) {
            fetchUpdatedData();
        } else {
            setUpFriendRequestSuggestionViewModel();
        }
        adapter = new FriendRequestAdapter(getContext());
        GridLayoutManager glm = new GridLayoutManager(getContext(), 2);
        glm.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (adapter.getItemViewType(position)) {
                    case VIEW_TYPE_FRIEND_REQUEST:
                    case VIEW_TYPE_NO_FRIEND_REQ:
                    case FriendRequestAdapter.VIEW_TYPE_DIVIDER_REQUESTS:
                        return 2;
                    case VIEW_TYPE_PEOPLE_KNOW:
                        return 1;
                    default:
                        return -1;
                }
            }
        });
        friendRequestsList.setLayoutManager(glm);
        friendRequestsList.setAdapter(adapter);
        friendRequestsList.setItemAnimator(new SlideInDownAnimator());
        return view;
    }

    public void fetchUpdatedData() {

        executors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                database.userFriendSuggestionDao().deleteAllUserFriends();
                database.userFriendRequestDao().deleteAllUserFriendRequests();
            }
        });
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                final UserFriendRequest friendRequest = dataSnapshot.getValue(UserFriendRequest.class);
                executors.diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        database.userFriendRequestDao().insertUserFriendRequest(friendRequest);
                    }
                });
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull final DataSnapshot dataSnapshot) {
                final UserFriendRequest friendRequest = dataSnapshot.getValue(UserFriendRequest.class);
                executors.diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        database.userFriendRequestDao().deleteUserFriendRequest(friendRequest);
                    }
                });
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference friendReqRef = null;
        if (user != null) {
            friendReqRef = FirebaseDatabase.getInstance().getReference().child(USERS).child(user.getUid()).child(FRIEND_REQ);
            friendReqRef.addChildEventListener(childEventListener);
        }

        ChildEventListener friendSuggestionListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String userId = dataSnapshot.getKey();
                if (userId.equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                    return;
                for (DataSnapshot temp : dataSnapshot.getChildren()) {
                    if (temp.getKey().equals(FRIEND_REQ))
                        continue;
                    Log.v("user", temp.toString());
                    String userName = temp.child("userName").getValue(String.class);
                    Log.v("user name", userName);
                    if (TextUtils.isEmpty(userName)) {
                        userName = temp.child("emailAddress").getValue(String.class);
                    }
                    final UserFriendSuggestion friendSuggestion = new UserFriendSuggestion(userId, userName);
                    executors.diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            database.userFriendSuggestionDao().insertUserFriend(friendSuggestion);
                        }
                    });
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        DatabaseReference userSuggestionRef = FirebaseDatabase.getInstance().getReference().child(USERS);
        userSuggestionRef.addChildEventListener(friendSuggestionListener);

        setUpFriendRequestSuggestionViewModel();
    }


    private void setUpFriendRequestSuggestionViewModel() {
        UserFriendRequestViewModel friendRequestViewModel = ViewModelProviders.of(this).get(UserFriendRequestViewModel.class);
        friendRequestViewModel.getFriendRequests().observe(this, new Observer<List<UserFriendRequest>>() {
            @Override
            public void onChanged(@Nullable List<UserFriendRequest> requests) {
                friendRequests = requests;
                adapter.updateFriendRequests(requests);
                adapter.notifyItemRangeChanged(0,adapter.getItemCount());
            }
        });

        UserFriendSuggestionViewModel friendSuggestionViewModel = ViewModelProviders.of(this).get(UserFriendSuggestionViewModel.class);
        friendSuggestionViewModel.getFriendSuggestions().observe(this, new Observer<List<UserFriendSuggestion>>() {
            @Override
            public void onChanged(@Nullable List<UserFriendSuggestion> suggestions) {
                friendSuggestions = suggestions;
                adapter.updateFriendSuggestions(suggestions);
                adapter.notifyItemRangeChanged(0,adapter.getItemCount());
            }
        });


    }

}
