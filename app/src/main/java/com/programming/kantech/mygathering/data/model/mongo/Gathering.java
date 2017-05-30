package com.programming.kantech.mygathering.data.model.mongo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by patrick keogh on 2017-05-19.
 */

public class Gathering implements Serializable
{

    @SerializedName("_id")
    @Expose
    private String gathering_id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("gathering_start_date_time")
    @Expose
    private String gatheringStartDateTime;
    @SerializedName("gathering_end_date_time")
    @Expose
    private String gatheringEndDateTime;
    @SerializedName("directions")
    @Expose
    private String directions;
    @SerializedName("notes")
    @Expose
    private String notes;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("__v")
    @Expose
    private Integer v;
    @SerializedName("banner")
    @Expose
    private Banner banner;
    @SerializedName("owner")
    @Expose
    private Owner owner;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("access")
    @Expose
    private String access;
    @SerializedName("location")
    @Expose
    private Location location;
    @SerializedName("topic")
    @Expose
    private List<GatheringTopic> topic = null;
    @SerializedName("type")
    @Expose
    private List<GatheringType> type = null;
    private final static long serialVersionUID = -9177985143034162336L;

    /**
     * No args constructor for use in serialization
     *
     */
    public Gathering() {
    }

    /**
     *
     * @param topic
     * @param location
     * @param status
     * @param gatheringStartDateTime
     * @param type
     * @param access
     * @param id
     * @param directions
     * @param v
     * @param description
     * @param gatheringEndDateTime
     * @param createdAt
     * @param name
     * @param owner
     * @param notes
     * @param banner
     */
    public Gathering(String id, String name, String description, String gatheringStartDateTime, String gatheringEndDateTime, String directions, String notes, String status, Integer v, Banner banner, Owner owner, String createdAt, String access, Location location, List<GatheringTopic> topic, List<GatheringType> type) {
        super();
        this.gathering_id = id;
        this.name = name;
        this.description = description;
        this.gatheringStartDateTime = gatheringStartDateTime;
        this.gatheringEndDateTime = gatheringEndDateTime;
        this.directions = directions;
        this.notes = notes;
        this.status = status;
        this.v = v;
        this.banner = banner;
        this.owner = owner;
        this.createdAt = createdAt;
        this.access = access;
        this.location = location;
        this.topic = topic;
        this.type = type;
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

    public String getGatheringStartDateTime() {
        return gatheringStartDateTime;
    }

    public void setGatheringStartDateTime(String gatheringStartDateTime) {
        this.gatheringStartDateTime = gatheringStartDateTime;
    }

    public String getGatheringEndDateTime() {
        return gatheringEndDateTime;
    }

    public void setGatheringEndDateTime(String gatheringEndDateTime) {
        this.gatheringEndDateTime = gatheringEndDateTime;
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

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }

    public Banner getBanner() {
        return banner;
    }

    public void setBanner(Banner banner) {
        this.banner = banner;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public List<GatheringTopic> getTopic() {
        return topic;
    }

    public void setTopic(List<GatheringTopic> topic) {
        this.topic = topic;
    }

    public List<GatheringType> getType() {
        return type;
    }

    public void setType(List<GatheringType> type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Gathering{" +
                "gathering_id='" + gathering_id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", gatheringStartDateTime='" + gatheringStartDateTime + '\'' +
                ", gatheringEndDateTime='" + gatheringEndDateTime + '\'' +
                ", directions='" + directions + '\'' +
                ", notes='" + notes + '\'' +
                ", status='" + status + '\'' +
                ", access='" + access + '\'' +
                '}';
    }
}