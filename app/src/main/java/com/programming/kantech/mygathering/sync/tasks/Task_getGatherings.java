package com.programming.kantech.mygathering.sync.tasks;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.programming.kantech.mygathering.data.model.mongo.Gathering;
import com.programming.kantech.mygathering.data.model.pojo.Gathering_Pojo;
import com.programming.kantech.mygathering.provider.Contract_MyGathering;
import com.programming.kantech.mygathering.utils.Constants;
import com.programming.kantech.mygathering.utils.Utils_ContentValues;
import com.programming.kantech.mygathering.utils.Utils_ConvertToPojo;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by patrick keogh on 2017-06-04.
 */

public class Task_getGatherings extends AsyncTask<Call, Void, List<Gathering>> {

    private WeakReference<Context> mContext;

    public Task_getGatherings(WeakReference<Context> mContext) {
        this.mContext = mContext;
    }

    @Override
    protected List<Gathering> doInBackground(Call... params) {
        try {
            Call<List<Gathering>> call = params[0];
            Response<List<Gathering>> response = call.execute();

            List<Gathering> gatherings = response.body();
            return gatherings;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<Gathering> gatherings) {

        Log.i(Constants.TAG, "Entered onPostExecute() in Task getGatherings: " + gatherings.size());

        List<ContentValues> values = new ArrayList<ContentValues>();

        //loop over the returned values and convert them to pogos
        for (int i = 0; i < gatherings.size(); i++) {
            // Convert mongo db doc to pojo and store in content provider
            Gathering_Pojo gathering = Utils_ConvertToPojo.convertGatheringMongoToPojo(gatherings.get(i));

            //Log.i(Constants.TAG, "Gathering Pojo:" + gathering.toString());

            values.add(Utils_ContentValues.extractGatheringValues(gathering));
        }

                /*
                 * Set the returned data to the adapter
                 */
        //mAdapter.setGatheringData(gatherings);

        // Delete all patients and restore them in the provider
        ContentResolver resolver = mContext.get().getContentResolver();
        resolver.delete(Contract_MyGathering.GatheringEntry.CONTENT_URI, null, null);

        // Bulk Insert our new weather data into Sunshine's Database
        mContext.get().getContentResolver().bulkInsert(
                Contract_MyGathering.GatheringEntry.CONTENT_URI,
                values.toArray(new ContentValues[gatherings.size()]));
    }
}

//
//public class MyCustomTask extends AsyncTask<Void, Void, Long> {
//
//    private Context mContext;
//
//    public MyCustomTask (Context context){
//        mContext = context;
//    }
//
//    //other methods like onPreExecute etc.
//    protected void onPostExecute(Long result) {
//        Toast.makeText(mContext,"Subiendo la foto. ¡Tras ser moderada empezara a ser votada!: ", Toast.LENGTH_LONG).show();
//    }
//}
