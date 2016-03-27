
package com.letv.android.wallpaper.listener;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;

import com.letv.android.wonderful.Tags;

import java.util.Timer;
import java.util.TimerTask;

public abstract class OnClickDoubleListener implements View.OnClickListener {
    private static final int MAX_INTERVAL = 300;
    private static final int WHAT = 999;
    private long mLastClickTime;
    private Timer mTimer;


    public OnClickDoubleListener() {
        mTimer = new Timer("OnClickDoubleListener", true);
    }

    @Override
    public void onClick(final View v) {
        final long currentTime = android.os.SystemClock.elapsedRealtime();
        final long clickInterval = currentTime - mLastClickTime;




        MAIN_HANDLER.removeMessages(WHAT);
        if (mLastClickTime != 0 && clickInterval < MAX_INTERVAL) {
            Log.i(Tags.WONDERFUL_APP, "--------- onClickDouble interval = " + clickInterval);
            // remove delay response
            // on click double
            onClickDouble(v);
            // reset last click time
            mLastClickTime = 0;
        } else {
            // on click once
            // onClickOnce(v);
            // delay response
            delayResponse(v);
            mLastClickTime = currentTime;
        }




/*
        if (mLastClickTime == 0) {
            // delay response
            delayResponse(v);
        } else {
            // remove delay response
            v.getHandler().removeMessages(WHAT);
            if (clickInterval < MAX_INTERVAL) {
                onClickDouble(v);
                mLastClickTime = 0;
            } else {
                delayResponse(v);
            }
        }
        */














    }

    private void delayResponse(final View v) {
        // delay response
        final Message message = Message.obtain(null, WHAT, -1, -1);
        MAIN_HANDLER.sendMessageDelayed(message, MAX_INTERVAL);
    }

    private final Handler MAIN_HANDLER = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            // get delay response message
            if (msg.what == WHAT) {
                onClickOnce(null);
            }
        }
    };

    public abstract void onClickOnce(View v);

    public abstract void onClickDouble(View v);

}
