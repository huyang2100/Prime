package com.hu.yang.prime.util;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.hu.yang.prime.R;


/**
 * Created by yanghu on 2017/11/19.
 */

public class PopUtil {

    /**
     * 从底部显示View
     *
     * @param activity    Activity
     * @param contentView 弹出框的view
     * @param rootView    最外层的view
     */
    public static PopupWindow showBottom(final Activity activity, View contentView, View rootView) {
        final PopupWindow pw = new PopupWindow(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        pw.setAnimationStyle(R.style.popwin_anim_style);
        pw.setContentView(contentView);
        pw.setBackgroundDrawable(activity.getResources().getDrawable(android.R.color.transparent));
        pw.setOutsideTouchable(true);
        pw.setFocusable(true);
        changeWindowAlpha(activity, 1f, 0.7f);
        pw.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
        pw.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                pw.dismiss();
                changeWindowAlpha(activity, 0.7f, 1f);
            }
        });
        return pw;
    }

    private static void changeWindowAlpha(final Activity activity, float start, float end) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(start, end);
        valueAnimator.setDuration(350);
        valueAnimator.start();
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float alpha = (float) animation.getAnimatedValue();
                WindowManager.LayoutParams params = activity.getWindow().getAttributes();
                params.alpha = alpha;
                activity.getWindow().setAttributes(params);
            }
        });
    }
}
