package com.example.dasser.popular.movies.stage2.model;

public final class IdAndPoster {
    private final int id;
    private final String posterPath;

    public IdAndPoster(final int id, final String posterPath) {
        this.id = id;
        this.posterPath = posterPath;
    }

    public int getID() {
        return id;
    }

    public String getPosterPath() {
        return posterPath;
    }
}