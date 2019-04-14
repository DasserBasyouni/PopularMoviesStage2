package com.example.dasser.popular.movies.stage2.utils;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import android.util.Log;

import com.example.dasser.popular.movies.stage2.database.Contract;
import com.example.dasser.popular.movies.stage2.model.Movie;
import com.example.dasser.popular.movies.stage2.model.MovieMoreDetails;
import com.example.dasser.popular.movies.stage2.retrofit.MoviesAPI;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

import static com.example.dasser.popular.movies.stage2.database.Contract.MoviesEntry.CONTENT_URI;
import static com.example.dasser.popular.movies.stage2.utils.Utils.getUrlSortingOptionUrlFormat;

public class AsyncTasks {
    private static final String TAG = AsyncTasks.class.getSimpleName();

    public static AsyncTaskLoader<Cursor> getGetAndSaveMainDataToDatabase(final Context context
            , final CursorLoader cursorLoader) {
        Log.d(TAG, "In getGetAndSaveMainDataToDatabase");
        return new AsyncTaskLoader<Cursor>(context) {
            boolean dataIsReady;

            @Override
            protected void onStartLoading() {
                if (dataIsReady) {
                    deliverResult(cursorLoader.loadInBackground());
                } else {
                    forceLoad();
                }
            }

            @Nullable
            @Override
            public Cursor loadInBackground() {
                Log.d(TAG, "In getGetAndSaveMainDataToDatabase.loadInBackground");

                MoviesAPI moviesAPI = Utils.setupRetrofitAndGetMoviesAPI();

                final int sortingOption = Utils.getSortingPreference(context);
                List<Movie> movies = null;
                Call<MoviesAPI.ArrayResultModel> connection = moviesAPI.getAllMovies(
                        getUrlSortingOptionUrlFormat(sortingOption, getContext()));

                Log.d(TAG, "getGetAndSaveMainDataToDatabase - Connecting to URL: " + connection.request().url().toString());
                try {
                    Response<MoviesAPI.ArrayResultModel> response = connection.execute();
                    if (response.isSuccessful()) {

                        MoviesAPI.ArrayResultModel body = response.body();
                        if (body != null) {
                            movies = body.getResults();
                            Log.d(TAG, "onResponse - body size is " + movies.size());
                        }
                        Utils.saveInitializationState(getContext(), sortingOption);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (movies != null)
                    saveDataToDatabase(movies, sortingOption);

                dataIsReady = true;
                return cursorLoader.loadInBackground();
            }

            private void saveDataToDatabase(final List<Movie> moviesResultModel, int sortingOption) {
                Log.d(TAG, "In saveDataToDatabase");
                final int moviesSize = moviesResultModel.size();
                final ContentValues[] moviesValues = new ContentValues[moviesSize];

                for (int i = 0; i < moviesSize; i++) {
                    Movie movie = moviesResultModel.get(i);
                    moviesValues[i] = new ContentValues();
                    moviesValues[i].put(Contract.MoviesEntry.COLUMN_MOVIE_RATED, sortingOption);
                    moviesValues[i].put(Contract.MoviesEntry.COLUMN_MOVIE_ID, movie.getId());
                    moviesValues[i].put(Contract.MoviesEntry.COLUMN_MOVIE_SYS, movie.getOverview());
                    moviesValues[i].put(Contract.MoviesEntry.COLUMN_MOVIE_RATE, movie.getVoteAverage());
                    moviesValues[i].put(Contract.MoviesEntry.COLUMN_MOVIE_POSTER, movie.getPosterPath());
                    moviesValues[i].put(Contract.MoviesEntry.COLUMN_MOVIE_NAME, movie.getOriginalTitle());
                    moviesValues[i].put(Contract.MoviesEntry.COLUMN_MOVIE_YEAR,
                            Utils.getDateFormat(movie.getReleaseDate()));
                }
                int inserted = getContext().getContentResolver().bulkInsert(CONTENT_URI, moviesValues);
                Log.d(TAG, "getGetAndSaveMainDataToDatabase.saveDataToDatabase Task Complete "
                        + inserted + " newly Inserted");
            }
        };
    }

    public static Loader<Cursor> getGetAndSaveDetailsDataToDatabase(final Context context, final String movie_id) {
        Log.d(TAG, "In getGetAndSaveDetailsDataToDatabase");
        return new AsyncTaskLoader<Cursor>(context) {
            boolean dataIsReady;

            @Override
            protected void onStartLoading() {
                if (dataIsReady) {
                    deliverResult(null);
                } else {
                    forceLoad();
                }
            }

            @Nullable
            @Override
            public Cursor loadInBackground() {
                Log.d(TAG, "In getGetAndSaveDetailsDataToDatabase.loadInBackground");

                MoviesAPI moviesAPI = Utils.setupRetrofitAndGetMoviesAPI();
                final int sortingOption = Utils.getSortingPreference(context);

                Call<MovieMoreDetails> connection = moviesAPI.getMovieMoreDetails(movie_id);

                Log.d(TAG, "getGetAndSaveDetailsDataToDatabase - Connecting to URL: " + connection.request().url().toString());
                try {
                    Response<MovieMoreDetails> response = connection.execute();

                    if (response.isSuccessful()) {
                        MovieMoreDetails movieMoreDetails = response.body();
                        Log.d(TAG, "onResponse - body is " + response.body());
                        Utils.saveInitializationState(getContext(), sortingOption);

                        if (movieMoreDetails != null)
                            saveDataToDatabase(movieMoreDetails);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                dataIsReady = true;
                return null;
            }

            private void saveDataToDatabase(final MovieMoreDetails movieMoreDetails) {
                Log.d(TAG, "In getGetAndSaveDetailsDataToDatabase.saveDataToDatabase");

                ContentValues moviesValues = new ContentValues();
                moviesValues.put(Contract.MoviesEntry.COLUMN_MOVIE_REVIEWS_A, movieMoreDetails.getAllReviewsAuthors());
                moviesValues.put(Contract.MoviesEntry.COLUMN_MOVIE_REVIEWS_C, movieMoreDetails.getAllReviewsContents());
                moviesValues.put(Contract.MoviesEntry.COLUMN_MOVIE_TRAILERS_KEYS, movieMoreDetails.getAllVideosKeys());
                moviesValues.put(Contract.MoviesEntry.COLUMN_MOVIE_TRAILERS_NAMES, movieMoreDetails.getAllVideosNames());
                moviesValues.put(Contract.MoviesEntry.COLUMN_MOVIE_TRAILERS_SITES
                        , Utils.convertSitesNamesToIntValue(movieMoreDetails.getAllVideosSites()));
                moviesValues.put(Contract.MoviesEntry.COLUMN_MOVIE_RUNTIME, movieMoreDetails.getRuntime());

                int update = getContext().getContentResolver()
                        .update(CONTENT_URI, moviesValues
                                , Contract.MoviesEntry.COLUMN_MOVIE_ID + "=?"
                                , new String[]{movie_id});
                Log.d(TAG, "getGetAndSaveDetailsDataToDatabase.saveDataToDatabase - Task Completed "
                        + update + "has been updated");
            }

        };
    }
}