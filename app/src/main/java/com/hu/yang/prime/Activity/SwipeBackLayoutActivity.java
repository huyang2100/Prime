package com.hu.yang.prime.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.hu.yang.prime.Fragment.PDFViewPagerFragment;
import com.hu.yang.prime.R;

/**
 * Created by yanghu on 2017/12/29.
 */

public class SwipeBackLayoutActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_layout,new PDFViewPagerFragment()).commit();
    }

    public static void actionStart(Activity activity) {
        Intent intent = new Intent(activity, SwipeBackLayoutActivity.class);
        activity.startActivity(intent);
    }
}
