package com.yourpaints.yourpaints.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yourpaints.yourpaints.PostDetailActivity;
import com.yourpaints.yourpaints.R;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.GONE;

/**
 * Created by pranjul on 24/4/18.
 */

public class EventDetailAdapter extends RecyclerView.Adapter<EventDetailAdapter.EventDetailViewHolder> {

    private static final int VIEW_TYPE_EVENT_DETAIL = 0;
    private static final int VIEW_TYPE_EVENT_POSTS = 1;
    private final Context context;

    public EventDetailAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return VIEW_TYPE_EVENT_DETAIL;
        else return VIEW_TYPE_EVENT_POSTS;
    }

    @Override
    public EventDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layout;
        switch (viewType) {
            case VIEW_TYPE_EVENT_DETAIL: {
                layout = R.layout.single_item_event;
                break;
            }
            case VIEW_TYPE_EVENT_POSTS: {
                layout = R.layout.item_post_view;
                break;
            }
            default:
                throw new IllegalArgumentException("Invalid value for viewtype found: " + viewType);
        }

        View view = LayoutInflater.from(context).inflate(layout, parent, false);
        return new EventDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final EventDetailViewHolder holder, int position) {
        if (position != 0) {
            holder.organizer.setText("Organizer Name");
            holder.imageView.setVisibility(GONE);
            holder.postContent.setText("Post about event by author will be published here.");

            holder.postShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Post will be shared when connected to database server for sync.", Snackbar.LENGTH_SHORT).show();
                }
            });

            holder.postComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, PostDetailActivity.class);
                    intent.putExtra("FROM", "EVENTS");
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
        return 16;
    }

    public class EventDetailViewHolder extends RecyclerView.ViewHolder {
        @Nullable
        @BindView(R.id.post_publisher)
        TextView organizer;
        @Nullable
        @BindView(R.id.post_content)
        TextView postContent;
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
        @BindView(R.id.post_content_img)
        ImageView imageView;

        public EventDetailViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
