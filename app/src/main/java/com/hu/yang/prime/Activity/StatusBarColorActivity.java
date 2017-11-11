package com.hu.yang.prime.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.hu.yang.prime.R;
import com.hu.yang.prime.util.StatusBarUtil;

/**
 * Created by yanghu on 2017/11/8.
 */

public class StatusBarColorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        StatusBarUtil.setStatusBarColor(this, Color.BLUE);
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, StatusBarColorActivity.class);
        context.startActivity(intent);
        String ss = "{\"message\":{\"content\":\"北京市高级人民法院[2017]第333次党组会议\",\"courtId\":0,\"inviteId\":12992,\"scheduleId\":283566,\"title\":\"李梅邀请参加院党组会议\",\"type\":0,\"uid\":9000706},\"red\":{\"id\":5,\"num\":9}}";
    }
}
