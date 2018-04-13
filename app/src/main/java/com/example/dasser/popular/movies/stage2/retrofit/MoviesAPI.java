package com.example.dasser.popular.movies.stage2.retrofit;

import com.example.dasser.popular.movies.stage2.model.Movie;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
   Created by dasser on 16-Mar-18.
 */

public interface MoviesAPI {
    String api_key_inUrl = "?api_key=";

    // TODO please add you API key here to get the app working, have a good day ^_^
    String apiKey = "64bed607af1f9b1c73ec98c70004f5e2";

    String lang = "&language=en-US";
    String page = "&page=1";

    @GET("{ratedOrPopular}" + api_key_inUrl + apiKey + lang + page)
    Call<ResultModel> getAllMovies(@Path("ratedOrPopular") String ratedOrPopular);

    class ResultModel {
        private List<Movie> results;

        public List<Movie> getResults() {
            return results;
        }
    }
}