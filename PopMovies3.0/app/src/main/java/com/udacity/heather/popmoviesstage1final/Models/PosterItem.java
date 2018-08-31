package com.udacity.heather.popmoviesstage1final.Models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;


import static com.udacity.heather.popmoviesstage1final.Utilities.Utils.buildImageBaseUrl;

@Entity(tableName = "favorite")
public class PosterItem {

    private final static String POSTER_WIDTH = "w500";

    @PrimaryKey
    private int id;
    private String title;
    private String poster_path;


    public PosterItem(int id, String title, String poster_path) {
        this.id = id;
        this.title = title;
        this.poster_path = poster_path;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public String getUrl() {
        return buildImageBaseUrl(POSTER_WIDTH, poster_path).toString();
    }
}

