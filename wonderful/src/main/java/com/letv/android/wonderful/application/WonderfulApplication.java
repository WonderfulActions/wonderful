package com.letv.android.wonderful.application;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.letv.android.wonderful.Tags;

import java.io.File;

public class WonderfulApplication extends Application {
    public static Context mContext;
    public static File EXTERNAl_CACHE_DIR;
    public static final String CACHE_PATH = "/sdcard/Android/data/com.letv.android.wonderful/cache/";
    public static int WIDTH;
    public static int HEIGHT;
    
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        final int width = getResources().getDisplayMetrics().widthPixels;
        final int height = getResources().getDisplayMetrics().heightPixels;
        WIDTH = width;
        HEIGHT = height;
        Log.i(Tags.WONDERFUL_APP, "screen width = " + WIDTH);
        Log.i(Tags.WONDERFUL_APP, "screen height = " + HEIGHT);
        final File externalCacheDir = getExternalCacheDir();
        if (externalCacheDir == null) {
            final File dir = new File(CACHE_PATH);
            dir.mkdirs();
        }
        EXTERNAl_CACHE_DIR = externalCacheDir;
        if (externalCacheDir != null) {
            Log.i(Tags.WONDERFUL_APP, "EXTERNAl_CACHE_DIR path = " + externalCacheDir.getAbsolutePath());
        }
    }
}
