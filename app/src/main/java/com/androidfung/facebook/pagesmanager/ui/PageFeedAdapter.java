package com.androidfung.facebook.pagesmanager.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidfung.facebook.graph.model.Post;
import com.androidfung.facebook.pagesmanager.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by fung on 10/14/2016.
 */

public class PageFeedAdapter extends RecyclerView.Adapter<PageFeedAdapter.ViewHolder> {

    private final SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.US);
    private static final String TAG = PageFeedAdapter.class.getSimpleName();
    private ArrayList<Post> mDataset;
    private Context mContext;
//    private String mPageId;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View mRootView;
        public TextView mTextViewId;
        public TextView mTextViewMessage;
//        public TextView mTextViewStory;
        public TextView mTextViewCreatedTime;
        public TextView mTextViewViewCount;
        public TextView mTextViewHidden;
        public ViewHolder(View v) {
            super(v);
            mTextViewId = (TextView) v.findViewById(R.id.textview_post_id);
            mTextViewCreatedTime = (TextView)v.findViewById(R.id.textview_post_created_time);
            mTextViewMessage = (TextView) v.findViewById(R.id.textview_post_message);
//            mTextViewStory = (TextView)v.findViewById(R.id.textview_post_story);
            mTextViewViewCount = (TextView) v.findViewById(R.id.textview_post_view_count);
            mTextViewHidden = (TextView) v.findViewById(R.id.textview_post_hidden);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public PageFeedAdapter(Context context, List<Post> myDataset) {
        mContext = context;
        mDataset = new ArrayList<>(myDataset);
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

        holder.mTextViewHidden.setVisibility(post.isHidden()?View.VISIBLE:View.GONE);

        holder.mTextViewCreatedTime.setText(mDateFormat.format(post.getCreatedTime()));

        if (post.getInsights() != null && post.getInsights().getData().get(0) != null && post.getInsights().getData().get(0).getValues().get(0) != null) {
            int viewCount = post.getInsights().getData().get(0).getValues().get(0).getValue();
            holder.mTextViewViewCount.setText(mContext.getResources().getQuantityString(R.plurals.label_view, viewCount, viewCount));
            holder.mTextViewViewCount.setVisibility(View.VISIBLE);
        }else{
            holder.mTextViewViewCount.setVisibility(View.GONE);
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}
