package com.hu.yang.prime.download;

import android.os.Environment;
import android.util.Log;

import com.hu.yang.prime.application.PrimeApplication;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by yanghu on 2017/11/12.
 */

public class DownloadManager {
    private static final String TAG = DownloadManager.class.getSimpleName();
    private static DownloadManager downloadManager = new DownloadManager();


    private DownloadManager() {

    }

    public static DownloadManager getInstance() {
        return downloadManager;
    }

    DownLoadTask d;

    public void down(String url, DownLoadTask.DownLoadLisenter downLoadLisenter) {
        new DownLoadTask(downLoadLisenter).execute(url);
    }
}
