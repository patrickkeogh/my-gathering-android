package com.programming.kantech.mygathering.view.activities;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.programming.kantech.mygathering.R;
import com.programming.kantech.mygathering.data.model.mongo.Gathering;
import com.programming.kantech.mygathering.data.model.mongo.Result_Logout;
import com.programming.kantech.mygathering.data.model.pojo.Gathering_Pojo;
import com.programming.kantech.mygathering.data.retrofit.ApiClient;
import com.programming.kantech.mygathering.data.retrofit.ApiInterface;
import com.programming.kantech.mygathering.provider.Contract_MyGathering;
import com.programming.kantech.mygathering.utils.Constants;
import com.programming.kantech.mygathering.utils.Utils_ContentValues;
import com.programming.kantech.mygathering.utils.Utils_ConvertToPojo;
import com.programming.kantech.mygathering.utils.Utils_General;
import com.programming.kantech.mygathering.view.ui.Adapter_Gatherings;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_Main extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>,
        Adapter_Gatherings.GatheringsAdapterOnClickHandler {

    /*
     * If we hold a reference to our Toast, we can cancel it (if it's showing)
     * to display a new Toast. If we didn't do this, Toasts would be delayed
     * in showing up if you clicked many list items in quick succession.
     */
    private Toast mToast;

    /*
     * References to RecyclerView and Adapter to reset the list to its
     * "pretty" state when the reset menu item is clicked.
     */
    private Adapter_Gatherings mGatheringAdapter;
    private RecyclerView mGatheringsList;
    private int mPosition = RecyclerView.NO_POSITION;

    private ProgressBar mLoadingIndicator;


    private static final int NUM_LIST_ITEMS = 100;

    private ApiInterface apiService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setElevation(0f);

        //setupSharedPreferences();



        apiService = ApiClient.getClient().create(ApiInterface.class);
        Log.i(Constants.TAG, "API Service Created");

        getGatherings();

        /*
         * The ProgressBar that will indicate to the user that we are loading data. It will be
         * hidden when no data is loading.
         */
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);


//        progressDialog = new ProgressDialog(Activity_Main.this,
//                R.style.Theme_AppCompat_Light_Dialog);

        /*
         * Using findViewById, we get a reference to our RecyclerView from xml. This allows us to
         * do things like set the adapter of the RecyclerView and toggle the visibility.
         */
        mGatheringsList = (RecyclerView) findViewById(R.id.rv_gatherings);

        /*
         * A LinearLayoutManager is responsible for measuring and positioning item views within a
         * RecyclerView into a linear list. This means that it can produce either a horizontal or
         * vertical list depending on which parameter you pass in to the LinearLayoutManager
         * constructor. In our case, we want a vertical list, so we pass in the constant from the
         * LinearLayoutManager class for vertical lists, LinearLayoutManager.VERTICAL.
         *
         * There are other LayoutManagers available to display your data in uniform grids,
         * staggered grids, and more! See the developer documentation for more details.
         *
         * The third parameter (shouldReverseLayout) should be true if you want to reverse your
         * layout. Generally, this is only true with horizontal lists that need to support a
         * right-to-left layout.
         */
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        /* setLayoutManager associates the LayoutManager we created above with our RecyclerView */
        mGatheringsList.setLayoutManager(layoutManager);

        /*
         * Use this setting to improve performance if you know that changes in content do not
         * change the child layout size in the RecyclerView
         */
        mGatheringsList.setHasFixedSize(true);

        /*
         * The Adapter_Gatherings is responsible for linking our data with the Views that
         * will end up displaying our data.
         *
         * Although passing in "this" twice may seem strange, it is actually a sign of separation
         * of concerns, which is best programming practice. The Adapter_Gatherings requires an
         * Android Context (which all Activities are) as well as an onClickHandler. Since our
         * MainActivity implements the Adapter_Gatherings GatheringOnClickHandler interface, "this"
         * is also an instance of that type of handler.
         */
        mGatheringAdapter = new Adapter_Gatherings(this, this);

        /* Setting the adapter attaches it to the RecyclerView in our layout. */
        mGatheringsList.setAdapter(mGatheringAdapter);


//      COMPLETED (18) Call the showLoading method
        showLoading();



        /*
         * Ensures a loader is initialized and active. If the loader doesn't already exist, one is
         * created and (if the activity/fragment is currently started) starts the loader. Otherwise
         * the last created loader is re-used.
         */
        getSupportLoaderManager().initLoader(Constants.GATHERING_DETAIL_LOADER, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true; //true means been handled
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, Activity_Settings.class);
            startActivity(intent);
            return true; // we handle it
        } else if (id == R.id.action_logout) {
            logout();

        }
        return super.onOptionsItemSelected(item); // let app handle it
    }

    private void logout() {

        //ToDo removed logged user info from prefs
        //ToDo call server.log oput
        // send user back to login screen

        ///progressDialog.setIndeterminate(true);
        //progressDialog.setMessage("Signing out...");
        //progressDialog.show();

        Call<Result_Logout> call = apiService.logout();

        call.enqueue(new Callback<Result_Logout>() {
            @Override
            public void onResponse(Call<Result_Logout> call, Response<Result_Logout> response) {

                Log.i(Constants.TAG, "Response:" + response);

                Result_Logout result = response.body();

                Log.i(Constants.TAG, "Logout Results:" + result.getStatus());

                // Notify the activity login was successfull
                onLogoutOk(result);

            }

            @Override
            public void onFailure(Call<Result_Logout> call, Throwable t) {
                // Log error here since request failed
                Log.e(Constants.TAG, t.toString());
                onLogoutFailed(t.toString());
            }
        });


    }

    private void onLogoutFailed(String reason) {
        Utils_General.showToast(this, reason);
    }

    private void onLogoutOk(Result_Logout result) {
        Utils_General.showToast(this, result.getStatus());

        startActivity(new Intent(this, Activity_Login.class));

        finish();
    }

    public void getGatherings() {

        Call<List<Gathering>> call = apiService.getGatherings();

        call.enqueue(new Callback<List<Gathering>>() {
            @Override
            public void onResponse(Call<List<Gathering>> call, Response<List<Gathering>> response) {

                Log.i(Constants.TAG, "Response:" + response);
                List<Gathering> gatherings = response.body();
                Log.d(Constants.TAG, "Number of Gathering Types received: " + gatherings.size());

                for (int i = 0; i < gatherings.size(); i++) {
                    //Log.i(Constants.TAG, "Gathering Mongo:" + gatherings.get(i).toString());
                }



                List<ContentValues> values = new ArrayList<ContentValues>();

                //loop over the returned values and convert them to pogos
                for (int i = 0; i < gatherings.size(); i++) {
                    // Convert mongo db doc to pojo and store in content provider
                    Gathering_Pojo gathering = Utils_ConvertToPojo.convertGatheringMongoToPojo(gatherings.get(i));

                    //Log.i(Constants.TAG, "Gathering Pojo:" + gathering.toString());

                    values.add(Utils_ContentValues.extractGatheringValues(gathering));
                }

                /*
                 * Set the returned data to the adapter
                 */
                //mAdapter.setGatheringData(gatherings);

                // Delete all patients and restore them in the provider
                ContentResolver resolver = getContentResolver();
                resolver.delete(Contract_MyGathering.GatheringEntry.CONTENT_URI, null, null);

                // Bulk Insert our new weather data into Sunshine's Database
                getApplicationContext().getContentResolver().bulkInsert(
                        Contract_MyGathering.GatheringEntry.CONTENT_URI,
                        values.toArray(new ContentValues[gatherings.size()]));

            }

            @Override
            public void onFailure(Call<List<Gathering>> call, Throwable t) {
                // Log error here since request failed
                Log.i(Constants.TAG, "We got an error somewhere");
                Log.e(Constants.TAG, t.toString());
            }
        });

    }

    /**
     * Called by the {@link android.support.v4.app.LoaderManagerImpl} when a new Loader needs to be
     * created. This Activity only uses one loader, so we don't necessarily NEED to check the
     * loaderId, but this is certainly best practice.
     *
     * @param loaderId The loader ID for which we need to create a loader
     * @param bundle   Any arguments supplied by the caller
     * @return A new Loader instance that is ready to start loading.
     */
    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle bundle) {

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

                return new CursorLoader(this,
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
     * Called when a Loader has finished loading its data.
     * <p>
     * NOTE: There is one small bug in this code. If no data is present in the cursor do to an
     * initial load being performed with no access to internet, the loading indicator will show
     * indefinitely, until data is present from the ContentProvider. This will be fixed in a
     * future version of the course.
     *
     * @param loader The Loader that has finished.
     * @param data   The data generated by the Loader.
     */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        mGatheringAdapter.swapCursor(data);
        if (mPosition == RecyclerView.NO_POSITION) mPosition = 0;

        mGatheringsList.smoothScrollToPosition(mPosition);

        //Log.v(Constants.TAG, "Cursor Object:" + DatabaseUtils.dumpCursorToString(data));

//      COMPLETED (31) If the Cursor's size is not equal to 0, call showWeatherDataView
        if (data.getCount() != 0) showDataView();
    }

    /**
     * Called when a previously created loader is being reset, and thus making its data unavailable.
     * The application should at this point remove any references it has to the Loader's data.
     *
     * @param loader The Loader that is being reset.
     */
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
//      COMPLETED (32) Call mForecastAdapter's swapCursor method and pass in null
        /*
         * Since this Loader's data is now invalid, we need to clear the Adapter that is/
         * displaying the data.
         */
        mGatheringAdapter.swapCursor(null);
    }

    /**
     * This method is for responding to clicks from our list.
     *
     * @param id The gathering id that was clicked in the list
     */
    @Override
    public void onClick(long id) {

        Intent intent = new Intent(Activity_Main.this, Activity_Gathering_Details.class);
        Uri uri = Contract_MyGathering.GatheringEntry.buildGatheringUri(id);
        intent.setData(uri);
        startActivity(intent);


        /*
         * Here, we create the Intent that will start the Gathering Details Activity
         */
        //Intent intent = new Intent(this, Activity_Gathering_Details.class);
        //intent.putExtra(Intent.EXTRA_EMAIL, gathering);
        //startActivity(intent);
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
