package com.programming.kantech.mygathering.data.model.mongo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by patrick keogh on 2017-05-23.
 */

public class Result_Logout {

    @SerializedName("status")
    private String status;

    public Result_Logout(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
