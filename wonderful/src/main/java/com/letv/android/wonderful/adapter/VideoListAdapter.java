
package com.letv.android.wonderful.adapter;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.letv.android.wallpaper.cache.NewCacheManager;
import com.letv.android.wallpaper.listener.OnClickDoubleListener;
import com.letv.android.wonderful.PreferenceUtil;
import com.letv.android.wonderful.R;
import com.letv.android.wonderful.Tags;
import com.letv.android.wonderful.activity.DisplayFullActivity;
import com.letv.android.wonderful.application.WonderfulApplication;
import com.letv.android.wonderful.display.VideoDisplayManager;
import com.letv.android.wonderful.download.DownloadVideoMananger;
import com.letv.android.wonderful.download.DownloadVideoTask;
import com.letv.android.wonderful.download.DownloadVideoUtil;
import com.letv.android.wonderful.entity.WonderfulVideo;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.VideoViewHolder> {

    public static class VideoViewHolder extends RecyclerView.ViewHolder {
        private SurfaceView surfaceView;
        // public ImageView coverView;
        // public TextView nameView;
        public TextView detailView;

        // progress
        // public TextView progressView;
        public ProgressBar progressBar;

        // user action
        // collect download setAsWallpaper full
        public Button collectButton;
        public Button downloadButton;
        // public Button repeatButton;
        // public Button muteButton;
        public Button setWallpaperButton;
        public Button fullButton;
        public TextView positionView;
        public ImageView moreButton;

        public VideoViewHolder(View itemView) {
            super(itemView);
            surfaceView = (SurfaceView) itemView.findViewById(R.id.video_surface_view);
            /*
            surfaceView.setZOrderOnTop(true);
            surfaceView.getHolder().setFormat(PixelFormat.TRANSPARENT);
            */
            // coverView = (ImageView) itemView.findViewById(R.id.video_cover_view);
            // nameView = (TextView) itemView.findViewById(R.id.video_name_view);
            detailView = (TextView) itemView.findViewById(R.id.video_detail_view);

            // progressView = (TextView) itemView.findViewById(R.id.video_progress_view);
            progressBar = (ProgressBar) itemView.findViewById(R.id.video_progress_bar);

            collectButton = (Button) itemView.findViewById(R.id.video_collect_button);
            downloadButton = (Button) itemView.findViewById(R.id.video_download_button);
            // repeatButton = (Button) itemView.findViewById(R.id.video_repeat_button);
            // muteButton = (Button) itemView.findViewById(R.id.video_mute_button);
            setWallpaperButton = (Button) itemView.findViewById(R.id.item_set_wallpaper_button);
            fullButton = (Button) itemView.findViewById(R.id.display_full_button);
            positionView = (TextView) itemView.findViewById(R.id.video_index_view);
            moreButton  = (ImageView) itemView.findViewById(R.id.video_more_view);
        }
    }
    
    public interface VideoDownloadCallback {
        public void onVideoDownloaded(int position);
    }

    private ArrayList<WonderfulVideo> mVideos;
    // private VideoDownloadCallback mDownloadCallback;

    public VideoListAdapter(ArrayList<WonderfulVideo> videos) {
        mVideos = videos;
        // mDownloadCallback = downloadCallback;
    }

    @Override
    public int getItemCount() {
        return mVideos.size();
    }

    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View videoView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video_list, parent, false);
        final int width = WonderfulApplication.WIDTH;
        final int height = width * 9/16;
        final ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(width, height);
        videoView.setLayoutParams(params);
        final VideoViewHolder holder = new VideoViewHolder(videoView);
        return holder;
    }

    public int mDisplayingPosition = -1;
    
    public int getDisplayingPosition() {
        return mDisplayingPosition;
    }
    
    /*
    private void display() throws InterruptedException {
        // get byteArray
        final String url = mTask.getUrl();
        final OnProgressListener onProgressListener = mTask.getOnProgressListener();
        // cache config
        final boolean cache2Memory = mTask.getDisplayOptions().isCache2Memory();
        final boolean cache2Disk = mTask.getDisplayOptions().isCache2Disk();
        final byte[] byteArray = NewCacheManager.getByteArray(url, onProgressListener, cache2Memory, cache2Disk);
        if (byteArray != null) {
            isInterrupted(INTERRUPTED_BEFORE_DECODE_BITMAP);
            // decode
            final Options decodeOptions = mTask.getDisplayOptions().getDecodeOptions();
            final Bitmap bitmap = BitmapDecoder.decode(byteArray, decodeOptions);
            if (bitmap != null) {
                isInterrupted(INTERRUPTED_BEFORE_DISPLAY_BITMAP);
                // display
                postDisplay(bitmap);
            } else {
                // notify complete
                postNotifyDisplayComplete(null);
            }
        } else {
            // notify complete
            postNotifyDisplayComplete(null);
        }
    }
    */
    
    
    private static final Handler MAIN_HANDLER = new Handler(Looper.getMainLooper());
    
    @Override
    public void onBindViewHolder(final VideoViewHolder holder, final int position) {
        Log.i(Tags.DISPLAY_VIDEO, "onBindViewHolder position = " + position);
        // get video
        final WonderfulVideo video = mVideos.get(position);
        final String url = video.getVideoUrl();
        final String coverUrl = video.getCoverUrl();
        Log.i(Tags.DISPLAY_VIDEO, "video cover url = " + video.getCoverUrl());
        
        // display video detail
        // holder.coverView.setImageResource(video.getCoverRes());
        // DisplayUtil.display(holder.surfaceView, video.getCoverUrl());
        
        
        final SurfaceView surfaceView = holder.surfaceView;
        setSurfaceBackground(surfaceView, coverUrl);
        
        
        
        holder.positionView.setText((position + 1) + "");
        
        // holder.nameView.setText(video.getName());
        // holder.detailView.setText(video.getDetail());
        holder.detailView.setText(video.getName());
        final boolean isDiskCacheAvailable = DownloadVideoUtil.isDiskCacheAvailable(url);
        
        final int progress = (isDiskCacheAvailable ? 100 : 0);
        holder.progressBar.setProgress(progress);
        
        video.addObserver(new WonderfulVideo.DownloadedObserver() {
            @Override
            public void isDownloaded(boolean isDownloaded) {
                holder.progressBar.setProgress(100);
            }
        });
        
        /*
        if (isDiskCacheAvailable) {
            mDownloadCallback.onVideoDownloaded(position);
        }
        */
        
        
        
        
        
        
        
        
        
        
        
        
        // set onClickListeners
        holder.surfaceView.setOnClickListener(new OnClickDoubleListener() {
            @Override
            public void onClickDouble(View v) {
                final Context context = v.getContext();
                displayFull(context, position);
            }
            
            private boolean hasBeenClicked;
            @Override
            public void onClickOnce(View view) {
                Log.i(Tags.DISPLAY_VIDEO, "video surfaceView onclick position = " + position);
                Log.i(Tags.DISPLAY_VIDEO, "holder.surfaceView hasBeenClicked = " + hasBeenClicked);
                if (!hasBeenClicked) {
                    // download video
                    final String path  = DownloadVideoUtil.generateDownloadPath(url);
                    // final TextView progressCounter = holder.progressView;
                    final ProgressBar progressBar = holder.progressBar;
                    // Log.i(Tags.DISPLAY_VIDEO, "list item download video before display");
                    video.addObserver(new WonderfulVideo.DownloadedObserver() {
                        @Override
                        public void isDownloaded(boolean isDownloaded) {
                            if (isDownloaded) {
                                Log.i(Tags.DISPLAY_VIDEO, "video is downloaded url = " + url);
                                displayVideo(holder, position, url);
                            }
                        }
                    });
                    persistentVideo(url, path, null, progressBar, video);
                } else {
                    displayVideo(holder, position, url);
                }
                
                
                
                hasBeenClicked = true;
            }
            private void displayVideo(final VideoViewHolder holder, final int position, final String url) {
                final boolean isValid = holder.surfaceView.getHolder().getSurface().isValid();
                Log.i(Tags.DISPLAY_VIDEO, "isValid = " + isValid);
                if (isValid) {
                    // remove background
                    holder.surfaceView.setBackgroundColor(0x00ffffff);
                    setmDisplayingPosition(position);
                    VideoDisplayManager.getInstance().display(url, holder.surfaceView, false);
                }
            }
        });
        // cache latest position
        holder.surfaceView.setTag(position);
        holder.surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                /*
                Log.i(Tags.DISPLAY_VIDEO, position + " surfaceDestroyed");
                Log.i(Tags.DISPLAY_VIDEO, "mDisplayingPosition = " + mDisplayingPosition);
                */
                if (position == mDisplayingPosition) {
                    Log.i(Tags.DISPLAY_VIDEO, position + " surfaceDestroyed");
                    VideoDisplayManager.getInstance().release();
                }
            }
            
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (position == mDisplayingPosition) {
                    Log.i(Tags.DISPLAY_VIDEO, position + " surfaceCreated");
                }
            }
            
            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
                /*
                Log.i(Tags.DISPLAY_VIDEO, position + " surfaceChanged");
                Log.i(Tags.DISPLAY_VIDEO, "mDisplayingPosition = " + mDisplayingPosition);
                */
                if (position == mDisplayingPosition) {
                    Log.i(Tags.DISPLAY_VIDEO, position + "new surfaceChanged = " + position);
                    final Object object = holder.surfaceView.getTag();
                    if (object == null) {
                        // VideoDisplayManager.getInstance().displayInNewSurface(holder.surfaceView);
                    } else {
                        final int latestPosition = (int) object;
                        Log.i(Tags.DISPLAY_VIDEO, "latestPosition = " + latestPosition);
                        /*
                        Log.i(Tags.DISPLAY_VIDEO, "list surface width = " + width);
                        Log.i(Tags.DISPLAY_VIDEO, "list surface height = " + height);
                        */
                        if (position == latestPosition) {
                            // VideoDisplayManager.getInstance().displayInNewSurface(holder.surfaceView);
                            // recovery background
                            // Log.i(Tags.DISPLAY_VIDEO, "recovery surface background");
                            // final String coverUrl = video.getCoverUrl();
                            // setSurfaceBackground(holder.surfaceView, coverUrl);
                            displayVideoInNewSurface(holder.surfaceView, position, coverUrl);
                        }
                    }
                }
            }
        });
        holder.collectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int id = video.getId();
                collectVideo(id);
            }
        });
        holder.downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // final Context context = view.getContext();
                final ProgressBar progressBar = holder.progressBar;
                downloadVideo(video, progressBar);
            }
        });
        /*
        holder.repeatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO
                
            }
        });
        */
        /*
        holder.muteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                muteVideoInSurfaceView(url);
            }
        });
        */
        holder.setWallpaperButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(Tags.DISPLAY_VIDEO, "setWallpaperButton onClick url = " + url);
                setAsWallpaper(url);
            }

        });
        holder.fullButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(Tags.DISPLAY_VIDEO, "fullButton onClick url = " + url);
                final Context context = v.getContext();
                displayFull(context, position);
            }
        });
        holder.moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(Tags.DISPLAY_VIDEO, "moreButton onClick url = " + url);
                popupActions(v, position, holder.progressBar);
            }
        });
    }
    
    private void displayVideoInNewSurface(final SurfaceView surfaceView, final int position, final String url) {
        Log.i(Tags.DISPLAY_VIDEO, "displayVideoInNewSurface");
        // remove background
        surfaceView.setBackgroundColor(0x00ffffff);
        setmDisplayingPosition(position);
        VideoDisplayManager.getInstance().displayInNewSurface(surfaceView);
    }

    private void popupActions(final View v, final int position, final ProgressBar progressBar) {
        final PopupMenu menu = new PopupMenu(v.getContext(), v);
        // menu.setGravity(Gravity.LEFT | Gravity.BOTTOM);
        menu.inflate(R.menu.video_actions);
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Log.i(Tags.DISPLAY_VIDEO, "onMenuItemClick item title = " + item.getTitle());
                final WonderfulVideo video = mVideos.get(position);
                final int id = video.getId();
                final String url = video.getVideoUrl();
                final int menuId = item.getItemId();
                switch (menuId) {
                    case R.id.collect_video_menu:
                        collectVideo(id);
                        break;
                    case R.id.download_view_menu:
                        downloadVideo(video, progressBar);
                        break;
                    case R.id.display_repeat_once:
                        repeatOnce();
                        break;
                    case R.id.set_video_menu:
                        setAsWallpaper(url);
                        break;
                        /*
                    case R.id.display_full_menu:
                        displayFull(v.getContext(), position);
                        break;
                        */
                    default:
                        
                        break;
                }
                return true;
            }
        });
        menu.show();
    }
    
    private void collectVideo(final int id) {
        Log.i(Tags.DISPLAY_VIDEO, "collect video id = " + id);
        // TODO collect in server
    }
    
    private static void downloadVideo(final WonderfulVideo video, final ProgressBar progressBar) {
        Log.i(Tags.DISPLAY_VIDEO, "download video id = " + video.getId());
        final String url = video.getVideoUrl();
        final String path  = DownloadVideoUtil.generateDownloadPath(url);
        // final TextView progressCounter = holder.progressView;
        // persistentVideo(position, url, path, progressCounter, progressBar);
        persistentVideo(url, path, null, progressBar, video);
    }
    
    public void repeatOnce() {
        VideoDisplayManager.getInstance().repeat();
    }
    
    private void setAsWallpaper(final String url) {
        Log.i(Tags.DISPLAY_VIDEO, "set video as wallpaper url = " + url);
        PreferenceUtil.cacheLockVideo(url);
    }
    
    private void displayFull(final Context context, final int position) {
        Log.i(Tags.DISPLAY_VIDEO, "display video full = " + position);
        /*
        final File downloadFile=  new File(path);
        if (path != null && downloadFile != null && downloadFile.exists() && downloadFile.isFile()) {
            DisplayFullActivity.displayVideo(context, mVideos, position);
        } else {
            Toast.makeText(context, R.string.download_before_display, Toast.LENGTH_SHORT).show();
        }
        */
        DisplayFullActivity.displayVideo(context, mVideos, position);
    }

    private void setSurfaceBackground(final SurfaceView surfaceView, final String coverUrl) {
        // display cover async
        new Thread(new Runnable() {
            @Override
            public void run() {
                /*
                final InputStream inputStream = getInputStreambyStr(video.getCoverUrl());
                final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                final BitmapDrawable background = new BitmapDrawable(bitmap);
                holder.surfaceView.post(new Runnable() {
                    @Override
                    public void run() {
                        holder.surfaceView.setBackground(background);
                    }
                });
                */
                
                final byte[] byteArray = NewCacheManager.getByteArray(coverUrl, null, true, true);
                if (byteArray != null) {
                    final Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length, null);
                    MAIN_HANDLER.post(new Runnable() {
                        @Override
                        public void run() {
                            surfaceView.setBackground(new BitmapDrawable(bitmap));
                        }
                    });
                }
            }
        }).start();
    }
    
    
    protected void muteVideoInSurfaceView(String url) {
        VideoDisplayManager.getInstance().mute(url);
    }

    /*
    private static void displayVideoInSurfaceView(SurfaceView surfaceView, String url) {
        // TODO display downloaded progress
        VideoDisplayManager.getInstance().display(url, surfaceView);
    }
    */
    
    // position url path progressCounter progressBar callback
    private static void persistentVideo(final String url, String path, final TextView progressCounter, final ProgressBar progressBar, final WonderfulVideo video) {
        // download to disk, and publish progress.
        final boolean isFileDownloaded = DownloadVideoMananger.isDownloaded(path);
        Log.i(Tags.DOWNLOAD_VIDEO, "isFileDownloaded = " + isFileDownloaded + " path = " + path);
        if (!isFileDownloaded) {
            Log.i(Tags.DOWNLOAD_VIDEO, "!!!!!downloadVideo url = " + url);
            final DownloadVideoTask.ProgressCallback progressCallback = new DownloadVideoTask.ProgressCallback() {
                @Override
                public void onProgressUpdate(int progress) {
                    // Log.i(Tags.DOWNLOAD_VIDEO, position + "video item onProgressUpdate progress = " + progress);
                    // update progress
                    if (progressCounter != null) {
                        progressCounter.setText(progress + "%");
                    }
                    if (progressBar != null) {
                        progressBar.setProgress(progress);
                    }
                }
            };
            final DownloadVideoTask.CompleteCallback completeCallback = new DownloadVideoTask.CompleteCallback() {
                @Override
                public void onDownloadComplete() {
                    /*
                    // update album download result progress
                    if (downloadCallback != null) {
                        downloadCallback.onVideoDownloaded(position);
                    }
                    */
                    video.setDownloaded(true);
                }
            };
            DownloadVideoMananger.downloadVideo(url, path, progressCallback, completeCallback);
        } else {
            video.setDownloaded(true);
        }
    }

    // private static final String VIDEO_URL = "http://h.hiphotos.baidu.com/zhidao/pic/item/0dd7912397dda1442234822cb0b7d0a20cf4866b.jpg";
    // private static final String VIDEO_URL = "http://pop900.com/top2.ts";

    public void downloadAll(Context context) {
        for (int i = 0; i < mVideos.size(); i++) {
            final WonderfulVideo video = mVideos.get(i);
            // get url
            final String url = video.getVideoUrl();
            // final String url = VIDEO_URL;
            // final int id = video.getId();
            // get path
            final String path  = DownloadVideoUtil.generateDownloadPath(url);
            Log.i(Tags.WONDERFUL_VIDEO, i + "downloadAll video persistent path = " + path);
            persistentVideo(url, path, null, null, video);
        }
    }
    
    public static InputStream getInputStreambyStr(String urlStr) {
        InputStream inputStream = null;
        if (null != urlStr && urlStr.length() > 6) {// http:// https://
            URL picUrl = null;
            try {
                picUrl = new URL(urlStr);
                    HttpURLConnection picConnection = (HttpURLConnection) picUrl.openConnection();
                    picConnection.setConnectTimeout(5000);
                    picConnection.setReadTimeout(9000);
                    picConnection.setDoInput(true);
                    picConnection.connect();
                    inputStream = picConnection.getInputStream();
            } catch (IOException e) {
            }
        }
        return inputStream;
    }

    public int getmDisplayingPosition() {
        return mDisplayingPosition;
    }

    public void setmDisplayingPosition(int mDisplayingPosition) {
        this.mDisplayingPosition = mDisplayingPosition;
    }
}
