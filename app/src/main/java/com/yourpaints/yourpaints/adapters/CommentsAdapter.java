package com.yourpaints.yourpaints.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yourpaints.yourpaints.R;
import com.yourpaints.yourpaints.model.Post;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.GONE;

/**
 * Created by pranjul on 18/4/18.
 * an adapter for post details activity views.
 */

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentViewHolder> {

    private final Context context;
    private final int VIEW_TYPE_POST = 0;
    private final int VIEW_TYPE_COMMENT = 1;
    private Cursor cursor;
    private String fromStr;

    Post post;
    public CommentsAdapter(Context context) {
        this.context = context;
        this.fromStr = null;
    }

    public CommentsAdapter(Context context, String fromStr, Post post) {
        this.context = context;
        this.fromStr = fromStr;
        this.post = post;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return VIEW_TYPE_POST;
        else return VIEW_TYPE_COMMENT;
    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId;
        switch (viewType) {
            case VIEW_TYPE_POST: {
                layoutId = R.layout.single_item_post;
                break;
            }
            case VIEW_TYPE_COMMENT: {
                layoutId = R.layout.single_comment_layout;
                break;
            }
            default:
                throw new IllegalArgumentException("View type is invalid " + viewType);
        }

        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        view.setFocusable(true);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CommentViewHolder holder, int position) {

        if (fromStr != null && fromStr.equals("EVENTS") && position == 0) {
            holder.organizer.setText("Organizer Name");
            holder.imageView.setVisibility(GONE);
            holder.postContent.setText("Post about event by author will be published here.");
        }
        if (TextUtils.isEmpty(fromStr) && post != null){
            if (getItemViewType(position) == VIEW_TYPE_POST) {
                holder.organizer.setText(post.getUserName());
                holder.postContent.setText(post.getPostText());
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:MM:SS");
                holder.postTimestamp.setText(formatter.format(new Date(post.getTimestamp())));
                if (!TextUtils.isEmpty(post.getPostImgUrl())) {
                    Picasso.get()
                            .load(post.getPostImgUrl())
                            .error(R.mipmap.ic_launcher_fore)
                            .placeholder(R.mipmap.ic_launcher_fore)
                            .into(holder.imageView);
                }
            }
        }
        if (position == 0) {
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
        return 18;
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {
        @Nullable
        @BindView(R.id.name_comment_poster)
        TextView tv_name;
        @Nullable
        @BindView(R.id.comment)
        TextView tv_comment;
        @Nullable
        @BindView(R.id.post_publisher)
        TextView organizer;
        @Nullable
        @BindView(R.id.post_content)
        TextView postContent;
        @Nullable
        @BindView(R.id.image_comment_poster)
        ImageView iv_profile;
        @Nullable
        @BindView(R.id.post_content_img)
        ImageView imageView;
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
        @BindView(R.id.post_timestamp)
        TextView postTimestamp;

        public CommentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

    }
}

