package com.hu.yang.prime.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Handler;
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

import com.foxit.gsdk.PDFException;
import com.foxit.gsdk.pdf.DefaultAppearance;
import com.foxit.gsdk.pdf.FontManager;
import com.foxit.gsdk.pdf.PDFDocument;
import com.foxit.gsdk.pdf.PDFPage;
import com.foxit.gsdk.pdf.PDFPath;
import com.foxit.gsdk.pdf.PDFTextPage;
import com.foxit.gsdk.pdf.PDFTextSelection;
import com.foxit.gsdk.pdf.Progress;
import com.foxit.gsdk.pdf.RenderContext;
import com.foxit.gsdk.pdf.Renderer;
import com.foxit.gsdk.pdf.annots.Annot;
import com.foxit.gsdk.pdf.annots.FreeText;
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
    private static int COLOR_BACKGROUD = Color.WHITE;
    private float maxScale = 6f;
    private float minScale = 1f;
    public float curScale = minScale;
    private float midScale = maxScale * 0.55f;
    public static ArrayList<RectF> hightLightRectFs;
    private final float SCALE_BIGGER = 1.25f;
    private final float SCALE_SMALLER = 0.75f;
    public static int COLOR_INK = 0xfffc615d;
    public static int BORDER_WIDTH_INK = 5;
    public static int SIZE_FREETEXT = 5;
    public static int COLOR_UNDER_LINE = 0xff34c749;
    public static int COLOR_HIGHT_LIGHT = 0xffffe21d;
    public static int COLOR_FREETEXT = 0x2bc3e9;
    private Bitmap bitmap;
    private Paint paint;
    private Matrix displayMatrix;
    private PDFPage page;
    private int widgetWidth;
    private int widgetHeight;
    private float pageWidth;
    private float pageHeight;
    private AsyncTask<Void, Void, Void> task;
    public static ShowMode showMode = ShowMode.ViewPager;
    private int position;
    private ScaleGestureDetector scaleGestureDetector;
    private float[] displayMatrixValues;
    //    private Matrix canvasMatrix;
    private boolean isDraging;
    private Bitmap initBitmap;
    private GestureDetector gestureDetector;
    private PointF lastPointF;
    private boolean isScaling;
    private Scroller scroller;
    private float startX;
    private float startY;
    private boolean isFiling;
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
    public static final String ANNOT_FILTER = Annot.TYPE_HIGHLIGHT + "," + Annot.TYPE_UNDERLINE + "," + Annot.TYPE_INK + "," + Annot.TYPE_FREETEXT + "," + Annot.TYPE_PSI;
    private OnAddAnnotLisenter onAddAnnotLisenter;
    private Paint hightLightPaint;
    private OnScaleListener scaleListener;

    public void setDisplayMatrix(Matrix displayMatrix) {
        this.displayMatrix = displayMatrix;
    }

    public void setPageWH(float width, float height) {
        pageWidth = width;
        pageHeight = height;
    }

    public void setPage(PDFPage page) {
        this.page = page;
    }

    public void update() {
        isScaling = false;
        isDraging = false;
        isFiling = false;
        postInvalidate();
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public interface OnScaleListener {
        void onScaleChange();
    }

    public void setOnScaleListener(OnScaleListener scaleListener) {
        this.scaleListener = scaleListener;
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

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        hightLightPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        hightLightPaint.setColor(Color.parseColor("#88ffff00"));
        scaleGestureDetector = new ScaleGestureDetector(getContext(), this);
        gestureDetector = new GestureDetector(getContext(), new GestureListener());
        displayMatrixValues = new float[9];
        lastPointF = new PointF();
        scroller = new Scroller(getContext());
        border = new Annot.Border();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.i(TAG, "onDraw: bitmap " + bitmap);
        if (bitmap == null) {
            paint.setColor(Color.WHITE);
            canvas.drawRect(0, 0, widgetWidth, widgetHeight, paint);
        } else {
            canvas.clipRect(getMatrixRectF());
            canvas.drawBitmap(bitmap, getMatrix(), paint);

            if (hightLightRectFs != null && hightLightRectFs.size() > 0) {
                for (int i = 0; i < hightLightRectFs.size(); i++) {
                    RectF srcRectF = hightLightRectFs.get(i);
                    RectF rect = convertRectF(srcRectF);
                    if (rect != null) {
                        canvas.drawRect(rect, hightLightPaint);
                    }
                }
            }
        }
    }

    public void clearHightLightRectF() {
        hightLightRectFs = null;
        postInvalidate();
    }


    public RectF convertRectF(RectF srcRectF) {
        if (displayMatrix == null) {
            return null;
        }

        RectF destRectF = new RectF();
        displayMatrix.mapRect(destRectF, srcRectF);
        return destRectF;
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

//            isFiling = true;
            scroller.fling((int) startX, (int) startY, (int) velocityX, (int) velocityY, minX, maxX, minY, maxY);
            return true;
        }

        @Override
        public boolean onDoubleTap(final MotionEvent e) {
            float curScale = getCurScale();
            final float targetScale;

            if (widgetWidth > widgetHeight) {
                midScale = widgetWidth / pageWidth * 1f;
            } else {
                midScale = maxScale * 0.55f;
            }

            BigDecimal bigDecimal = new BigDecimal(curScale);
            curScale = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();

            BigDecimal bigDecimal2 = new BigDecimal(midScale);
            midScale = bigDecimal2.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
            Log.i(TAG, "onDoubleTap: curscale: " + curScale + " midscale " + midScale);
            if (curScale < midScale) {
                targetScale = midScale;
            } else {
                targetScale = minScale;
            }

            ValueAnimator animator = ValueAnimator.ofFloat(curScale, targetScale);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float animatedValue = (float) animation.getAnimatedValue();
                    Log.i(TAG, "onAnimationUpdate: " + animatedValue);
                    scale(animatedValue / getCurScale(), e.getX(), e.getY());
                    if (animatedValue == targetScale) {
                        if (scaleListener != null) {
                            scaleListener.onScaleChange();
                        }
                    }
                }
            });
            animator.start();
            return true;
        }
    }

    private Handler handler = new Handler();

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            isFiling = true;
            int currX = scroller.getCurrX();
            int currY = scroller.getCurrY();
            move(currX - startX, currY - startY);
            startX = currX;
            startY = currY;
            postInvalidate();
            Log.i(TAG, "computeScroll: 0");

            if (scroller.isFinished()) {
//                renderBitmap();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        renderBitmap();
                    }
                }, 100);
                Log.i(TAG, "computeScroll: 1");
            } else {
                Log.i(TAG, "computeScroll: 2");
                isFiling = true;
            }
        } else {
            Log.i(TAG, "computeScroll: 3");
//            isFiling = false;
        }
        Log.i(TAG, "computeScroll: 4");
//        isFiling = false;
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
        if (onAddAnnotLisenter != null) {
            onAddAnnotLisenter.onAddAnnot();
        }

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

    public interface OnFreeTextEditModeLisenter {
        void onFreeTextEditModeLisenter(MotionEvent event, float width, RectF matrixRectF, HYPDFView hypdfView);
    }

    public void setOnFreeTextEditLisenter(OnFreeTextEditModeLisenter onFreeTextEditLisenter) {
        this.onFreeTextEditLisenter = onFreeTextEditLisenter;
    }

    public interface OnAddAnnotLisenter {
        void onAddAnnot();
    }

    public void setOnAddAnnotLisenter(OnAddAnnotLisenter onAddAnnotLisenter) {
        this.onAddAnnotLisenter = onAddAnnotLisenter;
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
            onFreeTextEditLisenter.onFreeTextEditModeLisenter(event, width, matrixRectF, this);
        }
    }

    public void saveFreeText(float x, float y, String freetext) {
        float[] dst = new float[2];
        getContentMatrix().mapPoints(dst, new float[]{x, y});

        RectF rectf = new RectF(dst[0] - 1, dst[1] + 1, dst[0] + 1, dst[1] - 1);

        try {
            page.loadAnnots();
            FreeText freeTextAnnot = (FreeText) page.addAnnot(rectf, Annot.TYPE_FREETEXT, null, -1);


            freeTextAnnot.setAlignment(0);
            freeTextAnnot.setIntent(FreeText.INTENTNAME_FREETEXT_TYPEWRITER);
            freeTextAnnot.setOpacity((float) 1.0);
            DefaultAppearance defaultAppearance = new DefaultAppearance();
            defaultAppearance.textColor = HYPDFView.COLOR_FREETEXT;
            defaultAppearance.flags = DefaultAppearance.DA_TEXTCOLOR | DefaultAppearance.DA_FONT;
            defaultAppearance.font = FontManager.createStandard(FontManager.STDFONT_COURIER);
            defaultAppearance.fontSize = HYPDFView.SIZE_FREETEXT * 1.5f * 4.8f;
            defaultAppearance.textMatrix = null;
            freeTextAnnot.setDefaultAppearance(defaultAppearance);
            freeTextAnnot.resetAppearance();
            freeTextAnnot.setContents(freetext);
            freeTextAnnot.resetAppearance();
            renderPage();
        } catch (PDFException e) {
            e.printStackTrace();
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
                if (isInValidInkEvent || pdfPath == null) {
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
                if (isInValidInkEvent || pdfPath == null) {
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

    public void removeOneAnnot() {
        if (page == null || !isHasAnnot()) {
            return;
        }
        try {
            page.loadAnnots();
            Annot[] annots = page.getAllAnnotsByTabOrder(ANNOT_FILTER);
            if (annots == null) {
                return;
            }
            removeAnnot(annots[annots.length - 1]);
        } catch (PDFException e) {
            e.printStackTrace();
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
                    isFiling = false;
                    scroller.forceFinished(true);
                }
                lastPointF.set(curPointF);
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
                if (!isFiling) {
                    renderBitmap();
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
        }

        renderBitmap();
    }

    public void setBitmap(Bitmap bitmapCache) {
        this.bitmap = bitmapCache;
        postInvalidate();
    }

    public void zoomH(float curScale) {
        scale(curScale / getCurScale(), widgetWidth / 2, 0);
        renderBitmap();
    }

    public void zoom(float curScale) {
        scale(curScale / getCurScale(), 0, 0);
        renderBitmap();
    }

    public void zoomOut() {
        Log.i(TAG, "zoomOut: curScale: " + getCurScale());
        if (getCurScale() < maxScale) {
            float scaleAfter = getCurScale() * SCALE_BIGGER;
            if (scaleAfter >= maxScale) {
                scale(maxScale / getCurScale(), widgetWidth / 2, widgetHeight / 2);
            } else {
                scale(SCALE_BIGGER, widgetWidth / 2, widgetHeight / 2);
            }
            if (scaleListener != null) {
                scaleListener.onScaleChange();
            }
            new RenderBitmapTask().execute();
        }
    }

    public void zoomIn() {
        Log.i(TAG, "zoomIn: curScale: " + getCurScale());
        if (getCurScale() > minScale) {
            float scaleAfter = getCurScale() * SCALE_SMALLER;
            if (scaleAfter <= minScale) {
                scale(minScale / getCurScale(), widgetWidth / 2, widgetHeight / 2);
            } else {
                scale(SCALE_SMALLER, widgetWidth / 2, widgetHeight / 2);
            }
            if (scaleListener != null) {
                scaleListener.onScaleChange();
            }
            new RenderBitmapTask().execute();
        }
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        float curScale = getCurScale();
        float scaleFactor = detector.getScaleFactor();
        float scaleAfter = curScale * scaleFactor;

        if (scaleAfter <= maxScale && scaleAfter >= minScale) {
            float focusX = detector.getFocusX();
            float focusY = detector.getFocusY();
            scale(scaleFactor, focusX, focusY);
        }
        return true;
    }

    public void moveAA(int x, int y) {
//        if (displayMatrix == null || canvasMatrix == null) {
//            return;
//        }
//        displayMatrix.postTranslate(x, y);
//        canvasMatrix.postTranslate(x, y);
//        if (getMatrixRectF().width() < widgetWidth) {
//            float dx = (widgetWidth - getMatrixRectF().width()) / 2 - getMatrixRectF().left;
//            displayMatrix.postTranslate(dx, 0);
//            canvasMatrix.postTranslate(dx, 0);
//        } else {
//            if ((int) getMatrixRectF().left >= 0) {
//                float dx = -getMatrixRectF().left;
//                displayMatrix.postTranslate(dx, 0);
//                canvasMatrix.postTranslate(dx, 0);
//            }
//            if (getMatrixRectF().right < widgetWidth) {
//                float dx = widgetWidth - getMatrixRectF().right;
//                displayMatrix.postTranslate(dx, 0);
//                canvasMatrix.postTranslate(dx, 0);
//            }
//        }
//
//        if (getMatrixRectF().height() < widgetHeight) {
//            float dy = (widgetHeight - getMatrixRectF().height()) / 2 - getMatrixRectF().top;
//            displayMatrix.postTranslate(0, dy);
//            canvasMatrix.postTranslate(0, dy);
//        } else {
//            if ((int) getMatrixRectF().top >= 0) {
//                float dy = -getMatrixRectF().top;
//                displayMatrix.postTranslate(0, dy);
//                canvasMatrix.postTranslate(0, dy);
//            }
//            if (getMatrixRectF().bottom < widgetHeight) {
//                float dy = widgetHeight - getMatrixRectF().bottom;
//                displayMatrix.postTranslate(0, dy);
//                canvasMatrix.postTranslate(0, dy);
//            }
//        }
//        renderBitmap();
    }

    private void scale(float scaleFactor, float focusX, float focusY) {
        isScaling = true;
        displayMatrix.postScale(scaleFactor, scaleFactor, focusX, focusY);

        if (getMatrixRectF().width() < widgetWidth) {
            float dx = (widgetWidth - getMatrixRectF().width()) / 2 - getMatrixRectF().left;
            displayMatrix.postTranslate(dx, 0);
        } else {
            if ((int) getMatrixRectF().left >= 0) {
                float dx = -getMatrixRectF().left;
                displayMatrix.postTranslate(dx, 0);
            }
            if (getMatrixRectF().right < widgetWidth) {
                float dx = widgetWidth - getMatrixRectF().right;
                displayMatrix.postTranslate(dx, 0);
            }
        }

        if (getMatrixRectF().height() < widgetHeight) {
            float dy = (widgetHeight - getMatrixRectF().height()) / 2 - getMatrixRectF().top;
            displayMatrix.postTranslate(0, dy);
        } else {
            if ((int) getMatrixRectF().top >= 0) {
                float dy = -getMatrixRectF().top;
                displayMatrix.postTranslate(0, dy);
            }
            if (getMatrixRectF().bottom < widgetHeight) {
                float dy = widgetHeight - getMatrixRectF().bottom;
                displayMatrix.postTranslate(0, dy);
            }
        }

        curScale = getCurScale();
        renderBitmap();
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {
        if (scaleListener != null) {
            scaleListener.onScaleChange();
        }
        renderBitmap();
    }

    public float getCurScale() {
        displayMatrix.getValues(displayMatrixValues);
        return displayMatrixValues[Matrix.MSCALE_X];
    }

    public float getCurScaleList() {
        displayMatrix.getValues(displayMatrixValues);
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
        if (displayMatrix == null) {
            return false;
        }

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
    public void onGlobalLayout() {
        getViewTreeObserver().removeGlobalOnLayoutListener(this);
        if (showMode == ShowMode.ViewPager) {
            widgetWidth = getWidth();
            widgetHeight = getHeight();


            if (task == null) {
                bitmap = null;
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

    public boolean isHasAnnot() {
        if (page == null) {
            return false;
        }

        try {
            page.loadAnnots();
            return page.countAnnots(ANNOT_FILTER) != 0;
        } catch (PDFException e) {
            e.printStackTrace();
        }
        return false;
    }


    public void updateInitBitmap(int currentItem) {
//        new RenderInitBitmapTask().execute(currentItem);

        if (page != null) {
            float scale, tranX, tranY;
            int num = 1;
            if (showMode == ShowMode.ViewPager) {
                if (widgetWidth > widgetHeight) {
                    scale = widgetHeight * 1.0f / pageHeight;
                    tranX = (widgetWidth - pageWidth * scale) / 2;
                    tranY = 0;

                    num = 2;

                } else {
                    scale = widgetWidth * 1.0f / pageWidth;
                    tranX = 0;
                    tranY = (widgetHeight - pageHeight * scale) / 2;
                    num = 1;
                }
            } else {
                scale = widgetWidth * 1.0f / pageWidth;
                tranX = 0;
                tranY = 0;
                num = 1;
            }

//            try {
//                displayMatrix = page.getDisplayMatrix(0, 0, (int) pageWidth, (int) pageHeight, PDFPage.ROTATION_0);
//            } catch (PDFException e) {
//                e.printStackTrace();
//            }
//
            //scale = 2f;

//            displayMatrix.postScale(scale*num, scale*num);
//            float ranX = (widgetWidth - pageWidth * scale*num) / 2;
//            displayMatrix.postTranslate(ranX, tranY);
//                postInvalidate();
            //renderBitmap();
            //
            Bitmap bitmap = Bitmap.createBitmap((int) (widgetWidth * num), (int) (widgetHeight * num), Bitmap.Config.ARGB_8888);
            bitmap.eraseColor(COLOR_BACKGROUD);
            try {
                Renderer renderer = Renderer.create(bitmap);
                RenderContext renderContext = RenderContext.create();
                Matrix matrix = page.getDisplayMatrix(0, 0, (int) pageWidth, (int) pageHeight, PDFPage.ROTATION_0);
//                matrix.postTranslate(2, 2);
                matrix.postScale(scale * num, scale * num);
                float ranX = (widgetWidth - pageWidth * scale * num) / 2;
                matrix.postTranslate(ranX, tranY);
                renderContext.setMatrix(matrix);
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
            initBitmap = bitmap;

//                displayMatrix.postScale(scale, scale);
//            postInvalidate();
//            renderBitmap();
        }

    }

    class RenderInitBitmapTask extends AsyncTask<Integer, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(Integer... params) {
            if (widgetWidth == 0 || widgetHeight == 0) {
                return null;
            }

            Bitmap bitmap = Bitmap.createBitmap(widgetWidth, widgetHeight, Bitmap.Config.ARGB_8888);
            bitmap.eraseColor(COLOR_BACKGROUD);

            if (page != null) {
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
                    Matrix matrix = page.getDisplayMatrix(0, 0, (int) pageWidth, (int) pageHeight, PDFPage.ROTATION_0);
                    matrix.postScale(scale, scale);
                    matrix.postTranslate(tranX, tranY);
                    Renderer renderer = Renderer.create(bitmap);
                    RenderContext renderContext = RenderContext.create();
                    renderContext.setMatrix(matrix);
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
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
//            HYPDFView.this.initBitmap = bitmap;
        }
    }


    class RenderBitmapTask extends AsyncTask<Void, Void, Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            setAlpha(0f);
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

            isScaling = false;
            isDraging = false;
            isFiling = false;

//            if (HYPDFView.this.bitmap == null) {
//                HYPDFView.this.initBitmap = bitmap;
//            }
            HYPDFView.this.bitmap = bitmap;
            Integer tag = (Integer) getTag();
            if (tag != null && tag == position) {
                if (getCurScale() != curScale) {
                    zoom(curScale);
                }
                postInvalidate();
            }

//            animate().alpha(1).setDuration(10).setListener(new Animator.AnimatorListener() {
//                @Override
//                public void onAnimationStart(Animator animation) {
//                    if (HYPDFView.this.bitmap == null) {
//                        HYPDFView.this.initBitmap = bitmap;
//                    }
//                    HYPDFView.this.bitmap = bitmap;
//                    Integer tag = (Integer) getTag();
//                    if (tag != null && tag == position) {
//                        if(getCurScale() != curScale){
//                            zoom(curScale);
//                        }
//                        postInvalidate();
//                    }
//                }
//
//                @Override
//                public void onAnimationEnd(Animator animation) {
//
//                }
//
//                @Override
//                public void onAnimationCancel(Animator animation) {
//
//                }
//
//                @Override
//                public void onAnimationRepeat(Animator animation) {
//
//                }
//            }).start();
        }
    }

    class ParsePageTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... avoid) {
            try {
                if (page != null && !page.isParsed()) {
                    Progress progress = page.startParse(PDFPage.RENDERFLAG_NORMAL);
                    if (progress != null) {
                        progress.continueProgress(30);
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

            try {
                if (page != null) {
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

                    minScale *= scale;
                    maxScale *= scale;
                    displayMatrix = page.getDisplayMatrix(0, 0, (int) pageWidth, (int) pageHeight, PDFPage.ROTATION_0);
                    displayMatrix.postScale(scale, scale);
                    displayMatrix.postTranslate(tranX, tranY);
                    bitmap = Bitmap.createBitmap(widgetWidth, widgetHeight, Bitmap.Config.ARGB_8888);
                    renderBitmap();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void renderBitmap() {
        try {
            isScaling = false;
            bitmap.eraseColor(COLOR_BACKGROUD);
            Renderer renderer = Renderer.create(bitmap);
            RenderContext renderContext = RenderContext.create();
            renderContext.setMatrix(displayMatrix);
            renderContext.setFlags(RenderContext.FLAG_ANNOT);
            Progress progress = page.startRender(renderContext, renderer, PDFPage.RENDERFLAG_NORMAL);
            if (progress != null) {
                int r = Progress.TOBECONTINUED;
                while (r == Progress.TOBECONTINUED) {
                    r = progress.continueProgress(30);
                }
                progress.release();
            }
            renderContext.release();
            renderer.release();
            postInvalidate();
        } catch (PDFException e) {
            e.printStackTrace();
        }
    }

}
