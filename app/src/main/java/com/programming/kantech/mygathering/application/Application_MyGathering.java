package com.programming.kantech.mygathering.application;

import android.content.Context;

import com.programming.kantech.mygathering.data.model.mongo.Owner;

/**
 * Created by Patrick Keogh on 06/06/2017.
 */
public class Application_MyGathering extends android.app.Application {

    private static Application_MyGathering instance;

    private Owner currentUser;
    private String token;

    private static boolean activityAdminAppointmentsVisible;

    public Application_MyGathering() {
        instance = this;
    }

    public static Context getContext() {
        return instance;
    }

    public Owner getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(Owner currentUser) {
        this.currentUser = currentUser;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}