package com.hu.yang.prime.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.hu.yang.prime.R;
import com.hu.yang.prime.download.DownLoadTask;
import com.hu.yang.prime.download.DownloadManager;

/**
 * Created by yanghu on 2017/11/12.
 */

public class DownLoadFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = DownLoadFragment.class.getSimpleName();
    private Button btnDownLoad;
    private String url = "http://10.0.2.2:8080/test.apk";
    private ProgressBar pb;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_download, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnDownLoad = (Button) view.findViewById(R.id.btn_download);
        pb = (ProgressBar) view.findViewById(R.id.pb);
        btnDownLoad.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        DownloadManager downloadManager = DownloadManager.getInstance();
        downloadManager.down(url, new DownLoadTask.DownLoadLisenter() {

            @Override
            public void onFailed(Exception e) {
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLoading(int progress, int contentLength) {
                pb.setMax(contentLength);
                pb.setProgress(progress);
            }

            @Override
            public void onSuccess() {
                Toast.makeText(getActivity(), R.string.down_success, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
