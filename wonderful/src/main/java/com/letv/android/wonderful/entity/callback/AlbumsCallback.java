package com.letv.android.wonderful.entity.callback;

import com.letv.android.wonderful.entity.WonderfulAlbum;

import java.util.ArrayList;

public interface AlbumsCallback {
    public void onAlbumsResponse(ArrayList<WonderfulAlbum> albums);
}
