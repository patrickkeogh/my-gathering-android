package com.programming.kantech.mygathering.data.retrofit;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.programming.kantech.mygathering.utils.Constants;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by patrik keogh on 2017-04-08.
 */

public class ApiClient_FileStack {

    private static Retrofit retrofit = null;

    Gson gson = new GsonBuilder()
            .setLenient()
            .create();

    public static Retrofit getClient() {
        if (retrofit==null) {

            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            OkHttpClient client = new OkHttpClient();

            retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL_FILESTACK)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }
}
