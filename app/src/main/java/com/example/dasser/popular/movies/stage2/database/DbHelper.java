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

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "movies.db";

    DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_WEATHER_TABLE = "CREATE TABLE " + Contract.MoviesEntry.TABLE_NAME + " (" +
                Contract.MoviesEntry.COLUMN_MOVIE_ID + " INTEGER UNIQUE NOT NULL, " +

                Contract.MoviesEntry.COLUMN_MOVIE_NAME + " TEXT NOT NULL, " +
                Contract.MoviesEntry.COLUMN_MOVIE_POSTER + " BLOB NOT NULL," +
                Contract.MoviesEntry.COLUMN_MOVIE_RATE + " REAL NOT NULL, " +
                Contract.MoviesEntry.COLUMN_MOVIE_SYS + " TEXT NOT NULL, " +
                Contract.MoviesEntry.COLUMN_MOVIE_YEAR + " INTEGER NOT NULL, " +

                Contract.MoviesEntry.COLUMN_MOVIE_LENGTH + " INTEGER, " +
                Contract.MoviesEntry.COLUMN_MOVIE_TRAILERS + " TEXT, " +
                Contract.MoviesEntry.COLUMN_MOVIE_REVIEWS_A + " TEXT, " +
                Contract.MoviesEntry.COLUMN_MOVIE_REVIEWS_C + " TEXT, " +
                Contract.MoviesEntry.COLUMN_MOVIE_FAV + " INTEGER, " +
                Contract.MoviesEntry.COLUMN_MOVIE_RATED + " TEXT);";

        sqLiteDatabase.execSQL(SQL_CREATE_WEATHER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        // If you want to update the schema without wiping data, commenting out the next 2 lines
        // should be your top priority before modifying this method.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Contract.MoviesEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}