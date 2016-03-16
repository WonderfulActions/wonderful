package com.letv.android.wallpaper.cache;

import com.letv.android.wallpaper.display.CacheTask.OnProgressListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class NewCacheUtil {
    private static final int BUFFER_SIZE = 50 * 1024;
    private static final String INTERRUPTED_BEFORE_READ_BYTEARRAY = "interrupted before read byteArray";

    public static byte[] getByteArray(String url, int contentLength, InputStream inputStream, OnProgressListener onProgressListener) {
        if (inputStream != null) {
            final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            final byte[] buffer = new byte[BUFFER_SIZE];
            int total = 0;
            int length = -1;
            try {
                while ( (length = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, length);
                    total += length;
                    notifyProgress(onProgressListener, url, contentLength, total);
                    // is interrupted
                    if (isInterrupted()) {
                        outputStream.close();
                        return null;
                    }
                }
                outputStream.close();
                final byte[] byteArray = outputStream.toByteArray();
                return byteArray;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static boolean isInterrupted() {
        final boolean isInterrupted = Thread.interrupted();
        if (isInterrupted) {
        }
        return isInterrupted;
    }

    private static void notifyProgress(OnProgressListener onProgressListener, String url, int contentLength, int progress) {
        if (onProgressListener != null) {
            onProgressListener.onProgressUpdate(url, contentLength, progress);
        }
    }
}
