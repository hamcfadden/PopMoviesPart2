package com.udacity.heather.popmoviesstage1final;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.heather.popmoviesstage1final.Adapters.ReviewAdapter;
import com.udacity.heather.popmoviesstage1final.Adapters.VideoAdapter;
import com.udacity.heather.popmoviesstage1final.Adapters.VideoAdapter.VideoAdapterOnClickHandler;
import com.udacity.heather.popmoviesstage1final.Database.FavDatabase;
import com.udacity.heather.popmoviesstage1final.Models.Movie;
import com.udacity.heather.popmoviesstage1final.Models.PosterItem;
import com.udacity.heather.popmoviesstage1final.Models.ReviewItem;
import com.udacity.heather.popmoviesstage1final.Models.TrailerItem;
import com.udacity.heather.popmoviesstage1final.Utilities.ExecUtils;
import com.udacity.heather.popmoviesstage1final.Utilities.JsonMovieUtils;
import com.udacity.heather.popmoviesstage1final.Utilities.JsonReviewUtils;
import com.udacity.heather.popmoviesstage1final.Utilities.JsonVideosUtils;
import com.udacity.heather.popmoviesstage1final.Utilities.Utils;
import com.udacity.heather.popmoviesstage1final.databinding.ActivityDetailBinding;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class DetailActivity extends AppCompatActivity implements VideoAdapter.VideoAdapterOnClickHandler, ReviewAdapter.ReviewAdapterOnClickHandler {

    private final static String MOVIE_ID = "movieId";
    private final static String REVIEW = "ReviewItem";
    private final static String POSTER_URL = "PosterUrl";
    private final static String BACKDROP_URL = "BackdropUrl";
    private final static String SCROLL_POSITION = "ScrollPosition";

    private boolean isFavorited = false;

    private Movie mMovie;
    private FavDatabase mDb;
    private ActivityDetailBinding mBinding;
    private VideoAdapter mVideoAdapter;
    private ReviewAdapter mReviewAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDb = FavDatabase.getInstance(getApplicationContext());

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        setTitle("Movie Details");

        LinearLayoutManager trailerItemLinearLayoutManager = new LinearLayoutManager(this);
        mBinding.rvTrailersList.setLayoutManager(trailerItemLinearLayoutManager);
        mBinding.rvTrailersList.setHasFixedSize(true);
        mBinding.rvTrailersList.setNestedScrollingEnabled(false);

        mVideoAdapter = new VideoAdapter(this);
        mBinding.rvTrailersList.setAdapter(mVideoAdapter);

        LinearLayoutManager reviewLinearLayoutManager = new LinearLayoutManager(this);
        mBinding.rvReviewsList.setLayoutManager(reviewLinearLayoutManager);
        mBinding.rvReviewsList.setHasFixedSize(true);
        mBinding.rvReviewsList.setNestedScrollingEnabled(false);

        mReviewAdapter = new ReviewAdapter(this);
        mBinding.rvReviewsList.setAdapter(mReviewAdapter);

        Intent intent = getIntent();

        if (intent == null) {
            closeOnError();
            return;
        }

        Bundle bundle = intent.getExtras();

        if (bundle == null) {
            closeOnError();
            return;
        }

        int id = bundle.getInt(MOVIE_ID);

        if (id == 0) {
            closeOnError();
            return;
        }

        showLoadingIndicator();

        try {
            fetchMovie(Utils.buildMovieUrl(id));
            fetchTrailers(Utils.buildVideosUrl(id));
            fetchReviews(Utils.buildReviewsUrl(id));
        } catch (IOException e) {
            e.printStackTrace();
        }

        LiveData<PosterItem> favorite = mDb.favoriteDao().loadFavoriteById(id);
        favorite.observe(this, new Observer<PosterItem>() {
            @Override
            public void onChanged(@Nullable PosterItem favorite) {
                if (favorite != null) {
                    isFavorited = true;
                    mBinding.ibFavorite.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), android.R.drawable.btn_star_big_on));
                }
            }
        });
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntArray(SCROLL_POSITION, new int[]{mBinding.svDetail.getScrollX(), mBinding.svDetail.getScrollY()});
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        final int[] scrollPosition = savedInstanceState.getIntArray(SCROLL_POSITION);

        if (scrollPosition != null) {
            mBinding.svDetail.postDelayed(new Runnable() {
                public void run() {
                    mBinding.svDetail.scrollTo(scrollPosition[0], scrollPosition[1]);
                }
            }, 500);
        }
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void showMovieDataView() {
        mBinding.tvDetailErrorMessage.setVisibility(View.INVISIBLE);
        mBinding.pbDetailLoadingIndicator.setVisibility(View.INVISIBLE);
        mBinding.svDetail.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage(int message_id) {
        mBinding.svDetail.setVisibility(View.INVISIBLE);
        mBinding.pbDetailLoadingIndicator.setVisibility(View.INVISIBLE);
        mBinding.tvDetailErrorMessage.setText(message_id);
        mBinding.tvDetailErrorMessage.setVisibility(View.VISIBLE);
    }

    private void showLoadingIndicator() {
        mBinding.tvDetailErrorMessage.setVisibility(View.INVISIBLE);
        mBinding.svDetail.setVisibility(View.INVISIBLE);
        mBinding.pbDetailLoadingIndicator.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(TrailerItem trailerItem) {
        Context context = this;

        String id = trailerItem.getKey();

        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + id));
        try {
            context.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            context.startActivity(webIntent);
        }
    }

    @Override
    public void onClick(ReviewItem reviewItem) {
        Context context = this;

        Class destinationClass = ReviewActivity.class;
        Intent intentToStartReviewActivity = new Intent(context, destinationClass);

        intentToStartReviewActivity.putExtra(REVIEW, reviewItem).putExtra(POSTER_URL, mMovie.getPosterUrl());
        intentToStartReviewActivity.putExtra(BACKDROP_URL, mMovie.getBackdropUrl());
        startActivity(intentToStartReviewActivity);
    }

    public void onToggleFavorite(View view) {
        isFavorited = !isFavorited;
        final PosterItem favorite = new PosterItem(mMovie.getId(), mMovie.getOriginalTitle(), mMovie.getPosterPath());

        if (isFavorited) {
            mBinding.ibFavorite.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), android.R.drawable.btn_star_big_on));

            // add favorite
            ExecUtils.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    mDb.favoriteDao().insertFavorite(favorite);
                }
            });

        } else {
            mBinding.ibFavorite.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), android.R.drawable.btn_star_big_off));

            // delete favorite
            ExecUtils.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    mDb.favoriteDao().deleteFavorite(favorite);

                }
            });
        }
    }

    void fetchMovie(URL url) throws IOException {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                call.cancel();

                DetailActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        showErrorMessage(R.string.main_network_error);
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                @SuppressWarnings("ConstantConditions") final String JsonResults = response.body().string();

                DetailActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        if (JsonResults != null && !JsonResults.equals("")) {
                            showMovieDataView();

                            mMovie = JsonMovieUtils.parse(JsonResults);

                            Picasso.with(DetailActivity.this).load(mMovie.getPosterUrl()).placeholder(R.drawable.ic_launcher_foreground).into(mBinding.ivMoviePoster);
                            Picasso.with(DetailActivity.this)
                                    .load(mMovie.getBackdropUrl())
                                    .placeholder(R.drawable.ic_launcher_background)
                                    .into(mBinding.ivBackdrop);

                            mBinding.tvOriginalTitle.setText(mMovie.getOriginalTitle());
                            mBinding.tvReleaseDate.setText(mMovie.getReleaseDate().substring(0,4));
                            mBinding.tvMovieVoteAverage.setText(mMovie.getVoteAverage().toString());
                            mBinding.tvMovieOverview.setText(mMovie.getOverview());
                        }
                    }

                });


            }
        });
    }


    void fetchTrailers(URL url) throws IOException {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                call.cancel();

                DetailActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showErrorMessage(R.string.main_network_error);
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                @SuppressWarnings("ConstantConditions") final String tmdbResults = response.body().string();

                DetailActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (tmdbResults != null && !tmdbResults.equals("")) {
                            showMovieDataView();

                            List<TrailerItem> trailers = JsonVideosUtils.parse(tmdbResults);
                            mVideoAdapter.setVideoData(trailers);
                        }
                    }
                });

            }
        });
    }

    void fetchReviews(URL url) throws IOException {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                call.cancel();

                DetailActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showErrorMessage(R.string.main_network_error);
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                assert response.body() != null;
                @SuppressWarnings("ConstantConditions") final String tmdbResults = response.body().string();

                DetailActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (tmdbResults != null && !tmdbResults.equals("")) {
                            showMovieDataView();

                            List<ReviewItem> reviews = JsonReviewUtils.parse(tmdbResults);
                            mReviewAdapter.setReviewData(reviews);
                        }

                    }
                });

            }


        });
    }
}

