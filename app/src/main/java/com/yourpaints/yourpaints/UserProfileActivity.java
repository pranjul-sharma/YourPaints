package com.yourpaints.yourpaints;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.yourpaints.yourpaints.adapters.UserProfileAdapter;
import com.yourpaints.yourpaints.model.AppDatabase;
import com.yourpaints.yourpaints.model.Post;
import com.yourpaints.yourpaints.model.User;
import com.yourpaints.yourpaints.viewmodels.UserPostViewModel;
import com.yourpaints.yourpaints.viewmodels.UserPostViewModelFactory;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserProfileActivity extends AppCompatActivity {

    private final String TAG = "TAG";
    public final String USER = "USER";

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.recycler_post_details)
    RecyclerView recyclerView;

    UserProfileAdapter adapter;
    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        ButterKnife.bind(this);

        toolbar.setTitle(R.string.user_profile);
        setSupportActionBar(toolbar);
        String extra = getIntent().getStringExtra(TAG);
        userName = getIntent().getStringExtra(USER);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setUpViewModel();

        adapter = new UserProfileAdapter(this, extra);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.smoothScrollToPosition(0);
        recyclerView.setAdapter(adapter);
    }

    private void setUpViewModel() {
        UserPostViewModelFactory factory = new UserPostViewModelFactory(AppDatabase.getInstance(this),userName);
        UserPostViewModel viewModel = ViewModelProviders.of(this,factory).get(UserPostViewModel.class);
        viewModel.getUserPosts().observe(this, new Observer<List<Post>>() {
            @Override
            public void onChanged(@Nullable List<Post> posts) {
                adapter.updateUserPosts(posts);
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
