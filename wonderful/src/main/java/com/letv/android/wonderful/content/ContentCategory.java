package com.letv.android.wonderful.content;

import android.content.Context;

import com.letv.android.wonderful.R;

public class ContentCategory {
    public static final int NBA = 0;
    public static final int SOCCER = 1;
    public static final int MUSIC = 2;
    public static final int PICTURE = 3;
    public static final int GAME = 4;
    
    public static final int COLLECTED = 100;
    public static final int HISTORY = 200;

    public static String getContentLabel(Context context, int contentType) {
        switch (contentType) {
            case NBA:
                return context.getString(R.string.category_nba);
            case SOCCER:
                return context.getString(R.string.category_soccer);
            case MUSIC:
                return context.getString(R.string.category_music);
            case PICTURE:
                return context.getString(R.string.category_picture);
            case GAME:
                return context.getString(R.string.category_game);
            default:
                return context.getString(R.string.category_nba);
        }
    }
}
