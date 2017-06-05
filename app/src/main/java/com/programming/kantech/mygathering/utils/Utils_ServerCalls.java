package com.programming.kantech.mygathering.utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.os.AsyncTask;
import android.util.Log;

import com.programming.kantech.mygathering.data.model.mongo.Gathering;
import com.programming.kantech.mygathering.data.model.pojo.Gathering_Pojo;
import com.programming.kantech.mygathering.data.model.pojo.Query_Search;
import com.programming.kantech.mygathering.data.retrofit.ApiInterface;
import com.programming.kantech.mygathering.provider.Contract_MyGathering;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by patrick keogh on 2017-06-04.
 */

public class Utils_ServerCalls {

    private static ApiInterface apiService;


//    public static boolean searchForGatherings(Query_Search query){
//
//        List<Gathering> mygatherings;
//
//        Call<List<Gathering>> call = apiService.getGatherings(query);
//
//        //Call<List<Gathering>> call = apiService.getGatherings();
//
//        call.enqueue(new Callback<List<Gathering>>() {
//            @Override
//            public void onResponse(Call<List<Gathering>> call, Response<List<Gathering>> response) {
//
//                Log.i(Constants.TAG, "Response:" + response);
//                List<Gathering> mygatherings = response.body();
//                Log.d(Constants.TAG, "Number of gatherings returned: " + mygatherings.size());
//
//                //return gatherings;
//
//            }
//
//            @Override
//            public void onFailure(Call<List<Gathering>> call, Throwable t) {
//                // Log error here since request failed
//                Log.i(Constants.TAG, "We got an error somewhere");
//                Log.e(Constants.TAG, t.toString());
//            }
//        });
//
//        boolean gatherings;
//        return gatherings;
//    }

//    public class getGatheringsFromServer extends AsyncTask<Call, Void, List<Gathering>> {
//
////        @Override
////        protected String doInBackground(Call… params) {
////            try {
////                Call<List<Contributor>> call = params[0];
////                Response<List<Contributor>> response = call.execute();
////                return response.body().toString();
////            } catch (IOException e) {
////                e.printStackTrace();
////            }
////            return null;
////        <List<Gathering>
////        }
//
//        @Override
//        protected List<Gathering> doInBackground(Call... params) {
//
//            try {
//                Call<List<Gathering>> call = params[0];
//                Response<List<Gathering>> response = call.execute();
//
//                List<Gathering> gatherings = response.body();
//                return gatherings;
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(List<Gathering> gatherings) {
//
//            //final TextView textView = (TextView) findViewById(R.id.textView);
//            //textView.setText(result);
//
//            List<ContentValues> values = new ArrayList<ContentValues>();
//
//            //loop over the returned values and convert them to pogos
//            for (int i = 0; i < gatherings.size(); i++) {
//                // Convert mongo db doc to pojo and store in content provider
//                Gathering_Pojo gathering = Utils_ConvertToPojo.convertGatheringMongoToPojo(gatherings.get(i));
//
//                //Log.i(Constants.TAG, "Gathering Pojo:" + gathering.toString());
//
//                values.add(Utils_ContentValues.extractGatheringValues(gathering));
//            }
//
//                /*
//                 * Set the returned data to the adapter
//                 */
//            //mAdapter.setGatheringData(gatherings);
//
//            // Delete all patients and restore them in the provider
//            ContentResolver resolver = mContext.getContentResolver();
//            resolver.delete(Contract_MyGathering.GatheringEntry.CONTENT_URI, null, null);
//
//            // Bulk Insert our new weather data into Sunshine's Database
//            getApplicationContext().getContentResolver().bulkInsert(
//                    Contract_MyGathering.GatheringEntry.CONTENT_URI,
//                    values.toArray(new ContentValues[gatherings.size()]));
//
//
//        }
//    }


    /**
     * Ensure this class is only used as a utility.
     */
    private Utils_ServerCalls() {
        throw new AssertionError();
    }


}
