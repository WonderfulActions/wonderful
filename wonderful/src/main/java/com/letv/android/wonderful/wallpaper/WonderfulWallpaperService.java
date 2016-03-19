
package com.letv.android.wonderful.wallpaper;

import android.content.Intent;
import android.service.wallpaper.WallpaperService;
import android.util.Log;

import com.letv.android.wonderful.activity.TestActivity;

import java.util.HashSet;

public class WonderfulWallpaperService extends WallpaperService {
    private static final String TAG = TestActivity.TAG;
    private int mEngineIndex;
    // TODO engine list
    private HashSet<WonderfulWallpaperEngine> mEngines = new HashSet<WonderfulWallpaperEngine>();

    @Override
    public Engine onCreateEngine() {
        // keep screen on
//        keepScreenOn(System.currentTimeMillis(), true);
        Log.i(TAG, "WonderfulWallpaperService onCreateEngine");
        final WonderfulWallpaperEngine wonderfulEngine = new WonderfulWallpaperEngine(this, mEngineIndex++);
        mEngines.add(wonderfulEngine);
        return wonderfulEngine;
    }
    
    public void removeEngine(WonderfulWallpaperEngine engine) {
        mEngines.remove(engine);
        Log.i(TAG, "mEngines size = " + mEngines.size());
    }
    
    public static final String COMMAND_HAS_VOLUME = "hasVolume";
    public static final String COMMAND_NEED_REPEAT = "needRepeat";
    public static final String COMMAND_PREPARE = "prepare";
    public static final String COMMAND_START = "start";
    public static final String COMMAND_PAUSE = "pause";
    public static final String COMMAND_STOP = "stop";
    public static final String COMMAND_PLAY_NEXT = "playNext";
    public static final String COMMAND_HOLD_DOWN = "holdDown";
    public static final String COMMAND_HOLD_UP = "holdUp";
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "wallpaper service onStartCommand");
        if (intent != null) {
            // handle command has volume
            if (intent.hasExtra(COMMAND_HAS_VOLUME)) {
                final boolean hasVolume = intent.getBooleanExtra(COMMAND_HAS_VOLUME, false);
                for (WonderfulWallpaperEngine engine : mEngines) {
                    engine.handleCommand(COMMAND_HAS_VOLUME, hasVolume);
                }
            }
            // handle command need repeat
            if (intent.hasExtra(COMMAND_NEED_REPEAT)) {
                final boolean needRepeat = intent.getBooleanExtra(COMMAND_NEED_REPEAT, false);
                for (WonderfulWallpaperEngine engine : mEngines) {
                    engine.handleCommand(COMMAND_NEED_REPEAT, needRepeat);
                }
            }
            // handle command prepare
            if (intent.hasExtra(COMMAND_PREPARE)) {
                for (WonderfulWallpaperEngine engine : mEngines) {
                    engine.handleCommand(COMMAND_PREPARE, true);
                }
            }
            // handle command start
            if (intent.hasExtra(COMMAND_START)) {
                for (WonderfulWallpaperEngine engine : mEngines) {
                    engine.handleCommand(COMMAND_START, true);
                }
            }
            // handle command pause
            if (intent.hasExtra(COMMAND_PAUSE)) {
                for (WonderfulWallpaperEngine engine : mEngines) {
                    engine.handleCommand(COMMAND_PAUSE, true);
                }
            }
            // handle command stop
            if (intent.hasExtra(COMMAND_STOP)) {
                for (WonderfulWallpaperEngine engine : mEngines) {
                    engine.handleCommand(COMMAND_STOP, true);
                }
            }
            // handle command play next
            if (intent.hasExtra(COMMAND_PLAY_NEXT)) {
                for (WonderfulWallpaperEngine engine : mEngines) {
                    engine.handleCommand(COMMAND_PLAY_NEXT, true);
                }
            }
            // handle command hold down
            if (intent.hasExtra(COMMAND_HOLD_DOWN)) {
                for (WonderfulWallpaperEngine engine : mEngines) {
                    engine.handleCommand(COMMAND_HOLD_DOWN, true);
                }
            }
            // handle command hold up
            if (intent.hasExtra(COMMAND_HOLD_UP)) {
                for (WonderfulWallpaperEngine engine : mEngines) {
                    engine.handleCommand(COMMAND_HOLD_UP, true);
                }
            }
        }
        
        
        
        
        
        /*
        if (hasVolume) {
            unmute();
        } else {
            mute();
        }
        */
        return super.onStartCommand(intent, flags, startId);
    }
    
    
    /*
    private static final String TAG = MainActivity.TAG;
    
    private int mCurrentVolumeIndex;
    
    public void mute() {
        final AudioManager audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        final int currentVolumeIndex = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        Log.i(TAG, "wallpaper service currentVolumeIndex = " + currentVolumeIndex);
        if (currentVolumeIndex != 0) {
            mCurrentVolumeIndex = currentVolumeIndex;
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, AudioManager.FLAG_SHOW_UI);
        }
    }
    
    public void unmute() {
        if (mCurrentVolumeIndex != 0) {
            final AudioManager audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
            final int currentVolumeIndex = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            Log.i(TAG, "wallpaper service currentVolumeIndex = " + currentVolumeIndex);
            if (currentVolumeIndex == 0) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mCurrentVolumeIndex, AudioManager.FLAG_SHOW_UI);
            }
        }
    }
    */
    
    
    
    
    
    
    
    
    
    
    

    /*
    private void keepScreenOn(long currentTimeMillis, boolean noChangeLights) {
        Log.i("WonderfulWallpaperService", "before invoke userActivity");
        userActivity(this, currentTimeMillis, noChangeLights);
    }

    public void userActivity(Context context, long when, boolean noChangeLights) {
        
         * userActivity(when, USER_ACTIVITY_EVENT_OTHER, noChangeLights ?
         * USER_ACTIVITY_FLAG_NO_CHANGE_LIGHTS : 0);
         

        final PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        try {
            final Method userActivity = PowerManager.class.getDeclaredMethod("getLockWallpaperBitmap", Long.class, Boolean.class);
            userActivity.setAccessible(true);
            userActivity.invoke(powerManager, when, noChangeLights);
            Log.i("WonderfulWallpaperService", "after invoke userActivity");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
    */
}
