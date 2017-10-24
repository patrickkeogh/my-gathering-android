package com.programming.kantech.mygathering.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.programming.kantech.mygathering.R;
import com.programming.kantech.mygathering.data.model.mongo.Gathering;
import com.programming.kantech.mygathering.data.model.mongo.GatheringTopic;
import com.programming.kantech.mygathering.data.model.mongo.GatheringType;
import com.programming.kantech.mygathering.utils.Constants;
import com.programming.kantech.mygathering.utils.Utils_General;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by patri on 2017-10-19.
 *
 */

public class Fragment_Add_Details extends Fragment {

    @BindView(R.id.btn_next_to_location)
    Button btn_proceed;

    @BindView(R.id.sp_types)
    Spinner sp_types;

    @BindView(R.id.sp_topics)
    Spinner sp_topics;

    @BindView(R.id.et_add_gathering_title)
    EditText et_add_gathering_title;

    @BindView(R.id.et_add_gathering_description)
    EditText et_add_gathering_description;

    // Define a new interface DetailsListener that triggers a callback in the host activity
    DetailsListener mCallback;

    // DetailsListener interface, calls a method in the host activity
    // after a callback has been triggered
    public interface DetailsListener {
        void addDetails(String nextFrag, Gathering gathering);
    }

    /**
     * Static factory method that
     * initializes the fragment's arguments, and returns the
     * new fragment to the client.
     */
    public static Fragment_Add_Details newInstance() {
        return new Fragment_Add_Details();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Get the fragment layout for the details fragment
        final View rootView = inflater.inflate(R.layout.fragment_details, container, false);

        ButterKnife.bind(this, rootView);

        return rootView;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setupSpinners();

        setupForm();

        et_add_gathering_title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                setupForm();
            }
        });

        et_add_gathering_description.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                setupForm();
            }
        });
    }


    // Override onAttach to make sure that the container activity has implemented the callback
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the host activity has implemented the callback interface
        // If not, it throws an exception
        try {
            mCallback = (Fragment_Add_Details.DetailsListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement DetailsListener");
        }
    }

    @OnClick(R.id.btn_next_to_location)
    public void proceedToNext() {


        Gathering mGathering = new Gathering();
        mGathering.setName(et_add_gathering_title.getText().toString().trim());
        mGathering.setDescription(et_add_gathering_description.getText().toString().trim());

        // Convert the selected topic and type to documents to store in MongoDB
        // by creating lists and adding the topic and type to them
        String topic = sp_topics.getSelectedItem().toString();
        GatheringTopic gathering_topic = new GatheringTopic(topic);
        List<GatheringTopic> gathering_topics = new ArrayList<>();
        gathering_topics.add(gathering_topic);
        mGathering.setTopic(gathering_topics);

        String type = sp_types.getSelectedItem().toString();
        GatheringType gathering_type = new GatheringType(type);
        List<GatheringType> gathering_types = new ArrayList<>();
        gathering_types.add(gathering_type);
        mGathering.setType(gathering_types);

        mCallback.addDetails(Constants.TAG_FRAGMENT_ADD_LOCATION, mGathering);

    }

    private void setupSpinners() {

        String[] topics = Constants.GATHERING_TOPICS;
        String[] types = Constants.GATHERING_TYPES;

        ArrayAdapter<String> topics_dataAdapter = new ArrayAdapter<>(getContext(), R.layout.item_spinner, topics);

        ArrayAdapter<String> types_dataAdapter = new ArrayAdapter<>(getContext(), R.layout.item_spinner, types);


        // Set the drop down list style
        topics_dataAdapter.setDropDownViewResource(R.layout.item_spinner_list);
        types_dataAdapter.setDropDownViewResource(R.layout.item_spinner_list);

        sp_topics.setSelection(0); //set the hint the default selection so it appears on launch.
        sp_types.setSelection(0); //set the hint the default selection so it appears on launch.

        // attaching data adapter to spinner
        sp_topics.setAdapter(topics_dataAdapter);
        sp_types.setAdapter(types_dataAdapter);
    }

    private void setupForm() {
        boolean isFormValid = true;

        String title = et_add_gathering_title.getText().toString().trim();

        if (title.length() == 0) {
            et_add_gathering_title.setError("A title is required.");
            isFormValid = false;
        }

        String description = et_add_gathering_description.getText().toString().trim();

        if (description.length() == 0) {
            et_add_gathering_description.setError("A description is required.");
            isFormValid = false;
        }

        String topic = sp_topics.getSelectedItem().toString();

        if (topic.length() == 0 || Objects.equals(topic, Constants.GATHERING_TOPIC_HINT)) {
            //sp_topics..setError("A description is required.");
            Utils_General.showToast(getContext(), "You must select a gathering topic!");

            // show toast saying a topic has not been selected
            isFormValid = false;
        }

        String type = sp_types.getSelectedItem().toString();

        if (type.length() == 0 || Objects.equals(type, Constants.GATHERING_TYPE_HINT)) {
            //sp_topics..setError("A description is required.");
            Utils_General.showToast(getContext(), "You must select a gathering type!");

            // show toast saying a topic has not been selected
            isFormValid = false;
        }

        if (!isFormValid) {
            btn_proceed.setEnabled(false);
            btn_proceed.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryLight));
        } else {

            btn_proceed.setEnabled(true);
            btn_proceed.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));

        }

    }


}
