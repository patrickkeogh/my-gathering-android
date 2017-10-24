package com.programming.kantech.mygathering.data.retrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by patrick keogh on 2017-09-25.
 * An Interface to query for the address details of a Place
 */

public interface ApiInterface_FileStack {

    // Parameters used
    String PARAM_PLACE_ID = "/crop";


    // Paths for the server
    String ROOT_PATH = "/crop";

    //https://process.filestackapi.com/crop=dim:[0,0, 720, 360]/0q9PIiJtSlydpgQ67Rvg

    @GET
    Call<okhttp3.ResponseBody> getCroppedImage(@Url String url);




}
