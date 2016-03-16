
package com.letv.android.wonderful.download;

import android.os.AsyncTask;

public class DownloadVideoTask extends AsyncTask<String, Integer, Boolean> {
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

}
