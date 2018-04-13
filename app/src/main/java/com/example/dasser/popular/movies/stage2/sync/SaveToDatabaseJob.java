package com.example.dasser.popular.movies.stage2.sync;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobManager;
import com.evernote.android.job.JobRequest;
import com.example.dasser.popular.movies.stage2.database.Contract;
import com.example.dasser.popular.movies.stage2.model.Constants;

public class SaveToDatabaseJob extends Job {

    private static final String TAG = SaveToDatabaseJob.class.getSimpleName();
    public static final String SAVE_TO_DATABASE_TAG = "save_to_database_job_tag";
    private static int saveToDatabaseJobId;


    @NonNull
    @Override
    protected Result onRunJob(@NonNull Params params) {
        Log.d(TAG, "In saveDataToDatabase 1");

        ContentValues[] moviesValues = (ContentValues[]) params.getTransientExtras()
                .getParcelableArray(Constants.EXTRA_MOVIES_VALUES);
        Log.d(TAG, "In saveDataToDatabase 2");
        Log.e("Z_", String.valueOf(moviesValues == null));

        if (moviesValues != null) {
            int inserted = getContext().getContentResolver()
                    .bulkInsert(Contract.MoviesEntry.CONTENT_URI, moviesValues);
            Log.d(TAG, "saveDataToDatabase - onBitmapLoaded: Task Complete " + inserted + " newly Inserted");
            return Result.SUCCESS;
        }else
            return Result.FAILURE;
    }

    public static void runJobImmediately(Bundle bundle) {
        Log.d(TAG, "In runJobImmediately");
        saveToDatabaseJobId = new JobRequest.Builder(SAVE_TO_DATABASE_TAG)
                .setTransientExtras(bundle)
                .startNow()
                .build()
                .schedule();
    }

    private void cancelJob() {
        JobManager.instance().cancel(saveToDatabaseJobId);
    }
}
