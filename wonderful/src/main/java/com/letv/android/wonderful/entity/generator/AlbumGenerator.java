package com.letv.android.wonderful.entity.generator;

import com.letv.android.wonderful.entity.WonderfulAlbum;
import com.letv.android.wonderful.entity.WonderfulVideo;

import java.util.ArrayList;

public class AlbumGenerator {
    /*
    public static final int ID_0 = 0;
    public static final String NAME_0 = "黑梦";
    public static final String DETAIL_0 = "这么多年过去了，国内又有哪张专辑超过《黑梦》，深以为然";
    public static final String COVER_URL_0 = "http://m.360buyimg.com/n12/jfs/t190/106/764203502/328423/17dc0cf5/539854e1N2c427793.jpg!q70.jpg";
    public static final int SIZE_0 = 4;

    public static final int ID_1 = 1;
    public static final String NAME_1 = "乔丹十大绝杀";
    public static final String DETAIL_1 = "乔丹职业生涯十大经典绝杀";
    public static final String COVER_URL_1 = "http://c.hiphotos.baidu.com/zhidao/pic/item/63d9f2d3572c11df1bd0fe6c612762d0f703c278.jpg";
    public static final int SIZE_1 = 4;

    public static final int ID_2 = 2;
    public static final String NAME_2 = "罗纳尔多十大进球";
    public static final String DETAIL_2 = "罗纳尔多职业生涯十佳进球";
    public static final String COVER_URL_2 = "http://lcd.yesky.com/imagelist/06/26/2os3osm649po.jpg";
    public static final int SIZE_2 = 4;

    public static final int ID_3 = 3;
    public static final String NAME_3 = "周润发十大经典镜头";
    public static final String DETAIL_3 = "小马哥　赌神　许文强　周润发在影坛塑造了无数经典形象";
    public static final String COVER_URL_3 = "http://imgsrc.baidu.com/forum/w%3D580/sign=90a0a5862e738bd4c421b239918b876c/66f272f082025aafee1ba490fbedab64024f1aa5.jpg";
    public static final int SIZE_3 = 4;

    public static final int ID_4 = 4;
    public static final String NAME_4 = "魔兽争霸１０大围杀";
    public static final String DETAIL_4 = "魔兽争霸各种经典围杀，精彩绝伦";
    // public static final String COVER_URL_4 = "http://www.minecraft222.com/uploads/allimg/110609/10345UL3-2.jpg";
    public static final String COVER_URL_4 = "http://www.minecraft222.com/uploads/allimg/110609/10345U964-3.jpg";
    public static final int SIZE_4 = 4;

    public static final int ID_5 = 5;
    public static final String NAME_5 = "邓丽君５大永恒回忆";
    public static final String DETAIL_5 = "华语乐坛第一人，邓丽君留下了无数经典瞬间，她婉转动人的歌喉至今仍然让人难以忘怀。";
    public static final String COVER_URL_5 = "http://www.chinadaily.com.cn/hqylss/2006-04/14/xin_5204031621225012690210.jpg";
    public static final int SIZE_5 = 4;

    public static final int ID_6 = 6;
    public static final String NAME_6 = "2015-2016赛季科比十佳球";
    public static final String DETAIL_6 = "2015-2016科比最后一个赛季，科比任然兢兢业业，全明星投票拿下了有史以来最高票数，无数人开始怀念这个倔强的男人";
    public static final String COVER_URL_6 = "http://sports.nen.com.cn/imagelist/10/17/434ttlfe96y3.jpg";
    public static final int SIZE_6 = 4;
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
        final ArrayList<WonderfulVideo> videos = VideoGenerator.generateNBAVideos();
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
