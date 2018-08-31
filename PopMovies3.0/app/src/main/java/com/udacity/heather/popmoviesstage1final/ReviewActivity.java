package com.udacity.heather.popmoviesstage1final;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.heather.popmoviesstage1final.databinding.ActivityReviewBinding;
import com.udacity.heather.popmoviesstage1final.Models.ReviewItem;


public class ReviewActivity extends AppCompatActivity {

private final static String EXTRA_REVIEW = "ReviewItem";
private final static String POSTER_URL = "PosterUrl";
private final static String BACKDROP_URL = "BackdropUrl";
private final static String SCROLL_POSITION = "reviewScrollPosition";

private ActivityReviewBinding mBinding;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_review);

    setTitle(R.string.review_title);

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

    ReviewItem reviewItem = bundle.getParcelable(EXTRA_REVIEW);
    String posterUrl = bundle.getString(POSTER_URL);
    String backdropUrl = bundle.getString(BACKDROP_URL);


    Picasso.with(this).load(posterUrl).placeholder(R.drawable.ic_launcher_foreground).into(mBinding.ivMoviePoster);

    Picasso.with(this).load(backdropUrl).placeholder(R.drawable.ic_launcher_background).into(mBinding.ivBackdrop);

    if (reviewItem != null) {
        mBinding.tvReviewAuthor.setText(reviewItem.getAuthor());
    }
    if (reviewItem != null) {
        mBinding.tvReviewContent.setText(reviewItem.getContent());

}

}


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntArray(SCROLL_POSITION, new int[]{ mBinding.svReview.getScrollX(),
                mBinding.svReview.getScrollY()});
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        final int[] scrollPosition = savedInstanceState.getIntArray(SCROLL_POSITION);

        if(scrollPosition != null) {
            mBinding.svReview.post(new Runnable() {
                public void run() {
                    mBinding.svReview.scrollTo(scrollPosition[0], scrollPosition[1]);
                }

            });

        }
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }
}



