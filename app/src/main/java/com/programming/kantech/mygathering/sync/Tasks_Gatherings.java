package com.programming.kantech.mygathering.sync;

import android.content.Context;
import android.util.Log;

import com.programming.kantech.mygathering.data.model.mongo.Gathering;
import com.programming.kantech.mygathering.data.model.pojo.Query_Search;
import com.programming.kantech.mygathering.data.retrofit.ApiClient;
import com.programming.kantech.mygathering.data.retrofit.ApiInterface;
import com.programming.kantech.mygathering.sync.tasks.Task_getGatherings;
import com.programming.kantech.mygathering.sync.tasks.Task_getNewGatheringsCount;
import com.programming.kantech.mygathering.utils.Constants;
import com.programming.kantech.mygathering.utils.Utils_Preferences;
import com.programming.kantech.mygathering.utils.Utils_ServerCalls;

import java.lang.ref.WeakReference;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;

/**
 * Created by patrick keogh on 2017-06-03.
 */

public class Tasks_Gatherings {

    private static ApiInterface apiService;

    public static final String ACTION_SEARCH_FOR_NEW_GATHERINGS = "search-for-new-gatherings";

    public static void executeTask(Context context, String action) {
        if (ACTION_SEARCH_FOR_NEW_GATHERINGS.equals(action)) {
            getNewGatheringsCount(context);
        }
    }

    private static void getNewGatheringsCount(Context context) {

        apiService = ApiClient.getClient().create(ApiInterface.class);

        String distance = Utils_Preferences.getPreferredDistance(context);
        float lat = 113;
        float lng = -75;

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);

        //current data and time - 1 hr
        final Date date = new Date(System.currentTimeMillis() - 3600 * 1000);

        final String ISO_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
        final SimpleDateFormat sdf = new SimpleDateFormat(ISO_FORMAT);
        //final TimeZone utc = TimeZone.getTimeZone("UTC");
        //sdf.setTimeZone(utc);
        Log.i(Constants.TAG, "start-date:" + sdf.format(date));

        List<Double> coords = Arrays.asList(-79.691124, 44.38760379999999);

        Query_Search query = new Query_Search(coords, 200000, sdf.format(date));

        Call<List<Gathering>> call = apiService.getGatherings(query);

        WeakReference<Context> mContext = new WeakReference<Context>(context);

        new Task_getNewGatheringsCount(mContext).execute(call);

    }


}
