package com.letv.android.wonderful.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.letv.android.wonderful.Tags;
import com.letv.android.wonderful.content.ContentGroup;
import com.letv.android.wonderful.fragment.group.LatestAlbumsFragment;

public class TabPagerAdapter extends FragmentPagerAdapter {

    private static final int PAGE_COUNT = 4;

    private Context mContext;
    private int mCategory;

    public TabPagerAdapter(FragmentManager fm, Context context, int category) {
        super(fm);
        mContext = context;
        mCategory = category;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        final String title = ContentGroup.getGroupLabel(mContext, position);
        Log.i(Tags.WONDERFUL_VIDEO, "title = " + title);
        return title;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                final Fragment latestFragment = LatestAlbumsFragment.newInstance(mCategory, ContentGroup.LATEST);
                return latestFragment;
            case 1:
                // final Fragment hotFragment = HotFragment.newInstance(mContentType);
                final Fragment hotFragment = LatestAlbumsFragment.newInstance(mCategory, ContentGroup.HOT);
                return hotFragment;
            case 2:
                // final Fragment topFragment = TopFragment.newInstance(mContentType);
                final Fragment topFragment = LatestAlbumsFragment.newInstance(mCategory, ContentGroup.TOP);
                return topFragment;
            case 3:
                // final Fragment recommendedFragment = RecommendedFragment.newInstance(mContentType);
                final Fragment recommendedFragment = LatestAlbumsFragment.newInstance(mCategory, ContentGroup.RECOMMENDED);
                return recommendedFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

}
