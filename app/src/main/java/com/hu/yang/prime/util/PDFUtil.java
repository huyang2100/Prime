package com.hu.yang.prime.util;

import android.graphics.Bitmap;
import android.graphics.RectF;
import android.util.Log;

import com.foxit.gsdk.PDFException;
import com.foxit.gsdk.PDFLibrary;
import com.foxit.gsdk.pdf.PDFDocument;
import com.foxit.gsdk.pdf.PDFPage;
import com.foxit.gsdk.pdf.PDFTextPage;
import com.foxit.gsdk.pdf.PDFTextSelection;
import com.foxit.gsdk.pdf.Progress;
import com.foxit.gsdk.utils.FileHandler;
import com.foxit.gsdk.utils.SizeF;
import com.hu.yang.prime.widget.PDFView;

import java.util.ArrayList;

/**
 * PDF相关操作工具类
 *
 * @author Yang
 * @time 2016/12/19 10:33
 */

public class PDFUtil {

    //过期
    private static final String SN = "kq60rxNSqAk3uVa3c2r+lwH3gzU/vu4hMPGbXIf4CqpBc22cR5zMJw==";
    private static final String KEY = "8f3o18FpvRkNQzfqO0hmX2EDOIM4wDoN4KLEm8EixMt3sdy04PV3H0kSUk4dgLJjOQL/m3v9Qr/pv1FYUDTjwtk1min5vtg2ZvIoWPTBFVYnv9rUA8UL6IRWVUZJnT/LzOsyvYHl6u2V5XOb0UIAJMVG26s9JgxImUzuKYkPhTC+9XS6lEgBMFq6Fi4srep7lsWFNC36nAc3GF4jQLQqfait15gDrvqe7qJ5uGSKBUy4xRXdXk2cw897hJld3XdBw4JbkigCs0Y/Vmfh8U2COQvGyWzPR4iOoBpNlTKCqMMXYB89JtoEPKafY4Af99kI/DLA/yBVeasIdHDPN10CvjToRZdY1yethHDH9/GVARefHI/y0tuTliD7o5xhflurh21BH5raeGTP/VUydymw+vUcExUX83IdhHkf1DvN0Ze6DV30QHvz/M7UHHlBGcu7xjp5Ya2IazWMAJBN0GEcMM3vxErxAtxvQx+UMDzFxi2tZeNfr7+v+ox0NIloLI0FN6/ErYiiUDvCkaXw1fpiTLkIEr40cxvODy+ESzmcKp+LFx5IGUKBrpkbkgVjcyyDDBiWOOIqBIGmelvOTaBCnaG1Whjaxksq1P6vv9rdjRBOWAFdrD9+Xly8CGna9jPIyDb2N+ZkeUTICZLvZuY4GMlnwvTFvjhXVjBaUgFGr251TyyjMjIFXKebpwt5OJEXpbHDh5Xj9QGzoGriP2eeaYDu45nWyK5c5Ir/HJ7oJQeBMeQbvVVF3YztmKfnnD+1i5B/KoxKEfTT29msk9VfW+4w/gq+5fLkyfSCs+YnKNh6vmLxB3rQM5RIJLeyMB2fLe4B1pXa5gfM64zDerkcbTL5iWkXlUhw83RJmhtFBRsI/sXrqFNCup2/xvWFLXywysH+j1Md/EqR3CL1ueuhFpqh5F/vYvEPT1Obp4Limz6bP6QiRQFFk3B97RvHPY8FZoX/Dp+bhi39UL4Spogx56xuZprUNqLtGI9R61QBICET0EsWzJWQky7Yt38KNlGZf8TmEPp+KAmfR/v93biw331VsvTYFmfpOKBLfo88A9YgQ2cYnQqG6EME8e59YZMeRqC+cA6FpnLAkMVsPe+5ckEvQhtKQIKPqi0CDTDNZ/EEacwaWFAEt1C5TvYYIT1jBYtnExkZcbyKNh0zmJJyZEu6f9F4+EhguUVrQ52QXuGYp6NWxeewZKzb2rp0OCPgNMR6ZOF+e744XFNSYX26CVhT1nbgg8FWYpCC7eJuBYPEdUK5/Vkw8sh1I3ST6Bz9VSFUzrNg5EB5o1rVeJR8JNUqV/QoVyxx6dyZUPcHHCOyR6/j7uZrTfxFjqoyFb+ma/1X71k/Y2cEGlJZwLiogyt89naQlNocKqcmo5ZUS/mR8dJ0GBWttZZRleHzAnIxPCMo1k4ZttikNXCW4dOLlvBUGlvCz+T8nc6GTO56KMq++HQSE94JKUatCIJXabmHr7bWSfsFD7cuIYZWZ1hDx4fo3/XHX43Myb3U5KDcAPjlRpnwuoRdzQerRgRqarpMFe2ZQuWLm7QKpw3Pq4IdDMT3Y2gnR47HFIxoo6auygpKOrYgrkg72JltaghiCXCOEUfTVar+";

    private static final String TAG = "PDFUtil";
    public static int SIZE_WORD = 5;
    public static int SIZE_PSI = 5;
    public static int COLOR_PSI = 0xfffc615d;
    public static int COLOR_WORD = 0x2bc3e9;
    public static int COLOR_UNDERlINE = 0xff34c749;
    public static int COLOR_HIGHLIGHT = 0xffffe21d;
    public static float MAX_SCALE = 8.0f;
    public static final float INIT_SCALE = 1.0f;
    public static float mScaleFoucsX;
    public static float mScaleFoucsY;
    public static PDFView.EditMode editMode = PDFView.EditMode.MODE_NONE;

    static {
        System.loadLibrary("fsdk_android");
    }

    private static PDFLibrary pdfLibrary;
    private static PDFDocument pdfDocument;
    private static Bitmap loadingBitmap;
    public static int pdfInitWidth;


    public static int getPdfInitWidth(int initPdfHeight) {
        if (pdfDocument == null) {
            return 0;
        }
        try {
            SizeF size = pdfDocument.getPage(0).getSize();
            pdfInitWidth = (int) (size.getWidth() * initPdfHeight * 1.0f / size.getHeight());
            return pdfInitWidth;
        } catch (PDFException e) {
            log(e);
            return 0;
        }
    }

    public static void log(PDFException e) {
        Log.i(TAG, e.getStackTrace()[0].getMethodName() + e.getLastError());
    }

    public static PDFDocument openDocument(String filename) {
        try {
            FileHandler fileHandler = FileHandler.create(filename, FileHandler.FILEMODE_READONLY);
            pdfDocument = PDFDocument.open(fileHandler, null);
            return pdfDocument;
        } catch (PDFException e) {
            e.printStackTrace();
            log(e);
            return null;
        }
    }

    public static void parsePage(PDFPage page) {

        //parse
        try {
            if (page == null || page.isParsed()) {
                return;
            }

            Progress parserProgress = null;
            parserProgress = page.startParse(PDFPage.PARSEFLAG_NORMAL);
            int ret_prog = Progress.TOBECONTINUED;
            while (ret_prog == Progress.TOBECONTINUED) {
                ret_prog = parserProgress.continueProgress(30);
            }
            parserProgress.release();
        } catch (PDFException e) {
            e.printStackTrace();
            log(e);
        }
    }

    public static PDFLibrary loadPDF() {
        if(pdfLibrary == null){
            pdfLibrary = PDFLibrary.getInstance();
            try {
                pdfLibrary.initialize(30 * 1024 * 1024, true);
                pdfLibrary.unlock(SN, KEY);
                return pdfLibrary;
            } catch (PDFException e) {
                e.printStackTrace();
                log(e);
                return null;
            }
        }else{
            return pdfLibrary;
        }

    }


    public static void detroyPDF() {
        if (pdfLibrary != null) {
            pdfLibrary.destroy();
            pdfLibrary = null;
        }
    }

    public static ArrayList<RectF> getPieceRectFs(PDFPage page, RectF newRectF) {
        ArrayList<RectF> rectFs = new ArrayList<>();
        try {
            PDFTextPage pdfTextPage = PDFTextPage.create(page);
            PDFTextSelection pdfTextSelection = pdfTextPage.selectByRectangle(newRectF);
            String chars = pdfTextSelection.getChars();
//            Log.i(TAG, "chars: " + chars);
            int countPieces = pdfTextSelection.countPieces();
//            Log.i(TAG, "countPieces: " + countPieces);

            for (int i = 0; i < countPieces; i++) {
                RectF pieceRect = pdfTextSelection.getPieceRect(i);
                rectFs.add(pieceRect);
            }
        } catch (PDFException e) {
            e.printStackTrace();
        }
        return rectFs;
    }

    public static void initToolsColor() {
        COLOR_PSI = 0xfffc615d;
        COLOR_WORD = 0x2bc3e9;
        COLOR_UNDERlINE = 0xff34c749;
        COLOR_HIGHLIGHT = 0xffffe21d;
        SIZE_WORD = 5;
        SIZE_PSI = 5;
    }
}
