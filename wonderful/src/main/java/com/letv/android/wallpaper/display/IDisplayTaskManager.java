package com.letv.android.wallpaper.display;

import android.widget.ImageView;

import com.letv.android.wallpaper.display.CacheTask.OnCompleteListener;
import com.letv.android.wallpaper.display.CacheTask.OnProgressListener;

public interface IDisplayTaskManager {
    public void display(ImageView imageView, String url, NewDisplayOptions displayOptions, OnCompleteListener onCompleteListener, OnProgressListener onProgressListener, long delay);
    public void cancelDisplay(ImageView imageView);
}
