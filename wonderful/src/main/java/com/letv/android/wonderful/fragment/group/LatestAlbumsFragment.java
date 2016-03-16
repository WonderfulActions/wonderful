package com.letv.android.wonderful.fragment.group;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.letv.android.wonderful.R;
import com.letv.android.wonderful.Tags;
import com.letv.android.wonderful.adapter.AlbumListAdapter;
import com.letv.android.wonderful.content.ContentCategory;
import com.letv.android.wonderful.content.ContentGroup;
import com.letv.android.wonderful.entity.WonderfulAlbum;
import com.letv.android.wonderful.entity.callback.AlbumsCallback;
import com.letv.android.wonderful.entity.task.FetchAlbumsTask;
import com.letv.android.wonderful.url.WonderfulUrl;

import java.util.ArrayList;

public class LatestAlbumsFragment extends Fragment {
    private static final String KEY_CONTENT_CATEGORY = "category";
    private static final String KEY_CONTENT_GROUP = "group";

    private int mCategory;
    private int mGroup;
    private ArrayList<WonderfulAlbum> mAlbums = new ArrayList<WonderfulAlbum>();
    private AlbumListAdapter mAlbumsAdapter;

    public static LatestAlbumsFragment newInstance(int contentType, int group) {
        final Bundle args = new Bundle();
        args.putInt(KEY_CONTENT_CATEGORY, contentType);
        args.putInt(KEY_CONTENT_GROUP, group);
        final LatestAlbumsFragment fragment = new LatestAlbumsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public LatestAlbumsFragment() {
        // do nothing
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(Tags.ALBUM_ADAPTER, "LatestFragment onCreate");
        super.onCreate(savedInstanceState);
        final Bundle bundle = getArguments();
        if (bundle != null) {
            mCategory = bundle.getInt(KEY_CONTENT_CATEGORY);
            mGroup = bundle.getInt(KEY_CONTENT_GROUP);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        Log.i(Tags.ALBUM_ADAPTER, "LatestFragment onCreateView");
        final View view = inflater.inflate(R.layout.fragment_latest, container, false);
        initAlbumList(view);
        fetchAlbums();
        return view;
    }

    private void fetchAlbums() {
        // String url = WonderfulUrl.ALL_ALBUMS;
        String url = null;
        if (mCategory == ContentCategory.NBA) {
            if (mGroup == ContentGroup.LATEST) {
                url = WonderfulUrl.NBA_LATEST_ALBUMS;
            } else if (mGroup == ContentGroup.HOT) {
                url = WonderfulUrl.NBA_HOT_ALBUMS;
            } else if (mGroup == ContentGroup.TOP) {
                url = WonderfulUrl.NBA_TOP_ALBUMS;
            } else if (mGroup == ContentGroup.RECOMMENDED) {
                url = WonderfulUrl.NBA_RECOMMENDED_ALBUMS;
            }
        } else if (mCategory == ContentCategory.SOCCER) {
            if (mGroup == ContentGroup.LATEST) {
                url = WonderfulUrl.SOCCER_LATEST_ALBUMS;
            } else if (mGroup == ContentGroup.HOT) {
                url = WonderfulUrl.SOCCER_HOT_ALBUMS;
            } else if (mGroup == ContentGroup.TOP) {
                url = WonderfulUrl.SOCCER_TOP_ALBUMS;
            } else if (mGroup == ContentGroup.RECOMMENDED) {
                url = WonderfulUrl.SOCCER_RECOMMENDED_ALBUMS;
            }
        } else if (mCategory == ContentCategory.MUSIC) {
            if (mGroup == ContentGroup.LATEST) {
                url = WonderfulUrl.MUSIC_LATEST_ALBUMS;
            } else if (mGroup == ContentGroup.HOT) {
                url = WonderfulUrl.MUSIC_HOT_ALBUMS;
            } else if (mGroup == ContentGroup.TOP) {
                url = WonderfulUrl.MUSIC_TOP_ALBUMS;
            } else if (mGroup == ContentGroup.RECOMMENDED) {
                url = WonderfulUrl.MUSIC_RECOMMENDED_ALBUMS;
            }
        } else if (mCategory == ContentCategory.PICTURE) {
            if (mGroup == ContentGroup.LATEST) {
                url = WonderfulUrl.PICTURE_LATEST_ALBUMS;
            } else if (mGroup == ContentGroup.HOT) {
                url = WonderfulUrl.PICTURE_HOT_ALBUMS;
            } else if (mGroup == ContentGroup.TOP) {
                url = WonderfulUrl.PICTURE_TOP_ALBUMS;
            } else if (mGroup == ContentGroup.RECOMMENDED) {
                url = WonderfulUrl.PICTURE_RECOMMENDED_ALBUMS;
            }
        } else if (mCategory == ContentCategory.GAME) {
            if (mGroup == ContentGroup.LATEST) {
                url = WonderfulUrl.GAME_LATEST_ALBUMS;
            } else if (mGroup == ContentGroup.HOT) {
                url = WonderfulUrl.GAME_HOT_ALBUMS;
            } else if (mGroup == ContentGroup.TOP) {
                url = WonderfulUrl.GAME_TOP_ALBUMS;
            } else if (mGroup == ContentGroup.RECOMMENDED) {
                url = WonderfulUrl.GAME_RECOMMENDED_ALBUMS;
            }
        } else if (mCategory == ContentCategory.HISTORY) {
            url = WonderfulUrl.ALL_ALBUMS;
        }
        Log.i(Tags.ALBUMS_FRAGMENT, "albums category = " + mCategory);
        Log.i(Tags.ALBUMS_FRAGMENT, "albums group = " + mGroup);
        Log.i(Tags.ALBUMS_FRAGMENT, "============ albums url = " + url);
        if (url != null) {
            new FetchAlbumsTask(mAlbumsCallback).executeOnExecutor(FetchAlbumsTask.THREAD_POOL_EXECUTOR, url);
        }
    }
    
    private AlbumsCallback mAlbumsCallback = new AlbumsCallback() {
        @Override
        public void onAlbumsResponse(ArrayList<WonderfulAlbum> albums) {
            Log.i(Tags.ALBUM_ACTIVITY, "LatestFragment onAlbumsResponse");
            if (albums != null && albums.size() > 0) {
                mAlbums.clear();
                mAlbums.addAll(albums);
                mAlbumsAdapter.notifyDataSetChanged();
            }
        }
    };

    private void initAlbumList(final View view) {
        // init adapter
        final RecyclerView latestAlbumList = (RecyclerView) view.findViewById(R.id.latest_album_list);
        mAlbumsAdapter = new AlbumListAdapter(mAlbums);
        latestAlbumList.setAdapter(mAlbumsAdapter);
        // init layoutManager
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        latestAlbumList.setLayoutManager(linearLayoutManager);
    }
}
