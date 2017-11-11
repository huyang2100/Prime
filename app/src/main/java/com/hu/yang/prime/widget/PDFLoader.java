package com.hu.yang.prime.widget;

import android.animation.Animator;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.foxit.gsdk.PDFException;
import com.foxit.gsdk.pdf.PDFDocument;
import com.foxit.gsdk.pdf.PDFPage;
import com.foxit.gsdk.pdf.Progress;
import com.foxit.gsdk.pdf.RenderContext;
import com.foxit.gsdk.pdf.Renderer;
import com.hu.yang.prime.R;

import java.util.HashSet;
import java.util.Set;

public class PDFLoader {
    private static final String TAG = PDFLoader.class.getSimpleName();
    private LruCache<Integer, Bitmap> mCache;
    private ListView mListView;
    private Set<LoadPDFTask> mTasks;
    private int widgetWidth;
    private int widgetHeight;
    private PDFDocument document;
    private Bitmap loadingBitmap;

    public PDFLoader() {
        mTasks = new HashSet<LoadPDFTask>();
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemory / 3;
        Log.i(TAG, "PDFLoader: cachemax "+maxMemory +" cacheSize: "+cacheSize);
        mCache = new LruCache<Integer, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(Integer key, Bitmap value) {
                return value.getByteCount();
            }
        };
    }

    public PDFLoader(ListView listView) {
        this();
        mListView = listView;
    }

    public void addBitmapToCache(int position, Bitmap bitmap) {
        Log.i("", "addBitmapToCache: index: " + position + mCache.size());

        mCache.put(position, bitmap);
        if (getBitmapFromCache(position) == null) {
            mCache.put(position, bitmap);
        }
    }

    public Bitmap getBitmapFromCache(int position) {
        return mCache.get(position);
    }

    public void cancelAllTasks() {
        if (mTasks != null) {
            for (LoadPDFTask task : mTasks) {
                task.cancel(true);
            }
        }
    }

    public void loadPDF(int start, int end) {
        for (int i = start; i < end; i++) {
            Bitmap bitmapCache = getBitmapFromCache(i);
            if (bitmapCache == null) {
                Log.i(TAG, "loadPDF: load pdf...");
                new LoadPDFTask(i).execute();
            } else {
                Log.i(TAG, "loadPDF: load from pdf bingo!!!");
                showPDF(i, bitmapCache);
            }
        }
    }

    public void setDoc(PDFDocument document) {
        this.document = document;
    }

    public void setWH(int w, int h) {
        widgetWidth = w;
        widgetHeight = h;
    }

    public void setLoadingBitmap(Bitmap loadingBitmap) {
        this.loadingBitmap = loadingBitmap;
    }

    class RenderBitmapTask extends AsyncTask<PDFPage, Void, Bitmap> {

        private final Integer position;

        public RenderBitmapTask(Integer position) {
            this.position = position;
        }

        @Override
        protected Bitmap doInBackground(PDFPage... pages) {
            if (widgetWidth == 0 || widgetHeight == 0) {
                return null;
            }
            PDFPage page = pages[0];
            Bitmap bitmap = Bitmap.createBitmap(widgetWidth, widgetHeight, Bitmap.Config.ARGB_8888);
            bitmap.eraseColor(Color.WHITE);
            try {
                Renderer renderer = Renderer.create(bitmap);
                RenderContext renderContext = RenderContext.create();
                float scale = widgetWidth * 1.0f / page.getSize().getWidth();
                Matrix displayMatrix = page.getDisplayMatrix(0, 0, (int) page.getSize().getWidth(), (int) page.getSize().getHeight(), PDFPage.ROTATION_0);
                displayMatrix.postScale(scale, scale);
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

            addBitmapToCache(position, bitmap);

            showPDF(position, bitmap);

        }
    }

    private void showPDF(Integer position, Bitmap bitmap) {
        View viewWithTag = mListView.findViewWithTag(position);
        if (viewWithTag != null) {
            HYPDFView pdfView = (HYPDFView) viewWithTag.findViewById(R.id.pdfView);
            pdfView.setBitmap(bitmap);
            Log.i(TAG, "showPDF: bitmapSize "+bitmap.getByteCount());
        }
    }

    public class LoadPDFTask extends AsyncTask<Void, Void, PDFPage> {

        private final int position;

        public LoadPDFTask(int position) {
            this.position = position;
        }

        @Override
        protected PDFPage doInBackground(Void... aVoid) {
            PDFPage page = null;
            try {
                page = document.getPage(position);
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
            return page;
        }

        @Override
        protected void onPostExecute(PDFPage page) {
            super.onPostExecute(page);
            if (page != null) {
                new RenderBitmapTask(position).execute(page);
            }
        }
    }

}