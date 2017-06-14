package com.programming.kantech.mygathering.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.programming.kantech.mygathering.utils.Constants;

import static com.programming.kantech.mygathering.provider.Contract_MyGathering.TopicEntry;
import static com.programming.kantech.mygathering.provider.Contract_MyGathering.TypeEntry;
import static com.programming.kantech.mygathering.provider.Contract_MyGathering.GatheringEntry;

/**
 * Created by patrick keogh on 2017-05-24.
 */

public class Provider_MyGathering extends ContentProvider {

    // Declare a static variable for the Uri matcher that you construct
    private static final UriMatcher sUriMatcher = buildUriMatcher();


    /**
     * Use DBHelper to manage database creation and version
     * management.
     */
    private DBHelper mOpenHelper;


    // Define final integer constants for the directory of tasks and a single item.
    // It's convention to use 100, 200, 300, etc for directories,
    // and related ints (101, 102, ..) for items in that directory.
    public static final int TOPICS_ALL = 100;
    public static final int TOPICS_ID = 101;

    public static final int TYPES_ALL = 200;
    public static final int TYPES_ID = 201;

    public static final int GATHERINGS_ALL = 300;
    public static final int GATHERINGS_ID = 301;

    public static final int PLACES = 400;
    public static final int PLACE_WITH_ID = 401;

    private static final String sGatheringByIdSelection =
            Contract_MyGathering.GatheringEntry.TABLE_NAME + "." + GatheringEntry._ID + " = ? ";

    /**
     * Define a static buildUriMatcher method that associates URI's with their int match
     *
     * @return UriMatcher
     */
    public static UriMatcher buildUriMatcher() {

        // Initialize a UriMatcher with no matches by passing in NO_MATCH to the constructor
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        /*
          All paths added to the UriMatcher have a corresponding int.
          For each kind of uri you may want to access, add the corresponding match with addURI.
          The two calls below add matches for the task directory and a single item by ID.
         */
        uriMatcher.addURI(Constants.CONTENT_AUTHORITY, Contract_MyGathering.PATH_TOPIC, TOPICS_ALL);
        uriMatcher.addURI(Constants.CONTENT_AUTHORITY, Contract_MyGathering.PATH_TYPE, TYPES_ALL);
        uriMatcher.addURI(Constants.CONTENT_AUTHORITY, Contract_MyGathering.PATH_GATHERINGS, GATHERINGS_ALL);

        // replaces # with int id
        uriMatcher.addURI(Constants.CONTENT_AUTHORITY, Contract_MyGathering.PATH_TOPIC + "/#", TOPICS_ID);
        uriMatcher.addURI(Constants.CONTENT_AUTHORITY, Contract_MyGathering.PATH_TYPE + "/#", TYPES_ID);
        uriMatcher.addURI(Constants.CONTENT_AUTHORITY, Contract_MyGathering.PATH_GATHERINGS + "/#", GATHERINGS_ID);

        uriMatcher.addURI(Constants.CONTENT_AUTHORITY, Contract_MyGathering.PATH_PLACES, PLACES);
        uriMatcher.addURI(Constants.CONTENT_AUTHORITY, Contract_MyGathering.PATH_PLACES + "/#", PLACE_WITH_ID);

        return uriMatcher;
    }

    /* onCreate() is where you should initialize anything you’ll need to setup
    your underlying data source.
    In this case, we are using a SQLite database, so you’ll need to
    initialize a DbHelper to gain access to it.
     */
    @Override
    public boolean onCreate() {
        mOpenHelper = new DBHelper(getContext());
        return true;
    }

    private Cursor getGatheringById(Uri uri, String[] projection, String sortOrder) {

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        queryBuilder.setTables(GatheringEntry.TABLE_NAME);

        queryBuilder.appendWhere(GatheringEntry._ID + "="
                + ContentUris.parseId(uri));

        //String[] resultColumns = new String[]{GatheringEntry.COLUMN_GATHERING_NAME};


        return queryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                sGatheringByIdSelection,
                null,
                null,
                null,
                null
        );
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        // Get access to underlying database (read-only for query)
        final SQLiteDatabase db = mOpenHelper.getReadableDatabase();

        // Write URI match code and set a variable to return a Cursor
        int match = sUriMatcher.match(uri);
        Cursor retCursor;
        String id;

        // Query for the tasks directory and write a default case
        switch (match) {
            // Query for the tasks directory
            case PLACES:
                retCursor = db.query(Contract_MyGathering.PlaceEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case GATHERINGS_ALL:
                retCursor = db.query(GatheringEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case PLACE_WITH_ID:
                Log.i(Constants.TAG, "URI MATCH:PLACE_WITH_ID:" + uri);

                id = uri.getLastPathSegment();

                Log.i(Constants.TAG, "ID TO QUERY BY:" + id);

                retCursor = db.query(Contract_MyGathering.PlaceEntry.TABLE_NAME,
                        projection,
                        "_id=?",
                        new String[]{id},
                        null,
                        null,
                        sortOrder);

                break;
            case GATHERINGS_ID:
                Log.i(Constants.TAG, "URI MATCH:GATHERINGS_ID:" + uri);

                id = uri.getLastPathSegment();

                Log.i(Constants.TAG, "ID TO QUERY BY:" + id);




                retCursor = db.query(GatheringEntry.TABLE_NAME,
                        projection,
                        "_id=?",
                        new String[]{id},
                        null,
                        null,
                        sortOrder);


                //retCursor = getGatheringById(uri, projection, sortOrder);

                //Log.v(Constants.TAG, "Cursor Object in Query:" + DatabaseUtils.dumpCursorToString(retCursor));

                break;
            // Query for the topics directory
            case TOPICS_ALL:
                retCursor = db.query(TopicEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            // Default exception
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Set a notification URI on the Cursor and return that Cursor
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        // Return the desired Cursor
        return retCursor;
    }

    /**
     * @param uri    The content:// URI of the insertion request. This must not be {@code null}.
     * @param values A set of column_name/value pairs to add to the database.
     *               This must not be {@code null}.
     * @return The URI for the newly inserted item.
     */
    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        // Get access to the topic database (to write new data to)
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        // Write URI matching code to identify the match for the tasks directory
        int match = sUriMatcher.match(uri);
        Uri returnUri = null; // URI to be returned
        long id = 0;

        switch (match) {
            case PLACES:
                // Insert new values into the database
                id = db.insert(Contract_MyGathering.PlaceEntry.TABLE_NAME, null, values);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(Contract_MyGathering.PlaceEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            case TOPICS_ALL:
                //Log.i(Constants.TAG, "INSERT TOPICS_ALL called");
                id = db.insert(TopicEntry.TABLE_NAME, null, values);
                if ( id > 0 ) {
                    returnUri = ContentUris.withAppendedId(TopicEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            case TYPES_ALL:
                //Log.i(Constants.TAG, "INSERT TYPES_ALL called");
                // Insert new values into the database
                // Inserting values into tasks table
                id = db.insert(TypeEntry.TABLE_NAME, null, values);
                if ( id > 0 ) {
                    returnUri = ContentUris.withAppendedId(TypeEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            // Set the value for the returnedUri and write the default case for unknown URI's
            // Default case throws an UnsupportedOperationException
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Notify the resolver if the uri has been changed, and return the newly inserted URI
        getContext().getContentResolver().notifyChange(uri, null);

        // Return constructed uri (this points to the newly inserted row of data)
        return returnUri;
    }

    // Implement delete to delete a single row of data
    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {

        // Get access to the database and write URI matching code to recognize a single item
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);

        // Keep track of the number of deleted tasks
        int rowsDeleted = 0; // starts as 0
        String id;

        // Write the code to delete a single row of data
        // [Hint] Use selections to delete an item by its row ID
        switch (match) {
            // Handle the single item case, recognized by the ID included in the URI path
            case PLACE_WITH_ID:
                // Get the place ID from the URI path
                id = uri.getPathSegments().get(1);
                // Use selections/selectionArgs to filter for this ID
                rowsDeleted = db.delete(Contract_MyGathering.PlaceEntry.TABLE_NAME, "_id=?", new String[]{id});
                break;
            case GATHERINGS_ALL:
                Log.i(Constants.TAG, "Entered Delete: GATHERINGS ALL");
                rowsDeleted = db.delete(GatheringEntry.TABLE_NAME, selection,
                        selectionArgs);

                Log.i(Constants.TAG, "Rows Deleted:" + rowsDeleted);
                break;
            case TOPICS_ALL:
                rowsDeleted = db.delete(TopicEntry.TABLE_NAME, selection,
                        selectionArgs);
                break;
            case TYPES_ALL:
                rowsDeleted = db.delete(TypeEntry.TABLE_NAME, selection,
                        selectionArgs);
                break;
            case TOPICS_ID:

                // Get the task ID from the URI path
                id = uri.getPathSegments().get(1);
                // Use selections/selectionArgs to filter for this ID
                rowsDeleted = db.delete(TopicEntry.TABLE_NAME, "_id=?", new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown Delete uri: " + uri);
        }

        // Notify the resolver of a change and return the number of items deleted
        if (rowsDeleted != 0) {
            // A task was deleted, set notification
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of tasks deleted
        return rowsDeleted;
    }

    @NonNull
    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {

        // Get access to underlying database
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        // Keep track of the number of updated places
        int placesUpdated;

        switch (match) {
            case PLACE_WITH_ID:
                // Get the place ID from the URI path
                String id = uri.getPathSegments().get(1);
                // Use selections/selectionArgs to filter for this ID
                placesUpdated = db.update(Contract_MyGathering.PlaceEntry.TABLE_NAME, values, "_id=?", new String[]{id});
                break;
            // Default exception
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Notify the resolver of a change and return the number of items updated
        if (placesUpdated != 0) {
            // A place (or more) was updated, set notification
            getContext().getContentResolver().notifyChange(uri, null);
        }
        // Return the number of places deleted
        return placesUpdated;
    }

    @NonNull
    @Override
    public String getType(@NonNull Uri uri) {

        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handles requests to insert a set of new rows. In Sthis app, we are only going to be
     * inserting multiple rows of data at a time. There is no use case
     * for inserting a single row of data into our ContentProvider, and so we are only going to
     * implement bulkInsert. In a normal ContentProvider's implementation, you will probably want
     * to provide proper functionality for the insert method as well.
     *
     * @param uri    The content:// URI of the insertion request.
     * @param values An array of sets of column_name/value pairs to add to the database.
     *               This must not be {@code null}.
     *
     * @return The number of values that were inserted.
     */
    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        switch (sUriMatcher.match(uri)) {

            case GATHERINGS_ALL:
                db.beginTransaction();
                int rowsInserted = 0;
                try {
                    for (ContentValues value : values) {


                        long _id = db.insert(GatheringEntry.TABLE_NAME, null, value);

                        //Log.i(Constants.TAG, "_id returned from insert:" + _id);

                        if (_id != -1) {
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }

                if (rowsInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }

                return rowsInserted;

            default:
                return super.bulkInsert(uri, values);
        }
    }
}
