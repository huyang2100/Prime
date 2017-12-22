package com.hu.yang.prime.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.githang.statusbar.StatusBarCompat;
import com.hu.yang.prime.R;

/**
 * Created by yanghu on 2017/12/22.
 */

public class StatusBarColor2Fragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Button button = new Button(getActivity());
        button.setText("change color");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StatusBarCompat.setStatusBarColor(getActivity(), Color.BLACK);
            }
        });
        return button;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        StatusBarCompat.setStatusBarColor(getActivity(), Color.WHITE);
    }
}
