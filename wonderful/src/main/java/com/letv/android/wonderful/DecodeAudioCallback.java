
package com.letv.android.wonderful;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaCodec.BufferInfo;
import android.media.MediaCodec.CodecException;
import android.media.MediaFormat;
import android.util.Log;

import java.io.IOException;
import java.nio.ByteBuffer;

public class DecodeAudioCallback extends MediaCodec.Callback {

    private static final String TAG = MediaCodecPlayer.class.getSimpleName();
    private static final int BUFFER_FLAG_SAMPLE = 0;

    private Context mContext;
    private String mPath;
    private int mAudioIndex;
    private MediaExtractor mExtractor;
    private AudioTrack mAudioTrack;
    private AudioOutputCallback mCallback;
    
    public interface AudioOutputCallback {
        public void onAudioCallback(AudioTrack mAudioTrack, byte[] chunk, int total);
    }

    public DecodeAudioCallback(Context context, String path, int videoIndex, AudioOutputCallback callback) {
        mContext = context;
        mPath = path;
        mAudioIndex = videoIndex;
        initExtractor();
        mCallback = callback;
    }

    private void initExtractor() {
        try {
            mExtractor = new MediaExtractor();
            mExtractor.setDataSource(mPath);
            mExtractor.selectTrack(mAudioIndex);
            /*
            // 音频文件信息
            format = extractor.getTrackFormat(0);
            mime = format.getString(MediaFormat.KEY_MIME);
            sampleRate = format.getInteger(MediaFormat.KEY_SAMPLE_RATE);
            // 声道个数：单声道或双声道
            channels = format.getInteger(MediaFormat.KEY_CHANNEL_COUNT);
            // if duration is 0, we are probably playing a live stream
            duration = format.getLong(MediaFormat.KEY_DURATION);
            // System.out.println("歌曲总时间秒:"+duration/1000000);
            bitrate = format.getInteger(MediaFormat.KEY_BIT_RATE);
            */
            final MediaFormat format = mExtractor.getTrackFormat(mAudioIndex);
            final String mime =format.getString(MediaFormat.KEY_MIME);
            Log.i(TAG, "audio mime = " + mime);
            final String sessionId = format.getString(MediaFormat.KEY_AUDIO_SESSION_ID);
            final int sampleRate = format.getInteger(MediaFormat.KEY_SAMPLE_RATE);
            final int channelCount = format.getInteger(MediaFormat.KEY_CHANNEL_COUNT);
//            Log.i(TAG, "sessionId = " + sessionId);
            Log.i(TAG, "sampleRate = " + sampleRate);
            Log.i(TAG, "channelCount = " + channelCount);
            initAudioTrack(sampleRate, channelCount, sessionId);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static final int STREAM_TYPE = AudioManager.STREAM_MUSIC;
    private static final int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
    private static final int MODE = AudioTrack.MODE_STREAM;
    private static final int BUFFER_SIZE = 50 * 1024 * 1024;
    private static final int CHANNEL_CONFIG = AudioFormat.CHANNEL_OUT_STEREO;

    private void initAudioTrack(int sampleRate, int channelCount, String sessionId) {
//        final int session = Integer.parseInt(sessionId);
        /*
        int bufSize = AudioTrack.getMinBufferSize(sampleRate, CHANNEL_CONFIG, AUDIO_FORMAT);
        Log.i(TAG, "bufSize = " + bufSize);
        mAudioTrack = new AudioTrack(STREAM_TYPE, sampleRate, CHANNEL_CONFIG, AUDIO_FORMAT, BUFFER_SIZE, MODE);
        mAudioTrack.play();
        */
        
        mAudioTrack = new AudioTrack(
                AudioManager.STREAM_MUSIC,
                sampleRate,
                AudioFormat.CHANNEL_OUT_STEREO,
                AudioFormat.ENCODING_PCM_16BIT,
                
                AudioTrack.getMinBufferSize(
                        sampleRate,
                        AudioFormat.CHANNEL_OUT_STEREO,
                        AudioFormat.ENCODING_PCM_16BIT
                        ),
                        
                AudioTrack.MODE_STREAM
                );
        mAudioTrack.play();
    }

    @Override
    public void onOutputFormatChanged(MediaCodec codec, MediaFormat format) {
        final String mime = format.getString(MediaFormat.KEY_MIME);
        Log.i(TAG, "audio onOutputFormatChanged mime = " + mime);
        // do nothing
    }

    @Override
    public void onError(MediaCodec codec, CodecException e) {
        Log.i(TAG, "audio onError");
        // do nothing
    }

    @Override
    public void onInputBufferAvailable(MediaCodec codec, int index) {
        final ByteBuffer inputBuffer = codec.getInputBuffer(index);
        final int sampleSize = mExtractor.readSampleData(inputBuffer, 0);
        Log.i(TAG, "audio sampleSize = " + sampleSize);
        if (sampleSize > -1) {
            // queue inputBuffer
            final long sampleTime = mExtractor.getSampleTime();
            Log.i(TAG, "audio sampleTime millis = " + sampleTime / 1000);
            codec.queueInputBuffer(index, 0, sampleSize, sampleTime, BUFFER_FLAG_SAMPLE);
            // advance
            mExtractor.advance();
//            sleepMillis(3000);
        } else {
            // release extractor
            // loop mode
//            final boolean loop = PreferenceUtil.loop(mContext);
            final boolean loop = false;
            Log.i(TAG, "loop = " + loop);
            if (loop) {
                mExtractor.seekTo(0, MediaExtractor.SEEK_TO_PREVIOUS_SYNC);
            } else {
                // no loop
                mExtractor.release();
//                mAudioTrack.stop();
//                mAudioTrack.release();
                // queue EOS
                Log.i(TAG, "audio queue input EOS");
                codec.queueInputBuffer(index, 0, 0, 0, MediaCodec.BUFFER_FLAG_END_OF_STREAM);
            }
        }
    }

    private byte[] chunk = new byte[5 * 1024 * 1024];
    private int total;
    
    @Override
    public void onOutputBufferAvailable(MediaCodec codec, int index, BufferInfo info) {
        final int flags = info.flags;
        if (flags == BUFFER_FLAG_SAMPLE) {
            
            
            
            // handle output audio byte
            final ByteBuffer outputBuffer = codec.getOutputBuffer(index);
            outputBuffer.get(chunk, total, info.size);
            total += info.size;
            
            
            
            
            
            
            
            codec.releaseOutputBuffer(index, false);
        } else if (flags == MediaCodec.BUFFER_FLAG_END_OF_STREAM) {
            Log.i(TAG, "audio handle output buffer EOS!!!!!");
            if (chunk.length > 0) {
                // display audio
//                sleepMillis(20);
//                final int playState = mAudioTrack.getPlayState();
//                Log.i(TAG, "playState = " + playState);
//                mAudioTrack.write(outputBuffer, info.size, AudioTrack.WRITE_NON_BLOCKING);
//                mAudioTrack.write(chunk, 0, total);
                mCallback.onAudioCallback(mAudioTrack, chunk, total);
            }
            shutdownCodec(codec);
        } else {
            // do nothing
        }
    }

    private static void shutdownCodec(MediaCodec codec) {
        codec.stop();
        Log.i(TAG, "audio stop async decode");
        Log.i(TAG, "audio decode interval = " + IntervalTimer.getInterval());
        codec.release();
    }

    private static void sleepMillis(long timeMillis) {
        try {
            Thread.sleep(timeMillis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
