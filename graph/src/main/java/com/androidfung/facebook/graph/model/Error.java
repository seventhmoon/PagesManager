package com.androidfung.facebook.graph.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by fung on 11/1/2016.
 */

public class Error {


//    "error":
//
//    {
//        "message":"Unknown path components: /11/posts",
//            "type":"OAuthException",
//            "code":2500,
//            "fbtrace_id":"GtTjzihmxKb"
//
//
//    }

    private String message;
    private String type;
    private int code;
    @SerializedName("fbtrace_id")
    private String fbtraceId;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getFbtraceId() {
        return fbtraceId;
    }

    public void setFbtraceId(String fbtraceId) {
        this.fbtraceId = fbtraceId;
    }
}