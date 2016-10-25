package com.androidfung.facebook.pagesmanager.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidfung.facebook.graph.model.Post;
import com.androidfung.facebook.pagesmanager.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fung on 10/14/2016.
 */

public class PageFeedAdapter extends RecyclerView.Adapter<PageFeedAdapter.ViewHolder> {


    private ArrayList<Post> mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View mRootView;
        public TextView mTextViewId;
        public TextView mTextViewMessage;
        public TextView mTextViewStory;
        public TextView mTextViewCreatedTime;
        public ViewHolder(View v) {
            super(v);
            mTextViewId = (TextView) v.findViewById(R.id.post_id_textview);
            mTextViewCreatedTime = (TextView)v.findViewById(R.id.post_created_time_textview);
            mTextViewMessage = (TextView) v.findViewById(R.id.post_message_textview);
            mTextViewStory = (TextView)v.findViewById(R.id.post_story_textview);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public PageFeedAdapter(List<Post> myDataset) {
        mDataset = new ArrayList<Post>(myDataset);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public PageFeedAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_post, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Post post = mDataset.get(position);
        holder.mTextViewId.setText(post.getId());
        holder.mTextViewMessage.setText(post.getMessage());
        holder.mTextViewStory.setText(post.getStory());
        holder.mTextViewCreatedTime.setText(post.getCreatedTime().toString());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
