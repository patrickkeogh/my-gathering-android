package com.programming.kantech.mygathering.view.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.programming.kantech.mygathering.R;
import com.programming.kantech.mygathering.data.model.google.AddressComponent;
import com.programming.kantech.mygathering.data.model.google.AddressResults;
import com.programming.kantech.mygathering.data.model.google.Geometry;
import com.programming.kantech.mygathering.data.model.google.GeometryLocation;
import com.programming.kantech.mygathering.data.model.google.Result;
import com.programming.kantech.mygathering.data.model.mongo.Location;
import com.programming.kantech.mygathering.data.model.mongo.Location_Coords;
import com.programming.kantech.mygathering.data.retrofit.ApiClient_Google;
import com.programming.kantech.mygathering.data.retrofit.ApiInterface_Google;
import com.programming.kantech.mygathering.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by patri on 2017-10-19.
 *
 */

public class Fragment_Add_Location extends Fragment {

    private Location mGatheringAddress;
    private Place mPlace;

    private ApiInterface_Google apiServiceForGoogle;

//    @BindView(R.id.iv_select_location)
//    ImageView iv_select_location;

    @BindView(R.id.btn_next_to_dates)
    Button btn_proceed;

    @BindView(R.id.tv_location_name)
    TextView tv_location_name;

    @BindView(R.id.tv_location_address)
    TextView tv_location_address;

    @BindView(R.id.et_add_gathering_location_notes)
    EditText et_add_gathering_location_notes;

    // Define a new interface LocationListener that triggers a callback in the host activity
    LocationListener mCallback;

    // LocationListener interface, calls a method in the host activity
    // after a callback has been triggered
    public interface LocationListener {
        void addLocation(String nextFrag, Location address);
    }

    /**
     * Static factory method that
     * initializes the fragment's arguments, and returns the
     * new fragment to the client.
     */
    public static Fragment_Add_Location newInstance() {
        return new Fragment_Add_Location();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Get the fragment layout for the driving list
        final View rootView = inflater.inflate(R.layout.fragment_location, container, false);

        ButterKnife.bind(this, rootView);

        apiServiceForGoogle = ApiClient_Google.getClient().create(ApiInterface_Google.class);


        return rootView;

    }

    // Override onAttach to make sure that the container activity has implemented the callback
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the host activity has implemented the callback interface
        // If not, it throws an exception
        try {
            mCallback = (Fragment_Add_Location.LocationListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement LocationListener");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(Constants.TAG, "onActivityResult in Fragment");
        if (requestCode == Constants.REQUEST_CODE_PLACE_PICKER && resultCode == Activity.RESULT_OK) {
            Place place = PlacePicker.getPlace(getContext(), data);
            if (place == null) {
                Log.i(Constants.TAG, "No place selected");
                return;
            }

            mPlace = place;

            tv_location_name.setText(mPlace.getName().toString());
            tv_location_address.setText(mPlace.getAddress().toString());

            Log.i(Constants.TAG, "Place:" + mPlace.toString());

            getAddressDetails();

            //tv_address.setText(place.getAddress().toString());
        }
    }

    @OnClick(R.id.iv_select_location)
    public void selectLocation() {

        Log.i(Constants.TAG, "onGetLocationButtonClicked() called");
        try {

            // Start a new Activity for the Place Picker API, this will trigger {@code #onActivityResult}
            // when a place is selected or with the user cancels.
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
            Intent i = builder.build(getActivity());
            startActivityForResult(i, Constants.REQUEST_CODE_PLACE_PICKER);

        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            Log.e(Constants.TAG, String.format("GooglePlayServices Not Available [%s]", e.getMessage()));
        } catch (Exception e) {
            Log.e(Constants.TAG, String.format("PlacePicker Exception: %s", e.getMessage()));
        }

    }

    @OnClick(R.id.btn_next_to_dates)
    public void proceedToNext() {

        if (mGatheringAddress != null) {

            mGatheringAddress.setNotes(et_add_gathering_location_notes.getText().toString());
            mCallback.addLocation(Constants.TAG_FRAGMENT_ADD_DATES, mGatheringAddress);


        }
//
//        if (validateFormFields()) {
//            mGatheringAddress = new Location();
//
////            mGathering.setName(et_add_gathering_title.getText().toString().trim());
////            mGathering.setDescription(et_add_gathering_description.getText().toString().trim());
////            mCallback.addDetails(Constants.TAG_FRAGMENT_ADD_LOCATION, mGathering);
//        }

    }

//    private boolean validateFormFields() {
//        boolean isFormValid = true;
//
//
//        return isFormValid;
//
//
//    }

    private void getAddressDetails() {
        Call<AddressResults> call = apiServiceForGoogle.getAddressDetails(mPlace.getId());

        call.enqueue(new Callback<AddressResults>() {
            @Override
            public void onResponse(@NonNull Call<AddressResults> call, @NonNull Response<AddressResults> response) {

                Log.i(Constants.TAG, "Response:" + response);

                if (response.isSuccessful()) {
                    // Code 200, 201
                    AddressResults addressResults = response.body();

                    Result result;
                    if (addressResults != null) {
                        result = addressResults.getResult();
                        parseAddressDetails(result);
                    }

                    // TODO: Add value checking

                    //Log.i(Constants.TAG, "Response Formatted Address:" + result.getFormattedAddress());



                    // Notify the activity movie fetch was successfull
                    //distance_ok(fetchResults);
                } else {
                    Log.i(Constants.TAG, "Response was unsuccessful");
                    //distance_failed(response.message());

                }
            }

            @Override
            public void onFailure(@NonNull Call<AddressResults> call, @NonNull Throwable t) {
                // Log error here since request failed
                Log.e(Constants.TAG, t.toString());
                //distance_failed(response.message());
            }
        });
    }

    private void parseAddressDetails(Result result) {

        mGatheringAddress = new Location();

        if (result != null) {
            // Get the formatted address
            String formattedAddress = result.getFormattedAddress();
            mGatheringAddress.setFormattedAddress(formattedAddress);

            // Get the formatted address
            String name = result.getName();
            mGatheringAddress.setName(name);

            //Log.i(Constants.TAG, "Formatted Aaddress:" + formattedAddress);

            Geometry geometry = result.getGeometry();
            GeometryLocation location = geometry.getLocation();

            // Get the lat and long
            Double lat = location.getLat();
            Log.i(Constants.TAG, "Lat:" + lat);
            Double lng = location.getLng();
            Log.i(Constants.TAG, "Long:" + lng);

            List<Double> coords = new ArrayList<>();

            coords.add(lng);
            coords.add(lat);

            Location_Coords location_coords = new Location_Coords();
            location_coords.setCoordinates(coords);

            mGatheringAddress.setLocation(location_coords);

            List<AddressComponent> components = result.getAddressComponents();

            for (AddressComponent component : components) {

                Log.i(Constants.TAG, "Component:" + component.toString());

                List<String> types = component.getTypes();

                String component_type = types.get(0);

                Log.i(Constants.TAG, "Type:" + component_type);

                // Seperate the address parts

                switch (component_type) {
                    case Constants.COMPONENT_TYPE_CITY:
                    case Constants.COMPONENT_TYPE_LOCALITY:
                        Log.i(Constants.TAG, "Locality:" + component.getLongName());
                        mGatheringAddress.setLocality(component.getLongName());
                        break;
                    case Constants.COMPONENT_TYPE_COUNTRY:
                        Log.i(Constants.TAG, "Country:" + component.getLongName());
                        mGatheringAddress.setCountry(component.getLongName());
                        mGatheringAddress.setCountryShort(component.getShortName());
                        break;
                    case Constants.COMPONENT_TYPE_PROV:
                        Log.i(Constants.TAG, "Prov/State:" + component.getLongName());
                        mGatheringAddress.setStateProv(component.getLongName());
                        break;
                    case Constants.COMPONENT_TYPE_POSTAL:
                        Log.i(Constants.TAG, "Postal:" + component.getLongName());
                        mGatheringAddress.setPostalCode(component.getLongName());
                        break;

                    default:
                        //default code block
                }
            }



            setForm();

        }
    }

    private void setForm() {

        if(mGatheringAddress == null){
            btn_proceed.setEnabled(false);
            btn_proceed.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryLight));
        }else{
            btn_proceed.setEnabled(true);
            btn_proceed.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));

        }

    }


}
