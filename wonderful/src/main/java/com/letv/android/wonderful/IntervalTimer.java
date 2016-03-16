package com.letv.android.wonderful;

public class IntervalTimer {
    private static long mStartTimeMillis;

    public static void start() {
        mStartTimeMillis = System.currentTimeMillis();
    }

    public static long getInterval() {
        final long intervalTimeMillis = System.currentTimeMillis() - mStartTimeMillis;
        return intervalTimeMillis;
    }
}
