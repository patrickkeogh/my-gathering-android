package com.programming.kantech.mygathering.data.model.pojo;


import java.io.Serializable;

/**
 * Created by patrick keogh on 2017-05-19.
 */

public class Gathering_Pojo implements Serializable {

    private long id;
    private String gathering_id;
    private String name;
    private String description;
    private String start_date;
    private String end_date;
    private String directions;
    private String notes;
    private String status;
    private String banner_url;
    private String access;
    private String location_name;
    private String location_city;
    private String location_prov;
    private String location_postal;
    private String location_country;
    private String location_notes;
    private String topic;
    private String type;

    private String owner_id;
    private String owner_username;

    private final static long serialVersionUID = -9177985143034162336L;

    /**
     * No args constructor for use in serialization
     */
    public Gathering_Pojo() {
    }


    /**
     * @param gathering_id
     * @param name
     * @param description
     * @param start_date
     * @param end_date
     * @param directions
     * @param notes
     * @param status
     * @param access
     * @param banner_url
     * @param location_name
     * @param location_city
     * @param location_prov
     * @param location_postal
     * @param location_country
     * @param location_notes
     * @param topic
     * @param type
     * @param owner_id
     * @param owner_username
     *
     */

    public Gathering_Pojo(String gathering_id, String name, String description, String start_date,
                          String end_date, String directions, String notes, String status,
                          String access, String banner_url, String location_name, String location_city,
                          String location_prov, String location_postal, String location_country,
                          String location_notes, String topic, String type, String owner_id, String owner_username) {
        this.id = id;
        this.gathering_id = gathering_id;
        this.name = name;
        this.description = description;
        this.start_date = start_date;
        this.end_date = end_date;
        this.directions = directions;
        this.notes = notes;
        this.status = status;
        this.banner_url = banner_url;
        this.access = access;
        this.location_name = location_name;
        this.location_city = location_city;
        this.location_prov = location_prov;
        this.location_postal = location_postal;
        this.location_country = location_country;
        this.location_notes = location_notes;
        this.topic = topic;
        this.type = type;
        this.owner_id = owner_id;
        this.owner_username = owner_username;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getGathering_id() {
        return gathering_id;
    }

    public void setGathering_id(String gathering_id) {
        this.gathering_id = gathering_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getDirections() {
        return directions;
    }

    public void setDirections(String directions) {
        this.directions = directions;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBanner_url() {
        return banner_url;
    }

    public void setBanner_url(String banner_url) {
        this.banner_url = banner_url;
    }

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    public String getLocation_name() {
        return location_name;
    }

    public void setLocation_name(String location_name) {
        this.location_name = location_name;
    }

    public String getLocation_city() {
        return location_city;
    }

    public void setLocation_city(String location_city) {
        this.location_city = location_city;
    }

    public String getLocation_prov() {
        return location_prov;
    }

    public void setLocation_prov(String location_prov) {
        this.location_prov = location_prov;
    }

    public String getLocation_postal() {
        return location_postal;
    }

    public void setLocation_postal(String location_postal) {
        this.location_postal = location_postal;
    }

    public String getLocation_country() {
        return location_country;
    }

    public void setLocation_country(String location_country) {
        this.location_country = location_country;
    }

    public String getLocation_notes() {
        return location_notes;
    }

    public void setLocation_notes(String location_notes) {
        this.location_notes = location_notes;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(String owner_id) {
        this.owner_id = owner_id;
    }

    public String getOwner_username() {
        return owner_username;
    }

    public void setOwner_username(String owner_username) {
        this.owner_username = owner_username;
    }

    @Override
    public String toString() {
        return "Gathering_Pojo{" +
                "id=" + id +
                ", gathering_id='" + gathering_id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
