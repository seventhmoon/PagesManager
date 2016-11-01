package com.androidfung.facebook.pagesmanager.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.androidfung.facebook.graph.GraphRequestHelper;
import com.androidfung.facebook.graph.model.Post;
import com.androidfung.facebook.graph.model.response.Response;
import com.androidfung.facebook.graph.model.response.page.FeedResponse;
import com.androidfung.facebook.pagesmanager.R;

import com.facebook.GraphRequest;
import com.facebook.login.LoginManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import java.util.Collections;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PageFeedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PageFeedFragment extends Fragment {


    @Retention(RetentionPolicy.SOURCE)
    @IntDef({TYPE_FEED, TYPE_PROMOTEABLE_POSTS})
    public @interface Type {
    }

    public static final int TYPE_FEED = 0;
    public static final int TYPE_PROMOTEABLE_POSTS = 1;

    private static final String TAG = PageFeedFragment.class.getSimpleName();


    private static final String ARG_PAGE_ID = "pageId";
    private static final String ARG_TYPE = "type";


    private String mPageId;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private TextView mTextViewEmpty;

    private int mType;

    public PageFeedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param pageId Numeric String of Page ID.
     * @return A new instance of fragment PageFeedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PageFeedFragment newInstance(String pageId, @Type int type) {
        PageFeedFragment fragment = new PageFeedFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PAGE_ID, pageId);
        args.putInt(ARG_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

//    public String getPageId() {
//        return mPageId;
//    }

    public void setPageId(String pageId) {
        this.mPageId = pageId;
        this.refresh();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPageId = getArguments().getString(ARG_PAGE_ID);
            mType = getArguments().getInt(ARG_TYPE);

            Log.d(TAG, mPageId);
        }
//        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("read_insights"));
        LoginManager.getInstance().logInWithReadPermissions(this, Collections.singletonList("read_insights"));
    }

    public void refresh() {
        updateFeedAsync();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_page_feed, container, false);
        mTextViewEmpty = (TextView) rootView.findViewById(R.id.textview_empty);
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swiperefresh);
/*
 * Sets up a SwipeRefreshLayout.OnRefreshListener that is invoked when the user
 * performs a swipe-to-refresh gesture.
 */
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
                    Log.i(TAG, "onRefresh called from SwipeRefreshLayout");

                    // This method performs the actual data-refresh operation.
                    // The method calls setRefreshing(false) when it's finished.
                    updateFeedAsync();
                }

        );

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.feed_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(false);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)


        updateFeedAsync();


        return rootView;
    }

    private void updateFeedAsync() {

        GraphRequest.Callback callback = graphResponse -> {

                      /* handle the result */
            Log.d(TAG, graphResponse.toString());
            if (graphResponse.getJSONObject() == null) {
                Log.d(TAG, graphResponse.getError().toString());
            } else {
                String graphObject = graphResponse.getJSONObject().toString();
                Gson gson = new GsonBuilder().create();
                ;
                Response<Post> feedResponse = gson.fromJson(graphObject, new TypeToken<Response<Post>>(){}.getType());


                if (feedResponse.getError() != null){
                    Toast.makeText(getActivity(), feedResponse.getError().getMessage(), Toast.LENGTH_SHORT).show();
                }else {
                    mAdapter = new PageFeedAdapter(getContext(), feedResponse.getData());
                    mRecyclerView.setAdapter(mAdapter);
                    if (feedResponse.getData().isEmpty()) {
                        mRecyclerView.setVisibility(View.GONE);
                        mTextViewEmpty.setVisibility(View.VISIBLE);
                    } else {
                        mRecyclerView.setVisibility(View.VISIBLE);
                        mTextViewEmpty.setVisibility(View.GONE);

                    }
                }
            }
            mSwipeRefreshLayout.setRefreshing(false);

        };

        if (mType == TYPE_FEED) {
            /* make the API call */
            GraphRequest graphRequest = GraphRequestHelper.getFeedGraphRequest(mPageId, callback);
            Log.d(TAG, graphRequest.toString());
            graphRequest.executeAsync();
        } else {
            GraphRequest graphRequest = GraphRequestHelper.getPromotablePostsGraphRequest(mPageId, callback);
            Log.d(TAG, graphRequest.toString());
            graphRequest.executeAsync();
        }


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
