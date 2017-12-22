package com.hu.yang.prime.util;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.hu.yang.prime.application.PrimeApplication;


/**
 * 屏幕大小工具类
 */
public class ScreenUtil {
    private WindowManager am;
    private Context screenUtilContext;
    public ScreenUtil(Context context) {
        screenUtilContext = context;
        try {
            am = ((Activity)context).getWindowManager();
        } catch (Exception e) {
            // TODO: handle exception
        }

    }
    /**
     * 获取屏幕的高
     * @return
     */
    public int getScreenHeight(){
        Display display = am.getDefaultDisplay();
        return  display.getHeight();
    }

    public int getScreenWidth(){
        Display display = am.getDefaultDisplay();
        return  display.getWidth();
    }

    public int dpTopx(float dpValue) {
        float scale = screenUtilContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public int pxTodp(float px) {
        float scale = screenUtilContext.getResources().getDisplayMetrics().density;
        return (int) ((px-0.5)/scale);
    }

    public static int getScreenWidth2() {
        return PrimeApplication.getContext().getResources().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight2() {
        return PrimeApplication.getContext().getResources().getDisplayMetrics().heightPixels;
    }

    public static int[] getScreenSize(Activity activity) {
        int[] size = new int[2];
        DisplayMetrics om = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(om);
        size[0] = om.widthPixels;
        size[1] = om.heightPixels;
        return size;
    }
}
