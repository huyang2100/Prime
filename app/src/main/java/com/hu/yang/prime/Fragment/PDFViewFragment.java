package com.hu.yang.prime.Fragment;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.foxit.gsdk.PDFException;
import com.foxit.gsdk.PDFLibrary;
import com.foxit.gsdk.pdf.PDFDocument;
import com.foxit.gsdk.utils.FileHandler;
import com.hu.yang.prime.R;
import com.hu.yang.prime.widget.HYPDFView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by yanghu on 2017/9/15.
 */

public class PDFViewFragment extends Fragment {

    private static final int memorySize = 1024 * 1024 * 30;
    private static boolean scaleable = true;
    private static final String key = "kq60rxNSqAk3uVa3c2r+lwH3gzU/vu4hMPGbXIf4CqpBc22cR5zMJw==";
    private static final String code = "8f3o18FpvRkNQzfqO0hmX2EDOIM4wDoN4KLEm8EixMt3sdy04PV3H0kSUk4dgLJjOQL/m3v9Qr/pv1FYUDTjwtk1min5vtg2ZvIoWPTBFVYnv9rUA8UL6IRWVUZJnT/LzOsyvYHl6u2V5XOb0UIAJMVG26s9JgxImUzuKYkPhTC+9XS6lEgBMFq6Fi4srep7lsWFNC36nAc3GF4jQLQqfait15gDrvqe7qJ5uGSKBUy4xRXdXk2cw897hJld3XdBw4JbkigCs0Y/Vmfh8U2COQvGyWzPR4iOoBpNlTKCqMMXYB89JtoEPKafY4Af99kI/DLA/yBVeasIdHDPN10CvjToRZdY1yethHDH9/GVARefHI/y0tuTliD7o5xhflurh21BH5raeGTP/VUydymw+vUcExUX83IdhHkf1DvN0Ze6DV30QHvz/M7UHHlBGcu7xjp5Ya2IazWMAJBN0GEcMM3vxErxAtxvQx+UMDzFxi2tZeNfr7+v+ox0NIloLI0FN6/ErYiiUDvCkaXw1fpiTLkIEr40cxvODy+ESzmcKp+LFx5IGUKBrpkbkgVjcyyDDBiWOOIqBIGmelvOTaBCnaG1Whjaxksq1P6vv9rdjRBOWAFdrD9+Xly8CGna9jPIyDb2N+ZkeUTICZLvZuY4GMlnwvTFvjhXVjBaUgFGr251TyyjMjIFXKebpwt5OJEXpbHDh5Xj9QGzoGriP2eeaYDu45nWyK5c5Ir/HJ7oJQeBMeQbvVVF3YztmKfnnD+1i5B/KoxKEfTT29msk9VfW+4w/gq+5fLkyfSCs+YnKNh6vmLxB3rQM5RIJLeyMB2fLe4B1pXa5gfM64zDerkcbTL5iWkXlUhw83RJmhtFBRsI/sXrqFNCup2/xvWFLXywysH+j1Md/EqR3CL1ueuhFpqh5F/vYvEPT1Obp4Limz6bP6QiRQFFk3B97RvHPY8FZoX/Dp+bhi39UL4Spogx56xuZprUNqLtGI9R61QBICET0EsWzJWQky7Yt38KNlGZf8TmEPp+KAmfR/v93biw331VsvTYFmfpOKBLfo88A9YgQ2cYnQqG6EME8e59YZMeRqC+cA6FpnLAkMVsPe+5ckEvQhtKQIKPqi0CDTDNZ/EEacwaWFAEt1C5TvYYIT1jBYtnExkZcbyKNh0zmJJyZEu6f9F4+EhguUVrQ52QXuGYp6NWxeewZKzb2rp0OCPgNMR6ZOF+e744XFNSYX26CVhT1nbgg8FWYpCC7eJuBYPEdUK5/Vkw8sh1I3ST6Bz9VSFUzrNg5EB5o1rVeJR8JNUqV/QoVyxx6dyZUPcHHCOyR6/j7uZrTfxFjqoyFb+ma/1X71k/Y2cEGlJZwLiogyt89naQlNocKqcmo5ZUS/mR8dJ0GBWttZZRleHzAnIxPCMo1k4ZttikNXCW4dOLlvBUGlvCz+T8nc6GTO56KMq++HQSE94JKUatCIJXabmHr7bWSfsFD7cuIYZWZ1hDx4fo3/XHX43Myb3U5KDcAPjlRpnwuoRdzQerRgRqarpMFe2ZQuWLm7QKpw3Pq4IdDMT3Y2gnR47HFIxoo6auygpKOrYgrkg72JltaghiCXCOEUfTVar+";

    static {
        System.loadLibrary("fsdk_android");
    }

    private PDFLibrary pdfLibrary;
    private PDFDocument document;

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
        } catch (PDFException e) {
            e.printStackTrace();
        }
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
        HYPDFView pdfView = new HYPDFView(getActivity());
        HYPDFView.showMode = HYPDFView.ShowMode.ViewPager;
        try {
            pdfView.setTag(21);
            pdfView.load(document, 21);
        } catch (PDFException e) {
            e.printStackTrace();
        }
        return pdfView;
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
