package com.letv.android.wonderful.fragment.group;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.letv.android.wonderful.R;
import com.letv.android.wonderful.content.ContentCategory;

public class RecommendedFragment extends Fragment {
    private static final String KEY_CONTENT_TYPE = "content";

    private int mContentType;

    public static RecommendedFragment newInstance(int contentType) {
        final Bundle args = new Bundle();
        args.putInt(KEY_CONTENT_TYPE, contentType);
        final RecommendedFragment fragment = new RecommendedFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public RecommendedFragment() {
        // do nothing
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle bundle = getArguments();
        if (bundle != null) {
            mContentType = bundle.getInt(KEY_CONTENT_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_recommended, container, false);
        final TextView textView = (TextView) view;
        final String contentLabel = ContentCategory.getContentLabel(this.getActivity(), mContentType);
        textView.setText(contentLabel);
        return view;
    }
}
