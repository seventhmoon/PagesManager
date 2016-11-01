package com.androidfung.facebook.graph.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by fung on 10/25/2016.
 */

public class Account {
    // TODO: 10/25/2016 full implementation of all attribute
    private String name;
    private String id;
    @SerializedName("access_token")
    private String accessToken;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
