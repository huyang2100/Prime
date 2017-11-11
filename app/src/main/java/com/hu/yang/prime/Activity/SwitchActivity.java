package com.hu.yang.prime.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.hu.yang.prime.R;

/**
 * Created by yanghu on 2017/9/19.
 */

public class SwitchActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switch);
    }

    public static void actionStart(Context ctx){
        ctx.startActivity(new Intent(ctx,SwitchActivity.class));
    }
}
