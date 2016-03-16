package com.letv.android.wonderful.fragment.category;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.letv.android.wonderful.R;
import com.letv.android.wonderful.adapter.TabPagerAdapter;
import com.letv.android.wonderful.content.ContentCategory;

public class NBAFragment extends Fragment {

    public static NBAFragment newInstance() {
        return new NBAFragment();
    }

    public NBAFragment() {
        // do nothing
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_nba, container, false);
        initTabLayout(view);
        return view;
    }
    
    private void initTabLayout(View view) {
        // init groupAdapter
        final ViewPager groupPager = (ViewPager) view.findViewById(R.id.group_pager);
        final TabPagerAdapter adapter = new TabPagerAdapter(getChildFragmentManager(), getContext(), ContentCategory.NBA);
        groupPager.setAdapter(adapter);

        // init tabLayout
        final TabLayout tabLayout = (TabLayout) view.findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(groupPager);
    }
}
