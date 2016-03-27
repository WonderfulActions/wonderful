package com.letv.android.wonderful.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.letv.android.wallpaper.display.DisplayUtil;
import com.letv.android.wonderful.R;
import com.letv.android.wonderful.Tags;
import com.letv.android.wonderful.activity.AlbumVideosActivity;
import com.letv.android.wonderful.application.WonderfulApplication;
import com.letv.android.wonderful.entity.WonderfulAlbum;

import java.util.ArrayList;

public class AlbumListAdapter extends RecyclerView.Adapter<AlbumListAdapter.AlbumViewHolder> {

    public static class AlbumViewHolder extends RecyclerView.ViewHolder {
        public ImageView coverView;
        public TextView nameView;
        // public TextView detailView;

        /*
        public Button likeButton;
        public Button collectButton;
        public Button downloadButton;
        public Button othersButton;
        */

        public AlbumViewHolder(View itemView) {
            super(itemView);
            coverView = (ImageView) itemView.findViewById(R.id.album_cover_view);
            nameView = (TextView) itemView.findViewById(R.id.album_name_view);
            // detailView = (TextView) itemView.findViewById(R.id.album_detail_view);

            /*
            likeButton = (Button) itemView.findViewById(R.id.album_like_button);
            collectButton = (Button) itemView.findViewById(R.id.album_collect_button);
            downloadButton = (Button) itemView.findViewById(R.id.album_download_button);
            othersButton = (Button) itemView.findViewById(R.id.album_others_button);
            */
        }
    }

    private ArrayList<WonderfulAlbum> mAlbums;

    public AlbumListAdapter(ArrayList<WonderfulAlbum> albums) {
        Log.i(Tags.ALBUM_ADAPTER, "LatestAlbumAdapter");
        mAlbums = albums;
    }

    @Override
    public int getItemCount() {
        return mAlbums.size();
    }

    @Override
    public AlbumViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i(Tags.ALBUM_ADAPTER, "onCreateViewHolder");
        final View albumView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_album_list, parent, false);
        final int width = WonderfulApplication.mDisplayWidth;
        final int height = width * 9/16;
        final ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(width, height);
        albumView.setLayoutParams(params);
        final AlbumViewHolder holder = new AlbumViewHolder(albumView);
        return holder;
    }

    @Override
    public void onBindViewHolder(final AlbumViewHolder holder, final int position) {
        Log.i(Tags.ALBUM_ADAPTER, "onBindViewHolder");
        // get data
        final WonderfulAlbum album = mAlbums.get(position);
        
        
        
        /*
        Log.i(Tags.ALBUM_ADAPTER, "album id = " + album.getId());
        Log.i(Tags.ALBUM_ADAPTER, "album name = " + album.getName());
        Log.i(Tags.ALBUM_ADAPTER, "album cover url = " + album.getCoverUrl());
        */
        
        
        
        // init item view
        DisplayUtil.display(holder.coverView, album.getCoverUrl());
        holder.nameView.setText(album.getName());
        // holder.detailView.setText(album.getDetail());
        // set onClickListener
        holder.coverView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(Tags.ALBUM_ADAPTER, "holder.coverView onClick position = " + position);
                // show album videos
                final Context context = v.getContext();
                AlbumVideosActivity.showAlbumVideos(context, album);
            }
        });
    }
}
