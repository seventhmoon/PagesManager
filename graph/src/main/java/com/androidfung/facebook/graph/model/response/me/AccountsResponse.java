package com.androidfung.facebook.graph.model.response.me;


import com.androidfung.facebook.graph.model.Account;
import com.androidfung.facebook.graph.model.Paging;

import java.util.List;
import java.util.TreeMap;

/**
 * @Deprcated please use Response&lt;Account&gt; instead
 * Created by fung on 10/19/2016.
 */
@Deprecated
public class AccountsResponse {
    private List<Account> data;
    private Paging paging;

    public List<Account> getData() {
        return data;
    }

    public void setData(List<Account> data) {
        this.data = data;
    }

    public Paging getPaging() {
        return paging;
    }

    public void setPaging(Paging paging) {
        this.paging = paging;
    }

}
