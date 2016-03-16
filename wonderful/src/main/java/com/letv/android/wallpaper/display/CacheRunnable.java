
package com.letv.android.wallpaper.display;

import android.os.Handler;
import android.os.Looper;

import com.letv.android.wallpaper.cache.NewCacheManager;
import com.letv.android.wallpaper.display.CacheTask.OnCompleteListener;
import com.letv.android.wallpaper.display.CacheTask.OnProgressListener;

public class CacheRunnable implements Runnable {
    private static final String INTERRUPTED_BEFORE_GET_BYTEARRAY = "interrupted before get byteArray";
    private static final Handler MAIN_HANDLER = new Handler(Looper.getMainLooper());
    private CacheTask mTask;

    public CacheRunnable(CacheTask cacheTask) {
        this.mTask = cacheTask;
    }

    @Override
    public void run() {
        try {
            // check to delay
            final long delay = mTask.getDelay();
            if (delay > 0) {
                Thread.sleep(delay);
            }
            isInterrupted(INTERRUPTED_BEFORE_GET_BYTEARRAY);
            // display
            cache();
        } catch (InterruptedException e) {
            // notify complete
            postNotifyCacheComplete(null);
        }
    }

    private void cache() throws InterruptedException {
        // get byteArray
        final String url = mTask.getUrl();
        final OnProgressListener onProgressListener = mTask.getOnProgressListener();
        // cache config
        final boolean cache2Memory = mTask.getCacheOptions().isCache2Memory();
        final boolean cache2Disk = mTask.getCacheOptions().isCache2Disk();
        final byte[] byteArray = NewCacheManager.getByteArray(url, onProgressListener, cache2Memory, cache2Disk);
        // notify cache result
        if (byteArray != null) {
            postNotifyCacheComplete(byteArray);
        } else {
            // notify complete
            postNotifyCacheComplete(null);
        }
    }

    public static void isInterrupted(String message) throws InterruptedException {
        if (Thread.interrupted()) {
            throw new InterruptedException(message);
        }
    }

    private void postNotifyCacheComplete(final byte[] byteArray) {
        final OnCompleteListener onCompleteListener = mTask.getOnCompleteListener();
        if (onCompleteListener != null) {
            MAIN_HANDLER.post(new Runnable() {
                @Override
                public void run() {
                    onCompleteListener.onByteArrayComplete(mTask.getUrl(), byteArray);
                }
            });
        }
    }

}
