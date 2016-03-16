
package com.letv.android.wallpaper.display;

import android.widget.ImageView;

public class NewDisplayTask extends CacheTask {
    private final ImageView imageView;

    public NewDisplayTask(ImageView imageView, String url, NewDisplayOptions displayOptions, OnCompleteListener onCompleteListener, OnProgressListener onProgressListener, long delay) {
        super(url, displayOptions, onCompleteListener, onProgressListener, delay);
        this.imageView = imageView;
    }

    @Override
    public boolean equals(Object o) {
        if (o != null && o instanceof NewDisplayTask) {
            final NewDisplayTask task = (NewDisplayTask) o;
            if (getImageView() != null && getUrl() != null && task.getImageView() != null && task.getUrl() != null) {
                if (getImageView() == task.getImageView() && getUrl().equals(task.getUrl())) {
                    return true;
                }
            }
        }
        return false;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public NewDisplayOptions getDisplayOptions() {
        final CacheOption cacheOption = super.cacheOption;
        if (cacheOption != null) {
            final NewDisplayOptions displayOptions = (NewDisplayOptions) cacheOption;
            return displayOptions;
        }
        return null;
    }
}
