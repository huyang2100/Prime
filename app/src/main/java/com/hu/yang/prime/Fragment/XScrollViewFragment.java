package com.hu.yang.prime.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hu.yang.prime.R;
import com.hu.yang.prime.widget.XScrollView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by yanghu on 2017/12/22.
 */

public class XScrollViewFragment extends Fragment implements XScrollView.IXScrollViewListener {

    private XScrollView mScrollView;
    private ArrayAdapter<String> mAdapter;
    private ArrayList<String> mItems = new ArrayList<String>();
    private Handler mHandler;
    private int mIndex = 0;
    private LinearLayout ll_content;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_x_scrollview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mScrollView = (XScrollView) view.findViewById(R.id.xscrollview);
        mScrollView.setPullRefreshEnable(false);
        mScrollView.setPullLoadEnable(false);
        mScrollView.setAutoLoadEnable(true);
        mScrollView.setIXScrollViewListener(this);
        mScrollView.setRefreshTime(getTime());

        ll_content = new LinearLayout(getActivity());
        ll_content.setOrientation(LinearLayout.VERTICAL);
        mHandler = new Handler();

        geneItems();
        mScrollView.setView(ll_content);
    }


    private void geneItems() {
        for (int i = 0; i < 20; i++) {
            TextView tv = new TextView(getActivity());
            tv.setText("item: " + i);
            tv.setTextSize(36);
            tv.setPadding(20, 20, 20, 20);
            ll_content.addView(tv);
        }
    }

    private String getTime() {
        return new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA).format(new Date());
    }

    private void onLoad() {
        mScrollView.stopRefresh();
        mScrollView.stopLoadMore();
        mScrollView.setRefreshTime(getTime());
    }

    @Override
    public void onRefresh() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ll_content.removeAllViews();
                onLoad();
            }
        }, 2500);
    }

    @Override
    public void onLoadMore() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                geneItems();
                onLoad();
            }
        }, 2500);
    }
}
