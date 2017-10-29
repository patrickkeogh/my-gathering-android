package com.programming.kantech.mygathering.view.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.programming.kantech.mygathering.R;
import com.programming.kantech.mygathering.data.model.pojo.Query_Search;
import com.programming.kantech.mygathering.utils.Constants;
import com.programming.kantech.mygathering.utils.Utils_DateFormatting;
import com.programming.kantech.mygathering.view.activities.Activity_Location_Select;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

/**
 * Created by patri on 2017-10-26.
 */

public class Fragment_SearchFiltersDialog extends DialogFragment {

    //private EditText mEditText;
    private long mFilterDate;

    private double mLat = 0;
    private double mLong = 0;

    private Query_Search mQuery;

    private String[] mFilterTypes;
    private String[] mFilterTopics;
    private String[] mFilterDistanceOptions;
    private String[] mFilterDateOptions;

    private int mSelectedType = 0;
    private int mSelectedTopic = 0;
    private int mSelectedDateOption = 0;
    private int mSelectedDistanceOption = 0;

    private int mDefaultType = 0;
    private int mDefaultTopic = 0;
    private int mDefaultDateOption = 0;
    private int mDefaultDistanceOption = 0;

    @BindView(R.id.iv_select_date_option)
    ImageView iv_select_date_option;

    @BindView(R.id.iv_select_date)
    ImageView iv_select_date;

    @BindView(R.id.iv_select_distance)
    ImageView iv_select_distance;

    @BindView(R.id.iv_select_location)
    ImageView iv_select_location;

    @BindView(R.id.iv_select_type)
    ImageView iv_select_type;

    @BindView(R.id.iv_select_topic)
    ImageView iv_select_topic;


    @BindView(R.id.tv_select_date_option)
    TextView tv_select_date_option;

    @BindView(R.id.tv_select_date)
    TextView tv_select_date;

    @BindView(R.id.tv_select_distance)
    TextView tv_select_distance;

    @BindView(R.id.tv_select_location)
    TextView tv_select_location;

    @BindView(R.id.tv_select_type)
    TextView tv_select_type;

    @BindView(R.id.tv_select_topic)
    TextView tv_select_topic;

    @BindView(R.id.tv_matches_found)
    TextView tv_matches_found;

    // Define a new interface MainListener that triggers a callback in the host activity
    DialogListener mCallback;

    public void setCount(int count) {
        //Log.i(Constants.TAG, "SetCountCalled:" + count);

        tv_matches_found.setText(count + " matches found");


    }

    // DialogListener interface, calls a method in the host activity
    // after a callback has been triggered
    public interface DialogListener {
        void onFinish(Query_Search query);

        void getQueryCount(Query_Search query);
    }

    public Fragment_SearchFiltersDialog() {

    }

    public static Fragment_SearchFiltersDialog newInstance() {
        return new Fragment_SearchFiltersDialog();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.dialog_search_filters, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);

        mFilterTypes = getResources().getStringArray(R.array.gathering_types);
        mFilterTopics = getResources().getStringArray(R.array.gathering_topics);
        mFilterDateOptions = getResources().getStringArray(R.array.date_filters);
        mFilterDistanceOptions = getResources().getStringArray(R.array.distance_filters);

        attachClickListeners();

        setDefaultValues();

        //mFilterDate = Calendar.getInstance().getTimeInMillis();

        getDialog().setCancelable(false);
        getDialog().setCanceledOnTouchOutside(false);

    }

    private void setDefaultValues() {

        mSelectedType = mDefaultType;
        mSelectedTopic = mDefaultTopic;
        mSelectedDateOption = mDefaultDateOption;
        mSelectedDistanceOption = mDefaultDistanceOption;

        setFormFields();

    }

    private void setFormFields() {

        String selected_type = mFilterTypes[mSelectedType];
        String selected_topic = mFilterTopics[mSelectedTopic];
        String selected_date_option = mFilterDateOptions[mSelectedDateOption];
        String selected_distance = mFilterDistanceOptions[mSelectedDistanceOption];

        tv_select_type.setText(selected_type);
        tv_select_topic.setText(selected_topic);
        tv_select_date_option.setText(selected_date_option);
        tv_select_distance.setText(selected_distance);

        if (Objects.equals(selected_distance, "No distance limit")) {
            iv_select_location.setEnabled(false);
            iv_select_location.setImageResource(R.drawable.ic_location_on_disabled_24dp);
            tv_select_location.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.border_disabled));
            tv_select_location.setText("");

            mLat = 0;
            mLat = 0;
        } else {
            iv_select_location.setEnabled(true);
            iv_select_location.setImageResource(R.drawable.ic_location_on_black_24dp);
            tv_select_location.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.border));

        }


        if (Objects.equals(selected_date_option, "No Date Filter")) {
            iv_select_date.setEnabled(false);
            iv_select_date.setImageResource(R.drawable.ic_date_range_disabled_24dp);
            tv_select_date.setEnabled(false);
            tv_select_date.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.border_disabled));
            tv_select_date.setText("");
        } else if (Objects.equals(selected_date_option, "Today") || Objects.equals(selected_date_option, "Tomorrow")) {
            iv_select_date.setImageResource(R.drawable.ic_date_range_disabled_24dp);

            iv_select_date.setEnabled(false);
            tv_select_date.setEnabled(false);
            tv_select_date.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.border));

            if (mFilterDate != 0)
                tv_select_date.setText(Utils_DateFormatting.getFormattedLongDateStringFromLongDate(mFilterDate));
        } else {
            iv_select_date.setImageResource(R.drawable.ic_date_range_black_24dp);
            iv_select_date.setEnabled(true);
            tv_select_date.setEnabled(true);
            tv_select_date.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.border));

            if (mFilterDate != 0)
                tv_select_date.setText(Utils_DateFormatting.getFormattedLongDateStringFromLongDate(mFilterDate));
        }


        getCount();
    }


    // Override onAttach to make sure that the container activity has implemented the callback
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the host activity has implemented the callback interface
        // If not, it throws an exception
        try {
            mCallback = (Fragment_SearchFiltersDialog.DialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement DialogListener");
        }
    }

    /***
     * Called when the Location Select Activity returns back with a selected place (or after canceling)
     *
     * @param requestCode The request code passed when calling startActivityForResult
     * @param resultCode  The result code specified by the second activity
     * @param data        The Intent that carries the result data.
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(Constants.TAG, "onActivityResult");

        // Check if this is the result for the Location Select Activity request
        if (requestCode == Constants.REQUEST_CODE_GET_LOCATION && resultCode == RESULT_OK) {

            String mLocationAddress = data.getStringExtra(Constants.EXTRA_LOCATION_ADDRESS);
            //String mLocationName = data.getStringExtra(Constants.EXTRA_LOCATION_NAME);

            mLat = data.getDoubleExtra(Constants.EXTRA_LOCATION_LAT, 0);
            mLong = data.getDoubleExtra(Constants.EXTRA_LOCATION_LONG, 0);

            //tv_location_name.setText(mLocationName);
            tv_select_location.setText(mLocationAddress);

            setFormFields();
        }
    }


    @Override
    public void onResume() {
        super.onResume();

        // Set the width of the dialog to 90% of the screen
        int width = (int) (getResources().getDisplayMetrics().widthPixels * .90);
        //int height = (int) (getResources().getDisplayMetrics().heightPixels);

        getDialog().getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @OnClick(R.id.btn_cancel)
    public void saveNewGathering() {
        dismiss();

    }

    @OnClick(R.id.btn_reset)
    public void resetFiltersToDefault() {


    }

    @OnClick(R.id.btn_ok)
    public void finishSearch() {

        //Log.i(Constants.TAG, "finishSearch() called");


        mQuery = createQuery();

        mCallback.onFinish(mQuery);
        dismiss();



    }

    private Query_Search createQuery() {

        Query_Search query = new Query_Search();

        if (mFilterDate != 0) {

            //Log.i(Constants.TAG, "mFilterDate:" + mFilterDate);

            Calendar startDateTime = Calendar.getInstance();
            Calendar endDateTime = Calendar.getInstance();

            // set the start and ednd time to 12:00 AM to 11:59 PM
            startDateTime.setTimeInMillis(mFilterDate);
            startDateTime.set(Calendar.HOUR, 0);
            startDateTime.set(Calendar.MINUTE, 0);
            startDateTime.set(Calendar.SECOND, 0);
            startDateTime.set(Calendar.MILLISECOND, 0);

            endDateTime.setTimeInMillis(mFilterDate);
            endDateTime.set(Calendar.HOUR, 23);
            endDateTime.set(Calendar.MINUTE, 59);
            endDateTime.set(Calendar.SECOND, 59);
            endDateTime.set(Calendar.MILLISECOND, 0);

            switch (mSelectedDateOption) {
                case 0: // Nothing here - no date filter
                    break;
                case 1: // Today
                    break;
                case 2: // Tomorrow
                    break;
                case 3: // One Week
                    endDateTime.add(Calendar.WEEK_OF_YEAR, 1);
                    break;
                case 4: // One Month
                    endDateTime.add(Calendar.MONTH, 1);
                    break;
                case 5: // One Year
                    endDateTime.add(Calendar.YEAR, 1);
                    break;
            }

            String start_date_mongo = new SimpleDateFormat(Constants.DATE_TIME_UTC_MONGODB, Locale.getDefault())
                    .format(new Date(startDateTime.getTimeInMillis()));

            query.setStart_date(start_date_mongo);

            //Log.i(Constants.TAG, "Start Date and Time:" + start_date_mongo);
            //Log.i(Constants.TAG, "Start Date and Time Formatted:" + Utils_DateFormatting.getFormattedLongDateAndTimeStringFromLongDate(startDateTime.getTimeInMillis()));

            String end_date_mongo = new SimpleDateFormat(Constants.DATE_TIME_UTC_MONGODB, Locale.getDefault())
                    .format(new Date(endDateTime.getTimeInMillis()));

            query.setEnd_date(end_date_mongo);

            //Log.i(Constants.TAG, "End Date and Time:" + end_date_mongo);
           // Log.i(Constants.TAG, "End Date and Time Formatted:" + Utils_DateFormatting.getFormattedLongDateAndTimeStringFromLongDate(endDateTime.getTimeInMillis()));



        }else{
            query.setStart_date(null);
            query.setEnd_date(null);

        }

        // Add topic and/or type filters if selected

        int[] mDistanceValues = getResources().getIntArray(R.array.distance_values);

        float distance = mDistanceValues[mSelectedDistanceOption];

        // Check if a location was selected
        if (mLong != 0 && mLat != 0) {
            List<Double> coords = Arrays.asList(mLong, mLat); //Reverse for mongo (lng, lat)

            query.setDistance(distance);
            query.setCoordinates(coords);
        }else{
            query.setDistance(distance);
            query.setCoordinates(null);
        }


        if (mSelectedTopic != 0) {
            query.setTopic(mFilterTopics[mSelectedTopic]);
        } else {
            query.setTopic(null);
        }
        if (mSelectedType != 0) {
            query.setType(mFilterTypes[mSelectedType]);
        } else {
            query.setType(null);
        }


        return query;
    }

    /***
     * Button Click event handler to handle clicking the "Location" TextView
     */
    public void onSelectLocationClicked() {
        Intent intent = new Intent(getActivity(), Activity_Location_Select.class);
        startActivityForResult(intent, Constants.REQUEST_CODE_GET_LOCATION);
    }

    private void attachClickListeners() {

        iv_select_topic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                builder.setTitle("Select Topic Filter")
                        .setSingleChoiceItems(mFilterTopics, mSelectedTopic, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int position) {
                                // The 'which' argument contains the index position
                                // of the selected item
                                dialog.dismiss();

                                mSelectedTopic = position;
                                setFormFields();
                            }
                        });

                AlertDialog dialog_date_filter = builder.create();
                dialog_date_filter.show();
            }
        });

        iv_select_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                builder.setTitle("Select Type Filter")
                        .setSingleChoiceItems(mFilterTypes, mSelectedType, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int position) {
                                // The 'which' argument contains the index position
                                // of the selected item
                                dialog.dismiss();

                                mSelectedType = position;
                                setFormFields();

                            }
                        });

                AlertDialog dialog_date_filter = builder.create();
                dialog_date_filter.show();

            }
        });

        iv_select_date_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                builder.setTitle("Select Date Filter")
                        .setSingleChoiceItems(mFilterDateOptions, mSelectedDateOption, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int position) {
                                // The 'which' argument contains the index position
                                // of the selected item
                                dialog.dismiss();

                                mSelectedDateOption = position;

                                Calendar c = Calendar.getInstance();

                                switch (mSelectedDateOption) {
                                    case 0:
                                        mFilterDate = 0;
                                        break;
                                    case 1:
                                        c.add(Calendar.HOUR, 24);
                                        mFilterDate = c.getTimeInMillis();
                                        break;
                                    case 2:
                                        mFilterDate = c.getTimeInMillis();
                                        break;

                                }

                                setFormFields();
                            }
                        });

                AlertDialog dialog_date_filter = builder.create();
                dialog_date_filter.show();
            }
        });

        iv_select_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar cal = Calendar.getInstance(TimeZone.getDefault()); // Get current date

                // Create the DatePickerDialog instance
                DatePickerDialog datePicker = new DatePickerDialog(getActivity(),
                        R.style.ThemeOverlay_AppCompat_Dialog_Alert, datePickerListener,
                        cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH));
                datePicker.setCancelable(false);
                datePicker.setTitle("Select Date");
                datePicker.show();

            }
        });

        iv_select_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSelectLocationClicked();
            }
        });

        iv_select_distance.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                builder.setTitle("Select Distance Option")
                        .setSingleChoiceItems(mFilterDistanceOptions, mSelectedDistanceOption, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int position) {
                                // The 'which' argument contains the index position
                                // of the selected item
                                dialog.dismiss();

                                mSelectedDistanceOption = position;

                                setFormFields();
                            }
                        });

                AlertDialog dialog_distance_filter = builder.create();
                dialog_distance_filter.show();

            }
        });
    }

    // Date picker listener for start date
    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {

            final Calendar c = Calendar.getInstance();
            c.set(Calendar.YEAR, selectedYear);
            c.set(Calendar.MONTH, selectedMonth);
            c.set(Calendar.DAY_OF_MONTH, selectedDay);

            c.set(Calendar.HOUR, 0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);

            mFilterDate = c.getTimeInMillis();

            setFormFields();

        }
    };

    public void getCount() {

        Query_Search query = createQuery();
        mCallback.getQueryCount(query);
    }
}
