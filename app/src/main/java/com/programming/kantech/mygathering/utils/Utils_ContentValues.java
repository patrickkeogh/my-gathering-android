package com.programming.kantech.mygathering.utils;

import android.content.ContentValues;
import android.util.Log;

import com.programming.kantech.mygathering.data.model.mongo.GatheringTopic;
import com.programming.kantech.mygathering.data.model.mongo.GatheringType;
import com.programming.kantech.mygathering.data.model.pojo.Gathering_Pojo;

import com.programming.kantech.mygathering.provider.Contract_MyGathering.GatheringEntry;
import com.programming.kantech.mygathering.provider.Contract_MyGathering.TopicEntry;
import com.programming.kantech.mygathering.provider.Contract_MyGathering.TypeEntry;

/**
 * Created by patrick keogh on 2017-05-25.
 */

public class Utils_ContentValues {

    /**
     * Extract the values from a gathering type to be used with a database.
     *
     * @param topic The gathering topic object to be extracted
     * @return result ContentValues instance with the value of the topic
     */
    public static ContentValues extractGatheringTopicValues(GatheringTopic topic) {
        ContentValues result = new ContentValues();
        result.put(TopicEntry.COLUMN_TOPIC_NAME, topic.getId());
        return result;
    }

    /**
     * Extract the values from a gathering type to be used with a database.
     *
     * @param type The gathering type object to be extracted
     * @return result ContentValues instance with the value of the topic
     */
    public static ContentValues extractGatheringTypeValues(GatheringType type) {
        ContentValues result = new ContentValues();
        result.put(TypeEntry.COLUMN_TYPE_NAME, type.getId());
        return result;
    }

    /**
     * Extract the values from a gathering to be used with a database.
     *
     * @param gathering The gathering object to be extracted
     * @return result ContentValues instance with the value of the topic
     */
    public static ContentValues extractGatheringValues(Gathering_Pojo gathering) {
        ContentValues result = new ContentValues();
        //result.put(GatheringEntry._ID, gathering.getId());
        result.put(GatheringEntry.COLUMN_GATHERING_ID, gathering.getGathering_id());
        result.put(GatheringEntry.COLUMN_GATHERING_ACCESS, gathering.getAccess());
        result.put(GatheringEntry.COLUMN_GATHERING_DESC, gathering.getDescription());
        result.put(GatheringEntry.COLUMN_GATHERING_NAME, gathering.getName());
        result.put(GatheringEntry.COLUMN_GATHERING_START_DATE, gathering.getStart_date());
        result.put(GatheringEntry.COLUMN_GATHERING_END_DATE, gathering.getEnd_date());
        result.put(GatheringEntry.COLUMN_GATHERING_STATUS, gathering.getStatus());

        result.put(GatheringEntry.COLUMN_GATHERING_TOPIC, gathering.getTopic());
        result.put(GatheringEntry.COLUMN_GATHERING_TYPE, gathering.getType());
        result.put(GatheringEntry.COLUMN_GATHERING_BANNER_URL,gathering.getBanner_url());

        result.put(GatheringEntry.COLUMN_GATHERING_LOCATION_NAME, gathering.getLocation_name());
        result.put(GatheringEntry.COLUMN_GATHERING_LOCATION_COUNTRY, gathering.getLocation_country());
        result.put(GatheringEntry.COLUMN_GATHERING_LOCATION_LOCALITY, gathering.getLocation_city());
        result.put(GatheringEntry.COLUMN_GATHERING_LOCATION_PROV, gathering.getLocation_prov());
        result.put(GatheringEntry.COLUMN_GATHERING_LOCATION_POSTAL, gathering.getLocation_postal());
        result.put(GatheringEntry.COLUMN_GATHERING_LOCATION_NOTES, gathering.getLocation_notes());

        result.put(GatheringEntry.COLUMN_GATHERING_OWNER_ID, gathering.getOwner_id());
        result.put(GatheringEntry.COLUMN_GATHERING_OWNER_USERNAME, gathering.getOwner_username());

        result.put(GatheringEntry.COLUMN_GATHERING_CREATED_DATE, gathering.getCreatedAt_date());

        return result;
    }



    /**
     * Ensure this class is only used as a utility.
     */
    private Utils_ContentValues() {
        throw new AssertionError();
    }
}
