package com.hu.yang.prime.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by yanghu on 2017/9/1.
 */

public class ProgressView extends View {

    private static final String TAG = ProgressView.class.getSimpleName();
    private static final String COLOR_PROGRESS = "#4CAF50";
    private static final String COLOR_BG = "#FF888888";
    private Paint paint;
    private double progress;
    private RectF rectF;
    private PorterDuffXfermode xfermode;
    private OnLoadingStateListener onLoadingStateListener;
    private ProgressState state = ProgressState.START;

    public void setState(ProgressState state) {
        this.state = state;
    }

    public enum ProgressState {
        START, LOADING, PAUSE, COMPLETED
    }

    public ProgressView(Context context) {
        super(context);
        init();
    }

    public ProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        xfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (onLoadingStateListener != null) {

                    switch (state) {
                        case START:
                            state = ProgressState.LOADING;
                            onLoadingStateListener.onStart();
                            break;
                        case LOADING:
                            state = ProgressState.PAUSE;
                            onLoadingStateListener.onPause();
                            postInvalidate();
                            break;
                        case PAUSE:
                            state = ProgressState.LOADING;
                            onLoadingStateListener.onContinue();
                            postInvalidate();
                            break;
                        case COMPLETED:
                            onLoadingStateListener.onCompleted();
                            break;
                    }

                }
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (rectF == null) {
            rectF = new RectF(0, 0, getWidth(), getHeight());
        }
        int cs = canvas.saveLayer(rectF, paint, Canvas.ALL_SAVE_FLAG);
        paint.setColor(Color.parseColor(COLOR_PROGRESS));
        canvas.drawRoundRect(rectF, getHeight() / 2, getHeight() / 2, paint);
        paint.setXfermode(xfermode);
        paint.setColor(Color.parseColor(COLOR_BG));
        canvas.drawRect((float) (getWidth() * progress), 0, getWidth(), getHeight(), paint);
        paint.setColor(Color.WHITE);
        paint.setXfermode(null);
        paint.setTextSize(getHeight() / 2);
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        int txtHeight = (int) (fontMetrics.bottom - fontMetrics.top);
        canvas.drawText(getProgressTxt(), (getWidth() - paint.measureText(getProgressTxt())) / 2, (getHeight() - txtHeight) / 2 - fontMetrics.top, paint);
        canvas.restoreToCount(cs);
    }


    @NonNull
    private String getProgressTxt() {
        String str;
        switch (state) {
            case START:
                str = "开始";
                break;
            case PAUSE:
                str = "继续";
                break;
            case COMPLETED:
                str = "完成";
                break;
            case LOADING:
            default:
                int pro = (int) (progress * 100);
                if (pro == 100) {
                    str = "完成";
                    state = ProgressState.COMPLETED;
                    onLoadingStateListener.onCompleted();
                } else {
                    str = pro + "%";
                }

                break;
        }
        return str;
    }

    public interface OnLoadingStateListener {
        void onStart();

        void onCompleted();

        void onPause();

        void onContinue();
    }

    public void setOnLoadingStateListener(OnLoadingStateListener onLoadingStateListener) {
        this.onLoadingStateListener = onLoadingStateListener;
    }

    public void setProgress(double progress) {
        this.progress = progress;
        postInvalidate();
    }
}
