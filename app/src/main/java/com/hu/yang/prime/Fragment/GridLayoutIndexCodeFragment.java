package com.hu.yang.prime.Fragment;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hu.yang.prime.R;

import java.util.Arrays;

/**
 * Created by yanghu on 2017/10/19.
 */

public class GridLayoutIndexCodeFragment extends Fragment {
    private static final String TAG = GridLayoutIndexCodeFragment.class.getSimpleName();
    private static final int COLUMN_COUNT = 3;
    private static final int GAP = 3;
    private String[] modes = {"新闻", "娱乐", "视频", "音乐", "电影", "艺术", "篮球", "足球", "科技", "未来"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gridlayout_index_code, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (modes.length % COLUMN_COUNT != 0) {
            int newLength = ((modes.length / COLUMN_COUNT) + 1) * COLUMN_COUNT;
            modes = Arrays.copyOf(modes, newLength);
        }

        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        final GridLayout gridLayout = (GridLayout) view.findViewById(R.id.gridLayout);
//        gridLayout.setBackgroundColor(Color.RED);
        gridLayout.setColumnCount(COLUMN_COUNT);
        for (int i = 0; i < modes.length; i++) {
            TextView tv = new TextView(getActivity());
            tv.setText(modes[i]);
            tv.setTextSize(16);
            tv.setHeight(screenWidth / COLUMN_COUNT);
            ColorStateList colorStateList = getResources().getColorStateList(R.color.sel_tv_index);
            tv.setTextColor(colorStateList);
            tv.setBackgroundResource(R.drawable.bg_index);
            tv.setGravity(Gravity.CENTER);
            GridLayout.Spec rowSpec = GridLayout.spec(i / COLUMN_COUNT);
            GridLayout.Spec columnSpec = GridLayout.spec(i % COLUMN_COUNT, 1f);
            GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams(rowSpec, columnSpec);

            if (i >= COLUMN_COUNT) {
                layoutParams.topMargin = GAP;
            }


            if(!TextUtils.isEmpty(modes[i])){
                tv.setClickable(true);
                if (i % COLUMN_COUNT < COLUMN_COUNT - 1) {
                    layoutParams.rightMargin = GAP;
                }
            }

            gridLayout.addView(tv, i, layoutParams);
        }
    }
}
