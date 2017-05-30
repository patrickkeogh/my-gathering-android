package com.programming.kantech.mygathering.data.model.mongo;

/**
 * Created by patrick keogh on 2017-04-13.
 * Pojo returned from server after registration
 */

public class Result_Register {

    private String status;
    private boolean success = false;

    public Result_Register(String status, boolean success) {
        this.status = status;
        this.success = success;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
