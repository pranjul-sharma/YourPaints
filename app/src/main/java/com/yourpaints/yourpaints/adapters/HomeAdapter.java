package com.yourpaints.yourpaints.adapters;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.transition.Slide;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;
import com.yourpaints.yourpaints.PostDetailActivity;
import com.yourpaints.yourpaints.R;
import com.yourpaints.yourpaints.SplashActivity;
import com.yourpaints.yourpaints.UserHomeActivity;
import com.yourpaints.yourpaints.UserProfileActivity;
import com.yourpaints.yourpaints.WritePostActivity;
import com.yourpaints.yourpaints.model.Post;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pranjul on 17/4/18.
 * An adapter class for binding post data with view.
 */

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeViewHolder> {

    private static final int VIEW_TYPE_WRITE_POST = 0;
    private static final int VIEW_TYPE_NO_POST = 2;
    private static final int VIEW_TYPE_POSTS = 1;
    private final PostClickListener clickListener;
    private int lastPosition = -1;
    private final Context mContext;
    private List<Post> posts;

    public HomeAdapter( Context context, PostClickListener clickListener) {
        this.clickListener = clickListener;
        this.mContext = context;
        posts = new ArrayList<>();
    }

    public interface PostClickListener{
        void onPostItemClick(HomeViewHolder viewHolder, int position);
    }


    public void updatePosts(List<Post> posts){
        this.posts = posts;
        notifyDataSetChanged();
        notifyItemRangeChanged(0,posts.size());
    }

    @Override
    public HomeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        int layoutId;
        switch (viewType) {
            case VIEW_TYPE_WRITE_POST: {
                layoutId = R.layout.home_first_item;
                break;
            }
            case VIEW_TYPE_POSTS: {
                layoutId = R.layout.item_post_view;
                break;
            }
            case VIEW_TYPE_NO_POST: {
                layoutId = R.layout.layout_no_post;
                break;
            }
            default:
                throw new IllegalArgumentException("Invalid value for view type :" + viewType);
        }
        View view = LayoutInflater.from(mContext).inflate(layoutId, parent, false);
        view.setFocusable(true);
        return new HomeViewHolder(view);

    }

    private void callIntent() {
        Intent intent = new Intent(mContext, WritePostActivity.class);
        mContext.startActivity(intent);
    }

    @Override
    public void onBindViewHolder(final HomeViewHolder holder, int position) {

        if (position == 0) {
            holder.tv_write.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callIntent();
                }
            });

            holder.et_write.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callIntent();
                }
            });

            holder.tv_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callIntent();
                }
            });

        } else if (posts.size() > 0 && position != 0){
            final Post post = posts.get(position - 1);
            //wiring up data
            holder.writerName.setText(post.getUserName());
            holder.postContent.setText(post.getPostText());
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:MM:SS");
            holder.timeStamp.setText(dateFormat.format(new Date(post.getTimestamp())));
            if (!TextUtils.isEmpty(post.getPostImgUrl()))
                Picasso.get().load(post.getPostImgUrl())
                        .placeholder(R.mipmap.ic_launcher_fore)
                        .error(R.mipmap.ic_launcher_fore)
                        .into(holder.postPic);


            holder.writerName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, UserProfileActivity.class);
                    intent.putExtra("TAG", "Home Fragment");
                    intent.putExtra("USER", FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                    mContext.startActivity(intent);
                }
            });

            holder.postPic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, PostDetailActivity.class);
                    intent.putExtra("POST",post);
                    mContext.startActivity(intent);
                }
            });

            holder.postContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, PostDetailActivity.class);
                    intent.putExtra("POST",post);
                    mContext.startActivity(intent);
                }
            });

            holder.progressBar.setVisibility(View.INVISIBLE);
            holder.postComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, PostDetailActivity.class);
                    intent.putExtra("POST",post);
                    mContext.startActivity(intent);
                }
            });

            holder.postShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Post will be shared when connected to database server for sync.", Snackbar.LENGTH_SHORT).show();
                }
            });

            holder.postLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view,R.string.time_shortage_msg,Snackbar.LENGTH_SHORT).show();
                    if (holder.postLike.getCurrentTextColor() == mContext.getResources().getColor(R.color.colorPrimary)) {
                        holder.postLike.setTextColor(mContext.getResources().getColor(R.color.colorTextSecondary));
                        holder.postLike.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_like_gray, 0, 0, 0);
                    } else {
                        holder.postLike.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
                        holder.postLike.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_like_blue, 0, 0, 0);
                    }
                }
            });
        }

    }


    @Override
    public int getItemViewType(int position) {
        if (posts.size() == 0){
            if (position == 0)
                return VIEW_TYPE_WRITE_POST;

            else
                return VIEW_TYPE_NO_POST;
        } else {
            if (position == 0)
                return VIEW_TYPE_WRITE_POST;

            else
                return VIEW_TYPE_POSTS;
        }
    }

    @Override
    public int getItemCount() {
        if (posts.size() == 0)
            return 2;
        return posts.size();

    }

    public class HomeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @Nullable
        @BindView(R.id.progress_indicator_for_img)
        public ProgressBar progressBar;
        @Nullable
        @BindView(R.id.post_content_img)
        public ImageView postPic;
        @Nullable
        @BindView(R.id.profile_pic)
        public ImageView posterProfilePic;
        @Nullable
        @BindView(R.id.post_content)
        public TextView postContent;
        @Nullable
        @BindView(R.id.post_timestamp)
        public TextView timeStamp;
        @Nullable
        @BindView(R.id.post_publisher)
        public TextView writerName;
        @Nullable
        @BindView(R.id.like_post)
        public TextView postLike;
        @Nullable
        @BindView(R.id.comment_post)
        public TextView postComment;
        @Nullable
        @BindView(R.id.share_post)
        public TextView postShare;
        @Nullable
        @BindView(R.id.comment_box)
        public EditText commentBox;
        @Nullable
        @BindView(R.id.et_write_post)
        public EditText et_write;
        @Nullable
        @BindView(R.id.post_comment_btn)
        public Button btnPostCommment;
        @Nullable
        @BindView(R.id.tv_add_photo)
        public TextView tv_add;
        @Nullable
        @BindView(R.id.tv_write_post)
        public TextView tv_write;

        public HomeViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            clickListener.onPostItemClick(this, position);
        }
    }
}
