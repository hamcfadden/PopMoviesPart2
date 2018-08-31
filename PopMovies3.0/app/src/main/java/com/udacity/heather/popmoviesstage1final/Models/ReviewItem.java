package com.udacity.heather.popmoviesstage1final.Models;

import android.os.Parcel;
import android.os.Parcelable;



public class ReviewItem implements Parcelable {

    private String author;
    private String content;
    private String id;
    private String url;

    private ReviewItem(Parcel source) {
        this.author = source.readString();
        this.content = source.readString();
        this.id = source.readString();
        this.url = source.readString();
    }

    @Override
    public int describeContents() {
        return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(author);
        dest.writeString(content);
        dest.writeString(id);
        dest.writeString(url);
    }
    public static final Parcelable.Creator<ReviewItem> CREATOR = new Parcelable.Creator<ReviewItem>() {

        @Override
        public ReviewItem createFromParcel(Parcel source) {
            return new ReviewItem(source);
        }

        @Override
        public ReviewItem[] newArray(int size) {return new ReviewItem[size];}
    };

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public String getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }


    }


