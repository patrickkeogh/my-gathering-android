package com.programming.kantech.mygathering.view.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.programming.kantech.mygathering.R;
import com.programming.kantech.mygathering.sync.ReminderUtilities;
import com.programming.kantech.mygathering.utils.Constants;
import com.programming.kantech.mygathering.utils.Utils_General;
import com.programming.kantech.mygathering.utils.Utils_Preferences;

import java.util.Objects;

import static android.app.Activity.RESULT_OK;

/**
 * Created by patrick keogh on 2017-05-22.
 *
 */

public class Fragment_Settings extends PreferenceFragmentCompat implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        SharedPreferences.OnSharedPreferenceChangeListener,
        Preference.OnPreferenceChangeListener,
        Preference.OnPreferenceClickListener {

    // Member variables
    private GoogleApiClient mClient;
    private String mPlaceId = "";

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (!Objects.equals(mPlaceId, "")) {

            // Get the location info for the Main Address
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(mClient, mPlaceId);

            placeResult.setResultCallback(new ResultCallback<PlaceBuffer>() {
                @Override
                public void onResult(@NonNull PlaceBuffer places) {

                    Log.i(Constants.TAG, "PLACE:" + places.get(0).getAddress().toString());

                    //tv_customer_address.setText(places.get(0).getAddress().toString());

                    Preference pref = findPreference(getString(R.string.pref_preferred_gathering_location_key));
                    pref.callChangeListener(places.get(0));


                }
            });

        }


    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void clearLocationPref() {
        Log.i(Constants.TAG, "clearLocationCalled()");

        Utils_Preferences.savePreferredLocation(getContext(), "");

        Preference pref = findPreference(getString(R.string.pref_preferred_gathering_location_key));
        pref.callChangeListener(null);

    }


    /**
     * Called during {@link #onCreate(Bundle)} to supply the preferences for this fragment.
     * Subclasses are expected to call {@link #setPreferenceScreen(PreferenceScreen)} either
     * directly or via helper methods such as {@link #addPreferencesFromResource(int)}.
     *
     * @param savedInstanceState If the fragment is being re-created from
     *                           a previous saved state, this is the state.
     * @param rootKey            If non-null, this preference fragment should be rooted at the
     *                           {@link PreferenceScreen} with this key.
     */
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

        addPreferencesFromResource(R.xml.pref_general);


        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
        PreferenceScreen prefScreen = getPreferenceScreen();
        int count = prefScreen.getPreferenceCount();

        Log.i(Constants.TAG, "Count in Prefs:" + count);


        //initSummary(getPreferenceScreen());

        // Go through all of the preferences, and set up their preference summary.
        for (int i = 0; i < count; i++) {
            Preference p = prefScreen.getPreference(i);

            // You don't need to set up preference summaries for checkbox preferences because
            // they are already set up in xml using summaryOff and summary On
            if (!(p instanceof CheckBoxPreference)) {

                if (p instanceof PreferenceCategory) {
                    Log.i(Constants.TAG, "We have a category");

                    PreferenceCategory cat = (PreferenceCategory) p;


                    for (int x = 0; x < cat.getPreferenceCount(); x++) {
                        final Preference pref = cat.getPreference(x);

                        // Get the keys for the prefs we want to validate
                        String topicKey = getString(R.string.pref_preferred_gathering_topic_key);
                        String typeKey = getString(R.string.pref_preferred_gathering_type_key);
                        String locationKey = getString(R.string.pref_preferred_gathering_location_key);

                        // Load Place id if one has been stored in prefs
                        if (pref.getKey().equals(locationKey)) {

                            mPlaceId = Utils_Preferences.getPreferredLocation(getContext());

                            // Get a reference to the Clear Button
                            Preference button = findPreference(getString(R.string.pref_remove_preferred_gathering_location_key));

                            // Hide the button if a location has not been selected
                            if (!Objects.equals(mPlaceId, "")) {
                                button.setVisible(true);
                            } else {
                                button.setVisible(false);

                            }
                        }

                        // Load the entries and entry values from Constants
                        // since we are not supporting multiple languages
                        if (pref.getKey().equals(topicKey)) {
                            //mFilterTypes = getResources().getStringArray(R.array.gathering_types);

                            // The value and id are both the same for this data
                            CharSequence[] topics = getResources().getStringArray(R.array.gathering_topics);
                            //CharSequence[] entryValues = topics;
                            ListPreference lp = (ListPreference) findPreference(topicKey);
                            lp.setEntries(topics);
                            lp.setEntryValues(topics);
                        }

                        if (pref.getKey().equals(typeKey)) {
                            String[] types = getResources().getStringArray(R.array.gathering_types);
                            //CharSequence[] entryValues = types;
                            ListPreference lp = (ListPreference) findPreference(typeKey);
                            lp.setEntries(types);
                            lp.setEntryValues(types);
                        }

                        // You don't need to set up preference summaries for checkbox preferences because
                        // they are already set up in xml using summaryOff and summary On
                        if (!(pref instanceof CheckBoxPreference)) {
                            Log.i(Constants.TAG, "Key in cat Prefs:" + pref.getKey());
                            String value = sharedPreferences.getString(pref.getKey(), "");
                            setPreferenceSummary(pref, value);
                        }
                    }
                } else {

                    String value = sharedPreferences.getString(p.getKey(), "");

                    //Log.i(Constants.TAG, "Key in Prefs:" + p.getKey());

                    //Log.i(Constants.TAG, "Value in Prefs:" + value);


                    setPreferenceSummary(p, value);

                }


            }
        }

        // Add the preference listener which checks that the size is correct to the size preference
//        Preference preference = findPreference(getString(R.string.pref_preferred_gathering_distance_key));
//        preference.setOnPreferenceChangeListener(this);

        Preference preference = findPreference(getString(R.string.pref_allow_notifications_key));
        preference.setOnPreferenceChangeListener(this);

        preference = findPreference(getString(R.string.pref_preferred_gathering_location_key));
        preference.setOnPreferenceChangeListener(this);

        preference.setOnPreferenceClickListener(this);

        preference = findPreference(getString(R.string.pref_remove_preferred_gathering_location_key));
        preference.setOnPreferenceClickListener(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
                /* Unregister the preference change listener */
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
                /* Register the preference change listener */
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);

        if (mClient == null) {
            buildApiClient();
            mClient.connect();
        } else {
            if (!mClient.isConnected()) {
                mClient.connect();
            }
        }
    }

    /**
     * Updates the summary for the preference
     *
     * @param preference The preference to be updated
     * @param value      The value that the preference was updated to
     */
    private void setPreferenceSummary(Preference preference, String value) {
        // set the summary for a ListPreference or EditTextPreference
        if (preference instanceof ListPreference) {
            // For list preferences, figure out the label of the selected value
            ListPreference listPreference = (ListPreference) preference;
            int prefIndex = listPreference.findIndexOfValue(value);
            if (prefIndex >= 0) {
                // Set the summary to that label
                listPreference.setSummary(listPreference.getEntries()[prefIndex]);
            }
        } else if (preference instanceof EditTextPreference) {
            // For EditTextPreferences, set the summary to the value's simple string representation.
            preference.setSummary(value);
        }
    }

    /***
     * Called when the Place Picker Activity returns back with a selected place (or after canceling)
     *
     * @param requestCode The request code passed when calling startActivityForResult
     * @param resultCode  The result code specified by the second activity
     * @param data        The Intent that carries the result data.
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_CODE_PLACE_PICKER && resultCode == RESULT_OK) {
            Place place = PlacePicker.getPlace(getContext(), data);

//            if (place == null) {
//                Log.i(Constants.TAG, "No place selected");
//                return;
//            } else {
//                Log.i(Constants.TAG, "Place info returned:" + place.toString());
//            }

            // Extract the place information from the API
//            String placeName = place.getName().toString();
//            String placeAddress = place.getAddress().toString();
//            String placeID = place.getId();
//            final LatLng latlng = place.getLatLng();

            Preference pref = findPreference(getString(R.string.pref_preferred_gathering_location_key));
            pref.callChangeListener(place);

        }
    }


    @Override
    public boolean onPreferenceClick(Preference preference) {
        //Log.i(Constants.TAG, "onPreferenceClick() called:" + preference.getKey());

        // Catch the get location button click
        if (Objects.equals(preference.getKey(), getResources().getString(R.string.pref_preferred_gathering_location_key))) {

            try {

                // Start a Place Picker to get a preferred search starting location
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                Intent intent = builder.build(getActivity());
                startActivityForResult(intent, Constants.REQUEST_CODE_PLACE_PICKER);

            } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                Log.e(Constants.TAG, e.toString());
            }

            return true;
        }

        // Catch the clear location button click
        if (Objects.equals(preference.getKey(), getResources().getString(R.string.pref_remove_preferred_gathering_location_key))) {

            //Log.i(Constants.TAG, "remove a location called");
            clearLocationPref();
            return true;
        }

        return false;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        Log.i(Constants.TAG, "onSharedPreferenceChanged() called");

        // Figure out which preference was changed
        Preference preference = findPreference(key);
        if (null != preference) {
            // Updates the summary for the preference
            if (!(preference instanceof CheckBoxPreference)) {
                String value = sharedPreferences.getString(preference.getKey(), "");
                setPreferenceSummary(preference, value);
            }
        }

    }

    /**
     * Called when a Preference has been changed by the user. This is
     * called before the state of the Preference is about to be updated and
     * before the state is persisted.
     *
     * @param preference The changed Preference.
     * @param newValue   The new value of the Preference.
     * @return True to update the state of the Preference with the new value.
     */
    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {

        //Log.i(Constants.TAG, "onPreferenceChange() called");

        String error = "Location distance must be a number between 1 and 10,000,000";

        // Get the keys for the prefs we want to validate
        String distanceKey = getString(R.string.pref_preferred_gathering_distance_key);
        String notificationsKey = getString(R.string.pref_allow_notifications_key);
        String locationKey = getString(R.string.pref_preferred_gathering_location_key);

        if (preference.getKey().equals(locationKey)) {
            //Log.i(Constants.TAG, "Location selected:" + locationKey);

            //  Check if the location has been changed to null
            Preference pref = findPreference(getString(R.string.pref_remove_preferred_gathering_location_key));

            if (newValue != null) {
                Place place = (Place) newValue;

                //Utils_General.showToast(getContext(), "Place is " + place.getAddress());
                preference.setSummary(place.getAddress());
                Utils_Preferences.savePreferredLocation(getContext(), place.getId());
                pref.setVisible(true);
            } else {
                preference.setSummary(getString(R.string.pref_preferred_gathering_location_default));
                Utils_Preferences.savePreferredLocation(getContext(), "");
                pref.setVisible(false);
            }

        }


        if (preference.getKey().equals(notificationsKey)) {
            //Log.i(Constants.TAG, "Key in onPrefChange:" + notificationsKey);

            Boolean isAllowed = true;

            try {
                isAllowed = (Boolean) newValue;
            } catch (ClassCastException exc) {
                Log.e(Constants.TAG, exc.toString());

            }

            if (!isAllowed) {
                ReminderUtilities.unscheduleNewGatheringSearch(getContext());
            }
        }

        if (preference.getKey().equals(distanceKey)) {
            Log.i(Constants.TAG, "Key in onPrefChange:" + distanceKey);

            String stringDistance = (String) newValue;
            try {
                float distance = Float.parseFloat((stringDistance));

                // Distance must be betwenn 1 and 10,000,000 km
                if (distance > 10000000 || distance < 1) {

                    Utils_General.showToast(getContext(), error);
                    return false;

                }

            } catch (NumberFormatException nfe) {
                // If whatever the user entered can't be parsed to a number, show an error
                Utils_General.showToast(getContext(), error);
                return false;
            }
        }

        return true;
    }

    private void buildApiClient() {
        Log.i(Constants.TAG, "buildApiClient() called");

        if (mClient == null) {
            Log.i(Constants.TAG, "CREATE NEW GOOGLE CLIENT");

            // Build up the LocationServices API client
            // Uses the addApi method to request the LocationServices API
            // Also uses enableAutoManage to automatically know when to connect/suspend the client
            mClient = new GoogleApiClient.Builder(getContext())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(Places.GEO_DATA_API)
                    .build();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mClient != null) {
            mClient.disconnect();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mClient = null;
    }
}
