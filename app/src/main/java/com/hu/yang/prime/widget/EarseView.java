package com.hu.yang.prime.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import static android.content.ContentValues.TAG;

/**
 * Created by yanghu on 2017/8/24.
 */

public class EarseView extends View {

    private float downX;
    private float downY;
    private float disX;
    private float disY;
    private float mCircleLeft;
    private float mCircleTop;
    private boolean flag;
    private float moveX;
    private float moveY;
    private boolean clearFlag;

    public EarseView(Context context) {
        super(context);
        init();
    }

    public EarseView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EarseView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (moveX == 0 && moveY == 0) {
            moveX = getWidth() / 2;
            moveY = getHeight() / 2;
        }

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        paint.setDither(true);//设定是否使用图像抖动处理，会使绘制出来的图片颜色更加平滑和饱满，图像更加清晰
//        paint.setFilterBitmap(true);//加快显示速度，本设置项依赖于dither和xfermode的设置

        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        bitmap.eraseColor(Color.BLACK);
        bitmap.eraseColor(Color.parseColor("#77000000"));
        PorterDuffXfermode xfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT);
        if(clearFlag){
//            xfermode = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);
        }
//        canvas.drawRect(280,0,1500,500,paint);
//        PorterDuffXfermode xfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
////        paint.setXfermode(xfermode);
//        Bitmap bitmap = Bitmap.createBitmap(getResources().getDisplayMetrics().widthPixels, getResources().getDisplayMetrics().heightPixels, Bitmap.Config.ARGB_8888);
//        bitmap.eraseColor(Color.parseColor("#aa000000"));
//        canvas.drawBitmap(bitmap,new Matrix(),paint);


        int sc = canvas.saveLayer(0, 0, getResources().getDisplayMetrics().widthPixels, getResources().getDisplayMetrics().heightPixels, paint, Canvas.ALL_SAVE_FLAG);
        Bitmap dst = makeCircle();
        mCircleLeft = moveX - 150;
        mCircleTop = moveY - 150;
        canvas.drawBitmap(dst, mCircleLeft, mCircleTop, paint);
//        canvas.drawRect(300+disX,300+disY,800+disX,800+disY,paint);
        paint.setXfermode(xfermode);
        canvas.drawBitmap(bitmap, 0, 0, paint);
//        paint.setXfermode(null);
//        canvas.restoreToCount(sc);

        Log.i(TAG, "onDraw: -----");
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {


            case MotionEvent.ACTION_DOWN:
//                clearFlag = true;
//                postInvalidate();
                downX = event.getX();
                downY = event.getY();

                if ((downX > mCircleLeft && downX < mCircleLeft + 300) && (downY > mCircleTop && downY < mCircleTop + 300)) {

                    flag = true;
                } else {
                    flag = false;
                }

                break;
            case MotionEvent.ACTION_MOVE:
                if (!flag) {
                    return true;
                }

                moveX = event.getX();
                disX = moveX - downX;
                moveY = event.getY();
                disY = moveY - downY;
                postInvalidate();

                Log.i(TAG, "onTouchEvent: disX: " + disX + "  disY: " + disY);
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }

    private Bitmap makeCircle() {
        Bitmap bitmap = Bitmap.createBitmap(300, 300, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        paint.setColor(Color.TRANSPARENT);
//        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
        int radius = 300 / 2;
        canvas.drawCircle(300 / 2, 300 / 2, radius, paint);
        return bitmap;
    }
}
