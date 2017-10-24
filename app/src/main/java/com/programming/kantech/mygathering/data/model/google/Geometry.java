package com.programming.kantech.mygathering.data.model.google;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by patri on 2017-10-21.
 */

public class Geometry {

    @SerializedName("location")
    @Expose
    private GeometryLocation location;
    /**
     * No args constructor for use in serialization
     *
     */
    public Geometry() {
    }

    /**
     *
     * @param location
     */
    public Geometry(GeometryLocation location) {
        super();
        this.location = location;
    }

    public GeometryLocation getLocation() {
        return location;
    }

    public void setLocation(GeometryLocation location) {
        this.location = location;
    }


}
