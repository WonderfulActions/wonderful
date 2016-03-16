package com.letv.android.wonderful.wallpaper;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.letv.android.wonderful.OnScreenShakeListener;
import com.letv.android.wonderful.PreferenceUtil;
import com.letv.android.wonderful.Tags;
import com.letv.android.wonderful.download.DownloadVideoUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

public class WonderfulWallpaperEngine extends WallpaperService.Engine {
    // private static final String TAG = TestActivity.TAG;
//    private static final String VIDEO_PATH = MainActivity.PATH_NBA_MICHAEL_TOP1;
//    private final String SAMPLE = MainActivity.SAMPLE;
    private WonderfulWallpaperService mService;
    private int mEngineIndex;
//    private Movie mMovie;
//    private float mScaleX;
//    private float mScaleY;
    private SurfaceHolder mHolder;
    private MediaPlayer mPlayer;

    public WonderfulWallpaperEngine(WallpaperService service, int engineIndex) {
        service.super();
        mService = (WonderfulWallpaperService) service;
        mEngineIndex = engineIndex;
        /*
        try {
            // get movie
            AssetFileDescriptor descriptor = mService.getAssets().openFd(VIDEO_PATH);
            Log.i(Tags.WALLPAPER_ENGINE, mEngineIndex + "descriptor = " + descriptor);
            final InputStream inputStream = descriptor.createInputStream();
            Log.i(Tags.WALLPAPER_ENGINE, mEngineIndex + "inputStream = " + inputStream);
            mMovie = Movie.decodeStream(inputStream);
            final int duration = mMovie.duration();
            Log.i(Tags.WALLPAPER_ENGINE, mEngineIndex + "duration = " + duration);
            Log.i(Tags.WALLPAPER_ENGINE, mEngineIndex + "movie = " + mMovie);
        } catch (IOException e) {
            e.printStackTrace();
        }
        */
    }
    public void handleCommand(String command, boolean action) {
        // handle has volume
        if (command.equals(WonderfulWallpaperService.COMMAND_HAS_VOLUME)) {
            handleCommandHasVolume(action);
        }
        // handle need repeat
        if (command.equals(WonderfulWallpaperService.COMMAND_NEED_REPEAT)) {
            handleCommandNeedRepeat(action);
        }
        // handle command prepare
        if (command.equals(WonderfulWallpaperService.COMMAND_PREPARE)) {
            handleCommandPrepare();
        }
        // handle command start
        if (command.equals(WonderfulWallpaperService.COMMAND_START)) {
            handleCommandStart();
        }
        // handle command pause
        if (command.equals(WonderfulWallpaperService.COMMAND_PAUSE)) {
            handleCommandPause();
        }
        // handle command stop
        if (command.equals(WonderfulWallpaperService.COMMAND_STOP)) {
            handleCommandStop();
        }
        // handle command play next
        if (command.equals(WonderfulWallpaperService.COMMAND_PLAY_NEXT)) {
            handleCommandPlayNext();
        }
        // handle command hold down
        if (command.equals(WonderfulWallpaperService.COMMAND_HOLD_DOWN)) {
            handleCommandHoldDown();
        }
        // handle command hold up
        if (command.equals(WonderfulWallpaperService.COMMAND_HOLD_UP)) {
            handleCommandHoldUp();
        }
    }
    
    private void handleCommandHoldUp() {
        Log.i(Tags.WALLPAPER_ENGINE, mEngineIndex + "wonderful engine handleCommandHoldUp");
        // unregister shake listener
        if (isRegister) {
            unregisterShakeListener();
            isRegister = false;
        }
    }
    
    private void handleCommandHoldDown() {
        Log.i(Tags.WALLPAPER_ENGINE, mEngineIndex + "wonderful engine handleCommandHoldDown");
        // register shake listener
        final boolean isVisible = isVisible();
        if (isVisible) {
            final boolean holdShakeChange = PreferenceUtil.holdShakeChange(mService);
            if (holdShakeChange) {
                if (!isRegister) {
                    registerShakeListener();
                    isRegister = true;
                }
            }
        }
    }
    
    private boolean isRegister;
    
    private void registerShakeListener() {
        Log.i(Tags.WALLPAPER_ENGINE, mEngineIndex + "wonderful engine registerShakeListener");
        final SensorManager manager = (SensorManager) mService.getSystemService(Context.SENSOR_SERVICE);
        final Sensor sensor = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        manager.registerListener(onShakeListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void unregisterShakeListener() {
        Log.i(Tags.WALLPAPER_ENGINE, mEngineIndex + "wonderful engine unregisterShakeListener");
        final SensorManager manager = (SensorManager) mService.getSystemService(Context.SENSOR_SERVICE);
        final Sensor sensor = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        manager.unregisterListener(onShakeListener, sensor);
    }

    private final OnScreenShakeListener onShakeListener = new OnScreenShakeListener(mService) {
        @Override
        public void onLockScreenShake() {
            Log.i(Tags.WALLPAPER_ENGINE, mEngineIndex + "wonderful engine onLockScreenShake");
            handleShake();
        }
    };
    
    private void handleCommandHasVolume(boolean hasVolume) {
        /*
        Log.i(Tags.WALLPAPER_ENGINE, mEngineIndex + "wonderful engine handle command has volume = " + hasVolume);
        if (hasVolume) {
            unmute();
        } else {
            mute();
        }
        */
        switchMute();
    }
    
    private void handleShake() {
        if (isVisible()) {
            displayNextVideo();
        }
    }
    
    private void handleCommandNeedRepeat(boolean needRepeat) {
        /*
        Log.i(Tags.WALLPAPER_ENGINE, mEngineIndex + "wonderful engine handle command need repeat = " + needRepeat);
        setLooping(needRepeat);
        */
        if (mPlayer != null) {
            final boolean repeat = mPlayer.isLooping();
            mPlayer.setLooping(!repeat);
        }
    }
    
    public void setLooping(boolean needRepeat) {
        if (mPlayer != null) {
            mPlayer.setLooping(needRepeat);
        }
    }
    
    private void handleCommandPrepare() {
        Log.i(Tags.WALLPAPER_ENGINE, mEngineIndex + "wonderful engine handle command prepare");
        prepareAsync();
    }
    
    private void handleCommandStart() {
        /*
        Log.i(Tags.WALLPAPER_ENGINE, mEngineIndex + "wonderful engine handle command start");
        start();
        */
        if (mPlayer != null) {
            final boolean isPlaying = mPlayer.isPlaying();
            if (isPlaying) {
                mPlayer.pause();
            } else {
                mPlayer.start();
            }
        }
    }
    
    private void handleCommandPause() {
        /*
        Log.i(Tags.WALLPAPER_ENGINE, mEngineIndex + "wonderful engine handle command pause");
        pause();
        */
        if (mPlayer != null) {
            final boolean isPlaying = mPlayer.isPlaying();
            if (isPlaying) {
                mPlayer.pause();
            } else {
                mPlayer.start();
            }
        }
    }
    
    private void handleCommandStop() {
        Log.i(Tags.WALLPAPER_ENGINE, mEngineIndex + "wonderful engine handle command stop");
        stop();
    }
    
    private void handleCommandPlayNext() {
        Log.i(Tags.WALLPAPER_ENGINE, mEngineIndex + "wonderful engine handle command play next");
        displayNextVideo();
    }
    
    @Override
    public void onCreate(SurfaceHolder surfaceHolder) {
        Log.i(Tags.WALLPAPER_ENGINE, mEngineIndex + " " + "WonderfulWallpaperEngine onCreate");
        super.onCreate(surfaceHolder);
        // enable touch event
        setTouchEventsEnabled(true);
        instantiateMediaPlayer();
        // set volume
        setVolume();
        // register mute listener
        PreferenceUtil.registerOnSharedPreferenceChangeListener(mService, hasVolumeListener);
        // register loop listener
        PreferenceUtil.registerOnSharedPreferenceChangeListener(mService, needRepeatListener);
        
//        final int width = getDesiredMinimumWidth();
//        final int height = super.getDesiredMinimumHeight();
//        Log.i(Tags.WALLPAPER_ENGINE, mEngineIndex + "width = " + width);
//        Log.i(Tags.WALLPAPER_ENGINE, mEngineIndex + "height = " + height);
//        surfaceHolder.setFixedSize(width, height);
//        final Rect rect = new Rect(0, 0, 640, 360);
//        surfaceHolder.lockCanvas(rect);
    }
    
    private void setVolume() {
        final boolean hasVolume = PreferenceUtil.hasVolume(mService);
        if (hasVolume) {
            unmute();
        } else {
            mute();
        }
    }

    private final OnSharedPreferenceChangeListener hasVolumeListener = new OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
//            Log.i(Tags.WALLPAPER_ENGINE, mEngineIndex + "wonderful engine hasVolumeListener key = " + key);
            if (sharedPreferences != null && key != null && key.equals(PreferenceUtil.KEY_HAS_VOLUME)) {
                final boolean hasVolume = sharedPreferences.getBoolean(key, false);
                Log.i(Tags.WALLPAPER_ENGINE, mEngineIndex + "wonderful engine new changed has volume = " + hasVolume);
                if (hasVolume) {
                    unmute();
                } else {
                    mute();
                }
            }
        }
    };
    
    private final OnSharedPreferenceChangeListener needRepeatListener = new OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
//            Log.i(Tags.WALLPAPER_ENGINE, mEngineIndex + "wonderful engine  needRepeatListener key = " + key);
            if (sharedPreferences != null && key != null && key.equals(PreferenceUtil.KEY_NEED_REPEAT)) {
                final boolean needRepeat = sharedPreferences.getBoolean(key, false);
                Log.i(Tags.WALLPAPER_ENGINE, mEngineIndex + " wonderful engine new changed need repeat = " + needRepeat);
                if (mPlayer != null) {
                    mPlayer.setLooping(needRepeat);
                }
            }
        }
    };
    
    @Override
    public void onDestroy() {
        Log.i(Tags.WALLPAPER_ENGINE, mEngineIndex + " " + "WonderfulWallpaperEngine onDestroy");
        // unregister mute listener
        PreferenceUtil.unregisterOnSharedPreferenceChangeListener(mService, hasVolumeListener);
        // unregister loop listener
        PreferenceUtil.unregisterOnSharedPreferenceChangeListener(mService, needRepeatListener);
        mService.removeEngine(this);
        super.onDestroy();
    }
    
    @Override
    public Bundle onCommand(String action, int x, int y, int z, Bundle extras, boolean resultRequested) {
        Log.i(Tags.WALLPAPER_ENGINE, mEngineIndex + "wonderful engine onCommand");
        // do nothing
        return super.onCommand(action, x, y, z, extras, resultRequested);
    }
    
    
    
    
    
    
    
    
    
    
    
    @Override
    public void onVisibilityChanged(boolean visible) {
        super.onVisibilityChanged(visible);
        Log.i(Tags.WALLPAPER_ENGINE, mEngineIndex + "wonderful engine onVisibilityChanged visible = " + visible);
        delayResponseListener.onStateChanged(visible);
    }
    
    private DelayResponseListener delayResponseListener = new DelayResponseListener() {
        @Override
        public void onResponseDelay(boolean state) {
            Log.i(Tags.WALLPAPER_ENGINE, mEngineIndex + " delayResponseListener response");
            handleVisibilityChanged(state);
        }
    };
    
    protected void handleVisibilityChanged(boolean visible) {
        if (visible) {
            if (noPrepare) {
                prepareAsync();
                noPrepare = false;
            } else {
                start();
            }
        } else {
            pause();
        }
    }

    abstract class DelayResponseListener {
        private static final long DELAY_TIME = 1 * 1000;
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
                        // do not resposne
                    }
                }
            };
        };
        
        public void onStateChanged(boolean state) {
            // update visible state
            mLastState = state;
            // remove previous message
            MAIN_HANDLER.removeMessages(WHAT);
            // delay response
            final Message message = Message.obtain(null, WHAT, (mLastState ? 1: 0), -1);
            MAIN_HANDLER.sendMessageDelayed(message, DELAY_TIME);
        }
        
        public abstract void onResponseDelay(boolean state);
    }
    
    
    
    
    
    
    
    
    
    
    @Override
    public void onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        Log.i(Tags.WALLPAPER_ENGINE, mEngineIndex + "onTouchEvent event action = " + event.getAction());
        Log.i(Tags.WALLPAPER_ENGINE, mEngineIndex + "onTouchEvent event x = " + event.getX());
        Log.i(Tags.WALLPAPER_ENGINE, mEngineIndex + "onTouchEvent event y = " + event.getY());
    }

    @Override
    public void onSurfaceCreated(SurfaceHolder holder) {
        Log.i(Tags.WALLPAPER_ENGINE, mEngineIndex + " " + "WonderfulWallpaperEngine onSurfaceCreated");
        super.onSurfaceCreated(holder);
//        mHolder = holder;
        // init mediaPlayer
//        final Surface surface = holder.getSurface();
//        final Rect rect = new Rect(0, 0, 640, 360);
//        final Canvas canvas = surface.lockCanvas(rect);
        /*
        final Rect rect = holder.getSurfaceFrame();
        Log.i(Tags.WALLPAPER_ENGINE, mEngineIndex + "left = " + rect.left);
        Log.i(Tags.WALLPAPER_ENGINE, mEngineIndex + "top = " + rect.top);
        Log.i(Tags.WALLPAPER_ENGINE, mEngineIndex + "right = " + rect.right);
        Log.i(Tags.WALLPAPER_ENGINE, mEngineIndex + "bottom = " + rect.bottom);
         */
        /*
        final SurfaceTexture surfaceTexture = new SurfaceTexture(12);
        surfaceTexture.setDefaultBufferSize(640, 360);
        final Surface surface = new Surface(surfaceTexture);
        mediaPlayer.setSurface(surface);
        */
//        displayWithMediaPlayer(holder, mService);
//        displayWithMovie(holder);
        getAlbumPaths();
        registerOnLockPathChangeListener();
    }

    @Override
    public void onSurfaceDestroyed(SurfaceHolder holder) {
        Log.i(Tags.WALLPAPER_ENGINE, mEngineIndex + " " + "WonderfulWallpaperEngine onSurfaceDestroyed");
        super.onSurfaceDestroyed(holder);
        releaseMediaPlayer();
        mHolder = null;
    }
    
    @Override
    public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.i(Tags.WALLPAPER_ENGINE, mEngineIndex + " " + "WonderfulWallpaperEngine onSurfaceChanged");
        mHolder = holder;
        displayCurrentVideo();
        /*
        super.onSurfaceChanged(holder, format, width, height);
        final float movieWidth = mMovie.width();
        final float movieHeight = mMovie.height();
        Log.i(Tags.WALLPAPER_ENGINE, mEngineIndex + "movieWidth = " + movieWidth);
        Log.i(Tags.WALLPAPER_ENGINE, mEngineIndex + "movieHeight = " + movieHeight);
        Log.i(Tags.WALLPAPER_ENGINE, mEngineIndex + "holder width = " + width);
        Log.i(Tags.WALLPAPER_ENGINE, mEngineIndex + "holder height = " + height);
        mScaleX = width / movieWidth;
        mScaleY = height / movieHeight;
        Log.i(Tags.WALLPAPER_ENGINE, mEngineIndex + "mScaleX = " + mScaleX);
        Log.i(Tags.WALLPAPER_ENGINE, mEngineIndex + "mScaleY = " + mScaleY);
        displayWithMovie(holder, mScaleX, mScaleY, mMovie);
        */
//        final Canvas canvas = holder.lockCanvas();
//        canvas.rotate(0.7F);
//        canvas.save();
//        holder.unlockCanvasAndPost(canvas);
//        displayWithMediaPlayer(holder, mService);
        /*
        PlayerThread mPlayer = new PlayerThread(holder.getSurface());
        mPlayer.start();
        */

        // loop display
//        final Surface surface = holder.getSurface();
//        while (PreferenceUtil.loop(mService)) {
//            MediaCodecPlayer.getInstance().displayMediaAsync(mService, SAMPLE, surface);
//        }
    }
    
    private int mCurrentVolumeIndex;
    
    public void switchMute() {
        final AudioManager audioManager = (AudioManager) mService.getSystemService(Context.AUDIO_SERVICE);
        final int currentVolumeIndex = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        Log.i(Tags.WALLPAPER_ENGINE, mEngineIndex + "currentVolumeIndex = " + currentVolumeIndex);
        if (currentVolumeIndex != 0) {
            // mute
            mCurrentVolumeIndex = currentVolumeIndex;
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, AudioManager.FLAG_SHOW_UI);
        } else {
            // unmute
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mCurrentVolumeIndex, AudioManager.FLAG_SHOW_UI);
        }
    }
    
    public void mute() {
        final AudioManager audioManager = (AudioManager) mService.getSystemService(Context.AUDIO_SERVICE);
        final int currentVolumeIndex = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        Log.i(Tags.WALLPAPER_ENGINE, mEngineIndex + "currentVolumeIndex = " + currentVolumeIndex);
        if (currentVolumeIndex != 0) {
            mCurrentVolumeIndex = currentVolumeIndex;
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, AudioManager.FLAG_SHOW_UI);
        }
    }
    
    public void unmute() {
        if (mCurrentVolumeIndex != 0) {
            final AudioManager audioManager = (AudioManager) mService.getSystemService(Context.AUDIO_SERVICE);
            final int currentVolumeIndex = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            Log.i(Tags.WALLPAPER_ENGINE, mEngineIndex + "currentVolumeIndex = " + currentVolumeIndex);
            if (currentVolumeIndex == 0) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mCurrentVolumeIndex, AudioManager.FLAG_SHOW_UI);
            }
        }
    }
    
//    private float mVolume;
//    private float VOLUME_STEP = 0.1f;
    
    public void increase() {
//        mVolume += VOLUME_STEP;
//        mPlayer.setVolume(mVolume, mVolume);
        final AudioManager audioManager = (AudioManager) mService.getSystemService(Context.AUDIO_SERVICE);
        audioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
    }
    
    public void decrease() {
//        mVolume -= VOLUME_STEP;
//        mPlayer.setVolume(mVolume, mVolume);
        final AudioManager audioManager = (AudioManager) mService.getSystemService(Context.AUDIO_SERVICE);
        audioManager.adjustVolume(AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
    }
    
    /*
    public void loop() {
        PreferenceUtil.switchRepeatState(mService);
    }
    */
    
    private void instantiateMediaPlayer() {
        if (mPlayer == null) {
            // instantiate player
            mPlayer = new MediaPlayer();
        }
    }
    
    private void releaseMediaPlayer() {
        if (mPlayer != null) {
            // release player
            mPlayer.release();
            mPlayer = null;
        }
    }
    
    public void prepareAsync() {
        if (mPlayer != null) {
            mPlayer.prepareAsync();
        }
    }
    
    public void start() {
        if (mPlayer != null) {
            mPlayer.start();
        }
    }
    
    public void pause() {
        if (mPlayer != null) {
            mPlayer.pause();
        }
    }
    
    public void stop() {
        if (mPlayer != null) {
            mPlayer.stop();
        }
    }
    
    public void displayCurrentVideo() {
        // get video path
        final String path = getCurrentVideoPath();
        Log.i(Tags.WALLPAPER_ENGINE, mEngineIndex + "displayCurrentVideo path = " + path);
        if (path != null) {
            displayVideo(path);
        }
    }

    public void displayNextVideo() {
        // get next video path
        final String path = getNextVideoPath();
        displayVideo(path);
    }
    
    
    
    
    
    
    
    
    
    
    // ===========================================================================================
    private ArrayList<String> mLockVideoPaths = new ArrayList<String>();
    // private String mInitPath;
    private int mLockVideoPosition;
    
    private String getNextVideoPath() {
        mLockVideoPosition++;
        if (mLockVideoPosition > mLockVideoPaths.size() - 1) {
            mLockVideoPosition = 0;
        }
        return mLockVideoPaths.get(mLockVideoPosition);
    }
    
    private String getCurrentVideoPath() {
        return mLockVideoPaths.get(mLockVideoPosition);
    }
    
    private void getAlbumPaths() {
        Log.i(Tags.WALLPAPER_ENGINE, mEngineIndex + "wonderful engine getAlbumPaths");
        final String lockVideoPath = PreferenceUtil.getLockVideoPath(mService);
        Log.i(Tags.WALLPAPER_ENGINE, mEngineIndex + "wonderful engine lock video path = " + lockVideoPath);
        mLockVideoPaths.clear();
        
        final Set<String> paths = PreferenceUtil.getLockAlbumPaths(mService);
        if (paths != null) {
            for (String path : paths) {
                Log.i(Tags.WALLPAPER_ENGINE, mEngineIndex + "wonderful engine album path = " + path);
                mLockVideoPaths.add(path);
                if (lockVideoPath.equals(path)) {
                    mLockVideoPosition = mLockVideoPaths.size() - 1;
                    Log.i(Tags.WALLPAPER_ENGINE, mEngineIndex + "lockVideoPath album mPosition = " + mLockVideoPosition);
                }
            }
        } else {
            mLockVideoPaths.add(lockVideoPath);
            mLockVideoPosition = 0;
        }
    }
    
    private void registerOnLockPathChangeListener() {
        // TODO Auto-generated method stub
        PreferenceUtil.registerOnSharedPreferenceChangeListener(mService, onLockAlbumChangeListener);
    }
    
    // TODO
    private OnSharedPreferenceChangeListener onLockAlbumChangeListener = new OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (key != null && key.equals(PreferenceUtil.KEY_LOCK_ALBUM_PATHS)) {
                Log.i(Tags.WALLPAPER_ENGINE, mEngineIndex + "wonderful engine on lock album paths changed");
                /*
                final String newVideoPath = sharedPreferences.getString(key, null);
                final String currentVideoPath = mLockVideoPaths.get(mLockVideoPosition);
                */
//                if (newVideoPath != null && !newVideoPath.equals(currentVideoPath)) {
                    getAlbumPaths();
                    if (isVisible()) {
                        displayCurrentVideo();
                    } else {
                        final String path = getCurrentVideoPath();
                        Log.i(Tags.WALLPAPER_ENGINE, mEngineIndex + "!!!!!set no prepare new lock albums path = " + path);
                        setVideoSourceNoPrepare(path);
                    }
//                }
            }
        }
    };
    
    // ===========================================================================================
    
    
    
    
    
    
    
    // ======================================================================
    /*
    private static int mVideoIndex;
    
    private static String[] SAMPLE_ARRAY = TestActivity.SAMPLE_ARRAY;
    
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
    */
    // ======================================================================
    
    
    
    
    private void displayVideo(String path) {
        // reset
        mPlayer.reset();
        // init player
        configureMediaPlayer();
        // set data source
        setDataSource(path);
        // prepare async
        mPlayer.prepareAsync();
    }
    
    private boolean noPrepare;
    // just do not prepare
    private void setVideoSourceNoPrepare(String path) {
        // reset
        mPlayer.reset();
        // init player
        configureMediaPlayer();
        // set data source
        setDataSource(path);
        // prepare async
        // mPlayer.prepareAsync();
        noPrepare = true;
    }
    
    private void setDataSource(String path) {
        try {
            if (path.contains("http")) {
                final String persistentPath = DownloadVideoUtil.getCachePath(path);
                path = persistentPath;
            }
            Log.i(Tags.WALLPAPER_ENGINE, mEngineIndex + "!!!!!!!!!!!!wonderful engine displayer path = " + path);
            mPlayer.setDataSource(path);
        } catch (IllegalArgumentException | SecurityException | IllegalStateException | IOException e) {
            Log.i(Tags.WALLPAPER_ENGINE, mEngineIndex + "setDataSource exception");
            e.printStackTrace();
        }
    }
    
    public void configureMediaPlayer() {
//        Log.i(Tags.WALLPAPER_ENGINE, mEngineIndex + "configureMediaPlayer");
        // set surface
        mPlayer.setSurface(mHolder.getSurface());
        // set looping
        final boolean loop = PreferenceUtil.needRepeat(mService);
        mPlayer.setLooping(loop);
        // add callbacks
        mPlayer.setOnErrorListener(onErrorListener);
        mPlayer.setOnPreparedListener(onPreparedListener);
        mPlayer.setOnSeekCompleteListener(onSeekCompleteListener);
        mPlayer.setOnCompletionListener(onCompletionListener);
    }
    
    private MediaPlayer.OnErrorListener onErrorListener = new MediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            Log.i(Tags.WALLPAPER_ENGINE, mEngineIndex + " wonderful engine mediaplayer onError what = " + what + " extra = " + extra);
            // TODO
            
            return false;
        }
    };
    
    private MediaPlayer.OnPreparedListener onPreparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
//            Log.i(Tags.WALLPAPER_ENGINE, mEngineIndex + "wonderful engine mediaplayer onPrepared");
            mPlayer.start();
        }
    };
    
    private MediaPlayer.OnCompletionListener onCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
//            Log.i(Tags.WALLPAPER_ENGINE, mEngineIndex + "wonderful engine mediaplayer onCompletion");
            // TODO
            
        }
    };
    
    private MediaPlayer.OnSeekCompleteListener onSeekCompleteListener = new MediaPlayer.OnSeekCompleteListener() {
        @Override
        public void onSeekComplete(MediaPlayer mp) {
//            Log.i(Tags.WALLPAPER_ENGINE, mEngineIndex + "wonderful engine mediaplayer onSeekComplete");
            // TODO
            
        }
    };


    /*
    public static void displayWithMovie(SurfaceHolder holder, float scaleX, float scaleY, Movie movie) {
        // get canvas
        final Canvas canvas = holder.lockCanvas();
        // draw
        canvas.save();
        canvas.scale(scaleX, scaleY);
        movie.draw(canvas, 0, 0);
        canvas.restore();
        holder.unlockCanvasAndPost(canvas);
    }
    */

    /*
    public static void displayWithMediaPlayer(SurfaceHolder holder, WonderfulWallpaperService service) {
        final MediaPlayer mediaPlayer = new MediaPlayer();
        final Surface surface = holder.getSurface();
        mediaPlayer.setSurface(surface);
        try {
            // set data source from asset
            final AssetFileDescriptor descriptor = service.getAssets().openFd(VIDEO_PATH);
            final FileDescriptor fileDescriptor = descriptor.getFileDescriptor();
            final long offset = descriptor.getStartOffset();
            final long length = descriptor.getLength();
            mediaPlayer.setDataSource(fileDescriptor, offset, length);
            // set video scale
            mediaPlayer.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
            // start
            mediaPlayer.prepare();
            mediaPlayer.start();
            final int width = mediaPlayer.getVideoWidth();
            final int height = mediaPlayer.getVideoHeight();
            Log.i(Tags.WALLPAPER_ENGINE, mEngineIndex + "video width = " + width);
            Log.i(Tags.WALLPAPER_ENGINE, mEngineIndex + "video height = " + height);
        } catch (IllegalArgumentException | SecurityException | IllegalStateException | IOException e) {
            e.printStackTrace();
        }
    }
    */

}
