package com.programming.kantech.mygathering.provider;

import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

import com.programming.kantech.mygathering.data.model.pojo.Gathering_Pojo;
import com.programming.kantech.mygathering.utils.Constants;

import com.programming.kantech.mygathering.provider.Contract_MyGathering.TopicEntry;
import com.programming.kantech.mygathering.provider.Contract_MyGathering.TypeEntry;
import com.programming.kantech.mygathering.provider.Contract_MyGathering.GatheringEntry;


/**
 * Created by patrick keogh on 2017-05-23.
 */

public class Contract_MyGathering {

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + Constants.CONTENT_AUTHORITY);

    // Possible paths (appended to base content URI for possible URI's)
    public static final String PATH_TOPIC = TopicEntry.TABLE_NAME;
    public static final String PATH_TYPE = TypeEntry.TABLE_NAME;
    public static final String PATH_GATHERINGS = GatheringEntry.TABLE_NAME;

    /* Inner class that defines the table contents of the topics table */
    public static final class GatheringEntry implements BaseColumns {

        /**
         * Use BASE_CONTENT_URI to create the unique URI for Topics Table that
         * apps will use to contact the content provider.
         */
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_GATHERINGS).build();

        /**
         * Name of the database table.
         */
        public static final String TABLE_NAME = "gatherings_table";

        //public static final String _ID = "_id";
        public static final String COLUMN_GATHERING_ID = "gathering_id";
        public static final String COLUMN_GATHERING_NAME = "gathering_name";
        public static final String COLUMN_GATHERING_DESC = "gathering_description";
        public static final String COLUMN_GATHERING_TYPE = "gathering_type";
        public static final String COLUMN_GATHERING_TOPIC = "gathering_topic";
        public static final String COLUMN_GATHERING_START_DATE = "gathering_start_date";
        public static final String COLUMN_GATHERING_END_DATE = "gathering_end_date";

        public static final String COLUMN_GATHERING_CREATED_DATE = "gathering_created_date";

        public static final String COLUMN_GATHERING_LOCATION_COUNTRY = "gathering_location_country";
        public static final String COLUMN_GATHERING_LOCATION_LOCALITY = "gathering_location_locality";
        public static final String COLUMN_GATHERING_LOCATION_POSTAL = "gathering_location_postal";
        public static final String COLUMN_GATHERING_LOCATION_PROV = "gathering_location_prov";
        public static final String COLUMN_GATHERING_LOCATION_NAME = "gathering_location_name";
        public static final String COLUMN_GATHERING_LOCATION_NOTES = "gathering_location_notes";
        public static final String COLUMN_GATHERING_STATUS = "gathering_status";
        public static final String COLUMN_GATHERING_ACCESS = "gathering_access";
        public static final String COLUMN_GATHERING_OWNER_ID = "gathering_owner_id";
        public static final String COLUMN_GATHERING_OWNER_USERNAME = "gathering_owner_username";

        public static final String COLUMN_GATHERING_BANNER_URL = "gathering_banner_url";

        /**
         * Return a Uri that points to the row containing a given id.
         *
         * @param id
         * @return Uri
         */
        public static Uri buildGatheringUri(Long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        /**
         * Create a Patient object with the data from a cursor.
         *
         * @param cursor
         * @return Gathering_Pojo
         */
        public static Gathering_Pojo getGatheringFromCursor(Cursor cursor) {
            //Log.i(Constants.TAG, "Entered getGatheringFromCursor() in Contract_Gathering");

            Gathering_Pojo g = new Gathering_Pojo();

            g.setId(cursor.getLong(cursor.getColumnIndexOrThrow(_ID)));

            g.setGathering_id(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GATHERING_ID)));
            g.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GATHERING_NAME)));
            g.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GATHERING_DESC)));
            g.setTopic(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GATHERING_TOPIC)));
            g.setType(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GATHERING_TYPE)));
            g.setStart_date(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GATHERING_START_DATE)));
            g.setEnd_date(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GATHERING_END_DATE)));
            g.setCreatedAt_date(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GATHERING_CREATED_DATE)));


            g.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GATHERING_STATUS)));
            g.setAccess(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GATHERING_ACCESS)));

            g.setLocation_name(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GATHERING_LOCATION_NAME)));
            g.setLocation_city(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GATHERING_LOCATION_LOCALITY)));
            g.setLocation_prov(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GATHERING_LOCATION_PROV)));
            g.setLocation_postal(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GATHERING_LOCATION_POSTAL)));
            g.setLocation_country(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GATHERING_LOCATION_COUNTRY)));
            g.setLocation_notes(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GATHERING_LOCATION_NOTES)));

            g.setOwner_id(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GATHERING_OWNER_ID)));
            g.setOwner_username(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GATHERING_OWNER_USERNAME)));

            g.setBanner_url(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GATHERING_BANNER_URL)));


            return g;
        }
    }


    /* Inner class that defines the table contents of the topics table */
    public static final class TopicEntry implements BaseColumns {

        /**
         * Use BASE_CONTENT_URI to create the unique URI for Topics Table that
         * apps will use to contact the content provider.
         */
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_TOPIC).build();

        /**
         * Name of the database table.
         */
        public static final String TABLE_NAME = "topics_table";

        public static final String _ID = "_id";
        public static final String COLUMN_TOPIC_NAME = "topic_name";

        /**
         * Return a Uri that points to the row containing a given id.
         *
         * @param id
         * @return Uri
         */
        public static Uri buildTopicUri(Long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    /* Inner class that defines the table contents of the gathering types table */
    public static final class TypeEntry implements BaseColumns {

        /**
         * Use BASE_CONTENT_URI to create the unique URI for Types Table that
         * apps will use to contact the content provider.
         */
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_TYPE).build();

        /**
         * Name of the database table.
         */
        public static final String TABLE_NAME = "types_table";

        public static final String _ID = "_id";
        public static final String COLUMN_TYPE_NAME = "type_name";

        /**
         * Return a Uri that points to the row containing a given id.
         *
         * @param id
         * @return Uri
         */
        public static Uri buildTypeUri(Long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }


}
