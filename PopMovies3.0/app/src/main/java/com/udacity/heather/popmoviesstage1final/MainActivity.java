package com.udacity.heather.popmoviesstage1final;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.udacity.heather.popmoviesstage1final.Adapters.MovieRecyclerViewAdapter;
import com.udacity.heather.popmoviesstage1final.Models.FavoritesView;
import com.udacity.heather.popmoviesstage1final.Models.PosterItem;
import com.udacity.heather.popmoviesstage1final.Utilities.JsonPosterUtils;
import com.udacity.heather.popmoviesstage1final.Utilities.Utils;
import com.udacity.heather.popmoviesstage1final.databinding.ActivityMainBinding;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity implements MovieRecyclerViewAdapter.MovieRecyclerViewAdapterOnClickHandler, AdapterView.OnItemSelectedListener {

    private final static String MOVIE_ID = "movieId";
    private final static String SPINNER_POSITION = "mainSpinnerPosition";
    private final static String MAIN_GRID_LAYOUT = "mainGridLayoutState";

    private int mSpinnerPosition;
    private boolean mDataLoading;

    private ActivityMainBinding mBinding;
    private MovieRecyclerViewAdapter mMovieRecyclerViewAdapter;
    private Parcelable mMainGridLayoutSavedState;

    private static final int GRID_NUMBER_OF_COLUMNS_PORTRAIT = 2;
    private static final int GRID_NUMBER_OF_COLUMNS_LANDSCAPE = 3;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

       GridLayoutManager gridLayoutManager;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            gridLayoutManager = new GridLayoutManager(this, GRID_NUMBER_OF_COLUMNS_LANDSCAPE);
        } else {
            gridLayoutManager = new GridLayoutManager(this, GRID_NUMBER_OF_COLUMNS_PORTRAIT);
        }

        mBinding.rvPosterGrid.setLayoutManager(gridLayoutManager);
        mBinding.rvPosterGrid.setHasFixedSize(true);

        mMovieRecyclerViewAdapter = new MovieRecyclerViewAdapter(this);
        mBinding.rvPosterGrid.setAdapter(mMovieRecyclerViewAdapter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(SPINNER_POSITION, mSpinnerPosition);

        mMainGridLayoutSavedState = mBinding.rvPosterGrid.getLayoutManager().onSaveInstanceState();
        outState.putParcelable(MAIN_GRID_LAYOUT, mMainGridLayoutSavedState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        mSpinnerPosition = savedInstanceState.getInt(SPINNER_POSITION);
        mMainGridLayoutSavedState = savedInstanceState.getParcelable(MAIN_GRID_LAYOUT);
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadData(mSpinnerPosition);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_spinner_menu, menu);

        MenuItem item = menu.findItem(R.id.spinner);

        Spinner spinner = (Spinner) item.getActionView();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.spinner_list_item_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        spinner.setSelection(mSpinnerPosition);

        return true;
    }

    private void loadDataFromUrl(URL url) {
        try {
            fetchPosterList(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void setupFavoritesViewModel() {
           FavoritesView viewModel = ViewModelProviders.of(this)
                    .get(FavoritesView.class);
            viewModel.getFavorites().observe(this, new Observer<List<PosterItem>>() {
                @Override
               public void onChanged(@Nullable List<PosterItem> favorites) {


                    if (favorites.isEmpty()) {
                        showErrorMessage(R.string.main_favorites_empty);
                   } else {
                        mMovieRecyclerViewAdapter.setPosterData(favorites);
                        mMovieRecyclerViewAdapter.notifyDataSetChanged();

                        if (mMainGridLayoutSavedState != null) {
                           mBinding.rvPosterGrid.getLayoutManager()
                                    .onRestoreInstanceState(mMainGridLayoutSavedState);
                            mMainGridLayoutSavedState = null;
                       }

                       showPosterDataView();
                  }
                }
           });
       }

    private void loadData(int position) {
        if (!mDataLoading) {
            switch (position) {
                case 0:
                    loadDataFromUrl(Utils.buildPopularMoviesQuery());
                    break;
                case 1:
                    loadDataFromUrl(Utils.buildTopRatedMoviesUrl());
                    break;
                case 2:
                    setupFavoritesViewModel();
                    break;
                default:
                    showErrorMessage(R.string.main_error_message);
                    break;
            }
        }
    }

    private void showPosterDataView() {
        mDataLoading = false;
        mBinding.tvErrorMessage.setVisibility(View.INVISIBLE);
        mBinding.pbLoadingIndicator.setVisibility(View.INVISIBLE);
        mBinding.rvPosterGrid.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage(int message_id) {
        mDataLoading = false;
        mBinding.rvPosterGrid.setVisibility(View.INVISIBLE);
        mBinding.pbLoadingIndicator.setVisibility(View.INVISIBLE);
        mBinding.tvErrorMessage.setText(message_id);
        mBinding.tvErrorMessage.setVisibility(View.VISIBLE);
    }

    private void showLoadingIndicator() {
        mDataLoading = true;
        mBinding.tvErrorMessage.setVisibility(View.INVISIBLE);
        mBinding.rvPosterGrid.setVisibility(View.INVISIBLE);
        mBinding.pbLoadingIndicator.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(PosterItem posterItem) {
        Context context = this;
        Class destinationClass = DetailActivity.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);

        intentToStartDetailActivity.putExtra(MOVIE_ID, posterItem.getId());
        startActivity(intentToStartDetailActivity);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mSpinnerPosition = position;
        if (mMainGridLayoutSavedState == null) {
           mBinding.rvPosterGrid.getLayoutManager().scrollToPosition(0);
        }
        loadData(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    void fetchPosterList(URL url) throws IOException {

        showLoadingIndicator();

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                call.cancel();

                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showErrorMessage(R.string.main_network_error);
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                final String tmdbResults = response.body().string();

                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (tmdbResults != null && !tmdbResults.equals("")) {

                            List<PosterItem> posterItem = JsonPosterUtils.parse(tmdbResults);
                            mMovieRecyclerViewAdapter.setPosterData(posterItem);
                            mMovieRecyclerViewAdapter.notifyDataSetChanged();

                            if (mMainGridLayoutSavedState != null) {
                                mBinding.rvPosterGrid.getLayoutManager().onRestoreInstanceState(mMainGridLayoutSavedState);
                                mMainGridLayoutSavedState = null;
                            }

                            showPosterDataView();

                        }
                    }
                });

            }

        });
    }
    }

