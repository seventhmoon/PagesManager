package com.androidfung.facebook.pagesmanager.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.androidfung.facebook.graph.model.Account;
import com.androidfung.facebook.pagesmanager.R;
import com.androudfung.facebook.graph.model.response.me.AccountsResponse;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, PageFeedFragment.OnFragmentInteractionListener, NavigationCallback, NewPostFragment.OnFragmentInteractionListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private CallbackManager mCallbackManager;
    private AccessTokenTracker mAccessTokenTracker;

    private String mPageId;
//    private String mPageAccessToken;

    private DrawerLayout mDrawer;
    private FloatingActionButton mFab;
    private NavigationView mNavigationView;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mCallbackManager = CallbackManager.Factory.create();
        mPageId = getString(R.string.facebook_page_id);

        setContentView(R.layout.activity_main);

        initViews();


        if (AccessToken.getCurrentAccessToken() == null) {
            //user not logged in

            //show login Fragment
            hideFab();

        } else {
            //user logged in

            //get page id array
            getAvailablePagesAsync();

            //show post feed fragment
            showFab();
        }
    }


    private void initViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setOnClickListener(view -> showNewPostDialog() );

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();


        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);

        mRecyclerView = (RecyclerView) mNavigationView.findViewById(R.id.recyclerview_pages);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(false);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        View headerLayout = mNavigationView.getHeaderView(0);

        LoginButton loginButton = (LoginButton) headerLayout.findViewById(R.id.login_button);

        loginButton.setPublishPermissions("manage_pages", "publish_pages", "publish_actions");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(getApplicationContext(), loginResult.toString(), Toast.LENGTH_SHORT).show();


                updateDrawerAsync();
                mDrawer.closeDrawers();
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), "onSuccess", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateDrawerAsync() {

        getAvailablePagesAsync();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

//        if (id == R.id.nav_camera) {
//            // Handle the camera action
//        } else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//            displayPostFeed();
//        }


        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

//    private void getPageAccessToken(){
//
//
//        /* make the API call */
//        new GraphRequest(
//                AccessToken.getCurrentAccessToken(),
//                "/" + getString(R.string.facebook_page_id) + "?fields=access_token",
//                null,
//                HttpMethod.GET,
//                new GraphRequest.Callback() {
//                    public void onCompleted(GraphResponse response) {
//            /* handle the result */
//                        Log.d(TAG, response.toString());
////                        String rawResponse = response.getRawResponse();
//                        String graphObject = response.getJSONObject().toString();
//                        Gson gson = new GsonBuilder().create();
//                        PageAccessTokenResponse tokenResponse = gson.fromJson(graphObject, PageAccessTokenResponse.class);
//
//                        mPageAccessToken = tokenResponse.getAccessToken();
//                        Log.d(TAG, mPageAccessToken);
//                    }
//                }
//        ).executeAsync();
//    }


    private void createNewPostAsync(String pageId, String content) {

        Bundle params = new Bundle();
        params.putString("message", content);
/* make the API call */
        new GraphRequest(

                AccessToken.getCurrentAccessToken(),
                "/" + pageId + "/feed",
                params,
                HttpMethod.POST,
                graphResponse -> {

            /* handle the result */
                    Log.d(TAG, graphResponse.toString());
                    displayPageFeed(mPageId);
                }

        ).executeAsync();
    }


    private void getAvailablePagesAsync() {

        /* make the API call */
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/accounts",
                null,
                HttpMethod.GET,
                response -> {

                    /* handle the result */

                    String graphObject = response.getJSONObject().toString();
                    Gson gson = new GsonBuilder().create();
                    AccountsResponse tokenResponse = gson.fromJson(graphObject, AccountsResponse.class);


                    // TODO: 10/19/2016 1. Update ActionBar Spinner; 2. Load item 0
                    List<Account> accounts = tokenResponse.getData();
                    if (accounts != null) {
                        //update Drawer
                        mAdapter = new NavPageListAdapter(accounts, this);
                        mRecyclerView.setAdapter(mAdapter);

                        //load item 0;
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

    @Override
    public void displayPageFeed(String pageId) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        PageFeedFragment feedFragment = PageFeedFragment.newInstance(pageId);
        ft.replace(R.id.content_main, feedFragment);
        ft.commit();
        mPageId = pageId;
        mDrawer.closeDrawers();
    }

    private void showNewPostDialog() {
        FragmentManager fm = getSupportFragmentManager();
        NewPostFragment newPostFragment = new NewPostFragment();
        newPostFragment.show(fm, "fragment_edit_name");
    }




    @Override
    public void onPostButtonPressed(String pageId, String content) {
        createNewPostAsync(pageId, content);
    }
}
