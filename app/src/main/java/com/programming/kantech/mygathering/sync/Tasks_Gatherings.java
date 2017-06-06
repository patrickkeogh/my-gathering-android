package com.programming.kantech.mygathering.sync;

import android.content.Context;
import android.util.Log;

import com.programming.kantech.mygathering.data.model.mongo.Gathering;
import com.programming.kantech.mygathering.data.model.pojo.Query_Search;
import com.programming.kantech.mygathering.data.retrofit.ApiClient;
import com.programming.kantech.mygathering.data.retrofit.ApiInterface;
import com.programming.kantech.mygathering.sync.tasks.Task_getNewGatherings;
import com.programming.kantech.mygathering.utils.Constants;
import com.programming.kantech.mygathering.utils.Utils_DateFormatting;
import com.programming.kantech.mygathering.utils.Utils_Preferences;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import retrofit2.Call;

/**
 * Created by patrick keogh on 2017-06-03.
 */

public class Tasks_Gatherings {

    private static ApiInterface apiService;

    public static final String ACTION_SEARCH_FOR_NEW_GATHERINGS = "search-for-new-gatherings";

    public static void executeTask(Context context, String action) {
        if (ACTION_SEARCH_FOR_NEW_GATHERINGS.equals(action)) {
            getNewGatherings(context);
        }
    }

    private static void getNewGatherings(Context context) {

        Log.i(Constants.TAG, "Entered getNewGatherings in Tasks_Gatherings");


        apiService = ApiClient.getClient().create(ApiInterface.class);

        /*
         * Get the preferred distance from location from preferences
         */
        float distance = Float.parseFloat(Utils_Preferences.getPreferredDistance(context));
        distance = distance * 1000;

        /*
         * Get the preferred topic from location from preferences
         */
        final String topic = Utils_Preferences.getPreferredTopic(context);

        /*
         * Get the preferred topic from location from preferences
         */
        final String type = Utils_Preferences.getPreferredType(context);

        /*
         * Get the last search date and time from preferences
         */
        long lastSearchDate = Utils_Preferences.getLastSearchDate(context);

        // test coords for Barrie ON CA
        double lat = -79.691124;
        double lng = 44.38760379999999;

        //DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);

        final Date date = new Date(lastSearchDate);
        Log.i(Constants.TAG, "last search date and time:" + date);

        String start_date = Utils_DateFormatting.convertLocalToUtc(date);
        Log.i(Constants.TAG, "last search date UTC:" + start_date);
        Log.i(Constants.TAG, "last search date back to Local:" + Utils_DateFormatting.convertUtcToLocal(start_date));
        List<Double> coords = Arrays.asList(-79.691124, 44.38760379999999);

        Query_Search query = new Query_Search();
        query.setCoordinates(coords);
        query.setDistance(distance);
        query.setStart_date(start_date);

        if (!topic.equals("All")) query.setTopic(topic);
        if (!type.equals("All")) query.setType(type);

        Log.i(Constants.TAG, "query:" + query);

        //Query_Search query = new Query_Search(coords, distance, start_date, "NULL");

        Call<List<Gathering>> call = apiService.getNewGatherings(query);

        WeakReference<Context> mContext = new WeakReference<Context>(context);

        new Task_getNewGatherings(mContext).execute(call);



        Utils_Preferences.saveLastSearchDate(context, System.currentTimeMillis());

    }


}
