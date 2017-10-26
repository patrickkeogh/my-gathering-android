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
     * Base url for google address details
     */
    public final static String BASE_URL_GOOGLE = "https://maps.googleapis.com/maps/api/place/details/";

    /**
     * Base url for google address details
     */
    public final static String BASE_URL_FILESTACK = "https://process.filestackapi.com/";


    public final static String KEY_GOOGLE = "AIzaSyC9g7DondEpw_OzTTJkWoG4z985fN0HgOM";

    /**
     * Key for File Stack
     */
    public final static String KEY_FILESTACK = "ANNrSlVqZSbCvpZVLcwspz";

    //https://maps.googleapis.com/maps/api/place/details/json?placeid=ChIJ7eSOJC-jKogRuckm35Fy600&key=AIzaSyC9g7DondEpw_OzTTJkWoG4z985fN0HgOM


    /**
     * The "Content authority" is a name for the entire content provider, similar to the
     * relationship between a domain name and its website.  A convenient string to use for the
     * content authority is the package name for the app, which is guaranteed to be unique on the
     * device.
     */
    public static final String CONTENT_AUTHORITY = "com.programming.kantech.mygathering";

    // Format used for storing dates in the database.  ALso used for converting those strings
    // back into date objects for comparison/processing.
    public static final String DATE_FORMAT = "EEEE, MMMM dd, yyyy";
    public static final String TIME_FORMAT = "hh:mm a";
    public static final String DATE_TIME_FORMAT = "MMM-dd-yyyy HH:mm:ss";
    public static final String DATE_TIME_UTC_MONGODB = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";


    // The loader's unique id. Loader ids are specific to the Activity or
    // Fragment in which they reside.
    public static final int GATHERING_DETAIL_LOADER = 1;

    // Constants used for the MyGathering REST api
    public static final String GATHERING_ACCESS_PUBLIC = "Public";
    public static final String GATHERING_ACCESS_PRIVATE = "Private";

    public static final String GATHERING_STATUS_PUBLISH = "Publish";
    public static final String GATHERING_STATUS_NOT_PUBLISHED = "Not Published";


    public static final String[] GATHERING_TOPICS_FILTER = {
            "All",
            "Air, Boat, & Auto",
            "Animals & Nature",
            "Business & Professional"
    };

    public static final String GATHERING_DEFAULT_TOPIC = "Business & Professional";

    public static final String GATHERING_TOPIC_HINT = "Select A Gathering Topic";

    public static final String[] GATHERING_TOPICS = {
            "Select A Gathering Topic",
            "Air, Boat, & Auto",
            "Animals & Nature",
            "Business & Professional"

    };

    public static final String[] GATHERING_TYPES_FILTER = {
            "All",
            "Appearance or Signing",
            "Attraction",
            "Camp, Trip, or Retreat",
            "Class, Training, or Workshop"
    };

    public static final String GATHERING_DEFAULT_TYPE = "Camp, Trip, or Retreat";

    public static final String GATHERING_TYPE_HINT = "Select A Gathering Type";

    public static final String[] GATHERING_TYPES = {
            "Select A Gathering Type",
            "Appearance or Signing",
            "Attraction",
            "Camp, Trip, or Retreat",
            "Class, Training, or Workshop"


    };

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

    /**
     * The Request Codes needed in Implicit Intents
     */
    public static final int REQUEST_CODE_GET_LOCATION = 1;
    public static final int REQUEST_CODE_GET_QUERY = 2;
    public static final int REQUEST_CODE_PLACE_PICKER = 3;

    /**
     * The Constants used for state objects
     */
    public static final String STATE_FRAGMENT_TAG = "com.programming.kantech.mygathering.app.state.fragment_tag";
    public static final String STATE_DETAILS_URI = "com.programming.kantech.mygathering.app.state.details_uri";

    /**
     * The Constants used for data added as extras to intents
     */
    public static final String EXTRA_LOCATION_NAME = "location_name";
    public static final String EXTRA_LOCATION_ADDRESS = "location_address";
    public static final String EXTRA_LOCATION_LAT = "location_lat";
    public static final String EXTRA_LOCATION_LONG = "location_long";
    public static final String EXTRA_SEARCH_QUERY = "search_query";
    public static final String EXTRA_DETAILS_URI = "details_uri";

    public static final String EXTRA_FRAGMENT_TAG = "com.programming.kantech.mygathering.app.extra.fragment_tag";

    /**
     * The Constants used for fragment tags
     */
    public static final String TAG_FRAGMENT_HEADER =
            "com.programming.kantech.mygathering.app.views.ui.fragment_nav_header";

    public static final String TAG_FRAGMENT_ADD_DETAILS =
            "com.programming.kantech.mygathering.app.views.ui.fragment_add_details";

    public static final String TAG_FRAGMENT_ADD_LOCATION =
            "com.programming.kantech.mygathering.app.views.ui.fragment_add_locations";

    public static final String TAG_FRAGMENT_ADD_DATES =
            "com.programming.kantech.mygathering.app.views.ui.fragment_add_dates";

    public static final String TAG_FRAGMENT_ADD_BANNER =
            "com.programming.kantech.mygathering.app.views.ui.fragment_add_banner";

    public static final String TAG_FRAGMENT_ADD_SAVE =
            "com.programming.kantech.mygathering.app.views.ui.fragment_add_save";

    public static final String TAG_FRAGMENT_MAIN =
            "com.programming.kantech.mygathering.app.views.ui.fragment_main";

    public static final String TAG_FRAGMENT_MAIN_LIST =
            "com.programming.kantech.mygathering.app.views.ui.fragment_gathering_list";

    public static final String TAG_FRAGMENT_MAIN_DETAILS =
            "com.programming.kantech.mygathering.app.views.ui.fragment_gathering_list";




    public static final String COMPONENT_TYPE_LOCALITY = "locality";
    public static final String COMPONENT_TYPE_COUNTRY = "country";
    public static final String COMPONENT_TYPE_CITY = "sublocality_level_1";
    public static final String COMPONENT_TYPE_POSTAL = "postal_code";
    public static final String COMPONENT_TYPE_PROV = "administrative_area_level_1";






}
