package com.programming.kantech.mygathering.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.programming.kantech.mygathering.R;
import com.programming.kantech.mygathering.data.model.mongo.Result_Logout;
import com.programming.kantech.mygathering.data.retrofit.ApiClient;
import com.programming.kantech.mygathering.data.retrofit.ApiInterface;
import com.programming.kantech.mygathering.sync.ReminderUtilities;
import com.programming.kantech.mygathering.utils.Constants;
import com.programming.kantech.mygathering.utils.Utils_General;
import com.programming.kantech.mygathering.utils.Utils_Preferences;
import com.programming.kantech.mygathering.view.ui.Adapter_Gatherings;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_Main extends AppCompatActivity {

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
    private ActionBar mActionBar;
    private int mPosition = RecyclerView.NO_POSITION;

    private static final int NUM_LIST_ITEMS = 100;

    private ApiInterface apiService;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.gv_gatherings)
    GridView gv_gatherings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        // Set the support action bar
        setSupportActionBar(mToolbar);

        // Set the action bar back button to look like an up button
        mActionBar = this.getSupportActionBar();

        if (mActionBar != null) {
            mActionBar.setDisplayHomeAsUpEnabled(false);
            mActionBar.setTitle("Select A Customer");
        }


        //setupSharedPreferences();


        apiService = ApiClient.getClient().create(ApiInterface.class);
        Log.i(Constants.TAG, "API Service Created");

        gv_gatherings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

//                Movie movie = mImageAdapter.getItem(position);
//
//                Log.i(Constants.LOG_TAG, "Movie:" + movie.toString());
//
//                Intent intent = new Intent(Activity_Main.this, Activity_Details.class);
//                intent.putExtra(Constants.EXTRA_MOVIE_DETAILS, movie);
//                startActivity(intent);
            }
        });



        getGatherings();

        /*
         * The ProgressBar that will indicate to the user that we are loading data. It will be
         * hidden when no data is loading.
         */
        //mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);


//        progressDialog = new ProgressDialog(Activity_Main.this,
//                R.style.Theme_AppCompat_Light_Dialog);

        /*
         * Using findViewById, we get a reference to our RecyclerView from xml. This allows us to
         * do things like set the adapter of the RecyclerView and toggle the visibility.
         */
        //mGatheringsList = (RecyclerView) findViewById(R.id.rv_gatherings);

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
//        LinearLayoutManager layoutManager =
//                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
//
//        /* setLayoutManager associates the LayoutManager we created above with our RecyclerView */
//        mGatheringsList.setLayoutManager(layoutManager);
//
//        /*
//         * Use this setting to improve performance if you know that changes in content do not
//         * change the child layout size in the RecyclerView
//         */
//        mGatheringsList.setHasFixedSize(true);

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
        //mGatheringAdapter = new Adapter_Gatherings(this, this);

        /* Setting the adapter attaches it to the RecyclerView in our layout. */
        //mGatheringsList.setAdapter(mGatheringAdapter);


        //showLoading();



        /*
         * Ensures a loader is initialized and active. If the loader doesn't already exist, one is
         * created and (if the activity/fragment is currently started) starts the loader. Otherwise
         * the last created loader is re-used.
         */
        //getSupportLoaderManager().initLoader(Constants.GATHERING_DETAIL_LOADER, null, this);

        // Schedule the new gathering search job if notifications are allowed
        Boolean isAllowed = Utils_Preferences.isNotificationsAllowed(this);
        if (isAllowed) {
            Log.i(Constants.TAG, "Notifications are allowed");

            ReminderUtilities.scheduleNewGatheringSearch(this);
        }

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

        }else if (id == R.id.action_logout) {
            Log.i(Constants.TAG, "id=logout" + id);

            logout();
            return true;


        }else if (id == R.id.action_search) {
            Log.i(Constants.TAG, "id=search" + id);
            Intent intent = new Intent(this, Activity_Search.class);
            startActivity(intent);
            return true;

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

        // Load the search preferences from preferences
        float distance = Float.parseFloat(Utils_Preferences.getPreferredDistance(this));
        distance = distance * 1000;
        final String topic = Utils_Preferences.getPreferredTopic(this);
        final String type = Utils_Preferences.getPreferredType(this);
        Log.i(Constants.TAG, "Topic in Preferences:" + topic);

        // test coords for Barrie ON CA
        double lng = -79.691124;
        double lat = 44.38760379999999;

        // For now just get all the gatherings and add them to the grid view
    }
//
//        // TODO: Load coords from prefs
//        List<Double> coords = Arrays.asList(lng, lat); //Reverse for mongo (lng, lat)
//
//        Query_Search query = new Query_Search();
//
//        query.setDistance(distance);
//        query.setCoordinates(coords);
//
//        if (!topic.equals("All")) query.setTopic(topic);
//        if (!type.equals("All")) query.setType(type);
//
//        Log.i(Constants.TAG, "query1:" + query);
//
//
//        //Query_Search query = new Query_Search(coords, distance, sdf.format(date), "NULL");
//
//        Call<List<Gathering>> call = apiService.getGatherings(query);
//
//        WeakReference<Context> mContext = new WeakReference<Context>(getApplicationContext());
//
//        new Task_getGatherings(mContext).execute(call);
//
//        //Call<List<Gathering>> call = apiService.getGatherings();
//
////        call.enqueue(new Callback<List<Gathering>>() {
////            @Override
////            public void onResponse(Call<List<Gathering>> call, Response<List<Gathering>> response) {
////
////                Log.i(Constants.TAG, "Response:" + response);
////                List<Gathering> gatherings = response.body();
////                Log.d(Constants.TAG, "Number of gatherings returned: " + gatherings.size());
////
////                if(gatherings != null){
////
////
////
////
////                }
////
//////                for (int i = 0; i < gatherings.size(); i++) {
//////                    //Log.i(Constants.TAG, "Gathering Mongo:" + gatherings.get(i).toString());
//////                }
////
////
////
////                List<ContentValues> values = new ArrayList<ContentValues>();
////
////                //loop over the returned values and convert them to pogos
////                for (int i = 0; i < gatherings.size(); i++) {
////                    // Convert mongo db doc to pojo and store in content provider
////                    Gathering_Pojo gathering = Utils_ConvertToPojo.convertGatheringMongoToPojo(gatherings.get(i));
////
////                    //Log.i(Constants.TAG, "Gathering Pojo:" + gathering.toString());
////
////                    values.add(Utils_ContentValues.extractGatheringValues(gathering));
////                }
////
////                /*
////                 * Set the returned data to the adapter
////                 */
////                //mAdapter.setGatheringData(gatherings);
////
////                // Delete all patients and restore them in the provider
////                ContentResolver resolver = getContentResolver();
////                resolver.delete(Contract_MyGathering.GatheringEntry.CONTENT_URI, null, null);
////
////                // Bulk Insert our new weather data into Sunshine's Database
////                getApplicationContext().getContentResolver().bulkInsert(
////                        Contract_MyGathering.GatheringEntry.CONTENT_URI,
////                        values.toArray(new ContentValues[gatherings.size()]));
////
////            }
////
////            @Override
////            public void onFailure(Call<List<Gathering>> call, Throwable t) {
////                // Log error here since request failed
////                Log.i(Constants.TAG, "We got an error somewhere");
////                Log.e(Constants.TAG, t.toString());
////            }
////        });
//
//    }









}
