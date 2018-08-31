package com.udacity.heather.popmoviesstage1final.Utilities;

import android.net.Uri;

import java.net.MalformedURLException;
import java.net.URL;

public class Utils {
  /* 1. To run this Popular Movies app you will need to create an account at https://www.themoviedb.org/account/signup
     2. Request an API key through your account at https://www.themoviedb.org/settings/api
     3. Copy and paste your API key into the the string below to replace "copy API Key here". */

    private static String API_KEY = "copy API Key Here";


    private static String API_KEY_PARAM = "api_key";

    private static String BASE_URL = "http://api.themoviedb.org/3/movie/";
    static String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";

    private static String POPULAR_MOVIES_PATH = "popular";
    private static String TOP_RATED_MOVIES_PATH = "top_rated";

    private static String REVIEWS_PATH = "reviews";
    private static String VIDEOS_PATH = "videos";


//Build the URL to query The Movie Database for the full list of movies

    private static URL buildUrl(String sortOder) {
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendEncodedPath(sortOder)
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

// Build URL to Query to sort by Most Popular
    public static URL buildPopularMoviesQuery() {
        return buildUrl(POPULAR_MOVIES_PATH);
    }

//Build URL to Query to sort by Top Rated
    public static URL buildTopRatedMoviesUrl() {
        return buildUrl(TOP_RATED_MOVIES_PATH);
    }

    //Build URL to query TMDB for images
    public static URL buildImageBaseUrl(String imageWidth, String imageName) {
        Uri builtUri = Uri.parse(IMAGE_BASE_URL).buildUpon().appendEncodedPath(imageWidth + imageName).build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    //Build URL to query TMDB for a single movie
    public static URL buildMovieUrl(int id) {
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendEncodedPath(Integer.toString(id))
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }
    //Builds the URL used to query The Movie Database for a single movie's list of videos.

    public static URL buildVideosUrl(int id) {
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendEncodedPath(Integer.toString(id))
                .appendEncodedPath(VIDEOS_PATH)
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    // Builds the URL used to query The Movie Database for a single movie's list of reviews.

    public static URL buildReviewsUrl(int id) {
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendEncodedPath(Integer.toString(id))
                .appendEncodedPath(REVIEWS_PATH)
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }



}




