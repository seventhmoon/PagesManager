package com.androidfung.facebook.pagesmanager;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by fung on 10/26/2016.
 */

public class MyApplication extends Application{

    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}
