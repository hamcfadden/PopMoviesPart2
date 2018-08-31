package com.udacity.heather.popmoviesstage1final.Utilities;

import com.google.gson.Gson;
import com.udacity.heather.popmoviesstage1final.Models.Movie;

public class JsonMovieUtils {

    Movie movie;

    public  JsonMovieUtils() {
        movie = new Movie();
    }

    public static Movie parse(String json) {
        Gson gson = new Gson();

        return gson.fromJson(json, Movie.class);
    }

}



