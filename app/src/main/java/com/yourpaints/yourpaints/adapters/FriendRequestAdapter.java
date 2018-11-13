package com.yourpaints.yourpaints.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.yourpaints.yourpaints.R;
import com.yourpaints.yourpaints.UserProfileActivity;
import com.yourpaints.yourpaints.model.UserFriendRequest;
import com.yourpaints.yourpaints.model.UserFriendSuggestion;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.yourpaints.yourpaints.LoginActivity.USERS;
import static com.yourpaints.yourpaints.fragments.RequestsFragment.FRIEND_REQ;

/**
 * Created by pranjul on 17/4/18.
 * An adapter class for binding friend request data with view.
 */

public class FriendRequestAdapter extends RecyclerView.Adapter<FriendRequestAdapter.FriendRequestViewHolder> {

    public static final int VIEW_TYPE_NO_FRIEND_REQ = 10;
    public static final int VIEW_TYPE_FRIEND_REQUEST = 0;
    public static final int VIEW_TYPE_DIVIDER_REQUESTS = 1;
    public static final int VIEW_TYPE_PEOPLE_KNOW = 2;
    private final Context mContext;
    List<UserFriendRequest> friendRequests;
    List<UserFriendSuggestion> friendSuggestions;

    public FriendRequestAdapter(Context mContext) {
        this.mContext = mContext;
        //to avoid null pointer exception
        friendRequests = new ArrayList<>();
        friendSuggestions = new ArrayList<>();
    }

    @Override
    public int getItemViewType(int position) {
        if (friendRequests.size() == 0) {
            if (position == 0)
                return VIEW_TYPE_NO_FRIEND_REQ;
            if (position == 1)
                return VIEW_TYPE_DIVIDER_REQUESTS;
            else return VIEW_TYPE_PEOPLE_KNOW;
        } else {
            if (position < friendRequests.size())
                return VIEW_TYPE_FRIEND_REQUEST;
            else if (position == friendRequests.size())
                return VIEW_TYPE_DIVIDER_REQUESTS;
            else return VIEW_TYPE_PEOPLE_KNOW;
        }
    }

    public void updateFriendSuggestions(List<UserFriendSuggestion> suggestions) {
        friendSuggestions = suggestions;
        notifyDataSetChanged();
    }

    public void updateFriendRequests(List<UserFriendRequest> requests) {
        friendRequests = requests;
        notifyDataSetChanged();
    }

    @Override
    public FriendRequestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId;
        switch (viewType) {
            case VIEW_TYPE_NO_FRIEND_REQ: {
                layoutId = R.layout.layout_no_friend_req;
                break;
            }
            case VIEW_TYPE_FRIEND_REQUEST: {
                layoutId = R.layout.list_item_request;
                break;
            }
            case VIEW_TYPE_DIVIDER_REQUESTS: {
                layoutId = R.layout.divider_requests;
                break;
            }
            case VIEW_TYPE_PEOPLE_KNOW: {
                layoutId = R.layout.item_circle_people;
                break;
            }
            default:
                throw new IllegalArgumentException("Invalid value of viewType for Friend Request: " + viewType);
        }

        View view = LayoutInflater.from(mContext).inflate(layoutId, parent, false);
        view.setFocusable(true);
        return new FriendRequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final FriendRequestViewHolder holder, final int position) {
        if (getItemViewType(position) == VIEW_TYPE_FRIEND_REQUEST) {
            holder.senderName.setText(friendRequests.get(position).getUsername());
            holder.deleteRequest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Snackbar.make(view, "Request will be dismissed.", Snackbar.LENGTH_SHORT).show();
                }
            });

            holder.acceptRequest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Request will be accepted.", Snackbar.LENGTH_SHORT).show();
                }
            });
            holder.senderName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, UserProfileActivity.class);
                    intent.putExtra("USER", friendRequests.get(position).getUsername());
                    mContext.startActivity(intent);
                }
            });

            holder.ivProfilePhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, UserProfileActivity.class);
                    intent.putExtra("USER", friendRequests.get(position).getUsername());
                    mContext.startActivity(intent);
                }
            });
        } else if (getItemViewType(position) == VIEW_TYPE_PEOPLE_KNOW) {
            final int tempPosition;
            if (friendRequests.size() == 0)
                tempPosition = position - 2;
            else
                tempPosition = position - friendRequests.size() - 1;

            holder.messageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
                    dbRef.child(USERS).child(friendSuggestions.get(tempPosition).getUserId())
                            .child(FRIEND_REQ).push().setValue(friendSuggestions.get(tempPosition))
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    holder.messageButton.setText(R.string.req_sent);
                                }
                            });
                    Snackbar.make(view, R.string.req_sent, Snackbar.LENGTH_SHORT).show();

                }
            });
            holder.senderName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, UserProfileActivity.class);
                    intent.putExtra("USER", friendSuggestions.get(tempPosition).getUsername());
                    mContext.startActivity(intent);
                }
            });

            holder.ivProfilePhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, UserProfileActivity.class);
                    intent.putExtra("USER", friendSuggestions.get(tempPosition).getUsername());
                    mContext.startActivity(intent);
                }
            });
            holder.senderName.setText(friendSuggestions.get(tempPosition).getUsername());
        }

    }

    @Override
    public int getItemCount() {
        return (friendRequests.size() == 0 ? 1 : friendRequests.size()) + friendSuggestions.size() + 1;
    }


    public class FriendRequestViewHolder extends RecyclerView.ViewHolder {
        @Nullable
        @BindView(R.id.tv_name_sender)
        public TextView senderName;
        @Nullable
        @BindView(R.id.btn_accept)
        public Button acceptRequest;
        @Nullable
        @BindView(R.id.parent_layout_request)
        public RelativeLayout parentLayout;
        @Nullable
        @BindView(R.id.iv_photo)
        public ImageView ivProfilePhoto;
        @Nullable
        @BindView(R.id.btn_delete)
        public TextView deleteRequest;
        @Nullable
        @BindView(R.id.message_btn)
        public TextView messageButton;

        public FriendRequestViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
