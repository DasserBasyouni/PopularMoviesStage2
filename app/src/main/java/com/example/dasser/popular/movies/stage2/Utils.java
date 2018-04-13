package com.example.dasser.popular.movies.stage2;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
   Created by Dasser on 21-Mar-18.
 */

public final class Utils {

    private static final String CHANNEL_ID = "syncing_notification";
    private static final int SYNCING_NOTIFICATION_ID = 0;

    private Utils() {}

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

    /*public static final class DataLoadedMode {
        public final static int sortedByPopularity = 0;
        public final static int sortedByRating = 1;
        public final static int sortedByFavorites = 2;
    }*/

    public final static class DataLoadedMode {
        public final static int sortedByPopularity = 0;
        public final static int sortedByRating = 1;
        public final static int sortedByFavorites = 2;
    }

    public static String getUrlSortingOptionUrlFormat(int sortBy, Context context, boolean inverted) {
        if (sortBy == DataLoadedMode.sortedByPopularity && !inverted
                || sortBy == DataLoadedMode.sortedByRating && inverted )
            return context.getString(R.string.most_popular_url_name);
        else
            return context.getString(R.string.top_rated_url_name);
    }

    static String getRateFormat(float rate) {
        return rate + "/10";
    }

    public static int getDateFormat(String date) {
        return Integer.parseInt(date.substring(0, 4));
    }

    static Bitmap getBitmapFromByteArray(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    public static String[] getYoutubeTrailers(String[] youtubeTrailers) {
        for(int i=0; i<youtubeTrailers.length ;i++){
            youtubeTrailers[i] = "https://www.youtube.com/watch?v=" + youtubeTrailers[i];
        }
        return youtubeTrailers;
    }

    static String getRunTimeFormat(String time) {
        return time + "mins";
    }

    public static byte[] convertBitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
        try {
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bos.toByteArray();
    }

    static void saveSortPreference(Context context, int newSortingPreference) {
        saveSharedPreferencesValue(context, R.string.pref_sorting_option, newSortingPreference);
    }

    public static void saveInitializationState(Context context, int sortingOption) {
        int stringResID;

        if (sortingOption == DataLoadedMode.sortedByPopularity)
            stringResID = R.string.pref_popular_initialized;
        else
            stringResID = R.string.pref_rated_initialized;

        saveSharedPreferencesValue(context, stringResID, true);
    }

    private static void saveSharedPreferencesValue(Context context, int stringResID, Object object) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        if (object instanceof Boolean)
            editor.putBoolean(context.getString(stringResID), (boolean) object);
        else
            editor.putInt(context.getString(stringResID), (int) object);
        editor.apply();
    }

    public static int getSortingPreference(Context context, boolean inverted) {
        int sortingPref = getSharedPreferences(context).getInt(
                context.getString(R.string.pref_sorting_option), DataLoadedMode.sortedByPopularity);
        if (inverted) {
            if (sortingPref == DataLoadedMode.sortedByPopularity)
                sortingPref = DataLoadedMode.sortedByRating;
            else
                sortingPref = DataLoadedMode.sortedByPopularity;
        }
        return sortingPref;
    }

    static boolean isMostPopularInitialized(Context context) {
        return getSharedPreferences(context).getBoolean(
                context.getString(R.string.pref_popular_initialized), false);
    }

    static boolean isTopRatedInitialized(Context context) {
        return getSharedPreferences(context).getBoolean(
                context.getString(R.string.pref_rated_initialized), false);
    }

    private static SharedPreferences getSharedPreferences(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context);
    }
}