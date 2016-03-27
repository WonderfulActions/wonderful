package com.letv.android.wonderful.display;

import android.media.MediaPlayer;
import android.util.Log;
import android.view.SurfaceView;

import com.letv.android.wonderful.Tags;
import com.letv.android.wonderful.download.DownloadVideoUtil;
import com.letv.android.wonderful.util.MediaUtil;

import java.io.IOException;


public class VideoDisplayManager {
    private static VideoDisplayManager instance;
    
    static {
        instance = new VideoDisplayManager();
    }
    
    public static VideoDisplayManager getInstance() {
        return instance;
    }
    // ========================================================================
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    // private ExecutorService mExecutorService = Executors.newSingleThreadExecutor();
    
    private MediaPlayer mPlayer;
    private VideoDisplayTask mTask;
    
    
    // display
    // init
    // configure
    // set data source
    // prepareAsync
    // start
    // start/pause
    // release
    
    private  VideoDisplayManager() {
        // instantiatePlayer();
    }
    
    public void display(String url, SurfaceView surfaceView, boolean isSurfaceChanged) {
        Log.i(Tags.DISPLAY_MANAGER, "display task manager display url = " + url);
        /*
        Log.i(Tags.DISPLAY_MANAGER, "isSurfaceChanged = " + isSurfaceChanged);
        Log.i(Tags.DISPLAY_MANAGER, "mTask = " + mTask);
        if (mTask != null) {
            Log.i(Tags.DISPLAY_MANAGER, "mTask.getUrl() = " + mTask.getUrl());
        }
        */
        
        if (mTask != null && url.equals(mTask.getUrl())) {
            // check surfaceView
            if (surfaceView == mTask.getSurfaceView() && !isSurfaceChanged) {
                // start or pause current task
                startOrPause();
            } else {
                // update surfaceView
                displayInNewSurface(surfaceView);
            }
        } else {
            displayNewTask(url, surfaceView);
        }
    }


    
    public void displayInNewSurface(SurfaceView surfaceView) {
        Log.i(Tags.DISPLAY_MANAGER, "!!!!!displayInNewSurface");
        // update surfaceView
        if (surfaceView != null) {
            mTask.setSurfaceView(surfaceView);
        }
        // updateSurfaceView();
        displayCurrentTask();
    }
    
    private void displayNewTask(String url, SurfaceView surfaceView) {
        Log.i(Tags.DISPLAY_MANAGER, "!!!!!displayNewTask url = " + url);
//        isPausing = false;
        // display new task
        // init task
        final VideoDisplayTask task = new VideoDisplayTask(surfaceView, url);
        mTask = task;
        // start display
        // displayVideo(url);
        displayCurrentTask();
    }
    
    
    private void displayCurrentTask() {
        Log.i(Tags.DISPLAY_MANAGER, "displayCurrentTask");
        if (mTask != null) {
            displayVideo(mTask.getUrl());
        }
    }
    
    private void start() {
        if (mPlayer != null && mTask != null) {
            mPlayer.start();
//            isPausing = false;
        }
    }

    private void pause() {
        if (mPlayer != null && mTask != null) {
            mPlayer.pause();
//            isPausing = true;
        }
    }

    
//    private boolean isPausing = false;
    
    /*
    public void stop() {
        if (mPlayer != null) {
            // cacheCurrentPosition();
            mPlayer.stop();
        }
    }
    */
    
    /*
    private int mPosition;
    
    public void reset() {
        if (mPlayer != null && mTask != null) {
            // cacheCurrentPosition();
            mPlayer.reset();
        }
    }
    */
    
    public void release() {
        Log.i(Tags.DISPLAY_MANAGER, "displayer release");
        if (mPlayer != null) {
            // cache current position
            // cacheCurrentPosition();
            // release player
            mPlayer.release();
            mPlayer = null;
        }
    }
    
    
    
    
    
    
    
    
    
    // ==================================================================================
    // current task actions
    /*
    public void mute(String url) {
        // current task
        if (mTask != null && url.equals(mTask.getUrl())) {
            // mute
            if (mPlayer != null) {
                // muteSwitch();
                // MediaUtil.muteSwitch(mTask.getSurfaceView().getContext());
                MediaUtil.muteSwitch();
            }
        }
    }
    */

    /*
    public void mute() {
        // current task
        if (mTask != null) {
            // mute
            if (mPlayer != null) {
                // muteSwitch();
                // MediaUtil.muteSwitch(mTask.getSurfaceView().getContext());
                MediaUtil.muteSwitch();
            }
        }
    }
    */

    
    public void repeat() {
        if (mTask != null && mPlayer != null) {
            mPlayer.setLooping(!mPlayer.isLooping());
        }
    }
    
    
    
    
    
    // ==================================================================================
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    // ==============================================================================================
    /*
    private int mCurrentVolumeIndex;

    private void muteSwitch() {
        final Context context = mTask.getSurfaceView().getContext();
        final AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        final int currentVolumeIndex = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        Log.i(Tags.VIDEO_ACTIVITY, "currentVolumeIndex = " + currentVolumeIndex);
        if (currentVolumeIndex != 0) {
            // mute
            mCurrentVolumeIndex = currentVolumeIndex;
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, AudioManager.FLAG_SHOW_UI);
        } else {
            // unmute
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mCurrentVolumeIndex, AudioManager.FLAG_SHOW_UI);
        }
    }
    */
    // ==============================================================================================
    
    // instantiate
    // configure
    // set source
    // prepare async
    // seek async
    // onSeekComplete start
    private void displayVideo(String dataSource) {
        Log.i(Tags.DISPLAY_MANAGER, "displayVideo dataSource = " + dataSource);
        // reset
        instantiatePlayer();
        mPlayer.reset();
        // configure player
        configurePlayer();
        // set surface
        mPlayer.setSurface(mTask.getSurfaceView().getHolder().getSurface());
        // set data source
        // only in idle, before prepare
        setDataSource(dataSource);
        // prepare async
//        if (!isPausing) {
            mPlayer.prepareAsync();
//        }
    }

    private void startOrPause() {
        Log.i(Tags.DISPLAY_MANAGER, "!!!!!startOrPause url = " + mTask.getUrl());
        if (mPlayer != null) {
            if (mPlayer.isPlaying()) {
                pause();
            } else {
                start();
            }
        }
    }
    
    private void instantiatePlayer() {
        Log.i(Tags.DISPLAY_MANAGER, "display manager instantiatePlayer");
        if (mPlayer == null) {
            // instantiate player
            mPlayer = new MediaPlayer();
            // register loop listener
            // PreferenceUtil.registerOnSharedPreferenceChangeListener(this, needRepeatListener);
        }
    }

    /*
    private void cacheCurrentPosition() {
        final int position = mPlayer.getCurrentPosition();;
        Log.i(Tags.DISPLAY_MANAGER, "cache current position = " + position);
        mPosition = position;
    }
     */

    private void configurePlayer() {
        Log.i(Tags.DISPLAY_MANAGER, "display manager configurePlayer");
        /*
        final boolean loop = PreferenceUtil.needRepeat(this);
        mPlayer.setLooping(loop);
        */
        mPlayer.setLooping(true);
        // add callbacks
        addCallbacks();
    }

    private void addCallbacks() {
        Log.i(Tags.DISPLAY_MANAGER, "display manager addCallbacks");
        mPlayer.setOnPreparedListener(onPreparedListener);
        mPlayer.setOnSeekCompleteListener(onSeekCompleteListener);
        mPlayer.setOnCompletionListener(onCompletionListener);
        mPlayer.setOnErrorListener(onErrorListener);
    }

    private MediaPlayer.OnErrorListener onErrorListener = new MediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            Log.i(Tags.DISPLAY_MANAGER, "onError what = " + what + " extra = " + extra);
            return false;
        }
    };

    private MediaPlayer.OnPreparedListener onPreparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
            // Log.i(Tags.DISPLAY_MANAGER, "onPrepared seek to position = " + mPosition);
            // no seek, prepare, then start
            // mPlayer.seekTo(mPosition);
            start();
        }
    };

    private MediaPlayer.OnCompletionListener onCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            Log.i(Tags.DISPLAY_MANAGER, "onCompletion");
        }
    };

    private MediaPlayer.OnSeekCompleteListener onSeekCompleteListener = new MediaPlayer.OnSeekCompleteListener() {
        @Override
        public void onSeekComplete(MediaPlayer mp) {
            Log.i(Tags.DISPLAY_MANAGER, "onSeekComplete");
            /*
            // mPlayer.prepareAsync();
            if (!hasFirstInitSeek) {
                start();
                hasFirstInitSeek = true;
            } else {
                
//                seekCount++;
//                if (seekCount == 2) {
//                    Log.i(Tags.DISPLAY_MANAGER, " before start seekCount = " + seekCount);
//                    start();
//                    seekCount = 0;
//                }
                
                start();
                Log.i(Tags.DISPLAY_MANAGER, "after seek start");
            }
            */
        }
    };
    

    
    
    
    
    
    
    
    
    

    private void setDataSource(String path) {
        Log.i(Tags.DISPLAY_MANAGER, "display manager setDataSource path = " + path);
        try {
            if (path.contains("http")) {
                final String persistentPath = DownloadVideoUtil.getCachePath(path);
                path = persistentPath;
            }
            Log.i(Tags.DISPLAY_MANAGER, "!!!!! final display manager dataSource = " + path);
            mPlayer.setDataSource(path);
        } catch (IllegalArgumentException | SecurityException | IllegalStateException | IOException e) {
            Log.i(Tags.DISPLAY_MANAGER, "setDataSource exception");
            e.printStackTrace();
        }
    }
    
    /*
    public void displayNextVideo() {
        // TODO
        
        
        
    }
    
    public void displayPreviousVideo() {
        // TODO

    }
*/
    
    

    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    


    
    
    
    
    
}
