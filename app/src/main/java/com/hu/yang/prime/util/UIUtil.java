package com.hu.yang.prime.util;

import android.content.Context;

/**
 * Created by yanghu on 2017/11/4.
 */

public class UIUtil {
    public static int dip2px(Context context, int dip) {
        //获取不同手机对应的不同的转换因子
        float d = context.getResources().getDisplayMetrics().density;
        //四舍五入( 80.5+0.5)   80.1+0.5
        return (int) (d * dip + 0.5);
    }

    public static int px2dip(Context context, int px) {
        float d = context.getResources().getDisplayMetrics().density;
        return (int) (px / d + 0.5);
    }
}
