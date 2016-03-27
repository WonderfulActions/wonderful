package com.letv.android.wonderful.wallpaper;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.FileObserver;
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
import com.letv.android.wonderful.application.WonderfulApplication;
import com.letv.android.wonderful.download.DownloadVideoUtil;
import com.letv.android.wonderful.util.DateUtil;
import com.letv.android.wonderful.util.LockVideoUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

public class WonderfulWallpaperEngine extends WallpaperService.Engine {
    // private static final String TAG = TestActivity.TAG;
//    private static final String VIDEO_PATH = MainActivity.PATH_NBA_MICHAEL_TOP1;
//    private final String SAMPLE = MainActivity.SAMPLE;
    private WonderfulWallpaperService mService;
    private int mEngineIndex;
    private String mTimeLabel;
//    private Movie mMovie;
//    private float mScaleX;
//    private float mScaleY;
//    private SurfaceHolder mHolder;
    private MediaPlayer mPlayer;

    public WonderfulWallpaperEngine(WallpaperService service, int engineIndex) {
        service.super();
//        Log.i(Tags.WALLPAPER_ENGINE, "WonderfulWallpaperEngine construct");
        mService = (WonderfulWallpaperService) service;
        mEngineIndex = engineIndex;
        mTimeLabel = DateUtil.getTimeLabel();
        Log.i(Tags.WALLPAPER_ENGINE, mEngineIndex + " mTimeLabel = " + mTimeLabel);
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
        // TODO temp test
        getSurfaceHolder();
        isPreview();
        // touch event
        // command
        // getSurfaceHolder
        // isPreview
        //





        // TODO temp test


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
            final boolean holdShakeChange = PreferenceUtil.holdShakeChange(WonderfulApplication.mContext);
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
        Log.i(Tags.WALLPAPER_ENGINE, mEngineIndex + " wonderful engine registerShakeListener");
        final SensorManager manager = (SensorManager) WonderfulApplication.mContext.getSystemService(Context.SENSOR_SERVICE);
        final Sensor sensor = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        manager.registerListener(onShakeListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void unregisterShakeListener() {
        Log.i(Tags.WALLPAPER_ENGINE, mEngineIndex + " wonderful engine unregisterShakeListener");
        final SensorManager manager = (SensorManager) WonderfulApplication.mContext.getSystemService(Context.SENSOR_SERVICE);
        final Sensor sensor = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        manager.unregisterListener(onShakeListener, sensor);
    }

    private final OnScreenShakeListener onShakeListener = new OnScreenShakeListener(WonderfulApplication.mContext) {
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
        } else {
            displayCurrentVideo();
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
        Log.i(Tags.WALLPAPER_ENGINE, mEngineIndex + " WonderfulWallpaperEngine onCreate");
        super.onCreate(surfaceHolder);
        // enable touch event
        setTouchEventsEnabled(true);
//        instantiateMediaPlayer();
        // set volume
        // register mute listener
        setVolume();
        PreferenceUtil.registerOnSharedPreferenceChangeListener(WonderfulApplication.mContext, hasVolumeListener);
        // register loop listener
        PreferenceUtil.registerOnSharedPreferenceChangeListener(WonderfulApplication.mContext, needRepeatListener);
        
//        final int width = getDesiredMinimumWidth();
//        final int height = super.getDesiredMinimumHeight();
//        Log.i(Tags.WALLPAPER_ENGINE, mEngineIndex + "width = " + width);
//        Log.i(Tags.WALLPAPER_ENGINE, mEngineIndex + "height = " + height);
//        surfaceHolder.setFixedSize(width, height);
//        final Rect rect = new Rect(0, 0, 640, 360);
//        surfaceHolder.lockCanvas(rect);
//        Log.i(Tags.WALLPAPER_ENGINE, "return onCreate");
    }
    
    private void setVolume() {
        final boolean hasVolume = PreferenceUtil.hasVolume(WonderfulApplication.mContext);
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
        PreferenceUtil.unregisterOnSharedPreferenceChangeListener(WonderfulApplication.mContext, hasVolumeListener);
        // unregister loop listener
        PreferenceUtil.unregisterOnSharedPreferenceChangeListener(WonderfulApplication.mContext, needRepeatListener);
        mService.removeEngine(this);
        super.onDestroy();
    }
    
    @Override
    public Bundle onCommand(String action, int x, int y, int z, Bundle extras, boolean resultRequested) {
        Log.i(Tags.WALLPAPER_ENGINE, mEngineIndex + " wonderful engine onCommand");
        // do nothing
        return super.onCommand(action, x, y, z, extras, resultRequested);
    }








    /*
    abstract class DelayResponseListener {
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
            };
        };

        public void onStateChanged(boolean state, long delay) {
            // update visible state
            mLastState = state;
            // remove previous message
            MAIN_HANDLER.removeMessages(WHAT);
            // delay response
            final Message message = Message.obtain(null, WHAT, (mLastState ? 1: 0), -1);
            MAIN_HANDLER.sendMessageDelayed(message, delay);
        }

        public abstract void onResponseDelay(boolean state);
    }
*/
    
    private static final long VISIBLE_DELAY = 30;

    @Override
    public void onVisibilityChanged(boolean visible) {
        super.onVisibilityChanged(visible);
        Log.i(Tags.WALLPAPER_ENGINE, mEngineIndex + " wonderful engine onVisibilityChanged visible = " + visible);
        delayResponseListener.onStateChanged(visible, VISIBLE_DELAY);
//        handleVisibilityChanged(visible);
    }
    
    private DelayResponseListener delayResponseListener = new DelayResponseListener() {
        @Override
        public void onResponseDelay(boolean state) {
            Log.i(Tags.WALLPAPER_ENGINE, mEngineIndex + " --- delayResponseListener response visible = " + state);
            handleVisibilityChanged(state);
        }
    };
    
    protected void handleVisibilityChanged(boolean visible) {
        final boolean isPreview = isPreview();
//        Log.i(Tags.WALLPAPER_ENGINE, "isPreview = " + isPreview);
        if (visible) {
            // visible
//            if (noPrepare) {
//                prepareAsync();
//                noPrepare = false;
//            } else {
//                start();

//            }
            // TODO replay current video
            displayCurrentVideo();
        } else {
            // invisible
//            pause();
            // TODO release mediaPlayer
            releaseMediaPlayer();
        }
    }


    
    
    
    
    
    
    
    
    @Override
    public void onTouchEvent(MotionEvent event) {
//        super.onTouchEvent(event);
        Log.i(Tags.WALLPAPER_ENGINE, mEngineIndex + " onTouchEvent event action = " + event.getAction());
        Log.i(Tags.WALLPAPER_ENGINE, mEngineIndex + " onTouchEvent event x = " + event.getX());
        Log.i(Tags.WALLPAPER_ENGINE, mEngineIndex + " onTouchEvent event y = " + event.getY());
    }

    @Override
    public void onSurfaceCreated(SurfaceHolder holder) {
        Log.i(Tags.WALLPAPER_ENGINE, mEngineIndex + " " + "WonderfulWallpaperEngine onSurfaceCreated");
//        super.onSurfaceCreated(holder);
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
//        registerOnLockPathChangeListener();
        observeLockVideoSetting();
    }

    private void observeLockVideoSetting() {
        Log.i(Tags.WALLPAPER_ENGINE, "--- observeLockVideoSetting");
        final File settingFile = LockVideoUtil.generateLockVideoSetting();
        if (settingFile != null) {
            final String settingPath = settingFile.getAbsolutePath();
            final FileObserver observer = new FileObserver(settingPath) {
                @Override
                public void onEvent(int event, String path) {
//                    Log.i(Tags.WALLPAPER_ENGINE, "--- observeLockVideoSetting event = " + event);
                    if (event == FileObserver.CLOSE_WRITE) {
                        Log.i(Tags.WALLPAPER_ENGINE, "--- observeLockVideoSetting close write path = " + path);
                        onLockPathsChanged();
                    }
                }
            };
            observer.startWatching();
        }
    }

    private void onLockPathsChanged() {
        // get paths
        // if visible, display current video
        getAlbumPaths();
        if (isVisible()) {
            displayCurrentVideo();
        }
    }

    @Override
    public void  onSurfaceDestroyed(SurfaceHolder holder) {
        Log.i(Tags.WALLPAPER_ENGINE, mEngineIndex + " " + "WonderfulWallpaperEngine onSurfaceDestroyed");
        super.onSurfaceDestroyed(holder);
        releaseMediaPlayer();
//        mHolder = null;
    }

    /*
    @Override
    public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.i(Tags.WALLPAPER_ENGINE, mEngineIndex + " " + "WonderfulWallpaperEngine onSurfaceChanged");
//        mHolder = holder;
//        displayCurrentVideo();
        *//*
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
        *//*
//        final Canvas canvas = holder.lockCanvas();
//        canvas.rotate(0.7F);
//        canvas.save();
//        holder.unlockCanvasAndPost(canvas);
//        displayWithMediaPlayer(holder, mService);
        *//*
        PlayerThread mPlayer = new PlayerThread(holder.getSurface());
        mPlayer.start();
        *//*

        // loop display
//        final Surface surface = holder.getSurface();
//        while (PreferenceUtil.loop(mService)) {
//            MediaCodecPlayer.getInstance().displayMediaAsync(mService, SAMPLE, surface);
//        }

        // TODO display current











    }
    */
    
    private int mCurrentVolumeIndex;
    
    public void switchMute() {
        final AudioManager audioManager = (AudioManager) WonderfulApplication.mContext.getSystemService(Context.AUDIO_SERVICE);
        final int currentVolumeIndex = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
//        Log.i(Tags.WALLPAPER_ENGINE, mEngineIndex + " currentVolumeIndex = " + currentVolumeIndex);
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
        final AudioManager audioManager = (AudioManager) WonderfulApplication.mContext.getSystemService(Context.AUDIO_SERVICE);
        final int currentVolumeIndex = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
//        Log.i(Tags.WALLPAPER_ENGINE, mEngineIndex + " currentVolumeIndex = " + currentVolumeIndex);
        if (currentVolumeIndex != 0) {
            mCurrentVolumeIndex = currentVolumeIndex;
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, AudioManager.FLAG_SHOW_UI);
        }
    }
    
    public void unmute() {
        if (mCurrentVolumeIndex != 0) {
            final AudioManager audioManager = (AudioManager) WonderfulApplication.mContext.getSystemService(Context.AUDIO_SERVICE);
            final int currentVolumeIndex = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            Log.i(Tags.WALLPAPER_ENGINE, mEngineIndex + " currentVolumeIndex = " + currentVolumeIndex);
            if (currentVolumeIndex == 0) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mCurrentVolumeIndex, AudioManager.FLAG_SHOW_UI);
            }
        }
    }
    
//    private float mVolume;
//    private float VOLUME_STEP = 0.1f;

    /*
    public void increase() {
//        mVolume += VOLUME_STEP;
//        mPlayer.setVolume(mVolume, mVolume);
        final AudioManager audioManager = (AudioManager) WonderfulApplication.mContext.getSystemService(Context.AUDIO_SERVICE);
        audioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
    }

    public void decrease() {
//        mVolume -= VOLUME_STEP;
//        mPlayer.setVolume(mVolume, mVolume);
        final AudioManager audioManager = (AudioManager) WonderfulApplication.mContext.getSystemService(Context.AUDIO_SERVICE);
        audioManager.adjustVolume(AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
    }
    */
    
    /*
    public void loop() {
        PreferenceUtil.switchRepeatState(mService);
    }
    */

    /*
    private void instantiateMediaPlayer() {
        if (mPlayer == null) {
            // instantiate player
            mPlayer = new MediaPlayer();
        }
    }
    */

    private void releaseMediaPlayer() {
        Log.i(Tags.WALLPAPER_ENGINE, mEngineIndex + " !!!!! wallpaper engine release player");
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
        Log.i(Tags.WALLPAPER_ENGINE, mEngineIndex + " !!!!! displayCurrentVideo path = " + path);
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
        Log.i(Tags.WALLPAPER_ENGINE, "mLockVideoPaths = " + mLockVideoPaths + mLockVideoPaths.size());
        return mLockVideoPaths.get(mLockVideoPosition);
    }
    
    private void getAlbumPaths() {
        Log.i(Tags.WALLPAPER_ENGINE, mEngineIndex + " wonderful engine getAlbumPaths");
        /*
        final String lockVideoPath = PreferenceUtil.getLockVideoPath();
        Log.i(Tags.WALLPAPER_ENGINE, mEngineIndex + " wonderful engine lock video path = " + lockVideoPath);
        mLockVideoPaths.clear();
        final Set<String> paths = PreferenceUtil.getLockAlbumPaths();
        if (paths != null) {
            for (String path : paths) {
                Log.i(Tags.WALLPAPER_ENGINE, mEngineIndex + " wonderful engine album path = " + path);
                mLockVideoPaths.add(path);
                if (lockVideoPath.equals(path)) {
                    mLockVideoPosition = mLockVideoPaths.size() - 1;
//                    Log.i(Tags.WALLPAPER_ENGINE, mEngineIndex + " lockVideoPath album mPosition = " + mLockVideoPosition);
                }
            }
        } else {
            mLockVideoPaths.add(lockVideoPath);
            mLockVideoPosition = 0;
        }
        */

        // clear paths
        mLockVideoPaths.clear();
        // reset position
        mLockVideoPosition = 0;
        // add paths

        /*
        final Set<String> paths = PreferenceUtil.getLockAlbumPaths();
        for (String path : paths) {
            Log.i(Tags.WALLPAPER_ENGINE, mEngineIndex + " wonderful engine album path = " + path);
            mLockVideoPaths.add(path);
        }
        */

        final ArrayList<String> paths = LockVideoUtil.getLockVideoPaths();
        mLockVideoPaths.addAll(paths);
        Log.i(Tags.WALLPAPER_ENGINE, "mLockVideoPaths = " + mLockVideoPaths + mLockVideoPaths.size());




    }
    
    private void registerOnLockPathChangeListener() {
        // TODO Auto-generated method stub
        PreferenceUtil.registerOnSharedPreferenceChangeListener(WonderfulApplication.mContext, onLockAlbumChangeListener);
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

                // update paths
                getAlbumPaths();


                // display current video
                if (isVisible()) {
                    displayCurrentVideo();
                } else {
                    final String path = getCurrentVideoPath();
                    Log.i(Tags.WALLPAPER_ENGINE, mEngineIndex + "!!!!!set no prepare new lock albums path = " + path);
//                        setVideoSourceNoPrepare(path);
                    // TODO update video paths
                    getAlbumPaths();
                }
//                }
            }
        }
    };
    // ===========================================================================================
    
    
    
    
    

    private void displayVideo(String path) {
        if (mPlayer == null) {
//            Log.i(Tags.WALLPAPER_ENGINE, "--- new player");
            mPlayer = new MediaPlayer();
        }
        // reset
        Log.i(Tags.WALLPAPER_ENGINE, "--- reset player");
        mPlayer.reset();
        // init player
        Log.i(Tags.WALLPAPER_ENGINE, "--- config player");
        configureMediaPlayer();
        // set data source
        Log.i(Tags.WALLPAPER_ENGINE, "--- set data source");
        setDataSource(path);
        // prepare async
        Log.i(Tags.WALLPAPER_ENGINE, "--- prepare async");
        mPlayer.prepareAsync();
    }

    /*
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
    */

    
    private void setDataSource(String path) {
        try {
            if (path.contains("http")) {
                final String persistentPath = DownloadVideoUtil.getCachePath(path);
                path = persistentPath;
            }
            Log.i(Tags.WALLPAPER_ENGINE, mEngineIndex + "!!!!!!!!!!!!wonderful engine setDataSource path = " + path);
            mPlayer.setDataSource(path);
        } catch (IllegalArgumentException | SecurityException | IllegalStateException | IOException e) {
            Log.i(Tags.WALLPAPER_ENGINE, mEngineIndex + "setDataSource exception");
            e.printStackTrace();
        }
    }
    
    public void configureMediaPlayer() {
//        Log.i(Tags.WALLPAPER_ENGINE, mEngineIndex + "configureMediaPlayer");
        // set surface
        mPlayer.setSurface(getSurfaceHolder().getSurface());
        // set looping
//        final boolean loop = PreferenceUtil.needRepeat(mService.getApplicationContext());
        final boolean loop = true;
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
            Log.i(Tags.WALLPAPER_ENGINE, mEngineIndex + " !!!!! wonderful engine mediaplayer onError what = " + what + " extra = " + extra);
            releaseMediaPlayer();
//            displayCurrentVideo();
            return true;
        }
    };
    
    private MediaPlayer.OnPreparedListener onPreparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
//            Log.i(Tags.WALLPAPER_ENGINE, mEngineIndex + "wonderful engine mediaplayer onPrepared");
            Log.i(Tags.WALLPAPER_ENGINE, "--- player prepared");
            mPlayer.start();
        }
    };

    private MediaPlayer.OnCompletionListener onCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            Log.i(Tags.WALLPAPER_ENGINE, mEngineIndex + "wonderful engine mediaplayer onCompletion");
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


}
