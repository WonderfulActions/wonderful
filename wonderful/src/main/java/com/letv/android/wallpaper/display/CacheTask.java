
package com.letv.android.wallpaper.display;

import android.graphics.Bitmap;

public class CacheTask {
    public interface OnCompleteListener {
        // callback when display bitmap complete
        void onBitmapComplete(String url, Bitmap bitmap);
        // callback when cache byteArray complete
        void onByteArrayComplete(String url, byte[] byteArray);
    }

    public interface OnProgressListener {
        void onProgressUpdate(String url, int length, int progress);
    }

    private final String url;
    protected final CacheOption cacheOption;
    private final OnCompleteListener onCompleteListener;
    private final OnProgressListener onProgressListener;
    private final long delay;

    private volatile boolean isCancelled;

    public CacheTask(String url, CacheOption cacheOption, OnCompleteListener onCompleteListener, OnProgressListener onProgressListener, long delay) {
        this.url = url;
        if (cacheOption == null) {
            cacheOption = CacheOption.generateDefaultCacheOption();
        }
        this.cacheOption = cacheOption;
        this.onCompleteListener = onCompleteListener;
        this.onProgressListener = onProgressListener;
        if (delay < 0) {
            delay = 0;
        }
        this.delay = delay;
    }

    @Override
    public boolean equals(Object o) {
        if (o != null && o instanceof NewDisplayTask) {
            final NewDisplayTask task = (NewDisplayTask) o;
            if (getUrl() != null && task.getUrl() != null) {
                if (getUrl().equals(task.getUrl())) {
                    return true;
                }
            }
        }
        return false;
    }

    public OnCompleteListener getOnCompleteListener() {
        return onCompleteListener;
    }

    public OnProgressListener getOnProgressListener() {
        return onProgressListener;
    }

    public CacheOption getCacheOptions() {
        return cacheOption;
    }

    public String getUrl() {
        return url;
    }

    public long getDelay() {
        return delay;
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    public void setCancelled(boolean isCancelled) {
        this.isCancelled = isCancelled;
    }
}
