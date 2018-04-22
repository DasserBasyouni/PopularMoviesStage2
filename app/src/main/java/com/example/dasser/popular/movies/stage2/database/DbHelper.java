package com.example.dasser.popular.movies.stage2.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.dasser.popular.movies.stage2.database.Contract.MoviesEntry.TABLE_NAME;


class DbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "movies.db";
    private static final String DATABASE_ALTER = "ALTER TABLE " + TABLE_NAME;


    DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_WEATHER_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                Contract.MoviesEntry.COLUMN_MOVIE_ID + " INTEGER UNIQUE NOT NULL, " +
                Contract.MoviesEntry.COLUMN_MOVIE_NAME + " TEXT NOT NULL, " +
                Contract.MoviesEntry.COLUMN_MOVIE_POSTER + " TEXT NOT NULL," +
                Contract.MoviesEntry.COLUMN_MOVIE_RATE + " REAL NOT NULL, " +
                Contract.MoviesEntry.COLUMN_MOVIE_SYS + " TEXT NOT NULL, " +
                Contract.MoviesEntry.COLUMN_MOVIE_YEAR + " INTEGER NOT NULL, " +

                Contract.MoviesEntry.COLUMN_MOVIE_RUNTIME + " INTEGER, " +
                Contract.MoviesEntry.COLUMN_MOVIE_TRAILERS_KEYS + " TEXT, " +
                Contract.MoviesEntry.COLUMN_MOVIE_TRAILERS_NAMES + " TEXT, " +
                Contract.MoviesEntry.COLUMN_MOVIE_TRAILERS_SITES + " INTEGER, " +
                Contract.MoviesEntry.COLUMN_MOVIE_REVIEWS_A + " TEXT, " +
                Contract.MoviesEntry.COLUMN_MOVIE_REVIEWS_C + " TEXT, " +
                Contract.MoviesEntry.COLUMN_MOVIE_FAV + " INTEGER, " +
                Contract.MoviesEntry.COLUMN_MOVIE_RATED + " TEXT);";
        sqLiteDatabase.execSQL(SQL_CREATE_WEATHER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL(DATABASE_ALTER);
    }
}