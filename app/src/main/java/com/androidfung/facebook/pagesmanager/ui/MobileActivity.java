package com.androidfung.facebook.pagesmanager.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.androidfung.facebook.pagesmanager.R;
import com.androidfung.facebook.graph.model.response.me.AccountsResponse;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Deprecated
public class MobileActivity extends BaseActivity {

    private static final String TAG = MobileActivity.class.getSimpleName();
    private FloatingActionButton mFab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        if (AccessToken.getCurrentAccessToken() != null) {
            //user not logged in

            //show login Fragment
            hideFab();

        } else {
            //user logged in

            //get page id array

            //show post feed fragment
            showFab();
        }
    }


    private void getAvailablePagesAsync() {

        /* make the API call */
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/" + getString(R.string.facebook_page_id) + "?fields=access_token",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
            /* handle the result */
                        Log.d(TAG, response.toString());
//                        String rawResponse = response.getRawResponse();
                        String graphObject = response.getJSONObject().toString();
                        Gson gson = new GsonBuilder().create();
                        AccountsResponse tokenResponse = gson.fromJson(graphObject, AccountsResponse.class);


                        // TODO: 10/19/2016 1. Update ActionBar Spinner; 2. Load item 0

                    }
                }
        ).executeAsync();

    }


    private void hideFab() {
        mFab.setVisibility(View.GONE);
    }

    private void showFab() {
        mFab.setVisibility(View.VISIBLE);
    }
}
