package com.androidfung.facebook.pagesmanager.ui;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidfung.facebook.pagesmanager.R;

/**
 * A placeholder fragment containing a simple view.
 */
@Deprecated
public class MobileActivityFragment extends Fragment {

    public MobileActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mobile, container, false);
    }
}
