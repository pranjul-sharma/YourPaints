package com.yourpaints.yourpaints.fragments;


import android.appwidget.AppWidgetManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.yourpaints.yourpaints.AppExecutors;
import com.yourpaints.yourpaints.InfoWidgetProvider;
import com.yourpaints.yourpaints.NetworkUtils;
import com.yourpaints.yourpaints.R;
import com.yourpaints.yourpaints.adapters.HomeAdapter;
import com.yourpaints.yourpaints.model.AppDatabase;
import com.yourpaints.yourpaints.model.Post;
import com.yourpaints.yourpaints.viewmodels.PostViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.animators.SlideInDownAnimator;

import static com.yourpaints.yourpaints.WritePostActivity.USER_POSTS;
import static com.yourpaints.yourpaints.WritePostActivity.USER_POST_PATH;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements HomeAdapter.PostClickListener {


    @BindView(R.id.recycler_view)
    RecyclerView recycler_post;
    HomeAdapter adapter;

    AppExecutors executors;
    AppDatabase appDatabase;
    FirebaseAuth auth;
    FirebaseDatabase database;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(){
        return new HomeFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recycler, container, false);
        ButterKnife.bind(this,view);
        executors = AppExecutors.getInstance();
        appDatabase = AppDatabase.getInstance(getContext());
        adapter = new HomeAdapter( getContext(), this);
        recycler_post.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler_post.setItemAnimator(new SlideInDownAnimator());
        adapter.notifyItemRangeChanged(0,adapter.getItemCount());
        setUpHomeViewModel();

        if (NetworkUtils.isNetworkEnabled(getContext())){
            updatePostAndLoad();
        } else {
            setUpHomeViewModel();
        }
        recycler_post.setFocusable(true);
        recycler_post.setAdapter(adapter);
        setUpHomeViewModel();
        updateWidget();
        return view;
    }

    private void updateWidget() {
        executors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                SharedPreferences preferences = getActivity().getSharedPreferences(getString(R.string.my_prefs), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt(getString(R.string.home_count),appDatabase.postDao().getPostsForWidget().size());
                editor.putInt(getString(R.string.req_count),appDatabase.userFriendRequestDao().getRequestsForWidget().size());
                editor.putInt(getString(R.string.message_count),appDatabase.messageDao().getMessagesForWidget().size());
                editor.apply();
            }
        });
        Intent intent = new Intent(getContext(), InfoWidgetProvider.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int[] ids = AppWidgetManager.getInstance(getActivity())
                .getAppWidgetIds(new ComponentName(getActivity(),InfoWidgetProvider.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,ids);
        getActivity().sendBroadcast(intent);
    }

    public void updatePostAndLoad(){
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        LoadPostAsync loadPostAsync = new LoadPostAsync();
        loadPostAsync.execute();
    }

    public void setUpHomeViewModel(){
        PostViewModel viewModel = ViewModelProviders.of(this).get(PostViewModel.class);
        viewModel.getPosts().observe(this, new Observer<List<Post>>() {
            @Override
            public void onChanged(@Nullable List<Post> posts) {
                if (posts != null) {
                    adapter.updatePosts(posts);
                }
            }
        });
    }
    @Override
    public void onPostItemClick(HomeAdapter.HomeViewHolder viewHolder, int position) {

    }


    public class LoadPostAsync extends AsyncTask<Void,Void,Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            executors.diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    appDatabase.postDao().deleteAllPosts();
                }
            });
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ChildEventListener childEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    for(DataSnapshot data: dataSnapshot.child(USER_POST_PATH).getChildren()){
                        final Post post = data.getValue(Post.class);
                        executors.diskIO().execute(new Runnable() {
                            @Override
                            public void run() {
                                appDatabase.postDao().insertPost(post);
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

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(USER_POSTS);
            reference.addChildEventListener(childEventListener);
            return null;
        }
    }
}
