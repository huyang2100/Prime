package com.hu.yang.prime.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import java.net.URLEncoder;

/**
 * Created by yanghu on 2017/10/27.
 */

public class MyWebViewFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        WebView webView = new WebView(getActivity());
        webView.loadUrl("https://github.com/");
        webView.getSettings().setJavaScriptEnabled(true);
        return webView;
    }
}
