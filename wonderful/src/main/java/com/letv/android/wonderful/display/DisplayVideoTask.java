package com.letv.android.wonderful.display;

import android.graphics.SurfaceTexture;
import android.view.Surface;

/**
 * Created by michael on 16-3-25.
 */
public class DisplayVideoTask {
    private String path;
    private SurfaceTexture surfaceTexture;
    private boolean isPlaying;

    public DisplayVideoTask(String path, SurfaceTexture surfaceTexture) {
        this.path = path;
        this.surfaceTexture = surfaceTexture;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public SurfaceTexture getSurfaceTexture() {
        return surfaceTexture;
    }

    public void setSurfaceTexture(SurfaceTexture surfaceTexture) {
        this.surfaceTexture = surfaceTexture;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    private void setIsPlaying(boolean isPlaying) {
        this.isPlaying = isPlaying;
    }

    @Override
    public boolean equals(Object o) {
        if (o != null && o instanceof DisplayVideoTask) {
            final DisplayVideoTask task = (DisplayVideoTask) o;
            final String path = task.getPath();
            if (this.path != null && path != null && this.path.equals(path)) {
                return true;
            }
        }
        return false;
    }

    public void updatePlayState() {
        setIsPlaying(!isPlaying());
    }
}
