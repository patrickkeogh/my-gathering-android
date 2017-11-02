package com.programming.kantech.mygathering.view.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.programming.kantech.mygathering.R;
import com.programming.kantech.mygathering.application.Application_MyGathering;
import com.programming.kantech.mygathering.data.model.mongo.Owner;
import com.programming.kantech.mygathering.data.model.mongo.Result_Login;
import com.programming.kantech.mygathering.data.model.mongo.UserCredentials;
import com.programming.kantech.mygathering.data.retrofit.ApiClient;
import com.programming.kantech.mygathering.data.retrofit.ApiInterface;
import com.programming.kantech.mygathering.utils.Constants;
import com.programming.kantech.mygathering.utils.Utils_General;
import com.programming.kantech.mygathering.utils.Utils_Preferences;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by patrick keogh on 2017-04-08.
 *
 */

public class Activity_Login extends AppCompatActivity {

    private ApiInterface apiService;

    private static final int REQUEST_LOGIN = 0;
    private ProgressDialog progressDialog;

    @BindView(R.id.input_email)
    EditText mEmailText;
    @BindView(R.id.input_password)
    EditText mPasswordText;
    @BindView(R.id.link_signup)
    TextView mSignupLink;
    @BindView(R.id.btn_login)
    Button mLoginButton;

    @BindView(R.id.saveLoginCheckBox)
    CheckBox mSaveLoginCheckBox;

    // create media player to play sound when login button is clicked
    //private MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        apiService =
                ApiClient.getClient().create(ApiInterface.class);

        progressDialog  = new ProgressDialog(Activity_Login.this,
                R.style.Theme_AppCompat_Light_Dialog);

        ButterKnife.bind(this);

        //mp = MediaPlayer.create(this, R.raw.bubble_pop);

        loadSavedCredentialsIfAvailable();

    }

    private void loadSavedCredentialsIfAvailable() {

        String preferredEmail = Utils_Preferences.getPreferredEmail(getApplicationContext());
        mEmailText.setText(preferredEmail);
        mSaveLoginCheckBox.setChecked(Utils_Preferences.getSaveCredentials(getApplicationContext()));

    }

    @OnClick(R.id.link_signup)
    public void goToRegister() {

        //mp.start();

        // Start the Signup activity
        Intent intent = new Intent(getApplicationContext(), Activity_Register.class);
        startActivityForResult(intent, REQUEST_LOGIN);

        finish();
    }

    @OnClick(R.id.btn_login)
    public void login_local() {
        login();
    }

    public void login() {
        //mp.start();

        if (!validate()) {
            onLoginFailed("Login Failed");
            return;
        }

        if (Utils_General.isNetworkAvailable(this)) {

            mLoginButton.setEnabled(false);

            String email = mEmailText.getText().toString();
            String password = mPasswordText.getText().toString();




            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Authenticating...");
            progressDialog.show();

            Call<Result_Login> call = apiService.login(new UserCredentials(email, password, ""));

            call.enqueue(new Callback<Result_Login>() {
                @Override
                public void onResponse(@NonNull Call<Result_Login>call, @NonNull Response<Result_Login> response) {

                    //Log.i(Constants.TAG, "Response:" + response);

                    if(response.isSuccessful()){
                        // Code 200, 201
                        Result_Login result = response.body();

                        // Notify the activity login was successfull
                        login_ok(result);
                    }else{
                        // code 400, 401, etc
                        switch (response.code()){
                            case 401:
                                onLoginFailed("Password or username are incorrect");
                            default:
                                onLoginFailed(response.message());
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Result_Login>call, @NonNull Throwable t) {
                    // Log error here since request failed
                    Log.e(Constants.TAG, t.toString());
                    onLoginFailed(t.toString());
                }
            });

        } else {
            onLoginFailed("You do NOT have an internet connection");
        }
    }

    public boolean validate() {
        boolean valid = true;

        String email = mEmailText.getText().toString();
        String password = mPasswordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mEmailText.setError("enter a valid email address");
            valid = false;
        } else {
            mEmailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            mPasswordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            mPasswordText.setError(null);
        }

        return valid;
    }
    // Hook method called when login is complete and succeeded
    public void login_ok(Result_Login result) {

        Application_MyGathering app = (Application_MyGathering) getApplication();

        Owner owner = new Owner();
        owner.setOwnerId(result.get_id());
        owner.setUsername(mEmailText.getText().toString());
        owner.setName(result.getName());

        app.setCurrentUser(owner);
        app.setToken(result.getToken());

        progressDialog.dismiss();
        //Log.i(Constants.TAG, "Login Result:" + result.toString());


        if (mSaveLoginCheckBox.isChecked()) {
            //Log.i(Constants.TAG, "Save Credentials in Prefs");

            // Store the preferred email address in preferences
            Utils_Preferences.savePreferredEmail(getApplicationContext(), mEmailText.getText().toString());

            // We are not storing passwords
            //Utils_Preferences.saveLoggedPassword(getApplicationContext(), _passwordText.getText().toString());

            // Save the option to use prefered email
            Utils_Preferences.saveCredentials(getApplicationContext(), true);

        } else {
            //
            // Log.i(Constants.TAG, "Remove Credentials in Prefs");

            // Remove the preferred email address in preferences
            Utils_Preferences.savePreferredEmail(getApplicationContext(), this.getString(R.string.pref_preferred_email_default));

            // Save the option to not use preferred email
            Utils_Preferences.saveCredentials(getApplicationContext(), false);

            // Remove the sign in credentials from  preferences
//            Utils_Preferences.saveLoggedEmail(this, "");
//            Utils_Preferences.saveLoggedPassword(this, "");
//            Utils_Preferences.saveCredentials(getApplicationContext(), false);
        }

        startActivity(new Intent(this, Activity_Main.class));

        Activity_Login.this.finish();

    }

    // Hook method called when login failed
    public void onLoginFailed(String reason) {
        progressDialog.dismiss();

        Utils_General.showToast(getBaseContext(), reason);

        mLoginButton.setEnabled(true);
    }

}

