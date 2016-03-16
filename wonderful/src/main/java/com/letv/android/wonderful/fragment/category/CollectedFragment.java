package com.letv.android.wonderful.fragment.category;

import android.support.v4.app.Fragment;

public class CollectedFragment extends Fragment {

    public static CollectedFragment newInstance() {
        return new CollectedFragment();
    }

    public CollectedFragment() {
        // do nothing
    }
    
    /*
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_collect, container, false);
        final Fragment latestFragment = LatestAlbumsFragment.newInstance(ContentCategory.COLLECTED, ContentGroup.COLLECTED);
        final FragmentManager manager = getChildFragmentManager();
        final FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.collect_container, latestFragment);
        transaction.commitAllowingStateLoss();
        return view;
    }
    */
    
}
