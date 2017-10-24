package com.programming.kantech.mygathering.view.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.programming.kantech.mygathering.R;
import com.programming.kantech.mygathering.application.Application_MyGathering;
import com.programming.kantech.mygathering.data.model.mongo.Banner;
import com.programming.kantech.mygathering.data.model.mongo.Gathering;
import com.programming.kantech.mygathering.data.model.mongo.GatheringTopic;
import com.programming.kantech.mygathering.data.model.mongo.GatheringType;
import com.programming.kantech.mygathering.data.model.mongo.Location;
import com.programming.kantech.mygathering.data.model.mongo.Location_Coords;
import com.programming.kantech.mygathering.data.model.mongo.Owner;
import com.programming.kantech.mygathering.data.retrofit.ApiClient;
import com.programming.kantech.mygathering.data.retrofit.ApiInterface;
import com.programming.kantech.mygathering.utils.Constants;
import com.programming.kantech.mygathering.view.fragments.Fragment_Add_Banner;
import com.programming.kantech.mygathering.view.fragments.Fragment_Add_Dates;
import com.programming.kantech.mygathering.view.fragments.Fragment_Add_Details;
import com.programming.kantech.mygathering.view.fragments.Fragment_Add_Location;
import com.programming.kantech.mygathering.view.fragments.Fragment_Add_Save;
import com.programming.kantech.mygathering.view.fragments.Fragment_Nav_Header;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by patri on 2017-10-18.
 *
 */

public class Activity_AddGathering extends AppCompatActivity implements
        Fragment_Add_Details.DetailsListener,
        Fragment_Add_Location.LocationListener,
        Fragment_Add_Dates.DatesListener,
        Fragment_Add_Banner.BannerListener,
        Fragment_Add_Save.SaveListener {

    // Member variables

    private ApiInterface apiService;


    // Build the gathering with info from multiple fragments for better screen layout
    private Gathering mGathering = new Gathering();

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_gathering);

        //ButterKnife.inject(this);
        ButterKnife.bind(this);

        apiService =
                ApiClient.getClient().create(ApiInterface.class);


        // Set the support action bar
        setSupportActionBar(mToolbar);

        // Set the action bar back button to look like an up button
        ActionBar mActionBar = this.getSupportActionBar();

        if (mActionBar != null) {
            mActionBar.setDisplayHomeAsUpEnabled(false);
            mActionBar.setTitle("Create gathering");
        }

        // first load the header fragment and the details fragment
        Fragment_Nav_Header fragment_header = Fragment_Nav_Header.newInstance(Constants.TAG_FRAGMENT_ADD_DETAILS);
        replaceFragment(R.id.container_header, Constants.TAG_FRAGMENT_HEADER, fragment_header);

        Fragment_Add_Details fragment_details = Fragment_Add_Details.newInstance();
        replaceFragment(R.id.container_details, Constants.TAG_FRAGMENT_ADD_DETAILS, fragment_details);

    }


    private void replaceFragment(int container, String fragment_tag, Fragment fragment_in) {

        // Get a fragment transaction to replace fragments
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (Objects.equals(fragment_tag, Constants.TAG_FRAGMENT_HEADER)) {
            transaction.add(container, fragment_in, fragment_tag);

        } else {
            transaction.replace(container, fragment_in, fragment_tag);
        }

        // Commit the transaction
        transaction.commit();


    }

    @Override
    public void addDetails(String nextFrag, Gathering gathering) {
        mGathering.setDescription(gathering.getDescription());
        mGathering.setName(gathering.getName());
        mGathering.setTopic(gathering.getTopic());
        mGathering.setType(gathering.getType());

        dumpGatheringVariable();

        Fragment_Nav_Header fragment = (Fragment_Nav_Header) getSupportFragmentManager()
                .findFragmentByTag(Constants.TAG_FRAGMENT_HEADER);

        fragment.setForm(nextFrag);

        Fragment_Add_Location fragment_location = Fragment_Add_Location.newInstance();

        replaceFragment(R.id.container_details, nextFrag, fragment_location);


    }

    private void dumpGatheringVariable() {

        Log.i(Constants.TAG, "gatheringDump:" + mGathering.toString());
    }

    @Override
    public void addLocation(String nextFrag, Location address) {
        mGathering.setLocation(address);

        //dumpGatheringVariable();

        Fragment_Nav_Header fragment = (Fragment_Nav_Header) getSupportFragmentManager()
                .findFragmentByTag(Constants.TAG_FRAGMENT_HEADER);

        fragment.setForm(nextFrag);

        Fragment_Add_Dates fragment_dates = Fragment_Add_Dates.newInstance();

        replaceFragment(R.id.container_details, nextFrag, fragment_dates);
    }

    @Override
    public void addDateInfo(String nextFrag, String start_date, String end_date) {

        Log.i(Constants.TAG, "Start Date:" + start_date);

        mGathering.setGatheringStartDateTime(start_date);
        mGathering.setGatheringEndDateTime(end_date);

        Fragment_Nav_Header fragment = (Fragment_Nav_Header) getSupportFragmentManager()
                .findFragmentByTag(Constants.TAG_FRAGMENT_HEADER);

        fragment.setForm(nextFrag);

        Fragment_Add_Banner fragment_banner = Fragment_Add_Banner.newInstance();

        replaceFragment(R.id.container_details, nextFrag, fragment_banner);

    }

    @Override
    public void addBanner(String nextFrag, Banner banner) {

        mGathering.setBanner(banner);

        Fragment_Nav_Header fragment = (Fragment_Nav_Header) getSupportFragmentManager()
                .findFragmentByTag(Constants.TAG_FRAGMENT_HEADER);

        fragment.setForm(nextFrag);

        Fragment_Add_Save fragment_save = Fragment_Add_Save.newInstance();

        replaceFragment(R.id.container_details, nextFrag, fragment_save);

    }

    @Override
    public void createNewGathering(String access, String status) {

        Application_MyGathering app = (Application_MyGathering) getApplication();

        Owner owner = app.getCurrentUser();
        //Log.i(Constants.TAG, "CurrentUser From App:" + owner.toString());

        mGathering.setOwner(owner);
        mGathering.setStatus(status);
        mGathering.setAccess(access);


        Call<Gathering> call = apiService.createGathering(app.getToken(), mGathering);

        call.enqueue(new Callback<Gathering>() {
            @Override
            public void onResponse(@NonNull Call<Gathering> call, @NonNull Response<Gathering> response) {

                Log.i(Constants.TAG, "Response:" + response);

                if (response.isSuccessful()) {
                    // Code 200, 201
                    //Result_Login result = response.body();

                    //Gathering gathering = response.body();

                    // TODO: Notify user the gathering was crteated and send them to the details screen

//                    Log.i(Constants.TAG, "We got a gathering back:" + gathering.toString());


                    // Notify the activity login was successfull
                    //login_ok(result);
                } else {
                    // code 400, 401, etc
//                    switch (response.code()){
//                        case 401:
//                            onLoginFailed("Password or username are incorrect");
//                        default:
//                            onLoginFailed(response.message());
//                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Gathering> call, @NonNull Throwable t) {
                // Log error here since request failed
                Log.e(Constants.TAG, t.toString());
                //onLoginFailed(t.toString());
            }
        });
    }

    private void createDummyGathering() {

        mGathering = new Gathering();

        mGathering.setName("Gathering Test");
        mGathering.setDescription("This is a test description");

        // Add type and topic
        String topic = Constants.GATHERING_DEFAULT_TOPIC;
        GatheringTopic gathering_topic = new GatheringTopic(topic);
        List<GatheringTopic> gathering_topics = new ArrayList<>();
        gathering_topics.add(gathering_topic);
        mGathering.setTopic(gathering_topics);

        String type = Constants.GATHERING_DEFAULT_TYPE;
        GatheringType gathering_type = new GatheringType(type);
        List<GatheringType> gathering_types = new ArrayList<>();
        gathering_types.add(gathering_type);
        mGathering.setType(gathering_types);

        Date start = new Date();

        String start_date = new SimpleDateFormat(Constants.DATE_TIME_FORMAT, Locale.getDefault()).format(start);

        mGathering.setGatheringStartDateTime(start_date);

        Date end = new Date();

        String end_date = new SimpleDateFormat(Constants.DATE_TIME_FORMAT, Locale.getDefault()).format(end);

        mGathering.setGatheringEndDateTime(end_date);

        Location address = new Location();

        address.setFormattedAddress("Formatted address");

        address.setName("Some Place");

        Double lat = 44.3591179;
        Double lng = -79.7357619;

        List<Double> coords = new ArrayList<>();

        coords.add(lat);
        coords.add(lng);

        Location_Coords location_coords = new Location_Coords();
        location_coords.setCoordinates(coords);

        address.setLocation(location_coords);

        address.setLocality("Barrie");
        address.setName("Name of the location");
        address.setCountry("Canada");
        address.setCountryShort("CA");
        address.setStateProv("ON");
        address.setPostalCode("L4N 3E2");
        address.setNotes("These are notes, directions, or other stuff");

        mGathering.setLocation(address);
        mGathering.setStatus("Not Published");
        mGathering.setAccess("Public");

        Application_MyGathering app = (Application_MyGathering) getApplication();

        Owner owner = app.getCurrentUser();
        //Log.i(Constants.TAG, "CurrentUser From App:" + owner.toString());

        mGathering.setOwner(owner);

        dumpGatheringVariable();

        Call<Gathering> call = apiService.createGathering(app.getToken(), mGathering);

        call.enqueue(new Callback<Gathering>() {
            @Override
            public void onResponse(@NonNull Call<Gathering> call, @NonNull Response<Gathering> response) {

                Log.i(Constants.TAG, "Response:" + response);

                if (response.isSuccessful()) {
                    // Code 200, 201
                    //Result_Login result = response.body();

                    //Gathering gathering = response.body();

                    //Log.i(Constants.TAG, "We got a gathering back:" + gathering.toString());


                    // Notify the activity login was successfull
                    //login_ok(result);
                } else {
                    // code 400, 401, etc
//                    switch (response.code()){
//                        case 401:
//                            onLoginFailed("Password or username are incorrect");
//                        default:
//                            onLoginFailed(response.message());
//                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Gathering> call, @NonNull Throwable t) {
                // Log error here since request failed
                Log.e(Constants.TAG, t.toString());
                //onLoginFailed(t.toString());
            }
        });


    }


}
