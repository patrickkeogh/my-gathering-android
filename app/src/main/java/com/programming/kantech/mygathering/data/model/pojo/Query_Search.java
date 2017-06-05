package com.programming.kantech.mygathering.data.model.pojo;

/**
 * Created by patrick keogh on 2017-06-04.
 */

import java.util.List;

public class Query_Search {

    private List<Double> coordinates = null;
    private float distance = 0;
    private String start_date;
    private String end_date;


    /**
     * No args constructor for use in serialization
     */
    public Query_Search() {

    }

    public Query_Search(List<Double> coordinates, float distance, String start_date) {
        this.coordinates = coordinates;
        this.distance = distance;
        this.start_date = start_date;
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
}
