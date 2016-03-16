
package com.letv.android.wonderful.download;

import java.io.File;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DownloadVideoMananger {
    private static Executor mSingleThreadExecutor = Executors.newSingleThreadExecutor();

    public static void downloadVideo(final String url, String path, final DownloadVideoTask.ProgressCallback progressCallback, final DownloadVideoTask.CompleteCallback completeCallback) {
        // new DownloadVideoTask(path, progressCallback, completeCallback).executeOnExecutor(DownloadVideoTask.THREAD_POOL_EXECUTOR, url);
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
