package com.letv.android.wallpaper.display;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.letv.android.wallpaper.cache.NewCacheManager;
import com.letv.android.wallpaper.display.CacheTask.OnCompleteListener;
import com.letv.android.wallpaper.display.CacheTask.OnProgressListener;
import com.letv.android.wallpaper.display.NewDisplayOptions.DisplayShape;
import com.letv.android.wallpaper.display.decoder.BitmapDecoder;
import com.letv.android.wonderful.R;

public class NewDisplayRunnable implements Runnable {
    private static final String INTERRUPTED_SLEEP = "interrupted sleeping";
    private static final String INTERRUPTED_BEFORE_DELAY = "interrupted before delay";
    private static final String INTERRUPTED_BEFORE_GET_BYTEARRAY = "interrupted before get byteArray";
    private static final String INTERRUPTED_BEFORE_DECODE_BITMAP = "interrupted before decode bitmap";
    private static final String INTERRUPTED_BEFORE_DISPLAY_BITMAP = "interrupted before display bitmap";
    private static final Handler MAIN_HANDLER = new Handler(Looper.getMainLooper());
    private NewDisplayTask mTask;

    public NewDisplayRunnable(NewDisplayTask task) {
        mTask = task;
    }

    @Override
    public void run() {
        try {
            isInterrupted(INTERRUPTED_BEFORE_DELAY);
            // check to delay
            final long delay = mTask.getDelay();
            if (delay > 0) {
                // Log.i(Tags.DISPLAY_TASK_MANAGER, "start sleep");
                Thread.sleep(delay);
                // Log.i(Tags.DISPLAY_TASK_MANAGER, "stop sleep");
            }
            isInterrupted(INTERRUPTED_BEFORE_GET_BYTEARRAY);
            // display
            display();
        } catch (InterruptedException e) {
            // print exception message
            final String message = e.getMessage();
            final String detail = (message == null ? INTERRUPTED_SLEEP : message);
            e.printStackTrace();
            // notify complete
            postNotifyDisplayComplete(null);
        }
    }

    public static void isInterrupted(String message) throws InterruptedException {
        if (Thread.interrupted()) {
            throw new InterruptedException(message);
        }
    }

    private void display() throws InterruptedException {
        // get byteArray
        final String url = mTask.getUrl();
        final OnProgressListener onProgressListener = mTask.getOnProgressListener();
        // cache config
        final boolean cache2Memory = mTask.getDisplayOptions().isCache2Memory();
        final boolean cache2Disk = mTask.getDisplayOptions().isCache2Disk();
        final byte[] byteArray = NewCacheManager.getByteArray(url, onProgressListener, cache2Memory, cache2Disk);
        if (byteArray != null) {
            isInterrupted(INTERRUPTED_BEFORE_DECODE_BITMAP);
            // decode
            final Options decodeOptions = mTask.getDisplayOptions().getDecodeOptions();
            final Bitmap bitmap = BitmapDecoder.decode(byteArray, decodeOptions);
            if (bitmap != null) {
                isInterrupted(INTERRUPTED_BEFORE_DISPLAY_BITMAP);
                // display
                postDisplay(bitmap);
            } else {
                // notify complete
                postNotifyDisplayComplete(null);
            }
        } else {
            // notify complete
            postNotifyDisplayComplete(null);
        }
    }

    private void postDisplay(final Bitmap bitmap) {
        MAIN_HANDLER.post(new Runnable() {
            @Override
            public void run() {
                if (mTask.isCancelled()) {notifyDisplayComplete(null);return;}
                displayBitmap(bitmap);
            }
        });
    }

    private void displayBitmap(Bitmap bitmap) {
        final ImageView imageView = mTask.getImageView();
        if (bitmap != null && imageView != null && imageView.getVisibility() == View.VISIBLE) {
            if (mTask.isCancelled()) {notifyDisplayComplete(null);return;}
            // scale type
            final ScaleType scaleType = mTask.getDisplayOptions().getScaleType();
            if (scaleType != null) {
                imageView.setScaleType(scaleType);
            }
            if (mTask.isCancelled()) {notifyDisplayComplete(null);return;}
            // show image in shape
            final int shape = mTask.getDisplayOptions().getDisplayShape();
            switch (shape) {
                /*
                case DisplayShape.ROUND:
                    final Drawable roundDrawable = RoundedDrawable.generateDefaultRoundDrawable(bitmap);
                    imageView.setImageDrawable(roundDrawable);
                    break;
                    */
                default:
                    imageView.setImageBitmap(bitmap);
//                    imageView.setTag(R.id.imageview_cache_bitmap_reference, bitmap);
                    break;
            }
            if (mTask.isCancelled()) {notifyDisplayComplete(null);return;}
            // display animation
            final int displayAnimation = mTask.getDisplayOptions().getDisplayAnimation();
            final Animation animation = generateDisplayAnimation(displayAnimation);
            if (animation != null) {
                imageView.startAnimation(animation);
            }
            // notify display complete
            notifyDisplayComplete(bitmap);
        }
    }

    private void notifyDisplayComplete(Bitmap bitmap) {
        final OnCompleteListener onCompleteListener = mTask.getOnCompleteListener();
        if (onCompleteListener != null) {
            onCompleteListener.onBitmapComplete(mTask.getUrl(), bitmap);
        }
    }

    private static Animation generateDisplayAnimation(int displayAnimation) {
        switch (displayAnimation) {
            /*
            case DisplayAnimation.FADE_IN:
                final Animation fadeInAnimation = AnimationUtil.generateDefaultFadeInAnimation();
                return fadeInAnimation;
                */
            default:
                return null;
        }
    }

    private void postNotifyDisplayComplete(final Bitmap bitmap) {
        final OnCompleteListener onCompleteListener = mTask.getOnCompleteListener();
        if (onCompleteListener != null) {
            MAIN_HANDLER.post(new Runnable() {
                @Override
                public void run() {
                    onCompleteListener.onBitmapComplete(mTask.getUrl(), bitmap);
                }
            });
        }
    }

}
