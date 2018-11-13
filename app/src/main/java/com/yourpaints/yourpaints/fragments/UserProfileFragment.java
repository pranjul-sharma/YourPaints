package com.yourpaints.yourpaints.fragments;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.yourpaints.yourpaints.AppExecutors;
import com.yourpaints.yourpaints.R;
import com.yourpaints.yourpaints.adapters.UserProfileAdapter;
import com.yourpaints.yourpaints.model.AppDatabase;
import com.yourpaints.yourpaints.model.Post;
import com.yourpaints.yourpaints.viewmodels.UserPostViewModel;
import com.yourpaints.yourpaints.viewmodels.UserPostViewModelFactory;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.animators.SlideInDownAnimator;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserProfileFragment extends Fragment {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    UserProfileAdapter adapter;
    public UserProfileFragment() {
        // Required empty public constructor
    }

    public static UserProfileFragment newInstance() {
        return new UserProfileFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recycler, container, false);
        ButterKnife.bind(this,view);

        adapter = new UserProfileAdapter(getContext());
        loadUserPosts();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.smoothScrollToPosition(0);
        recyclerView.setItemAnimator(new SlideInDownAnimator());
        recyclerView.setAdapter(adapter);
        adapter.notifyItemRangeChanged(0,adapter.getItemCount());
        Toast.makeText(getActivity(),R.string.time_shortage_msg,Toast.LENGTH_SHORT).show();
        return view;
    }

    public void loadUserPosts(){
        AppDatabase database = AppDatabase.getInstance(getContext());
        String username = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        UserPostViewModelFactory factory = new UserPostViewModelFactory(database,username);
        UserPostViewModel viewModel = ViewModelProviders.of(this,factory).get(UserPostViewModel.class);
        viewModel.getUserPosts().observe(this, new Observer<List<Post>>() {
            @Override
            public void onChanged(@Nullable List<Post> posts) {
                adapter.updateUserPosts(posts);
                adapter.notifyItemRangeChanged(0,adapter.getItemCount());
            }
        });

    }

}
