
package com.letv.android.wonderful.adapter;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.letv.android.wallpaper.display.DisplayUtil;
import com.letv.android.wallpaper.listener.OnClickDoubleListener;
import com.letv.android.wonderful.PreferenceUtil;
import com.letv.android.wonderful.R;
import com.letv.android.wonderful.Tags;
import com.letv.android.wonderful.activity.DisplayFullActivity;
import com.letv.android.wonderful.application.WonderfulApplication;
import com.letv.android.wonderful.display.DisplayVideoManager;
import com.letv.android.wonderful.display.VideoDisplayManager;
import com.letv.android.wonderful.download.DownloadVideoManager;
import com.letv.android.wonderful.download.DownloadVideoRunnable;
import com.letv.android.wonderful.download.DownloadVideoUtil;
import com.letv.android.wonderful.entity.WonderfulVideo;

import java.util.ArrayList;

public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.VideoViewHolder> {
    private ImageView currentCoverView;

    public static class VideoViewHolder extends RecyclerView.ViewHolder {
        private TextureView textureView;
        private ImageView coverView;
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
            textureView = (TextureView) itemView.findViewById(R.id.video_surface_view);
            coverView = (ImageView) itemView.findViewById(R.id.video_cover_view);
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

    private ArrayList<WonderfulVideo> mVideos;

    public VideoListAdapter(ArrayList<WonderfulVideo> videos) {
        mVideos = videos;
    }

    @Override
    public int getItemCount() {
        return mVideos.size();
    }

    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View videoView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video_list, parent, false);
        final int width = WonderfulApplication.mDisplayWidth;
        final int height = width * 9/16;
        final ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(width, height);
        videoView.setLayoutParams(params);
        // TODO
        final VideoViewHolder holder = new VideoViewHolder(videoView);
        return holder;
    }

    public int mDisplayingPosition = -1;

    @Override
    public void onBindViewHolder(final VideoViewHolder holder, final int position) {
        Log.i(Tags.DISPLAY_VIDEO, "onBindViewHolder position = " + position);
        // get video
        final WonderfulVideo video = mVideos.get(position);
        final String currentVideoUrl = video.getVideoUrl();
        final String currentCoverUrl = video.getCoverUrl();
        // display video detail
        Log.i(Tags.WONDERFUL_APP, "--- set cover = " + currentCoverUrl);
        holder.coverView.setTag(currentCoverUrl);
        setVideoCover(holder.coverView, currentCoverUrl);
        holder.positionView.setText((position + 1) + "");
        holder.detailView.setText(video.getName());

        final boolean isDiskCacheAvailable = DownloadVideoUtil.isDiskCacheAvailable(currentVideoUrl);
        final int progress = (isDiskCacheAvailable ? 100 : 0);
        holder.progressBar.setProgress(progress);
        
        video.addProgressCallback(new DownloadVideoRunnable.ProgressCallback() {
            @Override
            public void onProgressUpdate(int progress) {
                holder.progressBar.setProgress(progress);
            }
        });

        video.addCompleteCallback(new DownloadVideoRunnable.CompleteCallback() {
            @Override
            public void onDownloadComplete() {
                holder.progressBar.setProgress(100);
            }
        });

        holder.textureView.setOnClickListener(new OnClickDoubleListener() {
            @Override
            public void onClickOnce(View v) {
                Log.i(Tags.WONDERFUL_APP, "--- textureView onClickOnce");

                // if is displaying(play/pause) in manager, display(play/pause) current video
                // else display new video
                    // recovery previous video cover, cache current coverView
                    // if is not downloaded, download video
                    // if is downloaded or download complete, remove current cover then display video
                final String currentVideoPath = DownloadVideoUtil.getCachePath(currentVideoUrl);
                final String displayingVideoPath = DisplayVideoManager.getInstance().getCurrentPath();
                Log.i(Tags.WONDERFUL_APP, "------- currentVideoPath = " + currentVideoPath);
                Log.i(Tags.WONDERFUL_APP, "------- displayingVideoPath = " + displayingVideoPath);
                // recovery previous cover
                if (currentVideoPath != null && displayingVideoPath != null && !currentVideoPath.equals(displayingVideoPath)) {
                    Log.i(Tags.WONDERFUL_APP, "------- display new video path = " + currentVideoPath);
                    // recovery previous video cover
                    Log.i(Tags.WONDERFUL_APP, "------- recovery previous cover view = " + currentCoverView);
                    if (currentCoverView != null) {
                        final String previousCoverUrl = (String) currentCoverView.getTag();
                        Log.i(Tags.WONDERFUL_APP, "!!!!!!!! recovery previous cover url = " + previousCoverUrl);
                        if (previousCoverUrl != null) {
                            setVideoCover(currentCoverView, previousCoverUrl);
                        }
                    }
                    // cache current coverView
                    currentCoverView = holder.coverView;
                } else {
                    /*
                    Log.i(Tags.WONDERFUL_APP, "------- display current video path = " + currentVideoPath);
                    // display(play/pause) current video
                    if (currentVideoPath != null) {
                        DisplayVideoManager.getInstance().display(currentVideoPath, holder.textureView.getSurfaceTexture());
                    }
                    */
                }

                // display new video
                final String downloadPath  = DownloadVideoUtil.generateDownloadPath(currentVideoUrl);
                if (!DownloadVideoUtil.isDiskCacheAvailable(currentVideoUrl)) {
                    Log.i(Tags. WONDERFUL_APP, "------- start to download new video currentVideoUrl = " + currentVideoUrl);
                    // if is not downloaded, download video
                    persistentVideo(currentVideoUrl, downloadPath, video);
                    video.addCompleteCallback(new DownloadVideoRunnable.CompleteCallback() {
                        @Override
                        public void onDownloadComplete() {
                            displayVideo(downloadPath);
                        }
                    });
                } else {
                    // if is downloaded or download complete, remove current cover then display video
                    // remove current cover
                    displayVideo(downloadPath);
                }










            }

            private void displayVideo(String currentVideoPath) {
                Log.i(Tags.WONDERFUL_APP, "--- remove current cover = " + holder.coverView);
                removeVideoCover(holder.coverView);
                // display(play/pause) current video
                if (currentVideoPath != null) {
                    Log.i(Tags.WONDERFUL_APP, "--- display new video path = " + currentVideoPath);
                    DisplayVideoManager.getInstance().display(currentVideoPath, holder.textureView.getSurfaceTexture());
                }
            }

            @Override
            public void onClickDouble(View v) {
                Log.i(Tags.WONDERFUL_APP, "--- textureView onClickDouble !!!");
                // cancel display
                DisplayVideoManager.getInstance().cancelDisplayCurrentTask();
                // recovery displaying cover
                Log.i(Tags.WONDERFUL_APP, "------- currentCoverView = " + currentCoverView);
                if (currentCoverView != null) {
                    final String oldCoverUrl = (String) currentCoverView.getTag();
                    Log.i(Tags.WONDERFUL_APP, "!!!!!!!! recovery oldCoverUrl = " + oldCoverUrl);
                    if (oldCoverUrl != null) {
                        setVideoCover(currentCoverView, oldCoverUrl);
                    }
                }
                // display full
                displayFull(v.getContext(), position);
            }
        });

        holder.textureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                // check is cached
                if (DownloadVideoUtil.isDiskCacheAvailable(currentVideoUrl)) {
                    final String path = DownloadVideoUtil.getCachePath(currentVideoUrl);
                    final String currentPath = DisplayVideoManager.getInstance().getCurrentPath();
                    // check is current path
                    if (path != null && path.equals(currentPath)) {
                        onCurrentSurfaceAvailable(path, surface);
                    }
                }
            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                // check is cached
                if (DownloadVideoUtil.isDiskCacheAvailable(currentVideoUrl)) {
                    final String path = DownloadVideoUtil.getCachePath(currentVideoUrl);
                    final String currentPath = DisplayVideoManager.getInstance().getCurrentPath();
                    // check is current path
                    if (path != null && path.equals(currentPath)) {
                        onCurrentSurfaceDestroyed(path, surface);
                    }
                }
                return true;
            }

            private void onCurrentSurfaceAvailable(String path, SurfaceTexture surface) {
                Log.i(Tags.WONDERFUL_APP, "--- onCurrentSurfaceAvailable");
                Log.i(Tags.WONDERFUL_APP, "--- available path = " + path);
                // remove cover
                Log.i(Tags.WONDERFUL_APP, "--- remove cover = " + currentCoverUrl);
                removeVideoCover(holder.coverView);
                // replay video
//                DisplayVideoManager.getInstance().replay(path, surface);
                DisplayVideoManager.getInstance().display(path, surface);
            }

            private void onCurrentSurfaceDestroyed(String path, SurfaceTexture surface) {
                Log.i(Tags.WONDERFUL_APP, "--- onCurrentSurfaceDestroyed");
                Log.i(Tags.WONDERFUL_APP, "--- destroy path = " + path);
                // cancel display video
                DisplayVideoManager.getInstance().cancelDisplay(path, surface);
                // show last frame
                /*
                Log.i(Tags.WONDERFUL_APP, "--- show last cover = " + coverUrl);
                final Bitmap lastFrame = holder.textureView.getBitmap();
                holder.coverView.setImageBitmap(lastFrame);
                */
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
                // Log.i(Tags.WONDERFUL_APP, "onSurfaceTextureSizeChanged");
                // do nothing
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surface) {
                // Log.i(Tags.WONDERFUL_APP, "textureView onSurfaceTextureUpdated");
                // do nothing
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
        holder.setWallpaperButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(Tags.DISPLAY_VIDEO, "setWallpaperButton onClick url = " + currentVideoUrl);
                setAsWallpaper(currentVideoUrl);
            }

        });
        holder.fullButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(Tags.DISPLAY_VIDEO, "fullButton onClick url = " + currentVideoUrl);
                final Context context = v.getContext();
                displayFull(context, position);
            }
        });
        holder.moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(Tags.DISPLAY_VIDEO, "moreButton onClick url = " + currentVideoUrl);
                popupActions(v, position, holder.progressBar);
            }
        });
    }

    private static void removeVideoCover(ImageView imageView) {
        DisplayUtil.cancelDisplay(imageView);
        imageView.setImageDrawable(null);
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
        persistentVideo(url, path, video);
    }
    
    public void repeatOnce() {
        VideoDisplayManager.getInstance().repeat();
    }
    
    private void setAsWallpaper(final String url) {
        Log.i(Tags.WONDERFUL_APP, "set video as wallpaper url = " + url);
        PreferenceUtil.cacheLockVideo(url);
    }
    
    private void displayFull(final Context context, final int position) {
        Log.i(Tags.DISPLAY_VIDEO, "display video full = " + position);
        DisplayFullActivity.displayVideo(context, mVideos, position);
    }

    private void setVideoCover(final ImageView coverView, final String coverUrl) {
        // display cover async
        removeVideoCover(coverView);
        DisplayUtil.display(coverView, coverUrl);
    }
    
    // position url path progressCounter progressBar callback
    private static void persistentVideo(final String url, String path, final WonderfulVideo video) {
        // download to disk, and publish progress.
        final boolean isFileDownloaded = DownloadVideoManager.isDownloaded(path);
        Log.i(Tags.DOWNLOAD_VIDEO, "isFileDownloaded = " + isFileDownloaded + " path = " + path);
        if (!isFileDownloaded) {
            Log.i(Tags.DOWNLOAD_VIDEO, "!!!!!downloadVideo url = " + url);
            final DownloadVideoRunnable.ProgressCallback progressCallback = new DownloadVideoRunnable.ProgressCallback() {
                @Override
                public void onProgressUpdate(int progress) {
                    // Log.i(Tags.DOWNLOAD_VIDEO, position + "video item onProgressUpdate progress = " + progress);
                    // update progress
                    if (video != null) {
                        video.setProgress(progress);
                    }
                }
            };
            final DownloadVideoRunnable.CompleteCallback completeCallback = new DownloadVideoRunnable.CompleteCallback() {
                @Override
                public void onDownloadComplete() {
                    if (video != null) {
                        video.setDownloaded(true);
                    }
                }
            };
            DownloadVideoManager.downloadVideo(url, path, progressCallback, completeCallback);
        } else {
            video.setDownloaded(true);
        }
    }

    // TODO
    public void downloadAll(Context context) {
        for (int i = 0; i < mVideos.size(); i++) {
            final WonderfulVideo video = mVideos.get(i);
            // get url
            final String url = video.getVideoUrl();
            // get path
            final String path  = DownloadVideoUtil.generateDownloadPath(url);
            Log.i(Tags.WONDERFUL_VIDEO, i + "downloadAll video persistent path = " + path);
            persistentVideo(url, path, video);
        }
    }
}
