
package com.letv.android.wallpaper.display;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;

import java.util.Timer;
import java.util.TimerTask;

public class MemoryUtil {

    public static void printMemoryLoop() {
        final Timer timer = new Timer("memory", true);
        final TimerTask task = new TimerTask() {
            @Override
            public void run() {
                MemoryUtil.printMemory();
            }
        };
        timer.schedule(task, 0, 1000);
    }

    public static void printMemory() {
        final long totalMemory = Runtime.getRuntime().totalMemory();
        final String log = "totalMemory MB = " + totalMemory / 1024 / 1024;
    }

    public static void dumpMemoryInfo(final MemoryInfo memoryInfo) {
        if (memoryInfo != null) {
            final long totalMem = memoryInfo.totalMem;
            final long availMem = memoryInfo.availMem;
            final long threshold = memoryInfo.threshold;
            final boolean isMemoryLow = memoryInfo.lowMemory;
        }
    }

    public static boolean isSystemMemoryLow(Context context) {
        if (context == null) {
            return true;
        }
        final ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        final MemoryInfo memoryInfo = new MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        // dumpMemoryInfo(memoryInfo);
        final boolean isSystemMemoryLow = memoryInfo.lowMemory;
        // Log.i(Tags.MEMORY_INFO, "isSystemMemoryLow = " + isSystemMemoryLow);
        return isSystemMemoryLow;
    }

    private static final long THRESHOLD_RUNTIME = 15 * 2 * 1024 * 1024;

    private static MemoryInfo getRuntimeMemoryInfo() {
        // total memory
        // available memory
        // threshold
        // isMemoryLow
        final long maxMemory = Runtime.getRuntime().maxMemory();
        final long totalMemory = Runtime.getRuntime().totalMemory();
        final long availableMemory = maxMemory - totalMemory;
        final boolean isMemoryLow = availableMemory < THRESHOLD_RUNTIME;
        final MemoryInfo memoryInfo = new MemoryInfo();
        memoryInfo.totalMem = totalMemory;
        memoryInfo.availMem = availableMemory;
        memoryInfo.threshold = THRESHOLD_RUNTIME;
        memoryInfo.lowMemory = isMemoryLow;
        return memoryInfo;
    }

    public static boolean isRuntimeMemoryLow() {
        final MemoryInfo memoryInfo = getRuntimeMemoryInfo();
        // dumpMemoryInfo(memoryInfo);
        final boolean isRuntimeMemoryLow = memoryInfo.lowMemory;
        // Log.i(Tags.MEMORY_INFO, "isRuntimeMemoryLow = " + isRuntimeMemoryLow);
        return isRuntimeMemoryLow;
    }

    public static boolean isMemoryLow(Context context) {
        final boolean isMemoryLow = isSystemMemoryLow(context) || isRuntimeMemoryLow();
        return isMemoryLow;
    }
}
