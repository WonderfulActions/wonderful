package com.letv.android.wonderful.download;

import android.os.Handler;
import android.os.Looper;

import com.letv.android.wonderful.download.DownloadVideoTask.CompleteCallback;
import com.letv.android.wonderful.download.DownloadVideoTask.ProgressCallback;

public class DownloadVideoRunnable implements Runnable {
    /*
    public interface ProgressCallback {
        public void onProgressUpdate(int progress);
    }

    public interface CompleteCallback {
        public void onDownloadComplete();
    }

    private String mPath;
    private ProgressCallback mProgressCallback;
    private CompleteCallback mCompleteCallback;

    public DownloadVideoTask(String path, ProgressCallback progressCallback, CompleteCallback completeCallback) {
        mPath = path;
        mProgressCallback = progressCallback;
        mCompleteCallback = completeCallback;
    }

    @Override
    protected Boolean doInBackground(String... urls) {
        final String url = urls[0];
        final boolean result = DownloadVideoUtil.downloadFile(mPath, url, new ProgressCallback() {
            @Override
            public void onProgressUpdate(int progress) {
                publishProgress(progress);
            }
        });
        return result;
    }

    @Override
    protected void onProgressUpdate(Integer... progresses) {
        final int progress = progresses[0];
        mProgressCallback.onProgressUpdate(progress);
    }

    @Override
    protected void onPostExecute(Boolean result) {
        if (result) {
            mCompleteCallback.onDownloadComplete();
        }
    }
    */
    
    private String mPath;
    private String mUrl;
    private ProgressCallback mProgressCallback;
    private CompleteCallback mCompleteCallback;
    private Handler UI_HANDLER = new Handler(Looper.getMainLooper());
    
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

    public DownloadVideoRunnable(final String url, String path, final DownloadVideoTask.ProgressCallback progressCallback, final DownloadVideoTask.CompleteCallback completeCallback) {
        mUrl = url;
        mPath = path;
        mProgressCallback = progressCallback;
        mCompleteCallback = completeCallback;
    }
    
}
