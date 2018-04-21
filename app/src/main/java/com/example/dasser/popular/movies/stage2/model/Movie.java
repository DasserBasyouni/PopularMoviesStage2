package com.example.dasser.popular.movies.stage2.model;

/**
 * Created by Dasser on 16-Mar-18.
 */

public class Movie {

    private final int id;
    private final float vote_average;
    private final String original_title;
    private final String poster_path;
    private final String overview;
    private final String release_date;

    @SuppressWarnings("unused")
    public Movie(int id, float vote_average, String original_title, String poster_path, String overview, String release_date) {
        this.id = id;
        this.vote_average = vote_average;
        this.original_title = original_title;
        this.poster_path = poster_path;
        this.overview = overview;
        this.release_date = release_date;
    }

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
}