package com.letv.android.wonderful;

import android.content.Context;
import android.media.MediaCodec;
import android.media.MediaCodec.BufferInfo;
import android.media.AudioTrack;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.util.Log;
import android.view.Surface;

import com.letv.android.wonderful.DecodeAudioCallback.AudioOutputCallback;

import java.io.IOException;
import java.nio.ByteBuffer;

public class MediaCodecPlayer {
    private static MediaCodecPlayer mInstance;

    static {
        mInstance = new MediaCodecPlayer();
    }

    public  static MediaCodecPlayer getInstance() {
        return mInstance;
    }

    // single instance
    // ================================================================================
    private static final String TAG = MediaCodecPlayer.class.getSimpleName();

    private Context mContext;
    private String mPath;
    private Surface mSurface;
    
    private MediaCodecPlayer() {
        // TODO init
        
        
    }

    public Context getContext() {
        return mContext;
    }

    public void setContext(Context context) {
        this.mContext = context;
    }

    public String getPath() {
        return mPath;
    }

    public void setPath(String path) {
        this.mPath = path;
    }

    public Surface getSurface() {
        return mSurface;
    }

    public void setSurface(Surface surface) {
        this.mSurface = surface;
    }

    private static final String MIMETYPE_VIDEO = "video/";
    private static final String MIMETYPE_AUDIO = "audio/";

    private static boolean isVideo(String mime) {
        final boolean isVideo = mime.startsWith(MIMETYPE_VIDEO);
        return isVideo;
    }

    private static boolean isAudio(String mime) {
        final boolean isAudio = mime.startsWith(MIMETYPE_AUDIO);
        return isAudio;
    }

    private static void sleep(long timeMillis) {
        try {
            Thread.sleep(timeMillis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    private static final long DEQUEUE_TIMEOUT = 10 * 1000;

    public void displayMediaAsync(Context context, final String path, final Surface surface) {
        setContext(context);
        setPath(path);
        setSurface(surface);

        IntervalTimer.start();
        // display audio
        new Thread(new Runnable() {
            @Override
            public void run() {
                displayAudioAsync(path, surface, new DecodeAudioCallback.AudioOutputCallback() {
                    @Override
                    public void onAudioCallback(final AudioTrack mAudioTrack, final byte[] chunk, final int total) {
                        Log.i(TAG, "------------- start display video");
                        // display video
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                IntervalTimer.start();
                                displayVideoAsync(path, surface);
                                Log.i(TAG, "play video time = " + IntervalTimer.getInterval());
                            }
                        }).start();
                        
                        sleep(500);
                        
                        Log.i(TAG, "------------- start display audio");
                        // display audio
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                IntervalTimer.start();
                                mAudioTrack.write(chunk, 0, total);
                                Log.i(TAG, "play audio time = " + IntervalTimer.getInterval());
//                                if (PreferenceUtil.loop(mContext)) {
//                                    mAudioTrack.flush();
//                                    sleep(1000);
//                                    run();
//                                } else {
                                    mAudioTrack.release();
//                                }
                            }
                        }).start();
                        
                    }
                });
            }
        }).start();
    }

    private void displayVideoAsync(String path, Surface surface) {
        try {
            // decode video
            decodeVideo();
        } catch (IOException e1) {
            e1.printStackTrace();
            Log.i(TAG, "decode video exception");
        }
    }

    protected void displayAudioAsync(String path, Surface surface, AudioOutputCallback audioOutputCallback) {
        try {
            // decode audio
            decodeAudio(audioOutputCallback);
        } catch (IOException e1) {
            e1.printStackTrace();
            Log.i(TAG, "decode audio exception");
        }
    }

    private void decodeVideo() throws IOException {
        final MediaExtractor extractor = new MediaExtractor();
        // set dat source
        extractor.setDataSource(mPath);
        // get video track index
        int videoIndex = getVideoIndex(extractor);
        Log.i(TAG, "videoIndex = " + videoIndex);
        // get format
        final MediaFormat format = extractor.getTrackFormat(videoIndex);
        // get mime
        final String mime = format.getString(MediaFormat.KEY_MIME);
        Log.i(TAG, "video mime = " + mime);
        // release extractor
        extractor.release();
        // create codec
        final MediaCodec codec = MediaCodec.createDecoderByType(mime);
//        codec.stop();
//        codec.release();
        // stop release in callback
        codec.setCallback(new DecodeVideoCallback(getContext(), mPath, videoIndex));
        codec.configure(format, mSurface, null, 0);
        codec.setVideoScalingMode(MediaCodec.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
        codec.start();
    }

    private void decodeAudio(AudioOutputCallback audioOutputCallback) throws IOException {
        final MediaExtractor extractor = new MediaExtractor();
        // set dat source
        extractor.setDataSource(mPath);
        // get video track index
        int audioIndex = getAudioIndex(extractor);
        Log.i(TAG, "audioIndex = " + audioIndex);
        // get format
        final MediaFormat format = extractor.getTrackFormat(audioIndex);
        // get mime
        final String mime = format.getString(MediaFormat.KEY_MIME);
        Log.i(TAG, "audio mime = " + mime);
        // release extractor
        extractor.release();
        // create codec
        final MediaCodec codec = MediaCodec.createDecoderByType(mime);
//        codec.stop();
//        codec.release();
        // stop release in callback
        codec.setCallback(new DecodeAudioCallback(getContext(), mPath, audioIndex, audioOutputCallback));
        codec.configure(format, null, null, 0);
        codec.start();
    }

    public void displayMediaSync(final String path, final Surface surface) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                displaySync(path, surface);
            }
        }).start();
    }

    private void displaySync(String path, Surface surface) {
        if (path == null || surface == null) {
            return;
        }

        setPath(path);
        setSurface(surface);

        // extract
        final MediaExtractor extractor = new MediaExtractor();
        // TODO handle data source descriptor
        try {
            extractor.setDataSource(path);
            final int trackCount = extractor.getTrackCount();
//            Log.i(TAG, "trackCount = " + trackCount);
            // get mime
            // create decoder
            // configure
            // TODO
            for (int i = 1; i < trackCount; i++) {
                // get media format
                final MediaFormat format = extractor.getTrackFormat(i); 
                final String mime = format.getString(MediaFormat.KEY_MIME);
//                Log.i(TAG, "mime = " + mime);
                if (isVideo(mime)) {
                    // decode video
//                    final int width = format.getInteger(MediaFormat.KEY_WIDTH);
//                    Log.i(TAG, "width = " + width);
//                    final int height = format.getInteger(MediaFormat.KEY_HEIGHT);
//                    Log.i(TAG, "height = " + height);
                    extractor.selectTrack(i);
                    final MediaCodec decoder = MediaCodec.createDecoderByType(mime);
                    decoder.configure(format, surface, null, 0);
                    decoder.start();
                    // double check
                    boolean isEOS = false;
                    long startTimeMillis = System.currentTimeMillis();
                    
                    
//                    ByteBuffer[] inputBuffers = decoder.getInputBuffers();
//                    ByteBuffer[] outputBuffers = decoder.getOutputBuffers();
                    BufferInfo outputBufferInfo = new BufferInfo();
                    
                    IntervalTimer.start();
//                    long sampleTime = 0;
                    for (int j = 0; j < 10000; j++) {
                        if (isEOS) {
                            break;
                        }
//                        Log.i(TAG, "sample index j = " + j);
//                        Log.i(TAG, "after Thread.interrupted() time millis = " + IntervalTimer.getInterval());
                        // get input buffer
                        final int inputBufferIndex = decoder.dequeueInputBuffer(DEQUEUE_TIMEOUT);
//                        Log.i(TAG, "===================== dequeueInputBuffer time millis = " + IntervalTimer.getInterval());
//                        Log.i(TAG, "----------------------------------------inputBufferIndex = " + inputBufferIndex);
                        if (inputBufferIndex > -1) {
                            final ByteBuffer inputBuffer = decoder.getInputBuffer(inputBufferIndex);
//                            final int capcity = inputBuffer.capacity();
//                            final int position = inputBuffer.position();
//                            final int limit = inputBuffer.limit();
//                            Log.i(TAG, "capcity = " + capcity);
//                            Log.i(TAG, "position = " + position);
//                            Log.i(TAG, "limit = " + limit);
//                            final int trackIndex = extractor.getSampleTrackIndex();
//                            final int flags = extractor.getSampleFlags();
                            final long sampleTime = extractor.getSampleTime();
//                            Log.i(TAG, "trackIndex = " + trackIndex);
//                            Log.i(TAG, "flags = " + flags);
//                            Log.i(TAG, "sampleTime = " + sampleTime);
                            // read sample
                            final int sampleSize = extractor.readSampleData(inputBuffer, 0);
//                            final int sampleFlags = extractor.getSampleFlags();
//                            Log.i(TAG, "sampleFlags = " + sampleFlags);
//                            Log.i(TAG, "============== readSampleData time millis = " + IntervalTimer.getInterval());
//                            Log.i(TAG, "----------------------------------------sampleSize = " + sampleSize);
                            if (sampleSize > 0) {
                                // is head
                                // queue input buffer
//                                sampleTime = extractor.getSampleTime();
//                                startTimeMillis = System.currentTimeMillis();
                                // TODO now, presentation time is not available
//                                final long presentationTimeUs = 100 * 1000 * 1000;
//                                final long presentationTimeUs = 0 * 1000 * 1000;
//                                final long presentationTimeUs = 0;
                                decoder.queueInputBuffer(inputBufferIndex, 0, sampleSize, sampleTime, 0);
//                                Log.i(TAG, "============== queueInputBuffer time millis = " + IntervalTimer.getInterval());
                                extractor.advance();
                            } else {
                                // is end
                                // queue end flag
                                isEOS = true;
                                Log.i(TAG, "queueInputBuffer EOS!!!!!!!!!");
                                decoder.queueInputBuffer(inputBufferIndex, 0, 0, 0, MediaCodec.BUFFER_FLAG_END_OF_STREAM);
                                // TODO consume time
                            }
                        } else {
//                            sampleTime = 0;
                        }
                        
                        
                        
                        
                        
                        // get output buffer
//                        final BufferInfo outputBufferInfo = new BufferInfo();
                        // TODO release output buffer
                        final int outputBufferIndex = decoder.dequeueOutputBuffer(outputBufferInfo, DEQUEUE_TIMEOUT);
//                        Log.i(TAG, "============== dequeueOutputBuffer time millis = " + IntervalTimer.getInterval());
//                        Log.i(TAG, "outputBufferInfo offset = " + outputBufferInfo.offset);
//                        Log.i(TAG, "----------------------------outputBufferIndex = " + outputBufferIndex);
                        if (outputBufferIndex > -1) {
                            // check output buffer
//                            final ByteBuffer outputBuffer = decoder.getOutputBuffer(outputBufferIndex);
//                            final int outputCapacity = outputBuffer.capacity();
//                            Log.i(TAG, "outputCapacity = " + outputCapacity);
//                            final MediaFormat outputFormat = decoder.getOutputFormat(outputBufferIndex);
//                            final String outputMime = outputFormat.getString(MediaFormat.KEY_MIME);
//                            Log.i(TAG, "outputMime = " + outputMime);
                            // TODO process or rendered
                            // handle play too fast
                            final long presentationTimeMillis = outputBufferInfo.presentationTimeUs / 1000;
                            final long decodeTimeMillis = System.currentTimeMillis() - startTimeMillis;
//                            Log.i(TAG, "presentationTimeMillis = " + presentationTimeMillis);
//                            Log.i(TAG, "decodeTimeMillis = " + decodeTimeMillis);
                            final long intervalTimeMillis = presentationTimeMillis - decodeTimeMillis;
                            Log.i(TAG, "-----------------------------intervalTimeMillis = " + intervalTimeMillis);
//                            Log.i(TAG, "=======================================================================");
                            if (intervalTimeMillis > 0) {
                                // sleep time????
//                                sleep(30 * 1000);
                                sleep(intervalTimeMillis);
                            }
                            decoder.releaseOutputBuffer(outputBufferIndex, true);
                            // TODO now presentation time is despited by system
                            // TODO to display
                        } else if (outputBufferIndex == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
//                            final MediaFormat outputFormatChanged = decoder.getOutputFormat();
//                            final String outputMimeChanged = outputFormatChanged.getString(MediaFormat.KEY_MIME);
//                            Log.i(TAG, "outputMimeChanged = " + outputMimeChanged);
                        } else if (outputBufferIndex == -1) {
                            // TODO handle
                            // sleep sample time
//                            if (sampleTime != 0) {
//                                sleep(sampleTime);
//                            }
                        }
                        
                        
                        
                        
                        
                        /*
                        // handle EOS
                        // TODO handle for EOS
                        final int outputFlags = outputBufferInfo.flags;
//                        Log.i(TAG, "oooooooutputFlags = " + outputFlags);
                        if ((outputFlags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
                            Log.i(TAG, "output buffer info EOS!!!!!");
                            break;
                        }
                        */
                    }
                    
                    
                    
                    
                    
                    decoder.stop();
                    decoder.release();
                } else if (isAudio(mime)) {
                    // decode audio
                    // TODO
                    
                    
                    
                    
                } else {
                    // do nothing
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        extractor.release();
        
        
        
        
        
        
        /*
        ==================
                extract
                track
                mime
                width
                height

                select track
                decode type
                configure format surface
                set scale

                input
                output

                do not loop
                dequeu
                getInputBuffers
                readSample

                mute playSpeed
                queue
                end
                loop
                display
                decoder start


                decoder stop
                decoder release
                extractor release
                ==================
                */
        
        
        
    }

    private static int getVideoIndex(final MediaExtractor extractor) {
        final int trackCount = extractor.getTrackCount();
        for (int trackIndex = 0; trackIndex < trackCount; trackIndex++) {
            final MediaFormat format = extractor.getTrackFormat(trackIndex);
            final String mime = format.getString(MediaFormat.KEY_MIME);
            if (isVideo(mime)) {
                return trackIndex;
            }
        }
        return -1;
    }

    private static int getAudioIndex(MediaExtractor extractor) {
        final int trackCount = extractor.getTrackCount();
        for (int trackIndex = 0; trackIndex < trackCount; trackIndex++) {
            final MediaFormat format = extractor.getTrackFormat(trackIndex);
            final String mime = format.getString(MediaFormat.KEY_MIME);
            if (isAudio(mime)) {
                return trackIndex;
            }
        }
        return -1;
    }



}
