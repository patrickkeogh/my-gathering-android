package com.programming.kantech.mygathering.application;

import android.content.Context;

/**
 * Created by Patrick Keogh on 06/06/2017.
 */
public class Application_MyGathering extends android.app.Application {

    private static Application_MyGathering instance;

    private static boolean activityAdminAppointmentsVisible;

    public Application_MyGathering() {
        instance = this;
    }
    public static Context getContext() {
        return instance;
    }


}