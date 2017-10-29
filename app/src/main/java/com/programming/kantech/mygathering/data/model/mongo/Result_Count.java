package com.programming.kantech.mygathering.data.model.mongo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by patri on 2017-10-29.
 */

public class Result_Count {

    @SerializedName("recCount")
    @Expose
    private Integer recCount;


    public Integer getRecCount() {
        return recCount;
    }

    public void setRecCount(Integer recCount) {
        this.recCount = recCount;
    }

}
