package com.udacity.heather.popmoviesstage1final.Models;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.udacity.heather.popmoviesstage1final.Database.FavDatabase;

import java.util.List;

public class FavoritesView extends AndroidViewModel{
    private LiveData<List<PosterItem>> mFavorites;

    public FavoritesView(@NonNull Application application) {
        super(application);

        FavDatabase favDatabase = FavDatabase.getInstance(this.getApplication());
        mFavorites = favDatabase.favoriteDao().loadAllFavorites();
    }

    public LiveData<List<PosterItem>> getFavorites() {
        return mFavorites;
    }
}

