package com.hu.yang.prime.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewConfiguration;

import com.foxit.gsdk.PDFException;
import com.foxit.gsdk.pdf.DefaultAppearance;
import com.foxit.gsdk.pdf.FontManager;
import com.foxit.gsdk.pdf.PDFPage;
import com.foxit.gsdk.pdf.PDFTextPage;
import com.foxit.gsdk.pdf.PDFTextSearch;
import com.foxit.gsdk.pdf.PDFTextSelection;
import com.foxit.gsdk.pdf.Progress;
import com.foxit.gsdk.pdf.RenderContext;
import com.foxit.gsdk.pdf.Renderer;
import com.foxit.gsdk.pdf.annots.Annot;
import com.foxit.gsdk.pdf.annots.FreeText;
import com.foxit.gsdk.pdf.annots.Highlight;
import com.foxit.gsdk.pdf.annots.UnderLine;
import com.foxit.gsdk.psi.PSI;
import com.hu.yang.prime.util.PDFUtil;
import com.hu.yang.prime.util.ScreenUtil;

import java.util.ArrayList;

/**
 * 解析pdf文件的View
 *
 * @author Yang
 * @time 2016/12/19 11:23
 */

public class PDFView extends View implements View.OnTouchListener, ScaleGestureDetector.OnScaleGestureListener {

    private static final String TAG = "PDFView";
    private static final int INCREMENT = 65;
    public int pdfHeight;
    public int pdfWidth;
    public float moveX;
    public float moveY;
    public static float mScale = 1.0f;
    private Bitmap bitmap;
    private ScaleGestureDetector scaleGestureDetector;
    private PDFPage page;
    private int mLastPointerCount;
    private boolean isCanDrag;
    private float mLastCenterX;
    private float mLastCenterY;
    //移动的临界值
    private int mTouchSlop;
    private PSI psi;
    private RectF psiRectF;
    private RectF rectF;
    private Annot.QuadpointsF[] quadPoints;
    private Annot annot;
    private Highlight highlight;
    private UnderLine underLine;
    private float scaleBigger = 1.2f;
    private float scaleSmaller = 0.8f;
    private OnDissmissListener listener;
    private OnAnnotAddedListener annotAddedListener;
    private boolean isRenderAnnot = false;
    private FreeText freeText;
    private OnWordTouchLisenter wordTouchLisenter;
    private boolean isValid;
    private float downX;
    //搜索到的原始高亮集合
    private ArrayList<RectF> highLightRectList = new ArrayList<RectF>();
    //经过转换的页面实际高亮集合
    private ArrayList<RectF> highLightRectListConvert = new ArrayList<RectF>();
    private int numIndex;
    private OnHighLightPageChangeListener onHighLightPageChangeListener;
    private int num;
    private OnScaleChangeListener onScaleChangeListener;
    private OnArrowVisibleChangedListener onArrowVisibleChangedListener;


    public PDFView(Context context) {
        this(context, null);
    }

    public PDFView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PDFView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void zoomOut() {
        if (mScale * scaleBigger < PDFUtil.MAX_SCALE) {
            mScale *= scaleBigger;
        } else {
            mScale = PDFUtil.MAX_SCALE;
        }
        renderPage2Bitmap(this);
        postInvalidate();
        checkBorder();
    }

    public void zoomIn() {
        if (mScale * scaleSmaller > PDFUtil.INIT_SCALE) {
            mScale *= scaleSmaller;
        } else {
            mScale = PDFUtil.INIT_SCALE;
        }
        renderPage2Bitmap(this);
        postInvalidate();

        checkBorder();
    }


    /**
     * 检测页面处于那一个边界：top bottom left right
     */
    private void checkBorder() {
        if (onArrowVisibleChangedListener == null) {
            return;
        }

        if (mScale == PDFUtil.INIT_SCALE) {
            onArrowVisibleChangedListener.onArrowTopVisibleChanged(false);
            onArrowVisibleChangedListener.onArrowBottomVisibleChanged(false);
        } else {
            if (getDiffY() + Math.abs(moveY) == 0) {

                if (moveY > 0) {
                    onArrowVisibleChangedListener.onArrowTopVisibleChanged(false);
                } else {
                    onArrowVisibleChangedListener.onArrowBottomVisibleChanged(false);
                }
            } else {
                onArrowVisibleChangedListener.onArrowTopVisibleChanged(true);
                onArrowVisibleChangedListener.onArrowBottomVisibleChanged(true);
            }


            if (getDisplayWidth() > ScreenUtil.getScreenWidth2()) {
                if (getDiffX() + Math.abs(moveX) == 0) {
                    if (moveX > 0) {
                        onArrowVisibleChangedListener.onArrowLeftVisibleChanged(false);
                    } else {
                        onArrowVisibleChangedListener.onArrowRightVisibleChanged(false);
                    }
                } else {
                    onArrowVisibleChangedListener.onArrowLeftVisibleChanged(true);
                    onArrowVisibleChangedListener.onArrowRightVisibleChanged(true);
                }
            } else {
                onArrowVisibleChangedListener.onArrowLeftVisibleChanged(false);
                onArrowVisibleChangedListener.onArrowRightVisibleChanged(false);
            }
        }
    }

    public void checkBorderWhenOrientationChanged() {
        if (onArrowVisibleChangedListener == null) {
            return;
        }

        if (mScale == PDFUtil.INIT_SCALE) {
            onArrowVisibleChangedListener.onArrowTopVisibleChanged(false);
            onArrowVisibleChangedListener.onArrowBottomVisibleChanged(false);
            onArrowVisibleChangedListener.onArrowLeftVisibleChanged(false);
            onArrowVisibleChangedListener.onArrowRightVisibleChanged(false);
        } else {

            onArrowVisibleChangedListener.onArrowTopVisibleChanged(true);
            onArrowVisibleChangedListener.onArrowBottomVisibleChanged(true);
            if (getDisplayWidth() > ScreenUtil.getScreenWidth2()) {
                onArrowVisibleChangedListener.onArrowLeftVisibleChanged(true);
                onArrowVisibleChangedListener.onArrowRightVisibleChanged(true);
            } else {
                onArrowVisibleChangedListener.onArrowLeftVisibleChanged(false);
                onArrowVisibleChangedListener.onArrowRightVisibleChanged(false);
            }
        }
    }

    public boolean isAnnotsEmpty() throws PDFException {
        if (page == null) {
            return true;
        }
        int i = page.countAnnots(Annot.TYPE_PSI)
                + page.countAnnots(Annot.TYPE_FREETEXT)
                + page.countAnnots(Annot.TYPE_UNDERLINE)
                + page.countAnnots(Annot.TYPE_HIGHLIGHT);
//        Log.i(TAG, "isAnnotsEmpty: " + i);
        return i == 0;
    }

    public void saveWord2Doc(float x, float y, String str) {
        Matrix matrix = getPageMatrix();
        float[] srcPoint = new float[]{x, y};
        float[] pagePoint = new float[2];
        matrix.mapPoints(pagePoint, srcPoint);
        RectF rectf = new RectF();

        rectf.left = pagePoint[0] - 1;
        rectf.right = pagePoint[0] + 1;
        rectf.top = pagePoint[1] + 1;
        rectf.bottom = pagePoint[1] - 1;

        try {
            freeText = (FreeText) page.addAnnot(rectf, Annot.TYPE_FREETEXT, null, -1);
            if (!"".equals(str) && annotAddedListener != null) {
                annotAddedListener.onAnnotAdded(freeText);
            }
        } catch (PDFException e) {
            e.printStackTrace();
        }


        try {
            freeText.setAlignment(0);
            freeText.setIntent(FreeText.INTENTNAME_FREETEXT_TYPEWRITER);
            freeText.setOpacity((float) 1.0);
            DefaultAppearance defaultAppearance = new DefaultAppearance();
            defaultAppearance.textColor = PDFUtil.COLOR_WORD;
            defaultAppearance.flags = DefaultAppearance.DA_TEXTCOLOR | DefaultAppearance.DA_FONT;
            defaultAppearance.font = FontManager.createStandard(FontManager.STDFONT_COURIER);
            defaultAppearance.fontSize = 6 * PDFUtil.SIZE_WORD;
            defaultAppearance.textMatrix = null;
            freeText.setDefaultAppearance(defaultAppearance);
            freeText.resetAppearance();
        } catch (PDFException e) {
            e.printStackTrace();
        }


        try {
            freeText.setContents(str);
            freeText.resetAppearance();

        } catch (PDFException e) {
            e.printStackTrace();
        }

        renderPage2Bitmap(this);
        postInvalidate();
    }

    public float getScale() {
        return mScale;
    }

    public void moveUp() {
        moveY += INCREMENT;

        renderPage2Bitmap(this);
        postInvalidate();
        checkBorder();
    }

    public void moveDown() {
        moveY -= INCREMENT;

        renderPage2Bitmap(this);
        postInvalidate();
        checkBorder();
    }

    private void move(float disX, float disY) {
        moveX += disX;
        moveY += disY;

        renderPage2Bitmap(this);
        postInvalidate();
        checkBorder();
    }

    public void moveLeft() {
        moveX += INCREMENT;

        renderPage2Bitmap(this);
        postInvalidate();

        checkBorder();
    }

    public void moveRight() {
        moveX -= INCREMENT;

        renderPage2Bitmap(this);
        postInvalidate();

        checkBorder();
    }

    public void search(String keyWords, int numIndex) {
        highLightRectListConvert.clear();
        if (page != null) {
            num = 0;
            this.numIndex = numIndex;
            PDFUtil.parsePage(page);
            try {
                PDFTextPage pdfTextPage = PDFTextPage.create(page);
                PDFTextSearch pdfTextSearch = pdfTextPage.startSearch(keyWords, 0, 0);
                while (pdfTextSearch.findNext()) {
                    num++;
                    if (num == numIndex) {
                        highLightRectList.clear();
                        highLightRectListConvert.clear();
                        PDFTextSelection selection = pdfTextSearch.getSelection();
                        for (int i = 0; i < selection.countPieces(); i++) {
                            RectF pieceRect = selection.getPieceRect(i);
                            highLightRectList.add(pieceRect);
                            highLightRectListConvert.add(convertRectF(pieceRect));
                        }

                        RectF rectF = highLightRectListConvert.get(0);
                        float disX = 0f;
                        float disY = 0f;
                        if (rectF.top < 0 || rectF.top > pdfHeight) {
                            disY = pdfHeight / 2 - rectF.top;
                        }

                        if (rectF.left < 0 || rectF.left > ScreenUtil.getScreenWidth2()) {
                            disX = ScreenUtil.getScreenWidth2() / 2 - rectF.left;
                        }

                        if (disX != 0 || disY != 0) {
                            move(disX, disY);
                        } else {
                            postInvalidate();
                        }
                    }
                }
                pdfTextSearch.release();
                pdfTextPage.release();
            } catch (PDFException e) {
                e.printStackTrace();
            }
        }
    }


    public void clearHighLightKeyWords() {
        if (highLightRectListConvert.size() != 0) {
            highLightRectList.clear();
            highLightRectListConvert.clear();
            postInvalidate();
        }
    }

    public void nextHighLight(String keyWords, Integer pageIndex) {
        numIndex++;
        if (numIndex > num) {
            numIndex = num;
            if (onHighLightPageChangeListener != null) {
                onHighLightPageChangeListener.nextPage(keyWords, pageIndex);
            }
        } else {
            search(keyWords, numIndex);
        }
    }

    public void preHighLight(String keyWords, Integer pageIndex) {
        numIndex--;
        if (numIndex < 1) {
            numIndex = 1;
            if (onHighLightPageChangeListener != null) {
                onHighLightPageChangeListener.prePage(keyWords, pageIndex);
            }
        } else {
            search(keyWords, numIndex);
        }
    }

    public int getKeyWordCount(String keyWords) {
        int num = 0;
        if (page != null) {
            PDFTextPage pdfTextPage = null;
            try {
                pdfTextPage = PDFTextPage.create(page);
                PDFTextSearch pdfTextSearch = pdfTextPage.startSearch(keyWords, 0, 0);
                while (pdfTextSearch.findNext()) {
                    num++;
                }
                pdfTextSearch.release();
                pdfTextPage.release();
            } catch (PDFException e) {
                e.printStackTrace();
            }
        }
        return num;
    }

    public int getNumIndex() {
        return numIndex;
    }

    public void initParam() {
        moveY = -getDiffY();
    }

    public void setOnHighLightPageChangeListener(OnHighLightPageChangeListener onHighLightPageChangeListener) {
        this.onHighLightPageChangeListener = onHighLightPageChangeListener;
    }

    private void init(Context context) {
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        setOnTouchListener(this);
        scaleGestureDetector = new ScaleGestureDetector(context, this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Matrix matrix = new Matrix();
        Paint paint = new Paint();
        canvas.drawColor(Color.parseColor("#dddddd"));
        if (bitmap == null) {
            paint.setColor(Color.WHITE);
            paint.setStyle(Paint.Style.FILL);
            int diffX = (ScreenUtil.getScreenWidth2() - PDFUtil.pdfInitWidth) / 2;
            canvas.drawRect(diffX, 0, diffX + PDFUtil.pdfInitWidth, pdfHeight, paint);
        } else {
            canvas.clipRect(getDiffX() + moveX, getDiffY() + moveY, getDiffX() + moveX + getDisplayWidth(), getDiffY() + moveY + getDisplayHeight());
            canvas.drawBitmap(bitmap, matrix, paint);
        }

        if (highLightRectListConvert.size() != 0) {
            paint.setColor(Color.parseColor("#88ffff00"));
            for (int i = 0; i < highLightRectListConvert.size(); i++) {
                canvas.drawRect(highLightRectListConvert.get(i), paint);
            }
        }

        if (this.rectF != null) {
            paint.setColor(Color.RED);
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawRect(this.rectF.left, this.rectF.top, this.rectF.right, this.rectF.bottom, paint);
        }
    }

    public void setOnDissmissListener(OnDissmissListener listener) {
        this.listener = listener;
    }

    public void delAnnot(Annot annot) {
        if (annot == null) {
            return;
        }
        try {
            page.removeAnnot(annot);
        } catch (PDFException e) {
            PDFUtil.log(e);
        }
        renderPage2Bitmap(this);
        postInvalidate();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (listener != null) {
            listener.onDissmiss();
        }
        if (PDFUtil.editMode == EditMode.MODE_NONE) {
            scaleGestureDetector.onTouchEvent(event);
        }

        getParent().requestDisallowInterceptTouchEvent(true);
        if (PDFUtil.editMode != EditMode.MODE_NONE) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                if (event.getX() >= getDiffX() && event.getX() <= getDiffX() + getDisplayWidth()) {
                    isValid = true;
                } else {
                    isValid = false;
                }
            }

            if (isValid) {
                switch (PDFUtil.editMode) {
                    case MODE_PSI:
                        return onPSITouch(event);
                    case MODE_UNDERLINE:
                        return onUnderLineTouch(event);
                    case MODE_HIGHLIGHT:
                        return onHighLightTouch(event);
                    case MODE_WORD:
                        return onWordTouch(event);
                }
            } else {
                return true;
            }

        }

        //记录多点触控的中心点
        float cX = 0;
        float cY = 0;

        //获取多点的个数
        int pointerCount = event.getPointerCount();

        //遍历每个触控点的位置，得出中心点的位置
        for (int i = 0; i < pointerCount; i++) {
            cX += event.getX(i);
            cY += event.getY(i);
        }
        cX /= pointerCount;
        cY /= pointerCount;

        //触控点的个数发生了改变，记录最后一次的中心点位置
        if (mLastPointerCount != pointerCount) {
            //需重新判断是否可以触发滑动
            isCanDrag = false;
            mLastCenterX = cX;
            mLastCenterY = cY;
        }
        mLastPointerCount = pointerCount;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                //计算偏移量
                float dx = cX - mLastCenterX;
                float dy = cY - mLastCenterY;

                //判断此次偏移值是否可以滑动屏幕
                if (!isCanDrag) {
                    isCanDrag = isMoveAction(dx, dy);
                }

                Log.i(TAG, "dx: " + dx);
                Log.i(TAG, "dy: " + dy);

                //获取移动过程中的图片信息
                //图片宽度小于控件宽度时，不允许做横向移动
                Log.i(TAG, "disWidth: " + getDisplayWidth());
                if (getDisplayWidth() <= getWidth()) {
                    dx = 0;
                    getParent().requestDisallowInterceptTouchEvent(false);
                } else {
                    float v1 = getDiffX() + Math.abs(moveX);
                    Log.i(TAG, "v1: " + v1);

                    Matrix pageMatrix = getMyMatrix();
                    RectF r = new RectF();
                    pageMatrix.mapRect(r);

                    float moveX = event.getX();
                    float disX = moveX - downX;
                    downX = moveX;

                    if ((r.left == 0 && disX > 0) || ((ScreenUtil.getScreenWidth2() + Math.abs(r.left) == getDisplayWidth()) && disX < 0)) {
                        getParent().requestDisallowInterceptTouchEvent(false);
                    } else {
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }
                }

                //同理
                if (getDisplayHeight() <= getHeight()) {
                    dy = 0;
                }

                moveX += dx;
                moveY += dy;

                renderPage2Bitmap(this);
                postInvalidate();
                checkBorder();

                //移动过程中不断更新前一次移动的中心点位置
                mLastCenterX = cX;
                mLastCenterY = cY;
                break;
            case MotionEvent.ACTION_UP:
                //触控手指个数为0
                mLastPointerCount = 0;
                break;
        }
        return true;
    }

    public void setOnWordTouchLisenter(OnWordTouchLisenter wordTouchLisenter) {
        this.wordTouchLisenter = wordTouchLisenter;
    }

    private boolean onWordTouch(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (wordTouchLisenter != null) {
                    wordTouchLisenter.onWordTouch(event);
                }
                break;
        }

        return true;
    }

    private boolean onHighLightTouch(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                rectF = new RectF();
                rectF.left = x;
                rectF.top = y;
                break;
            case MotionEvent.ACTION_MOVE:
                rectF.right = x;
                rectF.bottom = y;

                postInvalidate();
                break;
            case MotionEvent.ACTION_UP:
                RectF newRectF = convertAnnotRectF(rectF);
                ArrayList<RectF> pieceRectFs = PDFUtil.getPieceRectFs(page, newRectF);

                if (pieceRectFs.size() == 0 || (rectF.bottom == 0 && rectF.right == 0)) {
                    rectF.setEmpty();
                    postInvalidate();
                    return true;
                }

                try {
                    page.loadAnnots();
                    quadPoints = new Annot.QuadpointsF[pieceRectFs.size()];
                    for (int i = 0; i < pieceRectFs.size(); i++) {
                        RectF rectF = pieceRectFs.get(i);
                        quadPoints[i] = new Annot.QuadpointsF();
                        quadPoints[i].x1 = rectF.left;
                        quadPoints[i].y1 = rectF.top;
                        quadPoints[i].x2 = rectF.right;
                        quadPoints[i].y2 = rectF.top;
                        quadPoints[i].x3 = rectF.left;
                        quadPoints[i].y3 = rectF.bottom;
                        quadPoints[i].x4 = rectF.right;
                        quadPoints[i].y4 = rectF.bottom;
                    }


                    String annotType = Annot.TYPE_HIGHLIGHT;
                    annot = page.addAnnot(new RectF(0, 0, 0, 0), annotType, annotType, -1);
                    if (annotAddedListener != null) {
                        annotAddedListener.onAnnotAdded(annot);
                    }
                    highlight = (Highlight) annot;
                    highlight.setBorderColor(PDFUtil.COLOR_HIGHLIGHT);
                    highlight.setQuadPoints(quadPoints);


                    rectF.setEmpty();
                    isRenderAnnot = true;
                    renderPage2Bitmap(this);
                    postInvalidate();
                } catch (PDFException e) {
                    PDFUtil.log(e);
                }

                break;
        }

        return true;
    }

    private boolean onUnderLineTouch(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                rectF = new RectF();
                rectF.left = x;
                rectF.top = y;
                break;
            case MotionEvent.ACTION_MOVE:
                rectF.right = x;
                rectF.bottom = y;

                postInvalidate();
                break;
            case MotionEvent.ACTION_UP:
                RectF newRectF = convertAnnotRectF(rectF);
                ArrayList<RectF> pieceRectFs = PDFUtil.getPieceRectFs(page, newRectF);

                if (pieceRectFs.size() == 0 || (rectF.bottom == 0 && rectF.right == 0)) {
                    rectF.setEmpty();
                    postInvalidate();
                    return true;
                }

                try {
                    page.loadAnnots();
                    quadPoints = new Annot.QuadpointsF[pieceRectFs.size()];
                    for (int i = 0; i < pieceRectFs.size(); i++) {
                        RectF rectF = pieceRectFs.get(i);
                        quadPoints[i] = new Annot.QuadpointsF();
                        quadPoints[i].x1 = rectF.left;
                        quadPoints[i].y1 = rectF.top;
                        quadPoints[i].x2 = rectF.right;
                        quadPoints[i].y2 = rectF.top;
                        quadPoints[i].x3 = rectF.left;
                        quadPoints[i].y3 = rectF.bottom;
                        quadPoints[i].x4 = rectF.right;
                        quadPoints[i].y4 = rectF.bottom;
                    }


                    String annotType = Annot.TYPE_UNDERLINE;
                    annot = page.addAnnot(new RectF(0, 0, 0, 0), annotType, annotType, -1);
                    if (annotAddedListener != null) {
                        annotAddedListener.onAnnotAdded(annot);
                    }
                    underLine = (UnderLine) annot;
                    underLine.setBorderColor(PDFUtil.COLOR_UNDERlINE);
                    underLine.setQuadPoints(quadPoints);

                    rectF.setEmpty();
                    isRenderAnnot = true;
                    renderPage2Bitmap(this);
                    postInvalidate();
                } catch (PDFException e) {
                    PDFUtil.log(e);
                }
                break;
        }

        return true;
    }

    private boolean onPSITouch(MotionEvent event) {
        if (bitmap == null) {
            return true;
        }
        psiRectF = new RectF(getDiffX() + moveX, getDiffY() + moveY, getDisplayWidth() + getDiffX() + moveX, getDisplayHeight() + getDiffY() + moveY);
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                psi = null;
                addPSIPoint(x, y, PSI.PT_MOVETO);
                break;
            case MotionEvent.ACTION_MOVE:
                addPSIPoint(x, y, PSI.PT_LINETO);
                break;
            case MotionEvent.ACTION_UP:
                addPSIPoint(x, y, PSI.PT_ENDPATH);
                confirmPSI();
                break;
        }

        renderPSI(bitmap, psiRectF);
        postInvalidate();
        return true;
    }

    /**
     * 判断是否是一次合法的移动操作
     *
     * @param dx 水平的移动距离
     * @param dy 竖直的移动距离
     * @return
     */
    private boolean isMoveAction(float dx, float dy) {
        return Math.sqrt(dx * dx + dy * dy) > mTouchSlop;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
        postInvalidate();
    }

    public void setOnScaleChangeListener(OnScaleChangeListener onScaleChangeListener) {
        this.onScaleChangeListener = onScaleChangeListener;
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        zoomScale(detector.getScaleFactor());
        return true;
    }

    public void zoomScale(float scaleFactor) {
        if (mScale * scaleFactor < PDFUtil.INIT_SCALE) {
            mScale = PDFUtil.INIT_SCALE;
        } else if (mScale * scaleFactor > PDFUtil.MAX_SCALE) {
            mScale = PDFUtil.MAX_SCALE;
        } else {
            mScale *= scaleFactor;
        }
        renderPage2Bitmap(this);
        postInvalidate();
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        getParent().requestDisallowInterceptTouchEvent(true);
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {
        getParent().requestDisallowInterceptTouchEvent(false);
        if(onScaleChangeListener != null){
            onScaleChangeListener.onScaleChange(mScale);
        }
    }

    public PDFPage getPage() {
        return page;
    }

    public void setPage(PDFPage page) {
        this.page = page;
    }

    public float getDisplayWidth() {
        return pdfWidth * mScale;
    }

    public float getDisplayHeight() {
        return pdfHeight * mScale;
    }

    public float getDiffY() {
        return (pdfHeight - getDisplayHeight()) / 2;
    }

    public float getDiffX() {
        return (ScreenUtil.getScreenWidth2() - getDisplayWidth()) / 2;
    }

    public RectF convertAnnotRectF(RectF srcRectF) {
        RectF destRectF = new RectF();
        Matrix reverse = getPageMatrix();
        reverse.mapRect(destRectF, srcRectF);
        return destRectF;
    }

    public RectF convertRectF(RectF srcRectF) {
        RectF destRectF = new RectF();
        Matrix reverse = getMyMatrix();
        reverse.mapRect(destRectF, srcRectF);
        return destRectF;
    }

    public Matrix getMyMatrix() {
        Matrix matrix = null;
        try {
            matrix = page.getDisplayMatrix(0, 0, pdfWidth, pdfHeight, 0);
        } catch (PDFException e) {
            e.printStackTrace();
        }
        matrix.postScale(mScale, mScale);
        if (getDiffY() + Math.abs(moveY) > 0) {
            if (moveY > 0) {
                moveY = Math.abs(getDiffY());
            } else {
                moveY = -Math.abs(getDiffY());
            }
        }
        if (getDiffX() + Math.abs(moveX) > 0 && ScreenUtil.getScreenWidth2() < getDisplayWidth()) {
            if (moveX > 0) {
                moveX = Math.abs(getDiffX());
            } else {
                moveX = -Math.abs(getDiffX());
            }
        }

        if (ScreenUtil.getScreenWidth2() > getDisplayWidth()) {
            moveX = 0;
        }
        matrix.postTranslate(getDiffX() + moveX, getDiffY() + moveY);

        return matrix;
    }

    @NonNull
    private Matrix getPageMatrix() {
        Matrix matrix = null;
        try {
            matrix = page.getDisplayMatrix(0, 0, pdfWidth, pdfHeight, 0);
        } catch (PDFException e) {
            e.printStackTrace();
        }
        matrix.postScale(mScale, mScale);
        if (getDiffY() + Math.abs(moveY) > 0) {
            if (moveY > 0) {
                moveY = Math.abs(getDiffY());
            } else {
                moveY = -Math.abs(getDiffY());
            }
        }
        if (getDiffX() + Math.abs(moveX) > 0 && ScreenUtil.getScreenWidth2() < getDisplayWidth()) {
            if (moveX > 0) {
                moveX = Math.abs(getDiffX());
            } else {
                moveX = -Math.abs(getDiffX());
            }
        }

        if (ScreenUtil.getScreenWidth2() > getDisplayWidth()) {
            moveX = 0;
        }
        matrix.postTranslate(getDiffX() + moveX, getDiffY() + moveY);
        Matrix reverse = new Matrix();
        matrix.invert(reverse);
        return reverse;
    }

    public Bitmap renderPage2Bitmap(PDFView pv) {

        Bitmap bmp = pv.getBitmap();
        PDFPage page = pv.getPage();
        if (page == null) {
            return null;
        }
        try {
            if (bmp == null) {
                Bitmap.Config conf = Bitmap.Config.ARGB_8888;
                float pageWidth = page.getSize().getWidth();
                float pageHeight = page.getSize().getHeight();
                pdfWidth = (int) (pageWidth * pdfHeight / pageHeight);

                bmp = Bitmap.createBitmap(ScreenUtil.getScreenWidth2(), pdfHeight, conf);
                Log.i(TAG, "bmp: width " + bmp.getWidth() + "-----h :" + bmp.getHeight());
            }
            if (!isRenderAnnot) {
                bmp.eraseColor(Color.WHITE);
            }
            Renderer render = null;
            render = Renderer.create(bmp);
            Matrix matrix = page.getDisplayMatrix(0, 0, pdfWidth, pdfHeight, 0);
            matrix.postScale(mScale, mScale);
            if (getDiffY() + Math.abs(moveY) > 0) {
                if (moveY > 0) {
                    moveY = Math.abs(getDiffY());
                } else {
                    moveY = -Math.abs(getDiffY());
                }
            }
            if (getDiffX() + Math.abs(moveX) > 0 && ScreenUtil.getScreenWidth2() < getDisplayWidth()) {
                if (moveX > 0) {
                    moveX = Math.abs(getDiffX());
                } else {
                    moveX = -Math.abs(getDiffX());
                }
            }

            if (ScreenUtil.getScreenWidth2() > getDisplayWidth()) {
                moveX = 0;
            }
            matrix.postTranslate(getDiffX() + moveX, getDiffY() + moveY);
            RenderContext renderContext = null;
            renderContext = RenderContext.create();
            renderContext.setFlags(RenderContext.FLAG_ANNOT);
            renderContext.setMatrix(matrix);

            //matrix以变动，更新
            highLightRectListConvert.clear();
            for (RectF rectF : highLightRectList) {
                highLightRectListConvert.add(convertRectF(rectF));
            }

            Progress renderProgress = null;
            if (isRenderAnnot) {
                int countAnnots = page.countAnnots(null);
                Annot[] annots = new Annot[1];
                annots[0] = page.getAnnot(null, countAnnots - 1);
                renderProgress = page.startRenderAnnots(renderContext, render, annots);
                isRenderAnnot = false;
            } else {
                renderProgress = page.startRender(renderContext, render, PDFPage.RENDERFLAG_NORMAL);
            }
            if (renderProgress != null) {
                int r = Progress.TOBECONTINUED;
                while (r == Progress.TOBECONTINUED) {
                    r = renderProgress.continueProgress(30);
                }
            }
            renderProgress.release();
            renderContext.release();
            render.release();
            return bmp;
        } catch (PDFException e) {
            e.printStackTrace();
            PDFUtil.log(e);
            return null;
        }
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        renderPage2Bitmap(this);
        postInvalidate();
    }

    public PSI getPSI() {
        try {
            if (psi == null) {
                psi = PSI.create(false);
                psi.initCanvas(ScreenUtil.getScreenWidth2(), pdfHeight);
                psi.setInkColor(PDFUtil.COLOR_PSI);
                psi.setInkDiameter((int) (PDFUtil.SIZE_PSI * mScale));
            }
            return psi;
        } catch (PDFException e) {
            e.printStackTrace();
            PDFUtil.log(e);
            return null;
        }
    }

    public void renderPSI(Bitmap bmp, RectF rectF) {
        try {
            Renderer render = Renderer.create(bmp);
            getPSI().render(render, new Point((int) rectF.left, (int) rectF.top), (int) rectF.width(), (int) rectF.height(), new PointF(rectF.left, rectF.top));
            render.release();
        } catch (PDFException e) {
            e.printStackTrace();
            PDFUtil.log(e);
        }
    }

    public void releasePSI() {
        if (psi != null) {
            try {
                psi.release();
                psi = null;
            } catch (PDFException e) {
                PDFUtil.log(e);
            }
        }
    }

    public void addPSIPoint(float x, float y, int flag) {
        PointF pointF = new PointF();
        pointF.set(x, y);
        try {
            getPSI().setInkColor(PDFUtil.COLOR_PSI);
            getPSI().setInkDiameter((int) (PDFUtil.SIZE_PSI * mScale));
            getPSI().addPoint(pointF, 1.0f, flag);
        } catch (PDFException e) {
            e.printStackTrace();
            PDFUtil.log(e);
        }
    }

    public void confirmPSI() {
        if (psi == null) {
            return;
        }

        try {
            RectF contentsRect = getPSI().getContentsRect();
            RectF annotRect = convertAnnotRectF(contentsRect);

            Annot annot = getPSI().convertToPDFAnnot(psiRectF, page, annotRect);
            Log.i(TAG, "annot: " + annot);
            if (annot != null && annotAddedListener != null) {
                annotAddedListener.onAnnotAdded(annot);
                releasePSI();
            }
        } catch (PDFException e) {
            PDFUtil.log(e);
        }
    }

    public void setOnAnnotAddedListener(OnAnnotAddedListener annotAddedListener) {
        this.annotAddedListener = annotAddedListener;
    }

    public void setOnArrowVisibleChangedListener(OnArrowVisibleChangedListener onArrowVisibleChangedListener) {
        this.onArrowVisibleChangedListener = onArrowVisibleChangedListener;
    }

    public void updateView() {
        if (moveY != -getDiffY()) {
            moveY = -getDiffY();
        }
        renderPage2Bitmap(this);
        postInvalidate();
    }

    public enum EditMode {
        MODE_NONE, MODE_PSI, MODE_WORD, MODE_HIGHLIGHT, MODE_UNDERLINE;
    }

    public interface OnArrowVisibleChangedListener {
        void onArrowTopVisibleChanged(boolean visible);

        void onArrowBottomVisibleChanged(boolean visible);

        void onArrowLeftVisibleChanged(boolean visible);

        void onArrowRightVisibleChanged(boolean visible);
    }

    public interface OnHighLightPageChangeListener {
        void prePage(String keyWords, Integer pageIndex);

        void nextPage(String keyWords, Integer pageIndex);
    }

    public interface OnDissmissListener {
        void onDissmiss();
    }

    public interface OnWordTouchLisenter {
        void onWordTouch(MotionEvent event);
    }

    public interface OnScaleChangeListener {
        void onScaleChange(float scale);
    }

    public interface OnAnnotAddedListener {
        void onAnnotAdded(Annot annot);
    }
}
