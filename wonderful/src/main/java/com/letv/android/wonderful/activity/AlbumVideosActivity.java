package com.letv.android.wonderful.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.letv.android.wonderful.PreferenceUtil;
import com.letv.android.wonderful.R;
import com.letv.android.wonderful.Tags;
import com.letv.android.wonderful.adapter.VideoListAdapter;
import com.letv.android.wonderful.display.VideoDisplayManager;
import com.letv.android.wonderful.download.DownloadVideoUtil;
import com.letv.android.wonderful.entity.WonderfulAlbum;
import com.letv.android.wonderful.entity.WonderfulVideo;
import com.letv.android.wonderful.util.MediaUtil;
import com.letv.android.wonderful.view.DividerItemDecoration;

import java.util.ArrayList;

public class AlbumVideosActivity extends AppCompatActivity {
    private static final String ACTION_SHOW_ALBUM_VIDEOS = "com.letv.android.wonderful.intent.action.SHOW_ALBUM_VIDEOS";

    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_DETAIL = "detail";
    private static final String KEY_COVER_URL = "coverUrl";
    // private static final String KEY_COUNT = "count";
    private static final String KEY_VIDEOS = "videos";

    private WonderfulAlbum mAlbum;
    // private TextView videoCounter;

    public static void showAlbumVideos(Context context, WonderfulAlbum album) {
        if (album != null) {
            /*
            private int id;
            private String name;
            private String detail;
            private String coverUrl;
            private int count;
            private ArrayList<WonderfulVideo> videos;
            */
            // id name detail cover size
            final Intent intent = new Intent(ACTION_SHOW_ALBUM_VIDEOS);
            intent.putExtra(KEY_ID, album.getId());
            intent.putExtra(KEY_NAME, album.getName());
            intent.putExtra(KEY_DETAIL, album.getDetail());
            intent.putExtra(KEY_COVER_URL, album.getCoverUrl());
            // intent.putExtra(KEY_COUNT, album.getCount());
            intent.putParcelableArrayListExtra(KEY_VIDEOS, album.getVideos());
            // update downloaded state
            for (WonderfulVideo video : album.getVideos()) {
                final boolean isDownloaded = DownloadVideoUtil.isDiskCacheAvailable(video.getVideoUrl());
                video.setDownloaded(isDownloaded);
            }
            context.startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(Tags.ALBUM_ACTIVITY, "AlbumActivity onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        getAlbum();
        // initToolBar();
        initVideoList();
        // album downloaded progress
        initAlbumProgress();
        updateDownloadedCounter();
        initDownloadedIndexer();
        
        // observe video downloaded state
        obServeVideos();
    }
    
    private void obServeVideos() {
        for (int i = 0; i < mAlbum.getVideos().size(); i++) {
            final int position = i;
            mAlbum.getVideos().get(i).addObserver(new WonderfulVideo.DownloadedObserver() {
                @Override
                public void isDownloaded(boolean isDownloaded) {
                    updateDownloadProgress(position);
                }
            });
        }
    }

    /*
    private Toolbar mToolbar;
    private void initToolBar() {
        mToolbar = (Toolbar) findViewById(R.id.videos_toolbar);
        final String albumTitle = mAlbum.getName();
        mToolbar.setTitle(albumTitle);
        setSupportActionBar(mToolbar);
        mToolbar.setOnMenuItemClickListener(new OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Log.i(Tags.ALBUM_ACTIVITY, "toolbar menu item = " + item.getItemId());
                return false;
            }
        });
    }
    */
    
    public void setAlbumAsWallpaper(View view) {
        // set current downloaded album video disk paths as video wallpaper
        PreferenceUtil.cacheLockAlbum(mAlbum.getVideos());
        Toast.makeText(this, R.string.set_lock_video_successful, Toast.LENGTH_SHORT).show();
    }
    
    public void repeatOnce(View view) {
        VideoDisplayManager.getInstance().repeat();
    }
    
    public void mute(View view) {
        // MediaUtil.muteSwitch(this);
//        MediaUtil.muteSwitch();
//        throw new NullPointerException("");
        throw new OutOfMemoryError();


    }
    
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.i(Tags.ALBUM_ACTIVITY, "album activity onConfigurationChanged ");
    }

    private static final int PROGRESS_BACKGROUND_COLOR_ENABLE = 0xff00ff00;
    private static final int PROGRESS_BACKGROUND_COLOR_DISABLE = 0xffffffff;
    private static final int PROGRESS_TEXT_COLOR = 0xff000000;

    private TextView mDownloadedCounter;
    private ArrayList<TextView> mDownloadedProgresses = new ArrayList<TextView>();;
    
    private void updateDownloadedCounter() {
        Log.i(Tags.VIDEO_LIST, "updateDownloadedCounter");
        final ArrayList<WonderfulVideo> videos = mAlbum.getVideos();
        final int maxCount = videos.size();
        int downloadedCount = 0;
        for (WonderfulVideo video : videos) {
            if (video.isDownloaded()) {
                downloadedCount++;
            }
        }
        Log.i(Tags.VIDEO_LIST, "downloadedCount = " + downloadedCount);
        final String counterLabel = downloadedCount + "/" + maxCount;
        mDownloadedCounter.setText(counterLabel);
    }
    
    private void initDownloadedIndexer() {
        for (int i = 0; i < mAlbum.getVideos().size(); i++) {
            final boolean isDownloaded = mAlbum.getVideos().get(i).isDownloaded();
            Log.i(Tags.VIDEO_LIST, i + " isDownloaded = " + isDownloaded);
            if (isDownloaded) {
                if (mDownloadedProgresses != null && mDownloadedProgresses.size() > 0) {
                    final TextView progressView = mDownloadedProgresses.get(i);
                    // update background, update clickable
                    progressView.setBackgroundColor(PROGRESS_BACKGROUND_COLOR_ENABLE);
                    progressView.setClickable(true);
                }
            }
        }
    }
    
    private void updateDownloadedIndexer(int position) {
        /*
        Log.i(Tags.ALBUM_ACTIVITY, "updateDownloadedProgress position = " + position);
        Log.i(Tags.ALBUM_ACTIVITY, "mDownloadedProgresses.size() = " + mDownloadedProgresses.size());
        */
        if (mDownloadedProgresses != null && mDownloadedProgresses.size() > 0) {
            final TextView progressView = mDownloadedProgresses.get(position);
            // update background, update clickable
            progressView.setBackgroundColor(PROGRESS_BACKGROUND_COLOR_ENABLE);
            progressView.setClickable(true);
        }
    }
    
    private void initAlbumProgress() {
        Log.i(Tags.ALBUM_ACTIVITY, "initVideoCounter");
        final int maxCount = mAlbum.getVideos().size();
        // init counter
        mDownloadedCounter = (TextView) findViewById(R.id.doenloaded_video_counter);
        // init indexer
        final LinearLayout downloadedProgresses = (LinearLayout) findViewById(R.id.downloaded_progresses);
        for (int i = 0; i < maxCount; i++) {
            // add disable progresses
            final String indexLabel = (i + 1) + "";
            final TextView progressView = new TextView(this);
            progressView.setBackgroundColor(PROGRESS_BACKGROUND_COLOR_DISABLE);
            progressView.setTextColor(PROGRESS_TEXT_COLOR);
            progressView.setTextSize(20);
            progressView.setGravity(Gravity.CENTER);
            progressView.setText(indexLabel);
            progressView.setClickable(false);
            final int position = i;
            progressView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // display video
                    // DisplayFullActivity.displayVideo(AlbumVideosActivity.this, mAlbum.getVideos(), position);
                    // scroll list
                    Log.i(Tags.VIDEO_LIST, "scrollToPosition position = " + position);
                    mVideoList.scrollToPosition(position);
                }
            });
            final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(10, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            params.setMargins(5, 0, 5, 0);
            // progressView.setLayoutParams(params);
            downloadedProgresses.addView(progressView, params);
            mDownloadedProgresses.add(progressView);
        }
    }
    
    public void downloadAll(View view) {
        Log.i(Tags.ALBUM_ACTIVITY, "mVideosAdapter = " + mVideosAdapter);
        if (mVideosAdapter != null) {
            mVideosAdapter.downloadAll(view.getContext());
        }
    }
    
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Log.i(Tags.ALBUM_ACTIVITY, "onWindowFocusChanged hasFocus = " + hasFocus);
        /*
        if (hasFocus) {
            VideoDisplayManager.getInstance().displayCurrentTask();
        } else {
            VideoDisplayManager.getInstance().shutdown();
        }
        */
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        Log.i(Tags.ALBUM_ACTIVITY, "album activity onResume");
        // VideoDisplayManager.getInstance().displayCurrentTask();
    };

    private void getAlbum() {
        final Intent intent = getIntent();
        final int id =  intent.getIntExtra(KEY_ID, -1);
        final String name = intent.getStringExtra(KEY_NAME);
        final String detail = intent.getStringExtra(KEY_DETAIL);
        final String coverUrl = intent.getStringExtra(KEY_COVER_URL);
        // final int count = intent.getIntExtra(KEY_COUNT, -1);
        final ArrayList<WonderfulVideo> videos = intent.getParcelableArrayListExtra(KEY_VIDEOS);
        final WonderfulAlbum album = new WonderfulAlbum(id, name, detail, coverUrl, videos);
        mAlbum = album;
    }

    private RecyclerView mVideoList;
    private VideoListAdapter mVideosAdapter;
    
    private void initVideoList() {
        mVideoList = (RecyclerView) findViewById(R.id.album_video_list);
        // set decoration
        /*
        final RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST);
        mVideoList.addItemDecoration(itemDecoration);
        */
        // set adapter
        // final ArrayList<WonderfulVideo> videos = VideoGenerator.generateNBAVideos();
        final ArrayList<WonderfulVideo> videos = mAlbum.getVideos();
        // mVideosAdapter = new VideoListAdapter(videos, mVideoDownloadCallback);
        mVideosAdapter = new VideoListAdapter(videos);
        mVideoList.setAdapter(mVideosAdapter);
        // set layoutManager
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mVideoList.setLayoutManager(linearLayoutManager);
    }
    
    // private int mDownloadedCount;
    /*
    private VideoDownloadCallback mVideoDownloadCallback = new VideoDownloadCallback() {
        @Override
        public void onVideoDownloaded(int position) {
            // update album video downloaded progress
            Log.i(Tags.ALBUM_ACTIVITY, "onVideoDownloaded position = " + position);
            updateDownloadProgress(position);
        }
    };
    */
    
    private void updateDownloadProgress(int position) {
        // update counter
        updateDownloadedCounter();
        // update progress
        updateDownloadedIndexer(position);
    }

}
