package com.letv.android.wonderful.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.letv.android.wonderful.R;

public class VideoCategoriesAdapter extends BaseAdapter {
    private String[] mCategories;

    public VideoCategoriesAdapter(String[] categories) {
        mCategories = categories;
    }

    @Override
    public int getCount() {
        return mCategories.length;
    }

    @Override
    public Object getItem(int position) {
        return mCategories[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final TextView categoryView = (TextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        final String category = mCategories[position];
        categoryView.setText(category);
        return categoryView;
    }

}
