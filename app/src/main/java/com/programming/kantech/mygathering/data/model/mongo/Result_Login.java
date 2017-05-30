package com.programming.kantech.mygathering.data.model.mongo;

/**
 * Created by patrick keogh on 2017-04-08.
 */

public class Result_Login {

    private String _id;
    private String status;
    private boolean success = false;
    private String token;
    private String name;

    public Result_Login() {
        super();
    }

    public Result_Login(boolean success, String _id, String status, String token, String name) {
        super();
        this.success = success;
        this._id = _id;
        this.status = status;
        this.token = token;
        this.name = name;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Result_Login{" +
                "_id='" + _id + '\'' +
                ", status='" + status + '\'' +
                ", success=" + success +
                ", token='" + token + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}

