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
import com.programming.kantech.mygathering.utils.Utils_Notifications;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by patrick keogh on 2017-06-04.
 */

public class Task_getNewGatheringsCount extends AsyncTask<Call, Void, List<Gathering>> {


    private WeakReference<Context> mContext;

    public Task_getNewGatheringsCount(WeakReference<Context> mContext) {
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
        Log.i(Constants.TAG, "Entered onPostExecute() in Task_getNewGatheringsCount: " + gatherings.size());

        if(gatherings.size() > 0 ){
            Log.i(Constants.TAG, "More than 1 gathering count:");
            // Send notification that there are new gathering

            Utils_Notifications.remindUserOfGathering(mContext.get());
        }
    }
}
