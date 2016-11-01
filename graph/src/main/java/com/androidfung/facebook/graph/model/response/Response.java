package com.androidfung.facebook.graph.model.response;

import com.androidfung.facebook.graph.model.Error;
import com.androidfung.facebook.graph.model.Paging;
import com.androidfung.facebook.graph.model.Post;

import java.util.List;

/**
 * Created by fung on 11/1/2016.
 */

public class Response<T> {

    private Error error;
    private List<T> data;
    private Paging paging;


    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public Paging getPaging() {
        return paging;
    }

    public void setPaging(Paging paging) {
        this.paging = paging;
    }
}
