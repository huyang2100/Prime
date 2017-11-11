package com.hu.yang.prime.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.hu.yang.prime.Activity.SwitchMenuActivity;

/**
 * Created by yanghu on 2017/8/30.
 */

public class SwitchMenuActivityFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Button button = new Button(getActivity());
        button.setText("start activity");
        button.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SwitchMenuActivity.action(getActivity());
            }
        });
        return button;
    }
}
