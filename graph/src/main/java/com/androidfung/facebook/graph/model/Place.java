package com.androidfung.facebook.graph.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by fung on 10/14/2016.
 * https://developers.facebook.com/docs/graph-api/reference/place
 */
// TODO: 10/14/2016 implement all attribute
public class Place {
    private String id;
    private Location location;
    private String name;
    @SerializedName("overall_rating")
    private float overallRating;
}
