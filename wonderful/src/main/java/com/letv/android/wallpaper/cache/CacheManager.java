
package com.letv.android.wallpaper.cache;

import com.letv.android.wallpaper.display.DisplayTask.OnProgressListener;

public class CacheManager {
    public static final int MEMORY = 0;
    public static final int DISK = 1;
    public static final int NETWORK = 2;
    public static final int NONE = 3;

    public static boolean hasCache(String url) {
        int position = CacheManager.getCachePosition(url);
        if (position == CacheManager.DISK || position == CacheManager.MEMORY) {
            return true;
        }
        return false;
    }

    public static byte[] getByteArray(String url) {
        return getByteArray(url, null);
    }

    public static byte[] getByteArray(String url, OnProgressListener onProgressListener) {
        int position = CacheManager.getCachePosition(url);
        byte[] byteArray = CacheManager.getByteArrayFromCache(url, position, onProgressListener);
        return byteArray;
    }

    public static int getCachePosition(String url) {
        if (url != null && url.length() > 5) {
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

    public static byte[] getByteArrayFromCache(String url, int position, OnProgressListener onProgressListener) {
        switch (position) {
            case MEMORY:
                return ByteArrayMemoryCache.getInstance().getByteArray(url);
            case DISK:
                if (onProgressListener == null) {
                    return ByteArrayDiskCache.getInstance().getByteArray(url);
                } else {
                    return ByteArrayDiskCache.getByteArray(url, onProgressListener);
                }
            case NETWORK:
                if (onProgressListener == null) {
                    return ByteArrayNetworkCache.getInstance().getByteArray(url);
                } else {
                    return ByteArrayNetworkCache.getInstance().getByteArray(url, onProgressListener);
                }
            default:
                return null;
        }
    }
}
