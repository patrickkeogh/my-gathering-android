package com.programming.kantech.mygathering.utils;

import com.programming.kantech.mygathering.provider.Contract_MyGathering;

/**
 * Created by patrick keogh on 2017-05-18.
 */

public class Constants {

    /**
     * Debugging tag used by the Android logger.
     */
    public final static String TAG = "MyGathering Client App:";

    /**
     * Base url for server running on Heroku
     */
    public final static String BASE_URL = "https://my-gathering.herokuapp.com/";

    /**
     * The "Content authority" is a name for the entire content provider, similar to the
     * relationship between a domain name and its website.  A convenient string to use for the
     * content authority is the package name for the app, which is guaranteed to be unique on the
     * device.
     */
    public static final String CONTENT_AUTHORITY = "com.programming.kantech.mygathering";

    // The loader's unique id. Loader ids are specific to the Activity or
    // Fragment in which they reside.
    public static final int GATHERING_DETAIL_LOADER = 1;

    /*
     * The columns of data that we are interested in displaying within our MainActivity's list of
     * weather data.
     */
    public static final String[] LOADER_GATHERING_DETAIL_COLUMNS = {
            Contract_MyGathering.GatheringEntry._ID,
            Contract_MyGathering.GatheringEntry.COLUMN_GATHERING_ID,
            Contract_MyGathering.GatheringEntry.COLUMN_GATHERING_NAME,
            Contract_MyGathering.GatheringEntry.COLUMN_GATHERING_DESC,
            Contract_MyGathering.GatheringEntry.COLUMN_GATHERING_TYPE,
            Contract_MyGathering.GatheringEntry.COLUMN_GATHERING_TOPIC,
            Contract_MyGathering.GatheringEntry.COLUMN_GATHERING_START_DATE,
            Contract_MyGathering.GatheringEntry.COLUMN_GATHERING_END_DATE,
            Contract_MyGathering.GatheringEntry.COLUMN_GATHERING_LOCATION_COUNTRY,
            Contract_MyGathering.GatheringEntry.COLUMN_GATHERING_LOCATION_LOCALITY,
            Contract_MyGathering.GatheringEntry.COLUMN_GATHERING_LOCATION_POSTAL,
            Contract_MyGathering.GatheringEntry.COLUMN_GATHERING_LOCATION_PROV,
            Contract_MyGathering.GatheringEntry.COLUMN_GATHERING_LOCATION_NAME,
            Contract_MyGathering.GatheringEntry.COLUMN_GATHERING_LOCATION_NOTES,
            Contract_MyGathering.GatheringEntry.COLUMN_GATHERING_STATUS,
            Contract_MyGathering.GatheringEntry.COLUMN_GATHERING_ACCESS,
            Contract_MyGathering.GatheringEntry.COLUMN_GATHERING_OWNER_ID,
            Contract_MyGathering.GatheringEntry.COLUMN_GATHERING_OWNER_USERNAME,
            Contract_MyGathering.GatheringEntry.COLUMN_GATHERING_BANNER_URL,
            Contract_MyGathering.GatheringEntry.COLUMN_GATHERING_CREATED_DATE

    };

    /*
     * We store the indices of the values in the array of Strings above to more quickly be able to
     * access the data from our query. If the order of the Strings above changes, these indices
     * must be adjusted to match the order of the Strings.
     */
    public static final int COL_ID = 0;
    public static final int COL_GATHERING_ID = 1;
    public static final int COL_GATHERING_NAME = 2;
    public static final int COL_GATHERING_DESC = 3;
    public static final int COL_GATHERING_TYPE = 4;
    public static final int COL_GATHERING_TOPIC = 5;
    public static final int COL_GATHERING_START_DATE = 6;
    public static final int COL_GATHERING_END_DATE = 7;
    public static final int COL_GATHERING_LOCATION_COUNTRY = 8;
    public static final int COL_GATHERING_LOCATION_LOCALITY = 9;
    public static final int COL_GATHERING_LOCATION_POSTAL = 10;
    public static final int COL_GATHERING_LOCATION_PROV = 11;
    public static final int COL_GATHERING_LOCATION_NAME = 12;
    public static final int COL_GATHERING_LOCATION_NOTES = 13;
    public static final int COL_GATHERING_STATUS = 14;
    public static final int COL_GATHERING_ACCESS = 15;
    public static final int COL_GATHERING_OWNER_ID = 16;
    public static final int COL_GATHERING_OWNER_USERNAME = 17;
    public static final int COL_GATHERING_BANNER_URL = 18;
    public static final int COL_GATHERING_CREATED_DATE = 19;




}
