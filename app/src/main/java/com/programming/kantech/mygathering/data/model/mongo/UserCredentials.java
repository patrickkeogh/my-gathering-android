package com.programming.kantech.mygathering.data.model.mongo;

/**
 * Created by patrik keogh on 2017-04-09.
 */

public class UserCredentials {

    private String username;
    private String password;
    private String name;

    public UserCredentials() {
        super();
    }

    public UserCredentials(String username, String password, String name) {
        super();
        this.username = username;
        this.password = password;
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "UserCredentials{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
