package com.androidfung.facebook.graph.model.response.post;

import com.androidfung.facebook.graph.model.Insight;
import com.androidfung.facebook.graph.model.Paging;
import com.androidfung.facebook.graph.model.response.Response;

import java.util.List;

/**
 * @Deprcated please use Response&lt;Insight&gt; instead
 * Created by fung on 10/26/2016.
 */
@Deprecated
public class InsightsResponse {

    private List<Insight> data;
    private Paging paging;
    private Error error;

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public List<Insight> getData() {
        return data;
    }

    public void setData(List<Insight> data) {
        this.data = data;
    }

    public Paging getPaging() {
        return paging;
    }

    public void setPaging(Paging paging) {
        this.paging = paging;
    }


}
