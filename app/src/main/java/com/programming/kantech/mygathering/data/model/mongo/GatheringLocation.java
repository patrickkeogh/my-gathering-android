package com.programming.kantech.mygathering.data.model.mongo;

import java.io.Serializable;

/**
 * Created by patrick keogh on 2017-05-19.
 */

public class GatheringLocation{

    private String locality;

    public GatheringLocation(String locality) {
        this.locality = locality;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }
}
