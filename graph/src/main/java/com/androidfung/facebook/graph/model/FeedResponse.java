package com.androidfung.facebook.graph.model;

import java.util.List;

/**
 * Created by fung on 10/14/2016.
 */

public class FeedResponse {

    private List<Post> data;
    private Paging paging;

    public List<Post> getData() {
        return data;
    }

    public void setData(List<Post> data) {
        this.data = data;
    }

    public Paging getPaging() {
        return paging;
    }

    public void setPaging(Paging paging) {
        this.paging = paging;
    }



}
