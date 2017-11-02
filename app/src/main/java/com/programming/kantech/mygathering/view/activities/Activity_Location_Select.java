package com.programming.kantech.mygathering.view.activities;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.programming.kantech.mygathering.R;
import com.programming.kantech.mygathering.provider.Contract_MyGathering;
import com.programming.kantech.mygathering.utils.Constants;
import com.programming.kantech.mygathering.view.ui.Adapter_PlaceList;

import java.util.ArrayList;
import java.util.List;

public class Activity_Location_Select extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        Adapter_PlaceList.PlaceListOnClickHandler {

    // Constants
    private static final int PERMISSIONS_REQUEST_FINE_LOCATION = 11167;
    private static final int PLACE_PICKER_REQUEST = 1;

    // Member variables
    private Adapter_PlaceList mAdapter;
    //private boolean mIsEnabled;
    private GoogleApiClient mClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_select);

        // Set up the recycler view
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.places_list_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new Adapter_PlaceList(this, null, this);
        mRecyclerView.setAdapter(mAdapter);

        // Build up the LocationServices API client
        // Uses the addApi method to request the LocationServices API
        // Also uses enableAutoManage to automatically know when to connect/suspend the client
        mClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(this, this)
                .build();
    }


    /***
     * Called when the Google API Client is successfully connected
     *
     * @param connectionHint Bundle of data provided to clients by Google Play services
     */
    @Override
    public void onConnected(@Nullable Bundle connectionHint) {
        refreshPlacesData();
        Log.i(Constants.TAG, "API Client Connection Successful!");
    }

    /***
     * Called when the Google API Client is suspended
     *
     * @param cause cause The reason for the disconnection. Defined by constants CAUSE_*.
     */
    @Override
    public void onConnectionSuspended(int cause) {
        Log.i(Constants.TAG, "API Client Connection Suspended!");
    }

    /***
     * Called when the Google API Client failed to connect to Google Play Services
     *
     * @param result A ConnectionResult that can be used for resolving the error
     */
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult result) {
        Log.e(Constants.TAG, "API Client Connection Failed!");
    }

    @Override
    public void onResume() {
        super.onResume();

        // Initialize location permissions checkbox
        CheckBox locationPermissions = (CheckBox) findViewById(R.id.location_permission_checkbox);
        if (ActivityCompat.checkSelfPermission(Activity_Location_Select.this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            locationPermissions.setChecked(false);
        } else {
            locationPermissions.setChecked(true);
            locationPermissions.setEnabled(false);
        }
    }

    /***
     * Called when the Place Picker Activity returns back with a selected place (or after canceling)
     *
     * @param requestCode The request code passed when calling startActivityForResult
     * @param resultCode  The result code specified by the second activity
     * @param data        The Intent that carries the result data.
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST && resultCode == RESULT_OK) {
            Place place = PlacePicker.getPlace(this, data);
            if (place == null) {
                Log.i(Constants.TAG, "No place selected");
                return;
            } else {
                Log.i(Constants.TAG, "Place info returned:" + place.toString());

            }

            // Extract the place information from the API
            //String placeName = place.getName().toString();
            //String placeAddress = place.getAddress().toString();
            String placeID = place.getId();
            //final LatLng latlng = place.getLatLng();

            //Log.i(Constants.TAG, "LatLng:" + latlng);

            // Insert a new place into DB
            ContentValues contentValues = new ContentValues();
            contentValues.put(Contract_MyGathering.PlaceEntry.COLUMN_PLACE_ID, placeID);
            getContentResolver().insert(Contract_MyGathering.PlaceEntry.CONTENT_URI, contentValues);

            // Get live data information
            refreshPlacesData();
        }
    }

    /***
     * Button Click event handler to handle clicking the "Add new location" Button
     *
     * @param view the button clicked
     */
    public void onAddPlaceButtonClicked(View view) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, getString(R.string.need_location_permission_message), Toast.LENGTH_LONG).show();
            return;
        }
        try {
            // Start a new Activity for the Place Picker API, this will trigger {@code #onActivityResult}
            // when a place is selected or with the user cancels.
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
            Intent i = builder.build(this);
            startActivityForResult(i, PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            Log.e(Constants.TAG, String.format("GooglePlayServices Not Available [%s]", e.getMessage()));
        } catch (Exception e) {
            Log.e(Constants.TAG, String.format("PlacePicker Exception: %s", e.getMessage()));
        }
    }

    public void onLocationPermissionClicked(View view) {
        Log.i(Constants.TAG, "onLocationPermissionClicked");

        ActivityCompat.requestPermissions(Activity_Location_Select.this,
                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSIONS_REQUEST_FINE_LOCATION);
    }

    public void refreshPlacesData() {
        Uri uri = Contract_MyGathering.PlaceEntry.CONTENT_URI;
        Cursor data = getContentResolver().query(
                uri,
                null,
                null,
                null,
                null);

        if (data == null || data.getCount() == 0) return;
        List<String> guids = new ArrayList<>();
        while (data.moveToNext()) {
            guids.add(data.getString(data.getColumnIndex(Contract_MyGathering.PlaceEntry.COLUMN_PLACE_ID)));
        }
        PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(mClient,
                guids.toArray(new String[guids.size()]));
        placeResult.setResultCallback(new ResultCallback<PlaceBuffer>() {
            @Override
            public void onResult(@NonNull PlaceBuffer places) {
                mAdapter.swapPlaces(places);

            }
        });

        data.close();
    }

    /**
     * This method is for responding to clicks from our list.
     *
     * @param id The id of the Place clicked
     * @param place The Place object clicked
     */
    @Override
    public void onClick(long id, Place place) {
        Log.i(Constants.TAG, "onClick:" + place.getAddress());

        LatLng latlng = place.getLatLng();

        Log.i(Constants.TAG, "Lat:" + latlng.latitude);
        Log.i(Constants.TAG, "Lng:" + latlng.longitude);
        Log.i(Constants.TAG, "name:" + place.getName());


        Intent returnIntent = new Intent();
        returnIntent.putExtra(Constants.EXTRA_LOCATION_NAME, place.getName());
        returnIntent.putExtra(Constants.EXTRA_LOCATION_ADDRESS, place.getAddress());
        returnIntent.putExtra(Constants.EXTRA_LOCATION_LAT, latlng.latitude);
        returnIntent.putExtra(Constants.EXTRA_LOCATION_LONG, latlng.longitude);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }
}
