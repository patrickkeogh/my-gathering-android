package com.programming.kantech.mygathering.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.programming.kantech.mygathering.R;

/**
 * Created by patrick keogh on 2017-05-20.
 */

public class Utils_Preferences {

    public static String getPreferredEmail(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.pref_preferred_email_key),"");
    }

    public static void savePreferredEmail(Context context, String username) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor spe = sp.edit();
        spe.putString(context.getString(R.string.pref_preferred_email_key), username);
        spe.apply();
    }

    public static void saveLoggedUserEmail(Context context, String email) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor spe = sp.edit();
        spe.putString(context.getString(R.string.pref_logged_email_key), email);
        spe.apply();
    }

    public static String getLoggedUserEmail(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.pref_logged_email_key), "");
    }

    public static Boolean getSaveCredentials(Context context) {
        //Log.i(Constants.TAG, "Enter saveNotifications");
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(context.getString(R.string.pref_save_credentials_key), false);
    }

    public static void saveCredentials(Context context, boolean b) {
        //Log.i(Constants.TAG, "Enter saveNotificationCancellations()");
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor spe = sp.edit();
        spe.putBoolean(context.getString(R.string.pref_save_credentials_key), b);
        spe.apply();
    }

    public static String getPreferredLocation(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.pref_preferred_gathering_location_key),"");
    }

    public static String getPreferredDistance(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.pref_preferred_gathering_distance_key),
                context.getResources().getString(R.string.pref_preferred_gathering_distance_default));
    }

    public static String getPreferredTopic(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.pref_preferred_gathering_topic_key),
                context.getResources().getString(R.string.pref_preferred_gathering_topic_default));
    }

    public static Boolean isNotificationsAllowed(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(context.getString(R.string.pref_allow_notifications_key),
                context.getResources().getBoolean(R.bool.pref_allow_notifications_default));
    }

    public static long getLastSearchDate(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getLong(context.getString(R.string.pref_last_search_date_key),0);
    }

    public static void saveLastSearchDate(Context context, long date) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor spe = sp.edit();
        spe.putLong(context.getString(R.string.pref_last_search_date_key), date);
        spe.apply();
    }


}
