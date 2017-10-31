package com.programming.kantech.mygathering.view.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.programming.kantech.mygathering.R;
import com.programming.kantech.mygathering.utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

import static android.support.v7.appcompat.R.styleable.AlertDialog;

/**
 * Created by patri on 2017-10-19.
 *
 */

public class Fragment_Add_Save extends Fragment {

    private String mStatus;
    private String mAccess;

    @BindView(R.id.btn_save_new_gathering)
    Button btn_save;

    @BindView(R.id.rb_add_gathering_access_public)
    RadioButton rb_add_gathering_access_public;

    @BindView(R.id.rb_add_gathering_status_published)
    RadioButton rb_add_gathering_status_published;

    // Define a new interface AddBannerListener that triggers a callback in the host activity
    SaveListener mCallback;

    // AddBannerListener interface, calls a method in the host activity
    // after a callback has been triggered
    public interface SaveListener {
        void createNewGathering(String nextFrag, String access, String status);
        void cancelNewGathering(String nextFrag);
    }

    /**
     * Static factory method that
     * initializes the fragment's arguments, and returns the
     * new fragment to the client.
     */
    public static Fragment_Add_Save newInstance() {
        return new Fragment_Add_Save();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Get the fragment layout for the driving list
        final View rootView = inflater.inflate(R.layout.fragment_save, container, false);

        ButterKnife.bind(this, rootView);
        return rootView;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        rb_add_gathering_access_public.setChecked(true);
        mAccess = Constants.GATHERING_ACCESS_PUBLIC;

        rb_add_gathering_status_published.setChecked(true);
        mStatus = Constants.GATHERING_STATUS_PUBLISH;

    }

    // Override onAttach to make sure that the container activity has implemented the callback
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the host activity has implemented the callback interface
        // If not, it throws an exception
        try {
            mCallback = (Fragment_Add_Save.SaveListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement SaveListener");
        }
    }

    @OnClick(R.id.btn_cancel_new_gathering)
    public void cancelNewGathering() {

        new AlertDialog.Builder(getContext())
                .setTitle("Confirmation Required")
                .setMessage("Please confirm you want to cancel this new gathering?")
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mCallback.cancelNewGathering(Constants.TAG_FRAGMENT_ADD_DETAILS);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        //Utils_General.showToast(Activity_Checkout.this, getString(R.string.order_cancelled));
                    }
                })
                .show();


    }

    @OnClick(R.id.btn_save_new_gathering)
    public void saveNewGathering() {

        new AlertDialog.Builder(getContext())
                .setTitle("Confirmation Required")
                .setMessage("Please confirm your new gathering?")
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mCallback.createNewGathering(Constants.TAG_FRAGMENT_ADD_DETAILS, mAccess, mStatus);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        //Utils_General.showToast(Activity_Checkout.this, getString(R.string.order_cancelled));
                    }
                })
                .show();

    }

    @OnCheckedChanged({R.id.rb_add_gathering_access_public, R.id.rb_add_gathering_access_private})
    public void onRadioButtonAccessChanged(CompoundButton button, boolean checked) {
        if (checked) {
            switch (button.getId()) {
                case R.id.rb_add_gathering_access_public:
                    mAccess = Constants.GATHERING_ACCESS_PUBLIC;
                    break;
                case R.id.rb_add_gathering_access_private:
                    mAccess = Constants.GATHERING_ACCESS_PRIVATE;
                    break;
            }
        }
    }

    @OnCheckedChanged({R.id.rb_add_gathering_status_published, R.id.rb_add_gathering_status_notpublished})
    public void onRadioButtonStatusChanged(CompoundButton button, boolean checked) {
        if (checked) {
            switch (button.getId()) {
                case R.id.rb_add_gathering_status_published:
                    mStatus = Constants.GATHERING_STATUS_PUBLISH;
                    break;
                case R.id.rb_add_gathering_status_notpublished:
                    mStatus = Constants.GATHERING_STATUS_NOT_PUBLISHED;
                    break;
            }
        }
    }
}
