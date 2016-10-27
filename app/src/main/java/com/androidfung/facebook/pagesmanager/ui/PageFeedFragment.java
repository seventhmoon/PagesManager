package com.androidfung.facebook.pagesmanager.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.androidfung.facebook.graph.GraphRequestHelper;
import com.androidfung.facebook.graph.model.response.page.FeedResponse;
import com.androidfung.facebook.pagesmanager.R;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Arrays;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PageFeedFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PageFeedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PageFeedFragment extends Fragment {

    private static final String TAG = PageFeedFragment.class.getSimpleName();


    private static final String ARG_PAGE_ID = "pageId";

    private String mPageId;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private OnFragmentInteractionListener mListener;

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
    public static PageFeedFragment newInstance(String pageId) {
        PageFeedFragment fragment = new PageFeedFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PAGE_ID, pageId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPageId = getArguments().getString(ARG_PAGE_ID);
        }
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("read_insights"));
    }

    public void refresh() {
        updateFeedAsync();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_page_feed, container, false);

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
        /* make the API call */
        GraphRequestHelper.getFeedGraphRequest(mPageId, graphResponse -> {
                /* handle the result */
            Log.d(TAG, graphResponse.toString());
            if (graphResponse.getJSONObject() == null) {
                Log.d(TAG, graphResponse.getError().toString());
            } else {
                String graphObject = graphResponse.getJSONObject().toString();
                Gson gson = new GsonBuilder().create();
                FeedResponse feedResponse = gson.fromJson(graphObject, FeedResponse.class);

                mAdapter = new PageFeedAdapter(getContext(), feedResponse.getData());
                mRecyclerView.setAdapter(mAdapter);
                if (feedResponse.getData().isEmpty()) {
                    Toast.makeText(getActivity(), "The result set is empty.", Toast.LENGTH_SHORT).show();
                }
            }
            mSwipeRefreshLayout.setRefreshing(false);
        }).executeAsync();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {

    }
}
