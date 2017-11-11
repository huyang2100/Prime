package com.hu.yang.prime.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.hu.yang.prime.Fragment.AppListFragment;
import com.hu.yang.prime.Fragment.CanvasFragment;
import com.hu.yang.prime.Fragment.ChinaFragment;
import com.hu.yang.prime.Fragment.GridViewIndexCodeFragment;
import com.hu.yang.prime.Fragment.PlayMusicFragment;
import com.hu.yang.prime.R;

/**
 * Created by yanghu on 2017/10/23.
 */

public class PagerActivity extends AppCompatActivity {
    String[] funs = {"canvas","applist","gridindex","china"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragmentpager);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(4);
        viewPager.setAdapter(new MyAdapter(getSupportFragmentManager()));

        TabLayout tabLayout = (TabLayout) findViewById(R.id.slidingTabLaout);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabTextColors(Color.GRAY,Color.BLACK);
    }

    class MyAdapter extends FragmentPagerAdapter{

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return funs[position];
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position){
                case 0:
                    fragment = CanvasFragment.newInstance();
                    break;
                case 1:
                    fragment = AppListFragment.newInstance();
                    break;
                case 2:
                    fragment = GridViewIndexCodeFragment.newInstance();
                    break;
                case 3:
                    fragment = ChinaFragment.newInstance();
                    break;
                case 4:
                    fragment = PlayMusicFragment.newInstance();
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return funs.length;
        }
    }

    public static void newIntent(Context context) {
        Intent intent = new Intent(context, PagerActivity.class);
        context.startActivity(intent);
    }
}
