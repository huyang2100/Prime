package com.hu.yang.prime.application;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

/**
 * Created by yanghu on 2017/10/20.
 */

public class PrimeApplication extends Application {
    private static Handler handler;
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler();
        context = getApplicationContext();
    }

    public static Handler getHandler() {
        return handler;
    }

    public static Context getContext() {
        return context;
    }
}
