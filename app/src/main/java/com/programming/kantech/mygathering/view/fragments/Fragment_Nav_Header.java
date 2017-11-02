package com.programming.kantech.mygathering.view.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.programming.kantech.mygathering.R;
import com.programming.kantech.mygathering.utils.Constants;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by patri on 2017-10-19.
 */

public class Fragment_Nav_Header extends Fragment {

    private String mFragmentTag;

    @BindView(R.id.tv_nav_header_details)
    TextView tv_nav_header_details;

    @BindView(R.id.tv_nav_header_location)
    TextView tv_nav_header_location;

    @BindView(R.id.tv_nav_header_dates)
    TextView tv_nav_header_dates;

    @BindView(R.id.tv_nav_header_banner)
    TextView tv_nav_header_banner;

    @BindView(R.id.tv_nav_header_save)
    TextView tv_nav_header_save;

    @BindView(R.id.iv_nav_header_location)
    ImageView iv_nav_header_location;

    @BindView(R.id.iv_nav_header_details)
    ImageView iv_nav_header_details;

    @BindView(R.id.iv_nav_header_dates)
    ImageView iv_nav_header_dates;

    @BindView(R.id.iv_nav_header_banner)
    ImageView iv_nav_header_banner;

    @BindView(R.id.iv_nav_header_save)
    ImageView iv_nav_header_save;

    @BindView(R.id.layout_nav_details)
    LinearLayout layout_nav_details;

    @BindView(R.id.layout_nav_location)
    LinearLayout layout_nav_location;

    @BindView(R.id.layout_nav_dates)
    LinearLayout layout_nav_dates;

    @BindView(R.id.layout_nav_banner)
    LinearLayout layout_nav_banner;

    @BindView(R.id.layout_nav_save)
    LinearLayout layout_nav_save;

    // Define a new interface StepNavClickListener that triggers a callback in the host activity
    NavheaderListener mCallback;

    public void setForm(String nextFrag) {

        mFragmentTag = nextFrag;
        setupNavHeader();
    }

    // NavheaderListener interface, calls a method in the host activity
    // after a callback has been triggered
    public interface NavheaderListener {
        //void onCustomerSaved(Customer customer);
    }

    /**
     * Static factory method that
     * initializes the fragment's arguments, and returns the
     * new fragment to the client.
     */
    public static Fragment_Nav_Header newInstance(String frag) {

        Fragment_Nav_Header f = new Fragment_Nav_Header();
        Bundle args = new Bundle();

        // Add any required arguments for start up - None needed right now
        args.putString(Constants.EXTRA_FRAGMENT_TAG, frag);
        f.setArguments(args);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        // Get the fragment layout for the driving list
        final View rootView = inflater.inflate(R.layout.fragment_header, container, false);

        ButterKnife.bind(this, rootView);


        // Load the saved state if there is one
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(Constants.STATE_FRAGMENT_TAG)) {
                mFragmentTag = savedInstanceState.getString(Constants.STATE_FRAGMENT_TAG);
            }

        } else {

            Bundle args = getArguments();
            mFragmentTag = args.getString(Constants.EXTRA_FRAGMENT_TAG);
        }

        if (mFragmentTag == null) {
            throw new IllegalArgumentException("Must pass EXTRA_FRAGMENT_TAG");
        }

        setupNavHeader();

        return rootView;

    }

    /**
     * Save the current state of this fragment
     */
    @Override
    public void onSaveInstanceState(Bundle currentState) {
        currentState.putString(Constants.STATE_FRAGMENT_TAG, mFragmentTag);
    }

    private void setupNavHeader() {

        Log.i(Constants.TAG, "setupNavheader called");

        tv_nav_header_details.setTextColor(ContextCompat.getColor(getContext(), R.color.colorWhiteFade));
        iv_nav_header_details.setImageResource(R.drawable.ic_details_white_fade_24dp);

        tv_nav_header_location.setTextColor(ContextCompat.getColor(getContext(), R.color.colorWhiteFade));
        iv_nav_header_location.setImageResource(R.drawable.ic_place_white_fade_24dp);

        tv_nav_header_dates.setTextColor(ContextCompat.getColor(getContext(), R.color.colorWhiteFade));
        iv_nav_header_dates.setImageResource(R.drawable.ic_date_white_fade_24dp);

        tv_nav_header_banner.setTextColor(ContextCompat.getColor(getContext(), R.color.colorWhiteFade));
        iv_nav_header_banner.setImageResource(R.drawable.ic_image_white_fade_24dp);

        tv_nav_header_save.setTextColor(ContextCompat.getColor(getContext(), R.color.colorWhiteFade));
        iv_nav_header_save.setImageResource(R.drawable.ic_image_white_fade_24dp);

        layout_nav_details.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        layout_nav_location.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        layout_nav_dates.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        layout_nav_banner.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        layout_nav_save.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));


        switch (mFragmentTag) {
            case Constants.TAG_FRAGMENT_ADD_DETAILS:
                Log.i(Constants.TAG, "Details Frag");

                tv_nav_header_details.setTextColor(ContextCompat.getColor(getContext(), R.color.colorWhite));
                iv_nav_header_details.setImageResource(R.drawable.ic_details_white_24dp);

                layout_nav_details.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
                break;

            case Constants.TAG_FRAGMENT_ADD_LOCATION:
                tv_nav_header_location.setTextColor(ContextCompat.getColor(getContext(), R.color.colorWhite));
                iv_nav_header_location.setImageResource(R.drawable.ic_place_white_24dp);

                layout_nav_location.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
                break;

            case Constants.TAG_FRAGMENT_ADD_DATES:
                tv_nav_header_dates.setTextColor(ContextCompat.getColor(getContext(), R.color.colorWhite));
                iv_nav_header_dates.setImageResource(R.drawable.ic_date_white_24dp);

                layout_nav_dates.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
                break;

            case Constants.TAG_FRAGMENT_ADD_BANNER:
                tv_nav_header_banner.setTextColor(ContextCompat.getColor(getContext(), R.color.colorWhite));
                iv_nav_header_banner.setImageResource(R.drawable.ic_image_white_24dp);

                layout_nav_banner.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorAccent));

                break;

            case Constants.TAG_FRAGMENT_ADD_SAVE:
                tv_nav_header_save.setTextColor(ContextCompat.getColor(getContext(), R.color.colorWhite));
                iv_nav_header_save.setImageResource(R.drawable.ic_image_white_24dp);

                layout_nav_save.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
                break;


        }

    }


}
