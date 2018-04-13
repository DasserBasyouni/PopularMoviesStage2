package com.example.dasser.popular.movies.stage2.sync;

import android.support.annotation.NonNull;
import android.util.Log;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobManager;
import com.evernote.android.job.JobRequest;

import java.util.concurrent.TimeUnit;

public class SyncJob extends Job {

    public static final String TAG = "job_tag";
    private static int jobId;

    @Override
    @NonNull
    protected Result onRunJob(@NonNull Params params) {
        Log.e("Z_", "In onRunJob");
        SyncAdapter.syncImmediately(getContext(), null);
        return Result.SUCCESS;
    }

    public synchronized static void schedulePeriodicJob() {
        Log.e("Z_", "In schedulePeriodicJob");
        jobId = new JobRequest.Builder(TAG)
                .setPeriodic(TimeUnit.MINUTES.toMillis(15), TimeUnit.MINUTES.toMillis(5))
                .setRequiredNetworkType(JobRequest.NetworkType.CONNECTED)
                .build()
                .schedule();
    }

    public static void runJobImmediately() {
        jobId = new JobRequest.Builder(TAG)
                .startNow()
                .build()
                .schedule();
    }

    private void cancelJob() {
        JobManager.instance().cancel(jobId);
    }
}