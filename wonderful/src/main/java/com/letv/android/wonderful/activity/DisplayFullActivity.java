package com.letv.android.wonderful.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.letv.android.wonderful.PreferenceUtil;
import com.letv.android.wonderful.R;
import com.letv.android.wonderful.Tags;
import com.letv.android.wonderful.display.VideoDisplayManager;
import com.letv.android.wonderful.download.DownloadVideoUtil;
import com.letv.android.wonderful.entity.WonderfulVideo;
import com.letv.android.wonderful.util.MediaUtil;

import java.util.ArrayList;

public class DisplayFullActivity extends Activity {
    public static final String TAG = Tags.WONDERFUL_VIDEO;
    private static final String ACTION_DISPLAY_VIDEO = "com.letv.android.wonderful.intent.action.DISPLAY_VIDEO";

    /*
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_DETAIL = "detail";
    private static final String KEY_COVER_URL = "coverUrl";
    private static final String KEY_COVER_RES = "coverRes";
    // private static final String KEY_PERFORMER = "performer";
    private static final String KEY_SIZE = "size";
    private static final String KEY_TIME = "time";
    private static final String KEY_VIDEO_URL = "videoUrl";
    */
    private static final String KEY_ALBUM_VIDEOS = "albumVideos";
    private static final String KEY_POSITION = "position";

    // private WonderfulVideo mVideo;
    private ArrayList<WonderfulVideo> mAlbumVideos;
    private int mPosition;
    private SurfaceView mSurfaceView;
    // private MediaPlayer mPlayer;

    
    
    // ========================================================================
    /*
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
    
    private static String getPreviousVideoPath() {
        mVideoIndex--;
        if (mVideoIndex < 0) {
            mVideoIndex = SAMPLE_ARRAY.length - 1;
        }
        return SAMPLE_ARRAY[mVideoIndex];
    }
    */
    // ========================================================================
    
    
    
    public static void displayVideo(Context context, ArrayList<WonderfulVideo> videos, int position) {
        /*
        private int id;
        private String name;
        private String detail;
        private String coverUrl;
        private long size;
        private long time;
        private String videoUrl;
        */
        /*
        // id name detail cover coverRes performer size time videoUrl
        final int id = video.getId();
        final String name = video.getName();
        final String detail = video.getDetail();
        final String coverUrl = video.getCoverUrl();
        final long size  = video.getSize();
        final long time = video.getTime();
        final String videoUrl = video.getVideoUrl();
        Log.i(Tags.VIDEO_ACTIVITY, "videoUrl = " + videoUrl);
        
        final Intent intent = new Intent(ACTION_DISPLAY_VIDEO);
        intent.putExtra(KEY_ID, id);
        intent.putExtra(KEY_NAME, name);
        intent.putExtra(KEY_DETAIL, detail);
        intent.putExtra(KEY_COVER_URL, coverUrl);
        // intent.putExtra(KEY_COVER_RES, coverRes);
        intent.putExtra(KEY_SIZE, size);
        intent.putExtra(KEY_TIME, time);
        intent.putExtra(KEY_VIDEO_URL, videoUrl);
        */
        final Intent intent = new Intent(ACTION_DISPLAY_VIDEO);
        intent.putParcelableArrayListExtra(KEY_ALBUM_VIDEOS, videos);
        intent.putExtra(KEY_POSITION, position);
        context.startActivity(intent);
    }

    private void getAlbumVideos() {
        /*
        private int id;
        private String name;
        private String detail;
        private String coverUrl;
        private long size;
        private long time;
        private String videoUrl;
        */
        /*
        final Intent intent = getIntent();
        final int id = intent.getIntExtra(KEY_ID, -1);
        final String name = intent.getStringExtra(KEY_NAME);
        final String detail = intent.getStringExtra(KEY_DETAIL);
        final String coverUrl = intent.getStringExtra(KEY_COVER_URL);
        // final int coverRes = intent.getIntExtra(KEY_COVER_RES, -1);
        final long size = intent.getLongExtra(KEY_SIZE, -1);
        final long time = intent.getLongExtra(KEY_TIME, -1);
        final String videoUrl = intent.getStringExtra(KEY_VIDEO_URL);
        Log.i(Tags.VIDEO_ACTIVITY, "display video url = " + videoUrl);
        final WonderfulVideo video = new WonderfulVideo(id, name, detail, coverUrl, size, time, videoUrl);
        // video.setCoverRes(coverRes);
        mVideo = video;
        */
        
        final Intent intent = getIntent();
        final ArrayList<WonderfulVideo> albumVideos = intent.getParcelableArrayListExtra(KEY_ALBUM_VIDEOS);
        final int position = intent.getIntExtra(KEY_POSITION, -1);
        mAlbumVideos = albumVideos;
        mPosition = position;
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        getAlbumVideos();
        mSurfaceView = (SurfaceView) findViewById(R.id.video_surface);
        // mSurfaceView.setZOrderOnTop(true);
        mSurfaceView.getHolder().addCallback(callback);
    }
    
    @Override
    protected void onDestroy() {
        Log.i(Tags.DISPLAY_FULL, "display full onDestroy release player");
        VideoDisplayManager.getInstance().release();
        super.onDestroy();
    };


    /*
    @Override
    protected void onPause() {
        super.onPause();
        // holdPause();
    }
    */

    /*
    private boolean isHoldPause;
    private void holdPause() {
        Log.i(Tags.VIDEO_ACTIVITY, "video activity holdPause");
        if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.pause();
            isHoldPause = true;
        }
    }
    */

    private SurfaceHolder.Callback callback = new SurfaceHolder.Callback() {
        // surface has been use to display
        // private boolean hasBeenDisplay;
        
        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            Log.i(Tags.VIDEO_ACTIVITY, "full display surface surfaceDestroyed");
            VideoDisplayManager.getInstance().release();
        }
        
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            // do nothing
        }
        
        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            Log.i(Tags.VIDEO_ACTIVITY, "full display surface surfaceChanged");
            /*
            if (isHoldPause) {
                holdStart();
            } else {
                displayVideo();
            }
            */
            /*
            if (!hasBeenDisplay) {
                displayVideo();
                hasBeenDisplay = true;
            }
            */
            // final String path = getCurrentVideoPath();
            displayCurrentVideo(true);
        }

    };
    
    private void displayCurrentVideo(boolean isSurfaceChanged) {
        Log.i(Tags.DISPLAY_FULL, "displayCurrentVideo isSurfaceChanged = " + isSurfaceChanged);
        Log.i(Tags.DISPLAY_FULL, "displayCurrentVideo album size = " + mAlbumVideos.size());
        Log.i(Tags.DISPLAY_FULL, "displayCurrentVideo mPosition = " + mPosition);
        final String url = mAlbumVideos.get(mPosition).getVideoUrl();
        VideoDisplayManager.getInstance().display(url, mSurfaceView, isSurfaceChanged);
    }
    
    
    
    
    
    
    
    
    // ================================================================================
    // actions
    public void start(View view) {
        displayCurrentVideo(false);
    }
    
    public void display(View view) {
        displayCurrentVideo(false);
    }
    
    public void mute(View view) {
        // VideoDisplayManager.getInstance().mute();
        MediaUtil.muteSwitch();
    }
    
    public void repeat(View view) {
        VideoDisplayManager.getInstance().repeat();
    }
    
    
    
  

    
    
    
    
    public void setAsWallpaper(View view) {
        // persistentLockAlbum();
        cacheLockVideoPath();
    }
    
    private void cacheLockVideoPath() {
        // cache lock video path
        final String url = mAlbumVideos.get(mPosition).getVideoUrl();
        PreferenceUtil.cacheLockVideo(url);
    }

    /*
    public static void cacheLockVideo(final String url) {
        final boolean isDiskCacheAvailable = DownloadVideoUtil.isDiskCacheAvailable(url);
        if (isDiskCacheAvailable) {
            final String diskCachePath = DownloadVideoUtil.getCachePath(url);
            PreferenceUtil.setLockVideoPath(diskCachePath);
        }
        // clear other videos
        PreferenceUtil.setLockAlbumPaths(null);
    }
    */

    /*
    // TODO get cache disk path is engine
    // only downloaded
    private void persistentLockAlbum() {
        // save live video path
        final String downloadedPath = getDownloadedVideoPath();
        Log.i(Tags.DISPLAY_FULL, "persistentLockAlbum downloadedPath = " + downloadedPath);
        PreferenceUtil.setLockVideoPath(this, downloadedPath);
        // persistent video paths
        Log.i(Tags.DISPLAY_FULL, "persistentLockAlbum");
        final ArrayList<String> videoPaths = getLockAlbumPaths();
        PreferenceUtil.setLockAlbumPaths(this, videoPaths);
    }
    
    private String getDownloadedVideoPath() {
        final WonderfulVideo video = mAlbumVideos.get(mPosition);
        final String downloadedPath = DownloadVideoUtil.getCachePath(video.getVideoUrl());
        return downloadedPath;
    }
    
    private ArrayList<String> getLockAlbumPaths() {
        final ArrayList<String> downloadedPaths = new ArrayList<String>();
        for (WonderfulVideo video : mAlbumVideos) {
            final String path = DownloadVideoUtil.generateDownloadPath(video.getVideoUrl());
            final File downloadedFile = new File(path);
            if (path != null && downloadedFile != null && downloadedFile.exists()) {
                Log.i(Tags.DISPLAY_FULL, "persistentLockAlbum lock album path = " + path);
                downloadedPaths.add(path);
            }
        }
        return downloadedPaths;
    }
    */
    
    
    
    
    
    
    public void playNext(View view) {
        nextPosition();
        displayCurrentVideo(false);
    }

    public void playPrevious(View view) {
        previousPosition();
        displayCurrentVideo(false);
    }
    
    private void nextPosition() {
        if (mAlbumVideos != null && mAlbumVideos.size() > 0) {
            mPosition++;
            if (mPosition > mAlbumVideos.size() - 1) {
                mPosition = 0;
            }
        }
    }
    
    private void previousPosition() {
        if (mAlbumVideos != null && mAlbumVideos.size() > 0) {
            mPosition--;
            if (mPosition < 0) {
                mPosition = mAlbumVideos.size() - 1;
            }
        }
    }
    // ================================================================================
    
    
    
    
    
    
    
    
    
    
    
    
    /*
    private String getCurrentVideoPath() {
        final WonderfulVideo video = mAlbumVideos.get(mPosition);
        final String url = video.getVideoUrl();
        final String path = DownloadVideoUtil.getAvailablePersistentPath(url);
        Log.i(Tags.DISPLAY_FULL, "display full available persistent path = " + path);
        return path;
    }
    */
    
    // onSurface changed first, display video
    // prepare async on prepare start
    // has start, control by focus
    /*
    public void onWindowFocusChanged(boolean hasFocus) {
        Log.i(Tags.VIDEO_ACTIVITY, "onWindowFocusChanged hasFocus = " + hasFocus);
//        if (hasDisplay && mPlayer != null) {
//            if (hasFocus) {
//                if (!mPlayer.isPlaying()) {
//                    mPlayer.start();
//                }
//            } else {
//                if (mPlayer.isPlaying()) {
//                    mPlayer.pause();
//                }
//            }
//            
//        }
        if (hasFocus) {
            // VideoDisplayManager.getInstance().displayCurrentTask();
        } else {
            // VideoDisplayManager.getInstance().pause();
        }
        
    };
    */

    /*
    protected void holdStart() {
        Log.i(Tags.VIDEO_ACTIVITY, "video activity holdStart");
        if (mPlayer != null && !mPlayer.isPlaying()) {
            mPlayer.start();
            isHoldPause = false;
        }
    }
    */

    // player has been inited
    // private boolean hasDisplay;
    /*
    private void displayVideo() {
        if (!hasDisplay) {
            instantiatePlayer();
            configurePlayer();
            
            final String downloadedPath = getDownloadedVideoPath();
            Log.i(Tags.VIDEO_ACTIVITY, "video activity display downloaded path = " + downloadedPath);
            setDataSource(downloadedPath);
             
            final String url = getCurrentVideoUrl();
            Log.i(Tags.VIDEO_ACTIVITY, "video activity display http url = " + url);
            setDataSource(url);
            mPlayer.prepareAsync();
        }
    }
     */

    /*
    private String getCurrentVideoUrl() {
        final WonderfulVideo video = mAlbumVideos.get(mPosition);
        final String url = video.getVideoUrl();
        return url;
    }

    private String getDownloadedVideoPath() {
        final WonderfulVideo video = mAlbumVideos.get(mPosition);
        final String downloadedPath = DownloadVideoUtil.generateDownloadPath(video.getVideoUrl());
        return downloadedPath;
    }
    */

    /*
    private void instantiatePlayer() {
        Log.i(Tags.VIDEO_ACTIVITY, "instantiatePlayer");
        if (mPlayer == null) {
            // instantiate player
            mPlayer = new MediaPlayer();
            // register loop listener
            PreferenceUtil.registerOnSharedPreferenceChangeListener(this, needRepeatListener);
        }
    }

    private void releasePlayer() {
        if (mPlayer != null) {
            // unregister loop listener
            PreferenceUtil.unregisterOnSharedPreferenceChangeListener(this, needRepeatListener);
            // release player
            mPlayer.release();
            mPlayer = null;
        }
    }

    private final OnSharedPreferenceChangeListener needRepeatListener = new OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            Log.i(Tags.VIDEO_ACTIVITY, "needRepeatListener key = " + key);
            if (sharedPreferences != null && key != null && key.equals(PreferenceUtil.KEY_NEED_REPEAT)) {
                final boolean needRepeat = sharedPreferences.getBoolean(key, false);
                Log.i(Tags.VIDEO_ACTIVITY, "new changed needRepeat = " + needRepeat);
                if (mPlayer != null) {
                    mPlayer.setLooping(needRepeat);
                }
            }
        }
    };
    */

    /*
    public void configurePlayer() {
        Log.i(Tags.VIDEO_ACTIVITY, "configurePlayer");
        // set surface
        mPlayer.setSurface(mSurfaceView.getHolder().getSurface());
        // set looping
        
        final boolean loop = PreferenceUtil.needRepeat(this);
        mPlayer.setLooping(loop);
        
        mPlayer.setLooping(true);
        // add callbacks
        mPlayer.setOnErrorListener(onErrorListener);
        mPlayer.setOnPreparedListener(onPreparedListener);
        mPlayer.setOnSeekCompleteListener(onSeekCompleteListener);
        mPlayer.setOnCompletionListener(onCompletionListener);
    }

    private MediaPlayer.OnErrorListener onErrorListener = new MediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            Log.i(Tags.VIDEO_ACTIVITY, "onError what = " + what + " extra = " + extra);
            return false;
        }
    };

    private MediaPlayer.OnPreparedListener onPreparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
            Log.i(Tags.VIDEO_ACTIVITY, "onPrepared");
            mPlayer.start();
            hasDisplay = true;
        }
    };

    private MediaPlayer.OnCompletionListener onCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            Log.i(Tags.VIDEO_ACTIVITY, "onCompletion");
        }
    };

    private MediaPlayer.OnSeekCompleteListener onSeekCompleteListener = new MediaPlayer.OnSeekCompleteListener() {
        @Override
        public void onSeekComplete(MediaPlayer mp) {
            Log.i(Tags.VIDEO_ACTIVITY, "onSeekComplete");
        }
    };
    */

    /*
    private void setDataSource(String path) {
        try {
            if (path.contains("http")) {
                final String persistentPath = DownloadVideoUtil.getAvailablePersistentPath(path);
                path = persistentPath;
            }
            Log.i(Tags.VIDEO_ACTIVITY, "video displayer = " + path);
            mPlayer.setDataSource(path);
        } catch (IllegalArgumentException | SecurityException | IllegalStateException | IOException e) {
            Log.i(Tags.VIDEO_ACTIVITY, "setDataSource exception");
            e.printStackTrace();
        }
    }
    */

    /*
    // actions
    public void start(View view) {
        startSwitch();
    }

    public void stop(View view) {
        stopSwitch();
    }

    private void stopSwitch() {
        mPlayer.stop();
    }

    public void mute(View view) {
        muteSwitch();
    }

    public void repeat(View view) {
        repeatSwitch();
    }
    
    public void setAsWallpaper(View view) {
        // save live video path
        // PreferenceUtil.setLockVideoPath(this, getDownloadedVideoPath());
        persistentLockAlbum();
        // showAsLiveWallpaper();
    }
    */

    /*
    private void persistentLockAlbum() {
        // save live video path
        final String downloadedPath = getDownloadedVideoPath();
        Log.i(Tags.VIDEO_ACTIVITY, "persistentLockAlbum downloadedPath = " + downloadedPath);
        PreferenceUtil.setLockVideoPath(this, downloadedPath);
        // persistent video paths
        Log.i(Tags.VIDEO_ACTIVITY, "persistentLockAlbum");
        final ArrayList<String> videoPaths = getLockAlbumPaths();
        PreferenceUtil.setLockAlbumPaths(this, videoPaths);
    }

    private ArrayList<String> getLockAlbumPaths() {
        final ArrayList<String> downloadedPaths = new ArrayList<String>();
        for (WonderfulVideo video : mAlbumVideos) {
            final String path = DownloadVideoUtil.generateDownloadPath(video.getVideoUrl());
            final File downloadedFile = new File(path);
            if (path != null && downloadedFile != null && downloadedFile.exists()) {
                Log.i(Tags.VIDEO_ACTIVITY, "persistentLockAlbum lock album path = " + path);
                downloadedPaths.add(path);
            }
        }
        return downloadedPaths;
    }
    */

    /*
    public void playNext(View view) {
        // get next video path
        final String path = getNextVideoPath();
        displayVideo(path);
    }

    public void playPrevious(View view) {
        final String path = getPreviousVideoPath();
        displayVideo(path);
    }
    */
    
    /*
    private String getCurrentVideoPath() {
        return mAlbumVideos.get(mPosition).getVideoUrl();
    }
    */
    
    /*
    private String getNextVideoPath() {
        mPosition++;
        if (mPosition > mAlbumVideos.size() - 1) {
            mPosition = 0;
        }
        return mAlbumVideos.get(mPosition).getVideoUrl();
    }
    
    private String getPreviousVideoPath() {
        mPosition--;
        if (mPosition < 0) {
            mPosition = mAlbumVideos.size() - 1;
        }
        return mAlbumVideos.get(mPosition).getVideoUrl();
    }

    private void displayVideo(final String path) {
        // reset
        mPlayer.reset();
        // configure player
        configurePlayer();
        // set data source
        setDataSource(path);
        // prepare async
        mPlayer.prepareAsync();
    }
    */

    /*
    private void startSwitch() {
        final boolean isPlaying = mPlayer.isPlaying();
        if (isPlaying) {
            mPlayer.pause();
        } else {
            mPlayer.start();
        }
    }

    private int mCurrentVolumeIndex;

    private void muteSwitch() {
        final AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
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

    private void repeatSwitch() {
        final boolean isLooping = mPlayer.isLooping();
        mPlayer.setLooping(!isLooping);
    }
    */

    
    
    
    
    
    
    
    // ================================================================================================
    /*
    private static final String LIVE_PICKER_PACKAGE_NAME = "com.android.wallpaper.livepicker";
    private static final String LIVE_PICKER_CLASS_NAME = "com.android.wallpaper.livepicker.LiveWallpaperPreview";
    private static final String EXTRA_LIVE_WALLPAPER_INTENT = "android.live_wallpaper.intent";

    private void showAsLiveWallpaper() {
        final WallpaperInfo wallpaperInfo = getLiveWallpaperInfo();
        if (wallpaperInfo != null) {
            // set live intent
            final Intent liveIntent = new Intent(WallpaperService.SERVICE_INTERFACE);
            liveIntent.setClassName(wallpaperInfo.getPackageName(), wallpaperInfo.getServiceName());
            // start system live picker activity
            final Intent pickerIntent = new Intent();
            final ComponentName pickerComponent = new ComponentName(LIVE_PICKER_PACKAGE_NAME, LIVE_PICKER_CLASS_NAME);
            pickerIntent.setComponent(pickerComponent);
            pickerIntent.putExtra(EXTRA_LIVE_WALLPAPER_INTENT, liveIntent);
            startActivity(pickerIntent);
        }
    }

    private WallpaperInfo getLiveWallpaperInfo() {
        // get wallpaperInfo
        final Intent wallpaperServiceIntent = new Intent(this, WonderfulWallpaperService.class);
        final ResolveInfo resolveInfo = getPackageManager().resolveService(wallpaperServiceIntent, PackageManager.GET_META_DATA);
        try {
            final WallpaperInfo wallpaperInfo = new WallpaperInfo(this, resolveInfo);
            return wallpaperInfo;
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    */
    // ================================================================================================
    
    
    
    
    
    
    
    
    
    
    
    
    
}
