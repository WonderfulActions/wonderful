package com.letv.android.wonderful.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by michael on 16-3-21.
 */
public class DateUtil {
    public static String getTimeLabel() {
        final SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss", Locale.SIMPLIFIED_CHINESE);
        final String timeLabel = format.format(new Date(System.currentTimeMillis()));
        return timeLabel;
    }
}
