package com.hu.yang.prime.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;

import com.hu.yang.prime.R;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * Created by yanghu on 2017/12/29.
 */

public class BaseActivity extends SwipeBackActivity implements SwipeBackLayout.SwipeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSwipeBackLayout().addSwipeListener(this);
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_none);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_none,R.anim.slide_from_left);
    }

    @Override
    public void onScrollStateChange(int state, float scrollPercent) {

    }

    @Override
    public void onEdgeTouch(int edgeFlag) {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(20);
    }

    @Override
    public void onScrollOverThreshold() {

    }
}
