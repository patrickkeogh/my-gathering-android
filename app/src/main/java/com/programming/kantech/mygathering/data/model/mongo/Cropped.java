package com.programming.kantech.mygathering.data.model.mongo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.programming.kantech.mygathering.data.model.mongo.CropArea;

import java.io.Serializable;
import java.util.List;

/**
 * Created by patrick keogh on 2017-05-19.
 */

public class Cropped implements Serializable
{

    @SerializedName("originalImageSize")
    @Expose
    private List<Integer> originalImageSize = null;
    @SerializedName("cropArea")
    @Expose
    private CropArea cropArea;
    private final static long serialVersionUID = -4841107911954294296L;

    /**
     * No args constructor for use in serialization
     *
     */
    public Cropped() {
    }

    /**
     *
     * @param cropArea
     * @param originalImageSize
     */
    public Cropped(List<Integer> originalImageSize, CropArea cropArea) {
        super();
        this.originalImageSize = originalImageSize;
        this.cropArea = cropArea;
    }

    public List<Integer> getOriginalImageSize() {
        return originalImageSize;
    }

    public void setOriginalImageSize(List<Integer> originalImageSize) {
        this.originalImageSize = originalImageSize;
    }

    public CropArea getCropArea() {
        return cropArea;
    }

    public void setCropArea(CropArea cropArea) {
        this.cropArea = cropArea;
    }

}
