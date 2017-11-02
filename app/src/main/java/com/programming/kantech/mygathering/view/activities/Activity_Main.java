package com.programming.kantech.mygathering.view.activities;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.programming.kantech.mygathering.R;
import com.programming.kantech.mygathering.data.model.mongo.Gathering;
import com.programming.kantech.mygathering.data.model.mongo.Result_Count;
import com.programming.kantech.mygathering.data.model.mongo.Result_Logout;
import com.programming.kantech.mygathering.data.model.pojo.Gathering_Pojo;
import com.programming.kantech.mygathering.data.model.pojo.Query_Search;
import com.programming.kantech.mygathering.data.retrofit.ApiClient;
import com.programming.kantech.mygathering.data.retrofit.ApiInterface;
import com.programming.kantech.mygathering.provider.Contract_MyGathering;
import com.programming.kantech.mygathering.sync.ReminderUtilities;
import com.programming.kantech.mygathering.sync.tasks.Task_getGatherings;
import com.programming.kantech.mygathering.utils.Constants;
import com.programming.kantech.mygathering.utils.Utils_General;
import com.programming.kantech.mygathering.utils.Utils_Preferences;
import com.programming.kantech.mygathering.view.fragments.Fragment_Gathering_List;
import com.programming.kantech.mygathering.view.fragments.Fragment_Main;
import com.programming.kantech.mygathering.view.fragments.Fragment_Main_Details;
import com.programming.kantech.mygathering.view.fragments.Fragment_SearchFiltersDialog;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_Main extends AppCompatActivity implements
        Fragment_Main.MainListener,
Fragment_Main_Details.DetailsListener,
        Fragment_Gathering_List.SelectListener,
        Fragment_SearchFiltersDialog.DialogListener,
        LoaderManager.LoaderCallbacks<Cursor> {


    private ArrayList<Gathering_Pojo> mGatherings;
    private ActionBar mActionBar;
    private int mLoaderId;
    private boolean mShowFavsMenu;

    private boolean mShowSearchMenu;

    private Query_Search mQuery;

    // Track the orientation mode
    private boolean mLandscapeView;

    private ApiInterface apiService;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.container_master)
    FrameLayout container_master;

    @BindView(R.id.container_details)
    FrameLayout container_details;

    @BindView(R.id.container_master_fullscreen)
    FrameLayout container_master_fullscreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        apiService = ApiClient.getClient().create(ApiInterface.class);
        //Log.i(Constants.TAG, "API Service Created");

        // Set the support action bar
        setSupportActionBar(mToolbar);

        // Set the action bar back button to look like an up button
        mActionBar = this.getSupportActionBar();

        setActionBarTitle(getString(R.string.app_title_live));

        //mShowFavsMenu = true;


        // Determine if you're in portrait or landscape mode
        mLandscapeView = (findViewById(R.id.layout_for_two_cols) != null);

        if (mLandscapeView) {
            //Log.i(Constants.TAG, "we are in landscape mode");

            // Hide the details column so grid loads full screen on start
            container_details.setVisibility(View.GONE);
            container_master.setVisibility(View.GONE);

            Fragment_Main fragment_main = Fragment_Main.newInstance();
            replaceFragment(R.id.container_master_fullscreen, Constants.TAG_FRAGMENT_MAIN, fragment_main, true);


        } else {

            Fragment_Main fragment_main = Fragment_Main.newInstance();
            replaceFragment(R.id.container_master, Constants.TAG_FRAGMENT_MAIN, fragment_main, true);

        }


        //Fragment_Main fragment_main = Fragment_Main.newInstance();
        //replaceFragment(R.id.container_master, Constants.TAG_FRAGMENT_MAIN, fragment_main);

        //Query_Search query = createSearchQueryFromPrefs();
        mQuery = new Query_Search();

        mLoaderId = Constants.GATHERING_DETAIL_LOADER;

        getGatherings(mQuery);

        // Schedule the new gathering search job if notifications are allowed
        Boolean isAllowed = Utils_Preferences.isNotificationsAllowed(this);
        if (isAllowed) {
            Log.i(Constants.TAG, "Notifications are allowed");

            ReminderUtilities.scheduleNewGatheringSearch(this);
        }



        /*
         * Ensures a loader is initialized and active. If the loader doesn't already exist, one is
         * created and (if the activity/fragment is currently started) starts the loader. Otherwise
         * the last created loader is re-used.
         */
        getSupportLoaderManager().initLoader(mLoaderId, null, this);

    }

    private void setActionBarTitle(String title) {
        Log.i(Constants.TAG, "setActionBarTitle called:" + title);

        if (mActionBar != null) {
            mActionBar.setTitle(title);
        }
    }

    public void setActionBarHomeButton(boolean b) {

        if (mActionBar != null) {
            Log.i(Constants.TAG, "action bar set:" + b);
            mActionBar.setDisplayHomeAsUpEnabled(b);
        }
    }

    @Override
    public void showSearchMenuItem(boolean b) {
        Log.i(Constants.TAG, "showSearchMenuItem called  in Main actiivty:" + b);
        mShowSearchMenu = b;
        invalidateOptionsMenu();

    }

    private void getGatherings(Query_Search query) {

        removeDetailsFrag();

        // Replace the Gathering List with unselected item
        if (mLandscapeView) {

            Fragment frag = getSupportFragmentManager()
                    .findFragmentByTag(Constants.TAG_FRAGMENT_MAIN_LIST);

            if (frag != null && frag instanceof Fragment_Gathering_List) {
                Log.i(Constants.TAG, "Fragment List is loaded so replace it");

                //Log.i(Constants.TAG, "we are in landscape mode");
                Fragment_Gathering_List fragment_list = Fragment_Gathering_List.newInstance(-1, Constants.GATHERING_DETAIL_LOADER);
                replaceFragment(R.id.container_master, Constants.TAG_FRAGMENT_MAIN_LIST, fragment_list, false);
            }
        }

        Call<List<Gathering>> call = apiService.getGatherings(query);

        WeakReference<Context> mContext = new WeakReference<>(getApplicationContext());

        new Task_getGatherings(mContext).execute(call);

    }

    private void replaceFragment(int container, String fragment_tag, Fragment fragment_in, boolean addToBackStack) {

        // Get a fragment transaction to replace fragments
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (Objects.equals(fragment_tag, Constants.TAG_FRAGMENT_HEADER)) {
            transaction.add(container, fragment_in, fragment_tag);

        } else {
            transaction.replace(container, fragment_in, fragment_tag);
            //if(addToBackStack) transaction.addToBackStack(null);
        }

        // Commit the transaction
        transaction.commit();
    }


    //setupSharedPreferences();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i(Constants.TAG, "onCreateOptionsMenu:" + mShowFavsMenu);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        MenuItem mMenuItem_Favs = menu.findItem(R.id.action_show_favs);
        MenuItem mMenuItem_Search = menu.findItem(R.id.action_search);

        mMenuItem_Favs.setVisible(mShowFavsMenu);
        mMenuItem_Search.setVisible(mShowSearchMenu);

        return true; //true means been handled
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            //Log.i(Constants.TAG, "id=settings" + id);
            Intent intent = new Intent(this, Activity_Settings.class);
            startActivity(intent);
            return true; // we handle it
        } else if (id == R.id.action_add_gathering) {
            //Log.i(Constants.TAG, "id=addGathering" + id);
            Intent intent = new Intent(this, Activity_AddGathering.class);
            startActivity(intent);
            return true;

        } else if (id == R.id.action_logout) {
            //Log.i(Constants.TAG, "id=logout" + id);

            logout();
            return true;

        } else if (id == R.id.action_search) {
            FragmentManager fm = getSupportFragmentManager();
            Fragment_SearchFiltersDialog dialog = Fragment_SearchFiltersDialog.newInstance();
            dialog.show(fm, Constants.TAG_FRAGMENT_SEARCH_DIALOG);

            return true;

        } else if (id == R.id.action_show_favs) {
            //Log.i(Constants.TAG, "id=favs" + id);

            // Get all the favorites from Storage
            getFavorites();
            setActionBarTitle(getString(R.string.app_title_favorites));
            return true;

        } else if (id == R.id.action_dump_favs) {
            //Log.i(Constants.TAG, "id=search" + id);

            // Check if the movie is in the favorites collection
            new Task_GetFavorites().execute();
            return true;

        } else if (id == android.R.id.home) {
            //Log.i(Constants.TAG, "Home called()");

            if(mLoaderId == Constants.GATHERING_DETAIL_LOADER){
                setActionBarTitle(getString(R.string.app_title_live));
            }else{
                setActionBarTitle(getString(R.string.app_title_favorites));
            }

            if (mLandscapeView) {
                //Log.i(Constants.TAG, "we are in landscape mode");

                // Hide the details column so grid loads full screen on start
                container_details.setVisibility(View.GONE);
                container_master.setVisibility(View.GONE);
                container_master_fullscreen.setVisibility(View.VISIBLE);

                Fragment_Main fragment_main = Fragment_Main.newInstance();
                replaceFragment(R.id.container_master_fullscreen, Constants.TAG_FRAGMENT_MAIN, fragment_main, true);

                //fragment_main.notifyDataChange(mGatherings);


            } else {

                Fragment_Main fragment_main = Fragment_Main.newInstance();
                replaceFragment(R.id.container_master, Constants.TAG_FRAGMENT_MAIN, fragment_main, true);
                //fragment_main.notifyDataChange(mGatherings);

            }

            getSupportLoaderManager().restartLoader(mLoaderId, null, this);

//            FragmentManager fm = getSupportFragmentManager();
//            if (fm.getBackStackEntryCount() > 0) {
//                fm.popBackStack();
//            }
            return true;

        }


        return super.onOptionsItemSelected(item); // let app handle it
    }

    private void getFavorites() {
        mLoaderId = Constants.GATHERING_FAVORITE_LOADER;

        /*
         * Ensures a loader is initialized and active. If the loader doesn't already exist, one is
         * created and (if the activity/fragment is currently started) starts the loader. Otherwise
         * the last created loader is re-used.
         */
        getSupportLoaderManager().restartLoader(mLoaderId, null, this);

    }

    @Override
    public void onBannerClick(Gathering_Pojo gathering) {

        setActionBarTitle(getString(R.string.app_title_details));
        //mMenuItem_Favs.setVisible(false);

        Uri uri;

        if (mLoaderId == Constants.GATHERING_DETAIL_LOADER) {
            uri = Contract_MyGathering.GatheringEntry.buildGatheringUri(gathering.getId());
        } else {
            uri = Contract_MyGathering.FavoriteEntry.buildFavoriteUri(gathering.getId());
        }


        if (mLandscapeView) {
            //Log.i(Constants.TAG, "we are in landscape mode");

            // Hide the details column so grid loads full screen on start
            container_details.setVisibility(View.VISIBLE);
            container_master.setVisibility(View.VISIBLE);
            container_master_fullscreen.setVisibility(View.GONE);


            Fragment_Gathering_List fragment_list = Fragment_Gathering_List.newInstance((int) gathering.getId(), mLoaderId);
            replaceFragment(R.id.container_master, Constants.TAG_FRAGMENT_MAIN_LIST, fragment_list, false);

            Fragment_Main_Details fragment_details = Fragment_Main_Details.newInstance(uri, mLoaderId);
            replaceFragment(R.id.container_details, Constants.TAG_FRAGMENT_MAIN_DETAILS, fragment_details, false);


        } else {

            // Hide the details column so grid loads full screen on start
            container_details.setVisibility(View.GONE);
            container_master.setVisibility(View.VISIBLE);
            container_master_fullscreen.setVisibility(View.GONE);

            Fragment_Main_Details fragment_details = Fragment_Main_Details.newInstance(uri, mLoaderId);
            replaceFragment(R.id.container_master, Constants.TAG_FRAGMENT_MAIN_DETAILS, fragment_details, false);

            //Fragment_Gathering_List fragment_list = Fragment_Gathering_List.newInstance();
            //replaceFragment(R.id.container_master, Constants.TAG_FRAGMENT_GATHERING_LIST, fragment_list);

        }


    }

    @Override
    public void onGatheringSelected(Uri uri) {

        Fragment_Main_Details fragment = Fragment_Main_Details.newInstance(uri, mLoaderId);

        if (mLandscapeView) {

            container_details.setVisibility(View.VISIBLE);
            container_master.setVisibility(View.VISIBLE);
            container_master_fullscreen.setVisibility(View.GONE);

            replaceFragment(R.id.container_details, Constants.TAG_FRAGMENT_MAIN_DETAILS, fragment, false);

        } else {

            // Hide the details column so grid loads full screen on start
            container_details.setVisibility(View.GONE);
            container_master.setVisibility(View.VISIBLE);
            container_master_fullscreen.setVisibility(View.GONE);

            replaceFragment(R.id.container_master, Constants.TAG_FRAGMENT_MAIN_DETAILS, fragment, false);

        }

    }

    @Override
    public void removeDetailsFrag() {
        Log.i(Constants.TAG, "removeDetailsFrag called");

        Fragment fragment_details = getSupportFragmentManager()
                .findFragmentByTag(Constants.TAG_FRAGMENT_MAIN_DETAILS);

        if (fragment_details != null && fragment_details instanceof Fragment_Main_Details) {
            Fragment_Main_Details frag = (Fragment_Main_Details) fragment_details;

            if (frag.isResumed()) {
                getSupportFragmentManager().beginTransaction().remove(fragment_details).commit();
                Log.i(Constants.TAG, "Details removed");

            }

        }

    }

    @Override
    public void refreshGatherings() {

        getGatherings(mQuery);
    }

    @Override
    public void showFavMenuItem(boolean b) {
        Log.i(Constants.TAG, "showFavMenuItem called  in Main actiivty:" + b);
        mShowFavsMenu = b;
        invalidateOptionsMenu();
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

            case Constants.GATHERING_FAVORITE_LOADER:
                /* URI for all rows of data in our gatherings table */
                Uri uri_favs = Contract_MyGathering.FavoriteEntry.CONTENT_URI;
                /* Sort order: Ascending by name */
                String fav_sort = Contract_MyGathering.FavoriteEntry.COLUMN_GATHERING_NAME + " ASC";

                return new android.support.v4.content.CursorLoader(this,
                        uri_favs,
                        Constants.LOADER_GATHERING_DETAIL_COLUMNS,
                        null,
                        null,
                        fav_sort);

            default:
                throw new RuntimeException("Loader Not Implemented: " + loaderId);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        //Log.i(Constants.TAG, "onLoadFinished in Activity main called");

        int loaderId = loader.getId();

        switch (loaderId) {

            case Constants.GATHERING_DETAIL_LOADER:
                //Log.i(Constants.TAG, "Gatherings are loaded");
                mGatherings = new ArrayList<>();

                for (data.moveToFirst(); !data.isAfterLast(); data.moveToNext()) {

                    /* Read date from the cursor */
                    Gathering_Pojo gathering = Contract_MyGathering.GatheringEntry.getGatheringFromCursor(data);

                    mGatherings.add(gathering);
                }

                notifyLoadedFragmentsDataHasChanged(loaderId);

                break;
            case Constants.GATHERING_FAVORITE_LOADER:
                //Log.i(Constants.TAG, "Favorites are loaded");
                mGatherings = new ArrayList<>();

                for (data.moveToFirst(); !data.isAfterLast(); data.moveToNext()) {


                    /* Read date from the cursor */
                    Gathering_Pojo favorite = Contract_MyGathering.FavoriteEntry.getFavoriteFromCursor(data);
                    //Log.i(Constants.TAG, "add favorite:" + favorite.getName());
                    mGatherings.add(favorite);
                }

                //Log.i(Constants.TAG, "Count:" + mGatherings.size());


                notifyLoadedFragmentsDataHasChanged(loaderId);

                break;
        }

        // Notify fragments we have new data

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private void notifyLoadedFragmentsDataHasChanged(int loader) {

//        Fragment_Main fragment_main = (Fragment_Main) getSupportFragmentManager()
//                .findFragmentByTag(Constants.TAG_FRAGMENT_MAIN);
//
//        if (fragment_main != null) {
//            fragment_main.notifyDataChange(mGatherings);
//        }

        Fragment fragment_list = getSupportFragmentManager()
                .findFragmentByTag(Constants.TAG_FRAGMENT_MAIN_LIST);

        if (fragment_list != null && fragment_list instanceof Fragment_Gathering_List) {
            //Log.i(Constants.TAG, "Fragment List is loaded");

            Fragment_Gathering_List frag = (Fragment_Gathering_List) fragment_list;
            frag.notifyDataChange(loader);
        }

        Fragment fragment_main = getSupportFragmentManager()
                .findFragmentByTag(Constants.TAG_FRAGMENT_MAIN);

        if (fragment_main != null && fragment_main instanceof Fragment_Main) {
            //Log.i(Constants.TAG, "Fragment Main is loaded");
            //Log.i(Constants.TAG, "GatheringCount:" + mGatherings.size());

            Fragment_Main frag = (Fragment_Main) fragment_main;
            frag.notifyDataChange(mGatherings);
        }


//        Fragment_Gathering_List fragment_list = (Fragment_Gathering_List) getSupportFragmentManager()
//                .findFragmentByTag(Constants.TAG_FRAGMENT_MAIN_LIST);
//
//        if (fragment_list != null) {
//            fragment_list.notifyDataChange();
//        }
    }

    @Override
    public void onFinish(Query_Search query) {
        //Log.i(Constants.TAG, "onFinish called in activity");

        setActionBarTitle(getString(R.string.app_title_live));

        mLoaderId = Constants.GATHERING_DETAIL_LOADER;

        getGatherings(query);


    }

    @Override
    public void getQueryCount(Query_Search query) {
        //Log.i(Constants.TAG, "onFinish called in activity");
        //Log.i(Constants.TAG, "Query:" + query.toString());

        Call<Result_Count> call = apiService.getQueryCount(query);

        call.enqueue(new Callback<Result_Count>() {
            @Override
            public void onResponse(@NonNull Call<Result_Count> call, @NonNull Response<Result_Count> response) {

                //Log.i(Constants.TAG, "Response:" + response);
                Result_Count count = response.body();

                if (count != null) {

                    finishGetCount(count.getRecCount());

                    // Return the count to the Dialog Fragment
                    //Log.d(Constants.TAG, "Number of gatherings returned: " + count.getRecCount());
                } else {
                    finishGetCount(0);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Result_Count> call, @NonNull Throwable t) {
                // Log error here since request failed
                Log.i(Constants.TAG, "We got an error somewhere");
                Log.e(Constants.TAG, t.toString());
            }
        });


    }

    private void finishGetCount(int size) {

        Fragment_SearchFiltersDialog fragment = (Fragment_SearchFiltersDialog) getSupportFragmentManager()
                .findFragmentByTag(Constants.TAG_FRAGMENT_SEARCH_DIALOG);

        fragment.setCount(size);
    }

    private void dumpFavs(Cursor cursor) {

        Log.i(Constants.TAG, "dumpFavs");

        if (cursor != null) {
            Log.i(Constants.TAG, "Count:" + cursor.getCount());



            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

            /* Read date from the cursor */
                Gathering_Pojo gathering = Contract_MyGathering.GatheringEntry.getGatheringFromCursor(cursor);

                Log.i(Constants.TAG, "Favorite:" + gathering.toString());
            }

            ContentResolver resolver = getContentResolver();
            resolver.delete(Contract_MyGathering.FavoriteEntry.CONTENT_URI, null, null);
            //Log.i(Constants.TAG, "Count:" + count);

        }
    }

    @Override
    public void refreshFavsList() {
        removeDetailsFrag();
        getFavorites();

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
            public void onResponse(@NonNull Call<Result_Logout> call, @NonNull Response<Result_Logout> response) {

                Log.i(Constants.TAG, "Response:" + response);

                Result_Logout result = response.body();

                if (result != null) {
                    Log.i(Constants.TAG, "Logout Results:" + result.getStatus());

                    // Notify the activity login was successfull
                    onLogoutOk(result);
                }


            }

            @Override
            public void onFailure(@NonNull Call<Result_Logout> call, @NonNull Throwable t) {
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
}
