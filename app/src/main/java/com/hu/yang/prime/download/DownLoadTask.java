package com.hu.yang.prime.download;

import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.hu.yang.prime.R;
import com.hu.yang.prime.application.PrimeApplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by yanghu on 2017/11/12.
 */

public class DownLoadTask extends AsyncTask<String, Integer, Void> {
    private static final String TAG = DownLoadTask.class.getSimpleName();
    public static final int STATUS_ERR = 0;
    public static final int STATUS_SUCCESS = 1;
    public static final String DOWN_DIR = "MM";
    private DownLoadLisenter downLoadLisenter;
    private FileOutputStream fos;
    private InputStream is;
    private Handler handler = new Handler();

    public DownLoadTask(DownLoadLisenter downLoadLisenter) {
        this.downLoadLisenter = downLoadLisenter;
    }

    public interface DownLoadLisenter {
        void onFailed(Exception e);

        void onLoading(int progress, int contentLength);

        void onSuccess();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        downLoadLisenter.onLoading(values[0], values[1]);
    }

    @Override
    protected Void doInBackground(String... url) {
        try {
            URL loadURL = new URL(url[0]);
            HttpURLConnection conn = (HttpURLConnection) loadURL.openConnection();
            conn.setConnectTimeout(10000);
            conn.connect();
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                int contentLength = conn.getContentLength();
                if (contentLength == 0) {
                    failed(new Exception(PrimeApplication.getContext().getString(R.string.down_err_file)));
                    return null;
                }
                Log.i(TAG, "down: size " + contentLength);
                String fileName = loadURL.getFile().substring(1);
                Log.i(TAG, "down: file " + fileName);
                File dir = new File(Environment.getExternalStorageDirectory(), DOWN_DIR);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                fos = new FileOutputStream(new File(dir, fileName));
                int len;
                int downNum = 0;
                byte[] buffer = new byte[1024 * 8];
                is = conn.getInputStream();
                while ((len = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                    downNum += len;
                    publishProgress(downNum, contentLength);
                }
                success();
            }
        } catch (final MalformedURLException e) {
            e.printStackTrace();
            failed(e);
        } catch (IOException e) {
            e.printStackTrace();
            failed(e);
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private void success() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                downLoadLisenter.onSuccess();
            }
        });
    }

    private void failed(final Exception e) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                downLoadLisenter.onFailed(e);
            }
        });
    }
}
