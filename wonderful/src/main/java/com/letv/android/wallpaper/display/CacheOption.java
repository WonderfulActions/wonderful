
package com.letv.android.wallpaper.display;

public class CacheOption {
    // cache
    protected final boolean cache2Memory;
    protected final boolean cache2Disk;

    // default cache
    public static CacheOption generateDefaultCacheOption() {
        final boolean cache2Memory = false;
        final boolean cache2Disk = true;
        return new CacheOption(cache2Memory, cache2Disk);
    }

    public CacheOption(boolean cache2Memory, boolean cache2Disk) {
        this.cache2Memory = cache2Memory;
        this.cache2Disk = cache2Disk;
    }

    public boolean isCache2Memory() {
        return cache2Memory;
    }

    public boolean isCache2Disk() {
        return cache2Disk;
    }
}
