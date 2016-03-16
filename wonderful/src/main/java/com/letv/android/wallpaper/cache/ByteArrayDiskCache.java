
package com.letv.android.wallpaper.cache;

import com.letv.android.wallpaper.display.DisplayTask.OnProgressListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ByteArrayDiskCache implements IByteArrayCache {
    public static String IMAGE_CACHE_DIR = CachePathUtil.CACHE_DIR + "/" + "image";
    public static final int MAX_COUNT = 5000;
    private static ByteArrayDiskCache sInstance;
    private ExecutorService service = new ThreadPoolExecutor(3, 12, 5, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>());

    private ByteArrayDiskCache() {
        // do nothing
    }

    static {
        sInstance = new ByteArrayDiskCache();
    }

    public static ByteArrayDiskCache getInstance() {
        return sInstance;
    }

    public static String getPath(String url) {
        if (isRegular(url)) {
            if (isNetUrl(url)) {
                String path = CacheUtil.buildSaveDir(IMAGE_CACHE_DIR, getName(url));
                return path;
            } else {
                return url;
            }
        }
        return null;
    }

    private static boolean isNetUrl(String url) {
        if (url != null && url.contains("http")) {
            return true;
        }
        return false;
    }

    public static String getName(String url) {
        if (isRegular(url)) {
            return url.hashCode() + ".jpg";
        }
        return null;
    }

    public static boolean isRegular(String url) {
        if (url != null && url.length() > 5) {
            return true;
        }
        return false;
    }

    @Override
    public boolean exists(String url) {
        String path = getPath(url);
        if (path != null) {
            File file = new File(path);
            if (file != null && file.exists() && file.isFile()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean cache(final String url, final byte[] byteArray) {
        if (url == null || byteArray == null) {
            return false;
        }

        if (url != null && exists(url)) {
            return true;
        }

        if (url != null && byteArray != null) {
            submitCacheRunnable(url, byteArray);
            return true;
        }
        return false;
    }

    private void submitCacheRunnable(final String url, final byte[] byteArray) {
        service.submit(new Runnable() {
            @Override
            public void run() {
                String path = getPath(url);
                boolean isSuccessful = CacheUtil.saveByteArray(path, byteArray);
                if (isSuccessful) {
                    DiskCacheChecker.getInstance().checkImageCacheDir();
                }
            }
        });
    }

    @Override
    public byte[] getByteArray(String url) {
        return getByteArray(url, null);
    }

    public static byte[] getByteArray(String url, OnProgressListener listener) {
        if (isRegular(url)) {
            String path = getPath(url);
            File file = new File(path);
            if (file != null && file.exists() && file.isFile()) {
                try {
                    FileInputStream inputStream = new FileInputStream(file);
                    // TODO content length
                    long length = file.length();
                    if (length > Integer.MAX_VALUE) {
                        inputStream.close();
                        return null;
                    }
                    byte[] byteArray = new byte[(int) length];
                    byteArray = CacheUtil.getByteArray(inputStream, byteArray, listener, "");
                    inputStream.close();
                    if (byteArray != null) {
                    }
                    return byteArray;
                } catch (FileNotFoundException e) {
//                    System.out.println("disk cache getByteArray FileNotFoundException");
                } catch (IOException e) {
//                    System.out.println("disk cache getByteArray IOException");
                }
            }
        }
        return null;
    }

    public static void checkCacheSizeToRemoveOldest() {
        if (getOverflowSize() > 0) {
            final int size = getOverflowSize();
            for (int i = 0; i < size; i++) {
                removeLeastRecentlyFile();
            }
        }
    }

    public static int getOverflowSize() {
        File dir = new File(IMAGE_CACHE_DIR);
        if (dir != null && dir.exists() && dir.isDirectory()) {
            File[] files = getFiles(dir);
            if (files != null) {
            }
            if (files != null && files.length > MAX_COUNT) {
                int size = files.length - MAX_COUNT;
                return size;
            }
        }
        return 0;
    }

    public static void removeLeastRecentlyFile() {
        File dir = new File(IMAGE_CACHE_DIR);
        if (dir != null && dir.exists() && dir.isDirectory()) {
            File[] files = getFiles(dir);
            if (files != null) {
                final File fileLeastRecently = getLeastRecentlyFile(files);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        fileLeastRecently.delete();
                    }
                }).start();
            }
        }
    }

    public static File[] getFiles(File dir) {
        File[] files = dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String fileName) {
                if (dir.getAbsolutePath().equals(IMAGE_CACHE_DIR) && fileName.contains(".jpg")) {
                    return true;
                }
                return false;
            }
        });
        return files;
    }

    public static File getLeastRecentlyFile(File[] files) {
        if (files != null && files.length >= 0) {
            long timeLeastRecently = files[0].lastModified();
            File fileLeastRecently = files[0];
            for (File file : files) {
                long time = file.lastModified();
                if (time < timeLeastRecently) {
                    timeLeastRecently = time;
                    fileLeastRecently = file;
                }
            }
            return fileLeastRecently;
        }
        return null;
    }

}
