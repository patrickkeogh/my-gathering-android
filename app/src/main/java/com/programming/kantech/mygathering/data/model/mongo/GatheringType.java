package com.programming.kantech.mygathering.data.model.mongo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by patrick keogh on 2017-04-08.
 */

public class GatheringType implements Serializable {

    @SerializedName("_id")
    @Expose
    private String id;
    private final static long serialVersionUID = -7209036184836874953L;

    /**
     * No args constructor for use in serialization
     *
     */
    public GatheringType() {
    }

    /**
     *
     * @param id
     */
    public GatheringType(String id) {
        super();
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
