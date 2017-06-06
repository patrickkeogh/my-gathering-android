package com.programming.kantech.mygathering.view.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;
import android.util.Log;
import android.widget.Toast;

import com.programming.kantech.mygathering.R;
import com.programming.kantech.mygathering.sync.ReminderUtilities;
import com.programming.kantech.mygathering.utils.Constants;
import com.programming.kantech.mygathering.utils.Utils_General;

/**
 * Created by patrick keogh on 2017-05-22.
 */

public class fragment_settings extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener,
        Preference.OnPreferenceChangeListener {
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
                        Preference pref = cat.getPreference(x);

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

                    Log.i(Constants.TAG, "Key in Prefs:" + p.getKey());

                    Log.i(Constants.TAG, "Value in Prefs:" + value);


                    setPreferenceSummary(p, value);

                }


            }
        }

        // COMPLETED (3) Add the OnPreferenceChangeListener specifically to the EditTextPreference
        // Add the preference listener which checks that the size is correct to the size preference
        Preference preference = findPreference(getString(R.string.pref_preferred_gathering_distance_key));
        preference.setOnPreferenceChangeListener(this);

        preference = findPreference(getString(R.string.pref_allow_notifications_key));
        preference.setOnPreferenceChangeListener(this);


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

    // COMPLETED (12) Register SettingsFragment (this) as a SharedPreferenceChangedListener in onStart
    @Override
    public void onStart() {
        super.onStart();
                /* Register the preference change listener */
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    /**
     * Updates the summary for the preference
     *
     * @param preference The preference to be updated
     * @param value      The value that the preference was updated to
     */
    private void setPreferenceSummary(Preference preference, String value) {
        // COMPLETED (3) Don't forget to add code here to properly set the summary for an EditTextPreference
//        if (preference instanceof ListPreference) {
//            // For list preferences, figure out the label of the selected value
//            ListPreference listPreference = (ListPreference) preference;
//            int prefIndex = listPreference.findIndexOfValue(value);
//            if (prefIndex >= 0) {
//                // Set the summary to that label
//                listPreference.setSummary(listPreference.getEntries()[prefIndex]);
//            }
//        } else
        if (preference instanceof EditTextPreference) {
            // For EditTextPreferences, set the summary to the value's simple string representation.
            preference.setSummary(value);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

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

        Log.i(Constants.TAG, "onPreferenceChange() called");
        // In this context, we're using the onPreferenceChange listener for checking whether the
        // size setting was set to a valid value.

        String error = "Location distance must be a number between 1 and 10,000,000";

        // Make sure the preference is for distance
        String distanceKey = getString(R.string.pref_preferred_gathering_distance_key);

        String notificationsKey = getString(R.string.pref_allow_notifications_key);



        if (preference.getKey().equals(notificationsKey)) {
            Log.i(Constants.TAG, "Key in onPrefChange:" + notificationsKey);

            Boolean isAllowed = (Boolean) newValue;

            if(!isAllowed){
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
}
