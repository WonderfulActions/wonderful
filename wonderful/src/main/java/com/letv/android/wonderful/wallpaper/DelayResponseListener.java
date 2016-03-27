package com.letv.android.wonderful.wallpaper;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/**
 * Created by michael on 16-3-21.
 */
public abstract class DelayResponseListener {
        //        private static final long DELAY_TIME = 1 * 1000;
        private static final int WHAT = 99;

        private volatile boolean mLastState;
        private final Handler MAIN_HANDLER = new Handler(Looper.getMainLooper()) {
            public void handleMessage(Message msg) {
                // get delay response message
                if (msg.what == WHAT) {
                    // get response visible
                    final boolean visibility = (msg.arg1 == 1);
                    // check current visible state
                    if (visibility == mLastState) {
                        // response
                        onResponseDelay(mLastState);
                    } else {
                        // do not response
                    }
                }
            }
        };

        public void onStateChanged(boolean state, long delay) {
            // update visible state
            mLastState = state;
            // remove previous message
            MAIN_HANDLER.removeMessages(WHAT);
            // delay response
            final Message message = Message.obtain(null, WHAT, (mLastState ? 1 : 0), -1);
            MAIN_HANDLER.sendMessageDelayed(message, delay);
        }

        public abstract void onResponseDelay(boolean state);


}
