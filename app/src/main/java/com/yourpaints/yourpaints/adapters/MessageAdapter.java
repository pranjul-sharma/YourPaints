package com.yourpaints.yourpaints.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yourpaints.yourpaints.MessageActivity;
import com.yourpaints.yourpaints.R;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by pranjul on 17/4/18.
 * An adapter class for binding messages data with view.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private final Context mContext;

    public MessageAdapter(Context mContext) {
        this.mContext = mContext;
    }


    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_messages, parent, false);
        view.setFocusable(true);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        if (position == 0) {
            holder.senderName.setText("All Together");
            holder.senderProfilePhoto.setImageResource(R.drawable.ic_group);
            holder.lastMessageTextView.setText("Messages for group chat with all active members");
        }
        holder.senderName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, MessageActivity.class);
                mContext.startActivity(intent);
            }
        });
        holder.lastMessageTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, MessageActivity.class);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 8;
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {
        @Nullable
        @BindView(R.id.iv_photo)
        ImageView senderProfilePhoto;
        @Nullable
        @BindView(R.id.tv_name_sender)
        TextView senderName;
        @Nullable
        @BindView(R.id.last_message)
        TextView lastMessageTextView;

        public MessageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

        }

    }
}
