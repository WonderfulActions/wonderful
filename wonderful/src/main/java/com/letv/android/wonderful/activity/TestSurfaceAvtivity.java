package com.letv.android.wonderful.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.letv.android.wonderful.R;
import com.letv.android.wonderful.Tags;
import com.letv.android.wonderful.display.VideoDisplayManager;
import com.letv.android.wonderful.server.AlbumsJson;

public class TestSurfaceAvtivity extends Activity {
    private SurfaceView mSurfaceView;
    private SurfaceView mAnotherSurfaceView;
    /*
    /storage/emulated/0/Android/data/com.letv.android.wonderful/cache/url267379374.wonderfulVideo
    /storage/emulated/0/Android/data/com.letv.android.wonderful/cache/url267409165.wonderfulVideo
    */
    private static final String PATH0 = "/storage/emulated/0/Android/data/com.letv.android.wonderful/cache/url267379374.wonderfulVideo";
    private static final String PATH1 ="/storage/emulated/0/Android/data/com.letv.android.wonderful/cache/url267409165.wonderfulVideo";
    
    private static final String PATH2 ="/sdcard/wonderful/worldcupMV.mp4";
    
    private static final String PATH = PATH0;
    private static final String PATH_ANOTHER = PATH1;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_surface);
        mSurfaceView = (SurfaceView) findViewById(R.id.test_surface);
        mSurfaceView.getHolder().addCallback(callback);
        mAnotherSurfaceView = (SurfaceView) findViewById(R.id.test_surface_another);
    }
    
    private SurfaceHolder.Callback callback = new SurfaceHolder.Callback() {
        private boolean surfaceHasBeenDisplayFirst;
        
        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            Log.i(Tags.TEST_SURFACE, "surfaceDestroyed");
            displayRelease();
        }
        
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            Log.i(Tags.TEST_SURFACE, "surfaceCreated");
        }
        
        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            Log.i(Tags.TEST_SURFACE, "surfaceChanged");
            // Log.i(Tags.TEST_SURFACE, "surfaceChanged surfaceHasBeenDisplayFirst = " + surfaceHasBeenDisplayFirst);
            // Log.i(Tags.TEST_SURFACE, "holder.getSurface() = " + holder.getSurface());
            if (!surfaceHasBeenDisplayFirst) {
                displayFirst();
                surfaceHasBeenDisplayFirst = true;
            } else {
                displayInNewSurface(mSurfaceView);
            }
        }
    };
    
    public void generateLatestNBAAlbumsJson(View view) {
        
        
        AlbumsJson.generateAlbums();
        
        
        
        
        
        
        
        
        
        
        
        
        
        
    }
    
    
    
    
    
    
    
    // actions from user
    // ==========================================================================
    public void displayVideo(View view) {
        displayFirst();
    }
    
    public void displayAnother(View view) {
        displayAnother();
    }
    
    public void start(View view) {
        displayStart();
    }
    
    public void pause(View view) {
        displayPause();
    }
    
    
    public void updateSurface(View view) {
        // displayStop();
        displayInNewSurface(mAnotherSurfaceView);
        // VideoDisplayManager.getInstance().displayCurrentTask();
    }
 // ==========================================================================
    
    
    
    
    
    
    
    
    
    // actions in display manager
    // ================================================================================================
    private void displayFirst() {
//        VideoDisplayManager.getInstance().display(PATH, mSurfaceView, false);
    }

    private void displayAnother() {
//        VideoDisplayManager.getInstance().display(PATH_ANOTHER, mSurfaceView, false);
    }
    
    private void displayStart() {
//        VideoDisplayManager.getInstance().start();
    }
    
    private void displayPause() {
//        VideoDisplayManager.getInstance().pause();
    }
    
    /*
    private void displayStop() {
        VideoDisplayManager.getInstance().stop();
    }
    */
    
    private void displayInNewSurface(SurfaceView surfaceView) {
//        VideoDisplayManager.getInstance().displayInNewSurface(surfaceView);
    }
    
    private void displayRelease() {
        Log.i(Tags.DISPLAY_MANAGER, "displayRelease");
//        VideoDisplayManager.getInstance().release();
        
    }
    // ================================================================================================
    
    
    
    
}
