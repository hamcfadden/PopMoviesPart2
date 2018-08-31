package com.udacity.heather.popmoviesstage1final.Models;

import android.databinding.BaseObservable;

import com.udacity.heather.popmoviesstage1final.Utilities.JsonReviewUtils;

import static com.udacity.heather.popmoviesstage1final.Utilities.Utils.buildImageBaseUrl;

public class Movie extends BaseObservable {

    private int id;
    private String original_title;
    private String poster_path;
    private String backdrop_path;
    private String overview;
    private Double vote_average;
    private String release_date;
    private Boolean video;
    private final static String POSTER_WIDTH = "w400";


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOriginalTitle() { return original_title; }

    public String getPosterPath() {
        return poster_path;
    }

    public String getOverview() { return overview; }

    public Double getVoteAverage() {return vote_average;}

    public String getReleaseDate() {return release_date;}

    public String getPosterUrl() {
    return buildImageBaseUrl(POSTER_WIDTH, poster_path).toString();
    }

    public String getBackdropUrl() {
        return buildImageBaseUrl(POSTER_WIDTH, backdrop_path).toString();
    }
    public Boolean getVideo() {
        return video;
    }

    public void setVideo(Boolean video) {
        this.video = video;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }
}







