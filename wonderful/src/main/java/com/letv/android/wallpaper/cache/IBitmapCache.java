package com.letv.android.wallpaper.cache;

import android.graphics.Bitmap;

public interface IBitmapCache {
    public boolean exists(String url);
    public boolean cache(String url, Bitmap bitmap);
    public Bitmap getBitmap(String url);
}
