package com.programming.kantech.mygathering.data.model.mongo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by patrick keogh on 2017-05-19.
 */

public class CropArea implements Serializable
{

    @SerializedName("position")
    @Expose
    private List<Integer> position = null;
    @SerializedName("size")
    @Expose
    private List<Integer> size = null;
    private final static long serialVersionUID = -4096036456761427014L;

    /**
     * No args constructor for use in serialization
     *
     */
    public CropArea() {
    }

    /**
     *
     * @param position
     * @param size
     */
    public CropArea(List<Integer> position, List<Integer> size) {
        super();
        this.position = position;
        this.size = size;
    }

    public List<Integer> getPosition() {
        return position;
    }

    public void setPosition(List<Integer> position) {
        this.position = position;
    }

    public List<Integer> getSize() {
        return size;
    }

    public void setSize(List<Integer> size) {
        this.size = size;
    }

}
