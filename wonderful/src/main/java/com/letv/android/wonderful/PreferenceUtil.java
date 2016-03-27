package com.letv.android.wonderful;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.letv.android.wonderful.application.WonderfulApplication;
import com.letv.android.wonderful.download.DownloadVideoUtil;
import com.letv.android.wonderful.entity.WonderfulVideo;
import com.letv.android.wonderful.util.LockVideoUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class PreferenceUtil {
    public static final String KEY_HAS_VOLUME = "hasVolume";
    public static final String KEY_NEED_REPEAT = "needRepeat";
    public static final String KEY_HOLD_SHAKE_CHANGE = "holdShakeChange";
    public static final String KEY_LOCK_VIDEO_PATH = "lockVideoPath";
    public static final String KEY_LOCK_ALBUM_PATHS = "lockAlbumPaths";
    // public static final String KEY_LOCK_VIDEO_POSITIONS= "lockVideoPosition";

    public static final int OTHER_SCREEN = -1;
    public static final int HOME_SCREEN = 1;
    public static final int LOCK_SCREEN = 2;
    public static final int BOTH_SCREEN = 3;
    
    // TODO replace with application context
    public static boolean hasVolume(Context context) {
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        final boolean hasVolume = preferences.getBoolean(KEY_HAS_VOLUME, false);
        return hasVolume;
    }

    public static boolean needRepeat(Context context) {
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        final boolean needRepeat = preferences.getBoolean(KEY_NEED_REPEAT, false);
        return needRepeat;
    }
    
    public static void setRepeat(Context context, boolean needRepeat) {
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        final Editor editor = preferences.edit();
        editor.putBoolean(KEY_NEED_REPEAT, needRepeat);
        editor.commit();
    }
    
    public static void switchRepeatState(Context context) {
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        final boolean currentNeedRepeat  = preferences.getBoolean(KEY_NEED_REPEAT, false);
        final boolean newNeedRepeat = !currentNeedRepeat;
        final Editor editor = preferences.edit();
        editor.putBoolean(KEY_NEED_REPEAT, newNeedRepeat);
        editor.commit();
    }
    
    public static boolean holdShakeChange(Context context) {
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        final boolean holdShakeChange = preferences.getBoolean(KEY_HOLD_SHAKE_CHANGE, true);
        return holdShakeChange;
    }
    
    public static void cacheLockVideo(final String url) {
        Log.i(Tags.WONDERFUL_APP, "cacheLockVideo url = " + url);
        // cache lock video path
        final boolean isDiskCacheAvailable = DownloadVideoUtil.isDiskCacheAvailable(url);
        if (isDiskCacheAvailable) {
            final String diskCachePath = DownloadVideoUtil.getCachePath(url);
            Log.i(Tags.WONDERFUL_APP, "cacheLockVideo diskCachePath = " + diskCachePath);


            /*
            PreferenceUtil.setLockVideoPath(diskCachePath);
            // clear other videos
            final ArrayList<String> paths = new ArrayList<String>();
            paths.add(diskCachePath);
            PreferenceUtil.setLockAlbumPaths(paths);
            */

            // only cache on lock video paths

/*

            final ArrayList<String> paths = new ArrayList<String>();
            paths.add(diskCachePath);
            PreferenceUtil.setLockAlbumPaths(paths);
*/

            LockVideoUtil.setLockVideoPath(diskCachePath);






            Toast.makeText(WonderfulApplication.mContext, R.string.set_lock_video_successful, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(WonderfulApplication.mContext, R.string.download_before_set, Toast.LENGTH_SHORT).show();
        }
    }
    
    public static void cacheLockAlbum(final ArrayList<WonderfulVideo> videos) {
        Log.i(Tags.WALLPAPER_ENGINE, "cacheLockAlbum");
        if (videos != null && videos.size() > 0) {
            final ArrayList<String> downloadedPaths = new ArrayList<String>();
            for (int i = 0; i < videos.size(); i++) {
                final String url = videos.get(i).getVideoUrl();
                final boolean isDiskCacheAvailable = DownloadVideoUtil.isDiskCacheAvailable(url);
                if (isDiskCacheAvailable) {
                    // cache lock video path
                    final String diskCachePath = DownloadVideoUtil.getCachePath(url);

                    /*
                    if (downloadedPaths.size() == 0) {
                        PreferenceUtil.setLockVideoPath(diskCachePath);
                    }
                    */

                    // add lock album paths
                    downloadedPaths.add(diskCachePath);
                    Log.i(Tags.WALLPAPER_ENGINE, "cacheLockAlbum url = " + url);
                    Log.i(Tags.WALLPAPER_ENGINE, "cacheLockAlbum path = " + diskCachePath);
                }
            }
            // cache lock album paths
            if (downloadedPaths.size() > 0) {
                // PreferenceUtil.setLockAlbumPaths(downloadedPaths);
                LockVideoUtil.setLockVideoPaths(downloadedPaths);
            }
        }
    }
    
    /*
    private void persistentLockAlbum() {
        // save live video path
        final String downloadedPath = getDownloadedVideoPath();
        Log.i(Tags.DISPLAY_FULL, "persistentLockAlbum downloadedPath = " + downloadedPath);
        PreferenceUtil.setLockVideoPath(this, downloadedPath);
        // persistent video paths
        Log.i(Tags.DISPLAY_FULL, "persistentLockAlbum");
        final ArrayList<String> videoPaths = getLockAlbumPaths();
        PreferenceUtil.setLockAlbumPaths(this, videoPaths);
    }
    
    private String getDownloadedVideoPath() {
        final WonderfulVideo video = mAlbumVideos.get(mPosition);
        final String downloadedPath = DownloadVideoUtil.getCachePath(video.getVideoUrl());
        return downloadedPath;
    }
    
    private ArrayList<String> getLockAlbumPaths() {
        final ArrayList<String> downloadedPaths = new ArrayList<String>();
        for (WonderfulVideo video : mAlbumVideos) {
            final String path = DownloadVideoUtil.generateDownloadPath(video.getVideoUrl());
            final File downloadedFile = new File(path);
            if (path != null && downloadedFile != null && downloadedFile.exists()) {
                Log.i(Tags.DISPLAY_FULL, "persistentLockAlbum lock album path = " + path);
                downloadedPaths.add(path);
            }
        }
        return downloadedPaths;
    }
    */
    
    
    
    public static void setLockVideoPath(String path) {
        Log.i(Tags.WONDERFUL_PREFERENCE, "setLockVideoPath path = " + path);
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(WonderfulApplication.mContext);
        final Editor editor = preferences.edit();
        editor.putString(KEY_LOCK_VIDEO_PATH, path);
        editor.commit();
    }
    
    public static String getLockVideoPath() {
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(WonderfulApplication.mContext);
        Log.i(Tags.WALLPAPER_ENGINE, "getLockVideoPath preferences = " + preferences);
        final String lockVideoPath = preferences.getString(KEY_LOCK_VIDEO_PATH, null);
        Log.i(Tags.WALLPAPER_ENGINE, "getLockVideoPath lockVideoPath = " + lockVideoPath);
        return lockVideoPath;
    }

    public static void setLockAlbumPaths(ArrayList<String> paths) {
        Log.i(Tags.WONDERFUL_APP, "setLockAlbumPaths preferences = " + WonderfulApplication.mPreferences);
        // final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(WonderfulApplication.mContext);
        final Editor editor = WonderfulApplication.mPreferences.edit();
        if (paths != null) {
            final HashSet<String> pathSet = new HashSet<String>();
            for (String path : paths) {
                Log.i(Tags.WONDERFUL_APP, "cache lock album path = " + path);
                pathSet.add(path);
            }
            editor.putStringSet(KEY_LOCK_ALBUM_PATHS, pathSet);
        } else {
            editor.putStringSet(KEY_LOCK_ALBUM_PATHS, null);
        }
        editor.commit();
    }

    public static Set<String> getLockAlbumPaths() {
        // final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(WonderfulApplication.mContext);
        Log.i(Tags.WALLPAPER_ENGINE, "getLockVideoPath preferences = " + WonderfulApplication.mPreferences);
        final Set<String> lockAlbumPaths = WonderfulApplication.mPreferences.getStringSet(KEY_LOCK_ALBUM_PATHS, null);
        for (String path : lockAlbumPaths) {
            Log.i(Tags.WALLPAPER_ENGINE, "lockAlbumPaths path = " + path);
        }
        return lockAlbumPaths;
    }
    
    // =========================================
    public static void registerOnSharedPreferenceChangeListener(Context context, OnSharedPreferenceChangeListener listener) {
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.registerOnSharedPreferenceChangeListener(listener);
    }
    
    public static void unregisterOnSharedPreferenceChangeListener(Context context, OnSharedPreferenceChangeListener listener) {
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.unregisterOnSharedPreferenceChangeListener(listener);
    }
}
