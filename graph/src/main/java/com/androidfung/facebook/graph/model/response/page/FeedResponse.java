package com.androidfung.facebook.graph.model.response.page;

import com.androidfung.facebook.graph.model.Paging;
import com.androidfung.facebook.graph.model.Post;
import com.androidfung.facebook.graph.model.Error;
import com.androidfung.facebook.graph.model.response.Response;

import java.util.List;

/**
 * @Deprcated please use Response&lt;Post&gt; instead
 * Created by fung on 10/14/2016.
 */
@Deprecated
public class FeedResponse {

    private Error error;



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

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

}
