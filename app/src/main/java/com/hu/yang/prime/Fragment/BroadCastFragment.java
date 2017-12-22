package com.hu.yang.prime.Fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hu.yang.prime.Activity.AppLayoutActivity;
import com.hu.yang.prime.R;
import com.hu.yang.prime.util.L;

import java.io.File;
import java.io.IOException;

/**
 * Created by yanghu on 2017/11/27.
 */

public class BroadCastFragment extends Fragment {

    public static final String SEND_ACTION = "com.test";
    public static final String EXTRA_NAME = "name";
    private MyReceiver myReceiver;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_broadcast, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SEND_ACTION);
                intent.putExtra(EXTRA_NAME, "小明");
                getActivity().sendBroadcast(intent);
            }
        });

        File dir = new File(Environment.getExternalStorageDirectory(), "bj");
        if(!dir.exists()){
            dir.mkdirs();
        }
        File file = new File(dir, "my.log");
        if (!file.exists()) {
            try {
                file.createNewFile();
                L.i("file created succ!");
            } catch (IOException e) {
                e.printStackTrace();
                L.i("file created err!");
            }
        }
        L.i("file path " + file.getAbsolutePath());
    }

    @Override
    public void onResume() {
        super.onResume();
        myReceiver = new MyReceiver();
        IntentFilter filter = new IntentFilter(SEND_ACTION);
        getActivity().registerReceiver(myReceiver, filter);
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(myReceiver);
        myReceiver = null;
    }

    public class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                String name = intent.getStringExtra(BroadCastFragment.EXTRA_NAME);
                Toast.makeText(context, name, Toast.LENGTH_SHORT).show();
            }
        }
    }

}
