package com.udacity.heather.popmoviesstage1final.Database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;


import com.udacity.heather.popmoviesstage1final.Models.PosterItem;

import java.util.List;

@Dao
public interface FavDao {

    @Query("SELECT * FROM favorite ORDER BY title")
    LiveData<List<PosterItem>> loadAllFavorites();

    @Insert
    void insertFavorite(PosterItem favorite);

    @Delete
    void deleteFavorite(PosterItem favorite);

    @Query("SELECT * FROM favorite WHERE id = :id")
    LiveData<PosterItem> loadFavoriteById(int id);
}


