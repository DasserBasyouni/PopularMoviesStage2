package com.example.dasser.popular.movies.stage2;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.evernote.android.job.JobManager;
import com.example.dasser.popular.movies.stage2.sync.SyncJobCreator;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        JobManager.create(this).addJobCreator(new SyncJobCreator());
    }
}