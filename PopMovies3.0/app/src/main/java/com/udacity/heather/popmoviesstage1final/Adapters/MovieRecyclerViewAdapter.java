package com.udacity.heather.popmoviesstage1final.Adapters;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;
import com.udacity.heather.popmoviesstage1final.Models.PosterItem;
import com.udacity.heather.popmoviesstage1final.R;
import com.udacity.heather.popmoviesstage1final.databinding.MainListItemBinding;


import java.util.List;

public class MovieRecyclerViewAdapter extends RecyclerView.Adapter<MovieRecyclerViewAdapter.MovieRecyclerViewAdapterImageViewHolder> {

    private List<PosterItem> mPosterItem;

    private final MovieRecyclerViewAdapterOnClickHandler mClickHandler;

    public interface MovieRecyclerViewAdapterOnClickHandler {
        void onClick(PosterItem posterItem);
    }

    public MovieRecyclerViewAdapter(MovieRecyclerViewAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    public class MovieRecyclerViewAdapterImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final MainListItemBinding mMainListItemBinding;

        MovieRecyclerViewAdapterImageViewHolder(MainListItemBinding mainListItemBinding) {
            super(mainListItemBinding.getRoot());
            mMainListItemBinding = mainListItemBinding;

            mMainListItemBinding.ivMoviePoster.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            PosterItem posterItem = mPosterItem.get(adapterPosition);
            mClickHandler.onClick(posterItem);
        }
    }


    @Override
    public MovieRecyclerViewAdapterImageViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        MainListItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.main_list_item, parent, false);

        return new MovieRecyclerViewAdapterImageViewHolder(binding);
    }

    @Override
    public void onBindViewHolder( MovieRecyclerViewAdapterImageViewHolder holder, int position) {
        PosterItem posterItem = mPosterItem.get(position);

        holder.mMainListItemBinding.setPoster(posterItem);

        Picasso.with(holder.itemView.getContext())
                .load(posterItem.getUrl())
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(holder.mMainListItemBinding.ivMoviePoster);
    } 

    @Override
    public int getItemCount() {
        return mPosterItem == null ? 0 : mPosterItem.size(); }

        public void setPosterData(List<PosterItem> posterItem) {
        mPosterItem = posterItem;
        notifyDataSetChanged();
        }
    }

