package com.programming.kantech.mygathering.view.fragments;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.programming.kantech.mygathering.R;
import com.programming.kantech.mygathering.provider.Contract_MyGathering;
import com.programming.kantech.mygathering.utils.Constants;
import com.programming.kantech.mygathering.view.ui.Adapter_List_Gatherings;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by patri on 2017-10-24.
 */

public class Fragment_Gathering_List extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor>,
        Adapter_List_Gatherings.GatheringsAdapterOnClickHandler {

    private Adapter_List_Gatherings mGatheringAdapter;

    private int mPosition = RecyclerView.NO_POSITION;


    @BindView(R.id.rv_gatherings)
    RecyclerView rv_gatherings;



    // Define a new interface MainListener that triggers a callback in the host activity
    SelectListener mCallback;

    public void notifyDataChange() {

        Log.i(Constants.TAG, "notifyDataChange");
        getLoaderManager().restartLoader(Constants.GATHERING_DETAIL_LOADER, null, this);

    }

    /**
     * This method is for responding to clicks from our list.
     *
     * @param id The gathering id that was clicked in the list
     */
    @Override
    public void onClick(long id) {

        // Return a uri to the main activity and use it to load the proper details record
        Uri uri = Contract_MyGathering.GatheringEntry.buildGatheringUri(id);
        mCallback.onGatheringSelected(uri);

    }


    // SelectListener interface, calls a method in the host activity
    // after a callback has been triggered
    public interface SelectListener {
        void onGatheringSelected(Uri uri);

        //void refreshGatherings();
    }

    /**
     * Static factory method that
     * initializes the fragment's arguments, and returns the
     * new fragment to the client.
     */
    public static Fragment_Gathering_List newInstance() {
        return new Fragment_Gathering_List();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Get the fragment layout for the details fragment
        final View rootView = inflater.inflate(R.layout.fragment_main_list, container, false);

        ButterKnife.bind(this, rootView);

        return rootView;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        /* setLayoutManager associates the LayoutManager we created above with our RecyclerView */
        rv_gatherings.setLayoutManager(layoutManager);

        /*
         * Use this setting to improve performance if you know that changes in content do not
         * change the child layout size in the RecyclerView
         */
        rv_gatherings.setHasFixedSize(true);

        mGatheringAdapter = new Adapter_List_Gatherings(getContext(), this);

        /* Setting the adapter attaches it to the RecyclerView in our layout. */
        rv_gatherings.setAdapter(mGatheringAdapter);

        // Prepare the loader.  Either re-connect with an existing one,
        // or start a new one.
        getLoaderManager().initLoader(Constants.GATHERING_DETAIL_LOADER, null, this);

    }

    // Override onAttach to make sure that the container activity has implemented the callback
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the host activity has implemented the callback interface
        // If not, it throws an exception
        try {
            mCallback = (Fragment_Gathering_List.SelectListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement DetailsListener");
        }
    }

    /**
     * Instantiate and return a new Loader for the given ID.
     *
     * @param loaderId The ID whose loader is to be created.
     * @param args     Any arguments supplied by the caller.
     * @return Return a new Loader instance that is ready to start loading.
     */
    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {
        switch (loaderId) {
            case Constants.GATHERING_DETAIL_LOADER:
                /* URI for all rows of data in our gatherings table */
                Uri uri = Contract_MyGathering.GatheringEntry.CONTENT_URI;
                /* Sort order: Ascending by name */
                String sortOrder = Contract_MyGathering.GatheringEntry.COLUMN_GATHERING_NAME + " ASC";

                return new android.support.v4.content.CursorLoader(getContext(),
                        uri,
                        Constants.LOADER_GATHERING_DETAIL_COLUMNS,
                        null,
                        null,
                        sortOrder);

            default:
                throw new RuntimeException("Loader Not Implemented: " + loaderId);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        mGatheringAdapter.swapCursor(data);
        if (mPosition == RecyclerView.NO_POSITION) mPosition = 0;

        rv_gatherings.smoothScrollToPosition(mPosition);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }


}
