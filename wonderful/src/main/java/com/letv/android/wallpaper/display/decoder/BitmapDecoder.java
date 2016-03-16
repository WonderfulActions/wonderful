
package com.letv.android.wallpaper.display.decoder;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;

public class BitmapDecoder {

    public static Bitmap decode(byte[] byteArray) {
        return decode(byteArray, null);
    }

    public static Bitmap decode(byte[] byteArray, Options options) {
        if (byteArray != null) {
            try {
                final Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length, options);
                return bitmap;
            } catch (Throwable e) {
            }
        }
        return null;
    }
}
