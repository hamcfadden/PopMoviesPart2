package com.udacity.heather.popmoviesstage1final.Utilities;

import com.google.gson.Gson;
import com.udacity.heather.popmoviesstage1final.Models.TrailerItem;
import com.udacity.heather.popmoviesstage1final.Models.Trailers;

import java.util.ArrayList;
import java.util.List;

public class JsonVideosUtils {
    List<TrailerItem> mTrailerItem;

    public JsonVideosUtils() {
        mTrailerItem = new ArrayList<>();
    }

    public static List<TrailerItem> parse(String json) {
        Gson gson = new Gson();
        Trailers trailers = gson.fromJson(json, Trailers.class);

        return trailers.getResults();
    }
}

