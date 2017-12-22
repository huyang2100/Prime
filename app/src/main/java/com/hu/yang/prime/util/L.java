package com.hu.yang.prime.util;

import android.util.Log;

/**
 * Created by yanghu on 2017/12/7.
 */

public class L {

    private static final String TAG = "tag";
    private static boolean isDebug;

    public static void i(String msg){
        if(!isDebug){
            Log.i(TAG, msg);
        }
    }
}
