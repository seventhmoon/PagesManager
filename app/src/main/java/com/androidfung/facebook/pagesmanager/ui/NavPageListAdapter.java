package com.androidfung.facebook.pagesmanager.ui;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidfung.facebook.graph.model.Account;
import com.androidfung.facebook.graph.model.Post;
import com.androidfung.facebook.pagesmanager.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fung on 10/14/2016.
 */

public class NavPageListAdapter extends RecyclerView.Adapter<NavPageListAdapter.ViewHolder> {


    private ArrayList<Account> mDataset;
    private NavigationCallback mNavigationCallback;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View mRootView;
        public TextView mTextViewId;
        public TextView mTextViewName;

        public ViewHolder(View v) {
            super(v);
            mRootView = v;
            mTextViewId = (TextView) v.findViewById(R.id.textview_page_id);
            mTextViewName = (TextView)v.findViewById(R.id.textview_page_name);

        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public NavPageListAdapter(List<Account> myDataset, NavigationCallback navigationCallback) {
        mDataset = new ArrayList<Account>(myDataset);
        mNavigationCallback = navigationCallback;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public NavPageListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_account, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        Account account = mDataset.get(position);
        holder.mTextViewId.setText(account.getId());
        holder.mTextViewName.setText(account.getName());
        holder.mRootView.setOnClickListener(view -> {
            mNavigationCallback.displayPageFeed(account.getId());
            mNavigationCallback.setTitle(account.getName());
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
