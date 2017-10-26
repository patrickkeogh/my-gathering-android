package com.programming.kantech.mygathering.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.programming.kantech.mygathering.provider.Contract_MyGathering.GatheringEntry;
import com.programming.kantech.mygathering.provider.Contract_MyGathering.TypeEntry;
import com.programming.kantech.mygathering.provider.Contract_MyGathering.TopicEntry;
import com.programming.kantech.mygathering.provider.Contract_MyGathering.PlaceEntry;
import com.programming.kantech.mygathering.provider.Contract_MyGathering.FavoriteEntry;

/**
 * Created by patrick keogh on 2017-05-23.
 */

public class DBHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 19;

    // Name of the sql database on the device
    static final String DATABASE_NAME = "mygathering.db";

    /**
     * Constructor for DatabaseHelper. Store the database in the cache
     * directory so Android can remove it if memory is low.
     *
     * @param context
     */
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    /**
     * Called when the database is created for the first time. This is where the
     * creation of tables and the initial population of the tables should happen.
     *
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create the gathering table to share data through out the application
        final String SQL_CREATE_GATHERING_TABLE = "CREATE TABLE " + GatheringEntry.TABLE_NAME + " (" +
                GatheringEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                GatheringEntry.COLUMN_GATHERING_ID + " TEXT NOT NULL, " +
                GatheringEntry.COLUMN_GATHERING_NAME + " TEXT NOT NULL, " +
                GatheringEntry.COLUMN_GATHERING_ACCESS + " TEXT NOT NULL, " +
                GatheringEntry.COLUMN_GATHERING_DESC + " TEXT NOT NULL, " +
                GatheringEntry.COLUMN_GATHERING_STATUS + " TEXT NOT NULL, " +
                GatheringEntry.COLUMN_GATHERING_START_DATE + " TEXT NOT NULL, " +
                GatheringEntry.COLUMN_GATHERING_END_DATE + " TEXT NOT NULL, " +
                GatheringEntry.COLUMN_GATHERING_CREATED_DATE + " TEXT NOT NULL, " +
                GatheringEntry.COLUMN_GATHERING_TOPIC + " TEXT NOT NULL, " +
                GatheringEntry.COLUMN_GATHERING_TYPE + " TEXT NOT NULL, " +
                GatheringEntry.COLUMN_GATHERING_LOCATION_COUNTRY + " TEXT NOT NULL, " +
                GatheringEntry.COLUMN_GATHERING_LOCATION_LOCALITY + " TEXT NOT NULL, " +
                GatheringEntry.COLUMN_GATHERING_LOCATION_NAME + " TEXT NOT NULL, " +
                GatheringEntry.COLUMN_GATHERING_LOCATION_POSTAL + " TEXT NOT NULL, " +
                GatheringEntry.COLUMN_GATHERING_LOCATION_PROV + " TEXT NOT NULL, " +
                GatheringEntry.COLUMN_GATHERING_LOCATION_NOTES + " TEXT NOT NULL, " +
                GatheringEntry.COLUMN_GATHERING_BANNER_URL + " TEXT, " +
                GatheringEntry.COLUMN_GATHERING_OWNER_ID + " TEXT NOT NULL, " +
                GatheringEntry.COLUMN_GATHERING_OWNER_USERNAME + " TEXT NOT NULL" + " );";

        // Create the favorites table to store favs for viewing offline
        final String SQL_CREATE_FAVORITES_TABLE = "CREATE TABLE " + FavoriteEntry.TABLE_NAME + " (" +
                FavoriteEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FavoriteEntry.COLUMN_GATHERING_ID + " TEXT NOT NULL, " +
                FavoriteEntry.COLUMN_GATHERING_NAME + " TEXT NOT NULL, " +
                FavoriteEntry.COLUMN_GATHERING_ACCESS + " TEXT NOT NULL, " +
                FavoriteEntry.COLUMN_GATHERING_DESC + " TEXT NOT NULL, " +
                FavoriteEntry.COLUMN_GATHERING_STATUS + " TEXT NOT NULL, " +
                FavoriteEntry.COLUMN_GATHERING_START_DATE + " TEXT NOT NULL, " +
                FavoriteEntry.COLUMN_GATHERING_END_DATE + " TEXT NOT NULL, " +
                FavoriteEntry.COLUMN_GATHERING_CREATED_DATE + " TEXT NOT NULL, " +
                FavoriteEntry.COLUMN_GATHERING_TOPIC + " TEXT NOT NULL, " +
                FavoriteEntry.COLUMN_GATHERING_TYPE + " TEXT NOT NULL, " +
                FavoriteEntry.COLUMN_GATHERING_LOCATION_COUNTRY + " TEXT NOT NULL, " +
                FavoriteEntry.COLUMN_GATHERING_LOCATION_LOCALITY + " TEXT NOT NULL, " +
                FavoriteEntry.COLUMN_GATHERING_LOCATION_NAME + " TEXT NOT NULL, " +
                FavoriteEntry.COLUMN_GATHERING_LOCATION_POSTAL + " TEXT NOT NULL, " +
                FavoriteEntry.COLUMN_GATHERING_LOCATION_PROV + " TEXT NOT NULL, " +
                FavoriteEntry.COLUMN_GATHERING_LOCATION_NOTES + " TEXT NOT NULL, " +
                FavoriteEntry.COLUMN_GATHERING_BANNER_URL + " TEXT, " +
                FavoriteEntry.COLUMN_GATHERING_OWNER_ID + " TEXT NOT NULL, " +
                FavoriteEntry.COLUMN_GATHERING_OWNER_USERNAME + " TEXT NOT NULL" + " );";


        // Create the topics table
        final String SQL_CREATE_TOPICS_TABLE = "CREATE TABLE " + TopicEntry.TABLE_NAME + " (" +
                TopicEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                TopicEntry.COLUMN_TOPIC_NAME + " TEXT NOT NULL" + " );";

        // Create the topics table
        final String SQL_CREATE_TYPES_TABLE = "CREATE TABLE " + TypeEntry.TABLE_NAME + " (" +
                TypeEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                TypeEntry.COLUMN_TYPE_NAME + " TEXT NOT NULL" + " );";

        // Create a table to hold the places data
        final String SQL_CREATE_PLACES_TABLE = "CREATE TABLE " + PlaceEntry.TABLE_NAME + " (" +
                PlaceEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                PlaceEntry.COLUMN_PLACE_ID + " TEXT NOT NULL, " +
                "UNIQUE (" + PlaceEntry.COLUMN_PLACE_ID + ") ON CONFLICT REPLACE" +
                "); ";


        db.execSQL(SQL_CREATE_TOPICS_TABLE);
        db.execSQL(SQL_CREATE_TYPES_TABLE);
        db.execSQL(SQL_CREATE_GATHERING_TABLE);
        db.execSQL(SQL_CREATE_PLACES_TABLE);
        db.execSQL(SQL_CREATE_FAVORITES_TABLE);


    }

    /**
     * Called when the database needs to be upgraded. The implementation
     * should use this method to drop tables, add tables, or do anything else it
     * needs to upgrade to the new schema version.
     *
     * @param db         The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + GatheringEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TopicEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TypeEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + PlaceEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + FavoriteEntry.TABLE_NAME);


        onCreate(db);
    }
}
