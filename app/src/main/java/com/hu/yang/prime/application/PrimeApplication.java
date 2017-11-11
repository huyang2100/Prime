package com.hu.yang.prime.application;

import android.app.Application;
import android.os.Handler;

/**
 * Created by yanghu on 2017/10/20.
 */

public class PrimeApplication extends Application {
    private static Handler handler;

    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler();
    }

    public static Handler getHandler(){
        return handler;
    }
}
