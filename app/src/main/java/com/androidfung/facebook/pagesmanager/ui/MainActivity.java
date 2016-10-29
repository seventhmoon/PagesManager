package com.androidfung.facebook.pagesmanager.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
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

import com.androidfung.facebook.graph.GraphRequestHelper;
import com.androidfung.facebook.graph.model.Account;
import com.androidfung.facebook.pagesmanager.R;
import com.androidfung.facebook.graph.model.response.me.AccountsResponse;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.TreeMap;

import static android.view.View.GONE;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, NavigationCallback, NewPostFragment.OnFragmentInteractionListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private CallbackManager mCallbackManager;
    private AccessTokenTracker mAccessTokenTracker;

    private String mPageId;
    private TreeMap<String, String> mAccessTokenMap;
//    private String mPageAccessToken;

    private DrawerLayout mDrawer;
    private FloatingActionButton mFab;
    private NavigationView mNavigationView;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    private PageFeedFragment mPublishedFragment, mUnpublishedFragment;


    public MainActivity() {
    }

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

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
//        mSectionsPagerAdapter = new DummyTabbedActivity.SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
//        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setOnClickListener(view -> {
            showNewPostDialog();
//            startNewPostActivity();
        });

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

        AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                if (currentAccessToken == null) {
                    //log out

                    hideFab();
                    hideTabs();
                    mRecyclerView.setVisibility(GONE);
                    setTitle(R.string.title_activity_main);
                    mViewPager.setVisibility(GONE);
                }
            }
        };

        LoginButton loginButton = (LoginButton) headerLayout.findViewById(R.id.login_button);

        loginButton.setPublishPermissions("manage_pages", "publish_pages", "publish_actions");
//        loginButton.setReadPermissions("read_insights");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
//                Toast.makeText(getApplicationContext(), loginResult.toString(), Toast.LENGTH_SHORT).show();


                if (AccessToken.getCurrentAccessToken() == null) {
                    //user not logged in

                    //show login Fragment
                    hideFab();
                    hideTabs();
                    mRecyclerView.setVisibility(GONE);
                    setTitle(R.string.title_activity_main);

                } else {
                    //user logged in

                    //get page id array
//                    getAvailablePagesAsync();

                    //show post feed fragment
                    showFab();
                    mRecyclerView.setVisibility(View.VISIBLE);
                }
                updateDrawerAsync();

//                mDrawer.closeDrawers();
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
        if (AccessToken.getCurrentAccessToken() == null) {
            //user not logged in
            mRecyclerView.setVisibility(GONE);
        } else {
            //user logged in
            mRecyclerView.setVisibility(View.VISIBLE);
            getAvailablePagesAsync();
        }

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

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        // Currently no menu item.
        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }


    private void createNewPostAsync(String pageId, String content, boolean published) {

        Bundle params = new Bundle();
        params.putString("message", content);
        params.putBoolean("published", published);


        AccessToken userToken = AccessToken.getCurrentAccessToken();
        AccessToken pageToken = new AccessToken(mAccessTokenMap.get(mPageId), userToken.getApplicationId(), userToken.getUserId(),
                userToken.getPermissions(), userToken.getDeclinedPermissions(), null, null, null
        );

        Log.d(TAG, userToken.getPermissions().toString());

        /* make the API call */
        GraphRequest graphRequest = GraphRequestHelper.getNewPostGraphRequest(pageToken, params, pageId, graphResponse -> {
            Log.d(TAG, graphResponse.toString());

            if (graphResponse.getError() != null) {
                Toast.makeText(this, graphResponse.getError().toString(), Toast.LENGTH_SHORT).show();
            } else {

//                if (mFeedFragment != null) {
//                    mFeedFragment.refresh();
//                }
//                displayPageFeed(mPageId);


                for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
//                int currentTabIndex = mViewPager.getCurrentItem();
                    ((PageFeedFragment) mSectionsPagerAdapter.getItem(i)).refresh();
                }

            }
        });

        graphRequest.executeAsync();
    }


    private void getAvailablePagesAsync() {
        GraphRequest graphRequest = GraphRequestHelper.getMeAccoountsGraphRequest(graphResponse -> {
            String graphObject = graphResponse.getJSONObject().toString();
            Gson gson = new GsonBuilder().create();
            AccountsResponse tokenResponse = gson.fromJson(graphObject, AccountsResponse.class);


            List<Account> accounts = tokenResponse.getData();
            if (accounts != null && !accounts.isEmpty()) {
                //update Drawer
                mAdapter = new NavPageListAdapter(accounts, this);
                mRecyclerView.setAdapter(mAdapter);

                //load item 0;
                mAccessTokenMap = tokenResponse.getAccessTokens();
                displayPageFeed(accounts.get(0).getId());
                setTitle(accounts.get(0).getName());
//                mRecyclerView.getChildAt(0).callOnClick();
            }

        });
        graphRequest.executeAsync();
    }


    private void hideTabs() {
        findViewById(R.id.tabs).setVisibility(GONE);
    }

    private void showTab() {
        findViewById(R.id.tabs).setVisibility(View.VISIBLE);
    }

    private void hideFab() {
        mFab.setVisibility(GONE);
    }

    private void showFab() {
        mFab.setVisibility(View.VISIBLE);
    }

    @Override
    public void displayPageFeed(String pageId) {
        if (mPublishedFragment == null) {
            mPublishedFragment = PageFeedFragment.newInstance(pageId, PageFeedFragment.TYPE_FEED);
        } else {
            mPublishedFragment.setPageId(pageId);
        }

        if (mUnpublishedFragment == null) {
            mUnpublishedFragment = PageFeedFragment.newInstance(pageId, PageFeedFragment.TYPE_PROMOTEABLE_POSTS);
        } else {
            mUnpublishedFragment.setPageId(pageId);
        }


        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());


        // Set up the ViewPager with the sections adapter.
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setVisibility(View.VISIBLE);

        mPageId = pageId;
        mViewPager.setVisibility(View.VISIBLE);
        mDrawer.closeDrawers();
    }

    private void showNewPostDialog() {
        FragmentManager fm = getSupportFragmentManager();
        NewPostFragment newPostFragment = NewPostFragment.newInstance(mPageId);
        newPostFragment.show(fm, "new_post_fragment");
    }


    @Override
    public void onPostButtonPressed(String pageId, String content, boolean published) {
        createNewPostAsync(pageId, content, published);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {


        private final String TAG = SectionsPagerAdapter.class.getSimpleName();

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).

            if (position == 0) {
                return mPublishedFragment;
            } else {
                return mUnpublishedFragment;
            }
        }

        @Override
        public int getCount() {

            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.label_published);
                case 1:
                    return getString(R.string.label_unpublished);
            }
            return null;
        }
    }
}
