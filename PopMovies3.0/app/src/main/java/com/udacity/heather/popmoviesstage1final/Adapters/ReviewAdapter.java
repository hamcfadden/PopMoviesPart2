package com.udacity.heather.popmoviesstage1final.Adapters;

import android.content.Context;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.heather.popmoviesstage1final.Models.ReviewItem;
import com.udacity.heather.popmoviesstage1final.R;
import com.udacity.heather.popmoviesstage1final.databinding.ReviewListItemBinding;


import java.util.List;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewAdapterImageViewHolder>{
    private final static int PREVIEW_LENGTH = 200;
    private List<ReviewItem> mReviewItem;
    private final ReviewAdapterOnClickHandler mClickHandler;
    private ReviewAdapterImageViewHolder holder;
    private ViewGroup parent;
    private int viewType;

    public interface ReviewAdapterOnClickHandler {
        void onClick(ReviewItem reviewItem);
    }

    public ReviewAdapter(ReviewAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    public class ReviewAdapterImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final ReviewListItemBinding mReviewListItemBinding;

        ReviewAdapterImageViewHolder(ReviewListItemBinding binding) {
            super(binding.getRoot());
           this.mReviewListItemBinding = binding;

            mReviewListItemBinding.tvItemReviewContent.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            ReviewItem reviewItem = mReviewItem.get(position);

            mClickHandler.onClick(reviewItem);
        }
    }

    @NonNull
    @Override
    public ReviewAdapterImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.parent = parent;
        this.viewType = viewType;
        ReviewListItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.review_list_item, parent, false);

        return new ReviewAdapterImageViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapterImageViewHolder holder, int position) {
        ReviewItem reviewItem = mReviewItem.get(position);

        if (reviewItem != null)
        holder.mReviewListItemBinding.setReview(reviewItem);
        if (reviewItem != null)
        holder.mReviewListItemBinding.tvItemReviewAuthor.setText(reviewItem.getAuthor());

        String content = null;
        if (reviewItem != null) {
            content = reviewItem.getContent();
        }

        if (content != null) {
            if (content.length() > PREVIEW_LENGTH) {
                Resources resources = holder.itemView.getResources();
                Context context = holder.itemView.getContext();

                content = content.substring(0, PREVIEW_LENGTH);

                SpannableString more = new SpannableString(resources
                        .getString(R.string.detail_content_more));

                more.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context,
                        R.color.colorPrimary)), 0, more.length(), 0);

                SpannableStringBuilder builder = new SpannableStringBuilder();
                builder.append(content);
                builder.append(resources.getString(R.string.detail_content_ellipsis));
                builder.append(" ");
                builder.append(more);

                holder.mReviewListItemBinding.tvItemReviewContent
                        .setText(builder, TextView.BufferType.SPANNABLE);


            } else {
                holder.mReviewListItemBinding.tvItemReviewContent.setText(content);
            }
        }

    }

    @Override
    public int getItemCount() {
        {
            return mReviewItem == null ? 0 : mReviewItem.size();
        }





    }
    public void setReviewData(List<ReviewItem> reviewItem) {
        mReviewItem =(reviewItem) ;

        notifyDataSetChanged();
    }

    }


