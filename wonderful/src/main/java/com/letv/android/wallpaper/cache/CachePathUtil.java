
package com.letv.android.wallpaper.cache;

import android.content.Context;
import android.os.Environment;

import java.io.File;

public class CachePathUtil {
    public static String CACHE_DIR = Environment.getExternalStorageDirectory().getPath() + "/" + "Android/data/com.letv.android.wonderful/cache";

    public static void initCachePath(Context context) {
        if (context != null) {
            File cacheDir = context.getExternalCacheDir();
            if (cacheDir != null) {
                CACHE_DIR = cacheDir.getAbsolutePath();
            }
        }
    }

}
