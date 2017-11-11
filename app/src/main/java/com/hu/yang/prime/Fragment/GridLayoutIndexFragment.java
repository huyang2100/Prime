package com.hu.yang.prime.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hu.yang.prime.R;

/**
 * Created by yanghu on 2017/10/18.
 */

public class GridLayoutIndexFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gridlayout_index,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final GridLayout gridLayout = (GridLayout) view.findViewById(R.id.gridLayout);
        view.findViewById(R.id.tv_eng).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                gridLayout.removeView(v);
            }
        });
    }
}
