package com.programming.kantech.mygathering.view.fragments;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.programming.kantech.mygathering.R;
import com.programming.kantech.mygathering.data.model.pojo.Gathering_Pojo;
import com.programming.kantech.mygathering.provider.Contract_MyGathering;
import com.programming.kantech.mygathering.utils.Constants;
import com.programming.kantech.mygathering.utils.Utils_ContentValues;
import com.programming.kantech.mygathering.utils.Utils_DateFormatting;
import com.programming.kantech.mygathering.utils.Utils_General;
import com.squareup.okhttp.internal.Util;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by patrick keogh on 2017-10-24.
 *
 */

public class Fragment_Main_Details extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private boolean mGatheringIsFav = false;
    private Gathering_Pojo mGathering;
    private int mLoaderId = 0;

    private Uri mSelectedUri;


    @BindView(R.id.iv_gathering_banner)
    ImageView iv_gathering_banner;

    @BindView(R.id.iv_favorite)
    ImageView iv_favorite;

    @BindView(R.id.tv_gathering_name)
    TextView tv_gathering_name;

    @BindView(R.id.tv_gathering_description)
    TextView tv_gathering_description;

    @BindView(R.id.tv_gathering_notes)
    TextView tv_gathering_notes;

    @BindView(R.id.tv_gathering_start_date)
    TextView tv_gathering_start_date;

    @BindView(R.id.tv_gathering_end_date)
    TextView tv_gathering_end_date;

    @BindView(R.id.tv_gathering_location)
    TextView tv_gathering_location;

    @BindView(R.id.tv_gathering_city)
    TextView tv_gathering_city;

    @BindView(R.id.tv_gathering_country)
    TextView tv_gathering_country;

    @BindView(R.id.tv_gathering_topic)
    TextView tv_gathering_topic;

    @BindView(R.id.tv_gathering_type)
    TextView tv_gathering_type;

    // Define a new interface MainListener that triggers a callback in the host activity
    DetailsListener mCallback;

    // DetailsListener interface, calls a method in the host activity
    // after a callback has been triggered
    public interface DetailsListener {

        void refreshFavsList();
        void showFavMenuItem(boolean b);
        void showSearchMenuItem(boolean b);
        void setActionBarHomeButton(boolean b);
    }

    /**
     * Static factory method that
     * initializes the fragment's arguments, and returns the
     * new fragment to the client.
     */
    public static Fragment_Main_Details newInstance(Uri uri, int loaderId) {

        Fragment_Main_Details f = new Fragment_Main_Details();
        Bundle args = new Bundle();

        // Add any required arguments for start up - None needed right now
        args.putString(Constants.EXTRA_DETAILS_URI, uri.toString());
        args.putInt(Constants.EXTRA_LOADER_ID, loaderId);

        f.setArguments(args);
        return f;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Get the fragment layout for the details fragment
        final View rootView = inflater.inflate(R.layout.fragment_main_details, container, false);

        ButterKnife.bind(this, rootView);

        // Load the saved state if there is one
        if (savedInstanceState != null) {
            //Log.i(Constants.TAG, "Fragment_Step savedInstanceState is not null");
            if (savedInstanceState.containsKey(Constants.STATE_DETAILS_URI)) {
                //Log.i(Constants.TAG, "we found the step key in savedInstanceState");
                mSelectedUri = Uri.parse(savedInstanceState.getString(Constants.STATE_DETAILS_URI));
            }
            if (savedInstanceState.containsKey(Constants.STATE_LOADER_ID)) {
                mLoaderId = savedInstanceState.getInt(Constants.STATE_LOADER_ID);
            }

        } else { // else load the extras

            Bundle args = getArguments();
            mSelectedUri = Uri.parse(args.getString(Constants.EXTRA_DETAILS_URI));
            mLoaderId = args.getInt(Constants.EXTRA_LOADER_ID);
        }

        if (mSelectedUri == null || mLoaderId == 0) {
            throw new IllegalArgumentException("Must pass EXTRA_DETAILS_URI");
        }

        return rootView;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Prepare the loader.  Either re-connect with an existing one,
        // or start a new one.
        getLoaderManager().initLoader(mLoaderId, null, this);


    }

    // Override onAttach to make sure that the container activity has implemented the callback
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the host activity has implemented the callback interface
        // If not, it throws an exception
        try {
            mCallback = (Fragment_Main_Details.DetailsListener) context;
            mCallback.showFavMenuItem(false);
            mCallback.showSearchMenuItem(false);
            mCallback.setActionBarHomeButton(true);
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

        Log.i(Constants.TAG, "onCreateLoader() called in Details uri:" + mSelectedUri);
        Log.i(Constants.TAG, "loaderId:" + loaderId);

        return new CursorLoader(getContext(),
                mSelectedUri,
                Constants.LOADER_GATHERING_DETAIL_COLUMNS,
                null,
                null,
                null);
    }

    /**
     * @param loader The Loader that has finished.
     * @param data   The data generated by the Loader.
     */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        //Log.i(Constants.TAG, "onLoadFinished() called in Details");


        boolean cursorHasValidData = false;

        if (data != null && data.moveToFirst()) {
            Log.i(Constants.TAG, "We have good data");
            /* We have valid data, continue on to bind the data to the UI */
            cursorHasValidData = true;
        }

        if (!cursorHasValidData) {
            /* No data to display, simply return and do nothing */
            Log.i(Constants.TAG, "No data to display");
            return;
        }

        switch (loader.getId()){
            case Constants.GATHERING_DETAIL_LOADER:
                /* Read gathering table data from the cursor */
                Log.i(Constants.TAG, "Read gathering table data from the cursor");

                mGathering = Contract_MyGathering.GatheringEntry.getGatheringFromCursor(data);
                break;
            case Constants.GATHERING_FAVORITE_LOADER:
                /* Read gathering table data from the cursor */
                Log.i(Constants.TAG, "Read favorite data from the cursor");
                mGathering = Contract_MyGathering.FavoriteEntry.getFavoriteFromCursor(data);

        }
        // Check if the movie is in the favorites collection
        new Task_CheckIfGatheringIsInFavorites().execute(mGathering);

        //Log.i(Constants.TAG, "Gathering Returned in Details cursor:" + mGathering.toString());

        tv_gathering_name.setText(mGathering.getName());
        tv_gathering_notes.setText(mGathering.getNotes());

        Date start_date = Utils_DateFormatting.convertUtcToLocal(mGathering.getStart_date());

        String start = "Start: " + Utils_DateFormatting.getFormattedLongDateAndTimeStringFromLongDate(start_date.getTime());

        tv_gathering_start_date.setText(start);

        Date end_date = Utils_DateFormatting.convertUtcToLocal(mGathering.getEnd_date());

        //String end = "End:   " + Utils_DateFormatting.getFormattedGatheringDate(end_date);
        String display_end_date = "End:   " + Utils_DateFormatting.getFormattedLongDateAndTimeStringFromLongDate(end_date.getTime());

        tv_gathering_end_date.setText(display_end_date);

        tv_gathering_description.setText(mGathering.getDescription());


        tv_gathering_location.setText(mGathering.getLocation_name());

        tv_gathering_city.setText(mGathering.getLocation_city() + ", " + mGathering.getLocation_prov());

        if (mGathering.getLocation_postal().length() >= 1) {
            tv_gathering_country.setText(mGathering.getLocation_postal() + ", " + mGathering.getLocation_country());
        } else {
            tv_gathering_country.setText(mGathering.getLocation_country());
        }

        tv_gathering_type.setText(mGathering.getType());
        tv_gathering_topic.setText(mGathering.getTopic());

        String banner_url = mGathering.getBanner_url();

        Picasso.with(getContext()).setLoggingEnabled(true);

        if (Objects.equals(banner_url, "null")) {

            Picasso.with(getContext())
                    .load(R.drawable.blank)
                    .placeholder(R.drawable.blank)
                    .error(R.drawable.circle)
                    .into(iv_gathering_banner);


        } else {
            Log.d(Constants.TAG, "Banner:" + banner_url);

            Picasso.with(getContext())
                    .load(banner_url)
                    .placeholder(R.drawable.blank)
                    .into(iv_gathering_banner);
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @OnClick(R.id.iv_favorite)
    public void UpdateFavorite() {
        if (mGatheringIsFav) {
            new Task_RemoveGatheringFromFavorites().execute(mGathering);
        } else {
            new Task_AddGatheringToFavorites().execute(mGathering);
        }

        // Update the home screen widget to show the ingredients for the new favorite recipe
        // TODO: update widget with favorite
        //Service_WidgetUpdate.startActionUpdateMovieWidget(this);
    }

    // Use an async task to add the movie to the db off of the main thread.
    private class Task_AddGatheringToFavorites extends AsyncTask<Gathering_Pojo, Void, Uri> {

        @Override
        protected Uri doInBackground(Gathering_Pojo... data) {
            // Get the content resolver
            ContentResolver resolver = getActivity().getContentResolver();

            // Call the insert method on the resolver with the correct Uri from the contract class
            return resolver.insert(Contract_MyGathering.FavoriteEntry.CONTENT_URI,
                    Utils_ContentValues.extractGatheringValues(data[0]));
        }


        @Override
        protected void onPostExecute(Uri uri) {

            gatheringAddedToFavorites();
        }
    }

    private void gatheringAddedToFavorites() {

        //change image to full heart
        iv_favorite.setImageResource(R.drawable.ic_favorite_primary_24dp);
        Utils_General.showToast(getContext(), this.getString(R.string.toast_fav_added));
        mGatheringIsFav = true;
    }

    // Use an async task to remove the movie from the db off of the main thread.
    private class Task_RemoveGatheringFromFavorites extends AsyncTask<Gathering_Pojo, Void, Integer> {

        @Override
        protected Integer doInBackground(Gathering_Pojo... data) {

            // Get the content resolver
            ContentResolver resolver = getActivity().getContentResolver();

            String gathering_id = data[0].getGathering_id();
            Log.i(Constants.TAG, "Gathering_Id in background:" + gathering_id);

            String selection = Contract_MyGathering.FavoriteEntry.COLUMN_GATHERING_ID + "=?";
            String[] args = {gathering_id};

            return resolver.delete(Contract_MyGathering.FavoriteEntry.CONTENT_URI, selection, args);
        }

        @Override
        protected void onPostExecute(Integer i) {

            gatheringDeletedFromFavorites(i);
        }
    }

    private void gatheringDeletedFromFavorites(Integer recsDeleted) {

        Utils_General.showToast(getContext(), "Recs Deleted: " + recsDeleted);

        //change image to empty heart
        iv_favorite.setImageResource(R.drawable.ic_favorite_border_primary_24dp);
        Utils_General.showToast(getContext(), this.getString(R.string.toast_fav_removed));
        mGatheringIsFav = false;

        if(mLoaderId == Constants.GATHERING_FAVORITE_LOADER){
            // If this is the favs list, then refresh
            mCallback.refreshFavsList();

        }
    }

    // Use an async task to add the movie do the data fetch off of the main thread.
    private class Task_CheckIfGatheringIsInFavorites extends AsyncTask<Gathering_Pojo, Void, Cursor> {

        @Override
        protected Cursor doInBackground(Gathering_Pojo... data) {

            // Get the content resolver
            ContentResolver resolver = getActivity().getContentResolver();

            // Call the query method on the resolver with the correct Uri from the contract class

            String gathering_id = data[0].getGathering_id();
            Log.i(Constants.TAG, "id:" + gathering_id);
            String selection = Contract_MyGathering.FavoriteEntry.COLUMN_GATHERING_ID + "=?";
            String[] args = {gathering_id};

            return resolver.query(Contract_MyGathering.FavoriteEntry.CONTENT_URI, null, selection, args, null);
        }

        @Override
        protected void onPostExecute(Cursor cursor) {

            favCheckComplete(cursor);
        }
    }

    private void favCheckComplete(Cursor cursor) {

        if (cursor != null) {

            Log.i(Constants.TAG, "Count:" + cursor.getCount());

            if (cursor.getCount() == 1) {
                Log.i(Constants.TAG, "Gathering found in Favs");
                iv_favorite.setImageResource(R.drawable.ic_favorite_primary_24dp);
                mGatheringIsFav = true;
            } else {
                iv_favorite.setImageResource(R.drawable.ic_favorite_border_primary_24dp);
                mGatheringIsFav = false;
            }
        } else {
            Log.i(Constants.TAG, "Gathering not found in Favs");
            iv_favorite.setImageResource(R.drawable.ic_favorite_border_primary_24dp);
            mGatheringIsFav = false;
        }


    }
}
