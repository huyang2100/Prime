package com.hu.yang.prime.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hu.yang.prime.R;
import com.hu.yang.prime.widget.ProgressView;

/**
 * Created by yanghu on 2017/9/1.
 */

public class ProgressFragment extends Fragment {
    private ProgressView progressView;
    private LoadingThread loadingThread;
    private SharedPreferences sp;
    private static final String KEY_PROGRESS = "progress";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_progress, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressView = (ProgressView) view.findViewById(R.id.progressView);
        sp = getActivity().getSharedPreferences(LoadingThread.class.getSimpleName(), Context.MODE_PRIVATE);
        int progress = sp.getInt(KEY_PROGRESS, 0);
        if(progress != 0){
            progressView.setState(ProgressView.ProgressState.PAUSE);
        }
        progressView.setProgress(progress * 1.0 / 100);
        progressView.setOnLoadingStateListener(new ProgressView.OnLoadingStateListener() {
            @Override
            public void onStart() {
                loadingThread = new LoadingThread();
                loadingThread.start();
            }

            @Override
            public void onCompleted() {
                loadingThread.clear();
                Toast.makeText(getActivity(), "completed!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPause() {
                loadingThread.pause();
            }

            @Override
            public void onContinue() {
                loadingThread = new LoadingThread();
                loadingThread.start();
            }
        });

    }

    class LoadingThread extends Thread {
        private final String TAG = LoadingThread.class.getSimpleName();
        private boolean flag = true;
        private int progress;

        public LoadingThread() {
            progress = sp.getInt(KEY_PROGRESS, 0);
        }

        @Override
        public void run() {
            for (int i = progress; i <= 100; i++) {
                if (!flag) {
                    sp.edit().putInt(KEY_PROGRESS, i).commit();
                    break;
                }
                progressView.setProgress(i * 1.0 / 100);
                SystemClock.sleep(100);
            }
        }

        public void pause() {
            flag = false;
        }

        public void clear() {
            sp.edit().clear().commit();
        }
    }
}
