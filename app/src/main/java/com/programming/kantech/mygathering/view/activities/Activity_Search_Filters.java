package com.programming.kantech.mygathering.view.activities;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;

import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.programming.kantech.mygathering.R;
import com.programming.kantech.mygathering.data.model.pojo.Query_Search;
import com.programming.kantech.mygathering.provider.Contract_MyGathering;
import com.programming.kantech.mygathering.utils.Constants;
import com.programming.kantech.mygathering.utils.Utils_Preferences;
import com.programming.kantech.mygathering.view.ui.Adapter_PlaceList;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Activity_Search_Filters extends AppCompatActivity {

    // Member variables
    private TextView tv_location_name;
    private TextView tv_location_address;
    private EditText et_distance;
    private Spinner sp_topic;
    private Spinner sp_type;

    private double mLat = 0;
    private double mLong = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_filters);
        Log.i(Constants.TAG, "Activity Search Filters loaded");

        tv_location_name = (TextView) findViewById(R.id.tv_location_name);
        tv_location_address = (TextView) findViewById(R.id.tv_location_address);
        et_distance = (EditText) findViewById(R.id.et_search_distance);
        sp_topic = (Spinner) findViewById(R.id.sp_topics);
        sp_type = (Spinner) findViewById(R.id.sp_types);

        // Load default values from prefs if available
        loadDefaultPrefs();

    }

    private void loadDefaultPrefs() {

        // Get the preferred distance and convert to a string
        float distance = Float.parseFloat(Utils_Preferences.getPreferredDistance(this));
        String strDistance = Float.toString(distance);
        et_distance.setText(strDistance);

        // Get the list of topics from Constants
        // TODO: Load topics from Resources so they can be translated

        String[] topics = Constants.GATHERING_TOPICS;
        String[] types = Constants.GATHERING_TYPES;

        final String prefTopic = Utils_Preferences.getPreferredTopic(this);
        final String prefType = Utils_Preferences.getPreferredType(this);


        ArrayAdapter<String> topics_dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, topics );

        ArrayAdapter<String> types_dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, types );

        // Drop down layout style - list view with radio button
        topics_dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        types_dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        sp_topic.setAdapter(topics_dataAdapter);
        sp_type.setAdapter(types_dataAdapter);

        sp_topic.setSelection(topics_dataAdapter.getPosition(prefTopic));
        sp_type.setSelection(types_dataAdapter.getPosition(prefType));




    }

    /***
     * Called when the Location Select Activity returns back with a selected place (or after canceling)
     *
     * @param requestCode The request code passed when calling startActivityForResult
     * @param resultCode  The result code specified by the second activity
     * @param data        The Intent that carries the result data.
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(Constants.TAG, "onActivityResult");

        // Check if this is the result for the Location Select Activity request
        if (requestCode == Constants.REQUEST_GET_LOCATION && resultCode == RESULT_OK) {

            String mLocationAddress = data.getStringExtra(Constants.EXTRA_LOCATION_ADDRESS);
            String mLocationName = data.getStringExtra(Constants.EXTRA_LOCATION_NAME);

            mLat = data.getDoubleExtra(Constants.EXTRA_LOCATION_LAT, 0);
            mLong = data.getDoubleExtra(Constants.EXTRA_LOCATION_LONG, 0);


            Log.i(Constants.TAG, "location Name:" + mLocationName);

            tv_location_name.setText(mLocationName);
            tv_location_address.setText(mLocationAddress);


//
//
//
//            Place place = PlacePicker.getPlace(this, data);
//            if (place == null) {
//                Log.i(TAG, "No place selected");
//                return;
//            }
//
//            // Extract the place information from the API
//            String placeName = place.getName().toString();
//            String placeAddress = place.getAddress().toString();
//            String placeID = place.getId();
//
//            // Insert a new place into DB
//            ContentValues contentValues = new ContentValues();
//            contentValues.put(PlaceContract.PlaceEntry.COLUMN_PLACE_ID, placeID);
//            getContentResolver().insert(PlaceContract.PlaceEntry.CONTENT_URI, contentValues);
//
//            // Get live data information
//            refreshPlacesData();
        }
    }


    /***
     * Button Click event handler to handle clicking the "Location" TextView
     */
    public void onSelectLocationClicked(View view) {
        Intent intent = new Intent(this, Activity_Location_Select.class);
        startActivityForResult(intent, Constants.REQUEST_GET_LOCATION);
    }

    /***
     * Button Click event handler to handle clicking the "Search" Button
     *
     * @param view
     */
    public void onSearchButtonClicked(View view) {
        Log.i(Constants.TAG, "onSearchButtonClicked");

        Query_Search query = new Query_Search();

        float distance = 10000;
        Log.i(Constants.TAG, "mLat:" + mLat);
        Log.i(Constants.TAG, "mLong:" + mLong);

        // Check if a location was selected
        if(mLong != 0 && mLat != 0 ) {
            List<Double> coords = Arrays.asList(mLong, mLat); //Reverse for mongo (lng, lat)

            query.setDistance(distance);
            query.setCoordinates(coords);
        }







        String topic = sp_topic.getSelectedItem().toString();
        Log.i(Constants.TAG, "selected Topic:" + topic);
        String type = sp_type.getSelectedItem().toString();

        if (!topic.equals("All")) query.setTopic(topic);
        if (!type.equals("All")) query.setType(type);



        Intent returnIntent = new Intent();
        returnIntent.putExtra(Constants.EXTRA_SEARCH_QUERY, query);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();


        // Load the search preferences from preferences
//        float distance = Float.parseFloat(Utils_Preferences.getPreferredDistance(this));
//        distance = distance * 1000;
//        final String topic = Utils_Preferences.getPreferredTopic(this);
//        final String type = Utils_Preferences.getPreferredType(this);
//        Log.i(Constants.TAG, "Topic in Preferences:" + topic);
//
//        // test coords for Barrie ON CA
//        double lng = -79.691124;
//        double lat = 44.38760379999999;
//

//        List<Double> coords = Arrays.asList(lng, lat); //Reverse for mongo (lng, lat)
//
//        Query_Search query = new Query_Search();
//
//        query.setDistance(distance);
//        query.setCoordinates(coords);
//
//        if (!topic.equals("All")) query.setTopic(topic);
//        if (!type.equals("All")) query.setType(type);


    }


}
