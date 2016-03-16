
package com.letv.android.wonderful.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.WallpaperInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.AssetFileDescriptor;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaCodec;
import android.media.MediaCodec.BufferInfo;
import android.media.MediaCodec.Callback;
import android.media.MediaCodec.CodecException;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.PowerManager.WakeLock;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Button;

import com.letv.android.wonderful.MediaCodecPlayer;
import com.letv.android.wonderful.PortraitActivity;
import com.letv.android.wonderful.PreferenceUtil;
import com.letv.android.wonderful.R;
import com.letv.android.wonderful.Tags;
import com.letv.android.wonderful.WallpaperCommand;
import com.letv.android.wonderful.wallpaper.WonderfulWallpaperService;

import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.List;

public class TestActivity extends Activity {
    public static final String TAG = Tags.WONDERFUL_VIDEO;

//    private final String SAMPLE = Environment.getExternalStorageDirectory() + "/goalWallpaper" + "/ver_00_22_2_2_1_1233092_1191732[1].ts";
    public static final String SAMPLE = "/sdcard/wonderful/michaelTop1.ts";

    private static final String SAMPLE1 = "/sdcard/wonderful/michaelTop1.ts";
    private static final String SAMPLE2 = "/sdcard/wonderful/michaelTop2.ts";
    private static final String SAMPLE3 = "/sdcard/wonderful/michaelTop3.ts";
    private static final String SAMPLE4 = "/sdcard/wonderful/michaelTop4.ts";
    
    public static String[] SAMPLE_ARRAY = new String[] {
            SAMPLE1,
            SAMPLE2,
            SAMPLE3,
            SAMPLE4,
    };
    
    private static final String SD_VIDEO_PATH = SAMPLE;
    /*
    private static final String VIDEO_PATH = "/sdcard/goalWallpaper/movieSoftboy.avi";
    private static final String VIDEO_PATH = "/sdcard/goalWallpaper/ver_00_22_2_2_1_1233092_1191732[1].ts";
    */

    private static final String PATH_MOVIE = "movie";
    private static final String PATH_MV = "mv";
    private static final String PATH_SOCCER = "soccer";
    private static final String PATH_NBA = "nba";
    private static final String PATH_MOVIE_SOFT = "movie/Softboy.avi";
    private static final String PATH_SOCCER_WORLD_CUP = "soccer/worldcupMV.mp4";
    public static final String PATH_NBA_MICHAEL_TOP1 = "nba/michaelTop1.ts";
    private static final String PATH_NBA_MICHAEL_FADE_AWAY = "nba/michael_fade_away.gif";
    private static final String PATH_NBA_MICHAEL_SHOOT = "nba/michael_shoot.gif";
    public static final String VIDEO_PATH = PATH_SOCCER_WORLD_CUP;
    private SurfaceView mSurfaceView;
    private MediaPlayer mPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        mSurfaceView = (SurfaceView) findViewById(R.id.surface_view);
        mSurfaceView.getHolder().addCallback(callback);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        final Button button = (Button) findViewById(R.id.has_volume);
        button.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.i(TAG, "event action = " + event.getActionMasked());
                return false;
            }
        });
    }
    
    WakeLock wakeLock;
    
    @Override
    protected void onResume() {
        super.onResume();
        /*
        final PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE, "screen onnnnnn");
        wakeLock.acquire();
        Log.i(TAG, "wakeLock.acquire()");
        */
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        /*
        if (wakeLock != null) {
            wakeLock.release();
            Log.i(TAG, "wakeLock.release()");
        }
        */
    }
    
    private static final String WONDERFUL_WALLPAPER_PACKAGE_NAME = "com.letv.android.wonderful";
    private static final String WONDERFUL_WALLPAPER_SERVICE_CLASS_NAME = "com.letv.android.wonderful.wallpaper.WonderfulWallpaperService";
    
    // TODO isServiceRunning
//    private static final String COMMAND_KEY = "command";
    public static final String COMMAND_HAS_VOLUME = "hasVolume";
    public static final String COMMAND_NEED_REPEAT = "needRepeat";
    public static final String COMMAND_PREPARE = "prepare";
    public static final String COMMAND_START = "start";
    public static final String COMMAND_PAUSE = "pause";
    public static final String COMMAND_STOP = "stop";
    public static final String COMMAND_PLAY_NEXT = "playNext";
    
    private boolean hasVolume;
    
    public void hasVolume(View view) {
        hasVolume = !hasVolume;
        final WallpaperCommand command = new WallpaperCommand(COMMAND_HAS_VOLUME, hasVolume);
        sendWallpaperCommand(this, command);
    }
    
    private boolean needRepeat;
    
    public void needRepeat(View view) {
        needRepeat = !needRepeat;
        final WallpaperCommand command = new WallpaperCommand(COMMAND_NEED_REPEAT, needRepeat);
        sendWallpaperCommand(this, command);
    }
    
    public void wallpaperPrepare(View view) {
        final WallpaperCommand command = new WallpaperCommand(COMMAND_PREPARE, true);
        sendWallpaperCommand(this, command);
    }
    
    public void wallpaperStart(View view) {
        final WallpaperCommand command = new WallpaperCommand(COMMAND_START, true);
        sendWallpaperCommand(this, command);
    }
    
    public void wallpaperPause(View view) {
        final WallpaperCommand command = new WallpaperCommand(COMMAND_PAUSE, true);
        sendWallpaperCommand(this, command);
    }
    
    public void wallpaperStop(View view) {
        final WallpaperCommand command = new WallpaperCommand(COMMAND_STOP, true);
        sendWallpaperCommand(this, command);
    }
    
    public void wallpaperPlayNext(View view) {
        final WallpaperCommand command = new WallpaperCommand(COMMAND_PLAY_NEXT, true);
        sendWallpaperCommand(this, command);
    }
    
    public static void sendWallpaperCommand(Context context, WallpaperCommand command) {
        // is service running
        final boolean isServiceRunning = isServiceRunning(context, WONDERFUL_WALLPAPER_PACKAGE_NAME, WONDERFUL_WALLPAPER_SERVICE_CLASS_NAME);
        if (isServiceRunning) {
            // if running, send command
            final Intent intent = new Intent();
            intent.putExtra(command.KEY, command.ACTION);
            final ComponentName componentName = new ComponentName(WONDERFUL_WALLPAPER_PACKAGE_NAME, WONDERFUL_WALLPAPER_SERVICE_CLASS_NAME);
            intent.setComponent(componentName);
            context.startService(intent);
        }
    }
    
    public static boolean isServiceRunning(Context context, String packageName, String className) {
        final ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        final List<RunningServiceInfo> serviceInfos = manager.getRunningServices(999);
        if (serviceInfos != null && serviceInfos.size() != 0) {
            for (int i = 0; i < serviceInfos.size(); i++) {
               final ComponentName info = serviceInfos.get(i).service;
                if (packageName.equals(info.getPackageName()) && className.equals(info.getClassName())) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private static int mVideoIndex;
    
    private static String getCurrentVideoPath() {
        return SAMPLE_ARRAY[mVideoIndex];
    }
    
    private static String getNextVideoPath() {
        mVideoIndex++;
        if (mVideoIndex > SAMPLE_ARRAY.length - 1) {
            mVideoIndex = 0;
        }
        return SAMPLE_ARRAY[mVideoIndex];
    }

    private MediaPlayer.OnErrorListener onErrorListener = new MediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            Log.i(TAG, "onError what = " + what + " extra = " + extra);
            // TODO
            
            return false;
        }
    };
    
    private MediaPlayer.OnPreparedListener onPreparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
            Log.i(TAG, "onPrepared");
            // TODO
            mPlayer.start();
        }
    };
    
    private MediaPlayer.OnCompletionListener onCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            Log.i(TAG, "onCompletion");
            // TODO
            
        }
    };
    
    private MediaPlayer.OnSeekCompleteListener onSeekCompleteListener = new MediaPlayer.OnSeekCompleteListener() {
        @Override
        public void onSeekComplete(MediaPlayer mp) {
            Log.i(TAG, "onSeekComplete");
            // TODO
            
        }
    };
    
    private SurfaceHolder.Callback callback = new SurfaceHolder.Callback() {
        
        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
//            Log.i(TAG, "surfaceDestroyed");
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
//            Log.i(TAG, "surfaceCreated");
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//            Log.i(TAG, "surfaceChanged!!!!!!!!!!!!!!!!!!!");
//            Log.i(TAG, "surfaceChanged width = " + width);
//            Log.i(TAG, "surfaceChanged height = " + height);
        }
    };
    
    public void settings(View view) {
        final Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
    
    public void setAsWallpaper(View view) {
        Log.i(TAG, "setAsWallpaper");
        showAsLiveWallpaper();
    }

    private void showAsLiveWallpaper() {
        try {
            // get wallpaperInfo
            final Intent wallpaperServiceIntent = new Intent(this, WonderfulWallpaperService.class);
            final ResolveInfo resolveInfo = getPackageManager().resolveService(wallpaperServiceIntent, PackageManager.GET_META_DATA);
            final WallpaperInfo wallpaperInfo = new WallpaperInfo(this, resolveInfo);
            // set live intent
            final Intent liveIntent = new Intent(WallpaperService.SERVICE_INTERFACE);
            liveIntent.setClassName(wallpaperInfo.getPackageName(), wallpaperInfo.getServiceName());
            // start live picker activity
            final Intent pickerIntent = new Intent();
            final ComponentName pickerComponent = new ComponentName(LIVE_PICKER_PACKAGE_NAME, LIVE_PICKER_CLASS_NAME);
            pickerIntent.setComponent(pickerComponent);
            pickerIntent.putExtra(EXTRA_LIVE_WALLPAPER_INTENT, liveIntent);
            startActivity(pickerIntent);
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
    }
    
    private int mCurrentVolumeIndex;
    
    public void mute(View view) {
        muteSwitch();
    }

    private void muteSwitch() {
        final AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        
        final int currentVolumeIndex = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        Log.i(TAG, "currentVolumeIndex = " + currentVolumeIndex);
        if (currentVolumeIndex != 0) {
            // mute
            mCurrentVolumeIndex = currentVolumeIndex;
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, AudioManager.FLAG_SHOW_UI);
        } else {
            // unmute
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mCurrentVolumeIndex, AudioManager.FLAG_SHOW_UI);
        }
    }
    
//    private float mVolume;
//    private float VOLUME_STEP = 0.1f;
    
    public void increase(View view) {
//        mVolume += VOLUME_STEP;
//        mPlayer.setVolume(mVolume, mVolume);
        final AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
    }
    
    public void decrease(View view) {
//        mVolume -= VOLUME_STEP;
//        mPlayer.setVolume(mVolume, mVolume);
        final AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.adjustVolume(AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
    }
    
    public void repeat(View view) {
        PreferenceUtil.switchRepeatState(this);
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    private void instantiateMediaPlayer() {
        if (mPlayer == null) {
            // instantiate player
            mPlayer = new MediaPlayer();
            // register loop listener
//            Log.i(TAG, "register loopListener");
            PreferenceUtil.registerOnSharedPreferenceChangeListener(this, needRepeatListener);
        }
    }
    
    private final OnSharedPreferenceChangeListener needRepeatListener = new OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
//            Log.i(TAG, "needRepeatListener key = " + key);
            if (sharedPreferences != null && key != null && key.equals(PreferenceUtil.KEY_NEED_REPEAT)) {
                final boolean needRepeat = sharedPreferences.getBoolean(key, false);
//                Log.i(TAG, "new changed needRepeat = " + needRepeat);
                if (mPlayer != null) {
                    mPlayer.setLooping(needRepeat);
                }
            }
        }
    };
    
    public void instantiate(View view) {
        instantiateMediaPlayer();
    }
    
    public void release(View view) {
        releaseMediaPlayer();
    }

    private void releaseMediaPlayer() {
        if (mPlayer != null) {
            // unregister loop listener
            PreferenceUtil.unregisterOnSharedPreferenceChangeListener(this, needRepeatListener);
            // release player
            mPlayer.release();
            mPlayer = null;
        }
    }
    
    public void configureMediaPlayer() {
        Log.i(TAG, "initMediaPlayer");
        // set surface
        mPlayer.setSurface(mSurfaceView.getHolder().getSurface());
        // set looping
        final boolean loop = PreferenceUtil.needRepeat(this);
        mPlayer.setLooping(loop);
        // add callbacks
        mPlayer.setOnErrorListener(onErrorListener);
        mPlayer.setOnPreparedListener(onPreparedListener);
        mPlayer.setOnSeekCompleteListener(onSeekCompleteListener);
        mPlayer.setOnCompletionListener(onCompletionListener);
    }
    
    public void configure(View view) {
        configureMediaPlayer();
    }
    
    public void setDataSource(View view) {
        final String path = getCurrentVideoPath();
        setDataSource(path);
    }

    private void setDataSource(final String path) {
        try {
            mPlayer.setDataSource(path);
        } catch (IllegalArgumentException | SecurityException | IllegalStateException | IOException e) {
            Log.i(TAG, "setDataSource exception");
            e.printStackTrace();
        }
    }
    
    public void prepare(View view) {
        mPlayer.prepareAsync();
    }
    
    public void start(View view) {
        mPlayer.start();
    }
    
    public void pause(View view) {
        mPlayer.pause();
    }
    
    public void stop(View view) {
        mPlayer.stop();
    }
    
    public void displayCurrent(View view) {
        Log.i(TAG, "displayCurrentVideo");
        // get video path
        final String path = getCurrentVideoPath();
        displayVideo(path);
    }

    public void displayNext(View view) {
        // get next video path
        final String path = getNextVideoPath();
        displayVideo(path);
    }
    
    private void displayVideo(final String path) {
        // reset
        mPlayer.reset();
        // init player
        configureMediaPlayer();
        try {
            // set data source
            mPlayer.setDataSource(path);
            // prepare async
            mPlayer.prepareAsync();
        } catch (IllegalArgumentException | SecurityException | IllegalStateException | IOException e) {
            Log.i(TAG, "setDataSource exception");
            e.printStackTrace();
        }
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    private static final int STREAM_TYPE = AudioManager.STREAM_MUSIC;
    private static final int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
    private static final int MODE = AudioTrack.MODE_STREAM;
    private static final int BUFFER_SIZE = 50 * 1024 * 1024;
    private static final int CHANNEL_CONFIG = AudioFormat.CHANNEL_OUT_MONO;
    private static final String AUDIO_PATH = "/sdcard/softheart.mp3";
    int mAudioIndex = 0;
    
    public void playAudioInAudioTrack(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    MediaExtractor mExtractor = new MediaExtractor();
                    mExtractor.setDataSource(AUDIO_PATH);
                    mExtractor.selectTrack(mAudioIndex);
                    final MediaFormat format = mExtractor.getTrackFormat(mAudioIndex);
                    final String mime = format.getString(MediaFormat.KEY_MIME);
                    Log.i(TAG, "audio mime = " + mime);
                    final int sampleRate = format.getInteger(MediaFormat.KEY_SAMPLE_RATE);
                    int bufferSize = AudioTrack.getMinBufferSize(sampleRate, CHANNEL_CONFIG, AUDIO_FORMAT);
                    Log.i(TAG, "sampleRate = " + sampleRate);
                    Log.i(TAG, "bufferSize = " + bufferSize);
                    /*
                     * AudioTrack mAudioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, sampleRate,
                     * CHANNEL_CONFIG, AUDIO_FORMAT, BUFFER_SIZE, MODE);
                     */

                    AudioTrack audioTrack = new AudioTrack(
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
                    audioTrack.play();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    public void displayInMediaCodec(View view) {
        final String path = SAMPLE;
        final SurfaceHolder surfaceHolder = mSurfaceView.getHolder();
        final Surface surface = surfaceHolder.getSurface();
//        MediaCodecPlayer.getInstance().displayVideoAsync(path, surface);
        MediaCodecPlayer.getInstance().displayMediaAsync(this, path, surface);
    }

    private static final String LIVE_PICKER_PACKAGE_NAME = "com.android.wallpaper.livepicker";
    private static final String LIVE_PICKER_CLASS_NAME = "com.android.wallpaper.livepicker.LiveWallpaperPreview";
    private static final String EXTRA_LIVE_WALLPAPER_INTENT = "android.live_wallpaper.intent";

    public static final int PICK_LIVE_WALLPAPER = 7;

    public void startActivity(View view) {
        startActivity(new Intent(this, PortraitActivity.class));
    }

    private static final int VIDEO_WIDTH = 1280;
    private static final int VIDEO_HEIGHT = 720;

    private static final String VIDEO_TYPE = MediaFormat.MIMETYPE_VIDEO_MPEG4;

    private void displayVideoWithMediaCodec(SurfaceHolder holder) {
        final Surface surface = holder.getSurface();
        final MediaCodecList mediaCodecList = new MediaCodecList(MediaCodecList.REGULAR_CODECS);
        final MediaCodecInfo[] mediaCodecInfos = mediaCodecList.getCodecInfos();
        // get codecName
        // TODO optimize with MediaExtractor
        final MediaExtractor mediaExtractor = new MediaExtractor();
        String mimeee = null;
        int widthhh = 0;
        int heighttt = 0;
        try {
            final AssetFileDescriptor descriptor = getAssets().openFd(VIDEO_PATH);
            mediaExtractor.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
            final int count = mediaExtractor.getTrackCount();
            Log.i(TAG, "track count = " + count);
            for (int i = 0; i < 1; i++) {
                final MediaFormat mediaFormat = mediaExtractor.getTrackFormat(i);
                final String mime = mediaFormat.getString(MediaFormat.KEY_MIME);
                final int width = mediaFormat.getInteger(MediaFormat.KEY_WIDTH);
                final int height = mediaFormat.getInteger(MediaFormat.KEY_HEIGHT);
                mimeee = mime;
                widthhh = width;
                heighttt = height;
                Log.i(TAG, "track mime = " + mime);
                Log.i(TAG, "widthhh = " + widthhh);
                Log.i(TAG, "heighttt = " + heighttt);
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        
        
        String mCodecName = null;
        MediaFormat mMediaFormat = null;
        for (MediaCodecInfo mediaCodecInfo : mediaCodecInfos) {
            if (!mediaCodecInfo.isEncoder()) {
                final String codecName = mediaCodecInfo.getName();
//                Log.i(TAG, "other codecName = " + codecName);
                final String[] types = mediaCodecInfo.getSupportedTypes();
                for (String type : types) {
                    if (type.equals(VIDEO_TYPE)) {
//                        Log.i(TAG, "==========================================================");
                        Log.i(TAG, "!!!!!!codecName = " + codecName);
                        Log.i(TAG, "!!!!!!supported type = " + type);
                        mCodecName = codecName;
                        final MediaCodecInfo.CodecCapabilities codecCapabilities = mediaCodecInfo.getCapabilitiesForType(type);
                        final MediaFormat mediaFormat = codecCapabilities.getDefaultFormat();
                        final String mime = mediaFormat.getString(MediaFormat.KEY_MIME);
                        Log.i(TAG, "mime = " + mime);
                        mMediaFormat = mediaFormat;
                        break;
                    }
                }
            }
            if (mCodecName != null) {
                break;
            }
        }
        try {
            // create codec
//            final MediaCodec mediaCodec = MediaCodec.createByCodecName(mCodecName);
            final MediaCodec mediaCodec = MediaCodec.createDecoderByType(VIDEO_TYPE);
            mediaCodec.setCallback(mediaCodecCallback);
            final MediaFormat formattt = MediaFormat.createVideoFormat(mimeee, widthhh, heighttt);
            mediaCodec.configure(formattt, surface, null, 0);
            mediaCodec.setVideoScalingMode(MediaCodec.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
            mediaCodec.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private MediaCodec.Callback mediaCodecCallback = new Callback() {
        
        @Override
        public void onOutputFormatChanged(MediaCodec codec, MediaFormat format) {
            // TODO Auto-generated method stub
            
        }
        
        @Override
        public void onOutputBufferAvailable(MediaCodec codec, int index, BufferInfo info) {
            Log.i(TAG, "mediaCodecCallback onOutputBufferAvailable");
            codec.releaseOutputBuffer(index, true);
            codec.dequeueInputBuffer(-1);
        }
        
        @Override
        public void onInputBufferAvailable(MediaCodec codec, int index) {
            Log.i(TAG, "onInputBufferAvailable");
            final ByteBuffer inputBufferrrr = codec.getInputBuffer(index);
//            Log.i(TAG, "inputBufferrrr capacity = " + inputBufferrrr.capacity());
            try {
                /*
                // input data from asset
                final AssetFileDescriptor descriptor = getAssets().openFd(VIDEO_PATH);
                final long length = descriptor.getLength();
                final long offset = descriptor.getStartOffset();
                Log.i(TAG, "asset offset = " + offset);
                Log.i(TAG, "asset length = " + length);
                final FileInputStream fileInputStream = descriptor.createInputStream();
                */
                final FileInputStream fileInputStream = new FileInputStream(SD_VIDEO_PATH);
                byte[] bufferrrrr = new byte[500 * 1024];
                int len = -1;
                if ((len = fileInputStream.read(bufferrrrr)) != -1) {
                    // outputStream.write(buffer, 0, len);
                    inputBufferrrr.clear();
                    inputBufferrrr.put(bufferrrrr, 0, len);
                    codec.queueInputBuffer(index, 0, len, 5 * 1000 * 1000, 0);
                }
                fileInputStream.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        
        @Override
        public void onError(MediaCodec codec, CodecException e) {
            // TODO Auto-generated method stub
            
        }
    };

    public static byte[] getByteArray(InputStream inputStream) {
        if (inputStream != null) {
            try {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[10 * 1024];
                int len = -1;
                while ((len = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, len);
                }
                byte[] byteArray = outputStream.toByteArray();
                outputStream.close();
                return byteArray;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private void playGif(final SurfaceHolder holder) {
        // get movie
        try {
            AssetFileDescriptor descriptor = this.getAssets().openFd(VIDEO_PATH);
            Log.i(TAG, "descriptor = " + descriptor);
            final InputStream inputStream = descriptor.createInputStream();
            Log.i(TAG, "inputStream = " + inputStream);
            Movie movie = Movie.decodeStream(inputStream);
            final float movieWidth = movie.width();
            final float movieHeight = movie.height();
            final Canvas canvas = holder.lockCanvas();
            final int width = canvas.getWidth();
            final int height = canvas.getHeight();
            holder.unlockCanvasAndPost(canvas);
            Log.i(TAG, "movie width = " + movieWidth);
            Log.i(TAG, "movie height = " + movieHeight);
            Log.i(TAG, "holder width = " + width);
            Log.i(TAG, "holder height = " + height);
            float scaleX = width / movieWidth;
            float scaleY = height / movieHeight;
//            WonderfulWallpaperEngine.displayWithMovie(holder, scaleX, scaleY, movie);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void displayVideo(SurfaceHolder holder) {
        Log.i(TAG, "displayVideo");
//        final Canvas lockCanvas = holder.lockCanvas();
//        holder.setFixedSize(640, 360);
        displayVideoInFixSize(holder);
//        holder.setFixedSize(640, 360);
//        holder.unlockCanvasAndPost(lockCanvas);
    }

    private void displayVideoInFixSize(SurfaceHolder holder) {
        Log.i(TAG, "displayVideoWithMediaPlayer");
        // init mediaPlayer
//        final Canvas canvas = holder.lockCanvas();
//        holder.setFixedSize(640, 360);
//        canvas.clipRect(0, 0, 640, 360);
//        holder.unlockCanvasAndPost(canvas);
//        displayWithMediaPlayer(holder);
    }

    private void displayWithMediaPlayer(SurfaceHolder holder) {
        final MediaPlayer mediaPlayer = new MediaPlayer();
//        mediaPlayer.setDisplay(holder);
        final Surface surface = holder.getSurface();
        mediaPlayer.setSurface(surface);
        try {
            /*
            // set data source from asset
            final AssetFileDescriptor descriptor = getAssets().openFd(VIDEO_PATH);
            final FileDescriptor fileDescriptor = descriptor.getFileDescriptor();
            final long offset = descriptor.getStartOffset();
            final long length = descriptor.getLength();
            mediaPlayer.setDataSource(fileDescriptor, offset, length);
            */
            // set data source from disk path
            mediaPlayer.setDataSource(SAMPLE);
            // set scale mode
            mediaPlayer.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
            // start
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IllegalArgumentException | SecurityException | IllegalStateException | IOException e) {
            e.printStackTrace();
        }
        /*
        final int width = mediaPlayer.getVideoWidth();
        final int height = mediaPlayer.getVideoHeight();
        Log.i(TAG, "video width = " + width);
        Log.i(TAG, "video height = " + height);
        */
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}
