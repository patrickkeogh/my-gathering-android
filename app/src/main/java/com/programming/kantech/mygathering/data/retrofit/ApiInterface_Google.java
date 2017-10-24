package com.programming.kantech.mygathering.data.retrofit;

import com.programming.kantech.mygathering.data.model.google.AddressResults;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by patrick keogh on 2017-09-25.
 * An Interface to query for the address details of a Place
 */

public interface ApiInterface_Google {

    // Parameters used
    String PARAM_PLACE_ID = "placeid";


    // Paths for the server
    String ROOT_PATH = "json";

    @GET(ROOT_PATH)
    Call<AddressResults> getAddressDetails(@Query(PARAM_PLACE_ID) String origin_place);




}
