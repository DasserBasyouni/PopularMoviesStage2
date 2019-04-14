package com.example.dasser.popular.movies.stage2;

import androidx.annotation.IntDef;
import androidx.annotation.StringDef;

import static com.example.dasser.popular.movies.stage2.Constants.ArrayStringMovieDataType.author;
import static com.example.dasser.popular.movies.stage2.Constants.ArrayStringMovieDataType.content;
import static com.example.dasser.popular.movies.stage2.Constants.ArrayStringMovieDataType.key;
import static com.example.dasser.popular.movies.stage2.Constants.ArrayStringMovieDataType.name;
import static com.example.dasser.popular.movies.stage2.Constants.ArrayStringMovieDataType.site;
import static com.example.dasser.popular.movies.stage2.Constants.DataLoadedMode.sortedByFavorites;
import static com.example.dasser.popular.movies.stage2.Constants.DataLoadedMode.sortedByPopularity;
import static com.example.dasser.popular.movies.stage2.Constants.DataLoadedMode.sortedByRating;
import static com.example.dasser.popular.movies.stage2.Constants.VideosSiteType.youTube;

public class Constants {
    public static final String STRING_SEPARATOR = "__,__";

    public static final String EXTRA_MOVIE_ID = "movie_id";

    static final int MAIN_LOADER_ID = 0;
    static final int DETAILS_MAIN_DATA_LOADER_ID = 1;
    public static final int DETAILS_MORE_DATA_LOADER_ID = 2;

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
}

