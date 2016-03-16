package com.letv.android.wallpaper.display;

import com.letv.android.wallpaper.display.CacheTask.OnCompleteListener;
import com.letv.android.wallpaper.display.CacheTask.OnProgressListener;

public interface ICacheTaskManager {
    public void cache(String url, CacheOption cacheOption, OnCompleteListener onCompleteListener, OnProgressListener onProgressListener, long delay);
    public void cancelCache(String url);
}
