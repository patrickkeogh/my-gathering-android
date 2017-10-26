package com.programming.kantech.mygathering.view.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.programming.kantech.mygathering.R;
import com.programming.kantech.mygathering.utils.Constants;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by patri on 2017-10-19.
 */

public class Fragment_Add_Dates extends Fragment {

    private long mStartDate;
    private long mStartTime;
    private long mEndDate;
    private long mEndTime;

    @BindView(R.id.btn_next_to_banner)
    Button btn_proceed;

    @BindView(R.id.tv_add_gathering_start_date)
    TextView tv_add_gathering_start_date;

    @BindView(R.id.tv_add_gathering_end_date)
    TextView tv_add_gathering_end_date;

    @BindView(R.id.tv_add_gathering_start_time)
    TextView tv_add_gathering_start_time;

    @BindView(R.id.tv_add_gathering_end_time)
    TextView tv_add_gathering_end_time;

    // Define a new interface DatesListener that triggers a callback in the host activity
    DatesListener mCallback;

    // AddDatesListener interface, calls a method in the host activity
    // after a callback has been triggered
    public interface DatesListener {
        void addDateInfo(String nextFrag, String start_date, String end_date);
    }

    /**
     * Static factory method that
     * initializes the fragment's arguments, and returns the
     * new fragment to the client.
     */
    public static Fragment_Add_Dates newInstance() {
        return new Fragment_Add_Dates();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Get the fragment layout for the driving list
        final View rootView = inflater.inflate(R.layout.fragment_dates, container, false);

        ButterKnife.bind(this, rootView);


        return rootView;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupForm();
    }

    // Override onAttach to make sure that the container activity has implemented the callback
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the host activity has implemented the callback interface
        // If not, it throws an exception
        try {
            mCallback = (Fragment_Add_Dates.DatesListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement DetailsListener");
        }
    }

    @OnClick(R.id.btn_next_to_banner)
    public void proceedToNext() {

        // Validate that the dates are correct

        Calendar cal = Calendar.getInstance(TimeZone.getDefault()); // Get current date

        cal.setTimeInMillis(mStartTime);

        Calendar startDateAndTime = new GregorianCalendar();
        startDateAndTime.setTimeInMillis(mStartDate);

        startDateAndTime.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY));
        startDateAndTime.set(Calendar.MINUTE, cal.get(Calendar.MINUTE));
        startDateAndTime.set(Calendar.SECOND, 0);
        startDateAndTime.set(Calendar.MILLISECOND, 0);

        Date start = new Date(startDateAndTime.getTimeInMillis());

        //new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")

        String start_date = new SimpleDateFormat(Constants.DATE_TIME_UTC_MONGODB, Locale.getDefault()).format(start);

        cal.setTimeInMillis(mEndTime);

        Calendar endDateAndTime = new GregorianCalendar();
        endDateAndTime.setTimeInMillis(mEndDate);

        endDateAndTime.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY));
        endDateAndTime.set(Calendar.MINUTE, cal.get(Calendar.MINUTE));
        endDateAndTime.set(Calendar.SECOND, 0);
        endDateAndTime.set(Calendar.MILLISECOND, 0);

        Date end = new Date(startDateAndTime.getTimeInMillis());

        //new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")

        String end_date = new SimpleDateFormat(Constants.DATE_TIME_UTC_MONGODB, Locale.getDefault()).format(end);

        mCallback.addDateInfo(Constants.TAG_FRAGMENT_ADD_BANNER, start_date, end_date);








    }

    @OnClick(R.id.iv_add_start_date)
    public void getStartDate() {

        Calendar cal = Calendar.getInstance(TimeZone.getDefault()); // Get current date

        // Create the DatePickerDialog instance
        DatePickerDialog datePicker = new DatePickerDialog(getContext(),
                R.style.ThemeOverlay_AppCompat_Dialog_Alert, datePickerListenerStartDate,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH));
        datePicker.setCancelable(false);
        datePicker.setTitle("Start Date");
        datePicker.show();

    }

    @OnClick(R.id.iv_add_end_date)
    public void getEndDate() {

        Calendar cal = Calendar.getInstance(TimeZone.getDefault()); // Get current date

        // Create the DatePickerDialog instance
        DatePickerDialog datePicker = new DatePickerDialog(getContext(),
                R.style.ThemeOverlay_AppCompat_Dialog_Alert, datePickerListenerEndDate,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH));
        datePicker.setCancelable(false);
        datePicker.setTitle("Start Date");
        datePicker.show();

    }

    @OnClick(R.id.iv_add_start_time)
    public void getStartTime() {

        Calendar cal = Calendar.getInstance(TimeZone.getDefault()); // Get current date

        // Create the DatePickerDialog instance
        TimePickerDialog timePicker = new TimePickerDialog(getContext(),
                R.style.ThemeOverlay_AppCompat_Dialog_Alert, timePickerListenerStartTime,
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                false);

        timePicker.setCancelable(false);
        timePicker.setTitle("Start Time");
        timePicker.show();

    }

    @OnClick(R.id.iv_add_end_time)
    public void getEndTime() {

        Calendar cal = Calendar.getInstance(TimeZone.getDefault()); // Get current date

        // Create the DatePickerDialog instance
        TimePickerDialog timePicker = new TimePickerDialog(getContext(),
                R.style.ThemeOverlay_AppCompat_Dialog_Alert, timePickerListenerEndTime,
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                false);

        timePicker.setCancelable(false);
        timePicker.setTitle("End Time");
        timePicker.show();

    }

    // Date picker listener for start date
    private DatePickerDialog.OnDateSetListener datePickerListenerStartDate = new DatePickerDialog.OnDateSetListener() {

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

            mStartDate = c.getTimeInMillis();
            setupForm();

        }
    };

    // Date picker listener for end date
    private DatePickerDialog.OnDateSetListener datePickerListenerEndDate = new DatePickerDialog.OnDateSetListener() {

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

            mEndDate = c.getTimeInMillis();
            setupForm();

        }
    };

    private TimePickerDialog.OnTimeSetListener timePickerListenerStartTime = new TimePickerDialog.OnTimeSetListener() {

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            Log.i(Constants.TAG, "Start Time Hour:" + hourOfDay);
            Log.i(Constants.TAG, "Start Time Min:" + minute);

            final Calendar c = Calendar.getInstance();

            c.set(Calendar.HOUR_OF_DAY, hourOfDay);
            c.set(Calendar.MINUTE, minute);
            c.set(Calendar.SECOND, 0);

            mStartTime = c.getTimeInMillis();

            setupForm();
        }
    };

    private TimePickerDialog.OnTimeSetListener timePickerListenerEndTime = new TimePickerDialog.OnTimeSetListener() {

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            final Calendar c = Calendar.getInstance();

            c.set(Calendar.HOUR_OF_DAY, hourOfDay);
            c.set(Calendar.MINUTE, minute);
            c.set(Calendar.SECOND, 0);

            mEndTime = c.getTimeInMillis();

            setupForm();
        }
    };

    private void setupForm() {

        boolean haveDatesBeenSelected = true;

        if (mStartDate == 0) {
            haveDatesBeenSelected = false;

        } else {
            //tv_add_gathering_start_date.setVisibility(View.VISIBLE);
            SimpleDateFormat format = new SimpleDateFormat(Constants.DATE_FORMAT, Locale.getDefault());

            String dateString;

            dateString = format.format(mStartDate);
            tv_add_gathering_start_date.setText(dateString);
        }

        if (mEndDate == 0) {
            haveDatesBeenSelected = false;

        } else {
            //tv_add_gathering_start_date.setVisibility(View.VISIBLE);
            SimpleDateFormat format = new SimpleDateFormat(Constants.DATE_FORMAT, Locale.getDefault());

            String dateString;

            dateString = format.format(mEndDate);
            tv_add_gathering_end_date.setText(dateString);
        }

        if (mStartTime == 0) {
            haveDatesBeenSelected = false;
        } else {

            SimpleDateFormat format = new SimpleDateFormat(Constants.TIME_FORMAT, Locale.getDefault());

            String start_time = format.format(mStartTime);

            tv_add_gathering_start_time.setText(start_time);
        }

        if (mEndTime == 0) {
            haveDatesBeenSelected = false;

        } else {

            SimpleDateFormat format = new SimpleDateFormat(Constants.TIME_FORMAT, Locale.getDefault());

            String start_time = format.format(mEndTime);

            tv_add_gathering_end_time.setText(start_time);
        }

        if (!haveDatesBeenSelected) {
            btn_proceed.setEnabled(false);
            btn_proceed.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryLight));
        } else {
            btn_proceed.setEnabled(true);
            btn_proceed.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));

        }
    }
}
