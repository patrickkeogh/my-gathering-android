package com.programming.kantech.mygathering.data.model.mongo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by patrick keogh on 2017-05-19.
 */

public class Location_Coords implements Serializable
{

    @SerializedName("coordinates")
    @Expose
    private List<Double> coordinates = null;
    @SerializedName("type")
    @Expose
    private String type;
    private final static long serialVersionUID = -6221790588013260467L;

    /**
     * No args constructor for use in serialization
     *
     */
    public Location_Coords() {
    }

    /**
     *
     * @param type
     * @param coordinates
     */
    public Location_Coords(List<Double> coordinates, String type) {
        super();
        this.coordinates = coordinates;
        this.type = type;
    }

    public List<Double> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<Double> coordinates) {
        this.coordinates = coordinates;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
