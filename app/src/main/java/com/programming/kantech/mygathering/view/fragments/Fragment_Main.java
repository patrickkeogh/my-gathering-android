package com.programming.kantech.mygathering.view.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.programming.kantech.mygathering.R;
import com.programming.kantech.mygathering.data.model.pojo.Gathering_Pojo;
import com.programming.kantech.mygathering.utils.Constants;
import com.programming.kantech.mygathering.view.ui.ImageAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by patri on 2017-10-24.
 */

public class Fragment_Main extends Fragment {

    private ArrayList<Gathering_Pojo> mGatherings;
    private ImageAdapter mImageAdapter;

    @BindView(R.id.gv_gatherings)
    GridView gv_gatherings;

    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout mySwipeRefreshLayout;

    // Define a new interface MainListener that triggers a callback in the host activity
    MainListener mCallback;

    public void notifyDataChange(ArrayList<Gathering_Pojo> gatherings) {
        Log.i(Constants.TAG, "notifyDataChange");
        mGatherings = gatherings;

        mImageAdapter = new ImageAdapter(getContext(), gatherings);

        gv_gatherings.setAdapter(mImageAdapter);

        // Signal SwipeRefreshLayout to end the progress indicator
        mySwipeRefreshLayout.setRefreshing(false);
    }

    // DetailsListener interface, calls a method in the host activity
    // after a callback has been triggered
    public interface MainListener {
        void onBannerClick(Gathering_Pojo gathering);

        void refreshGatherings();
    }

    /**
     * Static factory method that
     * initializes the fragment's arguments, and returns the
     * new fragment to the client.
     */
    public static Fragment_Main newInstance() {
        return new Fragment_Main();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Get the fragment layout for the details fragment
        final View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        ButterKnife.bind(this, rootView);

        return rootView;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



        gv_gatherings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Gathering_Pojo gathering = mImageAdapter.getItem(position);

                Log.i(Constants.TAG, "Gathering:" + gathering.toString());

                mCallback.onBannerClick(gathering);


            }
        });

        /*
         * Sets up a SwipeRefreshLayout.OnRefreshListener that is invoked when the user
         * performs a swipe-to-refresh gesture.
        */
        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Log.i(Constants.TAG, "onRefresh called from SwipeRefreshLayout");

                        // This method performs the actual data-refresh operation.
                        // The method calls setRefreshing(false) when it's finished.
                        mCallback.refreshGatherings();
                    }
                }
        );


    }

    // Override onAttach to make sure that the container activity has implemented the callback
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the host activity has implemented the callback interface
        // If not, it throws an exception
        try {
            mCallback = (Fragment_Main.MainListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement DetailsListener");
        }
    }


}
