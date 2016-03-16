
package com.letv.android.wonderful.display;

import android.view.SurfaceView;

public class VideoDisplayTask {
    private SurfaceView surfaceView;
    private String url;

    public VideoDisplayTask(SurfaceView surfaceView, String url) {
        this.surfaceView = surfaceView;
        this.url = url;
    }

    public SurfaceView getSurfaceView() {
        return surfaceView;
    }

    public void setSurfaceView(SurfaceView surfaceView) {
        this.surfaceView = surfaceView;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
