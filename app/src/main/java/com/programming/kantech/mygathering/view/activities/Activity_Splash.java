package com.programming.kantech.mygathering.view.activities;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.programming.kantech.mygathering.R;
import com.programming.kantech.mygathering.data.model.mongo.GatheringTopic;
import com.programming.kantech.mygathering.data.model.mongo.GatheringType;
import com.programming.kantech.mygathering.data.retrofit.ApiClient;
import com.programming.kantech.mygathering.data.retrofit.ApiInterface;
import com.programming.kantech.mygathering.provider.Contract_MyGathering;
import com.programming.kantech.mygathering.utils.Constants;
import com.programming.kantech.mygathering.utils.Utils_ContentValues;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Activity_Splash extends AppCompatActivity {

    private ApiInterface apiService;

    private static final long SPLASH_DISPLAY_LENGTH = 7000L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        apiService = ApiClient.getClient().create(ApiInterface.class);

        //mFrameLayout = findViewById(R.id.);

        ProgressDialog progressDialog = new ProgressDialog(Activity_Splash.this,
                R.style.Theme_AppCompat_Light_Dialog);

        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Fetching data from server...");
        //progressDialog.show();

        // ToDo: get all static data from the server required to initialize the app

        getGatheringTopics();
        getGatheringTypes();




        //onStartAnimation();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {



                //progressDialog.dismiss();
                Intent mainIntent = new Intent(Activity_Splash.this, Activity_Login.class);
                Activity_Splash.this.startActivity(mainIntent);
                Activity_Splash.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);




    }



    private void getGatheringTopics() {

        Call<List<GatheringTopic>> call = apiService.getGatheringTopics();

        call.enqueue(new Callback<List<GatheringTopic>>() {
            @Override
            public void onResponse(@NonNull Call<List<GatheringTopic>>call, @NonNull Response<List<GatheringTopic>> response) {

                //Log.i(Constants.TAG, "Response:" + response);
                List<GatheringTopic> topics = response.body();
                //Log.d(Constants.TAG, "Number of Gathering Types received: " + topic.size());

                // Make sure the call retrieved some data
                if(topics != null) {

                    // Get an instance of the content resolver
                    ContentResolver resolver = getContentResolver();

                    // Delete all topics and restore them in the provider
                    resolver.delete(Contract_MyGathering.TopicEntry.CONTENT_URI, null, null);

                    for (int i = 0; i < topics.size(); i++) {
                        resolver.insert(Contract_MyGathering.TopicEntry.CONTENT_URI,
                                Utils_ContentValues.extractGatheringTopicValues(topics.get(i)));
                    }

                }

//                for (int i = 0; i < types.size(); i++) {
//                    Log.i(Constants.TAG, "Type:" + types.get(i).getId());
//                }
            }

            @Override
            public void onFailure(@NonNull Call<List<GatheringTopic>>call, @NonNull Throwable t) {
                // Log error here since request failed
                Log.e(Constants.TAG, t.toString());
            }
        });

    }

    private void getGatheringTypes() {

        Call<List<GatheringType>> call = apiService.getGatheringTypes();

        call.enqueue(new Callback<List<GatheringType>>() {
            @Override
            public void onResponse(@NonNull Call<List<GatheringType>>call, @NonNull Response<List<GatheringType>> response) {

                //Log.i(Constants.TAG, "Response:" + response);
                List<GatheringType> types = response.body();
                //Log.d(Constants.TAG, "Number of Gathering Types received: " + topic.size());

                // Make sure the call retrieved some data
                if(types != null) {

                    // Get an instance of the content resolver
                    ContentResolver resolver = getContentResolver();

                    // Delete all gathering types and restore them in the provider
                    int recs = resolver.delete(Contract_MyGathering.TypeEntry.CONTENT_URI, null, null);
                    Log.e(Constants.TAG, "Types deleted:" + recs);


                    for (int i = 0; i < types.size(); i++) {
                        resolver.insert(Contract_MyGathering.TypeEntry.CONTENT_URI,
                                Utils_ContentValues.extractGatheringTypeValues(types.get(i)));
                    }

                }


            }

            @Override
            public void onFailure(@NonNull Call<List<GatheringType>>call, @NonNull Throwable t) {
                // Log error here since request failed
                Log.e(Constants.TAG, t.toString());
            }
        });

    }
}