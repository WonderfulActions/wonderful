
package com.letv.android.wallpaper.display;

public class DisplayTask {
    /*
     * public interface OnCompleteListener { void onComplete(ImageView imageView, String url,
     * boolean result, Bitmap bitmap); }
     */

    public interface OnProgressListener {
        void onProgressUpdate(byte[] byteArray, int offset, int length, boolean isEnd);
    }
}
