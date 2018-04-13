package com.example.dasser.popular.movies.stage2.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 Created by Dasser on 16-Mar-18.
 */

public class Movie implements Parcelable {

    private final int id;
    private final float vote_average;
    private final String original_title;
    private final String poster_path;
    private final String overview;
    private final String release_date;

    public int getId() {
        return id;
    }

    public float getVoteAverage() {
        return vote_average;
    }

    public String getOriginalTitle() {
        return original_title;
    }

    public String getPosterPath() {
        return poster_path;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {
        return release_date;
    }

    private Movie(Parcel in) {
        id = in.readInt();
        vote_average = in.readFloat();
        original_title = in.readString();
        poster_path = in.readString();
        overview = in.readString();
        release_date = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(id);
        dest.writeFloat(vote_average);
        dest.writeString(original_title);
        dest.writeString(poster_path);
        dest.writeString(overview);
        dest.writeString(release_date);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}