
package com.letv.android.wallpaper.cache;

import android.util.Log;

import com.letv.android.wallpaper.display.CacheTask.OnProgressListener;
import com.letv.android.wonderful.Tags;

public class NewCacheManager {
    public static final int MEMORY = 0;
    public static final int DISK = 1;
    public static final int NETWORK = 2;
    public static final int NONE = 3;

    public static boolean hasCache(String url) {
        final int position = getCachePosition(url);
        if (position == MEMORY || position == DISK || position == NETWORK) {
            return true;
        }
        return false;
    }

    public static byte[] getByteArray(String url) {
        return getByteArray(url, null, false, false);
    }

    public static byte[] getByteArray(String url, OnProgressListener onProgressListener, boolean cache2Memory, boolean cache2Disk) {
        // get position
        final int cachePosition = getCachePosition(url);
        Log.i(Tags.DISPLAY_IMAGE_MANAGER, "cachePosition = " + cachePosition);
        Log.i(Tags.DISPLAY_IMAGE_MANAGER, "url = " + url);
        if (cachePosition == NONE) {
            return null;
        }
        // get byteArray
        final byte[] byteArray = getByteArrayFromCache(cachePosition, url, onProgressListener);
        // cache byteArray
        if (cache2Memory && cachePosition != MEMORY) {
            ByteArrayMemoryCache.getInstance().cache(url, byteArray);
        }
        if (cache2Disk && cachePosition != DISK) {
            ByteArrayDiskCache.getInstance().cache(url, byteArray);
        }
        // return byteArray
        return byteArray;
    }

    private static int getCachePosition(String url) {
        if (url != null) {
            if (ByteArrayMemoryCache.getInstance().exists(url)) {
                return MEMORY;
            }
            if (ByteArrayDiskCache.getInstance().exists(url)) {
                return DISK;
            }
            return NETWORK;
        }
        return NONE;
    }

    private static byte[] getByteArrayFromCache(int position, String url, OnProgressListener onProgressListener) {
        switch (position) {
            case MEMORY:
                return ByteArrayMemoryCache.getInstance().getByteArray(url);
            case DISK:
                return ByteArrayDiskCache.getInstance().getByteArray(url);
            case NETWORK:
                return ByteArrayNetworkCache.getInstance().getByteArrayNew(url, onProgressListener);
            default:
                return null;
        }
    }

}
