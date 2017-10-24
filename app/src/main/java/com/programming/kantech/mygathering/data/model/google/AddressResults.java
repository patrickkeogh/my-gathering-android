package com.programming.kantech.mygathering.data.model.google;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by patri on 2017-10-21.
 */

public class AddressResults {

    @SerializedName("result")
    @Expose
    private Result result;

    /**
     * No args constructor for use in serialization
     *
     */
    public AddressResults() {
    }

    /**
     *
     * @param result
     */
    public AddressResults(Result result) {
        super();
        this.result = result;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

}