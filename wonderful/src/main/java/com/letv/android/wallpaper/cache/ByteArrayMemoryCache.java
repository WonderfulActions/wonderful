
package com.letv.android.wallpaper.cache;

import android.util.LruCache;

public class ByteArrayMemoryCache extends LruCache<String, byte[]> implements IByteArrayCache {
    private static ByteArrayMemoryCache sInstance;

    // max size 1/8 max memory
    static {
        // bytes
        // TODO optimize cache size
        int maxSize = (int) (Runtime.getRuntime().maxMemory() / 16);
        sInstance = new ByteArrayMemoryCache(maxSize);
    }

    public static ByteArrayMemoryCache getInstance() {
        return sInstance;
    }

    private ByteArrayMemoryCache(int maxSize) {
        super(maxSize);
    }

    public void shutdown() {
        evictAll();
    }

    @Override
    protected int sizeOf(String key, byte[] value) {
        if (value != null) {
            return value.length;
        }
        return 0;
    }

    @Override
    protected void entryRemoved(boolean evicted, String key, byte[] oldValue, byte[] newValue) {
        recycleByteArray(oldValue);
    }

    private static void recycleByteArray(byte[] byteArray) {
        byteArray = null;
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
    public boolean cache(String url, byte[] byteArray) {
        if (url == null || byteArray == null) {
            return false;
        }
        if (exists(url)) {
            return true;
        }
        if (url != null && byteArray != null) {
            put(url, byteArray);
            return true;
        }
        return false;
    }

    @Override
    public byte[] getByteArray(String url) {
        if (url != null) {
            byte[] byteArray = get(url);
            if (byteArray != null) {
                return byteArray;
            }
        }
        return null;
    }

    /*
    private void dump() {
//        System.out.println("---------------------------------------------------------------------------------------------------------------");
        int putCount = putCount();
        System.out.println("================= putCount = " + putCount);
//        int createCount = createCount();
//        System.out.println("================= createCount = " + createCount);
//        int hitCount = hitCount();
//        System.out.println("================= hitCount = " + hitCount);
        int maxSize = maxSize();
        System.out.println("================= maxSize bytes = " + maxSize);
//        int missCount = missCount();
//        System.out.println("================= missCount = " + missCount);
        int size = size();
        System.out.println("================= size byte = " + size);
//        int evictCount = evictionCount();
//        System.out.println("================= evictCount = " + evictCount);
//        int evictionCount = evictionCount();
//        System.out.println("evictionCount = " + evictionCount);
//        Map<String, byte[]> map = snapshot();
//        for (Entry<String, byte[]> entry : map.entrySet()) {
//            String key = entry.getKey();
//            System.out.println("key = " + key);
//            byte[] bytes = entry.getValue();
//            System.out.println("bytes = " + bytes.hashCode());
//        }
//        System.out.println("------------------------------------------------------------------------");
    }
    */
}
