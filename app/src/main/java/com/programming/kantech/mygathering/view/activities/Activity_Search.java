package com.programming.kantech.mygathering.view.activities;

import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.programming.kantech.mygathering.R;
import com.programming.kantech.mygathering.data.model.mongo.Gathering;
import com.programming.kantech.mygathering.data.model.pojo.Query_Search;
import com.programming.kantech.mygathering.data.retrofit.ApiClient;
import com.programming.kantech.mygathering.data.retrofit.ApiInterface;
import com.programming.kantech.mygathering.provider.Contract_MyGathering;
import com.programming.kantech.mygathering.sync.tasks.Task_getGatherings;
import com.programming.kantech.mygathering.utils.Constants;
import com.programming.kantech.mygathering.utils.Utils_Preferences;
import com.programming.kantech.mygathering.view.ui.Adapter_Gatherings;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;

public class Activity_Search extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, Adapter_Gatherings.GatheringsAdapterOnClickHandler {

    private Query_Search mQuery;
    private int mPosition = RecyclerView.NO_POSITION;

    private ApiInterface apiService;

    private Adapter_Gatherings mGatheringAdapter;
    private RecyclerView mGatheringsList;
    private ProgressBar mLoadingIndicator;

    private TextView tv_search_msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        tv_search_msg = (TextView) findViewById(R.id.tv_search_message);

        apiService = ApiClient.getClient().create(ApiInterface.class);

        /*
         * The ProgressBar that will indicate to the user that we are loading data. It will be
         * hidden when no data is loading.
         */
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        // Set the action bar back button to look like an up button
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mQuery = createSearchQueryFromPrefs();

        mGatheringsList = (RecyclerView) findViewById(R.id.rv_gatherings);

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        /* setLayoutManager associates the LayoutManager we created above with our RecyclerView */
        mGatheringsList.setLayoutManager(layoutManager);

        /*
         * Use this setting to improve performance if you know that changes in content do not
         * change the child layout size in the RecyclerView
         */
        mGatheringsList.setHasFixedSize(true);

        mGatheringAdapter = new Adapter_Gatherings(this, this);

        /* Setting the adapter attaches it to the RecyclerView in our layout. */
        mGatheringsList.setAdapter(mGatheringAdapter);

        showLoading();

        getGatherings();

        /*
         * Ensures a loader is initialized and active. If the loader doesn't already exist, one is
         * created and (if the activity/fragment is currently started) starts the loader. Otherwise
         * the last created loader is re-used.
         */
        getSupportLoaderManager().initLoader(Constants.GATHERING_DETAIL_LOADER, null, this);



        // Open the search filters activity automatically when the activity is loaded
        //Intent intent = new Intent(this, Activity_Search_Filters.class);
        //startActivity(intent);
        //startActivityForResult(intent, GET_QUERY_REQUEST);

    }

    private void getGatherings() {

        Call<List<Gathering>> call = apiService.getGatherings(mQuery);

        WeakReference<Context> mContext = new WeakReference<Context>(getApplicationContext());

        new Task_getGatherings(mContext).execute(call);


    }

    private Query_Search createSearchQueryFromPrefs() {

        Query_Search query = new Query_Search();

        // Load the search preferences from preferences
        float distance = Float.parseFloat(Utils_Preferences.getPreferredDistance(this));
        distance = distance * 1000;
        final String topic = Utils_Preferences.getPreferredTopic(this);
        final String type = Utils_Preferences.getPreferredType(this);
        Log.i(Constants.TAG, "Topic in Preferences:" + topic);

        // test coords for Barrie ON CA
        double lng = -79.691124;
        double lat = 44.38760379999999;

        // TODO: Load coords from prefs
        List<Double> coords = Arrays.asList(lng, lat); //Reverse for mongo (lng, lat)

        query.setDistance(distance);
        query.setCoordinates(coords);

        if (!topic.equals("All")) query.setTopic(topic);
        if (!type.equals("All")) query.setType(type);

        Log.i(Constants.TAG, "query1:" + query);

        return query;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        return true; //true means been handled
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        Log.i(Constants.TAG, "id=" + id);
        if (id == R.id.action_filter_list) {
            Log.i(Constants.TAG, "id=settings" + id);
            Intent intent = new Intent(this, Activity_Search_Filters.class);
            startActivityForResult(intent, Constants.REQUEST_GET_QUERY);
            return true; // we handle it
        }

        // When the home button is pressed, take the user back to the Activity_Main
//        if (id == android.R.id.home) {
//            NavUtils.navigateUpFromSameTask(this);
//        }
        return super.onOptionsItemSelected(item);
    }

    public void openSearchFilters(View v) {
        Log.i(Constants.TAG, "Open search filters called");

        // Open the search filters activity automatically when the activity is loaded
        Intent intent = new Intent(this, Activity_Search_Filters.class);
        startActivityForResult(intent, Constants.REQUEST_GET_QUERY);



    }

    /**
     * Dispatch incoming result to the correct fragment.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(Constants.TAG, "onActivityResult called in Activity Search");
        Log.i(Constants.TAG, "RequestCode:" + requestCode);
        Log.i(Constants.TAG, "ResultCode:" + resultCode);

        // Check if this is the result for the Location Select Activity request
        if (requestCode == Constants.REQUEST_GET_QUERY && resultCode == RESULT_OK) {

            mQuery = (Query_Search) data.getSerializableExtra(Constants.EXTRA_SEARCH_QUERY);
            Log.i(Constants.TAG, "Query:" + mQuery);

            getGatherings();


        }
    }

    @Override
    public void onClick(long id) {


    }

    /**
     * Instantiate and return a new Loader for the given ID.
     *
     * @param loaderId   The ID whose loader is to be created.
     * @param args Any arguments supplied by the caller.
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
                /*
                 * A SELECTION in SQL declares which rows you'd like to return. In our case, we
                 * want all weather data from today onwards that is stored in our weather table.
                 * We created a handy method to do that in our WeatherEntry class.
                 */
                //String selection = WeatherContract.WeatherEntry.getSqlSelectForTodayOnwards();

                return new android.support.v4.content.CursorLoader(this,
                        uri,
                        Constants.LOADER_GATHERING_DETAIL_COLUMNS,
                        null,
                        null,
                        sortOrder);

            default:
                throw new RuntimeException("Loader Not Implemented: " + loaderId);
        }
    }

    /**
     * @param loader The Loader that has finished.
     * @param data   The data generated by the Loader.
     */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mGatheringAdapter.swapCursor(data);
        if (mPosition == RecyclerView.NO_POSITION) mPosition = 0;

        mGatheringsList.smoothScrollToPosition(mPosition);

        //Log.v(Constants.TAG, "Cursor Object:" + DatabaseUtils.dumpCursorToString(data));
        showDataView();

        if(mGatheringAdapter.getItemCount() == 1){
            tv_search_msg.setText("1 match found");

        }else{
            tv_search_msg.setText(mGatheringAdapter.getItemCount() + " matches found");
        }



    }

    /**
     * Called when a previously created loader is being reset, and thus
     * making its data unavailable.  The application should at this point
     * remove any references it has to the Loader's data.
     *
     * @param loader The Loader that is being reset.
     */
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        /*
         * Since this Loader's data is now invalid, we need to clear the Adapter that is
         * displaying the data.
         */
        mGatheringAdapter.swapCursor(null);

    }

    /**
     * This method will make the View for the weather data visible and hide the error message and
     * loading indicator.
     * <p>
     * Since it is okay to redundantly set the visibility of a View, we don't need to check whether
     * each view is currently visible or invisible.
     */
    private void showDataView() {
        /* First, hide the loading indicator */
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        /* Finally, make sure the weather data is visible */
        mGatheringsList.setVisibility(View.VISIBLE);
    }

    /**
     * This method will make the loading indicator visible and hide the weather View and error
     * message.
     * <p>
     * Since it is okay to redundantly set the visibility of a View, we don't need to check whether
     * each view is currently visible or invisible.
     */
    private void showLoading() {
        /* Then, hide the weather data */
        mGatheringsList.setVisibility(View.INVISIBLE);
        /* Finally, show the loading indicator */
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }
}
