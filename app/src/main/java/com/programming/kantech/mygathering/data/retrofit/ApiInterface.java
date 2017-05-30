package com.programming.kantech.mygathering.data.retrofit;

import com.programming.kantech.mygathering.data.model.mongo.Gathering;
import com.programming.kantech.mygathering.data.model.mongo.GatheringTopic;
import com.programming.kantech.mygathering.data.model.mongo.GatheringType;
import com.programming.kantech.mygathering.data.model.mongo.Result_Login;
import com.programming.kantech.mygathering.data.model.mongo.Result_Logout;
import com.programming.kantech.mygathering.data.model.mongo.Result_Register;
import com.programming.kantech.mygathering.data.model.mongo.UserCredentials;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by patrik keogh on 2017-04-08.
 */

public interface ApiInterface {

    // Parameters used


    // Users
    public static final String PARAM_ID = "_id";
    public static final String PARAM_USERNAME = "username";
    public static final String PARAM_PASSWORD = "password";
    public static final String PARAM_NAME = "name";


    // The path used for OAuth 2.0 authorization token
    public static final String TOKEN_PATH = "/oauth/token";

    // Root paths for the server
    public static final String SERVICE_ROOT_PATH = "/api";

    public static final String SERVICE_LOGIN_PATH = SERVICE_ROOT_PATH + "/login";
    public static final String SERVICE_LOGOUT_PATH = SERVICE_ROOT_PATH + "/logout";
    public static final String SERVICE_REGISTER_PATH = SERVICE_ROOT_PATH + "/register";

    public static final String SERVICE_GATHERING_PATH = SERVICE_ROOT_PATH + "/gathering";

    public static final String GATHERING_GET_TOPICS = SERVICE_GATHERING_PATH + "/topics";

    // /impatient/checklogin
//    @GET(SERVICE_LOGIN_PATH)
//    public Result_Login checkLogin();

    //@GET("api/types")
    //Call<GatheringType> getUser(@Path("username") String username);

    @POST(SERVICE_LOGIN_PATH)
    Call<Result_Login> login(@Body UserCredentials creds);

    @GET(SERVICE_LOGOUT_PATH)
    Call<Result_Logout> logout();

    @POST(SERVICE_REGISTER_PATH)
    Call<Result_Register> register(@Body UserCredentials creds);

    @GET(GATHERING_GET_TOPICS)
    Call<List<GatheringTopic>> getGatheringTopics();

    @GET("api/gathering/types")
    Call<List<GatheringType>> getGatheringTypes();

    @GET("api/testing/android/gatherings")
    Call<List<Gathering>> getGatherings();


    //@GET("movie/top_rated")
    //Call<MoviesResponse> getTopRatedMovies(@Query("api_key") String apiKey);

    //@GET("movie/{id}")
    //Call<MoviesResponse> getMovieDetails(@Path("id") int id, @Query("api_key") String apiKey);
}
