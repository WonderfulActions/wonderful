
package com.letv.android.wonderful.download;

import java.io.File;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DownloadVideoManager {
    private static Executor mSingleThreadExecutor = Executors.newSingleThreadExecutor();

    public static void downloadVideo(final String url, String path, final DownloadVideoRunnable.ProgressCallback progressCallback, final DownloadVideoRunnable.CompleteCallback completeCallback) {
        final DownloadVideoRunnable runnable = new DownloadVideoRunnable(url, path, progressCallback, completeCallback);
        mSingleThreadExecutor.execute(runnable);
    }
    
    public static boolean isDownloaded(String path) {
        final File downloadedFile = new File(path);
        if (downloadedFile.exists()) {
            return true;
        }
        return false;
    }

}
