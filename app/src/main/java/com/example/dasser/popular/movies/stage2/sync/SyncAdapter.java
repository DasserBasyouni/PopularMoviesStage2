package com.example.dasser.popular.movies.stage2.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.dasser.popular.movies.stage2.R;
import com.example.dasser.popular.movies.stage2.Utils;
import com.example.dasser.popular.movies.stage2.database.Contract;
import com.example.dasser.popular.movies.stage2.model.Constants;
import com.example.dasser.popular.movies.stage2.model.Movie;
import com.example.dasser.popular.movies.stage2.retrofit.MoviesAPI;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.app.AlarmManager.INTERVAL_FIFTEEN_MINUTES;
import static com.example.dasser.popular.movies.stage2.Utils.getUrlSortingOptionUrlFormat;


public class SyncAdapter extends AbstractThreadedSyncAdapter {
    private static final long SYNC_INTERVAL = INTERVAL_FIFTEEN_MINUTES;
    private static final long SYNC_FLEXTIME = INTERVAL_FIFTEEN_MINUTES / 3;
    private static final String TAG = SyncAdapter.class.getSimpleName();

    SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        Log.d(TAG, "In SyncAdapter");
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority
            , ContentProviderClient provider, SyncResult syncResult) {
        Log.d(TAG, "In onPerformSync");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/movie/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MoviesAPI moviesAPI = retrofit.create(MoviesAPI.class);

        final int sortingOption = Utils.getSortingPreference(getContext(), false);
        Call<MoviesAPI.ResultModel> connection1 = moviesAPI.getAllMovies(
                getUrlSortingOptionUrlFormat(sortingOption, getContext(), false));

        connection1.enqueue(new Callback<MoviesAPI.ResultModel>() {
            @Override
            public void onResponse(@NonNull Call<MoviesAPI.ResultModel> call, @NonNull Response<MoviesAPI.ResultModel> response) {
                final MoviesAPI.ResultModel body = response.body();

                if (body != null) {
                    List<Movie> results = body.getResults();
                    Log.v(TAG, "onResponse - body size is " + results.size());
                    Thread thread = new Thread(new Runnable() {
                        public void run() {
                            getAndSaveDataToDatabase(body.getResults(), sortingOption);
                        }
                    });
                    thread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
                        @Override
                        public void uncaughtException(Thread t, Throwable e) {
                            Log.e(TAG, "Error with getAndSaveDataToDatabase thread: " + e.getLocalizedMessage());
                        }
                    });
                    thread.start();
                    Utils.saveInitializationState(getContext(), sortingOption);
                    //Utils.showSyncingNotification(getContext());
                }
            }

            @Override
            public void onFailure(@NonNull Call<MoviesAPI.ResultModel> call, @NonNull Throwable t) {
                Log.e(TAG, "connection.enqueue.onFailure: " + t.getLocalizedMessage());
            }
        });
        Log.d(TAG, "Connecting to URL: " + connection1.request().url().toString());
    }

    private void getAndSaveDataToDatabase(final List<Movie> movies, int sortingOption) {
        Log.d(TAG, "In getAndSaveDataToDatabase");
        final int moviesSize = movies.size();
        //final Vector<ContentValues> cVVector = new Vector<>(moviesSize);
        //final int[] inserted = {0};
        final ContentValues[] moviesValues = new ContentValues[moviesSize];

        for (int i = 0; i < moviesSize; i++) {
            moviesValues[i] = new ContentValues();
            moviesValues[i].put(Contract.MoviesEntry.COLUMN_MOVIE_RATED, sortingOption);
            moviesValues[i].put(Contract.MoviesEntry.COLUMN_MOVIE_ID, movies.get(i).getId());
            moviesValues[i].put(Contract.MoviesEntry.COLUMN_MOVIE_SYS, movies.get(i).getOverview());
            moviesValues[i].put(Contract.MoviesEntry.COLUMN_MOVIE_RATE, movies.get(i).getVoteAverage());
            moviesValues[i].put(Contract.MoviesEntry.COLUMN_MOVIE_NAME, movies.get(i).getOriginalTitle());
            moviesValues[i].put(Contract.MoviesEntry.COLUMN_MOVIE_YEAR,
                    Utils.getDateFormat(movies.get(i).getReleaseDate()));
            /*movieValues.put(Contract.MoviesEntry.COLUMN_MOVIE_REVIEWS_A, "");
            movieValues.put(Contract.MoviesEntry.COLUMN_MOVIE_REVIEWS_C, "");
            movieValues.put(Contract.MoviesEntry.COLUMN_MOVIE_LENGTH, "");
            movieValues.put(Contract.MoviesEntry.COLUMN_MOVIE_TRAILERS, "");*/
            fetchPostersAndSaveDataToDatabase(i, moviesValues, movies);
        }
    }

    private void fetchPostersAndSaveDataToDatabase(final int i, final ContentValues[] moviesValues,
                                                   final List<Movie> movies) {
        Log.d(TAG, "In fetchPostersAndSaveDataToDatabase");
        Target target = new Target() {
            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }

            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                moviesValues[i].put(Contract.MoviesEntry.COLUMN_MOVIE_POSTER,
                        Utils.convertBitmapToByteArray(bitmap));
                //cVVector.add(moviesValues[finalI]);

                    /*if (cVVector.size() > 0) {
                        ContentValues[] cvArray = new ContentValues[cVVector.size()];
                        cVVector.toArray(cvArray);*/
                //SaveToDatabaseJob.runJobImmediately();
                Log.d(TAG, "In fetchPostersAndSaveDataToDatabase 1");
                if (i == movies.size() - 1) {
                    Log.d(TAG, "In fetchPostersAndSaveDataToDatabase 2");
                    Bundle bundle = new Bundle();
                    //bundle.putParcelableArray(Constants.EXTRA_MOVIES_VALUES, moviesValues);
                    SaveToDatabaseJob.runJobImmediately(bundle);
                    Log.d(TAG, "In fetchPostersAndSaveDataToDatabase 3");
                }
                //}
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                Log.e(TAG, "onBitmapFailed " + errorDrawable);
            }
        };
        Picasso.get().load("https://image.tmdb.org/t/p/w185" + movies.get(i).getPosterPath()).into(target);
    }


    public static void syncImmediately(Context context, Bundle bundle) {
        if (bundle == null) bundle = new Bundle();

        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);

        ContentResolver.requestSync(getSyncAccount(context),
                context.getString(R.string.content_authority), bundle);
    }

    private static Account getSyncAccount(Context context) {
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        Account newAccount = new Account(
                context.getString(R.string.app_name), context.getString(R.string.sync_account_type));

        if (accountManager != null && null == accountManager.getPassword(newAccount)) {
            if (!accountManager.addAccountExplicitly(newAccount, "", null))
                return null;

            onAccountCreated(newAccount, context);
        }
        return newAccount;
    }

    private static void onAccountCreated(Account newAccount, Context context) {
        /*
         * Since we've created an account
         */
        SyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);

        /*
         * Without calling setSyncAutomatically, our periodic sync will not be enabled.
         */
        ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true);

        /*
         * Finally, let's do a sync to get things started
         */
        syncImmediately(context, null);
    }

    private static void configurePeriodicSync(Context context, long syncInterval, long flexTime) {
        Log.d(TAG, "In configurePeriodicSync");
        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.content_authority);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // we can enable inexact timers in our periodic sync
            SyncRequest request = new SyncRequest.Builder().
                    syncPeriodic(syncInterval, flexTime).
                    setSyncAdapter(account, authority).
                    setExtras(new Bundle()).build();
            ContentResolver.requestSync(request);
        } else
            ContentResolver.addPeriodicSync(account, authority, new Bundle(), syncInterval);
    }

}