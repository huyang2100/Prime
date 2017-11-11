package com.hu.yang.prime.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.hu.yang.prime.Activity.ToolBarActivity;

/**
 * Created by yanghu on 2017/9/4.
 */

public class ToolBarFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Button btn = new Button(getActivity());
        btn.setText("start activity");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToolBarActivity.actionStart(getActivity());
            }
        });
        return btn;

    }
}
