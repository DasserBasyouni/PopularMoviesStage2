package com.example.dasser.popular.movies.stage2.retrofit;

import com.example.dasser.popular.movies.stage2.model.Movie;
import com.example.dasser.popular.movies.stage2.model.MovieMoreDetails;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
   Created by dasser on 16-Mar-18.
 */

public interface MoviesAPI {
    String api_key_attribute = "?api_key=";

    // TODO please add you API key here to get the app working, have a good day ^_^
    String apiKey_value = "";

    String appendToResponse = "&append_to_response=reviews,videos";
    String lang = "&language=en-US";
    String page = "&page=1";


    @GET("{ratedOrPopular}" + api_key_attribute + apiKey_value + lang + page)
    Call<ArrayResultModel> getAllMovies(@Path("ratedOrPopular") String ratedOrPopular);

    @GET("{moveId}" + api_key_attribute + apiKey_value + appendToResponse)
    Call<MovieMoreDetails> getMovieMoreDetails(@Path("moveId") String moveId);


    class ArrayResultModel {
        private List<Movie> results;

        public List<Movie> getResults() {
            return results;
        }
    }
}
