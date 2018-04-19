/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.dasser.popular.movies.stage2.database;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Defines table and column names for the weather database.
 */
public class Contract {

    static final String CONTENT_AUTHORITY = "com.example.dasser.popular.movies.stage2";
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    static final String PATH_MOVIES = "movies";
    static final String PATH_FAV = "favorite";

    public static final int COLUMN_MOVIE_POSTER = 0;
    public static final int COLUMN_MOVIE_ID = 1;
    public static final int COLUMN_MOVIE_YEAR = 2;
    public static final int COLUMN_MOVIE_RATE = 3;
    public static final int COLUMN_MOVIE_RUNTIME = 4;
    public static final int COLUMN_MOVIE_SYS = 5;
    public static final int COLUMN_MOVIE_FAV = 6;
    public static final int COLUMN_MOVIE_NAME = 7;
    public static final int COLUMN_MOVIE_REVIEWS_A = 8;
    public static final int COLUMN_MOVIE_REVIEWS_C = 9;
    public static final int COLUMN_MOVIE_TRAILERS_KEYS = 10;
    public static final int COLUMN_MOVIE_TRAILERS_NAMES = 11;
    public static final int COLUMN_MOVIE_TRAILERS_SITES = 12;

    public static final String[] Main_Movies_COLUMNS = {
            MoviesEntry.COLUMN_MOVIE_POSTER,
            MoviesEntry.COLUMN_MOVIE_ID,
            MoviesEntry.COLUMN_MOVIE_YEAR,
            MoviesEntry.COLUMN_MOVIE_RATE,
            MoviesEntry.COLUMN_MOVIE_RUNTIME,
            MoviesEntry.COLUMN_MOVIE_SYS,
            MoviesEntry.COLUMN_MOVIE_FAV,
            MoviesEntry.COLUMN_MOVIE_NAME,
            MoviesEntry.COLUMN_MOVIE_REVIEWS_A,
            MoviesEntry.COLUMN_MOVIE_REVIEWS_C,
            MoviesEntry.COLUMN_MOVIE_TRAILERS_KEYS,
            MoviesEntry.COLUMN_MOVIE_TRAILERS_NAMES,
            MoviesEntry.COLUMN_MOVIE_TRAILERS_SITES
    };

    public static final class MoviesEntry implements BaseColumns {
        static final String TABLE_NAME = "movies";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_MOVIE_NAME = "name";
        public static final String COLUMN_MOVIE_RATE = "rate";
        public static final String COLUMN_MOVIE_YEAR = "year";
        public static final String COLUMN_MOVIE_SYS = "sys";
        public static final String COLUMN_MOVIE_POSTER = "poster";

        public static final String COLUMN_MOVIE_REVIEWS_A = "reviews_author";
        public static final String COLUMN_MOVIE_REVIEWS_C = "reviews_content";
        public static final String COLUMN_MOVIE_RUNTIME = "runtime";
        public static final String COLUMN_MOVIE_FAV = "fav";
        public static final String COLUMN_MOVIE_RATED = "rated";
        public static final String COLUMN_MOVIE_TRAILERS_KEYS = "trailers_keys";
        public static final String COLUMN_MOVIE_TRAILERS_NAMES = "trailers_names";
        public static final String COLUMN_MOVIE_TRAILERS_SITES = "trailers_sites";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

        static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;

        public static Uri buildMoviesUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }
}