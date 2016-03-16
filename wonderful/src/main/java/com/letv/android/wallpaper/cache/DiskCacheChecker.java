package com.letv.android.wallpaper.cache;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DiskCacheChecker {
    private static DiskCacheChecker mInstance;

    private static final String IMAGE_CACHE_DIR = ByteArrayDiskCache.IMAGE_CACHE_DIR;
    private static final int IMAGE_CACHE_MAX_COUNT = ByteArrayDiskCache.MAX_COUNT;
    private static final int IMAGE_CACHE_CHECK_INTERVAL = 100;
    private static final int IMAGE_CACHE_DELETE_NUM_PER_TIME = 500;

    private ExecutorService mExecutorService = Executors.newFixedThreadPool(1);

    private int mIndex = 0;

    static {
        mInstance = new DiskCacheChecker();
    }

    public synchronized static DiskCacheChecker getInstance() {
        return mInstance;
    }

    public void checkImageCacheDir() {
        if (mIndex % IMAGE_CACHE_CHECK_INTERVAL == 0) {
            mExecutorService.submit(new Runnable() {

                @Override
                public void run() {
                    File dir = new File(IMAGE_CACHE_DIR);
                    if (dir != null && dir.exists() && dir.isDirectory()) {
                        File[] files = getFiles(dir);
                        if (files != null && files.length >= IMAGE_CACHE_MAX_COUNT - 500) {
                            try {
                                sortAndDeleteOldestFile(files, IMAGE_CACHE_DELETE_NUM_PER_TIME);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });
        }
        mIndex++;
    }

    protected void sortAndDeleteOldestFile(File[] files, int deleteNubPerTime) {
        if (files != null && files.length > 0) {
            Arrays.sort(files, new Comparator<File>() {
                @Override
                public int compare(File lhs, File rhs) {
                    if (lhs.exists() && rhs.exists()) {
                        if (lhs.lastModified() < rhs.lastModified()) {
                            return -1;
                        } else if (lhs.lastModified() == rhs.lastModified()) {
                            return 0;
                        } else {
                            return 1;
                        }
                    } else {
                        if (rhs.exists()) {
                            return -1;
                        } else if (lhs.exists()) {
                            return 1;
                        } else {
                            return 0;
                        }
                    }
                }
            });
        }

        for (int i = 0; i < files.length && i < deleteNubPerTime; i++) {
            try {
                files[i].delete();
            } catch (Exception e) {
                e.printStackTrace();
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
}
