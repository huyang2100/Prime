package com.hu.yang.prime.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Xfermode;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;

/**
 * Created by yanghu on 2017/8/25.
 */

public class FocusView extends View implements ViewTreeObserver.OnGlobalLayoutListener, View.OnTouchListener {

    private static final String TAG = FocusView.class.getSimpleName();
    private static final String SHADOW_COLOR = "#77000000";
    private static final long DURATION = 500;
    public static final String KEY_HAS_SHOW = "has_show";
    private Paint paint;
    private float locX;
    private float locY;
    private Bitmap bitmap;
    private View targetView;
    private SharedPreferences sp;
    private float radius;

    public FocusView(Context context) {
        super(context);
        init();
    }

    public FocusView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FocusView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setFocusable(true);
        setFocusableInTouchMode(true);
        requestFocus();
        setOnTouchListener(this);
        paint = new Paint();
        paint.setAntiAlias(true);
        sp = getContext().getSharedPreferences(FocusView.class.getName(), Context.MODE_PRIVATE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int cs = canvas.saveLayer(0, 0, getWidth(), getHeight(), paint, Canvas.ALL_SAVE_FLAG);
        paint.setColor(Color.parseColor(SHADOW_COLOR));
        canvas.drawRect(0, 0, getWidth(), getHeight(), paint);

        Xfermode xfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_OUT);
        paint.setXfermode(xfermode);

        paint.setColor(Color.RED);
        canvas.drawCircle(locX, locY, radius, paint);
        paint.setXfermode(null);
        canvas.restoreToCount(cs);

        if (bitmap != null && targetView != null) {
            canvas.drawBitmap(bitmap, getLoc()[0] + targetView.getWidth(), getLoc()[1] - getStatusBarHeight(getContext()) - getActionBarHeight() - bitmap.getHeight(), paint);
        }
    }

    /**
     * 设置目标控件
     *
     * @param targetView    目标控件
     * @param landResId     横屏模式下的描述图片资源id
     * @param portraitResid 竖屏模式下的描述图片资源id
     */
    public void setTargetView(final View targetView, int landResId, int portraitResid) {
        boolean hasShow = sp.getBoolean(KEY_HAS_SHOW, false);
        if (hasShow) {
            setVisibility(View.GONE);
            return;
        }

        this.targetView = targetView;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            bitmap = BitmapFactory.decodeResource(getResources(), landResId);
        } else {
            bitmap = BitmapFactory.decodeResource(getResources(), portraitResid);
        }
        targetView.getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (targetView != null) {
            targetView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
        }
    }

    @Override
    public void onGlobalLayout() {
        radius = (float) Math.sqrt((targetView.getWidth() / 2) * (targetView.getWidth() / 2) + (targetView.getHeight() / 2) * (targetView.getHeight() / 2));
        locX = getLoc()[0] + targetView.getWidth() / 2;
        locY = getLoc()[1] + targetView.getHeight() / 2 - getStatusBarHeight(getContext()) - getActionBarHeight();
        postInvalidate();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && getVisibility() == View.VISIBLE) {
            dismiss();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private int getActionBarHeight() {
        int h = 0;
        ActionBar supportActionBar = ((AppCompatActivity) getContext()).getSupportActionBar();
        if (supportActionBar != null) {
            h = supportActionBar.getHeight();
        }
        return h;
    }

    private int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private int[] getLoc() {
        int[] loc = new int[2];
        targetView.getLocationInWindow(loc);
        return loc;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (MotionEvent.ACTION_DOWN == event.getAction()) {
            dismiss();
        }
        return true;
    }

    private void dismiss() {
        animate().setDuration(DURATION).alpha(0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                setVisibility(View.GONE);
                sp.edit().putBoolean(KEY_HAS_SHOW, true).commit();
            }
        }).start();
    }
}
