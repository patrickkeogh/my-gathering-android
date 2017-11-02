package com.programming.kantech.mygathering.view.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.programming.kantech.mygathering.R;
import com.programming.kantech.mygathering.data.model.mongo.Result_Register;
import com.programming.kantech.mygathering.data.model.mongo.UserCredentials;
import com.programming.kantech.mygathering.data.retrofit.ApiClient;
import com.programming.kantech.mygathering.data.retrofit.ApiInterface;
import com.programming.kantech.mygathering.utils.Constants;
import com.programming.kantech.mygathering.utils.Utils_General;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by patrick keogh on 2017-04-09.
 *
 */

public class Activity_Register extends AppCompatActivity {

    private ApiInterface apiService;

    private static final int REQUEST_REGISTER = 0;

    private ProgressDialog progressDialog;

    @BindView(R.id.input_name)
    EditText mNameText;
    @BindView(R.id.input_email)
    EditText mEmailText;
    @BindView(R.id.btn_register)
    Button mRegisterButton;
    @BindView(R.id.link_login)
    TextView mLoginLink;

    // create media player to play sound when login button is clicked
    //private MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        apiService = ApiClient.getClient().create(ApiInterface.class);

        progressDialog = new ProgressDialog(Activity_Register.this,
                R.style.Theme_AppCompat_Light_Dialog);

        ButterKnife.bind(this);

    }

    @OnClick(R.id.link_login)
    public void goToLogin() {

        //mp.start();

        // Start the Signup activity
        Intent intent = new Intent(getApplicationContext(), Activity_Login.class);
        startActivityForResult(intent, REQUEST_REGISTER);

        finish();
    }

    @OnClick(R.id.btn_register)
    public void register() {

        //mp.start();

        if (!validate()) {
            onRegisterFailed("Registration Failed");
            return;
        }

        if (Utils_General.isNetworkAvailable(this)) {

            mRegisterButton.setEnabled(false);

            String email = mEmailText.getText().toString();
            String name = mNameText.getText().toString();


            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Registering...");
            progressDialog.show();

            Call<Result_Register> call = apiService.register(new UserCredentials(email, "welcome", name));

            call.enqueue(new Callback<Result_Register>() {
                @Override
                public void onResponse(@NonNull Call<Result_Register> call, @NonNull Response<Result_Register> response) {

                    Log.i(Constants.TAG, "Response:" + response.toString());

                    if (response.isSuccessful()) {
                        // Status codes 200, 201, etc
                        Result_Register result = response.body();

                        // Notify the activity that registration was successfull
                        onRegistrationSuceeded(result);
                    } else {
                        // status codes 400, 401, etc.
                        // Notify the activity that registration was NOT successfull
                        switch (response.code()) {
                            case 400:
                                onRegisterFailed("The supplied email address has already be used to register!");
                                break;
                            default:
                                onRegisterFailed(response.message());

                        }

                    }
                }

                @Override
                public void onFailure(@NonNull Call<Result_Register> call, @NonNull Throwable t) {
                    // Log error here since request failed
                    Log.i(Constants.TAG, "Registration Failed");
                    Log.e(Constants.TAG, t.toString());
                    onRegisterFailed(t.toString());
                }
            });
        } else {
            onRegisterFailed("You do NOT have an internet connection");
        }

    }



    public boolean validate() {
        boolean valid = true;

        String name = mNameText.getText().toString();
        String email = mEmailText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            mNameText.setError("at least 3 characters");
            valid = false;
        } else {
            mNameText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mEmailText.setError("enter a valid email address");
            valid = false;
        } else {
            mEmailText.setError(null);
        }

        return valid;
    }

    public void onRegisterFailed(String reason) {
        progressDialog.dismiss();
        Utils_General.showToast(getBaseContext(), reason);
        mRegisterButton.setEnabled(true);
    }

    // Hook method called when login is complete
    public void onRegistrationSuceeded(Result_Register result) {
        progressDialog.dismiss();
        Utils_General.showToast(getBaseContext(), result.getStatus());
        startActivity(new Intent(this, Activity_Login.class));
        finish();

    }


    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }
}


