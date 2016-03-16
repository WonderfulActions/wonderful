package com.letv.android.wonderful.server;

import android.util.Log;

import com.letv.android.wonderful.Tags;
import com.letv.android.wonderful.entity.WonderfulAlbum;
import com.letv.android.wonderful.util.HttpUrlConnectionUtil;

import java.util.ArrayList;

public class WonderfulServer {
    /*
    public static ArrayList<WonderfulAlbum> getNBALatestAlbums(String url) {
        final String albumsJson = HttpUrlConnectionUtil.doGet(WonderfulUrl.NBA_LATEST_ALBUMS);
        final ArrayList<WonderfulAlbum> albums = AlbumsJson.parseAlbums(albumsJson);
        return albums;
    }
    */
    
    public static ArrayList<WonderfulAlbum> getAlbums(String url) {
        Log.i(Tags.WONDERFUL_REQUEST, "wonderful request url = " + url);
        final String albumsJson = HttpUrlConnectionUtil.doGet(url);
        final ArrayList<WonderfulAlbum> albums = AlbumsJson.parseAlbums(albumsJson);
        return albums;
    }
}
