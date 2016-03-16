package com.letv.android.wonderful.entity.generator;

import com.letv.android.wonderful.download.DownloadVideoUtil;
import com.letv.android.wonderful.entity.WonderfulVideo;

import java.util.ArrayList;

public class VideoGenerator {
    /*
        http://pop900.com/top1.ts
        http://pop900.com/top2.ts
        http://pop900.com/top3.ts
        http://pop900.com/top4.ts
    */
    /*
    private static final int ID_0 = 0;
    private static final String NAME_0 = "帮主战胜爵士尽情庆祝";
    private static final String DETAIL_0 = "乔丹封王最后一战，总决赛战胜爵士，赢得第六座总冠军。胜利后欢呼雀跃";
    private static final String COVER_URL_0 = "http://pop900.com/cover2.jpg";
    // private static final int COVER_RES_0 = R.drawable.cover4;
    // private static final String SAMPLE0 = "/sdcard/wonderful/michaelTop4.ts";
    private static final String VIDEO_URL_0 = "http://pop900.com/top1.ts";
    private static final long SIZE_0 = 2 * 1024 * 1024;
    private static final long TIME_0 = 10 * 1000;

    private static final int ID_1 = 1;
    private static final String NAME_1 = "绝杀凯尔特人干掉大鸟";
    private static final String DETAIL_1 = "乔丹总决赛绝杀凯尔特人，干掉大鸟伯德，赢得总冠军，宣告开起了一个时代，伯德说出了那句著名的，这个晚上是上帝穿着公牛的队服在打球";
    private static final String COVER_URL_1 = "http://pop900.com/cover3.jpg";
    // private static final int COVER_RES_1 = R.drawable.cover1;
    // private static final String SAMPLE1 = "/sdcard/wonderful/michaelTop1.ts";
    private static final String VIDEO_URL_1 = "http://pop900.com/top2.ts";
    private static final long SIZE_1 = 3 * 1024 * 1024;
    private static final long TIME_1 = 15 * 1000;

    private static final int ID_2 = 2;
    private static final String NAME_2 = "乔丹绝杀勇士";
    private static final String DETAIL_2 = "９３年季后赛绝杀勇士　牛刀小试";
    private static final String COVER_URL_2 = "http://pop900.com/cover1.jpg";
    // private static final int COVER_RES_2 = R.drawable.cover2;
    // private static final String SAMPLE2 = "/sdcard/wonderful/michaelTop2.ts";
    private static final String VIDEO_URL_2 = "http://pop900.com/top3.ts";
    private static final long SIZE_2 = 1 * 1024 * 1024;
    private static final long TIME_2 = 16 * 1000;

    private static final int ID_3 = 3;
    private static final String NAME_3 = "飞人最后一投";
    private static final String DETAIL_3 = "98年乔丹晃倒拉塞尔　完成最后一投　赢得比赛　无需多言";
    private static final String COVER_URL_3 = "http://pop900.com/cover4.jpg";
    // private static final int COVER_RES_3 = R.drawable.cover3;
    // private static final String SAMPLE3 = "/sdcard/wonderful/michaelTop3.ts";
    private static final String VIDEO_URL_3 = "http://pop900.com/top4.ts";
    private static final long SIZE_3 = 4 * 1024 * 1024;
    private static final long TIME_3 = 11 * 1000;
    */
    
    public static final int ID_0 = 0;
    public static final String NAME_0 = "帮主战胜爵士尽情庆祝";
    public static final String DETAIL_0 = "乔丹封王最后一战，总决赛战胜爵士，赢得第六座总冠军。胜利后欢呼雀跃";
    public static final String COVER_URL_0 = "http://xi-y.com/nba/10653/cover1.jpg";
    public static final String VIDEO_URL_0 = "http://xi-y.com/nba/10653/top1.ts";
    public static final long SIZE_0 = 2 * 1024 * 1024;
    public static final long TIME_0 = 10 * 1000;

    public static final int ID_1 = 1;
    public static final String NAME_1 = "绝杀凯尔特人干掉大鸟";
    public static final String DETAIL_1 = "乔丹总决赛绝杀凯尔特人，干掉大鸟伯德，赢得总冠军，宣告开起了一个时代，伯德说出了那句著名的，这个晚上是上帝穿着公牛的队服在打球";
    public static final String COVER_URL_1 = "http://xi-y.com/nba/10653/cover2.jpg";
    public static final String VIDEO_URL_1 = "http://xi-y.com/nba/10653/top2.ts";
    public static final long SIZE_1 = 3 * 1024 * 1024;
    public static final long TIME_1 = 15 * 1000;

    public static final int ID_2 = 2;
    public static final String NAME_2 = "乔丹绝杀勇士";
    public static final String DETAIL_2 = "９３年季后赛绝杀勇士　牛刀小试";
    public static final String COVER_URL_2 = "http://xi-y.com/nba/10653/cover3.jpg";
    public static final String VIDEO_URL_2 = "http://xi-y.com/nba/10653/top3.ts";
    public static final long SIZE_2 = 1 * 1024 * 1024;
    public static final long TIME_2 = 16 * 1000;

    public static final int ID_3 = 3;
    public static final String NAME_3 = "飞人最后一投";
    public static final String DETAIL_3 = "98年乔丹晃倒拉塞尔　完成最后一投　赢得比赛　无需多言";
    public static final String COVER_URL_3 = "http://xi-y.com/nba/10653/cover4.jpg";
    public static final String VIDEO_URL_3 = "http://xi-y.com/nba/10653/top4.ts";
    public static final long SIZE_3 = 4 * 1024 * 1024;
    public static final long TIME_3 = 11 * 1000;

    public static ArrayList<WonderfulVideo> generateNBAVideos() {
        final ArrayList<WonderfulVideo> videos = new ArrayList<WonderfulVideo>();
        final WonderfulVideo video0 = new WonderfulVideo(ID_0, NAME_0, DETAIL_0, COVER_URL_0, SIZE_0, TIME_0, VIDEO_URL_0);
        final boolean isDownloaded0 = DownloadVideoUtil.isDiskCacheAvailable(VIDEO_URL_0);
        video0.setDownloaded(isDownloaded0);
        final WonderfulVideo video1 = new WonderfulVideo(ID_1, NAME_1, DETAIL_1, COVER_URL_1, SIZE_1, TIME_1, VIDEO_URL_1);
        final boolean isDownloaded1 = DownloadVideoUtil.isDiskCacheAvailable(VIDEO_URL_1);
        video1.setDownloaded(isDownloaded1);
        final WonderfulVideo video2 = new WonderfulVideo(ID_2, NAME_2, DETAIL_2, COVER_URL_2, SIZE_2, TIME_2, VIDEO_URL_2);
        final boolean isDownloaded2 = DownloadVideoUtil.isDiskCacheAvailable(VIDEO_URL_2);
        video2.setDownloaded(isDownloaded2);
        final WonderfulVideo video3 = new WonderfulVideo(ID_3, NAME_3, DETAIL_3, COVER_URL_3, SIZE_3, TIME_3, VIDEO_URL_3);
        final boolean isDownloaded3 = DownloadVideoUtil.isDiskCacheAvailable(VIDEO_URL_3);
        video3.setDownloaded(isDownloaded3);
        videos.add(video0);
        videos.add(video1);
        videos.add(video2);
        videos.add(video3);
        return videos;
    }

}
