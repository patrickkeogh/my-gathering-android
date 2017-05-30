package com.programming.kantech.mygathering.data.model.mongo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by patrick keogh on 2017-05-19.
 */

public class Result_GetGatherings {

    @SerializedName("results")
    private List<Gathering> results;

    public List<Gathering> getResults() {
        return results;    }

    public void setResults(List<Gathering> results) {
        this.results = results;
    }
}
