package com.letv.android.wallpaper.display;

import android.widget.ImageView;

import com.letv.android.wallpaper.display.CacheTask.OnCompleteListener;
import com.letv.android.wallpaper.display.CacheTask.OnProgressListener;

public class DisplayUtil {
    // display
    public static void display(ImageView imageView, String url) {
        if (imageView != null && url != null) {
            display(imageView, url, null, null, null, 0);
        }
    }

    public static void display(ImageView imageView, String url, NewDisplayOptions displayOptions, OnCompleteListener onCompleteListener, OnProgressListener onProgressListener, long delay) {
        if (imageView != null && url != null) {
            NewDisplayTaskManager.getInstance().display(imageView, url, displayOptions, onCompleteListener, onProgressListener, delay);
        }
    }

    public static void cancelDisplay(ImageView imageView) {
        if (imageView != null) {
            NewDisplayTaskManager.getInstance().cancelDisplay(imageView);
        }
    }

    // cache
    public static void cache(String url) {
        if (url != null) {
            cache(url, null, null, null, 0);
        }
    }

    public static void cache(String url, CacheOption cacheOption, OnCompleteListener onCompleteListener, OnProgressListener onProgressListener, long delay) {
        if (url != null) {
            NewDisplayTaskManager.getInstance().cache(url, cacheOption, onCompleteListener, onProgressListener, delay);
        }
    }

    public static void cancelCache(String url) {
        if (url != null) {
            NewDisplayTaskManager.getInstance().cancelCache(url);
        }
    }






    // ==============================================================
    /*
    public static void displayOld(ImageView imageView, String url) {
        display(imageView, url);
    }

    @Deprecated
    public static void displayOld(ImageView imageView, String url, DisplayOptions options) {
        if (options != null) {
            final Options decodeOptions = options.getOptions();
            final ScaleType scaleType = options.getScaleType();
            final int displayShape = options.isRound() ? DisplayShape.ROUND : DisplayShape.ORIGINAL;
            final int displayAnimation = options.getAnimation() > 0 ? DisplayAnimation.FADE_IN : DisplayAnimation.NONE;
            final boolean cache2Memory = options.isCacheByteArrayInMemory();
            final boolean cache2Disk = options.isCacheByteArrayInDisk();
            final NewDisplayOptions displayOptions = new NewDisplayOptions(cache2Memory, cache2Disk, decodeOptions, scaleType, displayShape, displayAnimation);
            final long delay = options.getDelay();
            display(imageView, url, displayOptions, null, null, delay);
        } else {
            display(imageView, url);
        }
    }

    @Deprecated
    public static void displayOld(ImageView imageView, String url, DisplayOptions options
            , final com.letv.android.wallpaper.display.DisplayTask.OnCompleteListener onCompleteListener
            , com.letv.android.wallpaper.display.DisplayTask.OnProgressListener onProgressListener) {
        NewDisplayOptions displayOptions = null;
        long delay = 0;
        if (options != null) {
            final Options decodeOptions = options.getOptions();
            final ScaleType scaleType = options.getScaleType();
            final int displayShape = options.isRound() ? DisplayShape.ROUND : DisplayShape.ORIGINAL;
            final int displayAnimation = options.getAnimation() > 0 ? DisplayAnimation.FADE_IN : DisplayAnimation.NONE;
            final boolean cache2Memory = options.isCacheByteArrayInMemory();
            final boolean cache2Disk = options.isCacheByteArrayInDisk();
            displayOptions = new NewDisplayOptions(cache2Memory, cache2Disk, decodeOptions, scaleType, displayShape, displayAnimation);
            delay = options.getDelay();
        }
        final OnCompleteListener onCompleteListener2 = new OnCompleteListener() {
            @Override
            public void onBitmapComplete(String url, Bitmap bitmap) {
                onCompleteListener.onComplete(null, url, false, bitmap);
            }

            @Override
            public void onByteArrayComplete(String url, byte[] byteArray) {
                // do nothing
            }
        };
        display(imageView, url, displayOptions, onCompleteListener2, null, delay);
    }
     */





    /*
    // ==============================================================
    @Deprecated
    public static void displayIcon(final ImageView imageView, final String url) {
        displayIcon(imageView, url, null);
    }

    @Deprecated
    public static void displayIcon(final ImageView imageView, final String url, final DisplayOptions options) {
        if (imageView != null && url != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    displayIconImpl(imageView, url, options);
                }
            }).start();
        }
    }

    @Deprecated
    private static void displayIconImpl(ImageView imageView, String url, DisplayOptions options) {
        Log.i(Tags.DISPLAY_TASK_MANAGER, "private static showImage");
        // 1 get byteArray
        byte[] byteArray = CacheManager.getByteArray(url);
        if (byteArray == null) {
            return;
        }
        // 2 cache byteArray in disk
        if (CacheManager.getCachePosition(url) != CacheManager.DISK) {
            ByteArrayDiskCache.getInstance().cache(url, byteArray);
        }
        // 3 decode bitmap, but no cache bitmap
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        // 4 show bitmap with shape and animation
        CacheUtil.postSetImage(imageView, bitmap, options);
        WallpaperLog.print("topics", "show postSetImage url = " + url);
    }
    */

}
