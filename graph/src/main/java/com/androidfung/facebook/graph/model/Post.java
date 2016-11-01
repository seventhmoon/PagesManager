package com.androidfung.facebook.graph.model;


import com.androidfung.facebook.graph.model.response.Response;
import com.androidfung.facebook.graph.model.response.post.InsightsResponse;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by fung on 10/14/2016.
 * https://developers.facebook.com/docs/graph-api/reference/v2.8/post
 */
// TODO: 10/14/2016 full implementation of the attributes 
public class Post {


    public enum StatusType {
        MOBILE_STATUS_UPDATE, CREATED_NOTE, ADDED_PHOTOS, ADDED_VIDEO, SHARED_STORY, CREATED_GROUP, CREATED_EVENT, WALL_POST,
        APP_CREATED_STORY, PUBLISHED_STORY, TAGGED_IN_PHOTO, APPROVED_FRIEND
    }

    public enum Type {
        LINK, STATUS, PHOTO, VIDEO, OFFER
    }

//    private InsightsResponse insights;
    private Response<Insight> insights;

    private String id;
    @SerializedName("admin_creator")
    private Object[] adminCreator;
    private App application;
    @SerializedName("call_to_action")
    private Object callToAction;
    private String caption;
    @SerializedName("created_time")
    private Date createdTime;
    private String description;
    @SerializedName("feedtargeting")
    private Object feedTargeting;
    private Profile from;
    private String icon;
    @SerializedName("instagram_eligibility")
    private String instagramEligibility;


    @SerializedName("is_hidden")
    private boolean hidden;
    @SerializedName("is_instagram_eligible")
    private String instagramEligible;
    @SerializedName("is_published")
    private boolean published;
    private String link;
    private String message;
    @SerializedName("message_tags")
    private Object messageTags;
    private String name;
    @SerializedName("object_id")
    private String objectId;
    @SerializedName("parent_id")
    private String parentId;
    @SerializedName("permalink_url")
    private String permalinkUrl;
    private String picture;
    private Place place;
    private Object privacy;
    private Object[] properties;
    private Object shares;
    private String source;
    @SerializedName("status_type")
    private StatusType statusType;
    private String story;
    private Type type;
    @SerializedName("updated_time")
    private Date updatedTime;

    public Response<Insight> getInsights() {
        return insights;
    }

    public void setInsights(Response<Insight> insights) {
        this.insights = insights;
    }
//    public InsightsResponse getInsights() {
//        return insights;
//    }
//
//    public void setInsights(InsightsResponse insights) {
//        this.insights = insights;
//    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
    }

    public void setMessage(String message) {
        this.message = message;

    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }


    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }
}
