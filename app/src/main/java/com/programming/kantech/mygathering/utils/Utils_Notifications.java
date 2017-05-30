package com.programming.kantech.mygathering.utils;

/**
 * Created by patrick keogh on 2017-05-30.
 *
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
     * Ensure this class is only used as a utility.
     */
    private Utils_Notifications() {
        throw new AssertionError();
    }
}
