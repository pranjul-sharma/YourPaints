package com.yourpaints.yourpaints.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yourpaints.yourpaints.PostDetailActivity;
import com.yourpaints.yourpaints.R;
import com.yourpaints.yourpaints.UserProfileActivity;
import com.yourpaints.yourpaints.model.Post;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pranjul on 19/4/18.
 * An Adapter class for user profile view and its data binding.
 */

public class UserProfileAdapter extends RecyclerView.Adapter<UserProfileAdapter.UserProfileViewHolder> {

    private final Context context;
    private final int VIEW_TYPE_USER_INFO = 0;
    private final int VIEW_TYPE_USER_POST = 1;
    private String TAG;

    private List<Post> userPosts;
    public UserProfileAdapter(Context context) {
        this.context = context;
        this.TAG = null;
        this.userPosts = new ArrayList<>();
    }

    public UserProfileAdapter(Context context, String TAG) {
        this.context = context;
        this.TAG = TAG;
        this.userPosts = new ArrayList<>();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == 0) ? VIEW_TYPE_USER_INFO : VIEW_TYPE_USER_POST;
    }

    public void updateUserPosts(List<Post> posts){
        userPosts = posts;
        notifyDataSetChanged();
    }

    @Override
    public UserProfileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId;
        switch (viewType) {
            case VIEW_TYPE_USER_INFO: {
                layoutId = R.layout.single_item_profile;
                break;
            }
            case VIEW_TYPE_USER_POST: {
                layoutId = R.layout.item_post_view;
                break;
            }
            default:
                throw new IllegalArgumentException("Invalid value for viewtype " + viewType);
        }
        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        view.setFocusable(true);
        return new UserProfileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final UserProfileViewHolder holder, final int position) {
        if (position == 0) {

            holder.editProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Edit Profile Functionality is not available yet in app.", Snackbar.LENGTH_SHORT).show();
                }
            });
            if (context instanceof UserProfileActivity || (TAG != null && TAG.equals("Home Fragment")))
                holder.editProfile.setVisibility(View.GONE);
            holder.furtherDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "No further details available for now.", Snackbar.LENGTH_LONG).show();
                }
            });
        } else {
            Post post = null;
            if (userPosts.size() > 0)
                post = userPosts.get(position - 1);

            holder.postShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Post will be shared when connected to database server for sync.", Snackbar.LENGTH_SHORT).show();
                }
            });

            final Post finalPost = post;
            holder.postComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, PostDetailActivity.class);
                    if (finalPost != null)
                        intent.putExtra("POST", finalPost);
                    context.startActivity(intent);
                }
            });

            holder.postLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (holder.postLike.getCurrentTextColor() == context.getResources().getColor(R.color.colorPrimary)) {
                        holder.postLike.setTextColor(context.getResources().getColor(R.color.colorTextSecondary));
                        holder.postLike.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_like_gray, 0, 0, 0);
                    } else {
                        holder.postLike.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        holder.postLike.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_like_blue, 0, 0, 0);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public class UserProfileViewHolder extends RecyclerView.ViewHolder {
        @Nullable
        @BindView(R.id.like_post)
        TextView postLike;

        @Nullable
        @BindView(R.id.comment_post)
        TextView postComment;

        @Nullable
        @BindView(R.id.share_post)
        TextView postShare;

        @Nullable
        @BindView(R.id.profile_see_more)
        TextView furtherDetails;

        @Nullable
        @BindView(R.id.tv_edit_profile)
        TextView editProfile;

        public UserProfileViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
