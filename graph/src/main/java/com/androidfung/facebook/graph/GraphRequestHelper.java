package com.androidfung.facebook.graph;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.androidfung.facebook.graph.model.Account;
import com.androidfung.facebook.graph.model.response.page.FeedResponse;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.HttpMethod;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.TreeMap;

/**
 * Created by fung on 10/27/2016.
 */

public class GraphRequestHelper {





    public static GraphRequest getFeedGraphRequest(String pageId, GraphRequest.Callback callback) {

        return new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/" + pageId + "/feed?include_hidden=true&fields=message,created_time,is_hidden,insights.metric(post_impressions)",
                null,
                HttpMethod.GET,
                callback
        );
    }

    public static GraphRequest getPromotablePostsGraphRequest(String pageId, GraphRequest.Callback callback) {

        return new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/" + pageId + "/promotable_posts?is_published=false&include_hidden=true&fields=message,created_time,is_hidden,insights.metric(post_impressions)",
                null,
                HttpMethod.GET,
                callback
        );
    }

    public static GraphRequest getNewPostGraphRequest(AccessToken accessToken, Bundle params, String pageId, GraphRequest.Callback callback) {

        return new GraphRequest(

                accessToken,
                "/" + pageId + "/feed",
                params,
                HttpMethod.POST,
                callback
        );


    }

    public static GraphRequest getMeAccoountsGraphRequest(GraphRequest.Callback callback) {
        return new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/accounts",
                null,
                HttpMethod.GET,
                callback
        );
    }

}
