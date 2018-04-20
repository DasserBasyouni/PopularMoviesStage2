package com.example.dasser.popular.movies.stage2.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ImageView;

import com.example.dasser.popular.movies.stage2.R;
import com.example.dasser.popular.movies.stage2.retrofit.MoviesAPI;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.dasser.popular.movies.stage2.Constants.DataLoadedMode.sortedByPopularity;
import static com.example.dasser.popular.movies.stage2.Constants.DataLoadedMode.sortedByRating;
import static com.example.dasser.popular.movies.stage2.Constants.VideosSiteType.youTube;

/**
   Created by Dasser on 21-Mar-18.
 */

public final class Utils {

    private Utils() {}

    public static String convertSitesNamesToIntValue(String site) {
        return site.replace("Youtube", String.valueOf(youTube));
    }

    public static String getAuthorFormat(Context context, String author) {
        return context.getString(R.string.author_format, author);
    }

    public static MoviesAPI setupRetrofitAndGetMoviesAPI() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/movie/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(MoviesAPI.class);
    }

    public static void loadPosterImage(final String TAG, final String url, final ImageView imageView) {
        Picasso.get().load(url).networkPolicy(NetworkPolicy.OFFLINE).into(imageView, new Callback() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "success");
            }

            @Override
            public void onError(Exception e) {
                Log.e(TAG, "loadPosterImage - onError: " + e.getLocalizedMessage());
                Picasso.get().load(url).into(imageView);
            }
        });
    }

    static String getUrlSortingOptionUrlFormat(int sortBy, Context context, boolean inverted) {
        if (sortBy == sortedByPopularity && !inverted
                || sortBy == sortedByRating && inverted )
            return context.getString(R.string.most_popular_url_name);
        else
            return context.getString(R.string.top_rated_url_name);
    }

    public static String getRateFormat(float rate) {
        return rate + "/10";
    }

    public static int getDateFormat(String date) {
        return Integer.parseInt(date.substring(0, 4));
    }

    public static String getRunTimeFormat(Context context, String time) {
        return context.getString(R.string.runtime_format, time);
    }

    public static void saveSortPreference(Context context, int newSortingPreference) {
        saveSharedPreferencesValue(context, R.string.pref_sorting_option, newSortingPreference);
    }

    private static void saveSharedPreferencesValue(Context context, int stringResID, Object object) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        if (object instanceof Boolean)
            editor.putBoolean(context.getString(stringResID), (boolean) object);
        else
            editor.putInt(context.getString(stringResID), (int) object);
        editor.apply();
    }

    public static boolean isDataInitialized(Context context, int sortingOption) {
        if (sortingOption == sortedByPopularity)
            return getSharedPreferences(context).getBoolean(
                context.getString(R.string.pref_popular_initialized), false);
        else if (sortingOption == sortedByRating)
            return getSharedPreferences(context).getBoolean(
                context.getString(R.string.pref_rated_initialized), false);
        else
            return getSharedPreferences(context).getBoolean(context.getString(R.string.pref_popular_initialized)
                    , false) || getSharedPreferences(context)
                    .getBoolean(context.getString(R.string.pref_rated_initialized), false);
    }

    public static void saveInitializationState(Context context, int sortingOption) {
        int stringResID;

        if (sortingOption == sortedByPopularity)
            stringResID = R.string.pref_popular_initialized;
        else
            stringResID = R.string.pref_rated_initialized;

        saveSharedPreferencesValue(context, stringResID, true);
    }

    public static int getSortingPreference(Context context) {
        return getSharedPreferences(context).getInt(
                context.getString(R.string.pref_sorting_option), sortedByPopularity);
    }

    private static SharedPreferences getSharedPreferences(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

}