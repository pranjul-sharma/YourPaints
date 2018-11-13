package com.yourpaints.yourpaints.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yourpaints.yourpaints.R;
import com.yourpaints.yourpaints.adapters.EventsAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.animators.SlideInDownAnimator;

/**
 * Created by pranjul on 24/4/18.
 */

public class EventsFragment extends Fragment {

    @BindView(R.id.recycler_view)
    RecyclerView recycler_post;

    @BindView(R.id.tv_placeholder)
    TextView textView;

    public EventsFragment() {
        // Required empty public constructor
    }

    public static EventsFragment newInstance() {
        return new EventsFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recycler, container, false);

        ButterKnife.bind(this, view);

        textView.setText("No more events available");
        EventsAdapter adapter = new EventsAdapter(getContext());
        recycler_post.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler_post.setFocusable(true);
        recycler_post.setItemAnimator(new SlideInDownAnimator());
        recycler_post.setAdapter(adapter);
        adapter.notifyItemRangeChanged(0,adapter.getItemCount());
        return view;
    }
}


