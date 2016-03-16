package com.letv.android.wonderful.content;

import android.content.Context;

import com.letv.android.wonderful.R;

public class ContentGroup {
    public static final int LATEST = 0;
    public static final int HOT = 1;
    public static final int TOP = 2;
    public static final int RECOMMENDED = 3;
    
    public static final int COLLECTED = 100;
    public static final int HISTORY = 200;

    public static String getGroupLabel(Context context, int group) {
        switch (group) {
            case LATEST:
                return context.getString(R.string.group_latest);
            case HOT:
                return context.getString(R.string.group_hot);
            case TOP:
                return context.getString(R.string.group_top);
            case RECOMMENDED:
                return context.getString(R.string.group_recommended);
            default:
                return null;
        }
    }
}
