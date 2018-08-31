package com.udacity.heather.popmoviesstage1final.Adapters;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.heather.popmoviesstage1final.Models.TrailerItem;
import com.udacity.heather.popmoviesstage1final.R;
import com.udacity.heather.popmoviesstage1final.databinding.VideoListItemBinding;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoAdapterImageViewHolder> {
    private List<TrailerItem> mTrailerItem;

    private final VideoAdapterOnClickHandler mClickHandler;

    public interface VideoAdapterOnClickHandler {
        void onClick(TrailerItem trailerItem);
    }

    public VideoAdapter(VideoAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    public class VideoAdapterImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final VideoListItemBinding mVideoListItemBinding;

        VideoAdapterImageViewHolder(VideoListItemBinding videoListItemBinding) {
            super(videoListItemBinding.getRoot());
            mVideoListItemBinding = videoListItemBinding;

            mVideoListItemBinding.tvItemVideoName.setOnClickListener(this);
            mVideoListItemBinding.ivItemPlayButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            TrailerItem trailerItem = mTrailerItem.get(adapterPosition);
            mClickHandler.onClick(trailerItem);

        }
    }

    @NonNull
    @Override
    public VideoAdapterImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        VideoListItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.video_list_item, parent, false);

        return new VideoAdapterImageViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoAdapterImageViewHolder holder, int position) {
        TrailerItem trailerItem = mTrailerItem.get(position);

        holder.mVideoListItemBinding.setVideo(trailerItem);
        holder.mVideoListItemBinding.tvItemVideoName.setText(trailerItem.getName());
    }

    @Override
    public int getItemCount() {
        return mTrailerItem == null ? 0 : mTrailerItem.size();
    }

    public void setVideoData(List<TrailerItem> videos) {
        mTrailerItem = videos;
        notifyDataSetChanged();
    }
}

