package com.programming.kantech.mygathering.sync;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;
import com.programming.kantech.mygathering.utils.Constants;
import com.programming.kantech.mygathering.utils.Utils_Preferences;

import java.util.concurrent.TimeUnit;

/**
 * Created by patrick keogh on 2017-06-04.
 */

public class ReminderUtilities {

    //  - REMINDER_INTERVAL_SECONDS should be an integer constant storing the number of seconds in 15 minutes
    //  - SYNC_FLEXTIME_SECONDS should also be an integer constant storing the number of seconds in 15 minutes
    //  - sInitialized should be a private static boolean variable which will store whether the job
    //    has been activated or not
    /*
     * Interval at which to search for new gatherings matching the users preferred search filters.
     * Use TimeUnit for convenience, rather than writing out a bunch of multiplication ourselves
     * and risk making a silly mistake.
     */

    //private static final int REMINDER_INTERVAL_MINUTES = 1;
    //private static final int REMINDER_INTERVAL_SECONDS = (int) (TimeUnit.MINUTES.toSeconds(REMINDER_INTERVAL_MINUTES));
    //private static final int SYNC_FLEXTIME_SECONDS = REMINDER_INTERVAL_SECONDS;

    private static final String REMINDER_JOB_TAG = "search-new-gatherings-job";

    private static boolean sInitialized;

    // A synchronized, public static method called scheduleNewGatheringSearch that takes
    // in a context. This method will use FirebaseJobDispatcher to schedule a job that repeats roughly
    // every REMINDER_INTERVAL_SECONDS. It will trigger NewGatheringSearchFirebaseJobService
    // Checkout https://github.com/firebase/firebase-jobdispatcher-android for an example
    synchronized public static void scheduleNewGatheringSearch(@NonNull final Context context) {

        Log.i(Constants.TAG, "Entered NewGatherSearch()");

        // if the job has already been initialized, return
        if (sInitialized) return;

        //reminder_interval_seconds = Utils_Preferences.getSearchFrequency(context);

        Log.i(Constants.TAG, "FREQUENCY: ReminderUtils:" + Utils_Preferences.getSearchFrequency(context));

        int reminder_interval_seconds = 120;

        try {
            reminder_interval_seconds = Integer.parseInt(Utils_Preferences.getSearchFrequency(context));
        } catch(NumberFormatException nfe) {
            System.out.println("Could not parse " + nfe);
        }

        // Create a new GooglePlayDriver
        Driver driver = new GooglePlayDriver(context);

        // Create a new FirebaseJobDispatcher with the driver
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);

        Job seachForNewGatherings = dispatcher.newJobBuilder()
                /* The Service that will be used to write to preferences */
                .setService(JobService_SearchForNewGatherings.class)
                /*
                 * Set the UNIQUE tag used to identify this Job.
                 */
                .setTag(REMINDER_JOB_TAG)
                /*
                 * Network constraints on which this Job should run. In this app, we're using the
                 * device charging constraint so that the job only executes if the device is
                 * charging.
                 *
                 * In a normal app, it might be a good idea to include a preference for this,
                 * as different users may have different preferences on when you should be
                 * syncing your application's data.
                 */
                //.setConstraints(Constraint.DEVICE_CHARGING)
                /*
                 * setLifetime sets how long this job should persist. The options are to keep the
                 * Job "forever" or to have it die the next time the device boots up.
                 */
                .setLifetime(Lifetime.FOREVER)
                /*
                 * We want these reminders to continuously happen, so we tell this Job to recur.
                 */
                .setRecurring(true)
                /*
                 * We want the reminders to happen every 15 minutes or so. The first argument for
                 * Trigger class's static executionWindow method is the start of the time frame
                 * when the
                 * job should be performed. The second argument is the latest point in time at
                 * which the data should be synced. Please note that this end time is not
                 * guaranteed, but is more of a guideline for FirebaseJobDispatcher to go off of.
                 */
                .setTrigger(Trigger.executionWindow(60, 120))
                /*
                 * If a Job with the tag with provided already exists, this new job will replace
                 * the old one.
                 */
                .setReplaceCurrent(true)
                /* Once the Job is ready, call the builder's build method to return the Job */
                .build();

        /* Schedule the Job with the dispatcher */
        dispatcher.schedule(seachForNewGatherings);

        /* The job has been initialized */
        sInitialized = true;

    }

    // A synchronized, public static method called scheduleNewGatheringSearch that takes
    // in a context. This method will use FirebaseJobDispatcher to stop a scheduled job
    synchronized public static void unscheduleNewGatheringSearch(@NonNull final Context context) {

        Log.i(Constants.TAG, "Entered unscheduleNewGatheringSearch()");

        // if the job has not already been initialized, return
        if (!sInitialized) return;

        // Create a new GooglePlayDriver
        Driver driver = new GooglePlayDriver(context);

        // Create a new FirebaseJobDispatcher with the driver
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);

        dispatcher.cancelAll();

        /* Set the job to uninitialized */
        sInitialized = false;

    }


}
