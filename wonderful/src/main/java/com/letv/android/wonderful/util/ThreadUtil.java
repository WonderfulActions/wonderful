package com.letv.android.wonderful.util;

public class ThreadUtil {
    public static void sleep(long timeMillis) {
        try {
            Thread.sleep(timeMillis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
