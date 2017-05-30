package com.programming.kantech.mygathering.data.model.mongo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by patrick keogh on 2017-05-19.
 */

public class Owner implements Serializable
{

    @SerializedName("ownerId")
    @Expose
    private String ownerId;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("name")
    @Expose
    private String name;
    private final static long serialVersionUID = 5851912507069481926L;

    /**
     * No args constructor for use in serialization
     *
     */
    public Owner() {
    }

    /**
     *
     * @param username
     * @param ownerId
     * @param name
     */
    public Owner(String ownerId, String username, String name) {
        super();
        this.ownerId = ownerId;
        this.username = username;
        this.name = name;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
