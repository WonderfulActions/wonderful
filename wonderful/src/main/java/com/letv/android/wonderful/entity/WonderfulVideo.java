
package com.letv.android.wonderful.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class WonderfulVideo implements Parcelable {
    // id name detail cover coverRes performer size time videoUrl
    private int id;
    private String name;
    private String detail;
    private String coverUrl;
    // private int coverRes;
    // private WonderfulPerformer performer;
    private long size;
    private long time;
    private String videoUrl;
    

    public WonderfulVideo(int id, String name, String detail, String coverUrl, long size, long time, String videoUrl) {
        this.id = id;
        this.setName(name);
        this.detail = detail;
        this.coverUrl = coverUrl;
        // this.performer = performer;
        this.size = size;
        this.time = time;
        this.setVideoUrl(videoUrl);
    }

    /*
    public int getCoverRes() {
        return coverRes;
    }

    public void setCoverRes(int coverRes) {
        this.coverRes = coverRes;
    }
    */

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    /*
    public WonderfulPerformer getPerformer() {
        return performer;
    }

    public void setPerformer(WonderfulPerformer performer) {
        this.performer = performer;
    }
    */

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    
    
    
    
    // =====================================================================
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(detail);
        dest.writeString(coverUrl);
        dest.writeLong(size);
        dest.writeLong(time);
        dest.writeString(videoUrl);
        dest.writeInt(isDownloaded ? 1 : 0);
    }
    
    public static final Parcelable.Creator<WonderfulVideo> CREATOR = new Creator<WonderfulVideo>() {
        @Override
        public WonderfulVideo[] newArray(int size) {
            return new WonderfulVideo[size];
        }
        
        @Override
        public WonderfulVideo createFromParcel(Parcel source) {
            return new WonderfulVideo(source);
        }
    };
    
    private WonderfulVideo(Parcel parcel) {
        id = parcel.readInt();
        name = parcel.readString();
        detail = parcel.readString();
        coverUrl = parcel.readString();
        size = parcel.readLong();
        time = parcel.readLong();
        videoUrl = parcel.readString();
        isDownloaded = (parcel.readInt() == 1);
    }
    // =====================================================================
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    

    
    // =====================================================================
    private boolean isDownloaded;
    
    public boolean isDownloaded() {
        return isDownloaded;
    }

    public void setDownloaded(boolean isDownloaded) {
        this.isDownloaded = isDownloaded;
        notifyDownloaded(isDownloaded);
    }
    // =====================================================================
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    // =====================================================================
    private ArrayList<DownloadedObserver> mObservers = new ArrayList<WonderfulVideo.DownloadedObserver>();
    
    public interface DownloadedObserver {
        public void isDownloaded(boolean isDownloaded);
    }
    
    private void notifyDownloaded(boolean isDownloaded) {
        for (DownloadedObserver observer : mObservers) {
            observer.isDownloaded(isDownloaded);
        }
    }

    public void addObserver(DownloadedObserver observer) {
        mObservers.add(observer);
    }
    
    public void removeObserver(DownloadedObserver observer) {
        mObservers.remove(observer);
    }
    // =====================================================================

}
