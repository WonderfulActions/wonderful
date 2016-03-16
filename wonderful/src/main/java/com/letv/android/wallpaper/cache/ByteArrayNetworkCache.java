
package com.letv.android.wallpaper.cache;

import com.letv.android.wallpaper.display.DisplayTask.OnProgressListener;
import com.letv.android.wallpaper.display.NewDisplayTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

public class ByteArrayNetworkCache implements IByteArrayCache {
    private static final int DEFAULT_PROGRESS_BUFFER_LENGTH = 5 * 1024 * 1024;
    private static ByteArrayNetworkCache sInstance;

    private ByteArrayNetworkCache() {
        // do nothing
    }

    static {
        sInstance = new ByteArrayNetworkCache();
    }

    public static ByteArrayNetworkCache getInstance() {
        return sInstance;
    }

    @Override
    public byte[] getByteArray(String url) {
        return getByteArray(url, null);
    }

    public byte[] getByteArray(String url, OnProgressListener onProgressListener) {
        if (url != null) {
            HttpURLConnection connection = CacheUtil.getConnection(url);
            InputStream inputStream = CacheUtil.getInputStream(connection);
            if (inputStream != null) {
                int length = DEFAULT_PROGRESS_BUFFER_LENGTH;
                final int contentLength = connection.getContentLength();
                if (contentLength != -1) {
                    length = contentLength;
                }
                byte[] byteArray = new byte[length];
                byteArray = CacheUtil.getByteArray(inputStream, byteArray, onProgressListener, url);
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return byteArray;
            }
        }
        return null;
    }

    public byte[] getByteArrayNew(String url, NewDisplayTask.OnProgressListener onProgressListener) {
        if (url != null) {
            final HttpURLConnection connection = CacheUtil.getConnection(url);
            if (connection != null) {
                final int length = connection.getContentLength();
                try {
                    final InputStream inputStream = connection.getInputStream();
                    if (inputStream != null) {
                        byte[] byteArray = NewCacheUtil.getByteArray(url, length, inputStream, onProgressListener);
                        inputStream.close();
                        return byteArray;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    public boolean exists(String url) {
        // must in network cahche
        return true;
    }

    @Override
    public boolean cache(String url, byte[] byteArray) {
        // do nothing
        return false;
    }

}
