package com.letv.android.wonderful.application;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.letv.android.wonderful.Tags;

import java.io.File;

public class WonderfulApplication extends Application {
    public static final String CACHE_PATH = "/sdcard/Android/data/com.letv.android.wonderful/cache/";

    public static Context mContext;
    public static File mCacheDir;
    public static int mDisplayWidth;
    public static int mDisplayHeight;
    public static SharedPreferences mPreferences;
    
    @Override
    public void onCreate() {
        super.onCreate();
        cacheContext();
        cacheDimensions();
        cacheDownloadDir();
        cachePreferences();
    }

    private void cacheDownloadDir() {
        final File externalCacheDir = getExternalCacheDir();
        if (externalCacheDir == null) {
            final File dir = new File(CACHE_PATH);
            dir.mkdirs();
        }
        mCacheDir = externalCacheDir;
        if (externalCacheDir != null) {
//            Log.i(Tags.WONDERFUL_APP, "mCacheDir path = " + externalCacheDir.getAbsolutePath());
        }
    }

    private void cacheDimensions() {
        final int width = getResources().getDisplayMetrics().widthPixels;
        final int height = getResources().getDisplayMetrics().heightPixels;
        mDisplayWidth = width < height ? width : height;
        mDisplayHeight = height > width ? height : width;
//        Log.i(Tags.WONDERFUL_APP, "screen width = " + mDisplayWidth);
//        Log.i(Tags.WONDERFUL_APP, "screen height = " + mDisplayHeight);
    }

    private void cacheContext() {
        mContext = this;
    }

    private void cachePreferences() {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    }
}
