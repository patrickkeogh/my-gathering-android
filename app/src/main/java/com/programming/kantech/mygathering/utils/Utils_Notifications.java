package com.programming.kantech.mygathering.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.programming.kantech.mygathering.R;
import com.programming.kantech.mygathering.view.activities.Activity_Splash;

/**
 * Created by patrick keogh on 2017-05-30.
 * <p>
 * Utility class for creating notifications
 */
public class Utils_Notifications {

    /*
     * This notification ID can be used to access our notification after we've displayed it. This
     * can be handy when we need to cancel the notification, or perhaps update it. This number is
     * arbitrary and can be set to whatever you like.
     */
    private static final int GATHERING_REMINDER_NOTIFICATION_ID = 1138;

    /**
     * This pending intent id is used to uniquely reference the pending intent
     */
    private static final int GATHERING_REMINDER_PENDING_INTENT_ID = 3417;

    public static void notifyUserOfNewGatherings(Context context, int count) {

        String title = "";
        String body = "";

        if (count == 1) {
            title = context.getString(R.string.gathering_reminder_notification_title_1);
            body = "A new gathering matching your prefs has been created.";
        } else {
            title = context.getString(R.string.gathering_reminder_notification_title);
            body = count + " new gatherings matching your prefs have been created.";
        }

        // - has a color of R.colorPrimary - use ContextCompat.getColor to get a compatible color
        // - has ic_drink_notification as the small icon
        // - uses icon returned by the largeIcon helper method as the large icon
        // - sets the title to the charging_reminder_notification_title String resource
        // - sets the  .text to the charging_reminder_notification_body String resource
        // - sets the style to NotificationCompat.BigTextStyle().bigText(text)
        // - sets the notification defaults to vibrate
        // - uses the content intent returned by the contentIntent helper method for the contentIntent
        // - automatically cancels the notification when the notification is clicked
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setSmallIcon(R.mipmap.ic_add_alert_white_18dp)
//                .setLargeIcon(largeIcon(context))
                .setContentTitle(title)
                .setContentText(body)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(body))
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(contentIntent(context))
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setAutoCancel(true);

        // If the build version is greater than JELLY_BEAN, set the notification's priority
        // to PRIORITY_HIGH.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            notificationBuilder.setPriority(Notification.PRIORITY_HIGH);
        }

        // Get a NotificationManager, using context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Trigger the notification by calling notify on the NotificationManager.
        // Pass in a unique ID of your choosing for the notification and notificationBuilder.build()
        notificationManager.notify(GATHERING_REMINDER_NOTIFICATION_ID, notificationBuilder.build());


    }

    // A helper method called contentIntent with a single parameter for a Context. It
    // should return a PendingIntent. This method will create the pending intent which will trigger when
    // the notification is pressed. This pending intent should open up the Splash Screen.
    private static PendingIntent contentIntent(Context context) {

        // An intent that opens up the MainActivity
        Intent startActivityIntent = new Intent(context, Activity_Splash.class);

        // A PendingIntent using getActivity that:
        // - Take the context passed in as a parameter
        // - Takes an unique integer ID for the pending intent (you can create a constant for
        //   this integer above
        // - Takes the intent to open the MainActivity you just created; this is what is triggered
        //   when the notification is triggered
        // - Has the flag FLAG_UPDATE_CURRENT, so that if the intent is created again, keep the
        // intent but update the data
        return PendingIntent.getActivity(
                context,
                GATHERING_REMINDER_PENDING_INTENT_ID,
                startActivityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    // A helper method called largeIcon which takes in a Context as a parameter and
    // returns a Bitmap. This method is necessary to decode a bitmap needed for the notification.
    private static Bitmap largeIcon(Context context) {
        // Get a Resources object from the context.
        Resources res = context.getResources();
        // Create and return a bitmap using BitmapFactory.decodeResource, passing in the
        // resources object and R.mipmap.ic_add_alert_black_24dp
        Bitmap largeIcon = BitmapFactory.decodeResource(res, R.mipmap.ic_add_alert_black_24dp);
        return largeIcon;
    }


    /**
     * Ensure this class is only used as a utility.
     */
    private Utils_Notifications() {
        throw new AssertionError();
    }


}
