package com.letv.android.wonderful.server;

import android.util.Log;

import com.letv.android.wonderful.Tags;
import com.letv.android.wonderful.download.DownloadVideoUtil;
import com.letv.android.wonderful.entity.WonderfulAlbum;
import com.letv.android.wonderful.entity.WonderfulVideo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class AlbumsJson {
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String DETAIL = "detail";
    private static final String COVER = "cover";
    private static final String VIDEO_URL = "video_url";
    private static final String SIZE = "size";
    private static final String COUNT = "count";
    private static final String TIME = "time";
    private static final String VIDEOS = "videos";
    
    
    public static void generateAlbums() {
        try {
            final JSONArray albums = new JSONArray();
            final JSONArray videos = generateVideos();
            
            final JSONObject album0 = new JSONObject();
            album0.put(ID, Album.ID_0);
            album0.put(NAME, Album.NAME_0);
            album0.put(DETAIL, Album.DETAIL_0);
            album0.put(COVER, Album.COVER_URL_0);
            album0.put(SIZE, Album.SIZE_0);
            album0.put(VIDEOS, videos);
            
            final JSONObject album1 = new JSONObject();
            album1.put(ID, Album.ID_1);
            album1.put(NAME, Album.NAME_1);
            album1.put(DETAIL, Album.DETAIL_1);
            album1.put(COVER, Album.COVER_URL_1);
            album1.put(SIZE, Album.SIZE_1);
            album1.put(VIDEOS, videos);
            
            final JSONObject album2 = new JSONObject();
            album2.put(ID, Album.ID_2);
            album2.put(NAME, Album.NAME_2);
            album2.put(DETAIL, Album.DETAIL_2);
            album2.put(COVER, Album.COVER_URL_2);
            album2.put(SIZE, Album.SIZE_2);
            album2.put(VIDEOS, videos);
            
            final JSONObject album3 = new JSONObject();
            album3.put(ID, Album.ID_3);
            album3.put(NAME, Album.NAME_3);
            album3.put(DETAIL, Album.DETAIL_3);
            album3.put(COVER, Album.COVER_URL_3);
            album3.put(SIZE, Album.SIZE_3);
            album3.put(VIDEOS, videos);
            
            final JSONObject album4 = new JSONObject();
            album4.put(ID, Album.ID_4);
            album4.put(NAME, Album.NAME_4);
            album4.put(DETAIL, Album.DETAIL_4);
            album4.put(COVER, Album.COVER_URL_4);
            album4.put(SIZE, Album.SIZE_4);
            album4.put(VIDEOS, videos);
            
            final JSONObject album5 = new JSONObject();
            album5.put(ID, Album.ID_5);
            album5.put(NAME, Album.NAME_5);
            album5.put(DETAIL, Album.DETAIL_5);
            album5.put(COVER, Album.COVER_URL_5);
            album5.put(SIZE, Album.SIZE_5);
            album5.put(VIDEOS, videos);
            
            final JSONObject album6 = new JSONObject();
            album6.put(ID, Album.ID_6);
            album6.put(NAME, Album.NAME_6);
            album6.put(DETAIL, Album.DETAIL_6);
            album6.put(COVER, Album.COVER_URL_6);
            album6.put(SIZE, Album.SIZE_6);
            album6.put(VIDEOS, videos);
            
            albums.put(album0);
            albums.put(album1);
            albums.put(album2);
            albums.put(album3);
            albums.put(album4);
            albums.put(album5);
            albums.put(album6);
            
            final String json = albums.toString();
            Log.i(Tags.WONDERFUL_SERVER, "json = " + json);
            writeOut(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    
    
    
    // public static final String PATH = "/home/michael/Desktop/wonderfulGoals/albums.json";
    public static final String PATH = "/sdcard/wonderful/albums.json";
    
    public static void writeOut(String json) {
        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = new FileOutputStream(PATH);
            final BufferedOutputStream outputStream = new BufferedOutputStream(fileOutputStream);
            final byte[] buffer = json.getBytes();
            outputStream.write(buffer);
            outputStream.flush();
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    
    
    public static JSONArray generateVideos() {
        try {
            final JSONArray videos = new JSONArray();
            
            final JSONObject video0 = new JSONObject();
            video0.put(ID, Video.ID_0);
            video0.put(NAME, Video.NAME_0);
            video0.put(DETAIL, Video.DETAIL_0);
            video0.put(COVER, Video.COVER_URL_0);
            video0.put(VIDEO_URL, Video.VIDEO_URL_0);
            video0.put(SIZE, Video.SIZE_0);
            video0.put(TIME, Video.TIME_0);
            
            final JSONObject video1 = new JSONObject();
            video1.put(ID, Video.ID_0);
            video1.put(NAME, Video.NAME_1);
            video1.put(DETAIL, Video.DETAIL_1);
            video1.put(COVER, Video.COVER_URL_1);
            video1.put(VIDEO_URL, Video.VIDEO_URL_1);
            video1.put(SIZE, Video.SIZE_1);
            video1.put(TIME, Video.TIME_1);
            
            final JSONObject video2 = new JSONObject();
            video2.put(ID, Video.ID_2);
            video2.put(NAME, Video.NAME_2);
            video2.put(DETAIL, Video.DETAIL_2);
            video2.put(COVER, Video.COVER_URL_2);
            video2.put(VIDEO_URL, Video.VIDEO_URL_2);
            video2.put(SIZE, Video.SIZE_2);
            video2.put(TIME, Video.TIME_2);
            
            final JSONObject video3 = new JSONObject();
            video3.put(ID, Video.ID_3);
            video3.put(NAME, Video.NAME_3);
            video3.put(DETAIL, Video.DETAIL_3);
            video3.put(COVER, Video.COVER_URL_3);
            video3.put(VIDEO_URL, Video.VIDEO_URL_3);
            video3.put(SIZE, Video.SIZE_3);
            video3.put(TIME, Video.TIME_3);
            
            videos.put(video0);
            videos.put(video1);
            videos.put(video2);
            videos.put(video3);
            
            final String json = videos.toString();
//            Log.i(Tags.WONDERFUL_SERVER, "json = " + json);
            
            return videos;
            
            
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    
    /*
    [

    {
    "id":0,
    "name":"黑梦",
    "detail":"这么多年过去了，国内又有哪张专辑超过《黑梦》，深以为然",
    "cover":"http:\/\/xi-y.com\/nba\/album\/album1.jpg",
    "size":4,
    "videos":[{"id":0,"name":"帮主战胜爵士尽情庆祝","detail":"乔丹封王最后一战，总决赛战胜爵士，赢得第六座总冠军。胜利后欢呼雀跃","cover":"http:\/\/xi-y.com\/nba\/10653\/cover1.jpg","video_url":"http:\/\/xi-y.com\/nba\/10653\/top1.ts","size":2097152,"time":10000},{"id":0,"name":"绝杀凯尔特人干掉大鸟","detail":"乔丹总决赛绝杀凯尔特人，干掉大鸟伯德，赢得总冠军，宣告开起了一个时代，伯德说出了那句著名的，这个晚上是上帝穿着公牛的队服在打球","cover":"http:\/\/xi-y.com\/nba\/10653\/cover2.jpg","video_url":"http:\/\/xi-y.com\/nba\/10653\/top2.ts","size":3145728,"time":15000},{"id":2,"name":"乔丹绝杀勇士","detail":"９３年季后赛绝杀勇士　牛刀小试","cover":"http:\/\/xi-y.com\/nba\/10653\/cover3.jpg","video_url":"http:\/\/xi-y.com\/nba\/10653\/top3.ts","size":1048576,"time":16000},{"id":3,"name":"飞人最后一投","detail":"98年乔丹晃倒拉塞尔　完成最后一投　赢得比赛　无需多言","cover":"http:\/\/xi-y.com\/nba\/10653\/cover4.jpgg","video_url":"http:\/\/xi-y.com\/nba\/10653\/top4.ts","size":4194304,"time":11000}]
    },



    {"id":1,"name":"乔丹十大绝杀","detail":"乔丹职业生涯十大经典绝杀","cover":"http:\/\/xi-y.com\/nba\/album\/album2.jpg","size":4,"videos":[{"id":0,"name":"帮主战胜爵士尽情庆祝","detail":"乔丹封王最后一战，总决赛战胜爵士，赢得第六座总冠军。胜利后欢呼雀跃","cover":"http:\/\/xi-y.com\/nba\/10653\/cover1.jpg","video_url":"http:\/\/xi-y.com\/nba\/10653\/top1.ts","size":2097152,"time":10000},{"id":0,"name":"绝杀凯尔特人干掉大鸟","detail":"乔丹总决赛绝杀凯尔特人，干掉大鸟伯德，赢得总冠军，宣告开起了一个时代，伯德说出了那句著名的，这个晚上是上帝穿着公牛的队服在打球","cover":"http:\/\/xi-y.com\/nba\/10653\/cover2.jpg","video_url":"http:\/\/xi-y.com\/nba\/10653\/top2.ts","size":3145728,"time":15000},{"id":2,"name":"乔丹绝杀勇士","detail":"９３年季后赛绝杀勇士　牛刀小试","cover":"http:\/\/xi-y.com\/nba\/10653\/cover3.jpg","video_url":"http:\/\/xi-y.com\/nba\/10653\/top3.ts","size":1048576,"time":16000},{"id":3,"name":"飞人最后一投","detail":"98年乔丹晃倒拉塞尔　完成最后一投　赢得比赛　无需多言","cover":"http:\/\/xi-y.com\/nba\/10653\/cover4.jpgg","video_url":"http:\/\/xi-y.com\/nba\/10653\/top4.ts","size":4194304,"time":11000}]},



    {"id":2,"name":"罗纳尔多十大进球","detail":"罗纳尔多职业生涯十佳进球","cover":"http:\/\/xi-y.com\/nba\/album\/album3.jpg","size":4,"videos":[{"id":0,"name":"帮主战胜爵士尽情庆祝","detail":"乔丹封王最后一战，总决赛战胜爵士，赢得第六座总冠军。胜利后欢呼雀跃","cover":"http:\/\/xi-y.com\/nba\/10653\/cover1.jpg","video_url":"http:\/\/xi-y.com\/nba\/10653\/top1.ts","size":2097152,"time":10000},{"id":0,"name":"绝杀凯尔特人干掉大鸟","detail":"乔丹总决赛绝杀凯尔特人，干掉大鸟伯德，赢得总冠军，宣告开起了一个时代，伯德说出了那句著名的，这个晚上是上帝穿着公牛的队服在打球","cover":"http:\/\/xi-y.com\/nba\/10653\/cover2.jpg","video_url":"http:\/\/xi-y.com\/nba\/10653\/top2.ts","size":3145728,"time":15000},{"id":2,"name":"乔丹绝杀勇士","detail":"９３年季后赛绝杀勇士　牛刀小试","cover":"http:\/\/xi-y.com\/nba\/10653\/cover3.jpg","video_url":"http:\/\/xi-y.com\/nba\/10653\/top3.ts","size":1048576,"time":16000},{"id":3,"name":"飞人最后一投","detail":"98年乔丹晃倒拉塞尔　完成最后一投　赢得比赛　无需多言","cover":"http:\/\/xi-y.com\/nba\/10653\/cover4.jpgg","video_url":"http:\/\/xi-y.com\/nba\/10653\/top4.ts","size":4194304,"time":11000}]}

    ]
            */
    public static ArrayList<WonderfulAlbum> parseAlbums(String json) {
        /*
        public static final int ID_0 = 0;
        public static final String NAME_0 = "帮主战胜爵士尽情庆祝";
        public static final String DETAIL_0 = "乔丹封王最后一战，总决赛战胜爵士，赢得第六座总冠军。胜利后欢呼雀跃";
        public static final String COVER_URL_0 = "http://xi-y.com/nba/10653/cover1.jpg";
        public static final String VIDEO_URL_0 = "http://xi-y.com/nba/10653/top1.ts";
        public static final long SIZE_0 = 2 * 1024 * 1024;
        public static final long TIME_0 = 10 * 1000;
        */
        
        /*
        public static final int ID_0 = 0;
        public static final String NAME_0 = "黑梦";
        public static final String DETAIL_0 = "这么多年过去了，国内又有哪张专辑超过《黑梦》，深以为然";
        public static final String COVER_URL_0 = "http://xi-y.com/nba/album/album1.jpg";
        public static final int SIZE_0 = 4;
        */
        if (json != null) {
            try {
                final JSONArray albumArray = new JSONArray(json);
                final ArrayList<WonderfulAlbum> albums = new ArrayList<WonderfulAlbum>();
                for (int i = 0; i < albumArray.length(); i++) {
                    final JSONObject albumObject = albumArray.getJSONObject(i);
                    final int id = albumObject.getInt(ID);
                    final String name = albumObject.getString(NAME);
                    Log.i(Tags.WONDERFUL_SERVER, "============== wonderful server album name = " + name);
                    final String detail = albumObject.getString(DETAIL);
                    final String cover = albumObject.getString(COVER);
                    final int size = albumObject.getInt(SIZE);
                    // final String videosJson = album.getString(VIDEOS);
                    // parse videos
                    final ArrayList<WonderfulVideo> videos = parseVideos(albumObject.getString(VIDEOS));
                    final WonderfulAlbum album = new WonderfulAlbum(id, name, detail, cover, videos);
                    albums.add(album);
                }
                return albums;
            } catch (JSONException e) {
                Log.i(Tags.WONDERFUL_JSON, "parseAlbums json exeception e = " + e.toString());
                e.printStackTrace();
            }
        }
        return null;
    }
    
    /*
    [

    {
    "id":0,
    "name":"帮主战胜爵士尽情庆祝",
    "detail":"乔丹封王最后一战，总决赛战胜爵士，赢得第六座总冠军。胜利后欢呼雀跃",
    "cover":"http:\/\/xi-y.com\/nba\/10653\/cover1.jpg",
    "video":"http:\/\/xi-y.com\/nba\/10653\/top1.ts",
    "size":2097152,
    "time":10000
    },

    {"id":0,"name":"绝杀凯尔特人干掉大鸟","detail":"乔丹总决赛绝杀凯尔特人，干掉大鸟伯德，赢得总冠军，宣告开起了一个时代，伯德说出了那句著名的，这个晚上是上帝穿着公牛的队服在打球","cover":"http:\/\/xi-y.com\/nba\/10653\/cover2.jpg","video":"http:\/\/xi-y.com\/nba\/10653\/top2.ts","size":3145728,"time":15000},

    {"id":2,"name":"乔丹绝杀勇士","detail":"９３年季后赛绝杀勇士　牛刀小试","cover":"http:\/\/xi-y.com\/nba\/10653\/cover3.jpg","video":"http:\/\/xi-y.com\/nba\/10653\/top3.ts","size":1048576,"time":16000},

    {"id":3,"name":"飞人最后一投","detail":"98年乔丹晃倒拉塞尔　完成最后一投　赢得比赛　无需多言","cover":"http:\/\/xi-y.com\/nba\/10653\/cover4.jpgg","video":"http:\/\/xi-y.com\/nba\/10653\/top4.ts","size":4194304,"time":11000}

    ]
    */
    public static ArrayList<WonderfulVideo> parseVideos(String json) {
        try {
            JSONArray videoArray = new JSONArray(json);
            final ArrayList<WonderfulVideo> videos = new ArrayList<WonderfulVideo>();
            for (int i = 0; i < videoArray.length(); i++) {
                final JSONObject videoObject = videoArray.getJSONObject(i);
                final int id = videoObject.getInt(ID);
                final String name = videoObject.getString(NAME);
                // Log.i(Tags.WONDERFUL_SERVER, "wonderful server video name = " + name);
                final String detail = videoObject.getString(DETAIL);
                final String cover = videoObject.getString(COVER);
                final String videoUrl = videoObject.getString(VIDEO_URL);
                final long size = videoObject.getLong(SIZE);
                final long time = videoObject.getLong(TIME);
                final WonderfulVideo video = new WonderfulVideo(id, name, detail, cover, size, time, videoUrl);
                videos.add(video);
            }
            return videos;
        } catch (JSONException e) {
            Log.i(Tags.WONDERFUL_JSON, "parseAlbums json exeception e = " + e.toString());
            e.printStackTrace();
        }
        return null;
    }
}



class Video {
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

class Album {
    /*
    http://video0.oss-cn-beijing.aliyuncs.com/nba/11/10cover.jpg
        http://video0.oss-cn-beijing.aliyuncs.com/nba/11/albumCover.jpg
        http://video0.oss-cn-beijing.aliyuncs.com/nba/11/theShoot.ts


        http://xi-y.com/nba/11/10cover.jpg
        http://xi-y.com/nba/11/albumCover.jpg
        http://xi-y.com/nba/11/theShoot.ts
            */
    public static final int ID_0 = 0;
    public static final String NAME_0 = "黑梦";
    public static final String DETAIL_0 = "这么多年过去了，国内又有哪张专辑超过《黑梦》，深以为然";
    public static final String COVER_URL_0 = "http://xi-y.com/nba/album/album1.jpg";
    public static final int SIZE_0 = 4;

    public static final int ID_1 = 1;
    public static final String NAME_1 = "乔丹十大绝杀";
    public static final String DETAIL_1 = "乔丹职业生涯十大经典绝杀";
    public static final String COVER_URL_1 = "http://xi-y.com/nba/album/album2.jpg";
    public static final int SIZE_1 = 4;

    public static final int ID_2 = 2;
    public static final String NAME_2 = "罗纳尔多十大进球";
    public static final String DETAIL_2 = "罗纳尔多职业生涯十佳进球";
    public static final String COVER_URL_2 = "http://xi-y.com/nba/album/album3.jpg";
    public static final int SIZE_2 = 4;

    public static final int ID_3 = 3;
    public static final String NAME_3 = "周润发十大经典镜头";
    public static final String DETAIL_3 = "小马哥　赌神　许文强　周润发在影坛塑造了无数经典形象";
    public static final String COVER_URL_3 = "http://xi-y.com/nba/album/album4.jpg";
    public static final int SIZE_3 = 4;

    public static final int ID_4 = 4;
    public static final String NAME_4 = "魔兽争霸１０大围杀";
    public static final String DETAIL_4 = "魔兽争霸各种经典围杀，精彩绝伦";
    public static final String COVER_URL_4 = "http://xi-y.com/nba/album/album5.jpg";
    public static final int SIZE_4 = 4;

    public static final int ID_5 = 5;
    public static final String NAME_5 = "邓丽君５大永恒回忆";
    public static final String DETAIL_5 = "华语乐坛第一人，邓丽君留下了无数经典瞬间，她婉转动人的歌喉至今仍然让人难以忘怀。";
    public static final String COVER_URL_5 = "http://xi-y.com/nba/album/album6.jpg";
    public static final int SIZE_5 = 4;

    public static final int ID_6 = 6;
    public static final String NAME_6 = "2015-2016赛季科比十佳球";
    public static final String DETAIL_6 = "2015-2016科比最后一个赛季，科比任然兢兢业业，全明星投票拿下了有史以来最高票数，无数人开始怀念这个倔强的男人";
    public static final String COVER_URL_6 = "http://xi-y.com/nba/album/album7.jpg";
    public static final int SIZE_6 = 4;

    public static ArrayList<WonderfulAlbum> generateNBALatestAlbums() {
        final ArrayList<WonderfulAlbum> albums = new ArrayList<WonderfulAlbum>();
        final ArrayList<WonderfulVideo> videos = Video.generateNBAVideos();
        final WonderfulAlbum album0 = new WonderfulAlbum(ID_0, NAME_0, DETAIL_0, COVER_URL_0, videos);
        final WonderfulAlbum album1 = new WonderfulAlbum(ID_1, NAME_1, DETAIL_1, COVER_URL_1, videos);
        final WonderfulAlbum album2 = new WonderfulAlbum(ID_2, NAME_2, DETAIL_2, COVER_URL_2, videos);
        final WonderfulAlbum album3 = new WonderfulAlbum(ID_3, NAME_3, DETAIL_3, COVER_URL_3, videos);
        final WonderfulAlbum album4 = new WonderfulAlbum(ID_4, NAME_4, DETAIL_4, COVER_URL_4, videos);
        final WonderfulAlbum album5 = new WonderfulAlbum(ID_5, NAME_5, DETAIL_5, COVER_URL_5, videos);
        final WonderfulAlbum album6 = new WonderfulAlbum(ID_6, NAME_6, DETAIL_6, COVER_URL_6, videos);
        albums.add(album0);
        albums.add(album1);
        albums.add(album2);
        albums.add(album3);
        albums.add(album4);
        albums.add(album5);
        albums.add(album6);
        return albums;
    }
}
