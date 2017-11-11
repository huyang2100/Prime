package com.hu.yang.prime.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hu.yang.prime.R;

/**
 * Created by yanghu on 2017/10/23.
 */

public class PagerBarFragment extends Fragment {
    int[] cols = {Color.CYAN,Color.GREEN,Color.MAGENTA,Color.YELLOW};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pager_bar,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) viewPager.getLayoutParams();
        layoutParams.height = (int) (getResources().getDisplayMetrics().widthPixels*2/3.f);
        viewPager.setLayoutParams(layoutParams);
        viewPager.setAdapter(new MyPagerAdapter());
    }

    class MyPagerAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return cols.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TextView view = new TextView(getContext());
            view.setText(String.valueOf(position));
            view.setTextSize(48);
            view.setGravity(Gravity.CENTER);
            view.setBackgroundColor(cols[position]);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
