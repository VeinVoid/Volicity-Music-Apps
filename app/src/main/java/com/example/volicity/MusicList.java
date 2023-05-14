package com.example.volicity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class MusicList implements Parcelable {

    String path;
    String title;
    String duration;
    String cover;
    String singer;
    String musicLink;
    int id;
    int viewCount;

    protected MusicList(Parcel in) {
        path = in.readString();
        title = in.readString();
        duration = in.readString();
        cover = in.readString();
        singer = in.readString();
        musicLink = in.readString();
        viewCount = in.readInt();
        id = in.readInt();
    }

    protected MusicList() {

    }

    public static final Creator<MusicList> CREATOR = new Creator<MusicList>() {
        @Override
        public MusicList createFromParcel(Parcel in) {
            return new MusicList(in);
        }

        @Override
        public MusicList[] newArray(int size) {
            return new MusicList[size];
        }
    };

    public String getMusicLink() {
        return musicLink;
    }

    public void setMusicLink(String musicLink) {
        this.musicLink = musicLink;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(path);
        dest.writeString(title);
        dest.writeString(duration);
        dest.writeString(cover);
        dest.writeString(singer);
        dest.writeString(musicLink);
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
