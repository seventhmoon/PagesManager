package com.androidfung.facebook.graph.model.response.page;

import com.google.gson.annotations.SerializedName;

/**
 * Created by fung on 10/14/2016.
 * Class not in use
 */
public class PageAccessTokenResponse {
    @SerializedName("access_token")
    private String accessToken;

    private String id;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
