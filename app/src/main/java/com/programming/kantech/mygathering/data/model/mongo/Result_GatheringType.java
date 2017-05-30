package com.programming.kantech.mygathering.data.model.mongo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by patrick keogh on 2017-04-08.
 */

public class Result_GatheringType {

    @SerializedName("results")
    private List<GatheringType> results;

    public List<GatheringType> getResults() {
        return results;    }

    public void setResults(List<GatheringType> results) {
        this.results = results;
    }






}
