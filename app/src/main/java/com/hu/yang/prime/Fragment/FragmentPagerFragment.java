package com.hu.yang.prime.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.hu.yang.prime.Activity.PagerActivity;

/**
 * Created by yanghu on 2017/10/23.
 */

public class FragmentPagerFragment extends Fragment {
    String[] datas = {"News","Vedio","Music"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Button button = new Button(getActivity());
        button.setText("Go Pager Activity");
        button.setTextSize(24);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PagerActivity.newIntent(getActivity());
            }
        });
        return button;
    }
}
