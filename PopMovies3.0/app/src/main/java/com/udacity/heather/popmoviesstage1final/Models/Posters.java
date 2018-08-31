package com.udacity.heather.popmoviesstage1final.Models;

import java.util.List;

public class Posters {
    public Integer page;
    public Integer totalResults;
    public Integer totalPages;
    public List<PosterItem> results = null;

    public List<PosterItem> getResults() {
        return results;
    }

}

