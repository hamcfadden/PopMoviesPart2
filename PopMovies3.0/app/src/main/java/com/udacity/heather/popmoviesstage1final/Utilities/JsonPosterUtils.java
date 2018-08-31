package com.udacity.heather.popmoviesstage1final.Utilities;

import com.google.gson.Gson;
import com.udacity.heather.popmoviesstage1final.Models.PosterItem;
import com.udacity.heather.popmoviesstage1final.Models.Posters;

import java.util.ArrayList;
import java.util.List;

public class JsonPosterUtils {
    List<PosterItem> mPosterItem;

    public JsonPosterUtils() {
        mPosterItem = new ArrayList<>();
    }

    public static List<PosterItem> parse(String json) {
        Gson gson = new Gson();
        Posters posters = gson.fromJson(json, Posters.class);

        return posters.getResults();
    }
}

