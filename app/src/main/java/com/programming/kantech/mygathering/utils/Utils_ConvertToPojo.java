package com.programming.kantech.mygathering.utils;

import com.programming.kantech.mygathering.data.model.mongo.Banner;
import com.programming.kantech.mygathering.data.model.mongo.Gathering;
import com.programming.kantech.mygathering.data.model.mongo.GatheringTopic;
import com.programming.kantech.mygathering.data.model.mongo.GatheringType;
import com.programming.kantech.mygathering.data.model.mongo.Location;
import com.programming.kantech.mygathering.data.model.mongo.Owner;
import com.programming.kantech.mygathering.data.model.pojo.Gathering_Pojo;

import java.util.List;

/**
 * Created by patrick keogh on 2017-05-29.
 * This utility class will convert mongo documents to java pojo's
 */

public class Utils_ConvertToPojo {


    public static Gathering_Pojo convertGatheringMongoToPojo(Gathering doc) {

        String gathering_id = doc.getGathering_id();
        String name = doc.getName();
        String description = doc.getDescription();
        String start_date = doc.getGatheringStartDateTime();
        String end_date = doc.getGatheringEndDateTime();
        String createdAt = doc.getCreatedAt();
        String directions = doc.getDirections();
        String notes = doc.getNotes();
        String status = doc.getStatus();
        String access = doc.getAccess();

        Banner banner = doc.getBanner();
        String banner_url;

        if(banner == null){
            banner_url = "null";
        }else{
            banner_url = banner.getUrl();
        }
        Location location = doc.getLocation();
        String location_name = location.getName();
        String location_city = location.getLocality();
        String location_prov = location.getStateProv();
        String location_postal = location.getPostalCode();
        String location_country = location.getCountry();
        String location_notes = location.getNotes();

        List<GatheringType> types = doc.getType();
        List<GatheringTopic> topics = doc.getTopic();

        String topic = topics.get(0).getId();
        String type = types.get(0).getId();

        Owner owner = doc.getOwner();
        String owner_id = owner.getOwnerId();
        String owner_username = owner.getUsername();



        Gathering_Pojo gathering = new Gathering_Pojo(gathering_id, name, description, start_date,
                end_date, directions, notes, status, access, banner_url, location_name, location_city,
                location_prov, location_postal, location_country, location_notes, topic, type, owner_id, owner_username, createdAt );


        return gathering;
    }

    /**
     * Ensure this class is only used as a utility.
     */
    private Utils_ConvertToPojo() {
        throw new AssertionError();
    }
}
