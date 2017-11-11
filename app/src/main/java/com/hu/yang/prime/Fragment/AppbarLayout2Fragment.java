package com.hu.yang.prime.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.hu.yang.prime.Activity.AppLayoutActivity;

/**
 * Created by yanghu on 2017/11/1.
 */

public class AppbarLayout2Fragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Button btn = new Button(getContext());
        btn.setText("Click into");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppLayoutActivity.actionStart(getActivity());
            }
        });
        return btn;
    }
}
