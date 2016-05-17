package com.letv.android.wonderful.download;

import android.os.Handler;
import android.os.Looper;

public class DownloadVideoRunnable implements Runnable {
    // =====================================================================
    // callbacks
    public interface ProgressCallback {
        public void onProgressUpdate(int progress);
    }

    public interface CompleteCallback {
        public void onDownloadComplete();
    }
    // =====================================================================


    private static Handler UI_HANDLER = new Handler(Looper.getMainLooper());
    private String mUrl;
    private String mPath;
    private ProgressCallback mProgressCallback;
    private CompleteCallback mCompleteCallback;

    public DownloadVideoRunnable(final String url, String path, final ProgressCallback progressCallback, final CompleteCallback completeCallback) {
        mUrl = url;
        mPath = path;
        mProgressCallback = progressCallback;
        mCompleteCallback = completeCallback;
    }

    @Override
    public void run() {
        final boolean result = DownloadVideoUtil.downloadFile(mPath, mUrl, new ProgressCallback() {
            @Override
            public void onProgressUpdate(int progress) {
                postPorgress(progress);
            }
        });
        if (result) {
            postComplete();
        }
    }

    private void postPorgress(final int progress) {
        UI_HANDLER.post(new Runnable() {
            @Override
            public void run() {
                mProgressCallback.onProgressUpdate(progress);
            }
        });
    }

    private void postComplete() {
        UI_HANDLER.post(new Runnable() {
            @Override
            public void run() {
                mCompleteCallback.onDownloadComplete();
            }
        });
    }

}
