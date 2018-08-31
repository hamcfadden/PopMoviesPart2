package com.udacity.heather.popmoviesstage1final.Database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

import com.udacity.heather.popmoviesstage1final.Models.PosterItem;

@Database(entities = {PosterItem.class}, version = 1, exportSchema = false)
public abstract class FavDatabase extends RoomDatabase {

    private static final String TAG = FavDatabase.class.getSimpleName();

    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "favorites";
    private static FavDatabase sInstance;

    public static FavDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(TAG, "Creating new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        FavDatabase.class, FavDatabase.DATABASE_NAME)
                        .build();
            }
        }
        Log.d(TAG, "Getting the database instance");
        return sInstance;
    }

    public abstract FavDao favoriteDao();
}


