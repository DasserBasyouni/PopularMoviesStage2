package com.example.dasser.popular.movies.stage2.sync;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;

public class SyncJobCreator implements JobCreator {

    @Override
    @Nullable
    public Job create(@NonNull String tag) {
        switch (tag) {
            case SyncJob.TAG:
                Log.e("Z_", "In Job create - case TAG");
                return new SyncJob();
            case SaveToDatabaseJob.SAVE_TO_DATABASE_TAG:
                Log.e("Z_", "In Job create - case SAVE_TO_DATABASE_TAG");
                return new SaveToDatabaseJob();
            default:
                return null;
        }
    }
}