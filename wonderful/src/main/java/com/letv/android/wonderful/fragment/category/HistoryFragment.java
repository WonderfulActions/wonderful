package com.letv.android.wonderful.fragment.category;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.letv.android.wonderful.R;
import com.letv.android.wonderful.content.ContentCategory;
import com.letv.android.wonderful.content.ContentGroup;
import com.letv.android.wonderful.fragment.group.LatestAlbumsFragment;

public class HistoryFragment extends Fragment {

    public static HistoryFragment newInstance() {
        return new HistoryFragment();
    }

    public HistoryFragment() {
        // do nothing
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_history, container, false);
        final Fragment latestFragment = LatestAlbumsFragment.newInstance(ContentCategory.HISTORY, ContentGroup.HISTORY);
        final FragmentManager manager = getChildFragmentManager();
        final FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.history_container, latestFragment);
        transaction.commitAllowingStateLoss();
        return view;
    }
}
