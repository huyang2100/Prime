package com.hu.yang.prime.widget;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Scroller;
import android.widget.ViewAnimator;

import com.foxit.gsdk.PDFException;
import com.foxit.gsdk.pdf.PDFDocument;
import com.foxit.gsdk.pdf.PDFPage;
import com.foxit.gsdk.pdf.PDFPath;
import com.foxit.gsdk.pdf.PDFTextPage;
import com.foxit.gsdk.pdf.PDFTextSelection;
import com.foxit.gsdk.pdf.Progress;
import com.foxit.gsdk.pdf.RenderContext;
import com.foxit.gsdk.pdf.Renderer;
import com.foxit.gsdk.pdf.annots.Annot;
import com.foxit.gsdk.pdf.annots.Highlight;
import com.foxit.gsdk.pdf.annots.Ink;
import com.foxit.gsdk.pdf.annots.UnderLine;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by yanghu on 2017/9/15.
 */

public class HYPDFView extends View implements ViewTreeObserver.OnGlobalLayoutListener, ScaleGestureDetector.OnScaleGestureListener {
    private static final String TAG = HYPDFView.class.getSimpleName();
    private static final int COLOR_BACKGROUD = Color.WHITE;
    private static final float MAX_SCALE = 5f;
    private static final float MIN_SCALE = 1f;
    private static float MID_SCALE = MAX_SCALE * 0.55f;
    private static final long COLOR_INK = 0xfffc615d;
    private static final float BORDER_WIDTH_INK = 5;
    private static final long COLOR_UNDER_LINE = 0xff34c749;
    private static final long COLOR_HIGHT_LIGHT = 0xffffe21d;
    private Bitmap bitmap;
    private Paint paint;
    private Matrix displayMatrix;
    private PDFPage page;
    private int widgetWidth;
    private int widgetHeight;
    private float pageWidth;
    private float pageHeight;
    private AsyncTask<Void, Void, Void> task;
    private int lastVisibleBottom;
    public static ShowMode showMode = ShowMode.ViewPager;
    private int position;
    private ScaleGestureDetector scaleGestureDetector;
    private float[] displayMatrixValues;
    private Matrix canvasMatrix;
    private boolean isDraging;
    private Bitmap initBitmap;
    private GestureDetector gestureDetector;
    private PointF lastPointF;
    private boolean isScaling;
    private Scroller scroller;
    private float startX;
    private float startY;
    private boolean isFiling;
    private long startTime;
    public static boolean isEdited;
    public static EditMode editMode = EditMode.INK;
    private boolean isInValidInkEvent;
    private PDFPath pdfPath;
    private Ink ink;
    private Annot.Border border;
    private RectF rectF;
    private ArrayList<RectF> pieceRectFList;
    private UnderLine underLine;
    private Highlight hightLight;
    private OnFreeTextEditModeLisenter onFreeTextEditLisenter;

    public void setBitmap(Bitmap bitmapCache) {
        this.bitmap = bitmapCache;
        postInvalidate();
    }

    public enum EditMode {
        INK, UNDERLINE, HIGHTLIGHT, FREETEXT
    }

    public enum ShowMode {
        ViewPager, ListView
    }

    public HYPDFView(Context context) {
        super(context);
        init();
    }

    public HYPDFView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HYPDFView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public interface OnFreeTextEditModeLisenter {
        void onFreeTextEditModeLisenter(MotionEvent event, float width, RectF matrixRectF);
    }

    public void setOnFreeTextEditLisenter(OnFreeTextEditModeLisenter onFreeTextEditLisenter) {
        this.onFreeTextEditLisenter = onFreeTextEditLisenter;
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        lastVisibleBottom = getVisibleBottom();
        scaleGestureDetector = new ScaleGestureDetector(getContext(), this);
        gestureDetector = new GestureDetector(getContext(), new GestureListener());
        displayMatrixValues = new float[9];
        canvasMatrix = new Matrix();
        lastPointF = new PointF();
        scroller = new Scroller(getContext());
        border = new Annot.Border();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (bitmap == null) {
            paint.setColor(Color.WHITE);
            canvas.drawRect(0, 0, widgetWidth, widgetHeight, paint);
        } else {
            if (showMode == ShowMode.ListView) {
                canvas.drawBitmap(bitmap, getMatrix(), paint);
            } else {
                if (bitmap != null && initBitmap != null && displayMatrix != null) {
                    canvas.clipRect(getMatrixRectF());
                    canvas.drawBitmap(initBitmap, canvasMatrix, paint);
                    if (isDraging || isScaling || isFiling) {
                        canvas.drawBitmap(initBitmap, canvasMatrix, paint);
                    } else {
                        canvas.drawBitmap(bitmap, getMatrix(), paint);
                    }
                }
            }
        }
    }

    class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            startX = getMatrixRectF().left;
            startY = getMatrixRectF().top;
            int minX, maxX, minY, maxY;
            if (getMatrixRectF().width() > widgetWidth) {
                minX = (int) (widgetWidth - getMatrixRectF().width());
                maxX = 0;
            } else {
                minX = maxX = (int) startX;
            }

            if (getMatrixRectF().height() > widgetHeight) {
                minY = (int) (widgetHeight - getMatrixRectF().height());
                maxY = 0;
            } else {
                minY = maxY = (int) startY;
            }

            isFiling = true;
            scroller.fling((int) startX, (int) startY, (int) velocityX, (int) velocityY, minX, maxX, minY, maxY);
            return true;
        }

        @Override
        public boolean onDoubleTap(final MotionEvent e) {
            float curScale = getCurScale();
            final float targetScale;

            if (widgetWidth > widgetHeight) {
                MID_SCALE = widgetWidth / (pageWidth * (widgetHeight / pageHeight));
            } else {
                MID_SCALE = MAX_SCALE * 0.55f;
            }

            BigDecimal bigDecimal = new BigDecimal(curScale);
            curScale = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
            Log.i(TAG, "onDoubleTap: curscale: " + curScale + " midscale " + MID_SCALE);
            if (curScale < MID_SCALE) {
                targetScale = MID_SCALE;
            } else {
                targetScale = MIN_SCALE;
            }

            ValueAnimator animator = ValueAnimator.ofFloat(curScale, targetScale);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float animatedValue = (float) animation.getAnimatedValue();
                    Log.i(TAG, "onAnimationUpdate: " + animatedValue);
                    scale(animatedValue / getCurScale(), e.getX(), e.getY());
                    if (animatedValue == targetScale) {
                        renderBitmap();
                    }
                }
            });
            animator.start();
            return true;
        }
    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            int currX = scroller.getCurrX();
            int currY = scroller.getCurrY();
            move(currX - startX, currY - startY);
            startX = currX;
            startY = currY;
            postInvalidate();

            if (scroller.isFinished()) {
                isFiling = false;
                isDraging = false;
                renderBitmap();
            } else {
                isFiling = true;
            }
        } else {
            isFiling = false;
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (getAlpha() == 1 && displayMatrix != null) {
            if (isEdited) {
                if (getParent() != null) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                onEditEvent(event);
            } else {
                if (showMode == ShowMode.ViewPager) {
                    onNoEditEvent(event);
                }
            }
        }
        return true;
    }

    private boolean onEditEvent(MotionEvent event) {
        switch (editMode) {
            case INK:
                onInkEvent(event);
                break;
            case UNDERLINE:
                onUnderLineEvent(event);
                break;
            case HIGHTLIGHT:
                onHighLightEvent(event);
                break;
            case FREETEXT:
                onFreeTextEvent(event);
                break;
        }
        return false;
    }

    private void onFreeTextEvent(MotionEvent event) {
        RectF matrixRectF = getMatrixRectF();
        float x = event.getX();
        float y = event.getY();

        if (x < matrixRectF.left || x > matrixRectF.right || y < matrixRectF.top || y > matrixRectF.bottom) {
            return;
        }

        if (onFreeTextEditLisenter != null) {
            float width;
            if (matrixRectF.left > 0) {
                width = matrixRectF.right - event.getX();
            } else {
                width = (matrixRectF.right - event.getX()) + (matrixRectF.right - widgetWidth);
            }
            onFreeTextEditLisenter.onFreeTextEditModeLisenter(event, width, matrixRectF);
        }
    }

    private void onHighLightEvent(MotionEvent event) {
        PDFTextSelection pdfTextSelection = null;
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (rectF == null) {
                    rectF = new RectF();
                }
                rectF.left = x;
                rectF.top = y;
                break;
            case MotionEvent.ACTION_MOVE:
                rectF.right = x;
                rectF.bottom = y;


                if (pieceRectFList == null) {
                    pieceRectFList = new ArrayList<>();
                } else {
                    pieceRectFList.clear();
                }

                try {
                    PDFTextPage pdfTextPage = PDFTextPage.create(page);

                    PointF startF = getContentPoint(rectF.left, rectF.top);
                    int startCharIndex = pdfTextPage.getCharIndexAtPos(startF.x, startF.y, 100);
                    if (startCharIndex == -1) {
                        return;
                    }

                    PointF endF = getContentPoint(rectF.right, rectF.bottom);
                    int endCharIndex = pdfTextPage.getCharIndexAtPos(endF.x, endF.y, 100);
                    if (endCharIndex == -1) {
                        return;
                    }

                    int minIndex = Math.min(startCharIndex, endCharIndex);
                    int maxIndex = Math.max(startCharIndex, endCharIndex);

                    pdfTextSelection = pdfTextPage.selectByRange(minIndex, maxIndex - minIndex + 1);
                    if (pdfTextSelection == null) {
                        return;
                    }

                    for (int i = 0; i < pdfTextSelection.countPieces(); i++) {
                        pieceRectFList.add(pdfTextSelection.getPieceRect(i));
                    }

                    Annot.QuadpointsF[] quadPoints = new Annot.QuadpointsF[pieceRectFList.size()];
                    for (int i = 0; i < pieceRectFList.size(); i++) {
                        quadPoints[i] = new Annot.QuadpointsF();

                        RectF r = pieceRectFList.get(i);
                        quadPoints[i].x1 = r.left;
                        quadPoints[i].y1 = r.top;

                        quadPoints[i].x2 = r.right;
                        quadPoints[i].y2 = r.top;

                        quadPoints[i].x3 = r.left;
                        quadPoints[i].y3 = r.bottom;

                        quadPoints[i].x4 = r.right;
                        quadPoints[i].y4 = r.bottom;
                    }


                    if (hightLight != null) {
                        removeAnnot(hightLight);
                    }

                    hightLight = (Highlight) addAnnotNoRender(Annot.TYPE_HIGHLIGHT);
                    hightLight.setBorderColor(COLOR_HIGHT_LIGHT);
                    hightLight.setQuadPoints(quadPoints);
                    renderAnnot(hightLight);
                } catch (PDFException e) {
                    e.printStackTrace();
                }
                break;
            case MotionEvent.ACTION_UP:
                hightLight = null;
                if (pdfTextSelection != null) {
                    try {
                        pdfTextSelection.release();
                    } catch (PDFException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    private void onUnderLineEvent(MotionEvent event) {
        PDFTextSelection pdfTextSelection = null;
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (rectF == null) {
                    rectF = new RectF();
                }
                rectF.left = x;
                rectF.top = y;
                break;
            case MotionEvent.ACTION_MOVE:
                rectF.right = x;
                rectF.bottom = y;


                if (pieceRectFList == null) {
                    pieceRectFList = new ArrayList<>();
                } else {
                    pieceRectFList.clear();
                }

                try {
                    PDFTextPage pdfTextPage = PDFTextPage.create(page);

                    PointF startF = getContentPoint(rectF.left, rectF.top);
                    int startCharIndex = pdfTextPage.getCharIndexAtPos(startF.x, startF.y, 100);
                    if (startCharIndex == -1) {
                        return;
                    }

                    PointF endF = getContentPoint(rectF.right, rectF.bottom);
                    int endCharIndex = pdfTextPage.getCharIndexAtPos(endF.x, endF.y, 100);
                    if (endCharIndex == -1) {
                        return;
                    }

                    int minIndex = Math.min(startCharIndex, endCharIndex);
                    int maxIndex = Math.max(startCharIndex, endCharIndex);

                    pdfTextSelection = pdfTextPage.selectByRange(minIndex, maxIndex - minIndex + 1);
                    if (pdfTextSelection == null) {
                        return;
                    }

                    for (int i = 0; i < pdfTextSelection.countPieces(); i++) {
                        pieceRectFList.add(pdfTextSelection.getPieceRect(i));
                    }

                    Annot.QuadpointsF[] quadPoints = new Annot.QuadpointsF[pieceRectFList.size()];
                    for (int i = 0; i < pieceRectFList.size(); i++) {
                        quadPoints[i] = new Annot.QuadpointsF();

                        RectF r = pieceRectFList.get(i);
                        quadPoints[i].x1 = r.left;
                        quadPoints[i].y1 = r.top;

                        quadPoints[i].x2 = r.right;
                        quadPoints[i].y2 = r.top;

                        quadPoints[i].x3 = r.left;
                        quadPoints[i].y3 = r.bottom;

                        quadPoints[i].x4 = r.right;
                        quadPoints[i].y4 = r.bottom;
                    }


                    if (underLine != null) {
                        removeAnnot(underLine);
                    }

                    underLine = (UnderLine) addAnnotNoRender(Annot.TYPE_UNDERLINE);
                    underLine.setQuadPoints(quadPoints);
                    underLine.setBorderColor(COLOR_UNDER_LINE);
                    renderAnnot(underLine);
                } catch (PDFException e) {
                    e.printStackTrace();
                }
                break;
            case MotionEvent.ACTION_UP:
                underLine = null;
                if (pdfTextSelection != null) {
                    try {
                        pdfTextSelection.release();
                    } catch (PDFException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }


    private void onInkEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                RectF matrixRectF = getMatrixRectF();
                float x = event.getX();
                float y = event.getY();

                if (x < matrixRectF.left || x > matrixRectF.right || y < matrixRectF.top || y > matrixRectF.bottom) {
                    isInValidInkEvent = true;
                    return;
                } else {
                    isInValidInkEvent = false;
                }

                try {
                    if (pdfPath == null) {
                        pdfPath = PDFPath.create();
                    }
                    pdfPath.moveTo(getContentPoint(event.getX(), event.getY()));
                } catch (PDFException e) {
                    e.printStackTrace();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (isInValidInkEvent) {
                    return;
                }

                try {
                    pdfPath.lineTo(getContentPoint(event.getX(), event.getY()));
                    if (ink != null) {
                        page.removeAnnot(ink);
                    }
                    ink = (Ink) addAnnotNoRender(Annot.TYPE_INK);
                    ink.setBorderColor(COLOR_INK);
                    ink.setInkList(pdfPath);
                    border.setWidth(BORDER_WIDTH_INK);
                    ink.setBorder(border);
                    renderAnnot(ink);
                } catch (PDFException e) {
                    e.printStackTrace();
                }
                break;
            case MotionEvent.ACTION_UP:
                if (isInValidInkEvent) {
                    return;
                }

                ink = null;
                try {
                    pdfPath.clear();
                } catch (PDFException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void removeAnnot(Annot annot) {
        try {
            page.removeAnnot(annot);
        } catch (PDFException e) {
            e.printStackTrace();
        }
        renderPage();
    }

    private void renderPage() {
        try {
            RenderContext renderContext = RenderContext.create();
            renderContext.setMatrix(displayMatrix);
            renderContext.setFlags(RenderContext.FLAG_ANNOT);
            bitmap.eraseColor(COLOR_BACKGROUD);
            Renderer renderer = Renderer.create(bitmap);
            Progress progress = page.startRender(renderContext, renderer, PDFPage.RENDERFLAG_NORMAL);
            if (progress != null) {
                progress.continueProgress(0);
                progress.release();
            }

            progress.release();
            renderContext.release();
            renderer.release();
            postInvalidate();
        } catch (PDFException e) {
            e.printStackTrace();
        }
    }

    private void renderAnnot(Annot annot) {
        try {
            RenderContext renderContext = RenderContext.create();
            renderContext.setMatrix(displayMatrix);
            renderContext.setFlags(RenderContext.FLAG_ANNOT);
            Renderer renderer = Renderer.create(bitmap);
            Annot[] annots = new Annot[1];
            annots[0] = annot;
            Progress progress = page.startRenderAnnots(renderContext, renderer, annots);
            if (progress != null) {
                progress.continueProgress(0);
                progress.release();
            }
            renderer.release();
            renderContext.release();
            postInvalidate();
        } catch (PDFException e) {
            e.printStackTrace();
        }
    }

    private Annot addAnnotNoRender(String annotType) throws PDFException {
        page.loadAnnots();
        return page.addAnnot(new RectF(0, 0, 0, 0), annotType, annotType, -1);
    }

    private PointF getContentPoint(float x, float y) {
        float[] src = new float[]{x, y};
        float[] dst = new float[2];

        Matrix mtx = getContentMatrix();
        mtx.mapPoints(dst, src);

        return new PointF(dst[0], dst[1]);
    }

    @NonNull
    private Matrix getContentMatrix() {
        Matrix matrix = new Matrix();
        displayMatrix.invert(matrix);
        return matrix;
    }

    private boolean onNoEditEvent(MotionEvent event) {
        scaleGestureDetector.onTouchEvent(event);
        gestureDetector.onTouchEvent(event);
        Log.i(TAG, "onTouchEvent: " + event.getAction());
        if (isScaling) {
            return true;
        }

        PointF curPointF = new PointF(event.getX(), event.getY());
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!scroller.isFinished()) {
                    scroller.forceFinished(true);
                }
                lastPointF.set(curPointF);
                startTime = System.currentTimeMillis();
                isDraging = true;
                break;
            case MotionEvent.ACTION_MOVE:
                if (isDraging) {
                    float tranX = curPointF.x - lastPointF.x;
                    float tranY = curPointF.y - lastPointF.y;
                    move(tranX, tranY);
                    lastPointF.set(curPointF);
                }
                break;
            case MotionEvent.ACTION_UP:
                isDraging = false;
                if (!isFiling) {
                    isFiling = false;
                    if (System.currentTimeMillis() - startTime > 200) {
                        renderBitmap();
                    }
                }
                break;
        }
        return false;
    }

    private void move(float tranX, float tranY) {
        if (getMatrixRectF().height() > widgetHeight) {
            if (((int) getMatrixRectF().top == 0 && tranY > 0)) {
                tranY = 0;
            }

            if ((int) (getMatrixRectF().bottom - widgetHeight) == 0 && tranY < 0) {
                tranY = 0;
            }

            if (getMatrixRectF().top > 0 && tranY > 0) {
                tranY = -getMatrixRectF().top;
            }

            if (getMatrixRectF().bottom < widgetHeight && tranY < 0) {
                tranY = widgetHeight - getMatrixRectF().bottom;
            }

            displayMatrix.postTranslate(0, tranY);
            canvasMatrix.postTranslate(0, tranY);
            postInvalidate();
        }

        if (getMatrixRectF().width() > widgetWidth) {
            if ((int) (getMatrixRectF().left) == 0 && tranX > 0) {
                tranX = 0;
            }

            if ((int) (getMatrixRectF().right - widgetWidth) == 0 && tranX < 0) {
                tranX = 0;
            }

            if (getMatrixRectF().left > 0 && tranX > 0) {
                tranX = -getMatrixRectF().left;
            }

            if (getMatrixRectF().right < widgetWidth && tranX < 0) {
                tranX = widgetWidth - getMatrixRectF().right;
            }

            displayMatrix.postTranslate(tranX, 0);
            canvasMatrix.postTranslate(tranX, 0);
            postInvalidate();
        }
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        float curScale = getCurScale();
        float scaleFactor = detector.getScaleFactor();
        float scaleAfter = curScale * scaleFactor;

        if (scaleAfter <= MAX_SCALE && scaleAfter >= MIN_SCALE) {
            float focusX = detector.getFocusX();
            float focusY = detector.getFocusY();
            scale(scaleFactor, focusX, focusY);
        }
        return true;
    }

    private void scale(float scaleFactor, float focusX, float focusY) {
        isScaling = true;
        displayMatrix.postScale(scaleFactor, scaleFactor, focusX, focusY);
        canvasMatrix.postScale(scaleFactor, scaleFactor, focusX, focusY);

        if (getMatrixRectF().width() < widgetWidth) {
            float dx = (widgetWidth - getMatrixRectF().width()) / 2 - getMatrixRectF().left;
            displayMatrix.postTranslate(dx, 0);
            canvasMatrix.postTranslate(dx, 0);
        } else {
            if ((int) getMatrixRectF().left >= 0) {
                float dx = -getMatrixRectF().left;
                displayMatrix.postTranslate(dx, 0);
                canvasMatrix.postTranslate(dx, 0);
            }
            if (getMatrixRectF().right < widgetWidth) {
                float dx = widgetWidth - getMatrixRectF().right;
                displayMatrix.postTranslate(dx, 0);
                canvasMatrix.postTranslate(dx, 0);
            }
        }

        if (getMatrixRectF().height() < widgetHeight) {
            float dy = (widgetHeight - getMatrixRectF().height()) / 2 - getMatrixRectF().top;
            displayMatrix.postTranslate(0, dy);
            canvasMatrix.postTranslate(0, dy);
        } else {
            if ((int) getMatrixRectF().top >= 0) {
                float dy = -getMatrixRectF().top;
                displayMatrix.postTranslate(0, dy);
                canvasMatrix.postTranslate(0, dy);
            }
            if (getMatrixRectF().bottom < widgetHeight) {
                float dy = widgetHeight - getMatrixRectF().bottom;
                displayMatrix.postTranslate(0, dy);
                canvasMatrix.postTranslate(0, dy);
            }
        }
        postInvalidate();
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {
        renderBitmap();
    }

    private void renderBitmap() {
        new RenderBitmapTask().execute();
    }

    private float getCurScale() {
        canvasMatrix.getValues(displayMatrixValues);
        return displayMatrixValues[Matrix.MSCALE_X];
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        isScaling = true;
        return true;
    }


    private RectF getMatrixRectF() {
        RectF rectF = new RectF(0, 0, pageWidth, pageHeight);
        displayMatrix.mapRect(rectF);
        return rectF;
    }

    public boolean canScrollHorizontallyFroyo(int dx) {
        RectF matrixRectF = getMatrixRectF();

        if (matrixRectF.width() < widgetWidth) {
            return false;
        } else if (matrixRectF.right <= widgetWidth + 1 && dx < 0) {
            return false;
        } else if (matrixRectF.left >= -1 && dx > 0) {
            return false;
        }

        return true;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getViewTreeObserver().removeGlobalOnLayoutListener(this);
    }

    private int getVisibleBottom() {
        Rect rect = new Rect();
        getWindowVisibleDisplayFrame(rect);
        return rect.bottom;
    }

    @Override
    public void onGlobalLayout() {
        if (showMode == ShowMode.ViewPager) {
            widgetWidth = getWidth();
            widgetHeight = getHeight();
            int visibleBottom = getVisibleBottom();
            if (task == null || lastVisibleBottom != visibleBottom) {
                lastVisibleBottom = visibleBottom;
                bitmap = null;
                canvasMatrix.reset();
                task = new ParsePageTask().execute();
            }
        }
    }

    public void setWH(int width, int height) {
        widgetWidth = width;
        widgetHeight = height;
    }

    public void load(PDFDocument document, int position) throws PDFException {
        this.position = position;
        page = document.getPage(position);
        pageWidth = page.getSize().getWidth();
        pageHeight = page.getSize().getHeight();
        if (showMode == ShowMode.ListView) {
            bitmap = null;
            postInvalidate();
            task = new ParsePageTask().execute();
        }
    }

    class RenderBitmapTask extends AsyncTask<Void, Void, Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            isScaling = false;
            isDraging = false;
            isFiling = false;
            setAlpha(0f);
        }

        @Override
        protected Bitmap doInBackground(Void... avoid) {
            if (widgetWidth == 0 || widgetHeight == 0) {
                return null;
            }
            Bitmap bitmap = Bitmap.createBitmap(widgetWidth, widgetHeight, Bitmap.Config.ARGB_8888);
            bitmap.eraseColor(COLOR_BACKGROUD);
            try {
                Renderer renderer = Renderer.create(bitmap);
                RenderContext renderContext = RenderContext.create();
                renderContext.setMatrix(displayMatrix);
                renderContext.setFlags(RenderContext.FLAG_ANNOT);
                Progress progress = page.startRender(renderContext, renderer, PDFPage.RENDERFLAG_NORMAL);
                if (progress != null) {
                    progress.continueProgress(0);
                    progress.release();
                }
                renderContext.release();
                renderer.release();
            } catch (PDFException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(final Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (bitmap == null) {
                return;
            }


            animate().alpha(1).setDuration(350).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    if (HYPDFView.this.bitmap == null) {
                        HYPDFView.this.initBitmap = bitmap;
                    }
                    HYPDFView.this.bitmap = bitmap;
                    Integer tag = (Integer) getTag();
                    if (tag != null && tag == position) {
                        postInvalidate();
                    }
                }

                @Override
                public void onAnimationEnd(Animator animation) {

                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            }).start();
        }
    }

    class ParsePageTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... avoid) {
            try {
                if (!page.isParsed()) {
                    Progress progress = page.startParse(PDFPage.RENDERFLAG_NORMAL);
                    if (progress != null) {
                        progress.continueProgress(0);
                        progress.release();
                    }
                }
            } catch (PDFException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void avoid) {
            super.onPostExecute(avoid);
            float scale, tranX, tranY;

            if (showMode == ShowMode.ViewPager) {
                if (widgetWidth > widgetHeight) {
                    scale = widgetHeight * 1.0f / pageHeight;
                    tranX = (widgetWidth - pageWidth * scale) / 2;
                    tranY = 0;
                } else {
                    scale = widgetWidth * 1.0f / pageWidth;
                    tranX = 0;
                    tranY = (widgetHeight - pageHeight * scale) / 2;
                }
            } else {
                scale = widgetWidth * 1.0f / pageWidth;
                tranX = 0;
                tranY = 0;
            }

            try {
                displayMatrix = page.getDisplayMatrix(0, 0, (int) pageWidth, (int) pageHeight, PDFPage.ROTATION_0);
            } catch (PDFException e) {
                e.printStackTrace();
            }
            displayMatrix.postScale(scale, scale);
            displayMatrix.postTranslate(tranX, tranY);
            postInvalidate();
            renderBitmap();
        }
    }

}
