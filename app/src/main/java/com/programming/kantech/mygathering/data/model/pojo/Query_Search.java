package com.programming.kantech.mygathering.data.model.pojo;

/**
 * Created by patrick keogh on 2017-06-04.
 */

import java.io.Serializable;
import java.util.List;

public class Query_Search implements Serializable {

    private List<Double> coordinates = null;
    private float distance = 0;
    private String start_date;
    private String end_date;
    private String topic;
    private String type;

    private final static long serialVersionUID = -9177985143038762336L;


    /**
     * No args constructor for use in serialization
     */
    public Query_Search() {

    }

    public Query_Search(List<Double> coordinates, float distance, String start_date, String topic, String type) {
        this.coordinates = coordinates;
        this.distance = distance;
        this.start_date = start_date;
        this.topic = topic;
        this.type = type;
    }

    public List<Double> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<Double> coordinates) {
        this.coordinates = coordinates;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
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

    @Override
    public String toString() {
        return "Query_Search{" +
                "coordinates=" + coordinates +
                ", distance=" + distance +
                ", start_date='" + start_date + '\'' +
                ", end_date='" + end_date + '\'' +
                ", topic='" + topic + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
