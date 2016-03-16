
package com.letv.android.wonderful.entity;

import java.util.ArrayList;

public class WonderfulAlbum {
    // id name detail coverUrl size
    private int id;
    private String name;
    private String detail;
    private String coverUrl;
    // private int count;
    private ArrayList<WonderfulVideo> videos;

    public WonderfulAlbum(int id, String name, String detail, String coverUrl, ArrayList<WonderfulVideo> videos) {
        this.id = id;
        this.name = name;
        this.detail = detail;
        this.coverUrl = coverUrl;
        // this.count = count;
        this.videos = videos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
    */

    public ArrayList<WonderfulVideo> getVideos() {
        return videos;
    }

    public void setVideos(ArrayList<WonderfulVideo> videos) {
        this.videos = videos;
    }

}
