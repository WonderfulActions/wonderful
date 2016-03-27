
package com.letv.android.wonderful.download;

import android.util.Log;

import com.letv.android.wallpaper.display.DisplayTask.OnProgressListener;
import com.letv.android.wonderful.Tags;
import com.letv.android.wonderful.application.WonderfulApplication;
import com.letv.android.wonderful.download.DownloadVideoTask.ProgressCallback;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DownloadVideoUtil {
    public static final String NO_MEDIA = ".nomedia";
    public static final int FETCH_WALLPAPER = 10000;
    public static final int BUFFER_SIZE = 1024 * 5;
    /*
    wonderfulImage
    wonderfulAlbumCover
    wonderfulVideoCover
    */

    public static String generateDownloadPath(String url) {
        /*
        Log.i(Tags.WONDERFUL_VIDEO, "generateDownloadPath url = " + url);
        Log.i(Tags.WONDERFUL_VIDEO, "generateDownloadPath id = " + id);
        */
        // get external cache path
        // final File externalCacheDir = context.getApplicationContext().getExternalCacheDir();
        // final String cacheDirPath = externalCacheDir.getAbsolutePath();
        final String cacheDirPath = WonderfulApplication.mCacheDir.getAbsolutePath() + File.separator + "video";
        final String fileName = "url" + url.hashCode() + ".wonderfulVideo";
        final String cacheFilePath = cacheDirPath + File.separator + fileName;
        // Log.i(Tags.WONDERFUL_VIDEO, "generateDownloadPath cache path = " + cacheFilePath);
        return cacheFilePath;
    }
    
    public static String getCachePath(String url) {
        final String cachePath = generateDownloadPath(url);
        final File availableFile = new File(cachePath);
        if (availableFile != null && availableFile.exists() && availableFile.isFile()) {
            return cachePath;
        }
        return url;
    }
    
    public static boolean isDiskCacheAvailable(String url) {
        final String cachePath = generateDownloadPath(url);
        final File availableFile = new File(cachePath);
        if (availableFile != null && availableFile.exists() && availableFile.isFile()) {
            return true;
        }
        return false;
    }

    public static boolean downloadFile(String path, String urlStr, ProgressCallback progressCallback) {
        final File file = new File(path);
        boolean isSuccess = checkOrMkDir(file.getParentFile());
        if (isSuccess) {
            try {
                URL url = new URL(urlStr);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(FETCH_WALLPAPER);
                connection.setDoInput(true);
                BufferedInputStream inputStream = new BufferedInputStream(connection.getInputStream());
                final int contentLength = connection.getContentLength();
                Log.i(Tags.WONDERFUL_VIDEO, "contentLength = " + contentLength);
                int total = 0;
                int len = -1;
                byte[] buffer = new byte[BUFFER_SIZE];
                FileOutputStream outputStream = new FileOutputStream(path);
                while ((len = inputStream.read(buffer)) != -1) {
                    // Log.i(Tags.WONDERFUL_VIDEO, "len = " + len);
                    outputStream.write(buffer, 0, len);
                    total += len;
                    if (contentLength > 0) {
                        // get progress
                        final int progress = total * 100 / contentLength;
                        progressCallback.onProgressUpdate(progress);
                    }
                }
                inputStream.close();
                outputStream.flush();
                outputStream.close();
                connection.disconnect();
                Log.i(Tags.WONDERFUL_VIDEO, "total = " + total);
                if (file != null && contentLength > 0 && file.length() == contentLength && total == contentLength) {
                    return true;
                }
            } catch (MalformedURLException e) {
            } catch (IOException e) {
            }
            if (file != null && file.exists()) {
                file.delete();
            }
        }
        return false;
    }

    public static boolean checkOrMkDir(File file) {
        if (file != null && file.exists() && file.isDirectory()) {
            File noMedia = new File(file, NO_MEDIA);
            if (!noMedia.exists()) {
                createNoMedia(file);
            }
            return true;
        } else {
            boolean result = file.mkdirs();
            if (result) {
                createNoMedia(file);
            }
            return result;
        }
    }

    public static boolean createNoMedia(File dir) {
        final File noMedia = new File(dir, NO_MEDIA);
        try {
            return noMedia.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static byte[] getByteArray(InputStream inputStream, byte[] byteArray, OnProgressListener onProgressListener, String url) {
        if (inputStream != null) {
            try {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[50 * 1024];
                int total = 0;
                int len = -1;
                while ((len = inputStream.read(buffer)) != -1) {
                    if (Thread.interrupted()) {
                        outputStream.close();
                        return null;
                    }
                    outputStream.write(buffer, 0, len);
                    System.arraycopy(buffer, 0, byteArray, total, len);
                    total += len;

                    // onProgressUpdate
                    notifyProgress(onProgressListener, byteArray, total, false);
                }
                notifyProgress(onProgressListener, byteArray, total, true);
                byteArray = outputStream.toByteArray();
                outputStream.close();
                return byteArray;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static void notifyProgress(OnProgressListener onProgressListener, byte[] byteArray, int total, boolean isEnd) {
        if (onProgressListener != null) {
            onProgressListener.onProgressUpdate(byteArray, 0, total, isEnd);
        }
    }
}
