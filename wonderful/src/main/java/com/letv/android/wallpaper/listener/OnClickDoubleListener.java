
package com.letv.android.wallpaper.listener;

import android.util.Log;
import android.view.View;

import com.letv.android.wonderful.Tags;

public abstract class OnClickDoubleListener implements View.OnClickListener {
    private static final int MAX_INTERVAL = 300;
    private long mLastClickTime;

    @Override
    public void onClick(View v) {
        final long currentTime = android.os.SystemClock.elapsedRealtime();
        final long clickInterval = currentTime - mLastClickTime;
        if (mLastClickTime != 0 && clickInterval < MAX_INTERVAL) {
            Log.i(Tags.CLICK_DOUBLE, "onClickDouble clickInterval = " + clickInterval);
            // on click double
            onClickDouble(v);
        } else {
            Log.i(Tags.CLICK_DOUBLE, "----------------onClickOnce clickInterval = " + clickInterval);
            // on click once
            onClickOnce(v);
        }
        mLastClickTime = currentTime;
    }

    public abstract void onClickOnce(View v);

    public abstract void onClickDouble(View v);

}
