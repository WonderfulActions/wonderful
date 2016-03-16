
package com.letv.android.wallpaper.cache;

import android.graphics.Bitmap;
import android.util.LruCache;

public class BitmapMemoryCache extends LruCache<String, Bitmap> implements IBitmapCache {
    private static BitmapMemoryCache sInstance;

    // max size 1/8 max memory
    static {
        // bytes
        // TODO optimize cache size
        int maxSize = (int) (Runtime.getRuntime().maxMemory() / 16);
        sInstance = new BitmapMemoryCache(maxSize);
    }

    public static BitmapMemoryCache getInstance() {
        return sInstance;
    }

    private BitmapMemoryCache(int maxSize) {
        super(maxSize);
    }

    @Override
    protected int sizeOf(String key, Bitmap bitmap) {
        if (bitmap != null) {
            return bitmap.getAllocationByteCount();
        }
        return 0;
    }

    @Override
    protected void entryRemoved(boolean evicted, String key, Bitmap oldBitmap, Bitmap newBitmap) {
        recycleBitmap(oldBitmap);
    }

    private static void recycleBitmap(Bitmap bitmap) {
        bitmap = null;
        // Runtime.getRuntime().gc();
//        System.gc();
        CacheUtil.gc();
//        MemoryUtil.printMemory();
    }

    @Override
    public boolean exists(String url) {
        if (get(url) != null) {
            return true;
        }
        return false;
    }

    @Override
    public boolean cache(String url, Bitmap bitmap) {
        if (url == null || bitmap == null) {
            return false;
        }
        if (exists(url)) {
            return true;
        }
        if (url != null && bitmap != null) {
            put(url, bitmap);
            return true;
        }
        return false;
    }

    @Override
    public Bitmap getBitmap(String url) {
        if (url != null) {
            Bitmap bitmap = get(url);
            if (bitmap != null) {
                return bitmap;
            }
        }
        return null;
    }

}
