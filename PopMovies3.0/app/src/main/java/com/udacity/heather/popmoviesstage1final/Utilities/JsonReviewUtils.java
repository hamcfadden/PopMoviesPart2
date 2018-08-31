package com.udacity.heather.popmoviesstage1final.Utilities;

import com.google.gson.Gson;
import com.udacity.heather.popmoviesstage1final.Models.ReviewItem;
import com.udacity.heather.popmoviesstage1final.Models.Reviews;

import java.util.ArrayList;
import java.util.List;

public class JsonReviewUtils {
    List<ReviewItem> mReviewItem;

    public JsonReviewUtils() { mReviewItem = new ArrayList<>(); }

    public static List<ReviewItem> parse(String json) {
        Gson gson = new Gson();
        Reviews reviews = gson.fromJson(json, Reviews.class);

        return reviews.getResults();
    }
}


