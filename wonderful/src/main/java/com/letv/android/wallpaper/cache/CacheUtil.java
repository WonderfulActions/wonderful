
package com.letv.android.wallpaper.cache;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import com.letv.android.wallpaper.display.DisplayTask.OnProgressListener;
import com.letv.android.wonderful.R;
import com.letv.android.wonderful.Tags;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class CacheUtil {
    private static final int CONNECT_TIMEOUT = 3000;
    private static final int READ_TIMEOUT_1080P = 5000;
    private static final int READ_TIMEOUT_2K = 7000;
    public static final String NO_MEDIA = ".nomedia";

    public static boolean checkOrMkDir(String dirStr) {
        File dir = new File(dirStr);
        if (dir.exists() && dir.isDirectory()) {
            return true;
        } else {
            return dir.mkdirs();
        }
    }

    public static String buildSaveDir(String saveDir, String name) {
        String dir = saveDir + "/" + name;
        return dir;
    }

    public static byte[] getByteArray(InputStream inputStream) {
        if (inputStream != null) {
            try {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[10 * 1024];
                int len = -1;
                while ((len = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, len);
                }
                byte[] byteArray = outputStream.toByteArray();
                outputStream.close();
                return byteArray;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
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

    public static int getReadTimeout() {
        /*
        if (DisplayDimension.is2K()) {
            return READ_TIMEOUT_2K;
        }
        */
        return READ_TIMEOUT_1080P;
    }

    public static InputStream getInputStreambyStr(String urlStr) {
        InputStream inputStream = null;
        if (null != urlStr && urlStr.length() > 6) {// http:// https://
            URL picUrl = null;
            try {
                picUrl = new URL(urlStr);
                HttpURLConnection picConnection = (HttpURLConnection) picUrl.openConnection();
                picConnection.setConnectTimeout(CONNECT_TIMEOUT);
                picConnection.setReadTimeout(getReadTimeout());
                picConnection.setDoInput(true);
                picConnection.connect();
                inputStream = picConnection.getInputStream();
            } catch (IOException e) {
            }
        }
        return inputStream;
    }

    public static InputStream getInputStream(HttpURLConnection connection) {
        if (connection != null) {
            try {
                InputStream inputStream = connection.getInputStream();
                return inputStream;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static HttpURLConnection getConnection(String url) {
        if (null != url && url.length() > 6) {// http:// https://
            try {
                URL picUrl = new URL(url);
                HttpURLConnection picConnection = (HttpURLConnection) picUrl.openConnection();
                picConnection.setConnectTimeout(CONNECT_TIMEOUT);
                picConnection.setReadTimeout(getReadTimeout());
                picConnection.setDoInput(true);
                picConnection.connect();
                return picConnection;
            } catch (IOException e) {
            }
        }
        return null;
    }

    public static int getContentLength(String urlStr) {
        try {
            URL url = new URL(urlStr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(CONNECT_TIMEOUT);
            connection.setDoInput(true);
            int length = connection.getContentLength();
            return length;
        } catch (MalformedURLException e) {
        } catch (IOException e) {
        }
        return -1;
    }

    public static boolean saveByteArray(String path, byte[] byteArray) {
        // boolean isSuccess = mkParentDir(new File(path).getParentFile());
        boolean isSuccessful = checkOrMkDir(new File(path).getParentFile());
        if (isSuccessful) {
            try {
                FileOutputStream outputStream = new FileOutputStream(path);
                outputStream.write(byteArray);
                outputStream.close();
                return true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean checkOrMkDir(File dir) {
        if (dir.exists() && dir.isDirectory()) {
            File noMedia = new File(dir, NO_MEDIA);
            if (!noMedia.exists()) {
                createNoMedia(dir);
            }
            return true;
        } else {
            boolean result = dir.mkdirs();
            if (result) {
                createNoMedia(dir);
            }
            return result;
        }
    }

    public static boolean createNoMedia(File dir) {
        File noMedia = new File(dir, NO_MEDIA);
        try {
            return noMedia.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Animation generateFadeInAnimation() {
        AlphaAnimation fadeInAnimation = new AlphaAnimation(0, 1);
        fadeInAnimation.setDuration(1500);
        fadeInAnimation.setInterpolator(new DecelerateInterpolator());
        return fadeInAnimation;
    }

    public static void recycleOldImage(ImageView imageView) {
        /*
        if (imageView != null) {
            final Bitmap oldBitmap = (Bitmap) imageView.getTag(R.id.imageview_cache_bitmap_reference);
            if (oldBitmap != null) {
                oldBitmap.recycle();
            }
        }
        */
    }

    public static void gcDelay(long delay) {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                gc();
            }
        }, delay);
    }

    // TODO optimize the method invoke time
    public static void gc() {
        systemGC();
        runtimeGC();
    }

    public static void runtimeGC() {
        Runtime.getRuntime().gc();
    }

    public static void systemGC() {
        System.gc();
    }

    /*
    public static void postSetImage(final ImageView imageView, final Bitmap bitmap, final DisplayOptions options) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (imageView != null && bitmap != null) {
                    if (options != null && options.getScaleType() != null) {
                        ScaleType scaleType = options.getScaleType();
                        imageView.setScaleType(scaleType);
                    }
                    WallpaperLog.print("topics", "set image in UI thread");
                    imageView.setImageBitmap(bitmap);
                }
            }
        });
    }
    */

    public static String getFileFromDiskCache(String url) {
        if (url != null) {
            String path = ByteArrayDiskCache.getPath(url);
            if (path != null) {
                File file = new File(path);
                if (file != null && file.exists() && file.isFile()) {
                    return path;
                }
            }
        }
        return null;
    }
}
