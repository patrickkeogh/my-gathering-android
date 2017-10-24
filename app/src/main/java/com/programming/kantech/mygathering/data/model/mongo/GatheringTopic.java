package com.programming.kantech.mygathering.data.model.mongo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by patrick keogh on 2017-05-19.
 */

public class GatheringTopic implements Serializable{

    @SerializedName("_id")
    @Expose
    private String id;
    private final static long serialVersionUID = -4464116427153087644L;

    /**
     * No args constructor for use in serialization
     *
     */
    public GatheringTopic() {
    }

    /**
     *
     * @param id
     */
    public GatheringTopic(String id) {
        super();
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "GatheringTopic{" +
                "id='" + id + '\'' +
                '}';
    }
}
