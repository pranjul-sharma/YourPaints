package com.yourpaints.yourpaints.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yourpaints.yourpaints.R;
import com.yourpaints.yourpaints.adapters.MessageAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.animators.SlideInDownAnimator;


/**
 * A simple {@link Fragment} subclass.
 */
public class MessagesFragment extends Fragment {


    @BindView(R.id.recycler_view)
    RecyclerView messageList;
    public MessagesFragment() {
        // Required empty public constructor
    }

    public static MessagesFragment newInstance() {
        return new MessagesFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recycler, container, false);

        ButterKnife.bind(this,view);

        MessageAdapter messageAdapter = new MessageAdapter(getContext());
        messageList.setLayoutManager(new LinearLayoutManager(getContext()));
        messageList.setItemAnimator(new SlideInDownAnimator());
        messageList.setAdapter(messageAdapter);
        messageAdapter.notifyItemRangeChanged(0,messageAdapter.getItemCount());
        return view;
    }
}
