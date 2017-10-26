package com.programming.kantech.mygathering.view.activities;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.LoginFilter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.programming.kantech.mygathering.R;
import com.programming.kantech.mygathering.data.model.mongo.Gathering;
import com.programming.kantech.mygathering.data.model.pojo.Gathering_Pojo;
import com.programming.kantech.mygathering.data.model.pojo.Query_Search;
import com.programming.kantech.mygathering.data.retrofit.ApiClient;
import com.programming.kantech.mygathering.data.retrofit.ApiInterface;
import com.programming.kantech.mygathering.provider.Contract_MyGathering;
import com.programming.kantech.mygathering.sync.tasks.Task_getGatherings;
import com.programming.kantech.mygathering.utils.Constants;
import com.programming.kantech.mygathering.utils.Utils_ContentValues;
import com.programming.kantech.mygathering.view.fragments.Fragment_Gathering_List;
import com.programming.kantech.mygathering.view.fragments.Fragment_Main;
import com.programming.kantech.mygathering.view.fragments.Fragment_Main_Details;
import com.programming.kantech.mygathering.view.ui.ImageAdapter;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;

public class Activity_Main extends AppCompatActivity implements
        Fragment_Main.MainListener,
Fragment_Gathering_List.SelectListener,
        LoaderManager.LoaderCallbacks<Cursor> {

    /*
     * If we hold a reference to our Toast, we can cancel it (if it's showing)
     * to display a new Toast. If we didn't do this, Toasts would be delayed
     * in showing up if you clicked many list items in quick succession.
     */
    //private Toast mToast;

    /*
     * References to RecyclerView and Adapter to reset the list to its
     * "pretty" state when the reset menu item is clicked.
     */
    //private Adapter_Gatherings mGatheringAdapter;
    private ImageAdapter mImageAdapter;

    private ArrayList<Gathering_Pojo> mGatherings;

    private Query_Search mQuery;

    // Track the orientation mode
    private boolean mLandscapeView;

    //private int mPosition = RecyclerView.NO_POSITION;

    //private static final int NUM_LIST_ITEMS = 100;

    private ApiInterface apiService;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.container_master)
    FrameLayout container_master;

    @BindView(R.id.container_details)
    FrameLayout container_details;

    @BindView(R.id.container_master_fullscreen)
    FrameLayout container_master_fullscreen;

//    @BindView(R.id.gv_gatherings)
//    GridView gv_gatherings;
//
//    @BindView(R.id.swiperefresh)
//    SwipeRefreshLayout mySwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        apiService = ApiClient.getClient().create(ApiInterface.class);
        Log.i(Constants.TAG, "API Service Created");

        // Set the support action bar
        setSupportActionBar(mToolbar);

        // Set the action bar back button to look like an up button
        ActionBar mActionBar = this.getSupportActionBar();

        if (mActionBar != null) {
            mActionBar.setDisplayHomeAsUpEnabled(false);
            mActionBar.setTitle("My Gatherings");
        }

        // Determine if you're in portrait or landscape mode
        mLandscapeView = (findViewById(R.id.layout_for_two_cols) != null);

        if (mLandscapeView) {
            Log.i(Constants.TAG, "we are in landscape mode");

            // Hide the details column so grid loads full screen on start
            container_details.setVisibility(View.GONE);
            container_master.setVisibility(View.GONE);

            Fragment_Main fragment_main = Fragment_Main.newInstance();
            replaceFragment(R.id.container_master_fullscreen, Constants.TAG_FRAGMENT_MAIN, fragment_main);


        } else {

            Fragment_Main fragment_main = Fragment_Main.newInstance();
            replaceFragment(R.id.container_master, Constants.TAG_FRAGMENT_MAIN, fragment_main);

        }


        //Fragment_Main fragment_main = Fragment_Main.newInstance();
        //replaceFragment(R.id.container_master, Constants.TAG_FRAGMENT_MAIN, fragment_main);

        //Query_Search query = createSearchQueryFromPrefs();
        mQuery = new Query_Search();

        getGatherings(mQuery);

        /*
         * Ensures a loader is initialized and active. If the loader doesn't already exist, one is
         * created and (if the activity/fragment is currently started) starts the loader. Otherwise
         * the last created loader is re-used.
         */
        getSupportLoaderManager().initLoader(Constants.GATHERING_DETAIL_LOADER, null, this);

    }

    private void getGatherings(Query_Search query) {

        Call<List<Gathering>> call = apiService.getGatherings(query);

        WeakReference<Context> mContext = new WeakReference<Context>(getApplicationContext());

        new Task_getGatherings(mContext).execute(call);

    }

    private void replaceFragment(int container, String fragment_tag, Fragment fragment_in) {

        // Get a fragment transaction to replace fragments
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (Objects.equals(fragment_tag, Constants.TAG_FRAGMENT_HEADER)) {
            transaction.add(container, fragment_in, fragment_tag);

        } else {
            transaction.replace(container, fragment_in, fragment_tag);
        }

        // Commit the transaction
        transaction.commit();
    }


    //setupSharedPreferences();


//        apiService = ApiClient.getClient().create(ApiInterface.class);
//        Log.i(Constants.TAG, "API Service Created");
//
//        gv_gatherings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
//
////                Movie movie = mImageAdapter.getItem(position);
////
////                Log.i(Constants.LOG_TAG, "Movie:" + movie.toString());
////
////                Intent intent = new Intent(Activity_Main.this, Activity_Details.class);
////                intent.putExtra(Constants.EXTRA_MOVIE_DETAILS, movie);
////                startActivity(intent);
//            }
//        });
//
//        // Start the swipe progress indicator
//        mySwipeRefreshLayout.setRefreshing(true);
//
//
//        getGatherings();
//
//        // Schedule the new gathering search job if notifications are allowed
//        Boolean isAllowed = Utils_Preferences.isNotificationsAllowed(this);
//        if (isAllowed) {
//            Log.i(Constants.TAG, "Notifications are allowed");
//
//            ReminderUtilities.scheduleNewGatheringSearch(this);
//        }
//
//        /*
//         * Sets up a SwipeRefreshLayout.OnRefreshListener that is invoked when the user
//         * performs a swipe-to-refresh gesture.
//        */
//        mySwipeRefreshLayout.setOnRefreshListener(
//                new SwipeRefreshLayout.OnRefreshListener() {
//                    @Override
//                    public void onRefresh() {
//                        Log.i(Constants.TAG, "onRefresh called from SwipeRefreshLayout");
//
//                        // This method performs the actual data-refresh operation.
//                        // The method calls setRefreshing(false) when it's finished.
//                        getGatherings();
//                    }
//                }
//        );

    // }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true; //true means been handled
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        Log.i(Constants.TAG, "id=" + id);
        if (id == R.id.action_settings) {
            Log.i(Constants.TAG, "id=settings" + id);
            Intent intent = new Intent(this, Activity_Settings.class);
            startActivity(intent);
            return true; // we handle it
        } else if (id == R.id.action_add_gathering) {
            Log.i(Constants.TAG, "id=addGathering" + id);
            Intent intent = new Intent(this, Activity_AddGathering.class);
            startActivity(intent);
            return true;

        } else if (id == R.id.action_logout) {
            Log.i(Constants.TAG, "id=logout" + id);

            //logout();
            return true;

        } else if (id == R.id.action_search) {
            Log.i(Constants.TAG, "id=search" + id);
            Intent intent = new Intent(this, Activity_Search.class);
            startActivity(intent);
            return true;

        }else if (id == R.id.action_dump_favs) {
            Log.i(Constants.TAG, "id=search" + id);

            // Check if the movie is in the favorites collection
            new Task_GetFavorites().execute();
            return true;

        }

        return super.onOptionsItemSelected(item); // let app handle it
    }

    @Override
    public void onBannerClick(Gathering_Pojo gathering) {





        if (mLandscapeView) {
            Log.i(Constants.TAG, "we are in landscape mode");

            // Hide the details column so grid loads full screen on start
            container_details.setVisibility(View.VISIBLE);
            container_master.setVisibility(View.VISIBLE);
            container_master_fullscreen.setVisibility(View.GONE);

            //Fragment_Gathering_List fragment_list = Fragment_Gathering_List.newInstance();
            //replaceFragment(R.id.container_details, Constants.TAG_FRAGMENT_GATHERING_LIST, fragment_list);


        } else {

            // Hide the details column so grid loads full screen on start
            container_details.setVisibility(View.GONE);
            container_master.setVisibility(View.VISIBLE);
            container_master_fullscreen.setVisibility(View.GONE);

            //Fragment_Gathering_List fragment_list = Fragment_Gathering_List.newInstance();
            //replaceFragment(R.id.container_master, Constants.TAG_FRAGMENT_GATHERING_LIST, fragment_list);

        }

        Fragment_Gathering_List fragment_list = Fragment_Gathering_List.newInstance();
        replaceFragment(R.id.container_master, Constants.TAG_FRAGMENT_MAIN_LIST, fragment_list);




    }

    @Override
    public void onGatheringSelected(Uri uri) {

        Fragment_Main_Details fragment = Fragment_Main_Details.newInstance(uri);

        if(mLandscapeView){

            container_details.setVisibility(View.VISIBLE);
            container_master.setVisibility(View.VISIBLE);
            container_master_fullscreen.setVisibility(View.GONE);

            replaceFragment(R.id.container_details, Constants.TAG_FRAGMENT_MAIN_DETAILS, fragment);

        }else {

            // Hide the details column so grid loads full screen on start
            container_details.setVisibility(View.GONE);
            container_master.setVisibility(View.VISIBLE);
            container_master_fullscreen.setVisibility(View.GONE);

            replaceFragment(R.id.container_master, Constants.TAG_FRAGMENT_MAIN_DETAILS, fragment);

        }

    }

    @Override
    public void refreshGatherings() {

        getGatherings(mQuery);
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

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.i(Constants.TAG, "onLoadFinished");

        mGatherings = new ArrayList<>();

        for(data.moveToFirst(); !data.isAfterLast(); data.moveToNext()) {

            /* Read date from the cursor */
            Gathering_Pojo gathering = Contract_MyGathering.GatheringEntry.getGatheringFromCursor(data);

            mGatherings.add(gathering);
        }

        notifyLoadedFragmentsDataHasChanged();

        // Notify fragments we have new data

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private void notifyLoadedFragmentsDataHasChanged() {

        Fragment_Main fragment_main = (Fragment_Main) getSupportFragmentManager()
                .findFragmentByTag(Constants.TAG_FRAGMENT_MAIN);

        if (fragment_main != null){
            fragment_main.notifyDataChange(mGatherings);
        }

        Fragment_Gathering_List fragment_list = (Fragment_Gathering_List) getSupportFragmentManager()
                .findFragmentByTag(Constants.TAG_FRAGMENT_MAIN_LIST);

        if (fragment_list != null){
            fragment_list.notifyDataChange();
        }
    }

    // Use an async task to add the movie to the db off of the main thread.
    private class Task_GetFavorites extends AsyncTask<Void, Void, Cursor> {

        @Override
        protected Cursor doInBackground(Void... voids) {
            // Get the content resolver
            ContentResolver resolver = getContentResolver();

            return resolver.query(Contract_MyGathering.FavoriteEntry.CONTENT_URI, null, null, null, null);
        }

        @Override
        protected void onPostExecute(Cursor cursor) {

            dumpFavs(cursor);
        }
    }

    private void dumpFavs(Cursor cursor) {

        Log.i(Constants.TAG, "dumpFavs");

        if (cursor != null) {
            Log.i(Constants.TAG, "Count:" + cursor.getCount());

            ContentResolver resolver = getContentResolver();

            for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

            /* Read date from the cursor */
                Gathering_Pojo gathering = Contract_MyGathering.GatheringEntry.getGatheringFromCursor(cursor);

                Log.i(Constants.TAG, "Favorite:" + gathering.toString());
            }


            int count = resolver.delete(Contract_MyGathering.FavoriteEntry.CONTENT_URI, null, null);
            Log.i(Constants.TAG, "Count:" + count);



            if (cursor.getCount() == 1) {

            } else {

            }
        } else {

        }


    }


}

//    private void logout() {
//
//        //ToDo removed logged user info from prefs
//        //ToDo call server.log oput
//        // send user back to login screen
//
//        ///progressDialog.setIndeterminate(true);
//        //progressDialog.setMessage("Signing out...");
//        //progressDialog.show();
//
//        Call<Result_Logout> call = apiService.logout();
//
//        call.enqueue(new Callback<Result_Logout>() {
//            @Override
//            public void onResponse(@NonNull Call<Result_Logout> call, @NonNull Response<Result_Logout> response) {
//
//                Log.i(Constants.TAG, "Response:" + response);
//
//                Result_Logout result = response.body();
//
//                if (result != null) {
//                    Log.i(Constants.TAG, "Logout Results:" + result.getStatus());
//
//                    // Notify the activity login was successfull
//                    onLogoutOk(result);
//                }
//
//
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<Result_Logout> call, @NonNull Throwable t) {
//                // Log error here since request failed
//                Log.e(Constants.TAG, t.toString());
//                onLogoutFailed(t.toString());
//            }
//        });
//
//
//    }

//    private void onLogoutFailed(String reason) {
//        Utils_General.showToast(this, reason);
//    }
//
//    private void onLogoutOk(Result_Logout result) {
//        Utils_General.showToast(this, result.getStatus());
//
//        startActivity(new Intent(this, Activity_Login.class));
//
//        finish();
//    }
//
//    public void getGatheringsForDB() {
//
//        if (Utils_General.isNetworkAvailable(this)) {
//
//            Query_Search query = new Query_Search();
//
//            //Query_Search query = new Query_Search(coords, distance, sdf.format(date), "NULL");
//
//            Call<List<Gathering>> call = apiService.getGatherings(query);
//
//            WeakReference<Context> mContext = new WeakReference<Context>(getApplicationContext());
//
//            new Task_getGatherings(mContext).execute(call);
//
//        }else{
//
//        }
//
//
//
//
//
//    }
//
//    public void getGatherings() {
//
//        // For now just get all the gatherings and add them to the grid view
//
//        if (Utils_General.isNetworkAvailable(this)) {
//
//            Query_Search query = new Query_Search();
//
//            Call<List<Gathering>> call = apiService.getGatherings(query);
//
//            call.enqueue(new Callback<List<Gathering>>() {
//                @Override
//                public void onResponse(@NonNull Call<List<Gathering>> call, @NonNull Response<List<Gathering>> response) {
//
//                    Log.i(Constants.TAG, "Response:" + response);
//
//                    if (response.isSuccessful()) {
//                        // Code 200, 201
//                        List<Gathering> gatherings = response.body();
//
//                        if (gatherings != null) {
//                            for (int i = 0; i < gatherings.size(); i++) {
//                                Log.i(Constants.TAG, "Gathering From Server:" + gatherings.get(i).toString());
//                            }
//                        }
//
//                        // Notify the activity gathering fetch was successfull
//                        gathering_fetch_ok(gatherings);
//                        //movies_ok(fetchResults);
//                    } else {
//                        Log.i(Constants.TAG, "Gatherings Fetch was unsuccessful");
//                        //movies_failed(response.message());
//                    }
//                }
//
//                @Override
//                public void onFailure(@NonNull Call<List<Gathering>> call, @NonNull Throwable t) {
//                    // Log error here since request failed
//                    Log.e(Constants.TAG, t.toString());
//                    //movies_failed(t.toString());
//                }
//            });
//
//        } else {
//            Utils_General.showToast(this, getString(R.string.toast_not_connected_to_internet));
//        }
//
//
//    }
//
//
//    private void gathering_fetch_ok(List<Gathering> gatherings) {
//
//        mImageAdapter = new ImageAdapter(this, gatherings);
//
//        gv_gatherings.setAdapter(mImageAdapter);
//
//        // Signal SwipeRefreshLayout to end the progress indicator
//        mySwipeRefreshLayout.setRefreshing(false);
//    }
//


