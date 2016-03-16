
package com.letv.android.wallpaper.display;

import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.widget.ImageView.ScaleType;

public final class NewDisplayOptions extends CacheOption {
    public interface DisplayShape {
        public static final int ORIGINAL = 0;
        public static final int ROUND = 1;
    }

    public interface DisplayAnimation {
        public static final int NONE = 0;
        public static final int FADE_IN = 1;
    }

    // display
    // decode options forever not null
    private Options decodeOptions;
    private final ScaleType scaleType;
    private final int displayShape;
    private final int displayAnimation;

    // disk
    public static NewDisplayOptions generateDiskOptions() {
        final boolean cache2Memory = false;
        final boolean cache2Disk = false;
        final NewDisplayOptions displayOptions = new NewDisplayOptions(cache2Memory, cache2Disk, null, null, DisplayShape.ORIGINAL, DisplayAnimation.NONE);
        return displayOptions;
    }

    // icon
    public static NewDisplayOptions generateIconOptions() {
        final int displayShape = DisplayShape.ORIGINAL;
        final NewDisplayOptions displayOptions = new NewDisplayOptions(true, true, null, null, displayShape, DisplayAnimation.NONE);
        return displayOptions;
    }

    // thumbnail
    public static NewDisplayOptions generateThumbnailOptions() {
        final NewDisplayOptions displayOptions = new NewDisplayOptions();
        return displayOptions;
    }

    // cover
    public static NewDisplayOptions generateCoverOptions() {
        final NewDisplayOptions displayOptions = new NewDisplayOptions();
        return displayOptions;
    }

    // fullImage
    public static NewDisplayOptions generateFullImageOptions() {
        final ScaleType scaleType = ScaleType.FIT_CENTER;
        final NewDisplayOptions displayOptions = new NewDisplayOptions(true, true, null, scaleType, DisplayShape.ORIGINAL, DisplayAnimation.NONE);
        return displayOptions;
    }

    public NewDisplayOptions() {
        super(true, true);
        final BitmapFactory.Options decodeOptions = new BitmapFactory.Options();
        this.setDecodeOptions(decodeOptions);;
        this.scaleType = null;;
        this.displayShape = DisplayShape.ORIGINAL;
        this.displayAnimation = DisplayAnimation.NONE;
    }

    public NewDisplayOptions(boolean cache2Memory, boolean cache2Disk, Options decodeOptions, ScaleType scaleType, int displayShape, int displayAnimation) {
        super(cache2Memory, cache2Disk);
        if (decodeOptions == null) {
            decodeOptions = new BitmapFactory.Options();
        }
        this.setDecodeOptions(decodeOptions);
        this.scaleType = scaleType;
        this.displayShape = displayShape;
        this.displayAnimation = displayAnimation;
    }

    public Options getDecodeOptions() {
        return decodeOptions;
    }

    public ScaleType getScaleType() {
        return scaleType;
    }

    public int getDisplayShape() {
        return displayShape;
    }

    public int getDisplayAnimation() {
        return displayAnimation;
    }

    public void setDecodeOptions(Options decodeOptions) {
        this.decodeOptions = decodeOptions;
    }
}
