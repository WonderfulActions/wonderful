package com.letv.android.wonderful.display;

import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.Surface;

import com.letv.android.wonderful.Tags;

import java.io.IOException;

/**
 * Created by michael on 16-3-25.
 */
public class DisplayVideoManager {
    private static DisplayVideoManager mInstance;

    static {
        mInstance = new DisplayVideoManager();
    }

    public static DisplayVideoManager getInstance() {
        return mInstance;
    }
    // ========================================================================







    private MediaPlayer mPlayer;
    private DisplayVideoTask mTask;

    public DisplayVideoManager() {
        // do nothing
    }

    public void clearTask() {
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
        mTask = null;
    }

    /*
    public void stopTask() {
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }
    */

    public String getCurrentPath() {
        if (mTask != null) {
            return mTask.getPath();
        }
        return null;
    }

    public void display(String path, SurfaceTexture surfaceTexture) {
        Log.i(Tags.WONDERFUL_APP, "player display");
        Log.i(Tags.WONDERFUL_APP, "path = " + path);
        Log.i(Tags.WONDERFUL_APP, "surfaceTexture = " + surfaceTexture);
        if (path != null && surfaceTexture != null) {
            // do nothing
        } else {
            return;
        }

        // construct task
        final DisplayVideoTask task = new DisplayVideoTask(path, surfaceTexture);

        // path is task id

        // if is current task
            // if is the same surface start/pause
            // else display in new surface
        // else display new task


        // mTask = task;
        // displayCurrentTask();
        if (mTask != null && mTask.equals(task)) {
            // is current task
            if (mTask.getSurfaceTexture() == task.getSurfaceTexture()) {
                // is the same surface
                // start or pause
                startOrPauseCurrentTask();
            } else {
                // new surface (new surfaceTexture or fullScreen)
                // may be display in fullScreen
                displayCurrentTaskInNewSurfaceTexture(task.getSurfaceTexture());
            }
        } else {
            // is new task
            displayNewTask(task);
        }
    }

    // surfaceTexture is destroyed
    public void cancelDisplay(String path, SurfaceTexture surfaceTexture) {
//        Log.i(Tags.WONDERFUL_APP, "player cancelDisplay");
        if (path != null && surfaceTexture != null) {
            // do nothing
        } else {
            return;
        }

        // if is current task and same surfaceTexture, cancel display
        // else do nothing
        final DisplayVideoTask task = new DisplayVideoTask(path, surfaceTexture);
        if (mTask != null && mTask.equals(task)) {
            final SurfaceTexture currentSurfaceTexture = mTask.getSurfaceTexture();
            if (currentSurfaceTexture != null && currentSurfaceTexture == task.getSurfaceTexture()) {
                // pause current task
                cancelDisplayCurrentTask();
            }
        }
    }

    // surfaceTexture is available
    /*
    public void replay(String path, SurfaceTexture surfaceTexture) {
//        Log.i(Tags.WONDERFUL_APP, "player replay");
        if (path != null && surfaceTexture != null) {
            // do nothing
        } else {
            return;
        }

        // if is same task and current task surfaceTexture is null
        final DisplayVideoTask task = new DisplayVideoTask(path, surfaceTexture);
        if (mTask != null && mTask.equals(task)) {
            Log.i(Tags.WONDERFUL_APP, "mTask path = " + mTask.getPath());
            Log.i(Tags.WONDERFUL_APP, "mTask surfaceTexture = " + mTask.getSurfaceTexture());
            if (mTask.getSurfaceTexture() == null) {
                // replay current task in new surfaceTexture
                // update task surfaceTexture
                // replay current task in new surfaceTexture
                displayCurrentTaskInNewSurfaceTexture(task.getSurfaceTexture());

            }
        }

    }
    */




    // when SurfaceTexture null
    public void cancelDisplayCurrentTask() {
        mTask.updatePlayState();
        Log.i(Tags.WONDERFUL_APP, "--- cancelDisplayCurrentTask path = " + mTask.getPath());
        // set task surfaceTexture null
        if (mTask != null) {
            mTask.setSurfaceTexture(null);
        }
        // release player
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }

    }



    private void displayCurrentTaskInNewSurfaceTexture(SurfaceTexture surfaceTexture) {
        Log.i(Tags.WONDERFUL_APP, "--- displayCurrentTaskInNewSurfaceTexture");
        mTask.setSurfaceTexture(surfaceTexture);
        displayCurrentTask();


    }

    private void displayNewTask(DisplayVideoTask task) {
        Log.i(Tags.WONDERFUL_APP, "--- displayNewTask");
        mTask = task;
        displayCurrentTask();
    }

    private void startOrPauseCurrentTask() {
        Log.i(Tags.WONDERFUL_APP, "--- startOrPauseCurrentTask");
        if (mPlayer == null) {
            return;
        }

        final boolean isPlaying = mTask.isPlaying();
        mTask.updatePlayState();
        if (isPlaying) {
            mPlayer.pause();
        } else {
            mPlayer.start();
        }
    }



    private void displayCurrentTask() {
//        Log.i(Tags.WONDERFUL_APP, "DisplayVideoManager displayCurrentTask");
        // TODO optimize
        // update task play state
        mTask.updatePlayState();
        // display current task
        final String path = mTask.getPath();
        final SurfaceTexture surfaceTexture = mTask.getSurfaceTexture();

        if (mPlayer == null) {
            mPlayer = new MediaPlayer();
        }
        mPlayer.reset();
        mPlayer.setLooping(true);
        mPlayer.setSurface(new Surface(surfaceTexture));
        setDataSource(path);
        addCallbacks();
        mPlayer.prepareAsync();
    }


    // ========================================================
    // about player
    // instantiate
    // configure
    // set source
    // prepare async
    // seek async
    // onSeekComplete start

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

    private void setDataSource(String path) {
        Log.i(Tags.DISPLAY_MANAGER, "display manager setDataSource path = " + path);
        try {
            mPlayer.setDataSource(path);
        } catch (IllegalArgumentException | SecurityException | IllegalStateException | IOException e) {
            Log.i(Tags.DISPLAY_MANAGER, "setDataSource exception");
            e.printStackTrace();
        }
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
            Log.i(Tags.WONDERFUL_APP, "onError what = " + what + " extra = " + extra);
            return false;
        }
    };

    private MediaPlayer.OnPreparedListener onPreparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
//            Log.i(Tags.WONDERFUL_APP, "onPrepared" );
            // no seek, prepare, then start
            // mPlayer.seekTo(mPosition);
            mp.start();
        }
    };

    private MediaPlayer.OnCompletionListener onCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            Log.i(Tags.WONDERFUL_APP, "onCompletion");
        }
    };

    private MediaPlayer.OnSeekCompleteListener onSeekCompleteListener = new MediaPlayer.OnSeekCompleteListener() {
        @Override
        public void onSeekComplete(MediaPlayer mp) {
            Log.i(Tags.WONDERFUL_APP, "onSeekComplete");
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














}
