package com.letv.android.wonderful.entity.task;

import android.os.AsyncTask;
import android.util.Log;

import com.letv.android.wonderful.Tags;
import com.letv.android.wonderful.entity.WonderfulAlbum;
import com.letv.android.wonderful.entity.callback.AlbumsCallback;
import com.letv.android.wonderful.server.AlbumsJson;
import com.letv.android.wonderful.util.HttpUrlConnectionUtil;

import java.util.ArrayList;

public class FetchAlbumsTask extends AsyncTask<String, String, ArrayList<WonderfulAlbum>> {
    private AlbumsCallback mCallback;

    public FetchAlbumsTask(AlbumsCallback callback) {
        this.mCallback = callback;
    }

    @Override
    protected ArrayList<WonderfulAlbum> doInBackground(String... urls) {
        final String url = urls[0];
        // final ArrayList<WonderfulAlbum> albums = WonderfulServer.getNBALatestAlbums(url);
        final ArrayList<WonderfulAlbum> albums = getAlbums(url);
        return albums;
    }
    
    public static ArrayList<WonderfulAlbum> getAlbums(String url) {
        Log.i(Tags.WONDERFUL_REQUEST, "wonderful request url = " + url);
        final String albumsJson = HttpUrlConnectionUtil.doGet(url);
        Log.i(Tags.WONDERFUL_REQUEST, "wonderful response result = " + albumsJson);
        final ArrayList<WonderfulAlbum> albums = AlbumsJson.parseAlbums(albumsJson);
        return albums;
    }
    
    @Override
    protected void onPostExecute(ArrayList<WonderfulAlbum> albums) {
        mCallback.onAlbumsResponse(albums);
    }
}
