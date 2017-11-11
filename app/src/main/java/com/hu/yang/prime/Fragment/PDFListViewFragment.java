package com.hu.yang.prime.Fragment;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.foxit.gsdk.PDFException;
import com.foxit.gsdk.PDFLibrary;
import com.foxit.gsdk.pdf.PDFDocument;
import com.foxit.gsdk.utils.FileHandler;
import com.foxit.gsdk.utils.SizeF;
import com.hu.yang.prime.R;
import com.hu.yang.prime.widget.HYPDFView;
import com.hu.yang.prime.widget.PDFLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by yanghu on 2017/9/15.
 */

public class PDFListViewFragment extends Fragment {

    private static final int memorySize = 1024 * 1024 * 30;
    private static final String INDEX_PAGE = "index_page";
    private static boolean scaleable = true;
    private static final String key = "kq60rxNSqAk3uVa3c2r+lwH3gzU/vu4hMPGbXIf4CqpBc22cR5zMJw==";
    private static final String code = "8f3o18FpvRkNQzfqO0hmX2EDOIM4wDoN4KLEm8EixMt3sdy04PV3H0kSUk4dgLJjOQL/m3v9Qr/pv1FYUDTjwtk1min5vtg2ZvIoWPTBFVYnv9rUA8UL6IRWVUZJnT/LzOsyvYHl6u2V5XOb0UIAJMVG26s9JgxImUzuKYkPhTC+9XS6lEgBMFq6Fi4srep7lsWFNC36nAc3GF4jQLQqfait15gDrvqe7qJ5uGSKBUy4xRXdXk2cw897hJld3XdBw4JbkigCs0Y/Vmfh8U2COQvGyWzPR4iOoBpNlTKCqMMXYB89JtoEPKafY4Af99kI/DLA/yBVeasIdHDPN10CvjToRZdY1yethHDH9/GVARefHI/y0tuTliD7o5xhflurh21BH5raeGTP/VUydymw+vUcExUX83IdhHkf1DvN0Ze6DV30QHvz/M7UHHlBGcu7xjp5Ya2IazWMAJBN0GEcMM3vxErxAtxvQx+UMDzFxi2tZeNfr7+v+ox0NIloLI0FN6/ErYiiUDvCkaXw1fpiTLkIEr40cxvODy+ESzmcKp+LFx5IGUKBrpkbkgVjcyyDDBiWOOIqBIGmelvOTaBCnaG1Whjaxksq1P6vv9rdjRBOWAFdrD9+Xly8CGna9jPIyDb2N+ZkeUTICZLvZuY4GMlnwvTFvjhXVjBaUgFGr251TyyjMjIFXKebpwt5OJEXpbHDh5Xj9QGzoGriP2eeaYDu45nWyK5c5Ir/HJ7oJQeBMeQbvVVF3YztmKfnnD+1i5B/KoxKEfTT29msk9VfW+4w/gq+5fLkyfSCs+YnKNh6vmLxB3rQM5RIJLeyMB2fLe4B1pXa5gfM64zDerkcbTL5iWkXlUhw83RJmhtFBRsI/sXrqFNCup2/xvWFLXywysH+j1Md/EqR3CL1ueuhFpqh5F/vYvEPT1Obp4Limz6bP6QiRQFFk3B97RvHPY8FZoX/Dp+bhi39UL4Spogx56xuZprUNqLtGI9R61QBICET0EsWzJWQky7Yt38KNlGZf8TmEPp+KAmfR/v93biw331VsvTYFmfpOKBLfo88A9YgQ2cYnQqG6EME8e59YZMeRqC+cA6FpnLAkMVsPe+5ckEvQhtKQIKPqi0CDTDNZ/EEacwaWFAEt1C5TvYYIT1jBYtnExkZcbyKNh0zmJJyZEu6f9F4+EhguUVrQ52QXuGYp6NWxeewZKzb2rp0OCPgNMR6ZOF+e744XFNSYX26CVhT1nbgg8FWYpCC7eJuBYPEdUK5/Vkw8sh1I3ST6Bz9VSFUzrNg5EB5o1rVeJR8JNUqV/QoVyxx6dyZUPcHHCOyR6/j7uZrTfxFjqoyFb+ma/1X71k/Y2cEGlJZwLiogyt89naQlNocKqcmo5ZUS/mR8dJ0GBWttZZRleHzAnIxPCMo1k4ZttikNXCW4dOLlvBUGlvCz+T8nc6GTO56KMq++HQSE94JKUatCIJXabmHr7bWSfsFD7cuIYZWZ1hDx4fo3/XHX43Myb3U5KDcAPjlRpnwuoRdzQerRgRqarpMFe2ZQuWLm7QKpw3Pq4IdDMT3Y2gnR47HFIxoo6auygpKOrYgrkg72JltaghiCXCOEUfTVar+";

    static {
        System.loadLibrary("fsdk_android");
    }

    private PDFLibrary pdfLibrary;
    private PDFDocument document;
    private ListView listView;
    private PDFLoader pdfLoader;
    private int mStart;
    private int mEnd;
    private boolean isFirst;
    private int widgetHeight;
    private int widgetWidth;
    private Bitmap loadingBitmap;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        File file = getPDFile();

        pdfLibrary = PDFLibrary.getInstance();
        try {
            pdfLibrary.initialize(memorySize, scaleable);
            pdfLibrary.unlock(key, code);

            FileHandler fileHandler = FileHandler.create(file.getAbsolutePath(), FileHandler.FILEMODE_READONLY);
            document = PDFDocument.open(fileHandler, null);
            isFirst = true;
            try {
                SizeF pageSize = document.getPage(0).getSize();
                widgetWidth = getResources().getDisplayMetrics().widthPixels;
                widgetHeight = (int) (widgetWidth * 1.0f / pageSize.getWidth() * pageSize.getHeight());
            } catch (PDFException e) {
                e.printStackTrace();
            }
        } catch (PDFException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(INDEX_PAGE, listView.getFirstVisiblePosition());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (pdfLibrary != null) {
            pdfLibrary.destroy();
            pdfLibrary = null;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        listView = new ListView(getActivity());
        listView.setDividerHeight(10);
        pdfLoader = new PDFLoader(listView);
        pdfLoader.setDoc(document);

        pdfLoader.setWH(widgetWidth, widgetHeight);
        loadingBitmap = Bitmap.createBitmap(widgetWidth, widgetHeight, Bitmap.Config.ARGB_8888);
        loadingBitmap.eraseColor(Color.WHITE);
        pdfLoader.setLoadingBitmap(loadingBitmap);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    pdfLoader.loadPDF(mStart, mEnd);
                } else {
                    pdfLoader.cancelAllTasks();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                mStart = firstVisibleItem;
                mEnd = firstVisibleItem + visibleItemCount;
                if (isFirst && visibleItemCount > 0) {
                    isFirst = false;
                    pdfLoader.loadPDF(mStart, mEnd);
                }
            }
        });


        HYPDFView.showMode = HYPDFView.ShowMode.ListView;
        PDFListAdapter listAdapter = new PDFListAdapter(document);
        listView.setAdapter(listAdapter);
        if (savedInstanceState != null) {
            listView.setSelection(savedInstanceState.getInt(INDEX_PAGE));
        }
        return listView;
    }


    class PDFListAdapter extends BaseAdapter {

        private final String TAG = PDFListAdapter.class.getSimpleName();
        private final PDFDocument document;
        private HYPDFView pdfView;

        public PDFListAdapter(PDFDocument document) {
            this.document = document;
        }

        @Override
        public int getCount() {
            try {
                return document.countPages();
            } catch (PDFException e) {
                e.printStackTrace();
            }

            return 0;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            if (convertView == null) {
                convertView = View.inflate(getActivity(), R.layout.item_pdf_list, null);
            }

            convertView.setTag(position);
            pdfView = (HYPDFView) convertView.findViewById(R.id.pdfView);
            ViewGroup.LayoutParams layoutParams = pdfView.getLayoutParams();
            layoutParams.height = widgetHeight;
            pdfView.setLayoutParams(layoutParams);
            pdfView.setWH(viewGroup.getWidth(), widgetHeight);
            Bitmap bitmapFromCache = pdfLoader.getBitmapFromCache(position);
            if (bitmapFromCache == null) {
                pdfView.setBitmap(loadingBitmap);
            } else {
                pdfView.setBitmap(bitmapFromCache);
            }
            return convertView;
        }
    }

    private File getPDFile() {
        InputStream is = getResources().openRawResource(R.raw.making_the_invisible_visible);
        File dir = new File(Environment.getExternalStorageDirectory(), "pdf");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(dir, "nasa.pdf");
        if (file.exists()) {
            return file;
        }
        try {
            FileOutputStream fos = new FileOutputStream(file);
            int len;
            byte[] buffer = new byte[1024];
            while ((len = is.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
            fos.close();
            is.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
        }
        return file;
    }
}
