
package com.letv.android.wonderful;

import android.content.Context;
import android.media.MediaCodec;
import android.media.MediaCodec.BufferInfo;
import android.media.MediaCodec.CodecException;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.util.Log;

import java.io.IOException;
import java.nio.ByteBuffer;

public class DecodeVideoCallback extends MediaCodec.Callback {
    private static final String TAG = MediaCodecPlayer.class.getSimpleName();
    private static final int BUFFER_FLAG_SAMPLE = 0;
    private Context mContext;
    private String mPath;
    private int mVideoIndex;
    private MediaExtractor mExtractor;

    public DecodeVideoCallback(Context context, String path, int videoIndex) {
        mContext = context;
        mPath = path;
        mVideoIndex = videoIndex;
        initExtractor();
    }

    private void initExtractor() {
        try {
            mExtractor = new MediaExtractor();
            mExtractor.setDataSource(mPath);
            mExtractor.selectTrack(mVideoIndex);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onOutputFormatChanged(MediaCodec codec, MediaFormat format) {
        final String mime = format.getString(MediaFormat.KEY_MIME);
        Log.i(TAG, "video onOutputFormatChanged mime = " + mime);
        // do nothing
    }

    @Override
    public void onError(MediaCodec codec, CodecException e) {
        Log.i(TAG, "video onError");
        // do nothing
    }

    @Override
    public void onInputBufferAvailable(MediaCodec codec, int index) {
        final ByteBuffer inputBuffer = codec.getInputBuffer(index);
        final int sampleSize = mExtractor.readSampleData(inputBuffer, 0);
        if (sampleSize > -1) {
            // queue inputBuffer
            final long sampleTime = mExtractor.getSampleTime();
            codec.queueInputBuffer(index, 0, sampleSize, sampleTime, BUFFER_FLAG_SAMPLE);
            // advance
            mExtractor.advance();
            sleepMillis(25);
        } else {
            // release extractor
            // loop mode
            final boolean loop = PreferenceUtil.needRepeat(mContext);
//            final boolean loop = false;
            Log.i(TAG, "loop = " + loop);
            if (loop) {
                // loop
                sleep(1000);
                mExtractor.seekTo(0, MediaExtractor.SEEK_TO_PREVIOUS_SYNC);
            } else {
                // no loop
                mExtractor.release();
                // queue EOS
                Log.i(TAG, "video queue EOS");
                codec.queueInputBuffer(index, 0, 0, 0, MediaCodec.BUFFER_FLAG_END_OF_STREAM);
            }
        }
    }

    private static void sleep(long timeMillis) {
        try {
            Thread.sleep(timeMillis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onOutputBufferAvailable(MediaCodec codec, int index, BufferInfo info) {
        final int flags = info.flags;
        if (flags == BUFFER_FLAG_SAMPLE) {
            codec.releaseOutputBuffer(index, true);
        } else if (flags == MediaCodec.BUFFER_FLAG_END_OF_STREAM) {
            Log.i(TAG, "handle output buffer EOS!!!!!");
            shutdownCodec(codec);
        } else {
            // do nothing
        }
    }

    private static void shutdownCodec(MediaCodec codec) {
        codec.stop();
        Log.i(TAG, "video stop async decode");
        Log.i(TAG, "video decode interval = " + IntervalTimer.getInterval());
        codec.release();
    }

    private static void sleepMillis(long timeMillis) {
        try {
            Thread.sleep(timeMillis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
};
