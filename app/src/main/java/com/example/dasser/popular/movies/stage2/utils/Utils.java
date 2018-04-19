package com.example.dasser.popular.movies.stage2.utils;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.IntDef;
import android.support.annotation.StringDef;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.ImageView;

import com.example.dasser.popular.movies.stage2.MainActivity;
import com.example.dasser.popular.movies.stage2.R;
import com.example.dasser.popular.movies.stage2.retrofit.MoviesAPI;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.dasser.popular.movies.stage2.utils.Utils.ArrayStringMovieDataType.author;
import static com.example.dasser.popular.movies.stage2.utils.Utils.ArrayStringMovieDataType.content;
import static com.example.dasser.popular.movies.stage2.utils.Utils.ArrayStringMovieDataType.key;
import static com.example.dasser.popular.movies.stage2.utils.Utils.ArrayStringMovieDataType.name;
import static com.example.dasser.popular.movies.stage2.utils.Utils.ArrayStringMovieDataType.site;
import static com.example.dasser.popular.movies.stage2.utils.Utils.DataLoadedMode.sortedByFavorites;
import static com.example.dasser.popular.movies.stage2.utils.Utils.DataLoadedMode.sortedByPopularity;
import static com.example.dasser.popular.movies.stage2.utils.Utils.DataLoadedMode.sortedByRating;
import static com.example.dasser.popular.movies.stage2.utils.Utils.VideosSiteType.youTube;

/**
   Created by Dasser on 21-Mar-18.
 */

public final class Utils {

    private static final String CHANNEL_ID = "syncing_notification";
    private static final int SYNCING_NOTIFICATION_ID = 0;

    private Utils() {}

    public static String convertSitesNamesToIntValue(String site) {
        return site.replace("Youtube", String.valueOf(youTube));
    }

    @IntDef({sortedByPopularity, sortedByRating, sortedByFavorites})
    public @interface DataLoadedMode {
        int sortedByPopularity = 0;
        int sortedByRating = 1;
        int sortedByFavorites = 2;
    }


    @IntDef({author, content, key, name, site})
    public @interface ArrayStringMovieDataType {
        int author = 0;
        int content = 1;
        int key = 2;
        int name = 3;
        int site = 4;
    }

    @StringDef({youTube})
    public @interface VideosSiteType {
        String youTube = "0";
    }


    public static MoviesAPI setupRetrofitAndGetMoviesAPI() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/movie/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(MoviesAPI.class);
    }

    public static void showSyncingNotification(Context context) {
        Log.e("Z_", "show notification");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = context.getString(R.string.channel_name);
            String description = context.getString(R.string.channel_description);
            int importance = NotificationManagerCompat.IMPORTANCE_DEFAULT;
            @SuppressLint("WrongConstant") NotificationChannel channel
                    = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_sync_black_24dp)
                .setContentTitle("My notification")
                .setContentText("Much longer text that cannot fit one line...")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Much longer text that cannot fit one line..."))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);;

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(SYNCING_NOTIFICATION_ID, mBuilder.build());
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

    public static String[] getYoutubeTrailers(String[] youtubeTrailers) {
        for(int i=0; i<youtubeTrailers.length ;i++){
            youtubeTrailers[i] = "https://www.youtube.com/watch?v=" + youtubeTrailers[i];
        }
        return youtubeTrailers;
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
        else
            return getSharedPreferences(context).getBoolean(
                context.getString(R.string.pref_rated_initialized), false);
    }

    public static void saveInitializationState(Context context, int sortingOption) {
        int stringResID;

        if (sortingOption == sortedByPopularity)
            stringResID = R.string.pref_popular_initialized;
        else
            stringResID = R.string.pref_rated_initialized;

        saveSharedPreferencesValue(context, stringResID, true);
    }

    public static int getSortingPreference(Context context, boolean inverted) {
        int sortingPref = getSharedPreferences(context).getInt(
                context.getString(R.string.pref_sorting_option), sortedByPopularity);
        if (inverted) {
            if (sortingPref == sortedByPopularity)
                sortingPref = sortedByRating;
            else
                sortingPref = sortedByPopularity;
        }
        return sortingPref;
    }

    private static SharedPreferences getSharedPreferences(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static float dpToPixels(int dp, Context context) {
        return dp * (context.getResources().getDisplayMetrics().density);
    }
}